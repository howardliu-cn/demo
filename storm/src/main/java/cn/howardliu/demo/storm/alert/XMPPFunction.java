package cn.howardliu.demo.storm.alert;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

import java.util.Map;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
public class XMPPFunction extends BaseFunction {
    private static final Logger logger = LoggerFactory.getLogger(XMPPFunction.class);

    public static final String XMPP_TO = "storm.xmpp.to";
    public static final String XMPP_USER = "storm.xmpp.user";
    public static final String XMPP_PASSWORD = "storm.xmpp.password";
    public static final String XMPP_SERVER = "storm.xmpp.server";

    private XMPPConnection xmppConnection;
    private String to;
    private MessageMapper mapper;

    public XMPPFunction(MessageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void prepare(Map conf, TridentOperationContext context) {
        logger.debug("Prepare: {}", conf);
//        super.prepare(conf, context);
//        this.to = (String) conf.get(XMPP_TO);
//        ConnectionConfiguration config = new ConnectionConfiguration((String) conf.get(XMPP_SERVER));
//        this.xmppConnection = new XMPPConnection(config);
//        try {
//            this.xmppConnection.connect();
//            this.xmppConnection.login((String) conf.get(XMPP_USER), (String) conf.get(XMPP_PASSWORD));
//        } catch (XMPPException e) {
//            logger.warn("Error initializing XMPP Channel", e);
//        }
    }

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
//        Message msg = new Message(this.to, Message.Type.normal);
//        msg.setBody(this.mapper.toMessageBody(tuple));
//        this.xmppConnection.sendPacket(msg);
        System.out.println(this.mapper.toMessageBody(tuple));
    }
}
