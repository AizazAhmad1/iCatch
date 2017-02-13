package com.icatchtek.bluetooth.core.base;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BluetoothCondition {
    private static final String icatch_bluetooth_tag = BluetoothCondition.class.getSimpleName();
    private Condition condition = this.lock.newCondition();
    private Lock lock = new ReentrantLock();

    public void await(int timeMS) throws IchBluetoothTimeoutException {
        boolean result = false;
        this.lock.lock();
        try {
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "await now: " + timeMS + " milliseconds");
            result = this.condition.await((long) timeMS, TimeUnit.MILLISECONDS);
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "await result: " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lock.unlock();
        if (result) {
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "wait succeed.");
            return;
        }
        throw new IchBluetoothTimeoutException("lock failed, maybe timeout.");
    }

    public void signal() {
        this.lock.lock();
        this.condition.signal();
        this.lock.unlock();
    }
}
