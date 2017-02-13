package com.icatch.ismartdv2016.SystemInfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;

public class SystemInfo {
    private static SystemInfo instance = null;
    private static final String tag = "SystemInfo";

    public static SystemInfo getInstance() {
        if (instance == null) {
            instance = new SystemInfo();
        }
        return instance;
    }

    public static DisplayMetrics getMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        GlobalInfo.getInstance().getCurrentApp().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public String getLocalMacAddress(Activity activity) {
        WifiInfo info = ((WifiManager) activity.getSystemService("wifi")).getConnectionInfo();
        AppLog.i(tag, "current Mac=" + info.getMacAddress().toLowerCase());
        return info.getMacAddress().toLowerCase();
    }

    public static long getSDFreeSize() {
        long blockSize;
        long availableBlocks;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long totalBlocks;
        if (VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = (long) stat.getBlockSize();
            totalBlocks = (long) stat.getBlockCount();
            availableBlocks = (long) stat.getAvailableBlocks();
        }
        AppLog.i(tag, "getSDFreeSize=" + (blockSize * availableBlocks));
        return blockSize * availableBlocks;
    }

    private String formatSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    public static long getFreeMemory(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService("activity");
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        long freeMemorySize = mi.availMem / 1024;
        AppLog.i(tag, "current FreeMemory=" + freeMemorySize);
        return freeMemorySize;
    }

    public static int getWindowVisibleCountMax(int row) {
        int visibleCountMax = ((getMetrics().heightPixels / (getMetrics().widthPixels / row)) * row) + row;
        AppLog.i(tag, "end getWindowVisibleCountMax visibleCountMax=" + visibleCountMax);
        return visibleCountMax;
    }

    public static void hideInputMethod(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        if (inputMethodManager.isActive()) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 2);
            }
        }
    }
}
