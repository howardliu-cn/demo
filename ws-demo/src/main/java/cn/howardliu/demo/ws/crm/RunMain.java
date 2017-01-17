package cn.howardliu.demo.ws.crm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Holder;

/**
 * <br/>created at 16-9-1
 *
 * @author liuxh
 * @since 1.2.0
 */
public class RunMain {
    private static final Logger logger = LoggerFactory.getLogger(RunMain.class);

    public static void main(String[] args) {
        NFrdif service = new NFrdif();
        NFrdifSoap spt = service.getNFrdifSoap();
        spt.datasync("testQueueName", "testData", new Holder<>(false), new Holder<>(""), new Holder<>((short) 0));
    }
}
