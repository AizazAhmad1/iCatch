package com.icatch.ismartdv2016.BaseItems;

public class BluetoothAppDevice {
    private String bluetoothAddr;
    private boolean bluetoothConnect = false;
    private boolean bluetoothExist = false;
    private String bluetoothName;

    public BluetoothAppDevice(String name, String addr, boolean bluetoothConnect) {
        this.bluetoothName = name;
        this.bluetoothAddr = addr;
        this.bluetoothConnect = bluetoothConnect;
    }

    public BluetoothAppDevice(String name, String addr, boolean bluetoothConnect, boolean bluetoothExist) {
        this.bluetoothName = name;
        this.bluetoothAddr = addr;
        this.bluetoothConnect = bluetoothConnect;
        this.bluetoothExist = bluetoothExist;
    }

    public String getBluetoothName() {
        return this.bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAddr() {
        return this.bluetoothAddr;
    }

    public void setBluetoothAddr(String bluetoothAddr) {
        this.bluetoothAddr = bluetoothAddr;
    }

    public boolean getBluetoothConnect() {
        return this.bluetoothConnect;
    }

    public void setBluetoothConnect(boolean bluetoothConnect) {
        this.bluetoothConnect = bluetoothConnect;
    }

    public boolean getBluetoothExist() {
        return this.bluetoothExist;
    }

    public void setBluetoothExist(boolean bluetoothExist) {
        this.bluetoothExist = bluetoothExist;
    }
}
