package com.icatch.wificam.customer.exception;

public class IchInvalidSessionException extends Exception {
    private static final long serialVersionUID = 1;

    public IchInvalidSessionException(String reason) {
        super(reason);
    }
}
