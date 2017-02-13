package com.icatch.ismartdv2016.BaseItems;

import com.icatch.ismartdv2016.Tools.ConvertTools;
import com.icatch.wificam.customer.type.ICatchFile;
import uk.co.senab.photoview.BuildConfig;

public class MultiPbItemInfo {
    private String TAG = "MultiPbItemInfo";
    public ICatchFile iCatchFile;
    public boolean isItemChecked;
    public int section;

    public MultiPbItemInfo(ICatchFile file, int section) {
        this.iCatchFile = file;
        this.section = section;
        this.isItemChecked = false;
    }

    public MultiPbItemInfo(ICatchFile file) {
        this.iCatchFile = file;
        this.isItemChecked = false;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getFilePath() {
        return this.iCatchFile.getFilePath();
    }

    public int getFileHandle() {
        return this.iCatchFile.getFileHandle();
    }

    public String getFileDate() {
        String time = ConvertTools.getTimeByfileDate(this.iCatchFile.getFileDate());
        if (time == null) {
            return "unknown";
        }
        return time;
    }

    public String getFileSize() {
        return ConvertTools.ByteConversionGBMBKB(this.iCatchFile.getFileSize());
    }

    public long getFileSizeInteger() {
        return this.iCatchFile.getFileSize();
    }

    public String getFileName() {
        return this.iCatchFile.getFileName();
    }

    public String getFileDateMMSS() {
        return dateFormatTransform(this.iCatchFile.getFileDate());
    }

    public String dateFormatTransform(String value) {
        if (value == null) {
            return BuildConfig.FLAVOR;
        }
        String date = BuildConfig.FLAVOR;
        String time = BuildConfig.FLAVOR;
        String yy = BuildConfig.FLAVOR;
        String MM = BuildConfig.FLAVOR;
        String dd = BuildConfig.FLAVOR;
        String hh = BuildConfig.FLAVOR;
        String mm = BuildConfig.FLAVOR;
        String ss = BuildConfig.FLAVOR;
        int position = value.indexOf("T");
        date = value.substring(0, position);
        time = value.substring(position + 1);
        yy = date.substring(0, 4);
        MM = date.substring(4, 6);
        dd = date.substring(6, 8);
        hh = time.substring(0, 2);
        mm = time.substring(2, 4);
        return yy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + time.substring(4, 6);
    }
}
