package cn.howardliu.demo.websocket.xml;

import cn.howardliu.demo.websocket.xml.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * <br/>create at 16-2-2
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("echoWebSocketHandler")
public class EchoWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private EchoService echoService;
    @Autowired
    private SimpleCache simpleCache;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (simpleCache.get(session.getId()) == null) {
            simpleCache.put(session.getId(), session);
        }
        String reply = this.echoService.getMessage(message.getPayload());
        session.sendMessage(new TextMessage(reply));
    }

    public EchoService getEchoService() {
        return echoService;
    }

    public SimpleCache getSimpleCache() {
        return simpleCache;
    }
}
