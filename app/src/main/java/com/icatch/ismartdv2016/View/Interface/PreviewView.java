package com.icatch.ismartdv2016.View.Interface;

import android.graphics.Bitmap;
import com.icatch.ismartdv2016.Adapter.SettingListAdapter;
import com.icatch.ismartdv2016.MyCamera.MyCamera;

public interface PreviewView {
    void dismissPopupWindow();

    int getSetupMainMenuVisibility();

    int getZoomViewMaxZoomRate();

    int getZoomViewProgress();

    void setActionBarTitle(int i);

    void setAutoDownloadBitmap(Bitmap bitmap);

    void setAutoDownloadVisibility(int i);

    void setBackBtnVisibility(boolean z);

    void setBatteryIcon(int i);

    void setBatteryStatusVisibility(int i);

    void setBurstStatusIcon(int i);

    void setBurstStatusVisibility(int i);

    void setCaptureBtnBackgroundResource(int i);

    void setCaptureBtnEnability(boolean z);

    void setCaptureRadioBtnChecked(boolean z);

    void setCaptureRadioBtnVisibility(int i);

    void setCarModeVisibility(int i);

    void setDelayCaptureLayoutVisibility(int i);

    void setDelayCaptureTextTime(String str);

    void setImageSizeInfo(String str);

    void setImageSizeLayoutVisibility(int i);

    void setMaxZoomRate(int i);

    void setPvModeBtnBackgroundResource(int i);

    void setRecordingTime(String str);

    void setRecordingTimeVisibility(int i);

    void setRemainCaptureCount(String str);

    void setRemainRecordingTimeText(String str);

    void setSettingBtnVisible(boolean z);

    void setSettingMenuListAdapter(SettingListAdapter settingListAdapter);

    void setSetupMainMenuVisibility(int i);

    void setSlowMotionVisibility(int i);

    void setSupportPreviewTxvVisibility(int i);

    void setTimepLapseRadioBtnVisibility(int i);

    void setTimepLapseRadioChecked(boolean z);

    void setUpsideVisibility(int i);

    void setVideoRadioBtnChecked(boolean z);

    void setVideoRadioBtnVisibility(int i);

    void setVideoSizeInfo(String str);

    void setVideoSizeLayoutVisibility(int i);

    void setWbStatusIcon(int i);

    void setWbStatusVisibility(int i);

    void setWifiIcon(int i);

    void setWifiStatusVisibility(int i);

    void setmPreviewVisibility(int i);

    void settimeLapseModeIcon(int i);

    void settimeLapseModeVisibility(int i);

    void showPopupWindow(int i);

    void showZoomView();

    void startMPreview(MyCamera myCamera);

    void stopMPreview(MyCamera myCamera);

    void updateZoomViewProgress(int i);
}
