package cn.howardliu.demo.websocket.xml.echo.cache;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * <br/>create at 16-2-2
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("simpleCache")
public class SimpleCache {
    private static final Map<String, WebSocketSession> map = new HashMap<>();

    public WebSocketSession get(String id) {
        return map.get(id);
    }

    public void put(String id, WebSocketSession session) {
        map.put(id, session);
    }
}
