package com.icatchtek.bluetooth.core.event;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.icatchtek.bluetooth.core.ICatchCoreBluetoothAssist;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiverID;
import java.util.List;
import java.util.Map;

public class BluetoothSystemBroadcastHandler {
    private static final String icatch_bluetooth_tag = BluetoothSystemBroadcastHandler.class.getSimpleName();
    private BluetoothEventHandler bondStateChangedHandler = new BluetoothEventHandler() {
        public void onReceive(Context context, Intent intent, BluetoothDevice device) {
            if (device == null) {
                BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "ACTION_BOND_STATE_CHANGED with no EXTRA_DEVICE");
                return;
            }
            int bondState = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
            int icatchBondState = ICatchCoreBluetoothAssist.toICatchBondState(bondState);
            if (icatchBondState == -1) {
                BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "not defined bond state " + bondState);
                return;
            }
            Map<ICatchBroadcastReceiver, List<String>> broadcastReceiverMap = BluetoothSystemBroadcastHandler.this.eventManager.__get_locked_broadcast_receivers_map();
            for (ICatchBroadcastReceiver receiver : broadcastReceiverMap.keySet()) {
                if (((List) broadcastReceiverMap.get(receiver)).contains(ICatchBroadcastReceiverID.BT_ACTION_BOND_STATE_CHANGED)) {
                    Intent userIntent = new Intent();
                    userIntent.setAction(ICatchBroadcastReceiverID.BT_ACTION_BOND_STATE_CHANGED);
                    userIntent.putExtra(ICatchBroadcastReceiverID.BT_BOND_STATE, icatchBondState);
                    userIntent.putExtra(ICatchBroadcastReceiverID.BT_ADAPTER_ADDRESS, device.getAddress());
                    receiver.onReceive(userIntent);
                    BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "Bond state1: " + bondState);
                    BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "Bond state2: " + device.getBondState());
                } else {
                    BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "com.icatchtek.bluetooth.__icatch_bt_action_bond_state_changed not set by receiver: " + receiver);
                }
            }
            BluetoothSystemBroadcastHandler.this.eventManager.__put_broadcast_receivers_map(broadcastReceiverMap);
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "broadcastReceiver, received action: " + action);
            if (action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                BluetoothSystemBroadcastHandler.this.bondStateChangedHandler.onReceive(context, intent, device);
            }
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                BluetoothSystemBroadcastHandler.this.powerStateChangedHandler.onReceive(context, intent, device);
            }
        }
    };
    private Context context;
    private BluetoothCoreEventManager eventManager;
    private BluetoothEventHandler powerStateChangedHandler = new BluetoothEventHandler() {
        public void onReceive(Context context, Intent intent, BluetoothDevice device) {
            int powerState = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
            int icatchAdapterState = ICatchCoreBluetoothAssist.toICatchAdapterState(powerState);
            if (icatchAdapterState == -1) {
                BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "not defined adapter state " + icatchAdapterState);
                return;
            }
            Map<ICatchBroadcastReceiver, List<String>> broadcastReceiverMap = BluetoothSystemBroadcastHandler.this.eventManager.__get_locked_broadcast_receivers_map();
            for (ICatchBroadcastReceiver receiver : broadcastReceiverMap.keySet()) {
                if (((List) broadcastReceiverMap.get(receiver)).contains(ICatchBroadcastReceiverID.BT_ACTION_ADAPTER_STATE_CHANGED)) {
                    Intent userIntent = new Intent();
                    userIntent.setAction(ICatchBroadcastReceiverID.BT_ACTION_ADAPTER_STATE_CHANGED);
                    userIntent.putExtra(ICatchBroadcastReceiverID.BT_ADAPTER_STATE, icatchAdapterState);
                    receiver.onReceive(userIntent);
                    BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "Power state1: " + powerState + ", " + receiver);
                } else {
                    BluetoothLogger.getInstance().logI(BluetoothSystemBroadcastHandler.icatch_bluetooth_tag, "com.icatchtek.bluetooth.__icatch_bt_action_adapter_state_changed not set by receiver: " + receiver);
                }
            }
            BluetoothSystemBroadcastHandler.this.eventManager.__put_broadcast_receivers_map(broadcastReceiverMap);
        }
    };

    private interface BluetoothEventHandler {
        void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice);
    }

    public BluetoothSystemBroadcastHandler(Context context, BluetoothCoreEventManager coreEventManager) {
        this.context = context;
        this.eventManager = coreEventManager;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.context.registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public void release() {
        this.context.unregisterReceiver(this.broadcastReceiver);
    }
}
