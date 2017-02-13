package com.icatch.wificam.customer;

import com.icatch.wificam.core.jni.JWificamMediaServer;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

public class ICatchWificamMediaServer {
    private static ICatchWificamMediaServer instance = new ICatchWificamMediaServer();

    private ICatchWificamMediaServer() {
    }

    public static ICatchWificamMediaServer getInstance() {
        return instance;
    }

    public boolean startMediaServer(String localVideoFile) {
        return JWificamMediaServer.startMediaServerA(localVideoFile);
    }

    public boolean startMediaServer(boolean hasVideo, int video_codec, boolean hasAudio, int audio_codec) {
        return startMediaServer(hasVideo, video_codec, hasAudio, audio_codec, 16, 44100, 2);
    }

    public boolean startMediaServer(boolean hasVideo, int video_codec, boolean hasAudio, int audio_codec, int sample_rate, int sample_chnl, int sample_bits) {
        return JWificamMediaServer.startMediaServerB(hasVideo, video_codec, hasAudio, audio_codec, sample_rate, sample_chnl, sample_bits);
    }

    public boolean closeMediaServer() {
        return JWificamMediaServer.closeMediaServer();
    }

    boolean writeVideoFrame(ICatchFrameBuffer videoFrame) {
        return JWificamMediaServer.writeVideoFrame(videoFrame.getBuffer(), videoFrame.getFrameSize(), videoFrame.getPresentationTime());
    }

    public boolean writeAudioFrame(ICatchFrameBuffer audioFrame) {
        return JWificamMediaServer.writeAudioFrame(audioFrame.getBuffer(), audioFrame.getFrameSize(), audioFrame.getPresentationTime());
    }
}
