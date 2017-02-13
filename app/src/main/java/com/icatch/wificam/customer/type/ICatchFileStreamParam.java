package com.icatch.wificam.customer.type;

public class ICatchFileStreamParam implements ICatchStreamParam {
    private String fileName;

    public ICatchFileStreamParam(String fileName) {
        this.fileName = fileName;
    }

    public String toString() {
        return "file://" + this.fileName;
    }

    public String getCmdLineParam() {
        return toString();
    }

    public int getVideoWidth() {
        return 0;
    }

    public int getVideoHeight() {
        return 0;
    }
}
