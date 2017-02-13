package com.icatch.ismartdv2016.SystemInfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import uk.co.senab.photoview.BuildConfig;

public class MWifiManager {
    public static String getSsid(Context context) {
        WifiInfo wifiInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        if (wifiInfo.getSSID() == null) {
            return null;
        }
        return wifiInfo.getSSID().replaceAll("\"", BuildConfig.FLAVOR);
    }
}
