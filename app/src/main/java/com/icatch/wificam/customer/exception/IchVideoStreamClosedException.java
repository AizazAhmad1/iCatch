package com.icatch.wificam.customer.exception;

public class IchVideoStreamClosedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchVideoStreamClosedException(String reason) {
        super(reason);
    }
}
