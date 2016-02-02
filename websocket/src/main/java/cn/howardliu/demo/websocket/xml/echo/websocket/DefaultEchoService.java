package cn.howardliu.demo.websocket.xml.echo.websocket;

import org.springframework.stereotype.Component;

/**
 * <br/>create at 16-2-2
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component
public class DefaultEchoService implements EchoService {

    @Override
    public String getMessage(String message) {
        String echoFormat = "Did you say '%s'?";
        return String.format(echoFormat, message);
    }
}
