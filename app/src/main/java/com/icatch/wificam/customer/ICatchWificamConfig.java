package com.icatch.wificam.customer;

import com.icatch.wificam.core.jni.JWificamConfig;

public class ICatchWificamConfig {
    private static ICatchWificamConfig instance = new ICatchWificamConfig();

    private ICatchWificamConfig() {
    }

    public static ICatchWificamConfig getInstance() {
        return instance;
    }

    public boolean setConnectionCheckParam(int ptpTimeoutCheckCount, int rtpTimeoutInSecs) {
        return JWificamConfig.setConnectionCheckParam_Jni(ptpTimeoutCheckCount, ptpTimeoutCheckCount);
    }

    public boolean setConnectionCheckParam(int ptpTimeoutCheckCount, double ptpTimeoutCheckIntervalInSecs, int rtpTimeoutInSecs) {
        return JWificamConfig.setConnectionCheckParam_Jni(ptpTimeoutCheckCount, ptpTimeoutCheckIntervalInSecs, ptpTimeoutCheckCount);
    }

    public int getRtpTimeoutInSecs() {
        return JWificamConfig.getRtpTimeoutInSecs_Jni();
    }

    public int getPtpTimeoutCheckCount() {
        return JWificamConfig.getPtpTimeoutCheckCount_Jni();
    }

    public double getPtpTimeoutCheckIntervalInSecs() {
        return JWificamConfig.getPtpTimeoutCheckIntervalInSecs_Jni();
    }

    public boolean enablePTPIP() {
        return JWificamConfig.enablePTPIP_Jni();
    }

    public boolean disablePTPIP() {
        return JWificamConfig.disablePTPIP_Jni();
    }

    public boolean setPreviewCacheParam(int cacheTimeInMs, int dropFrameTimeOverMs) {
        return JWificamConfig.setPreviewCacheParam_Jni(cacheTimeInMs, dropFrameTimeOverMs);
    }

    public int getPreviewCacheTime() {
        return JWificamConfig.getPreviewCacheTime_Jni();
    }

    public int getDropFrameTime() {
        return JWificamConfig.getDropFrameTime_Jni();
    }

    public boolean enableDumpMediaStream(boolean videoStream, String filePath) {
        return JWificamConfig.enableDumpMediaStream_Jni(videoStream, filePath);
    }

    public boolean disableDumpMediaStream(boolean videoStream) {
        return JWificamConfig.disableDumpMediaStream_Jni(videoStream);
    }

    public boolean enableSoftwareDecoder(boolean softwareDecoder) {
        return JWificamConfig.enableSoftwareDecoder_Jni(softwareDecoder);
    }
}
