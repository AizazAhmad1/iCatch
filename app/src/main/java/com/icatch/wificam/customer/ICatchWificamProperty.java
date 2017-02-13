package com.icatch.wificam.customer;

import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.wificam.core.jni.JWificamProperty;
import com.icatch.wificam.core.jni.util.DataTypeUtil;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchByteArray;
import com.icatch.wificam.customer.type.ICatchVideoFormat;
import java.util.List;

public class ICatchWificamProperty {
    private int sessionID;

    ICatchWificamProperty(int sessionID) {
        this.sessionID = sessionID;
    }

    public boolean setPropertyValue(int propId, int value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.setNumericPropertyValue_Jni(this.sessionID, propId, value);
    }

    public boolean setPropertyValue(int propId, int value, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.setNumericPropertyValue_Jni(this.sessionID, propId, value, timeoutInSecs);
    }

    public int getCurrentPropertyValue(int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.getCurrentNumericPropertyValue_Jni(this.sessionID, propId);
    }

    public int getCurrentPropertyValue(int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.getCurrentNumericPropertyValue_Jni(this.sessionID, propId, timeoutInSecs);
    }

    public List<Integer> getSupportedPropertyValues(int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedNumericPropertyValues_Jni(this.sessionID, propId));
    }

    public List<Integer> getSupportedPropertyValues(int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedNumericPropertyValues_Jni(this.sessionID, propId, timeoutInSecs));
    }

    public boolean setStringPropertyValue(int propId, String value) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.setStringPropertyValue_Jni(this.sessionID, propId, value);
    }

    public boolean setStringPropertyValue(int propId, String value, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.setStringPropertyValue_Jni(this.sessionID, propId, value, timeoutInSecs);
    }

    public String getCurrentStringPropertyValue(int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.getCurrentStringPropertyValue_Jni(this.sessionID, propId);
    }

    public String getCurrentStringPropertyValue(int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.getCurrentStringPropertyValue_Jni(this.sessionID, propId, timeoutInSecs);
    }

    public List<String> getSupportedStringPropertyValues(int propId) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return DataTypeUtil.splitStringToStringList(JWificamProperty.getSupportedStringPropertyValues_Jni(this.sessionID, propId));
    }

    public List<String> getSupportedStringPropertyValues(int propId, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return DataTypeUtil.splitStringToStringList(JWificamProperty.getSupportedStringPropertyValues_Jni(this.sessionID, propId, timeoutInSecs));
    }

    public boolean setByteArrayPropertyValue(int propId, ICatchByteArray byteArray, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        return JWificamProperty.setByteArrayPropertyValue_Jni(this.sessionID, propId, byteArray, timeoutInSecs);
    }

    public ICatchByteArray getCurrentByteArrayPropertyValue(int propId, int objectPropCode, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException {
        byte[] byteValue = new byte[AppMessage.PHOTO_PBACTIVITY];
        int dataSize = JWificamProperty.getCurrentByteArrayPropertyValue_Jni(this.sessionID, propId, byteValue, timeoutInSecs);
        if (dataSize <= 0) {
            return null;
        }
        return new ICatchByteArray(byteValue, dataSize);
    }

    public boolean setLightFrequency(int value) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setLightFrequency_Jni(this.sessionID, value);
    }

    public List<Integer> getSupportedLightFrequencies() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedLightFrequencies_Jni(this.sessionID));
    }

    public int getCurrentLightFrequency() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentLightFrequency_Jni(this.sessionID);
    }

    public boolean setWhiteBalance(int value) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setWhiteBalance_Jni(this.sessionID, value);
    }

    public List<Integer> getSupportedWhiteBalances() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedWhiteBalances_Jni(this.sessionID));
    }

    public int getCurrentWhiteBalance() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentWhiteBalance_Jni(this.sessionID);
    }

    public boolean setCaptureDelay(int value) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setCaptureDelay_Jni(this.sessionID, value);
    }

    public List<Integer> getSupportedCaptureDelays() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedCaptureDelays_Jni(this.sessionID));
    }

    public int getCurrentCaptureDelay() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentCaptureDelay_Jni(this.sessionID);
    }

    public boolean setImageSize(String value) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setImageSize_Jni(this.sessionID, value);
    }

    public List<String> getSupportedImageSizes() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToStringList(JWificamProperty.getSupportedImageSizes_Jni(this.sessionID));
    }

    public String getCurrentImageSize() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentImageSize_Jni(this.sessionID);
    }

    public boolean setVideoSize(String value) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setVideoSize_Jni(this.sessionID, value);
    }

    public List<String> getSupportedVideoSizes() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToStringList(JWificamProperty.getSupportedVideoSizes_Jni(this.sessionID));
    }

    public String getCurrentVideoSize() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentVideoSize_Jni(this.sessionID);
    }

    public boolean setBurstNumber(int value) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setBurstNumber_Jni(this.sessionID, value);
    }

    public List<Integer> getSupportedBurstNumbers() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedBurstNumbers_Jni(this.sessionID));
    }

    public int getCurrentBurstNumber() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentBurstNumber_Jni(this.sessionID);
    }

    public boolean setDateStamp(int timestamp) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setDateStamp_Jni(this.sessionID, timestamp);
    }

    public List<Integer> getSupportedDateStamps() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedDateStamps_Jni(this.sessionID));
    }

    public int getCurrentDateStamp() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentDateStamp_Jni(this.sessionID);
    }

    public List<Integer> getSupportedTimeLapseIntervals() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedTimeLapseIntervals_Jni(this.sessionID));
    }

    public boolean setTimeLapseInterval(int stillInterval) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setTimeLapseInterval_Jni(this.sessionID, stillInterval);
    }

    public int getCurrentTimeLapseInterval() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentTimeLapseInterval_Jni(this.sessionID);
    }

    public List<Integer> getSupportedTimeLapseDurations() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedTimeLapseDurations_Jni(this.sessionID));
    }

    public boolean setTimeLapseDuration(int stillDuration) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setTimeLapseDuration_Jni(this.sessionID, stillDuration);
    }

    public int getCurrentTimeLapseDuration() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentTimeLapseDuration_Jni(this.sessionID);
    }

    public int getCurrentUpsideDown() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentUpsideDown_Jni(this.sessionID);
    }

    public boolean setUpsideDown(int usd) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setUpsideDown_Jni(this.sessionID, usd);
    }

    public int getCurrentSlowMotion() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentSlowMotion_Jni(this.sessionID);
    }

    public boolean setSlowMotion(int sm) throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.setSlowMotion_Jni(this.sessionID, sm);
    }

    public int getMaxZoomRatio() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getMaxZoomRatio_Jni(this.sessionID);
    }

    public int getCurrentZoomRatio() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamProperty.getCurrentZoomRatio_Jni(this.sessionID);
    }

    public List<Integer> getSupportedProperties() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToIntList(JWificamProperty.getSupportedCapabilities_Jni(this.sessionID));
    }

    public List<ICatchVideoFormat> getSupportedStreamingInfos() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.splitStringToVideoFormatList(JWificamProperty.getSupportedStreamingInfos_Jni(this.sessionID));
    }

    public ICatchVideoFormat getCurrentStreamingInfo() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return DataTypeUtil.toVideoFormat(JWificamProperty.getCurrentStreamingInfo_Jni(this.sessionID));
    }

    public boolean setStreamingInfo(ICatchVideoFormat info) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        if (info == null) {
            return false;
        }
        return JWificamProperty.setStreamingInfo_Jni(this.sessionID, DataTypeUtil.toVideoFormat(info));
    }

    public int getPreviewCacheTime() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDevicePropException {
        return JWificamProperty.getPreviewCacheTime_Jni(this.sessionID);
    }
}
