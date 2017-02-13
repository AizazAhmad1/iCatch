package com.icatch.ismartdv2016.Tools;

import com.icatch.ismartdv2016.Log.AppLog;
import java.text.DecimalFormat;
import uk.co.senab.photoview.BuildConfig;

public class ConvertTools {
    static double GB = 1.073741824E9d;
    static double KB = 1024.0d;
    static double MB = 1048576.0d;
    private static String TAG = "ConvertTools";

    public static String secondsToMinuteOrHours(int remainTime) {
        String time = BuildConfig.FLAVOR;
        if (remainTime < 0) {
            return "--:--:--";
        }
        Integer h = Integer.valueOf(remainTime / 3600);
        Integer m = Integer.valueOf((remainTime % 3600) / 60);
        Integer s = Integer.valueOf(remainTime % 60);
        if (h.intValue() > 0) {
            if (h.intValue() < 10) {
                time = "0" + h.toString();
            } else {
                time = h.toString();
            }
            time = time + ":";
        }
        if (m.intValue() < 10) {
            time = time + "0" + m.toString();
        } else {
            time = time + m.toString();
        }
        time = time + ":";
        if (s.intValue() < 10) {
            time = time + "0" + s.toString();
        } else {
            time = time + s.toString();
        }
        return time;
    }

    public static String secondsToHours(int remainTime) {
        String time = BuildConfig.FLAVOR;
        Integer h = Integer.valueOf(remainTime / 3600);
        Integer m = Integer.valueOf((remainTime % 3600) / 60);
        Integer s = Integer.valueOf(remainTime % 60);
        if (h.intValue() < 10) {
            time = "0" + h.toString();
        } else {
            time = h.toString();
        }
        time = time + ":";
        if (m.intValue() < 10) {
            time = time + "0" + m.toString();
        } else {
            time = time + m.toString();
        }
        time = time + ":";
        if (s.intValue() < 10) {
            return time + "0" + s.toString();
        }
        return time + s.toString();
    }

    public static String ByteConversionGBMBKB(long KSize) {
        DecimalFormat df = new DecimalFormat("######0.0");
        if (((double) KSize) / GB >= 1.0d) {
            return df.format(((double) KSize) / GB).toString() + "G";
        }
        if (((double) KSize) / MB >= 1.0d) {
            return df.format(((double) KSize) / MB).toString() + "M";
        }
        if (((double) KSize) / KB >= 1.0d) {
            return df.format(((double) KSize) / KB).toString() + "K";
        }
        return String.valueOf(KSize) + "B";
    }

    public static String resolutionConvert(String resolution) {
        AppLog.d(TAG, "start resolution = " + resolution);
        String[] temp = resolution.split("\\?|&");
        temp[1] = temp[1].replace("W=", BuildConfig.FLAVOR);
        temp[2] = temp[2].replace("H=", BuildConfig.FLAVOR);
        temp[3] = temp[3].replace("BR=", BuildConfig.FLAVOR);
        String ret = temp[0] + "?W=" + temp[1] + "&H=" + temp[2] + "&BR=" + temp[3];
        if (!resolution.contains("FPS")) {
            ret = resolution;
        } else if (temp[2].equals("720")) {
            ret = ret + "&FPS=15&";
        } else if (temp[2].equals("1080")) {
            ret = ret + "&FPS=10&";
        } else {
            ret = resolution;
        }
        AppLog.d(TAG, "end ret = " + ret);
        return ret;
    }

    public static String getTimeByfileDate(String fileDate) {
        if (fileDate == null || !fileDate.contains("T")) {
            return null;
        }
        fileDate = fileDate.substring(0, fileDate.indexOf("T"));
        AppLog.d(TAG, "getTimeByfileDate fileDate=[" + fileDate + "]");
        return fileDate;
    }
}
