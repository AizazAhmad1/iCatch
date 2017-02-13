package com.icatchtek.bluetooth.core;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.ICatchBluetoothAdapter;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.listener.ICatchBTDeviceDetectedListener;
import com.icatchtek.bluetooth.customer.type.ICatchBluetoothDevice;
import java.util.ArrayList;
import java.util.List;

public class ICatchCoreBluetoothAdapter implements ICatchBluetoothAdapter {
    private static final String icatch_bluetooth_tag = ICatchCoreBluetoothAdapter.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter = null;
    private Context bluetoothContext = null;
    private BluetoothLeScanner bluetoothLeScanner = null;
    private ICatchBTDeviceDetectedListener bluetoothListener = null;
    private List<ScanFilter> bluetoothScanFilters = null;
    private ScanSettings bluetoothScanSettings = null;
    private boolean discovering = false;
    private boolean discoveringBLE = false;
    private ScanCallbackA leScanCallbackA = null;
    private ScanCallbackB leScanCallbackB = null;
    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.device.action.FOUND".equals(intent.getAction())) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                ICatchBluetoothDevice bluetoothDevice = new ICatchBluetoothDevice(device.getType(), device.getName(), device.getAddress(), device.getBondState() == 12);
                ICatchCoreBluetoothAdapter.this.bluetoothListener.deviceDetected(bluetoothDevice);
                ICatchCoreBluetoothAssist.getInstance().putCachedBluetoothDevice(device);
                BluetoothLogger.getInstance().logI(ICatchCoreBluetoothAdapter.icatch_bluetooth_tag, "Bluetooth device founded, namme [" + bluetoothDevice.getName() + "], " + "address [" + bluetoothDevice.getAddress() + "].");
            }
        }
    };

    @TargetApi(21)
    private class ScanCallbackA extends ScanCallback {
        private ScanCallbackA() {
        }

        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            ICatchBluetoothDevice bluetoothDevice = new ICatchBluetoothDevice(device.getType(), device.getName(), device.getAddress(), device.getBondState() == 12);
            bluetoothDevice.setRssi(result.getRssi());
            bluetoothDevice.setScanRecord(result.getScanRecord().getBytes());
            ICatchCoreBluetoothAdapter.this.bluetoothListener.deviceDetected(bluetoothDevice);
            ICatchCoreBluetoothAssist.getInstance().putCachedBluetoothDevice(device);
            BluetoothLogger.getInstance().logI(ICatchCoreBluetoothAdapter.icatch_bluetooth_tag, "Bluetooth device founded, namme [" + bluetoothDevice.getName() + "], " + "address [" + bluetoothDevice.getAddress() + "].");
        }
    }

    private class ScanCallbackB implements LeScanCallback {
        private ScanCallbackB() {
        }

        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            ICatchBluetoothDevice bluetoothDevice = new ICatchBluetoothDevice(device.getType(), device.getName(), device.getAddress(), device.getBondState() == 12);
            bluetoothDevice.setRssi(rssi);
            bluetoothDevice.setScanRecord(scanRecord);
            ICatchCoreBluetoothAdapter.this.bluetoothListener.deviceDetected(bluetoothDevice);
            ICatchCoreBluetoothAssist.getInstance().putCachedBluetoothDevice(device);
            BluetoothLogger.getInstance().logI(ICatchCoreBluetoothAdapter.icatch_bluetooth_tag, "Bluetooth device founded, namme [" + bluetoothDevice.getName() + "], " + "address [" + bluetoothDevice.getAddress() + "].");
        }
    }

    public ICatchCoreBluetoothAdapter(Context bluetoothContext) {
        this.bluetoothContext = bluetoothContext;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (VERSION.SDK_INT >= 21) {
            Builder scanSettingsBuilder = new Builder();
            scanSettingsBuilder.setScanMode(2);
            this.bluetoothScanSettings = scanSettingsBuilder.build();
            this.bluetoothScanFilters = new ArrayList();
            this.bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
            this.leScanCallbackA = new ScanCallbackA();
        }
        this.leScanCallbackB = new ScanCallbackB();
    }

    public boolean startDiscovery(ICatchBTDeviceDetectedListener listener, boolean forBLE) throws IchBluetoothDeviceBusyException {
        if (this.discovering) {
            throw new IchBluetoothDeviceBusyException();
        }
        this.bluetoothListener = listener;
        this.discoveringBLE = forBLE;
        if (this.discoveringBLE) {
            startLeDiscovery();
        } else {
            this.bluetoothContext.registerReceiver(this.scanReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
            if (!this.bluetoothAdapter.startDiscovery()) {
                return false;
            }
        }
        this.discovering = true;
        return true;
    }

    public void stopDiscovery() {
        if (this.discoveringBLE) {
            stopLeDiscovery();
        } else {
            this.bluetoothAdapter.cancelDiscovery();
            this.bluetoothContext.unregisterReceiver(this.scanReceiver);
        }
        this.discovering = false;
    }

    private boolean startLeDiscovery() {
        if (VERSION.SDK_INT < 21) {
            return this.bluetoothAdapter.startLeScan(this.leScanCallbackB);
        }
        if (this.bluetoothLeScanner == null) {
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "Bluetooth scanner could not be getter.");
            return false;
        }
        this.bluetoothLeScanner.startScan(this.bluetoothScanFilters, this.bluetoothScanSettings, this.leScanCallbackA);
        return true;
    }

    private boolean stopLeDiscovery() {
        if (VERSION.SDK_INT < 21) {
            this.bluetoothAdapter.stopLeScan(this.leScanCallbackB);
        } else if (this.bluetoothLeScanner == null) {
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "Bluetooth scanner could not be getter.");
            return false;
        } else {
            this.bluetoothLeScanner.stopScan(this.leScanCallbackA);
        }
        return true;
    }
}
