package com.icatch.ismartdv2016.SdkApi;

import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamInfo;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import uk.co.senab.photoview.BuildConfig;

public class CameraFixedInfo {
    private static CameraFixedInfo instance;
    private ICatchWificamInfo cameraFixedInfo;
    private final String tag = "CameraFixedInfo";

    public static CameraFixedInfo getInstance() {
        if (instance == null) {
            instance = new CameraFixedInfo();
        }
        return instance;
    }

    private CameraFixedInfo() {
    }

    public void initCameraFixedInfo() {
        this.cameraFixedInfo = GlobalInfo.getInstance().getCurrentCamera().getCameraInfoClint();
    }

    public String getCameraName() {
        AppLog.i("CameraFixedInfo", "begin getCameraName");
        String name = BuildConfig.FLAVOR;
        try {
            name = this.cameraFixedInfo.getCameraProductName();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraFixedInfo", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraFixedInfo", "end getCameraName name =" + name);
        return name;
    }

    public String getCameraVersion() {
        AppLog.i("CameraFixedInfo", "begin getCameraVersion");
        String version = BuildConfig.FLAVOR;
        try {
            version = this.cameraFixedInfo.getCameraFWVersion();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraFixedInfo", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraFixedInfo", "end getCameraVersion version =" + version);
        return version;
    }
}
