package com.icatch.wificam.customer.exception;

public class IchListenerNotExistsException extends Exception {
    private static final long serialVersionUID = 1;

    public IchListenerNotExistsException(String reason) {
        super(reason);
    }
}
