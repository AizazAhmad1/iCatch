package com.icatch.wificam.core.jni.util;

import android.os.Build;
import android.util.Log;
import com.icatch.wificam.core.jni.JNativeEventsUtil;
import com.icatch.wificam.core.util.SDKEventHandleAPI;

public class NativeLibraryLoader {
    private static boolean bLoaded = false;
    private static final String intelArch = "x86";
    private static final String[] neonArmArchArray = new String[]{"armv7a", "armeabi-v7a"};

    private static boolean doSupportNeonArmArch(String armArch) {
        for (String neonArmArch : neonArmArchArray) {
            if (neonArmArch.contains(armArch)) {
                return true;
            }
        }
        return false;
    }

    public static void loadLibrary() {
        if (!bLoaded) {
            loadLibrary_1();
            bLoaded = true;
            JNativeEventsUtil.initNativeEventsUtil_Jni();
            Log.i("SDKEventHandleAPI", "intance: " + SDKEventHandleAPI.getInstance());
        }
    }

    private static void loadLibrary_1() {
        String cpuAbi = Build.CPU_ABI;
        if (cpuAbi.contains(intelArch)) {
            System.loadLibrary("icatch_wificam_sdk");
        } else if (!cpuAbi.contains("arm")) {
            System.load("icatch_wificam_sdk");
        } else if (doSupportNeonArmArch(cpuAbi)) {
            System.loadLibrary("icatch_wificam_sdk");
        } else {
            System.loadLibrary("icatch_wificam_sdk");
        }
    }
}
