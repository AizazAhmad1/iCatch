package com.icatch.wificam.customer;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.JWificamVideoPlayback;
import com.icatch.wificam.core.jni.util.DataTypeUtil;
import com.icatch.wificam.core.jni.util.PartialFrameInfo;
import com.icatch.wificam.core.util.type.NativeFile;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
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
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchVideoFormat;

public class ICatchWificamVideoPlayback {
    private int sessionID;

    public ICatchWificamVideoPlayback(int sessionID) {
        this.sessionID = sessionID;
    }

    public boolean play(ICatchFile file) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException {
        if (file == null) {
            throw new IchNoSuchFileException();
        }
        return JWificamVideoPlayback.play_Jni(this.sessionID, NativeFile.toICatchFile(file), 0, false, true);
    }

    public boolean play(ICatchFile file, boolean disableAudio) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException {
        String icatchFile = NativeFile.toICatchFile(file);
        CoreLogger.logI("media_api", "disableAudio: " + disableAudio);
        return JWificamVideoPlayback.play_Jni(this.sessionID, icatchFile, 0, disableAudio, true);
    }

    public boolean play(ICatchFile file, boolean disableAudio, boolean fromRemote) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchNoSuchFileException {
        if (file == null) {
            throw new IchNoSuchFileException();
        }
        return JWificamVideoPlayback.play_Jni(this.sessionID, NativeFile.toICatchFile(file), 0, disableAudio, fromRemote);
    }

    public boolean stop() throws IchInvalidSessionException, IchSocketException, IchCameraModeException {
        return JWificamVideoPlayback.stop_Jni(this.sessionID);
    }

    public ICatchVideoFormat getVideoFormat() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamVideoPlayback.getVideoFormat_Jni(this.sessionID);
    }

    public ICatchAudioFormat getAudioFormat() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamVideoPlayback.getAudioFormat_Jni(this.sessionID);
    }

    public boolean containsVideoStream() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamVideoPlayback.containsVideoStream_Jni(this.sessionID);
    }

    public boolean containsAudioStream() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamVideoPlayback.containsAudioStream_Jni(this.sessionID);
    }

    public boolean getNextVideoFrame(ICatchFrameBuffer buffer) throws IchBufferTooSmallException, IchTryAgainException, IchStreamNotRunningException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchInvalidArgumentException, IchVideoStreamClosedException, IchPbStreamPausedException {
        if (buffer == null || buffer.getBuffer() == null) {
            throw new IchInvalidArgumentException();
        }
        PartialFrameInfo frameInfo = DataTypeUtil.toPartialFrameInfo(JWificamVideoPlayback.getNextVideoFrame_Jni(this.sessionID, buffer.getBuffer()));
        if (frameInfo == null || frameInfo.getFrameSize() <= 0) {
            return false;
        }
        buffer.setFrameSize(frameInfo.getFrameSize());
        buffer.setPresentationTime(frameInfo.getPresentationTime());
        return true;
    }

    public boolean getNextAudioFrame(ICatchFrameBuffer buffer) throws IchBufferTooSmallException, IchTryAgainException, IchStreamNotRunningException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchInvalidArgumentException, IchAudioStreamClosedException, IchPbStreamPausedException {
        if (buffer == null || buffer.getBuffer() == null) {
            throw new IchInvalidArgumentException();
        }
        PartialFrameInfo frameInfo = DataTypeUtil.toPartialFrameInfo(JWificamVideoPlayback.getNextAudioFrame_Jni(this.sessionID, buffer.getBuffer()));
        if (frameInfo == null || frameInfo.getFrameSize() <= 0) {
            return false;
        }
        buffer.setFrameSize(frameInfo.getFrameSize());
        buffer.setPresentationTime(frameInfo.getPresentationTime());
        return true;
    }

    public boolean startThumbnailGet(String filename, int width, int height, int q, int startTime, int endTime, int interval) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException, IchAudioStreamClosedException, IchBufferTooSmallException, IchPbStreamPausedException, IchTryAgainException {
        return JWificamVideoPlayback.startThumbnailGet_Jni(this.sessionID, filename, width, height, q, startTime, endTime, interval);
    }

    public boolean stopThumbnailGet() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException, IchAudioStreamClosedException, IchBufferTooSmallException, IchPbStreamPausedException, IchTryAgainException {
        return JWificamVideoPlayback.stopThumbnailGet_Jni(this.sessionID);
    }

    public long downloadVideoThumbnail(String filePath, byte[] buffer) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException, IchBufferTooSmallException {
        return (long) JWificamVideoPlayback.downloadVideoThumbnail_Jni(this.sessionID, filePath, buffer);
    }

    public long deleteVideoThumbnail(String filePath) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return (long) JWificamVideoPlayback.deleteVideoThumbnail_Jni(this.sessionID, filePath);
    }

    public boolean trimVideo(String filename, int startTime, int endTime) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException, IchAudioStreamClosedException, IchBufferTooSmallException, IchPbStreamPausedException, IchTryAgainException {
        return JWificamVideoPlayback.trimVideo_Jni(this.sessionID, filename, startTime, endTime);
    }

    public double getLength() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamVideoPlayback.getLength_Jni(this.sessionID);
    }

    public boolean pause() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchPauseFailedException, IchStreamNotRunningException {
        return JWificamVideoPlayback.pause_Jni(this.sessionID);
    }

    public boolean resume() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchResumeFailedException, IchStreamNotRunningException {
        return JWificamVideoPlayback.resume_Jni(this.sessionID);
    }

    public boolean seek(double timeInSecs) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchSeekFailedException, IchStreamNotRunningException {
        return JWificamVideoPlayback.seek_Jni(this.sessionID, timeInSecs);
    }
}
