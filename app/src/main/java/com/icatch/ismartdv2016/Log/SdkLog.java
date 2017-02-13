package com.icatch.ismartdv2016.Log;

import android.os.Environment;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileOper;
import com.icatch.wificam.customer.ICatchWificamLog;
import com.icatch.wificam.customer.type.ICatchLogLevel;

public class SdkLog {
    private static SdkLog sdkLog;

    public static SdkLog getInstance() {
        if (sdkLog == null) {
            sdkLog = new SdkLog();
        }
        return sdkLog;
    }

    public void enableSDKLog() {
        String path = Environment.getExternalStorageDirectory().toString() + AppInfo.SDK_LOG_DIRECTORY_PATH;
        Environment.getExternalStorageDirectory();
        FileOper.createDirectory(path);
        AppLog.d("sdkLog", "start enable sdklog");
        ICatchWificamLog.getInstance().setDebugMode(true);
        ICatchWificamLog.getInstance().setFileLogPath(path);
        ICatchWificamLog.getInstance().setSystemLogOutput(true);
        ICatchWificamLog.getInstance().setFileLogOutput(true);
        ICatchWificamLog.getInstance().setRtpLog(true);
        ICatchWificamLog.getInstance().setPtpLog(true);
        ICatchWificamLog.getInstance().setRtpLogLevel(ICatchLogLevel.ICH_LOG_LEVEL_INFO);
        ICatchWificamLog.getInstance().setPtpLogLevel(ICatchLogLevel.ICH_LOG_LEVEL_INFO);
        AppLog.d("sdkLog", "end enable sdklog");
    }
}
