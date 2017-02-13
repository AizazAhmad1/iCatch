package com.icatch.wificam.customer;

import com.icatch.wificam.core.jni.JWificamState;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

public class ICatchWificamState {
    private int sessionID;

    ICatchWificamState(int sessionID) {
        this.sessionID = sessionID;
    }

    public boolean isStreaming() throws IchInvalidSessionException {
        return JWificamState.isStreaming_Jni(this.sessionID);
    }

    public boolean isMovieRecording() throws IchInvalidSessionException {
        return JWificamState.isMovieRecording_Jni(this.sessionID);
    }

    public boolean isMoviePlaying() throws IchInvalidSessionException {
        return JWificamState.isMoviePlaying_Jni(this.sessionID);
    }

    public boolean isTimeLapseStillOn() throws IchInvalidSessionException {
        return JWificamState.isTimeLapseStillOn_Jni(this.sessionID);
    }

    public boolean isTimeLapseVideoOn() throws IchInvalidSessionException {
        return JWificamState.isTimeLapseVideoOn_Jni(this.sessionID);
    }

    public boolean supportImageAutoDownload() throws IchInvalidSessionException {
        return JWificamState.supportImageAutoDownload_Jni(this.sessionID);
    }

    public boolean isCameraBusy() throws IchInvalidSessionException {
        return JWificamState.isCameraBusy_Jni(this.sessionID);
    }
}
