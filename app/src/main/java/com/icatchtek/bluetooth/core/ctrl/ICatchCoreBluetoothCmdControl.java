package com.icatchtek.bluetooth.core.ctrl;

import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothHostControl;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;

public class ICatchCoreBluetoothCmdControl implements ICatchBluetoothHostControl {
    private ICatchBluetoothClient bluetoothClient;

    public ICatchCoreBluetoothCmdControl(ICatchBluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    public String hostCommand(String command, String parameter) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        this.bluetoothClient.sendRequest(command, parameter, 5000);
        return this.bluetoothClient.receiveReply(command, 5000);
    }
}
