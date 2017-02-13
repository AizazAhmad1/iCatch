package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchByteArray;

public class JWificamProperty {
    private static native String getCurrentBurstNumber(int i);

    private static native String getCurrentCaptureDelay(int i);

    private static native String getCurrentDateStamp(int i);

    private static native String getCurrentImageSize(int i);

    private static native String getCurrentLightFrequency(int i);

    private static native String getCurrentPropertyValueByteArray(int i, int i2, byte[] bArr, int i3);

    private static native String getCurrentPropertyValueNumeric1(int i, int i2);

    private static native String getCurrentPropertyValueNumeric2(int i, int i2, int i3);

    private static native String getCurrentPropertyValueString1(int i, int i2);

    private static native String getCurrentPropertyValueString2(int i, int i2, int i3);

    public static native String getCurrentSlowMotion(int i);

    private static native String getCurrentStreamingInfo(int i);

    public static native String getCurrentTimeLapseDuration(int i);

    public static native String getCurrentTimeLapseInterval(int i);

    public static native String getCurrentUpsideDown(int i);

    private static native String getCurrentVideoSize(int i);

    private static native String getCurrentWhiteBalance(int i);

    public static native String getCurrentZoomRatio(int i);

    public static native String getMaxZoomRatio(int i);

    private static native String getPreviewCacheTime(int i);

    private static native String getSupportedBurstNumbers(int i);

    private static native String getSupportedCaptureDelays(int i);

    private static native String getSupportedDateStamps(int i);

    private static native String getSupportedImageSizes(int i);

    private static native String getSupportedLightFrequencies(int i);

    private static native String getSupportedProperties(int i);

    private static native String getSupportedPropertyValuesNumeric1(int i, int i2);

    private static native String getSupportedPropertyValuesNumeric2(int i, int i2, int i3);

    private static native String getSupportedPropertyValuesString1(int i, int i2);

    private static native String getSupportedPropertyValuesString2(int i, int i2, int i3);

    private static native String getSupportedStreamingInfos(int i);

    public static native String getSupportedTimeLapseDurations(int i);

    public static native String getSupportedTimeLapseIntervals(int i);

    private static native String getSupportedVideoSizes(int i);

    private static native String getSupportedWhiteBalances(int i);

    private static native String setBurstNumber(int i, int i2);

    private static native String setCaptureDelay(int i, int i2);

    private static native String setDateStamp(int i, int i2);

    private static native String setImageSize(int i, String str);

    private static native String setLightFrequency(int i, int i2);

    private static native String setPropertyValueByteArray(int i, int i2, byte[] bArr, int i3, int i4);

    private static native String setPropertyValueNumeric1(int i, int i2, int i3);

    private static native String setPropertyValueNumeric2(int i, int i2, int i3, int i4);

    private static native String setPropertyValueString1(int i, int i2, String str);

    private static native String setPropertyValueString2(int i, int i2, String str, int i3);

    public static native String setSlowMotion(int i, int i2);

    private static native String setStreamingInfo(int i, String str);

    public static native String setTimeLapseDuration(int i, int i2);

    public static native String setTimeLapseInterval(int i, int i2);

    public static native String setUpsideDown(int i, int i2);

    private static native String setVideoSize(int i, String str);

