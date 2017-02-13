package com.icatch.ismartdv2016.Beans;

public class SelectedCameraInfo {
    public String cameraIp;
    public int cameraMode;
    public String cameraName;
    public String password;
    public String uid;

    public SelectedCameraInfo(String cameraName, String cameraIp, int cameraMode, String uid) {
        this.cameraName = cameraName;
        this.cameraIp = cameraIp;
        this.cameraMode = cameraMode;
        this.uid = uid;
    }
}
