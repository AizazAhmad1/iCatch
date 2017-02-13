package com.icatch.ismartdv2016.Tools.FileOpertion;

import com.icatch.ismartdv2016.Log.AppLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileOper {
    public static void createDirectory(String directoryPath) {
        if (directoryPath != null) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }

    public static void createFile(String directoryPath, String fileName) {
        AppLog.i("FileOper", "start createFile");
        if (directoryPath != null) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        File file = new File(directoryPath + fileName);
        AppLog.i("FileOper", "directoryPath+fileName =" + directoryPath + fileName);
        if (!file.exists()) {
            AppLog.i("FileOper", "file is not exists,need to create!");
            try {
                file.createNewFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppLog.i("FileOper", "FileNotFoundException");
            } catch (IOException e2) {
                AppLog.i("FileOper", "IOException");
                e2.printStackTrace();
            }
        }
    }
}
