package cn.howardliu.demo.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;

import java.util.ArrayList;
import java.util.List;

public class HbaseTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-hbase.xml");
        HbaseTemplate hbaseTemplate = (HbaseTemplate) context.getBean("hbaseTemplate");

//        List<String> rows = hbaseTemplate.find("mesReq", "message", (result, i) -> {
//            return result.toString();
//        });
//
//        System.out.println(rows);

        Scan scan = new Scan();
        scan.setCaching(100);
        scan.setStartRow(" ".getBytes());
        scan.setStopRow("209999999999999999999999999999".getBytes());
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^\\d004002\\d+")));
        filterList.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(),
                CompareFilter.CompareOp.GREATER_OR_EQUAL, "2015-02-23 12:10:30".getBytes()));
        filterList.addFilter(new SingleColumnValueFilter("message".getBytes(), "create_date".getBytes(),
                CompareFilter.CompareOp.LESS_OR_EQUAL, "2017-03-23 12:10:30".getBytes()));
        filterList.addFilter(new SingleColumnValueFilter("message".getBytes(), "process_status".getBytes(),
                CompareFilter.CompareOp.EQUAL, "0".getBytes()));
        scan.setFilter(filterList);

        List<String> list = hbaseTemplate.find("mesReq", scan,
                new ResultsExtractor<List<String>>() {
                    List<String> list = new ArrayList<>();

                    @Override
                    public List<String> extractData(ResultScanner result) throws Exception {
                        for (Result re : result) {
                            Cell cell = re.getColumnLatestCell("message".getBytes(), "mes_value".getBytes());
                            list.add(new String(cell.getValueArray()));
                        }
                        return list;
                    }
                });
        System.out.println(list);
    }
}