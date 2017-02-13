package com.icatchtek.bluetooth.core.event;

import android.content.Intent;
import com.icatchtek.bluetooth.core.base.BluetoothContext;

public interface BluetoothBroadcastReceiver {
    void onReceive(BluetoothContext bluetoothContext, Intent intent);
}
