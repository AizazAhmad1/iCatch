package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.DataTypeUtil;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchPauseFailedException;
import com.icatch.wificam.customer.exception.IchPbStreamPausedException;
import com.icatch.wificam.customer.exception.IchResumeFailedException;
import com.icatch.wificam.customer.exception.IchSeekFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.icatch.wificam.customer.type.ICatchVideoFormat;

public class JWificamVideoPlayback {
    private static native String containsAudioStream(int i);

    private static native String containsVideoStream(int i);

    private static native String deleteVideoThumbnail(int i, String str);

    private static native String downloadVideoThumbnail(int i, String str, byte[] bArr);

    private static native String getAudioFormat(int i);

    private static native String getLength(int i);

    private static native String getNextAudioFrame(int i, byte[] bArr);

    private static native String getNextVideoFrame(int i, byte[] bArr);

    private static native String getVideoFormat(int i);

    private static native String getVideoFormatCsdA(int i, byte[] bArr);

    private static native String getVideoFormatCsdB(int i, byte[] bArr);

    private static native String pause(int i);

    private static native String play(int i, String str, int i2, boolean z, boolean z2);

    private static native String resume(int i);

    private static native String seek(int i, double d);

    private static native String startThumbnailGet(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native String stop(int i);

    private static native String stopThumbnailGet(int i);

    private static native String trimVideo(int i, String str, int i2, int i3);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static boolean play_Jni(int sessionID, String icatchFile, int cachingSize, boolean disableAudio, boolean fromRemote) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(play(sessionID, icatchFile, cachingSize, disableAudio, fromRemote));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchNoSuchFileException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (Exception ex5) {
            ex5.printStackTrace();
            return false;
        }
    }

    public static boolean stop_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException {
        String content = stop(sessionID);
        CoreLogger.logI("video_pb", "after jni stop, return str: " + content);
        try {
            return NativeValueExtractor.extractNativeBoolValue(content);
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

    public static String getNextVideoFrame_Jni(int sessionID, byte[] buffer) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchBufferTooSmallException, IchTryAgainException, IchStreamNotRunningException, IchVideoStreamClosedException, IchPbStreamPausedException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getNextVideoFrame(sessionID, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchTryAgainException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchPbStreamPausedException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (IchBufferTooSmallException ex6) {
            throw ex6;
        } catch (IchStreamNotRunningException ex7) {
            throw ex7;
        } catch (IchVideoStreamClosedException ex8) {
            throw ex8;
        } catch (Exception ex9) {
            ex9.printStackTrace();
            return null;
        }
    }

    public static String getNextAudioFrame_Jni(int sessionID, byte[] buffer) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchBufferTooSmallException, IchTryAgainException, IchStreamNotRunningException, IchAudioStreamClosedException, IchPbStreamPausedException {
        try {
            return NativeValueExtractor.extractNativeStringValue(getNextAudioFrame(sessionID, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchTryAgainException ex2) {
            throw ex2;
        } catch (IchCameraModeException ex3) {
            throw ex3;
        } catch (IchPbStreamPausedException ex4) {
            throw ex4;
        } catch (IchInvalidSessionException ex5) {
            throw ex5;
        } catch (IchBufferTooSmallException ex6) {
            throw ex6;
        } catch (IchStreamNotRunningException ex7) {
            throw ex7;
        } catch (IchAudioStreamClosedException ex8) {
            throw ex8;
        } catch (Exception ex9) {
            ex9.printStackTrace();
            return null;
        }
    }

    public static boolean startThumbnailGet_Jni(int sessionID, String filename, int width, int height, int q, int startTime, int endTime, int interval) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(startThumbnailGet(sessionID, filename, width, height, q, startTime, endTime, interval));
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

    public static boolean stopThumbnailGet_Jni(int sessionID) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(stopThumbnailGet(sessionID));
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

    public static int downloadVideoThumbnail_Jni(int sessionID, String filePath, byte[] buffer) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchBufferTooSmallException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeIntValue(downloadVideoThumbnail(sessionID, filePath, buffer));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchInvalidSessionException ex3) {
            throw ex3;
        } catch (IchBufferTooSmallException ex4) {
            throw ex4;
        } catch (IchStreamNotRunningException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return -1;
        }
    }

    public static int deleteVideoThumbnail_Jni(int sessionID, String filePath) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeIntValue(deleteVideoThumbnail(sessionID, filePath));
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
            return -1;
        }
    }

    public static boolean trimVideo_Jni(int sessionID, String filename, int startTime, int endTime) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(trimVideo(sessionID, filename, startTime, endTime));
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

    public static double getLength_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeDoubleValue(getLength(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchStreamNotRunningException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            ex4.printStackTrace();
            return -1.0d;
        }
    }

    public static boolean pause_Jni(int sessionID) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchPauseFailedException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(pause(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchPauseFailedException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (IchStreamNotRunningException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean resume_Jni(int sessionID) throws IchInvalidSessionException, IchResumeFailedException, IchSocketException, IchCameraModeException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(resume(sessionID));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchResumeFailedException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (IchStreamNotRunningException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }

    public static boolean seek_Jni(int sessionID, double timeInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchSeekFailedException, IchStreamNotRunningException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(seek(sessionID, timeInSecs));
        } catch (IchSocketException ex) {
            throw ex;
        } catch (IchCameraModeException ex2) {
            throw ex2;
        } catch (IchSeekFailedException ex3) {
            throw ex3;
        } catch (IchInvalidSessionException ex4) {
            throw ex4;
        } catch (IchStreamNotRunningException ex5) {
            throw ex5;
        } catch (Exception ex6) {
            ex6.printStackTrace();
            return false;
        }
    }
}
