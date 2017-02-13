package com.icatch.wificam.customer.type;

public class ICatchByteArray {
    private int dataSize;
    private byte[] value;

    public ICatchByteArray(byte[] value, int dataSize) {
        this.value = value;
        this.dataSize = dataSize;
    }

    public void setValue(byte[] value, int dataSize) {
        this.value = value;
        this.dataSize = dataSize;
    }

    public byte[] getValue() {
        return this.value;
    }

    public int getDataSize() {
        return this.dataSize;
    }
}
