package com.icatch.wificam.customer.exception;

public class IchNotImplementedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchNotImplementedException(String reason) {
        super(reason);
    }
}
