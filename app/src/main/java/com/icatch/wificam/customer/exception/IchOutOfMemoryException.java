package com.icatch.wificam.customer.exception;

public class IchOutOfMemoryException extends Exception {
    private static final long serialVersionUID = 1;

    public IchOutOfMemoryException(String reason) {
        super(reason);
    }
}
