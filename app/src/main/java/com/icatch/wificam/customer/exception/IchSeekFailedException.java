package com.icatch.wificam.customer.exception;

public class IchSeekFailedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchSeekFailedException(String reason) {
        super(reason);
    }
}
