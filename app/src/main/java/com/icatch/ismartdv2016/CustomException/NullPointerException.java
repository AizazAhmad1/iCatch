package com.icatch.ismartdv2016.CustomException;

public class NullPointerException extends Exception {
    private String exceptionType = "NullPointerException!";

    public NullPointerException(String tag, String describleInfo, String detailInfo) {
        super(describleInfo);
    }
}
