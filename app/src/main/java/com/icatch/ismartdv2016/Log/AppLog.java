package com.icatch.ismartdv2016.Log;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import uk.co.senab.photoview.BuildConfig;

public class AppLog {
    private static boolean enableLog = false;
    private static boolean hasConfiguration = false;
    private static final long maxFileSize = 52428800;
    private static FileOutputStream out = null;
    private static String writeFile;
    private static File writeLogFile = null;

    public static void enableAppLog() {
        enableLog = true;
        initConfiguration();
    }

    private static void initConfiguration() {
        File directory = null;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH@mm@ss", Locale.CHINA);
        String path = Environment.getExternalStorageDirectory().toString() + "/IcatchSportCamera_APP_Log/";
        if (path != null) {
            directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        String fileName = sdf.format(date) + ".log";
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
        if (out != null) {
            closeWriteStream();
        }
        try {
            out = new FileOutputStream(writeFile);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        hasConfiguration = true;
        i(BuildConfig.FLAVOR, sdf.format(date) + "\n");
        i(BuildConfig.FLAVOR, "R1.4.7.2\n");
    }

    public static String getSystemDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss\t").format(new Date(System.currentTimeMillis()));
    }

    public static void e(String tag, String conent) {
        if (enableLog) {
            if (!hasConfiguration) {
                initConfiguration();
            }
            if (writeLogFile.length() >= maxFileSize) {
                initConfiguration();
            }
            String temp = "[" + tag + "]" + getSystemDate() + ": " + "AppError:" + conent + "\n";
            Log.i("tigertiger", temp);
            try {
                if (out != null) {
                    out.write(temp.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void i(String tag, String conent) {
        if (enableLog) {
            if (!hasConfiguration) {
                initConfiguration();
            }
            if (writeLogFile.length() >= maxFileSize) {
                initConfiguration();
            }
            String temp = getSystemDate() + " " + "AppInfo:" + "[" + tag + "]" + conent + "\n";
            Log.i("tigertiger", temp);
            try {
                if (out != null) {
                    out.write(temp.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void w(String tag, String conent) {
        if (enableLog) {
            if (!hasConfiguration) {
                initConfiguration();
            }
            if (writeLogFile.length() >= maxFileSize) {
                initConfiguration();
            }
            String temp = "[" + tag + "]" + getSystemDate() + ": " + "AppWarning:" + conent + "\n";
            try {
                if (out != null) {
                    out.write(temp.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void d(String tag, String conent) {
        if (enableLog) {
            if (!hasConfiguration) {
                initConfiguration();
            }
            if (writeLogFile.length() >= maxFileSize) {
                initConfiguration();
            }
            String temp = "[" + tag + "]" + getSystemDate() + ":" + "AppDebug:" + conent + "\n";
            Log.i("tigertiger", temp);
            try {
                if (out != null) {
                    out.write(temp.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeWriteStream() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
