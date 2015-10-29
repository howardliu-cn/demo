/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.howardliu.lucene.extension;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.CharsRefBuilder;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.rest.BaseSolrResource;
import org.apache.solr.rest.ManagedResource;
import org.apache.solr.rest.ManagedResourceStorage.StorageIO;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

/**
 * TokenFilterFactory and ManagedResource implementation for
 * doing CRUD on synonyms using the REST API.
 */
public class ManagedSynonymFilterFactory extends BaseManagedTokenFilterFactory {
    public static final Logger log = LoggerFactory.getLogger(ManagedSynonymFilterFactory.class);

    public static final String SYNONYM_MAPPINGS = "synonymMappings";
    public static final String IGNORE_CASE_INIT_ARG = "ignoreCase";

    /**
     * Used internally to preserve the case of synonym mappings regardless
     * of the ignoreCase setting.
     */
    private static class CasePreservedSynonymMappings {
        Map<String, Set<String>> mappings = new TreeMap<>();

        /**
         * Provides a view of the mappings for a given term; specifically, if
         * ignoreCase is true, then the returned "view" contains the mappings
         * for all known cases of the term, if it is false, then only the
         * mappings for the specific case is returned.
         */
        Set<String> getMappings(boolean ignoreCase, String key) {
            Set<String> synMappings;
            if (ignoreCase) {
                // TODO: should we return the mapped values in all lower-case here?
                if (mappings.size() == 1) {
                    // if only one in the map (which is common) just return it directly
                    return mappings.values().iterator().next();
                }
                synMappings = new TreeSet<>();
                mappings.values().forEach(synMappings::addAll);
            } else {
                synMappings = mappings.get(key);
            }
            return synMappings;
        }

        public String toString() {
            return mappings.toString();
        }
    }

