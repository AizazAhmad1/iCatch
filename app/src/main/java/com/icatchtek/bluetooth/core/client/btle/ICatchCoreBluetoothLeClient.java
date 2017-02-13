package com.icatchtek.bluetooth.core.client.btle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.icatchtek.bluetooth.core.base.BluetoothContext;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.core.base.BluetoothUtils;
import com.icatchtek.bluetooth.core.client.transfer.BluetoothTextTransfer;
import com.icatchtek.bluetooth.core.client.transfer.btle.ICatchCoreBluetoothLeGattTransfer;
import com.icatchtek.bluetooth.core.client.transfer.btle.ICatchCoreBluetoothLeTextTransfer;
import com.icatchtek.bluetooth.core.ctrl.ICatchCoreBluetoothCmdControl;
import com.icatchtek.bluetooth.core.ctrl.ICatchCoreBluetoothSystemControl;
import com.icatchtek.bluetooth.core.ctrl.ICatchCoreBluetoothTelecontroller;
import com.icatchtek.bluetooth.core.event.BluetoothCoreEventManager;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothHostControl;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothSystemControl;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothTelecontroller;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;
import java.util.List;

public class ICatchCoreBluetoothLeClient implements ICatchBluetoothClient {
    private static final String HM10_UUID_CHAR_READ = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String HM10_UUID_CHAR_WRITE = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String ICAT_UUID_CHAR_READ = "e44b82fb-f3a6-4c72-ab3f-bf94abfd9930";
    private static final String ICAT_UUID_CHAR_WRITE = "e44b82fb-f3a6-4c72-ab3f-bf94abfd9930";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String icatch_bluetooth_tag = ICatchCoreBluetoothLeClient.class.getSimpleName();
    private BluetoothGatt bluetoothGatt;
    private int bluetoothState = 0;
    private BluetoothGattCharacteristic characteristic_read;
    private BluetoothGattCharacteristic characteristic_write;
    private BluetoothCoreEventManager eventManager;
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == ICatchCoreBluetoothLeClient.STATE_CONNECTED) {
                ICatchCoreBluetoothLeClient.this.bluetoothState = ICatchCoreBluetoothLeClient.STATE_CONNECTED;
                ICatchCoreBluetoothLeClient.this.bluetoothGatt.discoverServices();
                BluetoothContext.getInstance().putBluetoothGattEvent(33);
                BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "Connected to GATT server.");
                BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "Attempting to start service discovery.");
            } else if (newState == 0) {
                ICatchCoreBluetoothLeClient.this.bluetoothState = 0;
                BluetoothContext.getInstance().putBluetoothGattEvent(34);
                BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "Disconnected from GATT server." + newState);
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                ICatchCoreBluetoothLeClient.this.extractGattServices(gatt.getServices());
                BluetoothContext.getInstance().putBluetoothGattEvent(35);
            } else {
                BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "onServicesDiscovered received: " + status);
                BluetoothContext.getInstance().putBluetoothGattEvent(36);
            }
            BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "onServicesDiscovered, " + status);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            BluetoothContext.getInstance().putBluetoothGattEvent(37, characteristic, status);
            BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "onCharacteristicRead, [" + characteristic.getUuid() + "], " + status);
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            BluetoothContext.getInstance().putBluetoothGattEvent(38, characteristic, status);
            BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "onCharacteristicWrite, [" + characteristic.getUuid() + "], " + status);
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            BluetoothContext.getInstance().putBluetoothGattEvent(39, characteristic, 0);
            BluetoothLogger.getInstance().logI(ICatchCoreBluetoothLeClient.icatch_bluetooth_tag, "onCharacteristicChanged");
        }
    };
    private BluetoothTextTransfer textTransfer;

    public ICatchCoreBluetoothLeClient(Context context, BluetoothCoreEventManager eventManager, BluetoothDevice bluetoothDevice) throws IOException {
        this.bluetoothGatt = bluetoothDevice.connectGatt(context, false, this.gattCallback);
        this.bluetoothState = STATE_CONNECTING;
        this.eventManager = eventManager;
    }

    public void release() throws IOException {
        if (this.textTransfer != null) {
            this.textTransfer.release();
        }
        if (this.bluetoothGatt != null) {
            this.bluetoothGatt.disconnect();
            this.bluetoothGatt.close();
            this.bluetoothGatt = null;
            this.bluetoothState = 0;
        }
    }

    public ICatchBluetoothHostControl getHostControl() throws IOException, IchBluetoothDeviceBusyException {
        if (this.bluetoothState == STATE_CONNECTING) {
            throw new IchBluetoothDeviceBusyException("The connecting had not been done, please wait.");
        } else if (this.textTransfer != null) {
            return new ICatchCoreBluetoothCmdControl(this);
        } else {
            throw new IOException("textTransfer is null");
        }
    }

    public ICatchBluetoothSystemControl getSystemControl() throws IOException, IchBluetoothDeviceBusyException {
        if (this.bluetoothState == STATE_CONNECTING) {
            throw new IchBluetoothDeviceBusyException("The connecting had not been done, please wait.");
        } else if (this.textTransfer != null) {
            return new ICatchCoreBluetoothSystemControl(this);
        } else {
            throw new IOException("textTransfer is null");
        }
    }

    public ICatchBluetoothTelecontroller getTelecontroller() throws IOException, IchBluetoothDeviceBusyException {
        if (this.bluetoothState == STATE_CONNECTING) {
            throw new IchBluetoothDeviceBusyException("The connecting had not been done, please wait.");
        } else if (this.textTransfer != null) {
            return new ICatchCoreBluetoothTelecontroller(this);
        } else {
            throw new IOException("textTransfer is null");
        }
    }

    public void sendRequest(String request, String parameter, long timeout) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        if (this.bluetoothState == STATE_CONNECTING) {
            throw new IchBluetoothDeviceBusyException("The connecting had not been done, please wait.");
        } else if (this.textTransfer == null) {
            throw new IOException("textTransfer is null");
        } else {
            this.textTransfer.sendRequest(request, parameter, timeout);
        }
    }

    public String receiveReply(String request, long timeout) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        if (this.bluetoothState == STATE_CONNECTING) {
            throw new IchBluetoothDeviceBusyException("The connecting had not been done, please wait.");
        } else if (this.textTransfer != null) {
            return this.textTransfer.receiveReply(request, timeout);
        } else {
            throw new IOException("textTransfer is null");
        }
    }

    private void extractGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices != null) {
            for (BluetoothGattService gattService : gattServices) {
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-->service type:" + BluetoothUtils.getServiceType(gattService.getType()));
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-->includedServices size:" + gattService.getIncludedServices().size());
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-->service uuid:" + gattService.getUuid());
                for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
                    BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "---->char uuid:" + gattCharacteristic.getUuid());
                    BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "---->char permission:" + BluetoothUtils.getCharPermission(gattCharacteristic.getPermissions()));
                    int property = gattCharacteristic.getProperties();
                    String uuid = String.valueOf(gattCharacteristic.getUuid());
                    BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "---->char property:" + BluetoothUtils.getCharPropertie(property));
                    if (BluetoothUtils.getCharPropertie(property) == "READ") {
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "read uuid: " + uuid);
                    }
                    if (BluetoothUtils.getCharPropertie(property) == "WRITE") {
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "write uuid: " + uuid);
                    }
                    if (BluetoothUtils.getCharPropertie(property).equals("READ|WRITE_NO_RESPONSE|NOTIFY|")) {
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "read uuid: " + uuid);
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "write uuid: " + uuid);
                    }
                    byte[] data = gattCharacteristic.getValue();
                    if (data != null && data.length > 0) {
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "---->char value:" + new String(data));
                    }
                    if (uuid.equals(HM10_UUID_CHAR_WRITE) || uuid.equals(ICAT_UUID_CHAR_WRITE)) {
                        this.characteristic_read = gattCharacteristic;
                        ICatchCoreBluetoothLeGattTransfer.setCharacteristicNotification(this.bluetoothGatt, gattCharacteristic, true);
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "+++++++++found read characteristic");
                    }
                    if (uuid.equals(HM10_UUID_CHAR_WRITE) || uuid.equals(ICAT_UUID_CHAR_WRITE)) {
                        this.characteristic_write = gattCharacteristic;
                        ICatchCoreBluetoothLeGattTransfer.setCharacteristicNotification(this.bluetoothGatt, gattCharacteristic, true);
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "+++++++++found write characteristic");
                    }
                    for (BluetoothGattDescriptor gattDescriptor : gattCharacteristic.getDescriptors()) {
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-------->desc uuid:" + gattDescriptor.getUuid());
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-------->desc permission:" + BluetoothUtils.getDescPermission(gattDescriptor.getPermissions()));
                        byte[] desData = gattDescriptor.getValue();
                        if (desData != null && desData.length > 0) {
                            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-------->desc value:" + new String(desData));
                        }
                    }
                    if (!(this.characteristic_read == null || this.characteristic_write == null)) {
                        try {
                            this.textTransfer = new ICatchCoreBluetoothLeTextTransfer(this.eventManager, this.bluetoothGatt, this.characteristic_read, this.characteristic_write);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (this.textTransfer != null) {
                BluetoothContext.getInstance().putBluetoothGattEvent(33);
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-------->connected with ble.");
                return;
            }
            try {
                release();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            BluetoothContext.getInstance().putBluetoothGattEvent(34);
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "-------->could not connect with ble.");
        }
    }
}
