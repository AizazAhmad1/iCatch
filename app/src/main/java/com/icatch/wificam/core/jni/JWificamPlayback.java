package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchNoSuchPathException;
import com.icatch.wificam.customer.exception.IchSocketException;

public class JWificamPlayback {
    private static native String cancelFileDownload(int i);

    private static native String closeFileTransChannel(int i);

    private static native String deleteFile(int i, String str);

    private static native String downloadFile(int i, String str, String str2);

    private static native String downloadFile1(int i, String str, String str2);

    private static native String downloadFileQuick(int i, String str, String str2);

    private static native String downloadImage(int i, String str, byte[] bArr);

    private static native String getQuickView(int i, String str, byte[] bArr);

    private static native String getThumbnail(int i, String str, byte[] bArr);

    private static native String listFiles1(int i, int i2);

    private static native String listFiles2(int i, int i2, int i3);

    private static native String openFileTransChannel(int i);

    private static native String uploadFile(int i, String str, String str2);

    private static native String uploadFileQuick(int i, String str, String str2);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static String listFiles_Jni(int sessionID, int type) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchPathException {
        try {
            return NativeValueExtractor.extractNativeStringValue(listFiles1(sessionID, type));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchNoSuchPathException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String listFiles_Jni(int sessionID, int type, int timeoutInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchPathException {
        try {
            return NativeValueExtractor.extractNativeStringValue(listFiles2(sessionID, type, timeoutInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchNoSuchPathException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static boolean openFileTransChannel_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(openFileTransChannel(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchInvalidSessionException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            ex3.printStackTrace();
            return false;
        }
    }

    public static boolean downloadFileQuick_Jni(int sessionID, String icatchFile, String path) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(downloadFileQuick(sessionID, icatchFile, path));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean closeFileTransChannel_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(closeFileTransChannel(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchInvalidSessionException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            ex3.printStackTrace();
            return false;
        }
    }

    public static int downloadImage_Jni(int sessionID, String icatchFile, byte[] buffer) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException, IchDeviceException, IchBufferTooSmallException {
        try {
            return NativeValueExtractor.extractNativeIntValue(downloadImage(sessionID, icatchFile, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchBufferTooSmallException ex5) {
            throw ex5;
        } catch (IchInvalidSessionException ex6) {
            throw ex6;
        } catch (Exception ex7) {
            ex7.printStackTrace();
            return -1;
        }
    }

    public static boolean downloadFile_Jni(int sessionID, String icatchFile, String path) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(downloadFile(sessionID, icatchFile, path));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean downloadFile1_Jni(int sessionID, String srcPath, String dstPath) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(downloadFile1(sessionID, srcPath, dstPath));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean uploadFile_Jni(int sessionID, String localPath, String remotePath) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(uploadFile(sessionID, localPath, remotePath));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean uploadFileQuick_Jni(int sessionID, String localPath, String remotePath) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(uploadFileQuick(sessionID, localPath, remotePath));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean cancelFileDownload_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDeviceException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(cancelFileDownload(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFile_Jni(int sessionID, String icatchFile) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDeviceException, IchNoSuchFileException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(deleteFile(sessionID, icatchFile));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static int getThumbnail_Jni(int sessionID, String icatchFile, byte[] buffer) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException, IchDeviceException, IchBufferTooSmallException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getThumbnail(sessionID, icatchFile, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchBufferTooSmallException ex5) {
            throw ex5;
        } catch (IchInvalidSessionException ex6) {
            throw ex6;
        } catch (Exception ex7) {
            ex7.printStackTrace();
            return -1;
        }
    }

    public static int getQuickView_Jni(int sessionID, String icatchFile, byte[] buffer) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException, IchDeviceException, IchBufferTooSmallException {
        try {
            return NativeValueExtractor.extractNativeIntValue(getQuickView(sessionID, icatchFile, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchDeviceException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchNoSuchFileException ex4) {
            throw ex4;
        } catch (IchBufferTooSmallException ex5) {
            throw ex5;
        } catch (IchInvalidSessionException ex6) {
            throw ex6;
        } catch (Exception ex7) {
            ex7.printStackTrace();
            return -1;
        }
    }
}
