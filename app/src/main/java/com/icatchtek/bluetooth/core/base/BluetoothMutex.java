package com.icatchtek.bluetooth.core.base;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BluetoothMutex {
    private static final String icatch_bluetooth_tag = BluetoothMutex.class.getSimpleName();
    private Lock lock = new ReentrantLock();

    public void lock() throws IchBluetoothTimeoutException {
        try {
            this.lock.lockInterruptibly();
        } catch (InterruptedException e) {
            throw new IchBluetoothTimeoutException("lock failed, maybe timeout.");
        }
    }

    public boolean lock_1() {
        try {
            lock();
            return true;
        } catch (IchBluetoothTimeoutException e) {
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "lock failed, maybe timeout.");
            return false;
        }
    }

    public void unlock() {
        this.lock.unlock();
    }
}
