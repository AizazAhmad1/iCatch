package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;

public class JWificamConfig {
    private static native String disableDumpMediaStream(boolean z);

    private static native String disablePTPIP();

    private static native String enableDumpMediaStream(boolean z, String str);

    private static native String enablePTPIP();

    private static native String enableSoftwareDecoder(boolean z);

    private static native String getDropFrameTime();

    private static native String getPreviewCacheTime();

    private static native String getPtpTimeoutCheckCount();

    private static native String getPtpTimeoutCheckIntervalInSecs();

    private static native String getRtpTimeoutInSecs();

    private static native String setConnectionCheckParam(int i, int i2);

    private static native String setConnectionCheckParamA(int i, double d, int i2);

    private static native String setPreviewCacheParam(int i, int i2);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static boolean setConnectionCheckParam_Jni(int ptpTimeoutCheckCount, int rtpTimeoutInSecs) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setConnectionCheckParam(ptpTimeoutCheckCount, rtpTimeoutInSecs));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean setConnectionCheckParam_Jni(int ptpTimeoutCheckCount, double ptpTimeoutCheckIntervalInSecs, int rtpTimeoutInSecs) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setConnectionCheckParamA(ptpTimeoutCheckCount, ptpTimeoutCheckIntervalInSecs, rtpTimeoutInSecs));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static int getRtpTimeoutInSecs_Jni() {
        try {
            return NativeValueExtractor.extractNativeIntValue(getRtpTimeoutInSecs());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static int getPtpTimeoutCheckCount_Jni() {
        try {
            return NativeValueExtractor.extractNativeIntValue(getPtpTimeoutCheckCount());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static double getPtpTimeoutCheckIntervalInSecs_Jni() {
        try {
            return NativeValueExtractor.extractNativeDoubleValue(getPtpTimeoutCheckIntervalInSecs());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1.0d;
        }
    }

    public static boolean enablePTPIP_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(enablePTPIP());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean disablePTPIP_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(disablePTPIP());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean setPreviewCacheParam_Jni(int cacheTimeInMs, int dropFrameTimeOverMs) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setPreviewCacheParam(cacheTimeInMs, dropFrameTimeOverMs));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static int getPreviewCacheTime_Jni() {
        try {
            return NativeValueExtractor.extractNativeIntValue(getPreviewCacheTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static int getDropFrameTime_Jni() {
        try {
            return NativeValueExtractor.extractNativeIntValue(getDropFrameTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static boolean enableDumpMediaStream_Jni(boolean videoStream, String filePath) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(enableDumpMediaStream(videoStream, filePath));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean disableDumpMediaStream_Jni(boolean videoStream) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(disableDumpMediaStream(videoStream));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean enableSoftwareDecoder_Jni(boolean softwareDecoder) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(enableSoftwareDecoder(softwareDecoder));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
