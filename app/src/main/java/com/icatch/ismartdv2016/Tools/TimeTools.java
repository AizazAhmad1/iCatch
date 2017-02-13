package com.icatch.ismartdv2016.Tools;

public class TimeTools {
    private static long lastClickTime;

    public static synchronized boolean isFastClick() {
        boolean z;
        synchronized (TimeTools.class) {
            long time = System.currentTimeMillis();
            if (time - lastClickTime < 1000) {
                z = true;
            } else {
                lastClickTime = time;
                z = false;
            }
        }
        return z;
    }
}
