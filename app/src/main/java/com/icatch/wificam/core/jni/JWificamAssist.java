package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNotSupportedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchTimeOutException;

public class JWificamAssist {
    private static native String notifyUpdateFw();

    private static native String simpleConfig(String str, String str2, byte[] bArr, String str3, String str4, int i);

    private static native String simpleConfigCancel();

    private static native String simpleConfigGet();

    private static native String supportLocalPlay(String str);

    private static native String updateFw(int i, String str);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static boolean supportLocalPlay_Jni(String file) {
        try {
            return NativeValueExtractor.extractNativeBoolValue(supportLocalPlay(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean updateFw_Jni(int sessionID, String fwFile) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException, IchTimeOutException, IchDeviceException, IchNotSupportedException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(updateFw(sessionID, fwFile));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (IchSocketException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchDevicePropException ex4) {
            throw ex4;
        } catch (IchTimeOutException ex5) {
            throw ex5;
        } catch (IchDeviceException ex6) {
            throw ex6;
        } catch (IchNotSupportedException ex7) {
            throw ex7;
        } catch (Exception ex8) {
            ex8.printStackTrace();
            return false;
        }
    }

    public static void notifyUpdateFw_Jni() {
        notifyUpdateFw();
    }

    public static boolean simpleConfig_Jni(String essid, String passwd, byte[] key, String ipAddr, String macAddr, int timeout) throws IchSocketException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(simpleConfig(essid, passwd, key, ipAddr, macAddr, timeout));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static String simpleConfigGet_Jni() throws IchTimeOutException {
        try {
            return NativeValueExtractor.extractNativeStringValue(simpleConfigGet());
        } catch (IchTimeOutException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return null;
        }
    }

    public static boolean simpleConfigCancel_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(simpleConfigCancel());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
