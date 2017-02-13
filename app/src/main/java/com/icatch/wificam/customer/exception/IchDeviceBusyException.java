package com.icatch.wificam.customer.exception;

public class IchDeviceBusyException extends Exception {
    private static final long serialVersionUID = 1;

    public IchDeviceBusyException(String reason) {
        super(reason);
    }
}
