package com.icatch.ismartdv2016.BaseItems;

import com.icatch.ismartdv2016.Tools.ConvertTools;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalPbItemInfo {
    public File file;
    public boolean isItemChecked = false;
    public int section;

    public LocalPbItemInfo(File file, int section) {
        this.file = file;
        this.section = section;
    }

    public LocalPbItemInfo(File file) {
        this.file = file;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getFilePath() {
        return this.file.getPath();
    }

    public String getFileDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(this.file.lastModified()));
    }

    public String getFileSize() {
        return ConvertTools.ByteConversionGBMBKB(this.file.length());
    }

    public String getFileName() {
        return this.file.getName();
    }

    public String getFileDateMMSS() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.file.lastModified()));
    }

    public void getCreateTime() {
        String strTime = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cmd /C dir " + this.file.getPath() + "/tc").getInputStream()));
            do {
                line = br.readLine();
                if (line == null) {
                    break;
                }
            } while (!line.endsWith(".txt"));
            strTime = line.substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\u521b\u5efa\u65f6\u95f4    " + strTime);
    }
}
