package cn.howardliu.demo.websocket.xml;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 功能说明：websocket连接的拦截器
 * 有两种方式
 * 一种是实现接口HandshakeInterceptor，实现beforeHandshake和afterHandshake函数
 * 一种是继承HttpSessionHandshakeInterceptor，重载beforeHandshake和afterHandshake函数
 */
public class TelWebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * 从请求中获取唯一标记参数，填充到数据传递容器attributes
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (getSession(serverHttpRequest) != null) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
            HttpServletRequest request = servletRequest.getServletRequest();
            attributes.put("inquiryId", request.getParameter("inquiryId"));
            attributes.put("empNo", request.getParameter("empNo"));
        }
        super.beforeHandshake(serverHttpRequest, serverHttpResponse, wsHandler, attributes);
        return true;
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }
}
