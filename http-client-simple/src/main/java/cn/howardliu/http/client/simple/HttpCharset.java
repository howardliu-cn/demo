package cn.howardliu.http.client.simple;

/**
 * <br/>create at 15-10-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public enum HttpCharset {
    UTF8("UTF-8");

    private HttpCharset(String content) {
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }
}
