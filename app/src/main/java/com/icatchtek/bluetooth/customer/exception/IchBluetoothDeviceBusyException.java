package com.icatchtek.bluetooth.customer.exception;

public class IchBluetoothDeviceBusyException extends Exception {
    private static final long serialVersionUID = 1;

    public IchBluetoothDeviceBusyException(String reason) {
        super(reason);
    }
}
