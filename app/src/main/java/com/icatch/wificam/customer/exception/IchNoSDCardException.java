package com.icatch.wificam.customer.exception;

public class IchNoSDCardException extends Exception {
    private static final long serialVersionUID = 1;

    public IchNoSDCardException(String reason) {
        super(reason);
    }
}
