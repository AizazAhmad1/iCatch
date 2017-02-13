package com.icatch.wificam.customer.type;

public class ICatchH264StreamParam implements ICatchStreamParam {
    public String toString() {
        return "IMPL=SDK;CODEC=H264;";
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
