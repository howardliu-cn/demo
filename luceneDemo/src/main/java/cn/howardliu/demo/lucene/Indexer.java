package cn.howardliu.demo.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * <br/>create at 15-9-6
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Indexer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IndexWriter indexWriter;
    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir).toPath());
    }
}
