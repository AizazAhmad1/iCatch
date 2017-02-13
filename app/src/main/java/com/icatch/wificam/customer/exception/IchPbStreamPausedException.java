package com.icatch.wificam.customer.exception;

public class IchPbStreamPausedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchPbStreamPausedException(String reason) {
        super(reason);
    }
}
