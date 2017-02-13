package com.icatch.ismartdv2016.Hash;

import com.icatch.ismartdv2016.Android.NoDoubleClickListener;
import com.icatch.ismartdv2016.Beans.ItemInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.ICatchWificamUtil;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import com.icatch.wificam.customer.type.ICatchVideoSize;
import java.util.HashMap;
import java.util.List;

public class PropertyHashMapDynamic {
    private static PropertyHashMapDynamic propertyHashMap;
    private final String tag = "PropertyHashMapDynamic";

    public static PropertyHashMapDynamic getInstance() {
        if (propertyHashMap == null) {
            propertyHashMap = new PropertyHashMapDynamic();
        }
        return propertyHashMap;
    }

    public HashMap<Integer, ItemInfo> getDynamicHashInt(int propertyId) {
        switch (propertyId) {
            case ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY /*20498*/:
                return getCaptureDelayMap();
            default:
                return null;
        }
    }

    public HashMap<String, ItemInfo> getDynamicHashString(int propertyId) {
        switch (propertyId) {
            case ICatchCameraProperty.ICH_CAP_IMAGE_SIZE /*20483*/:
                return getImageSizeMap();
            case ICatchCameraProperty.ICH_CAP_VIDEO_SIZE /*54789*/:
                return getVideoSizeMap();
            default:
                return null;
        }
    }

    private HashMap<Integer, ItemInfo> getCaptureDelayMap() {
        HashMap<Integer, ItemInfo> captureDelayMap = new HashMap();
        List<Integer> delyaList = CameraProperties.getInstance().getSupportedPropertyValues(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY);
        for (int ii = 0; ii < delyaList.size(); ii++) {
            String temp;
            if (((Integer) delyaList.get(ii)).intValue() == 0) {
                temp = "OFF";
            } else {
                temp = (((Integer) delyaList.get(ii)).intValue() / NoDoubleClickListener.MIN_CLICK_DELAY_TIME) + "S";
            }
            AppLog.d("PropertyHashMapDynamic", "delyaList.get(ii) ==" + delyaList.get(ii));
            captureDelayMap.put(delyaList.get(ii), new ItemInfo(temp, temp, 0));
        }
        return captureDelayMap;
    }

    private HashMap<String, ItemInfo> getImageSizeMap() {
        AppLog.i("PropertyHashMapDynamic", "begin initImageSizeMap");
        HashMap<String, ItemInfo> imageSizeMap = new HashMap();
        List<String> imageSizeList = CameraProperties.getInstance().getSupportedImageSizes();
        List<Integer> convertImageSizeList = null;
        try {
            convertImageSizeList = ICatchWificamUtil.convertImageSizes(imageSizeList);
        } catch (IchInvalidArgumentException e) {
            e.printStackTrace();
        }
        String temp = "Undefined";
        String temp1 = "Undefined";
        for (int ii = 0; ii < imageSizeList.size(); ii++) {
            if (((Integer) convertImageSizeList.get(ii)).intValue() == 0) {
                temp = "VGA(" + ((String) imageSizeList.get(ii)) + ")";
                imageSizeMap.put(imageSizeList.get(ii), new ItemInfo(temp, "VGA", 0));
            } else {
                temp = convertImageSizeList.get(ii) + "M" + "(" + ((String) imageSizeList.get(ii)) + ")";
                imageSizeMap.put(imageSizeList.get(ii), new ItemInfo(temp, convertImageSizeList.get(ii) + "M", 0));
            }
            AppLog.i("PropertyHashMapDynamic", "imageSize =" + temp);
        }
        AppLog.i("PropertyHashMapDynamic", "end initImageSizeMap imageSizeMap =" + imageSizeMap.size());
        return imageSizeMap;
    }

    private HashMap<String, ItemInfo> getVideoSizeMap() {
        AppLog.i("PropertyHashMapDynamic", "begin initVideoSizeMap");
        HashMap<String, ItemInfo> videoSizeMap = new HashMap();
        List<String> videoSizeList = CameraProperties.getInstance().getSupportedVideoSizes();
        List<ICatchVideoSize> convertVideoSizeList = null;
        try {
            convertVideoSizeList = ICatchWificamUtil.convertVideoSizes(videoSizeList);
        } catch (IchInvalidArgumentException e) {
            e.printStackTrace();
        }
        for (int ii = 0; ii < convertVideoSizeList.size(); ii++) {
            AppLog.i("PropertyHashMapDynamic", "videoSizeList_" + ii + " = " + ((String) videoSizeList.get(ii)));
            if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_30FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1920x1080 30fps", "FHD30", 0));
            } else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_60FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1920x1080 60fps", "FHD60", 0));
            } else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1440P_30FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1920x1440 30fps", "1440P", 0));
            } else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_120FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x720 120fps", "HD120", 0));
            } else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_30FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x720 30fps", "HD30", 0));
            } else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_60FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x720 60fps", "HD60", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1280x720 50")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x720 50fps", "HD50", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1280x720 25")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x720 25fps", "HD25", 0));
            } else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_960P_60FPS) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x960 60fps", "960P", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1280x960 120")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x960 120fps", "960P", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1280x960 30")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1280x960 30fps", "960P", 0));
            } else if (((String) videoSizeList.get(ii)).equals("640x480 240")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("640x480 240fps", "VGA240", 0));
            } else if (((String) videoSizeList.get(ii)).equals("640x480 120")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("640x480 120fps", "VGA120", 0));
            } else if (((String) videoSizeList.get(ii)).equals("640x480 60")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("640x480 60fps", "VGA60", 0));
            } else if (((String) videoSizeList.get(ii)).equals("640x360 240")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("640x360 240fps", "VGA240", 0));
            } else if (((String) videoSizeList.get(ii)).equals("640x360 120")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("640x360 120fps", "VGA120", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1920x1080 24")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1920x1080 24fps", "FHD24", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1920x1080 50")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1920x1080 50fps", "FHD50", 0));
            } else if (((String) videoSizeList.get(ii)).equals("1920x1080 25")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("1920x1080 25fps", "FHD25", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 60")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 60fps", "4K60", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 50")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 50fps", "4K50", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 25")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 25fps", "4K25", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 24")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 24fps", "4K24", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 30")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 30fps", "4K30", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 15")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 15fps", "4K15", 0));
            } else if (((String) videoSizeList.get(ii)).equals("3840x2160 10")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("3840x2160 10fps", "4K10", 0));
            } else if (((String) videoSizeList.get(ii)).equals("2704x1524 30")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("2704x1524 30fps", "2.7K30", 0));
            } else if (((String) videoSizeList.get(ii)).equals("2704x1524 15")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("2704x1524 15fps", "2.7K15", 0));
            } else if (((String) videoSizeList.get(ii)).equals("2704x1524 60")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("2704x1524 60fps", "2.7K60", 0));
            } else if (((String) videoSizeList.get(ii)).equals("2704x1524 50")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("2704x1524 50fps", "2.7K50", 0));
            } else if (((String) videoSizeList.get(ii)).equals("2704x1524 25")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("2704x1524 25fps", "2.7K25", 0));
            } else if (((String) videoSizeList.get(ii)).equals("2704x1524 24")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("2704x1524 24fps", "2.7K24", 0));
            } else if (((String) videoSizeList.get(ii)).equals("848x480 240")) {
                videoSizeMap.put(videoSizeList.get(ii), new ItemInfo("848x480 240fps", "WVGA", 0));
            }
        }
        AppLog.i("PropertyHashMapDynamic", "end initVideoSizeMap videoSizeList=" + videoSizeList.size());
        return videoSizeMap;
    }
}
