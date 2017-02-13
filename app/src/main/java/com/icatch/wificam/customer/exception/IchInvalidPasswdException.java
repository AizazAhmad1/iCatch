package com.icatch.wificam.customer.exception;

public class IchInvalidPasswdException extends Exception {
    private static final long serialVersionUID = 1;

    public IchInvalidPasswdException(String reason) {
        super(reason);
    }
}
