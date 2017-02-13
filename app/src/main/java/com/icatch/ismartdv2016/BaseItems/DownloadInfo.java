package com.icatch.ismartdv2016.BaseItems;

import com.icatch.wificam.customer.type.ICatchFile;

public class DownloadInfo {
    public long curFileLength = 0;
    public boolean done = false;
    public ICatchFile file = null;
    public long fileSize = 0;
    public int progress = 0;

    public DownloadInfo(ICatchFile file, long fileSize, long curFileLength, int progress, boolean done) {
        this.file = file;
        this.fileSize = fileSize;
        this.curFileLength = curFileLength;
        this.progress = progress;
        this.done = done;
    }
}
