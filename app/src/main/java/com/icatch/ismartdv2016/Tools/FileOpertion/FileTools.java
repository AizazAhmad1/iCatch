package com.icatch.ismartdv2016.Tools.FileOpertion;

import android.os.Environment;
import android.util.Log;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FileTools {
    private static final String TAG = "FileTools";
    private static String[] Urls = null;

    public static List<File> getFilesOrderByLength(String fliePath) {
        List<File> files = Arrays.asList(new File(fliePath).listFiles());
        Collections.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.length() - f2.length();
                if (diff > 0) {
                    return 1;
                }
                if (diff == 0) {
                    return 0;
                }
                return -1;
            }

            public boolean equals(Object obj) {
                return true;
            }
        });
        for (File f : files) {
            if (f.isDirectory()) {
            }
        }
        return files;
    }

    public static List<File> getFilesOrderByName(String fliePath) {
        List<File> files = Arrays.asList(new File(fliePath).listFiles());
        Collections.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File f : files) {
            AppLog.i(TAG, f.getName());
        }
        return files;
    }

    public static List<File> getFilesOrderByDate(String filePath) {
        AppLog.i(TAG, "Start getFilesOrderByDate filePath=" + filePath);
        File file = new File(filePath);
        AppLog.i(TAG, "Start getFilesOrderByDate file=" + file);
        File[] fileArray = file.listFiles();
        AppLog.i(TAG, "Start getFilesOrderByDate fileArray=" + fileArray);
        if (fileArray == null) {
            return null;
        }
        AppLog.i(TAG, "Start getFilesOrderByDate size=" + fileArray.length);
        List<File> files = Arrays.asList(fileArray);
        AppLog.i(TAG, "Start getFilesOrderByDate 2");
        Collections.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff < 0) {
                    return 1;
                }
                if (diff == 0) {
                    return 0;
                }
                return -1;
            }

            public boolean equals(Object obj) {
                return true;
            }
        });
        AppLog.i(TAG, "files.size() = " + files.size());
        for (int ii = 0; ii < files.size(); ii++) {
            AppLog.i(TAG, "file name = " + ((File) files.get(ii)).getName());
            AppLog.i(TAG, "modify time = " + new Date(((File) files.get(ii)).lastModified()));
        }
        AppLog.i(TAG, "End getFilesOrderByDate");
        return files;
    }

    public static String getFileDate(String fileName) {
        if (fileName == null) {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        long time = file.lastModified();
        AppLog.d(TAG, "file neme" + fileName);
        AppLog.d(TAG, "file.lastModified()" + time);
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
    }

    public static long getFileSize(File f) {
        long size = 0;
        File[] flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size += getFileSize(flist[i]);
            } else {
                size += flist[i].length();
            }
        }
        return size;
    }

    public static void copyFile(int resourceId) {
        IOException e;
        File file;
        Throwable th;
        File Folder = new File("/sdcard/SportCamResoure/");
        if (!Folder.exists()) {
            Folder.mkdir();
        }
        String filename = Environment.getExternalStorageDirectory().toString() + AppInfo.PROPERTY_CFG_DIRECTORY_PATH;
        Log.d("1111", "copyFile filename ==" + filename);
        InputStream in = null;
        OutputStream out = null;
        String fileName1 = "sphost.BRN";
        File file2 = new File(filename, fileName1);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        try {
            OutputStream out2;
            in = GlobalInfo.getInstance().getCurrentApp().getResources().openRawResource(resourceId);
            File outFile = new File(filename + fileName1);
            try {
                Log.d("1111", "output file" + outFile.getAbsolutePath());
                out2 = new FileOutputStream(outFile);
            } catch (IOException e3) {
                e2 = e3;
                file = outFile;
                try {
                    Log.e("1111", "Failed to copy file", e2);
                    try {
                        in.close();
                        out.flush();
                        out.close();
                    } catch (Exception e4) {
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        in.close();
                        out.flush();
                        out.close();
                    } catch (Exception e5) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                file = outFile;
                in.close();
                out.flush();
                out.close();
                throw th;
            }
            try {
                byte[] buffer = new byte[AppMessage.PHOTO_PBACTIVITY];
                while (true) {
                    int read = in.read(buffer);
                    if (read != -1) {
                        out2.write(buffer, 0, read);
                    } else {
                        try {
                            in.close();
                            out2.flush();
                            out2.close();
                            file = outFile;
                            return;
                        } catch (Exception e6) {
                            file = outFile;
                            out = out2;
                            return;
                        }
                    }
                }
            } catch (IOException e7) {
                e2 = e7;
                file = outFile;
                out = out2;
                Log.e("1111", "Failed to copy file", e2);
                in.close();
                out.flush();
                out.close();
            } catch (Throwable th4) {
                th = th4;
                file = outFile;
                out = out2;
                in.close();
                out.flush();
                out.close();
                throw th;
            }
        } catch (IOException e8) {
            e2 = e8;
            Log.e("1111", "Failed to copy file", e2);
            in.close();
            out.flush();
            out.close();
        }
    }
}
