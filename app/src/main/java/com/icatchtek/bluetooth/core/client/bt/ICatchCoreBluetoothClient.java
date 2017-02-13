package com.icatchtek.bluetooth.core.client.bt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.core.client.transfer.bt.ICatchCoreBluetoothTextTransfer;
import com.icatchtek.bluetooth.core.ctrl.ICatchCoreBluetoothCmdControl;
import com.icatchtek.bluetooth.core.ctrl.ICatchCoreBluetoothSystemControl;
import com.icatchtek.bluetooth.core.ctrl.ICatchCoreBluetoothTelecontroller;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothHostControl;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothSystemControl;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothTelecontroller;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;
import java.util.UUID;

public class ICatchCoreBluetoothClient implements ICatchBluetoothClient {
    private static final UUID SPP_RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String icatch_bluetooth_tag = ICatchCoreBluetoothClient.class.getSimpleName();
    private BluetoothSocket bluetoothSocket;
    private ICatchCoreBluetoothTextTransfer textTransfer;

    public ICatchCoreBluetoothClient(BluetoothDevice bluetoothDevice) throws IOException {
        init(bluetoothDevice);
    }

    public void release() throws IOException {
        if (this.textTransfer != null) {
            this.textTransfer.release();
        }
        if (this.bluetoothSocket != null) {
            this.bluetoothSocket.close();
        }
    }

    public ICatchBluetoothHostControl getHostControl() throws IOException {
        if (this.textTransfer != null) {
            return new ICatchCoreBluetoothCmdControl(this);
        }
        throw new IOException("textTransfer is null");
    }

    public ICatchBluetoothSystemControl getSystemControl() throws IOException {
        if (this.textTransfer != null) {
            return new ICatchCoreBluetoothSystemControl(this);
        }
        throw new IOException("textTransfer is null");
    }

    public ICatchBluetoothTelecontroller getTelecontroller() throws IOException {
        if (this.textTransfer != null) {
            return new ICatchCoreBluetoothTelecontroller(this);
        }
        throw new IOException("textTransfer is null");
    }

    public void sendRequest(String request, String parameter, long timeout) throws IOException, IchBluetoothTimeoutException {
        if (this.textTransfer == null) {
            throw new IOException("textTransfer is null");
        }
        this.textTransfer.sendRequest(request, parameter, timeout);
    }

    public String receiveReply(String request, long timeout) throws IOException, IchBluetoothTimeoutException {
        if (this.textTransfer != null) {
            return this.textTransfer.receiveReply(request, timeout);
        }
        throw new IOException("textTransfer is null");
    }

    private void init(BluetoothDevice bluetoothDevice) throws IOException {
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "create socket for " + bluetoothDevice);
        this.bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SPP_RFCOMM_UUID);
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "connectting to " + bluetoothDevice);
        try {
            this.bluetoothSocket.connect();
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "connected to " + bluetoothDevice);
        } catch (IOException e) {
            try {
                BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "reconnecting to " + bluetoothDevice);
                this.bluetoothSocket = (BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE}).invoke(bluetoothDevice, new Object[]{Integer.valueOf(1)});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            this.bluetoothSocket.connect();
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "connected to " + bluetoothDevice);
        }
        this.textTransfer = new ICatchCoreBluetoothTextTransfer(this.bluetoothSocket);
    }
}
