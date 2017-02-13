package com.icatch.ismartdv2016.Beans;

public class SearchedCameraInfo {
    public String cameraIp;
    public int cameraMode;
    public String cameraName;
    public String uid;

    public SearchedCameraInfo(String cameraName, String cameraIp, int cameraMode, String uid) {
        this.cameraName = cameraName;
        this.cameraIp = cameraIp;
        this.cameraMode = cameraMode;
        this.uid = uid;
    }
}
