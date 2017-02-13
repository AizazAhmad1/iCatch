package com.icatch.wificam.customer.exception;

public class IchAudioStreamClosedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchAudioStreamClosedException(String reason) {
        super(reason);
    }
}