    /**
     * ManagedResource implementation for synonyms, which are so specialized that
     * it makes sense to implement this class as an inner class as it has little
     * application outside the SynonymFilterFactory use cases.
     */
    public static class SynonymManager extends ManagedResource
            implements ManagedResource.ChildResourceSupport {
        protected Map<String, CasePreservedSynonymMappings> synonymMappings;

        public SynonymManager(String resourceId, SolrResourceLoader loader, StorageIO storageIO)
                throws SolrException {
            super(resourceId, loader, storageIO);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onManagedDataLoadedFromStorage(NamedList<?> managedInitArgs, Object managedData)
                throws SolrException {
            NamedList<Object> initArgs = (NamedList<Object>) managedInitArgs;
            String format = (String) initArgs.get("format");
            if (format != null && !"solr".equals(format)) {
                throw new SolrException(ErrorCode.BAD_REQUEST, "Invalid format " +
                        format + "! Only 'solr' is supported.");
            }

            // the default behavior is to not ignore case,
            // so if not supplied, then install the default
            if (initArgs.get(IGNORE_CASE_INIT_ARG) == null) {
                initArgs.add(IGNORE_CASE_INIT_ARG, Boolean.FALSE);
            }

            boolean ignoreCase = getIgnoreCase(managedInitArgs);
            synonymMappings = new TreeMap<>();
            if (managedData != null) {
                Map<String, Object> storedSyns = (Map<String, Object>) managedData;
                for (String key : storedSyns.keySet()) {

                    String caseKey = applyCaseSetting(ignoreCase, key);
                    CasePreservedSynonymMappings cpsm = synonymMappings.get(caseKey);
                    if (cpsm == null) {
                        cpsm = new CasePreservedSynonymMappings();
                        synonymMappings.put(caseKey, cpsm);
                    }

                    // give the nature of our JSON parsing solution, we really have
                    // no guarantees on what is in the file
                    Object mapping = storedSyns.get(key);
                    if (!(mapping instanceof List)) {
                        throw new SolrException(ErrorCode.SERVER_ERROR,
                                "Invalid synonym file format! Expected a list of synonyms for " + key +
                                        " but got " + mapping.getClass().getName());
                    }

                    Set<String> sortedVals = new TreeSet<>();
                    sortedVals.addAll((List<String>) storedSyns.get(key));
                    cpsm.mappings.put(key, sortedVals);
                }
            }
            log.info("Loaded {} synonym mappings for {}", synonymMappings.size(), getResourceId());
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Object applyUpdatesToManagedData(Object updates) {
            boolean ignoreCase = getIgnoreCase();
            boolean madeChanges;
            if (updates instanceof List) {
                madeChanges = applyListUpdates((List<String>) updates, ignoreCase);
            } else if (updates instanceof Map) {
                madeChanges = applyMapUpdates((Map<String, Object>) updates, ignoreCase);
            } else {
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                        "Unsupported data format (" + updates.getClass()
                                .getName() + "); expected a JSON object (Map or List)!");
            }
            return madeChanges ? getStoredView() : null;
        }

        protected boolean applyListUpdates(List<String> jsonList, boolean ignoreCase) {
            boolean madeChanges = false;
            for (String term : jsonList) {
                // find the mappings using the case aware key
                String origTerm = term;
                term = applyCaseSetting(ignoreCase, term);
                CasePreservedSynonymMappings cpsm = synonymMappings.get(term);
                if (cpsm == null) {
                    cpsm = new CasePreservedSynonymMappings();
                }

                Set<String> treeTerms = new TreeSet<>();
                treeTerms.addAll(jsonList);
                treeTerms.remove(origTerm);
                cpsm.mappings.put(origTerm, treeTerms);
                madeChanges = true;
                // only add the cpsm to the synonymMappings if it has valid data
                if (!synonymMappings.containsKey(term) && cpsm.mappings.get(origTerm) != null) {
                    synonymMappings.put(term, cpsm);
                }
            }
            return madeChanges;
        }

        protected boolean applyMapUpdates(Map<String, Object> jsonMap, boolean ignoreCase) {
            boolean madeChanges = false;

            for (String term : jsonMap.keySet()) {
                String origTerm = term;
                term = applyCaseSetting(ignoreCase, term);

                // find the mappings using the case aware key
                CasePreservedSynonymMappings cpsm = synonymMappings.get(term);
                if (cpsm == null) {
                    cpsm = new CasePreservedSynonymMappings();
                }

                Set<String> output = cpsm.mappings.get(origTerm);

                Object val = jsonMap.get(origTerm); // IMPORTANT: use the original
                if (val instanceof String) {
                    String strVal = (String) val;
                    if (output == null) {
                        output = new TreeSet<>();
                        cpsm.mappings.put(origTerm, output);
                    }
                    if (output.add(strVal)) {
                        madeChanges = true;
                    }
                } else if (val instanceof List) {
                    List<String> vals = (List<String>) val;

                    if (output == null) {
                        output = new TreeSet<>();
                        cpsm.mappings.put(origTerm, output);
                    }

                    for (String nextVal : vals) {
                        if (output.add(nextVal)) {
                            madeChanges = true;
                        }
                    }
                } else {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unsupported value " + val +
                            " for " + term + "; expected single value or a JSON array!");
                }
                // only add the cpsm to the synonymMappings if it has valid data
                if (!synonymMappings.containsKey(term) && cpsm.mappings.get(origTerm) != null) {
                    synonymMappings.put(term, cpsm);
                }
            }

            return madeChanges;
        }

        /**
         * Returns a Map of how we store and load data managed by this resource,
         * which is different than how it is managed at runtime in order to support
         * the ignoreCase setting.
         */
        protected Map<String, Set<String>> getStoredView() {
            Map<String, Set<String>> storedView = new TreeMap<>();
            for (CasePreservedSynonymMappings cpsm : synonymMappings.values()) {
                for (String key : cpsm.mappings.keySet()) {
                    storedView.put(key, cpsm.mappings.get(key));
                }
            }
            return storedView;
        }

        protected String applyCaseSetting(boolean ignoreCase, String str) {
            return (ignoreCase && str != null) ? str.toLowerCase(Locale.ROOT) : str;
        }

        public boolean getIgnoreCase() {
            return getIgnoreCase(managedInitArgs);
        }

        public boolean getIgnoreCase(NamedList<?> initArgs) {
            Boolean ignoreCase = initArgs.getBooleanArg(IGNORE_CASE_INIT_ARG);
            // ignoreCase = false by default
            return null == ignoreCase ? false : ignoreCase;
        }

        @Override
        public void doGet(BaseSolrResource endpoint, String childId) {
            SolrQueryResponse response = endpoint.getSolrResponse();
            if (childId != null) {
                boolean ignoreCase = getIgnoreCase();
                String key = applyCaseSetting(ignoreCase, childId);

                // if ignoreCase==true, then we get the mappings using the lower-cased key
                // and then return a union of all case-sensitive keys, if false, then
                // we only return the mappings for the exact case requested
                CasePreservedSynonymMappings cpsm = synonymMappings.get(key);
                Set<String> mappings = (cpsm != null) ? cpsm.getMappings(ignoreCase, childId) : null;
                if (mappings == null) {
                    throw new SolrException(ErrorCode.NOT_FOUND,
                            String.format(Locale.ROOT, "%s not found in %s", childId, getResourceId()));
                }

                response.add(childId, mappings);
            } else {
                response.add(SYNONYM_MAPPINGS, buildMapToStore(getStoredView()));
            }
        }

        @Override
        public synchronized void doDeleteChild(BaseSolrResource endpoint, String childId) {
            boolean ignoreCase = getIgnoreCase();
            String key = applyCaseSetting(ignoreCase, childId);

            CasePreservedSynonymMappings cpsm = synonymMappings.get(key);
            if (cpsm == null) {
                throw new SolrException(ErrorCode.NOT_FOUND,
                        String.format(Locale.ROOT, "%s not found in %s", childId, getResourceId()));
            }

            if (ignoreCase) {
                // delete all mappings regardless of case
                synonymMappings.remove(key);
            } else {
                // just delete the mappings for the specific case-sensitive key
                if (cpsm.mappings.containsKey(childId)) {
                    cpsm.mappings.remove(childId);

                    if (cpsm.mappings.isEmpty()) {
                        synonymMappings.remove(key);
                    }
                } else {
                    throw new SolrException(ErrorCode.NOT_FOUND,
                            String.format(Locale.ROOT, "%s not found in %s", childId, getResourceId()));
                }
            }

            // store the updated data (using the stored view)
            storeManagedData(getStoredView());

            log.info("Removed synonym mappings for: {}", childId);
        }
    }

