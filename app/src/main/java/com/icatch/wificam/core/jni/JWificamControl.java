package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
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
import com.icatch.wificam.customer.type.cameraMode;

public class JWificamControl {
    private static native String addCustomEventListener(int i, int i2);

    private static native String capturePhotoA(int i);

    private static native String capturePhotoB(int i, int i2);

    private static native String delCustomEventListener(int i, int i2);

    private static native String formatStorage1(int i);

    private static native String formatStorage2(int i, int i2);

    private static native String getCurrentBatteryLevel(int i);

    private static native String getCurrentCameraMode(int i);

    private static native String getFreeSpaceInImages(int i);

    private static native String getRemainRecordingTime(int i);

    private static native String getSupportedModes(int i);

    private static native String isSDCardExist(int i);

    private static native String pan(int i, int i2, int i3);

    private static native String panReset(int i);

    private static native String startMovieRecord(int i);

    private static native String startTimeLapse(int i);

    private static native String stopMovieRecord(int i);

    private static native String stopTimeLapse(int i);

    private static native String supportedVideoPlayback(int i);

    private static native String toStandbyMode(int i);

    private static native String triggerCapturePhoto(int i);

    private static native String zoomIn(int i);

    private static native String zoomOut(int i);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static String getSupportedModes_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSupportedModes(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return null;
        }
    }

    public static int getCurrentCameraMode_Jni(int sessionID) {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentCameraMode(sessionID));
        } catch (Exception ex) {
            ex.printStackTrace();
            return cameraMode.MODE_UNDEFINED;
        }
    }

    public static boolean startTimeLapse_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(startTimeLapse(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return false;
        }
    }

    public static boolean stopTimeLapse_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(stopTimeLapse(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return false;
        }
    }

    public static int getCurrentBatteryLevel_Jni(int sessionID) throws IchSocketException, IchInvalidSessionException, IchCameraModeException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getCurrentBatteryLevel(sessionID));
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

    public static boolean startMovieRecord_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(startMovieRecord(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return false;
        }
    }

    public static boolean stopMovieRecord_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(stopMovieRecord(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return false;
        }
    }

    public static boolean capturePhoto_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchCaptureImageException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(capturePhotoA(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchCaptureImageException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean capturePhoto_Jni(int sessionID, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchCaptureImageException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(capturePhotoB(sessionID, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchCaptureImageException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean triggerCapturePhoto_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchCaptureImageException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(triggerCapturePhoto(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchCaptureImageException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean formatStorage_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStorageFormatException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(formatStorage1(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchStorageFormatException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean formatStorage_Jni(int sessionID, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStorageFormatException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(formatStorage2(sessionID, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchStorageFormatException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean zoomIn_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchStorageFormatException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(zoomIn(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchStorageFormatException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean zoomOut_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchStorageFormatException, IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(zoomOut(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchStorageFormatException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean pan_Jni(int sessionID, int xshift, int yshfit) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(pan(sessionID, xshift, yshfit));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean panReset_Jni(int sessionID) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(panReset(sessionID));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean toStandbyMode_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(toStandbyMode(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return false;
        }
    }

    public static boolean isSDCardExist_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isSDCardExist(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchDeviceException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static int getFreeSpaceInImages_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchNoSDCardException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getFreeSpaceInImages(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchNoSDCardException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchDevicePropException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return -1;
        }
    }

    public static int getRemainRecordingTime_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchNoSDCardException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getRemainRecordingTime(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchNoSDCardException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchDevicePropException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return -1;
        }
    }

    public static boolean supportedVideoPlayback_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchNoSDCardException, IchDevicePropException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(supportedVideoPlayback(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchNoSDCardException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchDevicePropException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean addCustomEventListener_Jni(int sessionID, int eventID) throws IchInvalidSessionException, IchListenerExistsException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(addCustomEventListener(sessionID, eventID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (IchListenerExistsException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            ex3.printStackTrace();
            return false;
        }
    }

    public static boolean delCustomEventListener_Jni(int sessionID, int eventID) throws IchInvalidSessionException, IchListenerNotExistsException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(delCustomEventListener(sessionID, eventID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (IchListenerNotExistsException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            ex3.printStackTrace();
            return false;
        }
    }
}
