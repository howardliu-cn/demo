package cn.howardliu.demo.websocket.xml.echo.controller;

import cn.howardliu.demo.websocket.xml.echo.websocket.EchoWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * <br/>create at 16-2-2
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
public class AnotherMessageController {
    @Autowired
    private EchoWebSocketHandler echoWebSocketHandler;

    @RequestMapping("sendMsg")
    @ResponseBody
    public Map<String, Object> message(String id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        WebSocketSession session = echoWebSocketHandler.getSimpleCache().get(id);
        echoWebSocketHandler.handleTextMessage(session, new TextMessage("new msg:" + System.currentTimeMillis()));
        msg.put("success", true);
        return msg;
    }
}
