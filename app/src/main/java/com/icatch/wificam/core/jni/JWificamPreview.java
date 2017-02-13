package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.DataTypeUtil;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.core.util.type.NativePreviewMode;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchStreamNotSupportException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.icatch.wificam.customer.type.ICatchPreviewMode;
import com.icatch.wificam.customer.type.ICatchVideoFormat;

public class JWificamPreview {
    private static native String changePreviewMode(int i, int i2);

    private static native String containsAudioStream(int i);

    private static native String containsVideoStream(int i);

    private static native String disableAudio(int i);

    private static native String enableAudio(int i);

    private static native String getAudioFormat(int i);

    private static native String getNextAudioFrame(int i, byte[] bArr);

    private static native String getNextVideoFrame(int i, byte[] bArr);

    private static native String getVideoFormat(int i);

    private static native String getVideoFormatCsdA(int i, byte[] bArr);

    private static native String getVideoFormatCsdB(int i, byte[] bArr);

    private static native String start(int i, String str, int i2, boolean z, boolean z2, boolean z3);

    private static native String startSavePreviewStream(int i, String str, String str2, int i2, boolean z);

    private static native String stop(int i);

    private static native String stopSavePreviewStream(int i);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static boolean start_Jni(int sessionID, String parameter, int pvMode, boolean disableAudio, boolean convertVideo, boolean convertAudio) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchInvalidArgumentException, IchStreamNotSupportException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(start(sessionID, parameter, pvMode, disableAudio, convertVideo, convertAudio));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchInvalidArgumentException ex4) {
            throw ex4;
        } catch (IchStreamNotSupportException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean stop_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(stop(sessionID));
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

    public static boolean enableAudio_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotSupportException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(enableAudio(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotSupportException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean disableAudio_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotSupportException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(disableAudio(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotSupportException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean changePreviewMode_Jni(int sessionID, ICatchPreviewMode previewMode) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotSupportException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(changePreviewMode(sessionID, NativePreviewMode.convertValue(previewMode)));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotSupportException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean containsVideoStream_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(containsVideoStream(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotRunningException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean containsAudioStream_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(containsAudioStream(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotRunningException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static ICatchAudioFormat getAudioFormat_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotRunningException {
        try {
            ICatchAudioFormat audioFormat = DataTypeUtil.toPartialAudioFormat(NativeValueExtractor.extractNativeStringValue(getAudioFormat(sessionID)));
            return audioFormat == null ? null : audioFormat;
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotRunningException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static ICatchVideoFormat getVideoFormat_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotRunningException {
        byte[] csd0 = new byte[ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED];
        byte[] csd1 = new byte[ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED];
        String formatRetStr = getVideoFormat(sessionID);
        String csd0RetStr = getVideoFormatCsdA(sessionID, csd0);
        String csd1RetStr = getVideoFormatCsdB(sessionID, csd1);
        try {
            ICatchVideoFormat videoFormat = DataTypeUtil.toPartialVideoFormat(NativeValueExtractor.extractNativeStringValue(formatRetStr));
            if (videoFormat == null) {
                return null;
            }
            int csd0DataSize = NativeValueExtractor.extractNativeIntValue(csd0RetStr);
            if (csd0DataSize > 0) {
                videoFormat.setCsd_0(csd0, csd0DataSize);
            }
            int csd1DataSize = NativeValueExtractor.extractNativeIntValue(csd1RetStr);
            if (csd1DataSize <= 0) {
                return videoFormat;
            }
            videoFormat.setCsd_1(csd1, csd1DataSize);
            return videoFormat;
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchStreamNotRunningException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return null;
        }
    }

    public static String getNextVideoFrame_Jni(int sessionID, byte[] buffer) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchBufferTooSmallException, IchTryAgainException, IchStreamNotRunningException, IchVideoStreamClosedException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getNextVideoFrame(sessionID, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchTryAgainException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (IchBufferTooSmallException ex5) {
            throw ex5;
        } catch (IchStreamNotRunningException ex6) {
            throw ex6;
        } catch (IchVideoStreamClosedException ex7) {
            throw ex7;
        } catch (Exception ex8) {
            ex8.printStackTrace();
            return null;
        }
    }

    public static String getNextAudioFrame_Jni(int sessionID, byte[] buffer) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchBufferTooSmallException, IchTryAgainException, IchStreamNotRunningException, IchAudioStreamClosedException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getNextAudioFrame(sessionID, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchTryAgainException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (IchBufferTooSmallException ex5) {
            throw ex5;
        } catch (IchStreamNotRunningException ex6) {
            throw ex6;
        } catch (IchAudioStreamClosedException ex7) {
            throw ex7;
        } catch (Exception ex8) {
            ex8.printStackTrace();
            return null;
        }
    }

    public static boolean startSavePreviewStream_Jni(int sessionID, String filePath, String fileName, int fileFormat, boolean saveAudio) throws IchSocketException, IchInvalidSessionException, IchStreamNotRunningException, IchVideoStreamClosedException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(startSavePreviewStream(sessionID, filePath, fileName, fileFormat, saveAudio));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchInvalidSessionException ex2) {
            throw ex2;
        } catch (IchStreamNotRunningException ex3) {
            throw ex3;
        } catch (IchVideoStreamClosedException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean stopSavePreviewStream_Jni(int sessionID) throws IchSocketException, IchInvalidSessionException, IchStreamNotRunningException, IchVideoStreamClosedException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(stopSavePreviewStream(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchInvalidSessionException ex2) {
            throw ex2;
        } catch (IchStreamNotRunningException ex3) {
            throw ex3;
        } catch (IchVideoStreamClosedException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }
}
