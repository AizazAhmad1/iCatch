package com.icatch.wificam.customer.exception;

public class IchPtpInitFailedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchPtpInitFailedException(String reason) {
        super(reason);
    }
}
