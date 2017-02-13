package com.icatch.wificam.customer.exception;

public class IchPauseFailedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchPauseFailedException(String reason) {
        super(reason);
    }
}
