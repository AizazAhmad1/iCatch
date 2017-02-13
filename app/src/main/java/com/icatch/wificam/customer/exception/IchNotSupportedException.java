package com.icatch.wificam.customer.exception;

public class IchNotSupportedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchNotSupportedException(String reason) {
        super(reason);
    }
}
