package com.icatchtek.bluetooth.customer.type;

public class ICatchBluetoothDevice {
    public static final int DEVICE_TYPE_CLASSIC = 1;
    public static final int DEVICE_TYPE_DUAL = 3;
    public static final int DEVICE_TYPE_LE = 2;
    public static final int DEVICE_TYPE_UNKNOWN = 0;
    private String address;
    private boolean bonded;
    private String name;
    private int rssi;
    private byte[] scanRecord;
    private int type;

    public ICatchBluetoothDevice(int type, String name, String address, boolean bonded) {
        this.type = type;
        this.name = name;
        this.address = address;
        setBonded(bonded);
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isBonded() {
        return this.bonded;
    }

    public void setBonded(boolean bonded) {
        this.bonded = bonded;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getScanRecord() {
        return this.scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }
}
