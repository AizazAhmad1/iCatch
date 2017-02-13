package com.icatch.ismartdv2016.SdkApi;

import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamConfig;
import uk.co.senab.photoview.IPhotoView;

public class AppCfgParameter {
    private ICatchWificamConfig configuration = ICatchWificamConfig.getInstance();
    private final String tag = "AppCfgParameter";

    public int getPreviewCacheTime() {
        AppLog.i("AppCfgParameter", "begin getPreviewCacheTime");
        int retVal = this.configuration.getPreviewCacheTime();
        AppLog.i("AppCfgParameter", "end getPreviewCacheTime retVal =" + retVal);
        return retVal;
    }

    public void setPreviewCacheParam(int cacheTimeInMs) {
        AppLog.i("AppCfgParameter", "begin setPreviewCacheParam cacheTimeInMs =" + cacheTimeInMs);
        this.configuration.setPreviewCacheParam(cacheTimeInMs, IPhotoView.DEFAULT_ZOOM_DURATION);
        AppLog.i("AppCfgParameter", "end setPreviewCacheParam");
    }
}
