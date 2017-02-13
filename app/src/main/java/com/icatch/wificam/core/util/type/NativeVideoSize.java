package com.icatch.wificam.core.util.type;

import android.annotation.SuppressLint;
import com.icatch.wificam.customer.type.ICatchVideoSize;
import java.util.HashMap;
import java.util.Map;

public class NativeVideoSize {
    private static final int VIDEO_SIZE_1080P_WITH_30FPS = 3;
    private static final int VIDEO_SIZE_1080P_WITH_60FPS = 4;
    private static final int VIDEO_SIZE_1440P_30FPS = 6;
    private static final int VIDEO_SIZE_640_360_240FPS = 16;
    private static final int VIDEO_SIZE_720P_120FPS = 5;
    private static final int VIDEO_SIZE_720P_WITH_30FPS = 1;
    private static final int VIDEO_SIZE_720P_WITH_60FPS = 2;
    private static final int VIDEO_SIZE_960P_60FPS = 7;
    private static final int VIDEO_SIZE_FULL_30FPS = 10;
    private static final int VIDEO_SIZE_QVGA_240FPS = 9;
    private static final int VIDEO_SIZE_UNDEFINED = 0;
    private static final int VIDEO_SIZE_VGA_120FPS = 8;
    private static Map<Integer, ICatchVideoSize> videoSizeMaps;

    @SuppressLint({"UseSparseArrays"})
    private static void fillVideoSizeMaps() {
        videoSizeMaps = new HashMap();
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_UNDEFINED), ICatchVideoSize.ICH_VIDEO_SIZE_UNDEFINED);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_720P_WITH_30FPS), ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_30FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_720P_WITH_60FPS), ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_60FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_1080P_WITH_30FPS), ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_30FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_1080P_WITH_60FPS), ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_60FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_720P_120FPS), ICatchVideoSize.ICH_VIDEO_SIZE_720P_120FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_1440P_30FPS), ICatchVideoSize.ICH_VIDEO_SIZE_1440P_30FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_960P_60FPS), ICatchVideoSize.ICH_VIDEO_SIZE_960P_60FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_VGA_120FPS), ICatchVideoSize.ICH_VIDEO_SIZE_VGA_120FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_QVGA_240FPS), ICatchVideoSize.ICH_VIDEO_SIZE_QVGA_240FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_FULL_30FPS), ICatchVideoSize.ICH_VIDEO_SIZE_FULL_30FPS);
        videoSizeMaps.put(Integer.valueOf(VIDEO_SIZE_640_360_240FPS), ICatchVideoSize.ICH_VIDEO_SIZE_640_360_240FPS);
    }

    public static ICatchVideoSize convertValue(int value) {
        if (videoSizeMaps == null) {
            fillVideoSizeMaps();
        }
        ICatchVideoSize videoSize = (ICatchVideoSize) videoSizeMaps.get(Integer.valueOf(value));
        return videoSize != null ? videoSize : ICatchVideoSize.ICH_VIDEO_SIZE_UNDEFINED;
    }
}
