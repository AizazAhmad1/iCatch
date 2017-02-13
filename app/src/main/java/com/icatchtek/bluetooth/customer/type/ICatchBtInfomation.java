package com.icatchtek.bluetooth.customer.type;

public class ICatchBtInfomation {
    private String btPassword;
    private String btSSID;

    public String getBtSSID() {
        return this.btSSID;
    }

    public void setBtSSID(String btSSID) {
        this.btSSID = btSSID;
    }

    public String getBtPassword() {
        return this.btPassword;
    }

    public void setWifiPassword(String btPassword) {
        this.btPassword = btPassword;
    }
}
