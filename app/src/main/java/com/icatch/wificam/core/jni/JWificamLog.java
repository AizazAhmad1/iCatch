package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.util.NativeLibraryLoader;

public class JWificamLog {
    public static final int LOG_LEVEL_CONNECT = 0;
    public static final int LOG_LEVEL_ERROR = 3;
    public static final int LOG_LEVEL_INFO = 1;
    public static final int LOG_LEVEL_WARN = 2;

    public static native void setDebugMode(boolean z);

    public static native void setFileLogOutput(boolean z);

    public static native void setFileLogPath(String str);

    public static native void setPtpLog(boolean z);

    public static native void setPtpLogLevel(int i);

    public static native void setRtpLog(boolean z);

    public static native void setRtpLogLevel(int i);

    public static native void setSystemLogOutput(boolean z);

    public static native void writeLog(int i, String str, String str2);

    static {
        NativeLibraryLoader.loadLibrary();
    }
}
