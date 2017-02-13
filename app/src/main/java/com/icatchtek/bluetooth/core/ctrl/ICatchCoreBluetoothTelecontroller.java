package com.icatchtek.bluetooth.core.ctrl;

import com.icatchtek.bluetooth.core.ICatchCoreBluetoothCommand;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothTelecontroller;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;

public class ICatchCoreBluetoothTelecontroller implements ICatchBluetoothTelecontroller {
    private ICatchBluetoothClient bluetoothClient;

    public ICatchCoreBluetoothTelecontroller(ICatchBluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    public boolean openMenu() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_MENU, null);
    }

    public boolean moveLeft() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_LEFT, null);
    }

    public boolean moveRight() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_RIGHT, null);
    }

    public boolean moveUp() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_UP, null);
    }

    public boolean moveDown() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_DOWN, null);
    }

    public boolean execute() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_SET, null);
    }

    public boolean btKeyDel() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_DEL, null);
    }

    public boolean changeMode() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_MODE, null);
    }

    public boolean zoomIn() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_WIDE, null);
    }

    public boolean zoomOut() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_TELE, null);
    }

    public boolean captureHalfPress() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_CAPTURE_HALF_PRESS, null);
    }

    public boolean capture() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_EVENT_KEY_CAPTURE, null);
    }
}
