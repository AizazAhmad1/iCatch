package com.icatchtek.bluetooth.customer.exception;

public class IchBluetoothNotSupportedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchBluetoothNotSupportedException(String reason) {
        super(reason);
    }
}
