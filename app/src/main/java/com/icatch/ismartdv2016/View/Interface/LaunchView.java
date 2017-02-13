package com.icatch.ismartdv2016.View.Interface;

import android.graphics.Bitmap;
import com.icatch.ismartdv2016.Adapter.CameraSlotAdapter;

public interface LaunchView {
    void fragmentPopStackOfAll();

    void loadDefaultLocalPhotoThumbnail();

    void loadDefaultLocalVideooThumbnail();

    void setBackBtnVisibility(boolean z);

    void setLaunchLayoutVisibility(int i);

    void setLaunchSettingFrameVisibility(int i);

    void setListviewAdapter(CameraSlotAdapter cameraSlotAdapter);

    void setLocalPhotoThumbnail(Bitmap bitmap);

    void setLocalVideoThumbnail(Bitmap bitmap);

    void setNavigationTitle(int i);

    void setNavigationTitle(String str);

    void setNoPhotoFilesFoundVisibility(int i);

    void setNoVideoFilesFoundVisibility(int i);

    void setPhotoClickable(boolean z);

    void setVideoClickable(boolean z);
}
