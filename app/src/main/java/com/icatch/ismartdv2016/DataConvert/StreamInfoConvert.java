package com.icatch.ismartdv2016.DataConvert;

import com.icatch.ismartdv2016.Beans.StreamInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import uk.co.senab.photoview.BuildConfig;

public class StreamInfoConvert {
    public static StreamInfo convertToStreamInfoBean(String cmd) {
        StreamInfo streamInfo = new StreamInfo();
        String[] temp;
        if (cmd.contains("FPS")) {
            temp = cmd.split("\\?|&");
            streamInfo.mediaCodecType = temp[0];
            temp[1] = temp[1].replace("W=", BuildConfig.FLAVOR);
            temp[2] = temp[2].replace("H=", BuildConfig.FLAVOR);
            temp[3] = temp[3].replace("BR=", BuildConfig.FLAVOR);
            temp[4] = temp[4].replace("FPS=", BuildConfig.FLAVOR);
            streamInfo.width = Integer.parseInt(temp[1]);
            streamInfo.height = Integer.parseInt(temp[2]);
            streamInfo.bitrate = Integer.parseInt(temp[3]);
            streamInfo.fps = Integer.parseInt(temp[4]);
        } else {
            temp = cmd.split("\\?|&");
            streamInfo.mediaCodecType = temp[0];
            temp[1] = temp[1].replace("W=", BuildConfig.FLAVOR);
            temp[2] = temp[2].replace("H=", BuildConfig.FLAVOR);
            temp[3] = temp[3].replace("BR=", BuildConfig.FLAVOR);
            streamInfo.width = Integer.parseInt(temp[1]);
            streamInfo.height = Integer.parseInt(temp[2]);
            streamInfo.bitrate = Integer.parseInt(temp[3]);
            streamInfo.fps = 30;
        }
        AppLog.i("1111", "streamInfo.width =" + streamInfo.width);
        AppLog.i("1111", "streamInfo.heigh =" + streamInfo.height);
        AppLog.i("1111", "streamInfo.mediaCodecType =" + streamInfo.mediaCodecType);
        AppLog.i("1111", "streamInfo.bitrate =" + streamInfo.bitrate);
        AppLog.i("1111", "streamInfo.fps =" + streamInfo.fps);
        return streamInfo;
    }
}
