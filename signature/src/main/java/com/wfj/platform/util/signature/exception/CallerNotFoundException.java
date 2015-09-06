package com.wfj.platform.util.signature.exception;

/**
 * <br/>create at 15-7-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CallerNotFoundException extends Exception {
    private final static String msg = "Caused by Caller not found!";

    public CallerNotFoundException() {
        super(msg);
    }

    public CallerNotFoundException(String message) {
        super(msg + "\n" + message);
    }

    public CallerNotFoundException(String message, Throwable cause) {
        super(msg + "\n" + message, cause);
    }

    public CallerNotFoundException(Throwable cause) {
        super(msg, cause);
    }

    protected CallerNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(msg + "\n" + message, cause, enableSuppression, writableStackTrace);
    }
}
