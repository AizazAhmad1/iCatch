package com.icatchtek.bluetooth.customer.type;

public class ICatchWifiInformation {
    private ICatchWifiEncType wifiEncType;
    private String wifiPassword;
    private String wifiSSID;

    public String getWifiSSID() {
        return this.wifiSSID;
    }

    public void setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
    }

    public String getWifiPassword() {
        return this.wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public ICatchWifiEncType getWifiEncType() {
        return this.wifiEncType;
    }

    public void setWifiEncType(ICatchWifiEncType wifiEncType) {
        this.wifiEncType = wifiEncType;
    }
}
