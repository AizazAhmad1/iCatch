package com.icatch.wificam.core.jni.extractor;

public class NativeValueUtil {
    public static int getIntValue(String content) {
        String[] values = content.split(NativeValueTag.SEPARATOR_TAG);
        if (values.length == 2 && values[0].equals(NativeValueTag.RETURN_INT_TAG)) {
            return Integer.parseInt(values[1]);
        }
        return -1;
    }

    public static long getLongValue(String content) {
        String[] values = content.split(NativeValueTag.SEPARATOR_TAG);
        if (values.length == 2 && values[0].equals(NativeValueTag.RETURN_LONG_TAG)) {
            return Long.parseLong(values[1]);
        }
        return -1;
    }

    public static int getErrValue(String content) {
        String[] values = content.split(NativeValueTag.SEPARATOR_TAG);
        if (values.length == 2 && values[0].equals(NativeValueTag.RETURN_ERR_TAG)) {
            return Integer.parseInt(values[1]);
        }
        return -11111;
    }

    public static boolean getBoolValue(String content) {
        String[] values = content.split(NativeValueTag.SEPARATOR_TAG);
        if (values.length != 2 || !values[0].equals(NativeValueTag.RETURN_BOOL_TAG)) {
            return false;
        }
        if (values[1].equals("true")) {
            return true;
        }
        return false;
    }

    public static double getDoubleValue(String content) {
        String[] values = content.split(NativeValueTag.SEPARATOR_TAG);
        if (values.length == 2 && values[0].equals(NativeValueTag.RETURN_DOUBLE_TAG)) {
            return Double.parseDouble(values[1]);
        }
        return -1.0d;
    }

    public static String getStringValue(String content) {
        String[] values = content.split(NativeValueTag.SEPARATOR_TAG);
        if (values.length == 2 && values[0].equals(NativeValueTag.RETURN_STRING_TAG)) {
            return values[1];
        }
        return null;
    }
}
