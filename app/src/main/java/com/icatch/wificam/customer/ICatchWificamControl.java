package com.icatch.wificam.customer;

import com.icatch.wificam.core.jni.JWificamControl;
import com.icatch.wificam.core.jni.util.DataTypeUtil;
import com.icatch.wificam.core.util.SDKEventHandleAPI;
import com.icatch.wificam.core.util.type.NativeMode;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchCaptureImageException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.exception.IchNoSDCardException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStorageFormatException;
import com.icatch.wificam.customer.type.ICatchMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ICatchWificamControl {
    private String ipAddress;
    private int sessionID;

    ICatchWificamControl(int sessionID, String ipAddress) {
        this.sessionID = sessionID;
        this.ipAddress = ipAddress;
    }

    public List<ICatchMode> getSupportedModes() throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        List<Integer> modeIntValues = DataTypeUtil.splitStringToIntList(JWificamControl.getSupportedModes_Jni(this.sessionID));
        List<ICatchMode> modes = new LinkedList();
        for (Integer intValue : modeIntValues) {
            modes.add(NativeMode.convertValue(intValue.intValue()));
        }
        return modes;
    }

    public int getCurrentCameraMode() {
        return JWificamControl.getCurrentCameraMode_Jni(this.sessionID);
    }

    public boolean startTimeLapse() throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.startTimeLapse_Jni(this.sessionID);
    }

    public boolean stopTimeLapse() throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.stopTimeLapse_Jni(this.sessionID);
    }

    public int getCurrentBatteryLevel() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchInvalidSessionException {
        return JWificamControl.getCurrentBatteryLevel_Jni(this.sessionID);
    }

    public boolean isSDCardExist() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamControl.isSDCardExist_Jni(this.sessionID);
    }

    public int getFreeSpaceInImages() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchNoSDCardException, IchDevicePropException {
        return JWificamControl.getFreeSpaceInImages_Jni(this.sessionID);
    }

    public int getRemainRecordingTime() throws IchSocketException, IchCameraModeException, IchDevicePropException, IchNoSDCardException, IchInvalidSessionException {
        return JWificamControl.getRemainRecordingTime_Jni(this.sessionID);
    }

    public boolean supportVideoPlayback() throws IchDevicePropException, IchNoSDCardException, IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.supportedVideoPlayback_Jni(this.sessionID);
    }

    public boolean startMovieRecord() throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.startMovieRecord_Jni(this.sessionID);
    }

    public boolean stopMovieRecord() throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.stopMovieRecord_Jni(this.sessionID);
    }

    public boolean capturePhoto() throws IchCaptureImageException, IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.capturePhoto_Jni(this.sessionID);
    }

    public boolean capturePhoto(int timeoutInSecs) throws IchCaptureImageException, IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.capturePhoto_Jni(this.sessionID, timeoutInSecs);
    }

    public boolean triggerCapturePhoto() throws IchCaptureImageException, IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamControl.triggerCapturePhoto_Jni(this.sessionID);
    }

    public boolean formatStorage() throws IchSocketException, IchCameraModeException, IchStorageFormatException, IchInvalidSessionException {
        return JWificamControl.formatStorage_Jni(this.sessionID);
    }

    public boolean formatStorage(int timeoutInSecs) throws IchSocketException, IchCameraModeException, IchStorageFormatException, IchInvalidSessionException {
        return JWificamControl.formatStorage_Jni(this.sessionID, timeoutInSecs);
    }

    public boolean zoomIn() throws IchSocketException, IchCameraModeException, IchStorageFormatException, IchInvalidSessionException {
        return JWificamControl.zoomIn_Jni(this.sessionID);
    }

    public boolean zoomOut() throws IchSocketException, IchCameraModeException, IchStorageFormatException, IchInvalidSessionException {
        return JWificamControl.zoomOut_Jni(this.sessionID);
    }

    public boolean pan(int xshift, int yshfit) {
        return JWificamControl.pan_Jni(this.sessionID, xshift, yshfit);
    }

    public boolean panReset() {
        return JWificamControl.panReset_Jni(this.sessionID);
    }

    public boolean toStandbyMode() throws IchSocketException, IchDeviceException, IchInvalidSessionException {
        return JWificamControl.toStandbyMode_Jni(this.sessionID);
    }

    public String getMacAddress() {
        String line = getMatchedLineFromArpCache(this.ipAddress);
        if (line == null) {
            return null;
        }
        String[] attributes = line.split("\\s+");
        if (attributes.length >= 3) {
            return attributes[3];
        }
        return null;
    }

    private String getMatchedLineFromArpCache(String ipAddress) {
        Exception ex;
        Throwable th;
        String line = null;
        BufferedReader reader = null;
        try {
            BufferedReader reader2 = new BufferedReader(new FileReader("/proc/net/arp"));
            do {
                try {
                    line = reader2.readLine();
                    if (line == null) {
                        break;
                    }
                } catch (Exception e) {
                    ex = e;
                    reader = reader2;
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                }
            } while (!line.contains(ipAddress));
            if (reader2 != null) {
                try {
                    reader2.close();
                    reader = reader2;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    reader = reader2;
                }
            }
        } catch (Exception e3) {
            ex = e3;
            try {
                ex.printStackTrace();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                return line;
            } catch (Throwable th3) {
                th = th3;
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                throw th;
            }
        }
        return line;
    }

    public boolean addEventListener(int eventID, ICatchWificamListener listener) throws IchListenerExistsException, IchInvalidSessionException {
        SDKEventHandleAPI.getInstance().addStandardEventListener(eventID, this.sessionID, listener);
        return true;
    }

    public boolean delEventListener(int eventID, ICatchWificamListener listener) throws IchListenerNotExistsException, IchInvalidSessionException {
        SDKEventHandleAPI.getInstance().removeStandardEventListener(eventID, this.sessionID, listener);
        return true;
    }

    public boolean addCustomEventListener(int eventID, ICatchWificamListener listener) throws IchListenerExistsException, IchInvalidSessionException {
        JWificamControl.addCustomEventListener_Jni(this.sessionID, eventID);
        SDKEventHandleAPI.getInstance().addCustomerEventListener(eventID, this.sessionID, listener);
        return true;
    }

    public boolean delCustomEventListener(int eventID, ICatchWificamListener listener) throws IchListenerNotExistsException, IchInvalidSessionException {
        JWificamControl.delCustomEventListener_Jni(this.sessionID, eventID);
        SDKEventHandleAPI.getInstance().removeCustomerEventListener(eventID, this.sessionID, listener);
        return true;
    }
}
