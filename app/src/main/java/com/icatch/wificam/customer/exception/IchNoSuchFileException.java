package com.icatch.wificam.customer.exception;

public class IchNoSuchFileException extends Exception {
    private static final long serialVersionUID = 1;

    public IchNoSuchFileException(String reason) {
        super(reason);
    }
}
