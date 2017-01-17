package cn.howardliu.demo.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>created at 16-8-31
 *
 * @author liuxh
 * @since 1.2.0
 */
public class RunMain {
    private static final Logger logger = LoggerFactory.getLogger(RunMain.class);

    public static void main(String[] args) {
        FujiSyncWsService service = new FujiSyncWsService();
        FujiSyncWsServicePortType spt = service.getFujiSyncWsServiceHttpPort();
        String result = spt.fujiSync("EfutureERP.Consumer", "{\"version\": 1,\"header\": {  \"titile\" : \"消息描述\",  \"reset\":  false,  \"count\": 1 },\"data\":[{\"CODE\":\"7101101034189\",\"HYKLB\":\"J\",\"SEQNO\":6894069,\"HY\":\"Y\",\"NAME\":\"郑永\",\"ADDR\":\"浙江省乐清市\",\"YYZZNO\":\"20160911135451\",\"CLRQ\":\"\",\"FRDB\":\"郑\",\"FRDBSFZ\":\"永\",\"LXR\":\"18361X\",\"LXFS\":\"电话\",\"XYKNAME\":\"1\",\"XYKIDNO\":\"33038219840918361X\",\"SEX\":\"M\",\"BIRTH\":\"1984/09/18 00:00:00\",\"INCOME\":\"J\",\"KHRQ\":\"2016/09/14 14:46:24\",\"SXRQ\":\"\",\"FLZK\":0,\"YE\":0,\"LJJF\":0,\"LJXF\":0,\"LRRY\":\"71050002\",\"SX\":\"鼠\",\"BP\":\"18220886777\",\"ZHGWSJ\":\"\",\"ICKKE\":0,\"ICKKUE\":0,\"FLAG\":\"1\",\"AH\":\"\",\"JNR1\":\"\",\"JNR2\":\"\",\"JNR3\":\"\",\"VIP_KSRQ\":\"2016/09/14 14:46:25\",\"VIP_JSRQ\":\"2017/09/14 00:00:00\",\"HDJF\":0,\"SJJRQ\":\"2017/09/14 00:00:00\",\"MDH\":\"71011\",\"CITY\":\"0609\",\"KMCODE\":\"7101101034189\",\"ISLMK\":\"N\",\"XINGZ\":\"9\",\"ZHXGSJ\":\"2016/09/09 05:15:37\",\"JCJF\":0,\"BY2\":\"N\",\"ACT_CODE\":\"U\",\"ACT_DATE\":\"2016/09/14 14:46:25\",\"ACT_PERSON\":\"SYSTEM\",\"STATUS\":\"N\",\"DID\":7734600}] }");
        System.out.println(result);
    }
}
