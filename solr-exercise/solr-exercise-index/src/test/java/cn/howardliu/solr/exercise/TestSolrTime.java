package cn.howardliu.solr.exercise;

import cn.howardliu.solr.exercise.pojo.DemoModel;
import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;

/**
 * <br/>create at 15-10-27
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test.xml")
public class TestSolrTime {
    @Autowired
    private SolrClient solrClient;

    @Test
    public void test() throws IOException, SolrServerException {
        solrClient.addBeans("solr-exercise-time",
                Lists.newArrayList(
                        new DemoModel("id1", "type1", "name1", "1995-12-31T23:59:59Z", new Date()),
                        new DemoModel("id2", "type2", "name2", "1996-12-31T23:59:59Z", new Date()),
                        new DemoModel("id3", "type3", "name3", "1997-12-31T23:59:59Z", new Date()),
                        new DemoModel("id4", "type4", "name4", "1998-12-31T23:59:59Z", new Date()),
                        new DemoModel("id4", "type4", "name4", "1999-12-31T23:59:59Z", new Date())));
        solrClient.commit("solr-exercise-time");
    }
}
