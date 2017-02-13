package com.icatchtek.bluetooth.customer.client;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;

public interface ICatchBluetoothHostControl {
    String hostCommand(String str, String str2) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;
}
