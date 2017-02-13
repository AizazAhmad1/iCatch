package com.icatch.wificam.customer.exception;

public class IchInvalidArgumentException extends Exception {
    private static final long serialVersionUID = 1;

    public IchInvalidArgumentException(String reason) {
        super(reason);
    }
}
