package com.icatch.wificam.customer.exception;

public class IchListenerExistsException extends Exception {
    private static final long serialVersionUID = 1;

    public IchListenerExistsException(String reason) {
        super(reason);
    }
}
