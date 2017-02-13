package com.icatch.ismartdv2016.test;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class test {
    private static int count = 0;
    static File directory = null;
    private static File directory1;
    static String fileName = null;
    private static String fileName1;
    private static long lastTime = 0;
    private static long lastTime11 = 0;
    private static FileOutputStream out = null;
    private static FileOutputStream out1;
    static String path = null;
    private static String path1;
    private static Bitmap videoBitmap;
    private static String writeFile;
    private static String writeFile1;
    private static File writeLogFile = null;

    public static void saveImage(Bitmap bitmap, long time) {
        videoBitmap = bitmap;
        lastTime = System.currentTimeMillis();
        if (path != null) {
            directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        path = Environment.getExternalStorageDirectory().toString() + "/SportCam/Photo/";
        fileName = time + "_count.jpg";
        File file = new File(directory, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile = path + fileName;
        writeLogFile = new File(writeFile);
        Log.d("tigertiger", "writeFile: " + writeFile);
        try {
            out = new FileOutputStream(writeFile, false);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            videoBitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        count++;
    }

    public static void saveImage11(ICatchFrameBuffer buffer, int size) {
        lastTime11 = System.currentTimeMillis();
        if (path != null) {
            directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        path = Environment.getExternalStorageDirectory().toString() + "/bitmapSave11/";
        fileName = System.currentTimeMillis() + "_" + count + ".jpg";
        File file = new File(directory, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile = path + fileName;
        writeLogFile = new File(writeFile);
        Log.d("tigertiger", "writeFile: " + writeFile + " file size=" + size);
        try {
            out = new FileOutputStream(writeFile, false);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            out.write(buffer.getBuffer(), 0, size);
            out.close();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        count++;
    }

    public static void savefile(ICatchFrameBuffer buffer, int size) {
        if (path != null) {
            directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        path = Environment.getExternalStorageDirectory().toString() + "/saveVideo/";
        fileName = "video01.aaa";
        File file = new File(directory, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile = path + fileName;
        writeLogFile = new File(writeFile);
        Log.d("tigertiger", "writeFile: " + writeFile);
        try {
            if (out == null) {
                out = new FileOutputStream(writeFile);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            out.write(buffer.getBuffer(), 0, size);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void saveAduio(ICatchFrameBuffer buffer, int size) {
        if (path1 != null) {
            directory1 = new File(path1);
            if (!directory1.exists()) {
                directory1.mkdirs();
            }
        }
        path1 = Environment.getExternalStorageDirectory().toString() + "/saveAudio/";
        fileName1 = "audio.aaa";
        File file = new File(directory1, fileName1);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile1 = path1 + fileName1;
        Log.d("tigertiger", "writeFile: " + writeFile1);
        try {
            if (out1 == null) {
                out1 = new FileOutputStream(writeFile1);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            out1.write(buffer.getBuffer(), 0, size);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void emptyFolder(String path) {
        String folderPath = Environment.getExternalStorageDirectory().toString() + path;
        if (folderPath != null) {
            File file = new File(folderPath);
            String[] fileNames = file.list();
            if (file.exists() && fileNames.length != 0) {
                for (String str : fileNames) {
                    File temp = new File(folderPath + str);
                    if (temp.isFile()) {
                        temp.delete();
                    }
                }
            }
        }
    }
}
