package com.icatch.ismartdv2016.Listener;

public interface Callback {
    void processAbnormal();

    void processFailed();

    void processSucceed();
}
