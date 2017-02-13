package com.icatch.ismartdv2016.SdkApi;

import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamState;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

public class CameraState {
    private static CameraState instance;
    private ICatchWificamState cameraState;
    private final String tag = "CameraState";

    public static CameraState getInstance() {
        if (instance == null) {
            instance = new CameraState();
        }
        return instance;
    }

    private CameraState() {
    }

    public void initCameraState() {
        this.cameraState = GlobalInfo.getInstance().getCurrentCamera().getCameraStateClint();
    }

    public boolean isMovieRecording() {
        AppLog.i("CameraState", "begin isMovieRecording");
        boolean retValue = false;
        try {
            retValue = this.cameraState.isMovieRecording();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraState", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraState", "end isMovieRecording retValue=" + retValue);
        return retValue;
    }

    public boolean isTimeLapseVideoOn() {
        AppLog.i("CameraState", "begin isTimeLapseVideoOn");
        boolean retValue = false;
        try {
            retValue = this.cameraState.isTimeLapseVideoOn();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraState", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraState", "end isTimeLapseVideoOn retValue=" + retValue);
        return retValue;
    }

    public boolean isTimeLapseStillOn() {
        AppLog.i("CameraState", "begin isTimeLapseStillOn");
        boolean retValue = false;
        try {
            retValue = this.cameraState.isTimeLapseStillOn();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraState", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraState", "end isTimeLapseStillOn retValue=" + retValue);
        return retValue;
    }

    public boolean isSupportImageAutoDownload() {
        AppLog.i("CameraState", "begin isSupportImageAutoDownload");
        boolean retValue = false;
        try {
            retValue = this.cameraState.supportImageAutoDownload();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraState", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraState", "end isSupportImageAutoDownload = " + retValue);
        return retValue;
    }

    public boolean isStreaming() {
        AppLog.i("CameraState", "begin isStreaming");
        boolean retValue = false;
        try {
            retValue = this.cameraState.isStreaming();
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraState", "IchInvalidSessionException");
            e.printStackTrace();
        }
        AppLog.i("CameraState", "end isStreaming retValue=" + retValue);
        return retValue;
    }
}
