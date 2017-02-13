package com.icatchtek.bluetooth.core.base;

import android.util.Log;
import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;

public class BluetoothLogger {
    private static BluetoothLogger logger = new BluetoothLogger();
    private boolean enableSystemLog;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$icatchtek$bluetooth$core$base$BluetoothLogger$AppLogLevel = new int[AppLogLevel.values().length];

        static {
            try {
                $SwitchMap$com$icatchtek$bluetooth$core$base$BluetoothLogger$AppLogLevel[AppLogLevel.APP_LOG_INFO.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$icatchtek$bluetooth$core$base$BluetoothLogger$AppLogLevel[AppLogLevel.APP_LOG_WARN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$icatchtek$bluetooth$core$base$BluetoothLogger$AppLogLevel[AppLogLevel.APP_LOG_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private enum AppLogLevel {
        APP_LOG_INFO,
        APP_LOG_WARN,
        APP_LOG_ERROR
    }

    private BluetoothLogger() {
        this.enableSystemLog = true;
        this.enableSystemLog = true;
    }

    public static BluetoothLogger getInstance() {
        return logger;
    }

    public void logI(String tag, String message) {
        if (this.enableSystemLog) {
            logToSystem(AppLogLevel.APP_LOG_INFO, tag, message);
        }
    }

    public void logW(String tag, String message) {
        if (this.enableSystemLog) {
            logToSystem(AppLogLevel.APP_LOG_WARN, tag, message);
        }
    }

    public void logE(String tag, String message) {
        if (this.enableSystemLog) {
            logToSystem(AppLogLevel.APP_LOG_ERROR, tag, message);
        }
    }

    private synchronized void logToSystem(AppLogLevel appLogLevel, String tag, String message) {
        switch (AnonymousClass1.$SwitchMap$com$icatchtek$bluetooth$core$base$BluetoothLogger$AppLogLevel[appLogLevel.ordinal()]) {
            case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                Log.i("app::" + tag, message);
                break;
            case SlidingMenu.TOUCHMODE_NONE /*2*/:
                Log.w("app::" + tag, message);
                break;
            case R.styleable.SlidingMenu_behindOffset /*3*/:
                Log.e("app::" + tag, message);
                break;
        }
    }
}
