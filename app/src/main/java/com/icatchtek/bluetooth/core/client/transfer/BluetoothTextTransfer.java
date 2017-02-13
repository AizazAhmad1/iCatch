package com.icatchtek.bluetooth.core.client.transfer;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;

public interface BluetoothTextTransfer {
    String receiveReply(String str, long j) throws IOException, IchBluetoothTimeoutException;

    void release() throws IOException;

    void sendRequest(String str, String str2, long j) throws IOException, IchBluetoothTimeoutException;
}
