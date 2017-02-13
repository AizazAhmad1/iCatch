package com.icatchtek.bluetooth.core.event;

import android.content.Intent;
import android.content.IntentFilter;
import com.icatchtek.bluetooth.core.base.BluetoothContext;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiverID;
import java.util.List;
import java.util.Map;

public class BluetoothBroadcastHandler {
    private static final String icatch_bluetooth_tag = BluetoothBroadcastHandler.class.getSimpleName();
    private BluetoothBroadcastReceiver broadcastReceiver = new BluetoothBroadcastReceiver() {
        public void onReceive(BluetoothContext context, Intent intent) {
            String action = intent.getAction();
            BluetoothLogger.getInstance().logI(BluetoothBroadcastHandler.icatch_bluetooth_tag, "broadcastReceiver, received action: " + action);
            Map<ICatchBroadcastReceiver, List<String>> broadcastReceiverMap = BluetoothBroadcastHandler.this.eventManager.__get_locked_broadcast_receivers_map();
            for (ICatchBroadcastReceiver receiver : broadcastReceiverMap.keySet()) {
                if (((List) broadcastReceiverMap.get(receiver)).contains(action)) {
                    BluetoothLogger.getInstance().logI(BluetoothBroadcastHandler.icatch_bluetooth_tag, action + " set by receiver: " + receiver);
                    receiver.onReceive(intent);
                }
            }
            BluetoothBroadcastHandler.this.eventManager.__put_broadcast_receivers_map(broadcastReceiverMap);
        }
    };
    private BluetoothCoreEventManager eventManager;

    public BluetoothBroadcastHandler(BluetoothCoreEventManager coreEventManager) {
        this.eventManager = coreEventManager;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_DATA_AVAILABLE);
        intentFilter.addAction(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_SERVICE_DISCOVERY_STATE_CHANGED);
        BluetoothContext.getInstance().registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public void release() {
        BluetoothContext.getInstance().unregisterReceiver(this.broadcastReceiver);
    }
}
