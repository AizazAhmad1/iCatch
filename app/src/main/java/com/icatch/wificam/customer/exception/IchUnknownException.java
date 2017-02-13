package com.icatch.wificam.customer.exception;

public class IchUnknownException extends Exception {
    private static final long serialVersionUID = 1;

    public IchUnknownException(String reason) {
        super(reason);
    }
}
