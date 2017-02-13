package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

public class JWificamInfo {
    private static native String getCameraFWVersion(int i);

    private static native String getCameraProductName(int i);

    private static native String getSDKVersion(int i);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static String getSDKVersion_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getSDKVersion(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return null;
        }
    }

    public static String getCameraFWVersion_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getCameraFWVersion(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return null;
        }
    }

    public static String getCameraProductName_Jni(int sessionID) throws IchInvalidSessionException {
        String content = getCameraProductName(sessionID);
        CoreLogger.logI("xxx", "getCameraProductName_Jni: " + content);
        try {
            return NativeValueExtractor.extractNativeStringValue(content);
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return null;
        }
    }
}
