package com.icatch.ismartdv2016.Listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.PreviewMode;

public class WifiListener {
    private static String TAG = "WifiListener";
    private Context context;
    private Handler handler;
    private WifiReceiver wifiReceiver;

    private class WifiReceiver extends BroadcastReceiver {
        private WifiReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("android.net.wifi.RSSI_CHANGED")) {
                if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
                    NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (info.getState().equals(State.DISCONNECTED)) {
                        AppLog.d(WifiListener.TAG, "wifi\u7f51\u7edc\u8fde\u63a5\u65ad\u5f00\u3000AppInfo.isReconnecting=" + GlobalInfo.isReconnecting);
                    } else if (info.getState().equals(State.CONNECTED)) {
                        WifiInfo wifiInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                        AppLog.d(WifiListener.TAG, "\u8fde\u63a5\u5230\u7f51\u7edc " + wifiInfo.getSSID());
                        WifiListener.this.handler.obtainMessage(PreviewMode.APP_STATE_VIDEO_MODE, wifiInfo).sendToTarget();
                    }
                } else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                    int wifistate = intent.getIntExtra("wifi_state", 1);
                    if (wifistate == 1) {
                        AppLog.d(WifiListener.TAG, "\u7cfb\u7edf\u5173\u95edwifi");
                    } else if (wifistate == 3) {
                        AppLog.d(WifiListener.TAG, "\u7cfb\u7edf\u5f00\u542fwifi");
                    }
                }
            }
        }
    }

    public WifiListener(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void registerReceiver() {
        AppLog.d(TAG, "registerReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.wifiReceiver = new WifiReceiver();
        this.context.registerReceiver(this.wifiReceiver, filter);
    }

    public void unregisterReceiver() {
        AppLog.d(TAG, "unregisterReceiver");
        this.context.unregisterReceiver(this.wifiReceiver);
    }
}
