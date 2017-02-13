package com.icatch.wificam.core.jni.util;

public class PartialFrameInfo {
    private int codec;
    private int frameSize;
    private double presentationTime;

    public int getCodec() {
        return this.codec;
    }

    public void setCodec(int codec) {
        this.codec = codec;
    }

    public int getFrameSize() {
        return this.frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public double getPresentationTime() {
        return this.presentationTime;
    }

    public void setPresentationTime(double presentationTime) {
        this.presentationTime = presentationTime;
    }
}
