package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidPasswdException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchPtpInitFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;

public class JWificamSession {
    private static native String checkConnection(int i);

    private static native String createJniSession();

    private static native String deleteJniSession(int i);

    private static native String destroySession(int i);

    private static native String deviceInit(String str);

    private static native String prepareSession(int i, String str, String str2, String str3);

    private static native String startDeviceScan();

    private static native String stopDeviceScan();

    private static native String wakeUpCamera(String str);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static int createJniSession_Jni() {
        try {
            return NativeValueExtractor.extractNativeIntValue(createJniSession());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static boolean deleteJniSession_Jni(int sessionID) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(deleteJniSession(sessionID));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static int prepareSession_Jni(int sessionID, String ipAddr, String username, String password) throws IchInvalidPasswdException, IchPtpInitFailedException {
        try {
            return NativeValueExtractor.extractNativeIntValue(prepareSession(sessionID, ipAddr, username, password));
        } catch (IchInvalidPasswdException ex) {
            throw ex;
        } catch (IchPtpInitFailedException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            ex3.printStackTrace();
            return -1;
        }
    }

    public static boolean destroySession_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(destroySession(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean startDeviceScan_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(startDeviceScan());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean stopDeviceScan_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(stopDeviceScan());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deviceInit_Jni(String ipAddr) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(deviceInit(ipAddr));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean checkConnection_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(checkConnection(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean wakeUpCamera_Jni(String macAddress) throws IchSocketException, IchInvalidArgumentException {
        if (macAddress == null) {
            throw new IchInvalidArgumentException();
        }
        try {
            return NativeValueExtractor.extractNativeBoolValue(wakeUpCamera(macAddress));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }
}
