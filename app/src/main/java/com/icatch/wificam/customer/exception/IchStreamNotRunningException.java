package com.icatch.wificam.customer.exception;

public class IchStreamNotRunningException extends Exception {
    private static final long serialVersionUID = 1;

    public IchStreamNotRunningException(String reason) {
        super(reason);
    }
}
