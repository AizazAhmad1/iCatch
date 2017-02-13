package com.icatchtek.bluetooth.customer.client;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;

public interface ICatchBluetoothClient {
    ICatchBluetoothHostControl getHostControl() throws IOException, IchBluetoothDeviceBusyException;

    ICatchBluetoothSystemControl getSystemControl() throws IOException, IchBluetoothDeviceBusyException;

    ICatchBluetoothTelecontroller getTelecontroller() throws IOException, IchBluetoothDeviceBusyException;

    String receiveReply(String str, long j) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    void release() throws IOException;

    void sendRequest(String str, String str2, long j) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;
}
