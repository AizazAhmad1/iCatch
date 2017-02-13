package com.icatchtek.bluetooth.customer;

import android.content.Context;
import android.content.Intent;
import com.icatch.wificam.customer.type.ICatchCaptureDelay;
import com.icatchtek.bluetooth.core.base.BluetoothCondition;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothContextInvalidException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceDisabledException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothNotBondedException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothNotSupportedException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import com.icatchtek.bluetooth.customer.listener.ICatchBTDeviceDetectedListener;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiverID;
import com.icatchtek.bluetooth.customer.type.ICatchBluetoothDevice;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ICatchBluetoothAssist {
    private static ICatchBluetoothAssist bluetoothAssist = new ICatchBluetoothAssist();
    private static final String icatch_bluetooth_tag = ICatchBluetoothAssist.class.getSimpleName();
    private BluetoothCondition bluetoothCondition = new BluetoothCondition();
    private ICatchBluetoothDevice bluetoothDevice;
    private List<ICatchBluetoothDevice> bluetoothDevices;
    private ICatchBluetoothManager bluetoothManager;
    private ICatchBroadcastReceiver userBroadcastReceiver = new ICatchBroadcastReceiver() {
        public void onReceive(Intent intent) {
            boolean z = true;
            String action = intent.getAction();
            BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "ICatchBroadcastReceiver, action: " + action);
            if (action.equals(ICatchBroadcastReceiverID.BT_ACTION_ADAPTER_STATE_CHANGED)) {
                int adapterState = intent.getIntExtra(ICatchBroadcastReceiverID.BT_ADAPTER_STATE, 18);
                if (adapterState == 17 || adapterState == 18) {
                    ICatchBluetoothAssist.this.bluetoothCondition.signal();
                }
                BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "ICatchBroadcastReceiver, adapterState: " + adapterState);
            }
            if (action.equals(ICatchBroadcastReceiverID.BT_ACTION_BOND_STATE_CHANGED)) {
                int bondState = intent.getIntExtra(ICatchBroadcastReceiverID.BT_BOND_STATE, 1);
                if (bondState == 3 || bondState == 1) {
                    ICatchBluetoothDevice access$500 = ICatchBluetoothAssist.this.bluetoothDevice;
                    if (bondState != 3) {
                        z = false;
                    }
                    access$500.setBonded(z);
                    ICatchBluetoothAssist.this.bluetoothCondition.signal();
                }
                BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "ICatchBroadcastReceiver, bondState: " + bondState);
            }
        }
    };

    private class BTDeviceDetectedListener implements ICatchBTDeviceDetectedListener {
        private BTDeviceDetectedListener() {
        }

        public void deviceDetected(ICatchBluetoothDevice bluetoothDevice) {
            ICatchBluetoothAssist.this.bluetoothDevices.add(bluetoothDevice);
            BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "device [" + bluetoothDevice.getName() + "|" + bluetoothDevice.getAddress() + "] added.");
        }
    }

    private class CreateBTBoundThread extends Thread {
        private CreateBTBoundThread() {
        }

        public void run() {
            BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "CreateBTBound request send.");
            if (!(ICatchBluetoothAssist.this.bluetoothManager == null || ICatchBluetoothAssist.this.bluetoothDevice == null)) {
                ICatchBluetoothAssist.this.bluetoothManager.createBond(ICatchBluetoothAssist.this.bluetoothDevice.getAddress());
            }
            BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "CreateBTBound request send done.");
        }
    }

    private class EnableBTAdapterThread extends Thread {
        private EnableBTAdapterThread() {
        }

        public void run() {
            BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "EnableBTAdapter request send.");
            if (ICatchBluetoothAssist.this.bluetoothManager != null) {
                ICatchBluetoothAssist.this.bluetoothManager.enableBluetooth();
            }
            BluetoothLogger.getInstance().logI(ICatchBluetoothAssist.icatch_bluetooth_tag, "EnableBTAdapter request send done.");
        }
    }

    private ICatchBluetoothAssist() {
    }

    public static ICatchBluetoothAssist getBluetoothAssist() {
        return bluetoothAssist;
    }

    public ICatchBluetoothClient getBluetoothClient(Context appContext, boolean isBLE) throws IchBluetoothNotSupportedException, IchBluetoothContextInvalidException, IchBluetoothDeviceDisabledException, IOException, IchBluetoothTimeoutException, IchBluetoothNotBondedException {
        ICatchBluetoothClient iCatchBluetoothClient = null;
        try {
            this.bluetoothManager = ICatchBluetoothManager.getBluetoothManager(appContext);
            List<String> actionFilter = new LinkedList();
            actionFilter.add(ICatchBroadcastReceiverID.BT_ACTION_BOND_STATE_CHANGED);
            actionFilter.add(ICatchBroadcastReceiverID.BT_ACTION_ADAPTER_STATE_CHANGED);
            this.bluetoothManager.registerBroadcastReceiver(this.userBroadcastReceiver, actionFilter);
            if (!this.bluetoothManager.isBluetoothEnabled()) {
                new EnableBTAdapterThread().start();
                this.bluetoothCondition.await(ICatchCaptureDelay.ICH_CAP_DELAY_10S);
            }
            if (this.bluetoothManager.isBluetoothEnabled()) {
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "bluetooth device enabled");
                this.bluetoothDevices = this.bluetoothManager.getBondedDevices();
                if (this.bluetoothDevices == null || this.bluetoothDevices.isEmpty()) {
                    discoveryBluetoothDevices();
                }
                if (this.bluetoothDevices != null && !this.bluetoothDevices.isEmpty()) {
                    BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "found bluetooth devices");
                    this.bluetoothDevice = chooseBluetoothDevice();
                    if (this.bluetoothDevice != null) {
                        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "found specified device");
                        if (!this.bluetoothDevice.isBonded()) {
                            new CreateBTBoundThread().start();
                            this.bluetoothCondition.await(30000);
                        }
                        if (this.bluetoothDevice.isBonded()) {
                            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "specified device is in specified state.");
                            iCatchBluetoothClient = this.bluetoothManager.getBluetoothClient(appContext, this.bluetoothDevice.getAddress(), isBLE);
                            if (this.bluetoothManager != null) {
                                this.bluetoothManager.unregisterBroadcastReceiver(this.userBroadcastReceiver);
                            }
                        } else {
                            throw new IchBluetoothNotBondedException("Bluetooth is not bounded.");
                        }
                    } else if (this.bluetoothManager != null) {
                        this.bluetoothManager.unregisterBroadcastReceiver(this.userBroadcastReceiver);
                    }
                } else if (this.bluetoothManager != null) {
                    this.bluetoothManager.unregisterBroadcastReceiver(this.userBroadcastReceiver);
                }
                return iCatchBluetoothClient;
            }
            throw new IchBluetoothDeviceDisabledException("Bluetooth is not enabled.");
        } catch (Throwable th) {
            if (this.bluetoothManager != null) {
                this.bluetoothManager.unregisterBroadcastReceiver(this.userBroadcastReceiver);
            }
        }
    }

    private void discoveryBluetoothDevices() {
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "get adapter");
        ICatchBluetoothAdapter adapter = this.bluetoothManager.getBluetoothAdapter();
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "start discovery");
        try {
            adapter.startDiscovery(new BTDeviceDetectedListener(), false);
        } catch (IchBluetoothDeviceBusyException ex) {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.stopDiscovery();
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "stop discovery");
    }

    private ICatchBluetoothDevice chooseBluetoothDevice() {
        for (ICatchBluetoothDevice bluetoothDevice : this.bluetoothDevices) {
            if (verifyBluetoothDevice(bluetoothDevice)) {
                return bluetoothDevice;
            }
        }
        return null;
    }

    private boolean verifyBluetoothDevice(ICatchBluetoothDevice bluetoothDevice) {
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "verify device: " + bluetoothDevice.getName() + " with " + bluetoothDevice.getAddress());
        if (bluetoothDevice.getName().equals("iCatchBT")) {
            return true;
        }
        return false;
    }
}
