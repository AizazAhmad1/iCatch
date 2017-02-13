package com.icatch.wificam.customer.type;

public class ICatchMJPGStreamParam implements ICatchStreamParam {
    private int bitrate;
    private boolean defSize;
    private ICatchFrameSize frameSize;
    private int qSize;

    public ICatchMJPGStreamParam(int width, int height, int bitrate, int qSize) {
        this.frameSize = new ICatchFrameSize(width, height);
        this.bitrate = bitrate;
        this.qSize = qSize;
        this.defSize = true;
    }

    public ICatchMJPGStreamParam(int width, int height) {
        this.frameSize = new ICatchFrameSize(width, height);
        this.bitrate = 5000000;
        this.qSize = 50;
        this.defSize = true;
    }

    public ICatchMJPGStreamParam() {
        this.bitrate = 5000000;
        this.qSize = 50;
        this.defSize = false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IMPL=SDK;");
        builder.append("CODEC=MJPG;");
        builder.append("SIZE=").append(this.defSize ? "true" : "false").append(";");
        if (this.defSize) {
            builder.append("W=").append(this.frameSize.getFrameWidth()).append(";");
            builder.append("H=").append(this.frameSize.getFrameHeight()).append(";");
        }
        builder.append("Q=").append(this.qSize).append(";");
        builder.append("BR=").append(this.bitrate).append(";");
        return builder.toString();
    }

    public String getCmdLineParam() {
        return toString();
    }

    public int getVideoWidth() {
        return this.frameSize.getFrameWidth();
    }

    public int getVideoHeight() {
        return this.frameSize.getFrameHeight();
    }
}
