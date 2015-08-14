package cn.howardliu.demo.http;

/**
 * <p>create at 15-7-11</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class HttpRequestException extends Exception {
    public HttpRequestException() {
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

    public HttpRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
