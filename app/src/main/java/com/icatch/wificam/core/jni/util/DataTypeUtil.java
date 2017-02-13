package com.icatch.wificam.core.jni.util;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchCodec;
import com.icatch.wificam.customer.type.ICatchPhotoExif;
import com.icatch.wificam.customer.type.ICatchVideoFormat;
import java.util.LinkedList;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class DataTypeUtil {
    public static ICatchPhotoExif toPhotoExif(String exifStr) {
        return null;
    }

    public static List<Integer> splitStringToIntList(String value) {
        if (value == null) {
            return new LinkedList();
        }
        String[] intValStrs = value.split(";");
        List<Integer> intVals = new LinkedList();
        for (String intValStr : intValStrs) {
            intVals.add(Integer.valueOf(Integer.parseInt(intValStr)));
        }
        return intVals;
    }

    public static List<ICatchVideoFormat> splitStringToVideoFormatList(String value) {
        if (value == null) {
            return new LinkedList();
        }
        List<ICatchVideoFormat> videoFormats = new LinkedList();
        for (String videoFormatValStr : value.split(";")) {
            ICatchVideoFormat videoFormat = toVideoFormat(videoFormatValStr);
            if (videoFormat != null) {
                videoFormats.add(videoFormat);
            }
        }
        return videoFormats;
    }

    public static List<String> splitStringToStringList(String value) {
        if (value == null) {
            return new LinkedList();
        }
        String[] strValStrs = value.split(";");
        List<String> stringVals = new LinkedList();
        for (String stringVal : strValStrs) {
            stringVals.add(stringVal);
        }
        return stringVals;
    }

    public static PartialFrameInfo toPartialFrameInfo(String value) {
        if (value == null) {
            return null;
        }
        int codec = 0;
        int frameSize = 0;
        double presentationTime = 0.0d;
        for (String strVal : value.split(";")) {
            String[] keyVal = strVal.split("=");
            if (keyVal.length == 2) {
                if (keyVal[0].equals("codec")) {
                    codec = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("frameSize")) {
                    frameSize = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("presentationTime")) {
                    presentationTime = Double.parseDouble(keyVal[1]);
                }
            }
        }
        PartialFrameInfo frameInfo = new PartialFrameInfo();
        frameInfo.setCodec(codec);
        frameInfo.setFrameSize(frameSize);
        frameInfo.setPresentationTime(presentationTime);
        return frameInfo;
    }

    public static ICatchVideoFormat toPartialVideoFormat(String value) {
        return toPartialVideoFormat(value, ";");
    }

    public static ICatchVideoFormat toVideoFormat(String value) {
        return toPartialVideoFormat(value, ",");
    }

    public static String toVideoFormat(ICatchVideoFormat videoFormat) {
        StringBuilder videoFormatS = new StringBuilder();
        videoFormatS.append("mineType=").append(videoFormat.getMineType()).append(",");
        videoFormatS.append("codec=").append(videoFormat.getCodec()).append(",");
        videoFormatS.append("videoW=").append(videoFormat.getVideoW()).append(",");
        videoFormatS.append("videoH=").append(videoFormat.getVideoH()).append(",");
        videoFormatS.append("bitrate=").append(videoFormat.getBitrate()).append(",");
        videoFormatS.append("durationUs=").append(videoFormat.getDurationUs()).append(",");
        videoFormatS.append("maxInputSize=").append(videoFormat.getMaxInputSize()).append(",");
        return videoFormatS.toString();
    }

    public static ICatchVideoFormat toPartialVideoFormat(String value, String seperator) {
        if (value == null) {
            return null;
        }
        String mineType = BuildConfig.FLAVOR;
        int codec = ICatchCodec.ICH_CODEC_UNKNOWN;
        int videoW = 0;
        int videoH = 0;
        int bitrate = 0;
        int durationUs = 0;
        int maxInputSize = 0;
        int fps = 0;
        CoreLogger.logI("DtaTypeUtil", "value: " + value);
        for (String strVal : value.split(seperator)) {
            String[] keyVal = strVal.split("=");
            CoreLogger.logI("DtaTypeUtil", "strVal: " + strVal);
            CoreLogger.logI("DtaTypeUtil", "keyVal: " + keyVal + "; len: " + keyVal.length);
            if (keyVal.length == 2) {
                if (keyVal[0].equals("mineType")) {
                    mineType = keyVal[1];
                } else if (keyVal[0].equals("codec")) {
                    codec = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("videoW")) {
                    videoW = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("videoH")) {
                    videoH = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("bitrate")) {
                    bitrate = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("durationUs")) {
                    durationUs = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("maxInputSize")) {
                    maxInputSize = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("fps")) {
                    fps = Integer.parseInt(keyVal[1]);
                }
            }
        }
        CoreLogger.logI("DtaTypeUtil", "mineType: " + mineType);
        CoreLogger.logI("DtaTypeUtil", "codec: " + codec);
        CoreLogger.logI("DtaTypeUtil", "videoW: " + videoW);
        CoreLogger.logI("DtaTypeUtil", "videoH: " + videoH);
        CoreLogger.logI("DtaTypeUtil", "bitrate: " + bitrate);
        CoreLogger.logI("DtaTypeUtil", "durationUs: " + durationUs);
        CoreLogger.logI("DtaTypeUtil", "maxInputSize: " + maxInputSize);
        CoreLogger.logI("DtaTypeUtil", "fps: " + fps);
        ICatchVideoFormat videoFormat = new ICatchVideoFormat();
        videoFormat.setMineType(mineType);
        videoFormat.setCodec(codec);
        videoFormat.setVideoW(videoW);
        videoFormat.setVideoH(videoH);
        videoFormat.setBitrate(bitrate);
        videoFormat.setDurationUs(durationUs);
        videoFormat.setMaxInputSize(maxInputSize);
        videoFormat.setFps(fps);
        return videoFormat;
    }

    public static ICatchAudioFormat toPartialAudioFormat(String value) {
        if (value == null) {
            return null;
        }
        int codec = ICatchCodec.ICH_CODEC_UNKNOWN;
        int frequency = 44100;
        int nChannels = 2;
        int sampleBits = 16;
        CoreLogger.logI("DtaTypeUtil", "value: " + value);
        for (String strVal : value.split(";")) {
            String[] keyVal = strVal.split("=");
            CoreLogger.logI("DtaTypeUtil", "strVal: " + strVal);
            CoreLogger.logI("DtaTypeUtil", "keyVal: " + keyVal + "; len: " + keyVal.length);
            if (keyVal.length == 2) {
                if (keyVal[0].equals("codec")) {
                    codec = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("frequency")) {
                    frequency = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("nChannels")) {
                    nChannels = Integer.parseInt(keyVal[1]);
                } else if (keyVal[0].equals("sampleBits")) {
                    sampleBits = Integer.parseInt(keyVal[1]);
                }
            }
        }
        CoreLogger.logI("DtaTypeUtil", "codec: " + codec);
        CoreLogger.logI("DtaTypeUtil", "frequency: " + frequency);
        CoreLogger.logI("DtaTypeUtil", "nChannels: " + nChannels);
        CoreLogger.logI("DtaTypeUtil", "sampleBits: " + sampleBits);
        ICatchAudioFormat audioFormat = new ICatchAudioFormat();
        audioFormat.setCodec(codec);
        audioFormat.setFrequency(frequency);
        audioFormat.setNChannels(nChannels);
        audioFormat.setSampleBits(sampleBits);
        return audioFormat;
    }
}
