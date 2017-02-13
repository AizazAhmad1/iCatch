package com.icatchtek.bluetooth.core.client.transfer.bt;

import android.bluetooth.BluetoothSocket;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class BluetoothBinaryTransfer {
    private static final String icatch_bluetooth_tag = BluetoothBinaryTransfer.class.getSimpleName();
    private DataInputStream bluetoothInputStream;
    private DataOutputStream bluetoothOutputStream;

    public BluetoothBinaryTransfer(BluetoothSocket bluetoothSocket) throws IOException {
        this.bluetoothInputStream = new DataInputStream(bluetoothSocket.getInputStream());
        this.bluetoothOutputStream = new DataOutputStream(bluetoothSocket.getOutputStream());
    }

    public void release() throws IOException {
    }

    public int readData(byte[] buffer, int bufferSize, int timeout) throws IOException, IchBluetoothTimeoutException {
        int receivedSize = this.bluetoothInputStream.read(buffer, 0, bufferSize);
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "Read " + receivedSize + " bytes raw data now.");
        return receivedSize;
    }

    public void writeData(byte[] buffer, int dataSize) throws IOException {
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "write data to remote device.");
        this.bluetoothOutputStream.write(buffer, 0, dataSize);
        this.bluetoothOutputStream.flush();
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "successfully written data to remote device.");
    }
}
