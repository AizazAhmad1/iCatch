package com.icatchtek.bluetooth.core.client.transfer.btle;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import java.util.List;
import java.util.UUID;

public class ICatchCoreBluetoothLeGattTransfer {
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final String TAG = ICatchCoreBluetoothLeGattTransfer.class.getSimpleName();

    public static void readCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            bluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public static void writeCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    public static void setCharacteristicNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (enabled) {
            Log.i(TAG, "Enable Notification");
            bluetoothGatt.setCharacteristicNotification(characteristic, true);
        } else {
            Log.i(TAG, "Disable Notification");
            bluetoothGatt.setCharacteristicNotification(characteristic, false);
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public static List<BluetoothGattService> getSupportedGattServices(BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt == null) {
            return null;
        }
        return bluetoothGatt.getServices();
    }
}
