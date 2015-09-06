package com.wfj.platform.util.signature.json;

/**
 * 签名JSON校验失败异常
 * <br/>create at 15-7-24
 *
 * @author liufl
 * @since 1.0.0
 */
public class SignedJsonVerifyException extends Exception {
    public SignedJsonVerifyException() {
    }

    public SignedJsonVerifyException(String message) {
        super(message);
    }

    public SignedJsonVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignedJsonVerifyException(Throwable cause) {
        super(cause);
    }

    public SignedJsonVerifyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
