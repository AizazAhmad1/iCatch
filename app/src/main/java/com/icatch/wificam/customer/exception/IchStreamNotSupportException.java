package com.icatch.wificam.customer.exception;

public class IchStreamNotSupportException extends Exception {
    private static final long serialVersionUID = 1;

    public IchStreamNotSupportException(String reason) {
        super(reason);
    }
}