    /**
     * Custom SynonymMap.Parser implementation that provides synonym
     * mappings from the managed JSON in this class during SynonymMap
     * building.
     */
    private class ManagedSynonymParser extends SynonymMap.Parser {
        SynonymManager synonymManager;

        public ManagedSynonymParser(SynonymManager synonymManager, boolean dedup, Analyzer analyzer) {
            super(dedup, analyzer);
            this.synonymManager = synonymManager;
        }

        /**
         * Add the managed synonyms and their mappings into the SynonymMap builder.
         */
        @Override
        public void parse(Reader in) throws IOException, ParseException {
            // 添加对同义词文件的解析 add by liuxh
            if (in != null) {
                LineNumberReader br = new LineNumberReader(in);
                try {
                    addInternal(br);
                } catch (IllegalArgumentException e) {
                    ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber(), 0);
                    ex.initCause(e);
                    throw ex;
                } finally {
                    br.close();
                }
            }
            boolean ignoreCase = synonymManager.getIgnoreCase();
            log.info("\n\n\nthis synonym mappings in storage is {}\n\n\n", synonymManager.synonymMappings);
            for (CasePreservedSynonymMappings cpsm : synonymManager.synonymMappings.values()) {
                for (String term : cpsm.mappings.keySet()) {
                    for (String mapping : cpsm.mappings.get(term)) {
                        // apply the case setting to match the behavior of the SynonymMap builder
                        String casedTerm = synonymManager.applyCaseSetting(ignoreCase, term);
                        String casedMapping = synonymManager.applyCaseSetting(ignoreCase, mapping);
                        log.info("load from storage, input is {}, output is {}", casedTerm, casedMapping);
                        add(new CharsRef(casedTerm), new CharsRef(casedMapping), false);
                    }
                }
            }
        }

