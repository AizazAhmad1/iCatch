package com.icatch.wificam.customer.type;

public class ICatchAudioFormat {
    private int codec;
    private int frequency;
    private int nChannels;
    private int sampleBits;

    public int getCodec() {
        return this.codec;
    }

    public void setCodec(int codec) {
        this.codec = codec;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getSampleBits() {
        return this.sampleBits;
    }

    public void setSampleBits(int sampleBits) {
        this.sampleBits = sampleBits;
    }

    public int getNChannels() {
        return this.nChannels;
    }

    public void setNChannels(int nChannels) {
        this.nChannels = nChannels;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("codec: ").append(this.codec).append(";");
        sb.append("frequency: ").append(this.frequency).append(";");
        sb.append("nChannels: ").append(this.nChannels).append(";");
        sb.append("sampleBits: ").append(this.sampleBits).append(";");
        return sb.toString();
    }
}
