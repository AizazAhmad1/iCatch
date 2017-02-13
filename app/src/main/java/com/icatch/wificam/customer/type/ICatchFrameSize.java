package com.icatch.wificam.customer.type;

public class ICatchFrameSize {
    private int frameHeight;
    private int frameWidth;

    public ICatchFrameSize(int width, int height) {
        this.frameWidth = width;
        this.frameHeight = height;
    }

    public int getFrameWidth() {
        return this.frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return this.frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }
}
