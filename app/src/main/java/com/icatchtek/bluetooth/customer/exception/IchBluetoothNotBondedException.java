package com.icatchtek.bluetooth.customer.exception;

public class IchBluetoothNotBondedException extends Exception {
    private static final long serialVersionUID = 1;

    public IchBluetoothNotBondedException(String reason) {
        super(reason);
    }
}
