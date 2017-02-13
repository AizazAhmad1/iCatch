package com.icatch.wificam.customer.exception;

public class IchResumeFailedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchResumeFailedException(String reason) {
        super(reason);
    }
}
