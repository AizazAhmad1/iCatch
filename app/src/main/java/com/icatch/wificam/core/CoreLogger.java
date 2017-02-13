package com.icatch.wificam.core;

import android.util.Log;
import com.icatch.wificam.core.jni.JWificamLog;

public class CoreLogger {
    public static void logW(String subTag, String message) {
        if (subTag == null || message == null) {
            String str = "peng.tan";
            StringBuilder append = new StringBuilder().append("tag = ");
            if (subTag == null) {
                subTag = "null";
            }
            Log.e(str, append.append(subTag).toString());
            str = "peng.tan";
            append = new StringBuilder().append("tag = ");
            if (message == null) {
                message = "null";
            }
            Log.e(str, append.append(message).toString());
            return;
        }
        JWificamLog.writeLog(2, subTag, message);
    }

    public static void logI(String subTag, String message) {
        if (subTag == null || message == null) {
            String str = "peng.tan";
            StringBuilder append = new StringBuilder().append("tag = ");
            if (subTag == null) {
                subTag = "null";
            }
            Log.e(str, append.append(subTag).toString());
            str = "peng.tan";
            append = new StringBuilder().append("tag = ");
            if (message == null) {
                message = "null";
            }
            Log.e(str, append.append(message).toString());
            return;
        }
        JWificamLog.writeLog(1, subTag, message);
    }

    public static void logE(String subTag, String message) {
        if (subTag == null || message == null) {
            String str = "peng.tan";
            StringBuilder append = new StringBuilder().append("tag = ");
            if (subTag == null) {
                subTag = "null";
            }
            Log.e(str, append.append(subTag).toString());
            str = "peng.tan";
            append = new StringBuilder().append("tag = ");
            if (message == null) {
                message = "null";
            }
            Log.e(str, append.append(message).toString());
            return;
        }
        JWificamLog.writeLog(3, subTag, message);
    }
}
