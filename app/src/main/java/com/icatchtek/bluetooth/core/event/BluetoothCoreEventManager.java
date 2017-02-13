package com.icatchtek.bluetooth.core.event;

import android.content.Context;
import com.icatchtek.bluetooth.core.base.BluetoothMutex;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothCoreEventManager {
    private BluetoothMutex bluetoothMutex = new BluetoothMutex();
    private BluetoothBroadcastHandler broadcastHandler = new BluetoothBroadcastHandler(this);
    private Map<ICatchBroadcastReceiver, List<String>> broadcastReceiverMap = new HashMap();
    private BluetoothSystemBroadcastHandler systemBroadcastHandler;

    public BluetoothCoreEventManager(Context context) {
        this.systemBroadcastHandler = new BluetoothSystemBroadcastHandler(context, this);
    }

    public void release() {
        this.broadcastHandler.release();
        this.systemBroadcastHandler.release();
    }

    public void registerBroadcastReceiver(ICatchBroadcastReceiver broadcastReceiver, List<String> actionFilter) {
        lock_map_status();
        this.broadcastReceiverMap.put(broadcastReceiver, actionFilter);
        unlock_map_status();
    }

    public void unregisterBroadcastReceiver(ICatchBroadcastReceiver broadcastReceiver) {
        lock_map_status();
        this.broadcastReceiverMap.remove(broadcastReceiver);
        unlock_map_status();
    }

    public Map<ICatchBroadcastReceiver, List<String>> __get_locked_broadcast_receivers_map() {
        lock_map_status();
        return this.broadcastReceiverMap;
    }

    public void __put_broadcast_receivers_map(Map<ICatchBroadcastReceiver, List<String>> broadcastReceiverMap) {
        if (broadcastReceiverMap == this.broadcastReceiverMap) {
            unlock_map_status();
        }
    }

    private void lock_map_status() {
        this.bluetoothMutex.lock_1();
    }

    private void unlock_map_status() {
        this.bluetoothMutex.unlock();
    }
}
