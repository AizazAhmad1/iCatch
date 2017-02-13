package com.icatch.wificam.customer.exception;

public class IchPermissionDeniedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchPermissionDeniedException(String reason) {
        super(reason);
    }
}
