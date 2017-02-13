package com.icatch.wificam.customer.exception;

public class IchBufferTooSmallException extends Exception {
    private static final long serialVersionUID = 1;

    public IchBufferTooSmallException(String reason) {
        super(reason);
    }
}
