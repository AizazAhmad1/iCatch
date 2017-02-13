package com.icatchtek.bluetooth.customer;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.listener.ICatchBTDeviceDetectedListener;

public interface ICatchBluetoothAdapter {
    boolean startDiscovery(ICatchBTDeviceDetectedListener iCatchBTDeviceDetectedListener, boolean z) throws IchBluetoothDeviceBusyException;

    void stopDiscovery();
}
