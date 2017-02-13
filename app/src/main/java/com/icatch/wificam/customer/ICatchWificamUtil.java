package com.icatch.wificam.customer;

import android.util.Log;
import com.icatch.wificam.core.jni.JWificamUtil;
import com.icatch.wificam.core.util.type.NativeVideoSize;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchVideoSize;
import java.util.LinkedList;
import java.util.List;

public class ICatchWificamUtil {
    public static int convertImageSize(String imageSize) throws IchInvalidArgumentException {
        return JWificamUtil.convertImageSize_Jni(imageSize);
    }

    public static List<Integer> convertImageSizes(List<String> imageSizes) throws IchInvalidArgumentException {
        List<Integer> imageSizesEnum = new LinkedList();
        for (String imageSize : imageSizes) {
            imageSizesEnum.add(Integer.valueOf(convertImageSize(imageSize)));
        }
        return imageSizesEnum;
    }

    public static ICatchVideoSize convertVideoSize(String videoSize) throws IchInvalidArgumentException {
        int sizeInt = JWificamUtil.convertVideoSize_Jni(videoSize);
        Log.e("videoSize", "videoSize " + videoSize + "--> " + sizeInt);
        return NativeVideoSize.convertValue(sizeInt);
    }

    public static List<ICatchVideoSize> convertVideoSizes(List<String> videoSizes) throws IchInvalidArgumentException {
        List<ICatchVideoSize> videoSizesEnum = new LinkedList();
        for (String videoSize : videoSizes) {
            videoSizesEnum.add(convertVideoSize(videoSize));
        }
        return videoSizesEnum;
    }

    public static boolean decodeAAC(ICatchFrameBuffer inuptFrame, ICatchFrameBuffer outputFrame) throws IchInvalidArgumentException {
        int decodedSize = JWificamUtil.decodeAAC_Jni(inuptFrame.getBuffer(), inuptFrame.getFrameSize(), outputFrame.getBuffer());
        if (decodedSize <= 0) {
            return false;
        }
        outputFrame.setFrameSize(decodedSize);
        return true;
    }

    public static boolean decodeJPEG(ICatchFrameBuffer inuptFrame, ICatchFrameBuffer outputFrame) throws IchInvalidArgumentException {
        int decodedSize = JWificamUtil.decodeJPEG_Jni(inuptFrame.getBuffer(), inuptFrame.getFrameSize(), outputFrame.getBuffer());
        if (decodedSize <= 0) {
            return false;
        }
        outputFrame.setFrameSize(decodedSize);
        return true;
    }

    public static boolean convertVideoFile(String originalVideo, String destVideoName) throws IchInvalidArgumentException {
        StringBuilder commandArgs = new StringBuilder();
        commandArgs.append("-i").append(" ");
        commandArgs.append(originalVideo).append(" ");
        commandArgs.append("-vcodec").append(" ");
        commandArgs.append("copy").append(" ");
        commandArgs.append("-acodec").append(" ");
        commandArgs.append("aac").append(" ");
        commandArgs.append("-strict").append(" ");
        commandArgs.append("-2").append(" ");
        commandArgs.append(destVideoName).append(" ");
        return executeFFMPEG(commandArgs.toString());
    }

    public static boolean executeFFMPEG(String commandArgs) throws IchInvalidArgumentException {
        return JWificamUtil.executeFFMPEG_Jni(commandArgs);
    }
}
