package com.icatch.wificam.core.util.type;

import android.annotation.SuppressLint;
import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import uk.co.senab.photoview.BuildConfig;

public class NativeFile {
    private static final int TYPE_ALL = 15;
    private static final int TYPE_AUDIO = 4;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_TEXT = 8;
    private static final int TYPE_UNKNOWN = 16;
    private static final int TYPE_VIDEO = 2;
    private static Map<Integer, ICatchFileType> types;

    @SuppressLint({"UseSparseArrays"})
    private static void fillFileTypes() {
        types = new HashMap();
        types.put(Integer.valueOf(TYPE_IMAGE), ICatchFileType.ICH_TYPE_IMAGE);
        types.put(Integer.valueOf(TYPE_VIDEO), ICatchFileType.ICH_TYPE_VIDEO);
        types.put(Integer.valueOf(TYPE_AUDIO), ICatchFileType.ICH_TYPE_AUDIO);
        types.put(Integer.valueOf(TYPE_TEXT), ICatchFileType.ICH_TYPE_TEXT);
        types.put(Integer.valueOf(TYPE_ALL), ICatchFileType.ICH_TYPE_ALL);
        types.put(Integer.valueOf(TYPE_UNKNOWN), ICatchFileType.ICH_TYPE_UNKNOWN);
    }

    public static List<ICatchFile> toIcatchFiles(String ntvfilesStr) {
        List<ICatchFile> files = new LinkedList();
        if (ntvfilesStr != null) {
            String[] fileObjStrs = ntvfilesStr.split(";");
            int length = fileObjStrs.length;
            for (int i = 0; i < length; i += TYPE_IMAGE) {
                files.add(toICatchFile(fileObjStrs[i]));
            }
        }
        return files;
    }

    public static ICatchFile toICatchFile(String substring) {
        int fileHandle = -1;
        String filePath = BuildConfig.FLAVOR;
        String fileName = BuildConfig.FLAVOR;
        String fileDate = BuildConfig.FLAVOR;
        long fileSize = -1;
        double frameRate = 0.0d;
        int fileWidth = 0;
        int fileHeight = 0;
        ICatchFileType fileType = ICatchFileType.ICH_TYPE_UNKNOWN;
        String[] fileAttrs = substring.split(",");
        int length = fileAttrs.length;
        for (int i = 0; i < length; i += TYPE_IMAGE) {
            String[] keyVal = fileAttrs[i].split("=");
            if (keyVal.length == TYPE_VIDEO) {
                if (keyVal[0].equals("handle")) {
                    fileHandle = Integer.parseInt(keyVal[TYPE_IMAGE]);
                } else if (keyVal[0].equals("path")) {
                    filePath = keyVal[TYPE_IMAGE];
                } else if (keyVal[0].equals("name")) {
                    fileName = keyVal[TYPE_IMAGE];
                } else if (keyVal[0].equals("date")) {
                    fileDate = keyVal[TYPE_IMAGE];
                } else if (keyVal[0].equals("type")) {
                    fileType = convertValue(Integer.parseInt(keyVal[TYPE_IMAGE]));
                } else if (keyVal[0].equals("size")) {
                    fileSize = Long.parseLong(keyVal[TYPE_IMAGE]);
                } else if (keyVal[0].equals("framerate")) {
                    frameRate = Double.parseDouble(keyVal[TYPE_IMAGE]);
                } else if (keyVal[0].equals("width")) {
                    fileWidth = Integer.parseInt(keyVal[TYPE_IMAGE]);
                } else if (keyVal[0].equals("height")) {
                    fileHeight = Integer.parseInt(keyVal[TYPE_IMAGE]);
                }
            }
        }
        CoreLogger.logI("toIcatchFile", "fileHandle: " + fileHandle);
        CoreLogger.logI("toIcatchFile", "filePath: " + filePath);
        CoreLogger.logI("toIcatchFile", "fileName: " + fileName);
        CoreLogger.logI("toIcatchFile", "fileDate: " + fileDate);
        CoreLogger.logI("toIcatchFile", "fileType: " + fileType);
        CoreLogger.logI("toIcatchFile", "fileSize: " + fileSize);
        CoreLogger.logI("toIcatchFile", "framerate: " + frameRate);
        CoreLogger.logI("toIcatchFile", "fileWidth: " + fileWidth);
        CoreLogger.logI("toIcatchFile", "fileHeight: " + fileHeight);
        if (fileHandle < 0) {
            return null;
        }
        return new ICatchFile(fileHandle, fileType, filePath, fileName, fileSize, fileDate, frameRate, fileWidth, fileHeight);
    }

    public static String toICatchFile(ICatchFile file) {
        String filePath = file.getFilePath();
        String fileName = file.getFileName();
        String fileDate = file.getFileDate();
        ICatchFileType fileType = file.getFileType();
        StringBuilder sb = new StringBuilder();
        sb.append("handle=").append(file.getFileHandle()).append(",");
        StringBuilder append = sb.append("path=");
        if (filePath == null) {
            filePath = BuildConfig.FLAVOR;
        }
        append.append(filePath).append(",");
        append = sb.append("name=");
        if (fileName == null) {
            fileName = BuildConfig.FLAVOR;
        }
        append.append(fileName).append(",");
        append = sb.append("date=");
        if (fileDate == null) {
            fileDate = BuildConfig.FLAVOR;
        }
        append.append(fileDate).append(",");
        sb.append("type=").append(convertValue(fileType)).append(",");
        sb.append("size=").append(file.getFileSize()).append(",");
        sb.append("framerate=").append(file.getFrameRate()).append(",");
        sb.append("width=").append(file.getFileWidth()).append(",");
        sb.append("height=").append(file.getFileHeight()).append(",");
        return sb.toString();
    }

    public static ICatchFileType convertValue(int value) {
        if (types == null) {
            fillFileTypes();
        }
        ICatchFileType type = (ICatchFileType) types.get(Integer.valueOf(value));
        return type != null ? type : ICatchFileType.ICH_TYPE_UNKNOWN;
    }

    public static int convertValue(ICatchFileType value) {
        if (types == null) {
            fillFileTypes();
        }
        int intVal = TYPE_UNKNOWN;
        for (Integer intValue : types.keySet()) {
            int key = intValue.intValue();
            if (types.get(Integer.valueOf(key)) == value) {
                intVal = key;
            }
        }
        return intVal;
    }
}
