package com.aprz.component_impl.exception;

public class NotSupportException extends RuntimeException {

    public NotSupportException() {
    }

    public NotSupportException(String message) {
        super(message);
    }

    public NotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportException(Throwable cause) {
        super(cause);
    }

}
