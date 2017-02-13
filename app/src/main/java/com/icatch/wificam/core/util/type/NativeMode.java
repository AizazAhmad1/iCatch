package com.icatch.wificam.core.util.type;

import android.annotation.SuppressLint;
import com.icatch.wificam.customer.type.ICatchMode;
import java.util.HashMap;
import java.util.Map;

public class NativeMode {
    private static final int ICATCH_MODE_CAMERA = 1;
    private static final int ICATCH_MODE_TIMELAPSE = 3;
    private static final int ICATCH_MODE_UNDEFINED = 65535;
    private static final int ICATCH_MODE_VIDEO = 2;
    private static Map<Integer, ICatchMode> modes;

    @SuppressLint({"UseSparseArrays"})
    private static void fillLightModeMaps() {
        modes = new HashMap();
        modes.put(Integer.valueOf(ICATCH_MODE_CAMERA), ICatchMode.ICH_MODE_CAMERA);
        modes.put(Integer.valueOf(ICATCH_MODE_VIDEO), ICatchMode.ICH_MODE_VIDEO);
        modes.put(Integer.valueOf(ICATCH_MODE_TIMELAPSE), ICatchMode.ICH_MODE_TIMELAPSE);
        modes.put(Integer.valueOf(ICATCH_MODE_UNDEFINED), ICatchMode.ICH_MODE_UNDEFINED);
    }

    public static ICatchMode convertValue(int value) {
        if (modes == null) {
            fillLightModeMaps();
        }
        ICatchMode mode = (ICatchMode) modes.get(Integer.valueOf(value));
        return mode != null ? mode : ICatchMode.ICH_MODE_UNDEFINED;
    }

    public static int convertValue(ICatchMode value) {
        if (modes == null) {
            fillLightModeMaps();
        }
        int intVal = ICATCH_MODE_UNDEFINED;
        for (Integer intValue : modes.keySet()) {
            int key = intValue.intValue();
            if (modes.get(Integer.valueOf(key)) == value) {
                intVal = key;
            }
        }
        return intVal;
    }
}
