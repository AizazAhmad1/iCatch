package com.icatchtek.bluetooth.customer;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.icatchtek.bluetooth.core.ICatchCoreBluetoothAdapter;
import com.icatchtek.bluetooth.core.ICatchCoreBluetoothAssist;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.core.base.BluetoothMutex;
import com.icatchtek.bluetooth.core.client.bt.ICatchCoreBluetoothClient;
import com.icatchtek.bluetooth.core.client.btle.ICatchCoreBluetoothLeClient;
import com.icatchtek.bluetooth.core.event.BluetoothCoreEventManager;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothContextInvalidException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothNotSupportedException;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import com.icatchtek.bluetooth.customer.type.ICatchBluetoothDevice;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ICatchBluetoothManager {
    private static ICatchBluetoothManager bluetoothManager;
    private static BluetoothMutex bluetoothMutex = new BluetoothMutex();
    private static final String icatch_bluetooth_tag = ICatchBluetoothManager.class.getSimpleName();
    private Context appContext = null;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothCoreEventManager bluetoothCoreEventManager = null;

    private ICatchBluetoothManager(Context context) throws IchBluetoothNotSupportedException {
        if (context.getPackageManager().hasSystemFeature("android.hardware.bluetooth")) {
            this.appContext = context;
            this.bluetoothAdapter = getDefaultBluetoothAdapter();
            this.bluetoothCoreEventManager = new BluetoothCoreEventManager(this.appContext);
            return;
        }
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "Bluetooth is not supported.");
        throw new IchBluetoothNotSupportedException("Bluetooth is not supported.");
    }

    public void finalize() {
        this.bluetoothCoreEventManager.release();
    }

    public static ICatchBluetoothManager getBluetoothManager(Context appContext) throws IchBluetoothNotSupportedException, IchBluetoothContextInvalidException {
        validateAppContext(appContext);
        bluetoothMutex.lock_1();
        if (bluetoothManager == null) {
            bluetoothManager = new ICatchBluetoothManager(appContext);
        }
        bluetoothMutex.unlock();
        return bluetoothManager;
    }

    public List<ICatchBluetoothDevice> getBondedDevices() {
        List<ICatchBluetoothDevice> icatchDevices = new LinkedList();
        for (BluetoothDevice bluetoothDevice : this.bluetoothAdapter.getBondedDevices()) {
            icatchDevices.add(new ICatchBluetoothDevice(bluetoothDevice.getType(), bluetoothDevice.getName(), bluetoothDevice.getAddress(), true));
        }
        return icatchDevices;
    }

    public ICatchBluetoothAdapter getBluetoothAdapter() {
        return new ICatchCoreBluetoothAdapter(this.appContext);
    }

    public ICatchBluetoothClient getBluetoothClient(Context context, String macAddr, boolean isBLE) throws IOException {
        if (macAddr == null) {
            throw new IOException("invalid mac address.");
        }
        BluetoothDevice bluetoothDevice = ICatchCoreBluetoothAssist.getInstance().getBluetoothDevice(macAddr);
        if (bluetoothDevice == null) {
            bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddr);
        }
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "bt_type: " + bluetoothDevice.getType());
        if (isBLE && (bluetoothDevice.getType() == 3 || bluetoothDevice.getType() == 2)) {
            return new ICatchCoreBluetoothLeClient(context, this.bluetoothCoreEventManager, bluetoothDevice);
        }
        if (isBLE || (bluetoothDevice.getType() != 3 && bluetoothDevice.getType() != 1)) {
            return null;
        }
        return new ICatchCoreBluetoothClient(bluetoothDevice);
    }

    public void registerBroadcastReceiver(ICatchBroadcastReceiver broadcastReceiver, List<String> actionFilter) {
        this.bluetoothCoreEventManager.registerBroadcastReceiver(broadcastReceiver, actionFilter);
    }

    public void unregisterBroadcastReceiver(ICatchBroadcastReceiver broadcastReceiver) {
        this.bluetoothCoreEventManager.unregisterBroadcastReceiver(broadcastReceiver);
    }

    public boolean enableBluetooth() {
        return this.bluetoothAdapter.enable();
    }

    public boolean disableBluetooth() {
        return this.bluetoothAdapter.disable();
    }

    public boolean isBluetoothEnabled() {
        return this.bluetoothAdapter.isEnabled();
    }

    public boolean createBond(String addr) {
        boolean z = false;
        BluetoothDevice bluetoothDevice = ICatchCoreBluetoothAssist.getInstance().getBluetoothDevice(addr);
        if (bluetoothDevice == null) {
            bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addr);
        }
        try {
            Boolean returnValue = (Boolean) bluetoothDevice.getClass().getMethod("createBond", new Class[0]).invoke(bluetoothDevice, new Object[0]);
            if (!returnValue.booleanValue()) {
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "createBond failed.");
            }
            z = returnValue.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "createBond failed.");
        }
        return z;
    }

    public boolean removeBond(String addr) throws IchBluetoothContextInvalidException {
        boolean z = false;
        BluetoothDevice bluetoothDevice = ICatchCoreBluetoothAssist.getInstance().getBluetoothDevice(addr);
        if (bluetoothDevice == null) {
            bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addr);
        }
        try {
            Boolean returnValue = (Boolean) bluetoothDevice.getClass().getMethod("removeBond", new Class[0]).invoke(bluetoothDevice, new Object[0]);
            if (!returnValue.booleanValue()) {
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "removeBond failed.");
            }
            z = returnValue.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "removeBond failed.");
        }
        return z;
    }

    private static BluetoothAdapter getDefaultBluetoothAdapter() throws IchBluetoothNotSupportedException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter;
        }
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "Bluetooth not supported.");
        throw new IchBluetoothNotSupportedException("Bluetooth is not supported.");
    }

    private static void validateAppContext(Context appContext) throws IchBluetoothContextInvalidException {
        if (appContext == null || !(appContext instanceof Application)) {
            throw new IchBluetoothContextInvalidException("Must be an application context, Use getApplicationContext to get one.");
        }
    }
}
