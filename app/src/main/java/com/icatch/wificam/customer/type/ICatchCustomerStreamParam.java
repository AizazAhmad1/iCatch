package com.icatch.wificam.customer.type;

public class ICatchCustomerStreamParam implements ICatchStreamParam {
    private String param;
    private int port;

    public ICatchCustomerStreamParam(int port, String param) {
        this.port = port;
        this.param = param;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(":").append(this.port);
        builder.append("/").append(this.param);
        return builder.toString();
    }

    public String getCmdLineParam() {
        return toString();
    }

    public int getVideoWidth() {
        return -1;
    }

    public int getVideoHeight() {
        return -1;
    }
}
