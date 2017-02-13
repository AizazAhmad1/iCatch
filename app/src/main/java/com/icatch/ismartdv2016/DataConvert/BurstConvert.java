package com.icatch.ismartdv2016.DataConvert;

import android.annotation.SuppressLint;
import android.util.SparseArray;

public class BurstConvert {
    private static BurstConvert burstConvert;
    @SuppressLint({"UseSparseArrays"})
    private SparseArray<Integer> burstMap = new SparseArray();

    public static BurstConvert getInstance() {
        if (burstConvert == null) {
            burstConvert = new BurstConvert();
        }
        return burstConvert;
    }

    public BurstConvert() {
        initBurstMap();
    }

    private void initBurstMap() {
        this.burstMap.put(0, Integer.valueOf(0));
        this.burstMap.put(1, Integer.valueOf(1));
        this.burstMap.put(2, Integer.valueOf(3));
        this.burstMap.put(3, Integer.valueOf(5));
        this.burstMap.put(4, Integer.valueOf(10));
    }

    public int getBurstConverFromFw(int fwValue) {
        if (fwValue < 0 || fwValue > 4) {
            return 0;
        }
        return ((Integer) this.burstMap.get(fwValue)).intValue();
    }
}
