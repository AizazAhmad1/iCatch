package com.icatchtek.bluetooth.core.base;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.IntentFilter;
import com.icatchtek.bluetooth.core.event.BluetoothBroadcastReceiver;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiverID;
import com.slidingmenu.lib.R;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import uk.co.senab.photoview.BuildConfig;

public class BluetoothContext {
    private static final String icatch_bluetooth_tag = BluetoothContext.class.getSimpleName();
    private static BluetoothContext instance = new BluetoothContext();
    private BluetoothMutex bluetoothMutex = new BluetoothMutex();
    private Map<BluetoothBroadcastReceiver, List<String>> broadcastReceivers = new HashMap();
    private String receiveData = BuildConfig.FLAVOR;

    private BluetoothContext() {
    }

    public static BluetoothContext getInstance() {
        return instance;
    }

    public void registerReceiver(BluetoothBroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        List<String> actions = new LinkedList();
        for (int index = 0; index < intentFilter.countActions(); index++) {
            actions.add(intentFilter.getAction(index));
        }
        __lock_map_status();
        this.broadcastReceivers.put(broadcastReceiver, actions);
        __unlock_map_status();
    }

    public void unregisterReceiver(BluetoothBroadcastReceiver broadcastReceiver) {
        __lock_map_status();
        this.broadcastReceivers.remove(broadcastReceiver);
        __unlock_map_status();
    }

    public void putBluetoothGattEvent(int eventID) {
        Intent intent = __generate_general_gatt_intent(eventID);
        if (intent == null) {
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "unrecognized gatt event id: " + eventID);
        } else {
            __call_gatt_broadcast_receivers(intent);
        }
    }

    public void putBluetoothGattEvent(int eventID, BluetoothGattCharacteristic characteristic, int status) {
        Intent intent = __generate_general_gatt_intent(eventID);
        if (intent == null) {
            BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "unrecognized gatt event id: " + eventID);
            return;
        }
        intent.putExtra(ICatchBroadcastReceiverID.BT_LE_GATT_STATUS, status);
        byte[] data = characteristic.getValue();
        if (eventID != 38) {
            this.receiveData += BluetoothUtils.bytesToString(data);
        }
        if (data != null && data.length > 0 && this.receiveData.contains("}")) {
            intent.putExtra(ICatchBroadcastReceiverID.BT_LE_GATT_DATA, this.receiveData);
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "extra data is: " + data + ", length: " + this.receiveData.length());
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "extra data is: " + this.receiveData);
            this.receiveData = BuildConfig.FLAVOR;
        }
        __call_gatt_broadcast_receivers(intent);
    }

    private void __lock_map_status() {
        this.bluetoothMutex.lock_1();
    }

    private void __unlock_map_status() {
        this.bluetoothMutex.unlock();
    }

    private void __call_gatt_broadcast_receivers(Intent intent) {
        __lock_map_status();
        for (BluetoothBroadcastReceiver receiver : this.broadcastReceivers.keySet()) {
            if (((List) this.broadcastReceivers.get(receiver)).contains(intent.getAction())) {
                BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, intent.getAction() + " set by receiver: " + receiver);
                receiver.onReceive(this, intent);
            }
        }
        __unlock_map_status();
    }

    private Intent __generate_general_gatt_intent(int eventID) {
        Intent intent = new Intent();
        switch (eventID) {
            case R.styleable.SherlockTheme_searchViewCloseIcon /*33*/:
            case R.styleable.SherlockTheme_searchViewGoIcon /*34*/:
                intent.setAction(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_CONNECTION_STATE_CHANGED);
                intent.putExtra(ICatchBroadcastReceiverID.BT_LE_GATT_CONNECTION_STATE, eventID);
                return intent;
            case R.styleable.SherlockTheme_searchViewSearchIcon /*35*/:
            case R.styleable.SherlockTheme_searchViewVoiceIcon /*36*/:
                intent.setAction(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_SERVICE_DISCOVERY_STATE_CHANGED);
                intent.putExtra(ICatchBroadcastReceiverID.BT_LE_GATT_SERVICE_DISCOVERY_STATE, eventID);
                return intent;
            case R.styleable.SherlockTheme_searchViewEditQuery /*37*/:
            case R.styleable.SherlockTheme_searchViewEditQueryBackground /*38*/:
            case R.styleable.SherlockTheme_searchViewTextField /*39*/:
                intent.setAction(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_DATA_AVAILABLE);
                intent.putExtra(ICatchBroadcastReceiverID.BT_LE_GATT_DATA_TYPE, eventID);
                return intent;
            default:
                return null;
        }
    }
}
