package com.icatch.wificam.customer.exception;

public class IchTimeOutException extends Exception {
    private static final long serialVersionUID = 1;

    public IchTimeOutException(String reason) {
        super(reason);
    }
}
