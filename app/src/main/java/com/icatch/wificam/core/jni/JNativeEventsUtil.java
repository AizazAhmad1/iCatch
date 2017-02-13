package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;

public class JNativeEventsUtil {
    private static native String initNativeEventsUtil();

    private static native String receiveOneNativeEvent();

    private static native String uninitNativeEventsUtil();

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static boolean initNativeEventsUtil_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(initNativeEventsUtil());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean uninitNativeEventsUtil_Jni() {
        try {
            return NativeValueExtractor.extractNativeBoolValue(uninitNativeEventsUtil());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String receiveOneNativeEvent_Jni() {
        try {
            return NativeValueExtractor.extractNativeStringValue(receiveOneNativeEvent());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
