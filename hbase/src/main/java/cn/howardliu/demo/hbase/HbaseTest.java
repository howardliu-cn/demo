package cn.howardliu.demo.hbase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.util.List;

public class HbaseTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-hbase.xml");
        HbaseTemplate hbaseTemplate = (HbaseTemplate) context.getBean("hbaseTemplate");

        List<String> rows = hbaseTemplate.find("mesReq", "message", (result, i) -> {
            return result.toString();
        });

        System.out.println(rows);
    }
}  