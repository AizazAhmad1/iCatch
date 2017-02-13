package com.icatch.wificam.customer.exception;

public class IchTryAgainException extends Exception {
    private static final long serialVersionUID = 1;

    public IchTryAgainException(String reason) {
        super(reason);
    }
}
