package com.icatch.wificam.customer.type;

public class ICatchFile {
    private String fileDate;
    private int fileHandle;
    private int fileHeight;
    private String fileName;
    private String filePath;
    private long fileSize;
    private ICatchFileType fileType;
    private int fileWidth;
    private double frameRate;

    public ICatchFile(int fileHandle) {
        this.fileHandle = fileHandle;
    }

    public ICatchFile(int fileHandle, ICatchFileType fileType, String fileName, long fileSize) {
        this.fileHandle = fileHandle;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public ICatchFile(int fileHandle, ICatchFileType fileType, String filePath, String fileName, long fileSize) {
        this.fileHandle = fileHandle;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public ICatchFile(int fileHandle, ICatchFileType fileType, String filePath, String fileName, long fileSize, String fileDate) {
        this.fileHandle = fileHandle;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
    }

    public ICatchFile(int fileHandle, ICatchFileType fileType, String filePath, String fileName, long fileSize, String fileDate, double frameRate, int fileWidth, int fileHeight) {
        this.fileHandle = fileHandle;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.frameRate = frameRate;
        this.fileWidth = fileWidth;
        this.fileHeight = fileHeight;
    }

    public int getFileHandle() {
        return this.fileHandle;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileDate() {
        return this.fileDate;
    }

    public ICatchFileType getFileType() {
        return this.fileType;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public double getFrameRate() {
        return this.frameRate;
    }

    public long getFileWidth() {
        return (long) this.fileWidth;
    }

    public long getFileHeight() {
        return (long) this.fileHeight;
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj || ((ICatchFile) obj).getFileHandle() == getFileHandle()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Integer.valueOf(this.fileHandle).hashCode();
    }

    public String toString() {
        return "fileHandle: " + this.fileHandle + ", filePath: " + this.filePath + ", " + "fileName: " + this.fileName + ", fileDate: " + this.fileDate + ", " + "fileType: " + this.fileType + ", fileSize: " + this.fileSize + ", " + "fileWidth: " + this.fileWidth + ", fileHeight: " + this.fileHeight + ", " + "frameRate: " + this.frameRate;
    }
}
