package com.icatch.ismartdv2016.Tools;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.icatch.ismartdv2016.Log.AppLog;

public class PermissionTools {
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    private static String TAG = "PermissionTools";
    public static final int WRITE_OR_READ_EXTERNAL_STORAGE_REQUEST_CODE = 102;

    public static void RequestPermissions(Activity activity) {
        AppLog.d(TAG, "Start RequestPermissions");
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, WRITE_OR_READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        AppLog.d(TAG, "End RequestPermissions");
    }

    public static boolean CheckSelfPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
}
