package com.icatch.ismartdv2016.CustomException;

public class DataFormatException extends Exception {
    public DataFormatException(String tag, String message) {
        super(message);
    }
}
