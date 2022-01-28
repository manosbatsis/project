package com.topideal.order.exception;

public class NCException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NCException() {
    }

    public NCException(String message) {
        super(message);
    }

    public NCException(String message, Throwable cause) {
        super(message, cause);
    }

    public NCException(Throwable cause) {
        super(cause);
    }

    public NCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
