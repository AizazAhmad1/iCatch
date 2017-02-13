package com.icatch.ismartdv2016.AppInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;
import com.icatch.ismartdv2016.Log.AppLog;
import uk.co.senab.photoview.IPhotoView;

public class AppInfo {
    public static final String APP_VERSION = "R1.4.7.2";
    public static final String AUTO_DOWNLOAD_PATH = "/DCIM/iSmartDV/";
    public static final String DOWNLOAD_PATH = "/DCIM/iSmartDV/";
    public static final String PROPERTY_CFG_DIRECTORY_PATH = "/SportCamResoure/";
    public static final String PROPERTY_CFG_FILE_NAME = "netconfig.properties";
    public static final String SDK_LOG_DIRECTORY_PATH = "/IcatchSportCamera_SDK_Log";
    public static final String STREAM_OUTPUT_DIRECTORY_PATH = "/SportCamResoure/Raw/";
    public static final String UPDATEFW_FILENAME = "/SportCamResoure/sphost.BRN";
    public static boolean autoDownloadAllow = false;
    public static float autoDownloadSizeLimit = IPhotoView.DEFAULT_MIN_SCALE;
    public static boolean disableAudio = false;
    public static boolean isDownloading = false;
    public static boolean isSdCardExist = true;
    public static boolean isSupportAutoReconnection = false;
    public static boolean isSupportBroadcast = false;
    public static boolean isSupportSetting = false;
    public static boolean saveSDKLog = false;

    public static boolean isAppSentToBackground(Context context) {
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            Log.d("1111", "appProcess.processName =" + appProcess.processName);
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != 100) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static void initAppdata(Context context) {
        String sizeLimit = AppSharedPreferences.readDataByName(context, AppSharedPreferences.OBJECT_NAME);
        AppLog.d("dd", "initAppdata sizeLimit=" + sizeLimit);
        if (sizeLimit == null) {
            autoDownloadSizeLimit = IPhotoView.DEFAULT_MIN_SCALE;
        } else {
            autoDownloadSizeLimit = Float.parseFloat(sizeLimit);
        }
        AppLog.d("dd", "initAppdata autoDownloadSizeLimit=" + autoDownloadSizeLimit);
    }
}
