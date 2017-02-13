package com.icatch.wificam.customer;

import android.annotation.SuppressLint;
import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.JWificamPreview;
import com.icatch.wificam.core.jni.util.DataTypeUtil;
import com.icatch.wificam.core.jni.util.PartialFrameInfo;
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
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchPreviewMode;
import com.icatch.wificam.customer.type.ICatchStreamParam;
import com.icatch.wificam.customer.type.ICatchVideoFormat;

public class ICatchWificamPreview {
    private int sessionID;

    ICatchWificamPreview(int sessionID) {
        this.sessionID = sessionID;
    }

    public boolean start(ICatchStreamParam param, ICatchPreviewMode pvMode) throws IchSocketException, IchCameraModeException, IchInvalidArgumentException, IchInvalidSessionException, IchStreamNotSupportException {
        CoreLogger.logI("live555media", "start media stream");
        return JWificamPreview.start_Jni(this.sessionID, param.getCmdLineParam(), NativePreviewMode.convertValue(pvMode), false, true, true);
    }

    public boolean start(ICatchStreamParam param, ICatchPreviewMode pvMode, boolean disableAudio) throws IchSocketException, IchCameraModeException, IchInvalidArgumentException, IchInvalidSessionException, IchStreamNotSupportException {
        return JWificamPreview.start_Jni(this.sessionID, param.getCmdLineParam(), NativePreviewMode.convertValue(pvMode), disableAudio, true, true);
    }

    public boolean start(ICatchStreamParam param, ICatchPreviewMode pvMode, boolean disableAudio, boolean convertVideo, boolean convertAudio) throws IchSocketException, IchCameraModeException, IchInvalidArgumentException, IchInvalidSessionException, IchStreamNotSupportException {
        return JWificamPreview.start_Jni(this.sessionID, param.getCmdLineParam(), NativePreviewMode.convertValue(pvMode), disableAudio, convertVideo, convertAudio);
    }

    public boolean stop() throws IchSocketException, IchCameraModeException, IchInvalidSessionException {
        return JWificamPreview.stop_Jni(this.sessionID);
    }

    public boolean enableAudio() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotSupportException {
        return JWificamPreview.enableAudio_Jni(this.sessionID);
    }

    public boolean disableAudio() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotSupportException {
        return JWificamPreview.disableAudio_Jni(this.sessionID);
    }

    public boolean changePreviewMode(ICatchPreviewMode previewMode) throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotSupportException {
        return JWificamPreview.changePreviewMode_Jni(this.sessionID, previewMode);
    }

    public ICatchVideoFormat getVideoFormat() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamPreview.getVideoFormat_Jni(this.sessionID);
    }

    public ICatchAudioFormat getAudioFormat() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamPreview.getAudioFormat_Jni(this.sessionID);
    }

    public boolean containsVideoStream() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamPreview.containsVideoStream_Jni(this.sessionID);
    }

    public boolean containsAudioStream() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchStreamNotRunningException {
        return JWificamPreview.containsAudioStream_Jni(this.sessionID);
    }

    @SuppressLint({"SdCardPath"})
    public boolean getNextVideoFrame(ICatchFrameBuffer buffer) throws IchSocketException, IchBufferTooSmallException, IchCameraModeException, IchInvalidSessionException, IchTryAgainException, IchStreamNotRunningException, IchInvalidArgumentException, IchVideoStreamClosedException {
        if (buffer == null || buffer.getBuffer() == null) {
            throw new IchInvalidArgumentException();
        }
        PartialFrameInfo frameInfo = DataTypeUtil.toPartialFrameInfo(JWificamPreview.getNextVideoFrame_Jni(this.sessionID, buffer.getBuffer()));
        if (frameInfo == null || frameInfo.getFrameSize() <= 0) {
            return false;
        }
        buffer.setFrameSize(frameInfo.getFrameSize());
        buffer.setPresentationTime(frameInfo.getPresentationTime());
        return true;
    }

    public boolean getNextAudioFrame(ICatchFrameBuffer buffer) throws IchSocketException, IchBufferTooSmallException, IchCameraModeException, IchInvalidSessionException, IchTryAgainException, IchStreamNotRunningException, IchInvalidArgumentException, IchAudioStreamClosedException {
        if (buffer == null || buffer.getBuffer() == null) {
            throw new IchInvalidArgumentException();
        }
        PartialFrameInfo frameInfo = DataTypeUtil.toPartialFrameInfo(JWificamPreview.getNextAudioFrame_Jni(this.sessionID, buffer.getBuffer()));
        if (frameInfo == null || frameInfo.getFrameSize() <= 0) {
            return false;
        }
        buffer.setFrameSize(frameInfo.getFrameSize());
        buffer.setPresentationTime(frameInfo.getPresentationTime());
        return true;
    }

    public boolean startSavePreviewStream(String filePath, String fileName, int fileFormat, boolean saveAudio) throws IchSocketException, IchInvalidSessionException, IchStreamNotRunningException, IchVideoStreamClosedException {
        return JWificamPreview.startSavePreviewStream_Jni(this.sessionID, filePath, fileName, fileFormat, saveAudio);
    }

    public boolean stopSavePreviewStream() throws IchSocketException, IchInvalidSessionException, IchStreamNotRunningException, IchVideoStreamClosedException {
        return JWificamPreview.stopSavePreviewStream_Jni(this.sessionID);
    }
}
