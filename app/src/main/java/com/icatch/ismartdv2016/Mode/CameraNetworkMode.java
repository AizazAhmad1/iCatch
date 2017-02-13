package com.icatch.ismartdv2016.Mode;

import android.util.SparseArray;

public final class CameraNetworkMode {
    public static final int AP = 1;
    public static final int ETHERNET = 2;
    public static final int STATION = 0;
    private static SparseArray<String> modeMap = new SparseArray();

    public static String getModeConvert(int mode) {
        if (modeMap.size() == 0) {
            initNetworkModeMap();
        }
        return (String) modeMap.get(mode);
    }

    private static void initNetworkModeMap() {
        modeMap.put(0, "Station");
        modeMap.put(AP, "AP");
        modeMap.put(ETHERNET, "Ethernet");
    }
}
