package com.icatch.ismartdv2016.Tools.FileOpertion;

import com.icatch.ismartdv2016.Log.AppLog;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MFileTools extends FileTools {
    private static final String tag = "MFileTools";

    public static String getNewestPhotoFromDirectory(String directoryPath) {
        List<File> files = getPhotosOrderByDate(directoryPath);
        if (files == null || files.isEmpty()) {
            AppLog.i(tag, "getNewestPhotoFromDirectory = null");
            return null;
        }
        AppLog.i(tag, "111111111111111");
        AppLog.i(tag, "getNewestPhotoFromDirectory path =" + ((File) files.get(0)).getPath());
        return ((File) files.get(0)).getPath();
    }

    public static String getNewestVideoFromDirectory(String directoryPath) {
        List<File> files = getVideosOrderByDate(directoryPath);
        if (files == null || files.isEmpty()) {
            return null;
        }
        AppLog.i(tag, "getNewestVideoFromDirectory path =" + ((File) files.get(0)).getPath());
        return ((File) files.get(0)).getPath();
    }

    public static List<File> getPhotosOrderByDate(String directoryPath) {
        AppLog.i(tag, "start getPhotosOrderByDate");
        List<File> files = FileTools.getFilesOrderByDate(directoryPath);
        if (files == null || files.isEmpty()) {
            return null;
        }
        AppLog.i(tag, "start 2222222222");
        List<File> tempFiles = new LinkedList();
        for (File f : files) {
            String fileName = f.getName();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".PNG") || fileName.endsWith(".JPG")) {
                tempFiles.add(f);
            }
        }
        AppLog.i(tag, "end getPhotosOrderByDate");
        return tempFiles;
    }

    public static List<File> getVideosOrderByDate(String directoryPath) {
        List<File> files = FileTools.getFilesOrderByDate(directoryPath);
        if (files == null || files.isEmpty()) {
            return null;
        }
        List<File> tempFiles = new LinkedList();
        for (File f : files) {
            String fileName = f.getName();
            if (fileName.endsWith(".MP4") || fileName.endsWith(".wmv") || fileName.endsWith(".mp4") || fileName.endsWith(".3gp") || fileName.endsWith(".MOV") || fileName.endsWith(".mov") || fileName.endsWith(".AVI") || fileName.endsWith(".avi")) {
                tempFiles.add(f);
            }
        }
        return tempFiles;
    }
}
