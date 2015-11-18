package cn.howardliu.miner.basic;

import cn.howardliu.miner.CharacterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * <br/>create at 15-11-11
 *
 * @author liuxh
 * @since 1.0.0
 */
public class NewWordTest {
    private static final Logger logger = LoggerFactory.getLogger(NewWordTest.class);
    private int d = 5;
    private int frequency = 10;
    private int num = 2;// 2
    //    String fileName = "A Brief History Of Time.txt";
//    String resultFileName = "out1.txt";
//    String resultFileName = "out1-no-one-character-word.txt";
    String fileName = "xiyouji.txt";
    //    String resultFileName = "out2.txt";
    String resultFileName = "out2-no-one-character-word.txt";

    public void test() throws IOException {
        long total = 0;
        Map<String, CandidateWord> map = new HashMap<>();
        List<String> strings = new ArrayList<>();
        InputStream in = new FileInputStream(
                this.getClass().getResource("/file").getPath() + File.separator + fileName);
        try (BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in))) {
            String tempString;
            while ((tempString = bufferReader.readLine()) != null) {
                tempString = CharacterUtils.sbc2Dbc(tempString.trim());
                if (StringUtils.isBlank(tempString)) {
                    continue;
                }
                String[] array = tempString.split("[\\p{P}\\p{S}[a-z0-9A-Z]\\s+\\n\\t]");
                for (String s : array) {
                    if (StringUtils.isBlank(s)) {
                        continue;
                    }
                    strings.add(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String str : strings) {
            for (int i = 0; i < str.length(); i++) {
                for (int j = 0; j < d && j < str.length() - i; j++) {
                    String s = str.substring(i, i + j + 1);
                    total++;
                    CandidateWord word = map.get(s);
                    if (word == null) {
                        word = new CandidateWord();
                        word.frequency = 1L;
                        word.word = s;
                        map.put(s, word);
                    } else {
                        word.frequency += 1L;
                    }
                    String preS = str.substring(0, i);
                    if (StringUtils.isBlank(preS)) {
                        continue;
                    }
                    Map<String, Long> preMap = word.preMap;
                    for (int k = preS.length(); k > 0 && k > preS.length() - d; k--) {
                        String p = preS.substring(k - 1);
                        Long pl = preMap.get(p);
                        if (pl == null) {
                            pl = 1L;
                            preMap.put(p, pl);
                        } else {
                            preMap.put(p, pl + 1);
                        }
                        word.preSum += 1L;
                    }
                    String postS = str.substring(i + j + 1);
                    Map<String, Long> postMap = word.postMap;
                    for (int k = 0; k < postS.length() && k < d + 1; k++) {
                        String p = postS.substring(0, k + 1);
                        Long pl = postMap.get(p);
                        if (pl == null) {
                            pl = 1L;
                            postMap.put(p, pl);
                        } else {
                            postMap.put(p, pl + 1);
                        }
                        word.postSum += 1L;
                    }
                }
            }
        }
        Collection<CandidateWord> words = map.values();
        List<CandidateWord> list = new ArrayList<>(words.size());
        words.forEach(p -> {
            if (p.word.length() < num) {
                return;
            }
            if (p.frequency < frequency) {
                return;
            }
            Map<String, Long> preM = p.preMap;
            double preEntropy = 0;
            for (Map.Entry<String, Long> e : preM.entrySet()) {
                double ratio = e.getValue() / (double) p.preSum;
                preEntropy -= Math.log(ratio) * ratio;
            }
            p.preEntropy = preEntropy;
            Map<String, Long> postM = p.postMap;
            double postEntropy = 0;
            for (Map.Entry<String, Long> e : postM.entrySet()) {
                double ratio = e.getValue() / (double) p.postSum;
                postEntropy -= Math.log(ratio) * ratio;
            }
            p.postEntropy = postEntropy;
            list.add(p);
        });
        Collections.sort(list);
        logger.debug("排序后数据：");
        String filePath = this.getClass().getResource("/file").getPath() + File.separator + resultFileName;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        list.forEach(w -> {
            logger.debug("候选词：[{}]，出现频数：[{}]，左邻字信息熵：[{}]，右邻字信息熵：[{}]",
                    w.word, w.frequency, w.preEntropy, w.postEntropy);
            try {
                writer.write("候选词：[" + w.word + "]，出现频数：[" + w.frequency + "]，" +
                        "左邻字信息熵：[" + w.preEntropy + "]，右邻字信息熵：[" + w.postEntropy + "]");
                writer.newLine();
            } catch (IOException e) {
                logger.error("写数据出错", e);
            }
        });
        writer.flush();
        writer.close();

        logger.debug("总候选词数：{}", total);
    }
}
