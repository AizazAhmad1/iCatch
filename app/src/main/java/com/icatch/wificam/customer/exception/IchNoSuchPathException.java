package com.icatch.wificam.customer.exception;

public class IchNoSuchPathException extends Exception {
    private static final long serialVersionUID = 1;

    public IchNoSuchPathException(String reason) {
        super(reason);
    }
}
