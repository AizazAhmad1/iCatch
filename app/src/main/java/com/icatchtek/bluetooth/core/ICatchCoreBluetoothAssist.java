package com.icatchtek.bluetooth.core;

import android.bluetooth.BluetoothDevice;
import com.slidingmenu.lib.R;
import java.util.HashMap;
import java.util.Map;

public class ICatchCoreBluetoothAssist {
    private static ICatchCoreBluetoothAssist instance = new ICatchCoreBluetoothAssist();
    private Map<String, BluetoothDevice> bluetoothDevices = new HashMap();

    private ICatchCoreBluetoothAssist() {
    }

    public static ICatchCoreBluetoothAssist getInstance() {
        return instance;
    }

    protected void putCachedBluetoothDevice(BluetoothDevice device) {
        this.bluetoothDevices.put(device.getAddress(), device);
    }

    public BluetoothDevice getBluetoothDevice(String addr) {
        return (BluetoothDevice) this.bluetoothDevices.get(addr);
    }

    public static int toICatchBondState(int bondState) {
        switch (bondState) {
            case R.styleable.SlidingMenu_fadeEnabled /*10*/:
                return 1;
            case R.styleable.SlidingMenu_fadeDegree /*11*/:
                return 2;
            case R.styleable.SlidingMenu_selectorEnabled /*12*/:
                return 3;
            default:
                return -1;
        }
    }

    public static int toICatchAdapterState(int adapterState) {
        switch (adapterState) {
            case R.styleable.SlidingMenu_fadeEnabled /*10*/:
                return 18;
            case R.styleable.SlidingMenu_fadeDegree /*11*/:
                return 19;
            case R.styleable.SlidingMenu_selectorEnabled /*12*/:
                return 17;
            case R.styleable.SlidingMenu_selectorDrawable /*13*/:
                return 20;
            default:
                return -1;
        }
    }
}