        private void addInternal(BufferedReader in) throws IOException {
            String line;
            Map<String, List<String>> fileMappings = new TreeMap<>();
            while ((line = in.readLine()) != null) {
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue; // ignore empty lines and comments
                }
                String sides[] = split(line, "=>");
                List<String> values = new ArrayList<>();
                if (sides.length > 1) { // explicit mapping
                    // 有明确方向的同义词定义，=>符号前的作为key，符号后的作为value队列。
                    if (sides.length != 2) {
                        throw new IllegalArgumentException("more than one explicit mapping specified on the same line");
                    }
                    String inputStrings[] = split(sides[0], ",");
                    for (String inputString : inputStrings) {
                        String key = analyze(unescape(inputString).trim(), new CharsRefBuilder()).toString();
                        if (fileMappings.containsKey(key)) {
                            values.addAll(fileMappings.get(key));
                        }
                        fileMappings.put(key, values);
                    }
                    String outputStrings[] = split(sides[1], ",");
                    for (String outputString : outputStrings) {
                        values.add(analyze(unescape(outputString).trim(), new CharsRefBuilder()).toString());
                    }
                } else {
                    String inputStrings[] = split(line, ",");
                    for (String inputString : inputStrings) {
                        values.add(analyze(unescape(inputString).trim(), new CharsRefBuilder()).toString());
                    }
                    for (String key : values) {
                        if (fileMappings.containsKey(key)) {
                            values.addAll(fileMappings.get(key));
                        }
                        fileMappings.put(key, values);
                    }
                }
            }
            Object updated = synonymManager.applyUpdatesToManagedData(fileMappings);
            if (updated != null) {
                log.info("\n\n\nload mappings from file to storage, the synonym mappings is {}\n\n\n", fileMappings);
                synonymManager.storeManagedData(updated);
            }
        }

        private String[] split(String s, String separator) {
            ArrayList<String> list = new ArrayList<>(2);
            StringBuilder sb = new StringBuilder();
            int pos = 0, end = s.length();
            while (pos < end) {
                if (s.startsWith(separator, pos)) {
                    if (sb.length() > 0) {
                        list.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    pos += separator.length();
                    continue;
                }
                char ch = s.charAt(pos++);
                if (ch == '\\') {
                    sb.append(ch);
                    if (pos >= end) {
                        break;  // ERROR, or let it go?
                    }
                    ch = s.charAt(pos++);
                }
                sb.append(ch);
            }
            if (sb.length() > 0) {
                list.add(sb.toString());
            }
            return list.toArray(new String[list.size()]);
        }

        private String unescape(String s) {
            if (s.contains("\\")) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    char ch = s.charAt(i);
                    if (ch == '\\' && i < s.length() - 1) {
                        sb.append(s.charAt(++i));
                    } else {
                        sb.append(ch);
                    }
                }
                return sb.toString();
            }
            return s;
        }
    }

    protected SynonymFilterFactory delegate;
    private final String synonymFile;// 同义词文件定义

    public ManagedSynonymFilterFactory(Map<String, String> args) {
        super(args);
        this.synonymFile = get(args, "synonymFile");
    }

    @Override
    public String getResourceId() {
        return "/schema/analysis/synonyms/" + handle;
    }

    protected Class<? extends ManagedResource> getManagedResourceImplClass() {
        return SynonymManager.class;
    }

    /**
     * Called once, during core initialization, to initialize any analysis components
     * that depend on the data managed by this resource. It is important that the
     * analysis component is only initialized once during core initialization so that
     * text analysis is consistent, especially in a distributed environment, as we
     * don't want one server applying a different set of stop words than other servers.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onManagedResourceInitialized(NamedList<?> initArgs, final ManagedResource res)
            throws SolrException {
        NamedList<Object> args = (NamedList<Object>) initArgs;
        args.add("synonyms", getResourceId());
        args.add("expand", "false");
        args.add("format", "solr");

        Map<String, String> filtArgs = new HashMap<>();
        for (Map.Entry<String, ?> entry : args) {
            filtArgs.put(entry.getKey(), entry.getValue().toString());
        }
        // create the actual filter factory that pulls the synonym mappings
        // from synonymMappings using a custom parser implementation
        delegate = new SynonymFilterFactory(filtArgs) {
            @Override
            protected SynonymMap loadSynonyms(ResourceLoader loader, String cname, boolean dedup, Analyzer analyzer)
                    throws IOException, ParseException {
                CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);
                ManagedSynonymParser parser = new ManagedSynonymParser((SynonymManager) res, dedup, analyzer);
                // 加入同义词文件流
                InputStreamReader in = null;
                if (StringUtils.isNotBlank(synonymFile)) {
                    in = new InputStreamReader(loader.openResource(synonymFile), decoder);
                }
                parser.parse(in);
                return parser.build();
            }
        };
        try {
            delegate.inform(res.getResourceLoader());
        } catch (IOException e) {
            throw new SolrException(ErrorCode.SERVER_ERROR, e);
        }
    }

    @Override
    public TokenStream create(TokenStream input) {
        if (delegate == null) {
            throw new IllegalStateException(this.getClass().getName() +
                    " not initialized correctly! The SynonymFilterFactory delegate was not initialized.");
        }

        return delegate.create(input);
    }
}
