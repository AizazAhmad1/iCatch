package com.icatch.ismartdv2016.AppInfo;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class AppSharedPreferences {
    private static final String FILE_NAME = "storeInfo";
    public static final String OBJECT_NAME = "autoDownloadSizeLimit";

    public static void writeDataByName(Context context, String name, String value) {
        Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String readDataByName(Context context, String name) {
        return context.getSharedPreferences(FILE_NAME, 0).getString(name, "1.0");
    }
}