    private static native String setWhiteBalance(int i, int i2);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static int getCurrentNumericPropertyValue_Jni(int sessionID, int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentPropertyValueNumeric1(sessionID, propId));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static int getCurrentNumericPropertyValue_Jni(int sessionID, int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentPropertyValueNumeric2(sessionID, propId, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setNumericPropertyValue_Jni(int sessionID, int propId, int value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setPropertyValueNumeric1(sessionID, propId, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean setNumericPropertyValue_Jni(int sessionID, int propId, int value, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setPropertyValueNumeric2(sessionID, propId, value, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedNumericPropertyValues_Jni(int sessionID, int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedPropertyValuesNumeric1(sessionID, propId));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getSupportedNumericPropertyValues_Jni(int sessionID, int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedPropertyValuesNumeric2(sessionID, propId, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setStringPropertyValue_Jni(int sessionID, int propId, String value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setPropertyValueString1(sessionID, propId, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean setStringPropertyValue_Jni(int sessionID, int propId, String value, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setPropertyValueString2(sessionID, propId, value, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getCurrentStringPropertyValue_Jni(int sessionID, int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getCurrentPropertyValueString1(sessionID, propId));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getCurrentStringPropertyValue_Jni(int sessionID, int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getCurrentPropertyValueString2(sessionID, propId, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getSupportedStringPropertyValues_Jni(int sessionID, int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedPropertyValuesString1(sessionID, propId));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getSupportedStringPropertyValues_Jni(int sessionID, int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedPropertyValuesString2(sessionID, propId, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setByteArrayPropertyValue_Jni(int sessionID, int propId, ICatchByteArray byteArray, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setPropertyValueByteArray(sessionID, propId, byteArray.getValue(), byteArray.getDataSize(), timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getCurrentByteArrayPropertyValue_Jni(int sessionID, int propId, byte[] byteValue, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentPropertyValueByteArray(sessionID, propId, byteValue, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setLightFrequency_Jni(int sessionID, int value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setLightFrequency(sessionID, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedLightFrequencies_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedLightFrequencies(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static int getCurrentLightFrequency_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentLightFrequency(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setWhiteBalance_Jni(int sessionID, int value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setWhiteBalance(sessionID, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedWhiteBalances_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedWhiteBalances(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static int getCurrentWhiteBalance_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentWhiteBalance(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setCaptureDelay_Jni(int sessionID, int value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setCaptureDelay(sessionID, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedCaptureDelays_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedCaptureDelays(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static int getCurrentCaptureDelay_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentCaptureDelay(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setImageSize_Jni(int sessionID, String value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setImageSize(sessionID, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedImageSizes_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedImageSizes(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getCurrentImageSize_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getCurrentImageSize(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setVideoSize_Jni(int sessionID, String value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setVideoSize(sessionID, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedVideoSizes_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedVideoSizes(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getCurrentVideoSize_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getCurrentVideoSize(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setBurstNumber_Jni(int sessionID, int value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setBurstNumber(sessionID, value));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedBurstNumbers_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedBurstNumbers(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static int getCurrentBurstNumber_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentBurstNumber(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setDateStamp_Jni(int sessionID, int timestamp) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setDateStamp(sessionID, timestamp));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static String getSupportedDateStamps_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedDateStamps(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static int getCurrentDateStamp_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentDateStamp(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static String getSupportedTimeLapseIntervals_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedTimeLapseIntervals(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setTimeLapseInterval_Jni(int sessionID, int stillInterval) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setTimeLapseInterval(sessionID, stillInterval));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getCurrentTimeLapseInterval_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentTimeLapseInterval(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static String getSupportedTimeLapseDurations_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedTimeLapseDurations(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setTimeLapseDuration_Jni(int sessionID, int stillDuration) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setTimeLapseDuration(sessionID, stillDuration));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getCurrentTimeLapseDuration_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentTimeLapseDuration(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static int getCurrentUpsideDown_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentUpsideDown(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setUpsideDown_Jni(int sessionID, int usd) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setUpsideDown(sessionID, usd));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getCurrentSlowMotion_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentSlowMotion(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static boolean setSlowMotion_Jni(int sessionID, int sm) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setSlowMotion(sessionID, sm));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getMaxZoomRatio_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getMaxZoomRatio(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static int getCurrentZoomRatio_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentZoomRatio(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return -1;
        }
    }

    public static String getSupportedCapabilities_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedProperties(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getSupportedStreamingInfos_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedStreamingInfos(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getCurrentStreamingInfo_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getCurrentStreamingInfo(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean setStreamingInfo_Jni(int sessionID, String videoInfo) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(setStreamingInfo(sessionID, videoInfo));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getPreviewCacheTime_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getPreviewCacheTime(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchDevicePropException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return 400;
        }
    }
}
