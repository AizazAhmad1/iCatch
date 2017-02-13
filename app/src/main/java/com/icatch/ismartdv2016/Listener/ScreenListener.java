package com.icatch.ismartdv2016.Listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.icatch.ismartdv2016.Log.AppLog;

public class ScreenListener {
    private final String TAG = "ScreenListener";
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public interface ScreenStateListener {
        void onScreenOff();

        void onScreenOn();

        void onUserPresent();
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action;

        private ScreenBroadcastReceiver() {
            this.action = null;
        }

        public void onReceive(Context context, Intent intent) {
            this.action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(this.action)) {
                ScreenListener.this.mScreenStateListener.onScreenOn();
            } else if ("android.intent.action.SCREEN_OFF".equals(this.action)) {
                ScreenListener.this.mScreenStateListener.onScreenOff();
            } else if ("android.intent.action.USER_PRESENT".equals(this.action)) {
                ScreenListener.this.mScreenStateListener.onUserPresent();
            }
        }
    }

    public ScreenListener(Context context) {
        this.mContext = context;
        this.mScreenReceiver = new ScreenBroadcastReceiver();
    }

    public void begin(ScreenStateListener listener) {
        this.mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }

    private void getScreenState() {
        if (((PowerManager) this.mContext.getSystemService("power")).isScreenOn()) {
            if (this.mScreenStateListener != null) {
                this.mScreenStateListener.onScreenOn();
            }
        } else if (this.mScreenStateListener != null) {
            this.mScreenStateListener.onScreenOff();
        }
    }

    public void unregisterListener() {
        AppLog.d("ScreenListener", "unregisterListener");
        this.mContext.unregisterReceiver(this.mScreenReceiver);
    }

    private void registerListener() {
        AppLog.d("ScreenListener", "registerListener");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        this.mContext.registerReceiver(this.mScreenReceiver, filter);
    }
}
