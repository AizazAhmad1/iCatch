package com.icatch.ismartdv2016.Beans;

public class CameraSlot {
    public String cameraName;
    public byte[] cameraPhoto;
    public boolean isOccupied;
    public boolean isWifiReady;
    public int slotPosition;

    public CameraSlot(int slotPosition, boolean isOccupied, String cameraName, byte[] cameraPhoto, boolean isWifiReady) {
        this.slotPosition = slotPosition;
        this.isOccupied = isOccupied;
        this.cameraName = cameraName;
        this.cameraPhoto = cameraPhoto;
        this.isWifiReady = isWifiReady;
    }

    public CameraSlot(int slotPosition, boolean isOccupied, String cameraName, byte[] cameraPhoto) {
        this.slotPosition = slotPosition;
        this.isOccupied = isOccupied;
        this.cameraName = cameraName;
        this.cameraPhoto = cameraPhoto;
        this.isWifiReady = false;
    }
}
