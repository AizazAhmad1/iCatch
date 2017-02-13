package com.icatch.wificam.customer.type;

public class ICatchVideoFormat {
    private int bitrate;
    private int codec;
    private byte[] csd_0;
    private int csd_0_size;
    private byte[] csd_1;
    private int csd_1_size;
    private int durationUs;
    private int fps;
    private int maxInputSize;
    private String mineType;
    private int videoH;
    private int videoW;

    public String getMineType() {
        return this.mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public int getCodec() {
        return this.codec;
    }

    public void setCodec(int codec) {
        this.codec = codec;
    }

    public int getVideoW() {
        return this.videoW;
    }

    public void setVideoW(int videoW) {
        this.videoW = videoW;
    }

    public int getVideoH() {
        return this.videoH;
    }

    public void setVideoH(int videoH) {
        this.videoH = videoH;
    }

    public int getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getDurationUs() {
        return this.durationUs;
    }

    public void setDurationUs(int durationUs) {
        this.durationUs = durationUs;
    }

    public int getMaxInputSize() {
        return this.maxInputSize;
    }

    public void setMaxInputSize(int maxInputSize) {
        this.maxInputSize = maxInputSize;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getFps() {
        return this.fps;
    }

    public void setCsd_0(byte[] csd_0, int csd_0_size) {
        this.csd_0 = csd_0;
        this.csd_0_size = csd_0_size;
    }

    public void setCsd_1(byte[] csd_1, int csd_1_size) {
        this.csd_1 = csd_1;
        this.csd_1_size = csd_1_size;
    }

    public byte[] getCsd_0() {
        return this.csd_0;
    }

    public byte[] getCsd_1() {
        return this.csd_1;
    }

    public int getCsd_0_size() {
        return this.csd_0_size;
    }

    public int getCsd_1_size() {
        return this.csd_1_size;
    }

    public String toString() {
        int idx;
        StringBuilder sb = new StringBuilder();
        sb.append("mineType: ").append(this.mineType).append(";");
        sb.append("codec: ").append(this.codec).append(";");
        sb.append("frameW: ").append(this.videoW).append(";");
        sb.append("frameH: ").append(this.videoH).append(";");
        sb.append("bitrate: ").append(this.bitrate).append(";");
        sb.append("durationUs: ").append(this.durationUs).append(";");
        sb.append("maxInputSize: ").append(this.maxInputSize).append(";");
        sb.append("csd_0_size: ").append(this.csd_0_size).append(";");
        sb.append("csd_1_size: ").append(this.csd_1_size).append(";");
        sb.append("csd_0: ");
        for (idx = 0; idx < this.csd_0_size; idx++) {
            sb.append(this.csd_0[idx]).append(" ");
        }
        sb.append(";");
        sb.append("csd_1: ");
        for (idx = 0; idx < this.csd_1_size; idx++) {
            sb.append(this.csd_1[idx]).append(" ");
        }
        sb.append(";");
        return sb.toString();
    }
}
