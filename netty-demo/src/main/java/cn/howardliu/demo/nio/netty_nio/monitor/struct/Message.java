package cn.howardliu.demo.nio.netty_nio.monitor.struct;

import java.io.Serializable;

/**
 * <br>created at 17-5-11
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Message implements Serializable {
    private Header header;
    private String body;

    public Header getHeader() {
        return header;
    }

    public Message setHeader(Header header) {
        this.header = header;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
