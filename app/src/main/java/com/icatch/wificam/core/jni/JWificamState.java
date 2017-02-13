package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

public class JWificamState {
    private static native String isCameraBusy(int i);

    private static native String isMoviePlaying(int i);

    private static native String isMovieRecording(int i);

    private static native String isStreaming(int i);

    private static native String isTimeLapseStillOn(int i);

    private static native String isTimeLapseVideoOn(int i);

    private static native String supportImageAutoDownload(int i);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static boolean isStreaming_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isStreaming(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean isCameraBusy_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isCameraBusy(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean isMovieRecording_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isMovieRecording(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean isMoviePlaying_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isMoviePlaying(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean isTimeLapseStillOn_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isTimeLapseStillOn(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean isTimeLapseVideoOn_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(isTimeLapseVideoOn(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public static boolean supportImageAutoDownload_Jni(int sessionID) throws IchInvalidSessionException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(supportImageAutoDownload(sessionID));
        } catch (IchInvalidSessionException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }
}
