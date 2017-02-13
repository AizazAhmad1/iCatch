package com.icatch.wificam.customer.type;

public class ICatchFrameBuffer {
    private byte[] buffer;
    private int frameSize;
    private double presentationTime;

    public ICatchFrameBuffer(int bufferSize) {
        this.buffer = new byte[bufferSize];
    }

    public ICatchFrameBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return this.buffer;
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
