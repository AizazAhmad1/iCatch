package com.icatchtek.bluetooth.customer.exception;

public class IchBluetoothDeviceDisabledException extends Exception {
    private static final long serialVersionUID = 1;

    public IchBluetoothDeviceDisabledException(String reason) {
        super(reason);
    }
}
