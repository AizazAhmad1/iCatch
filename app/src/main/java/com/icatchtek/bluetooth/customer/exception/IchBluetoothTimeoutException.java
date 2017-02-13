package com.icatchtek.bluetooth.customer.exception;

public class IchBluetoothTimeoutException extends Exception {
    private static final long serialVersionUID = 1;

    public IchBluetoothTimeoutException(String reason) {
        super(reason);
    }
}
