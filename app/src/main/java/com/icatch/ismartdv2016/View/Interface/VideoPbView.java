package com.icatch.ismartdv2016.View.Interface;

import com.icatch.ismartdv2016.MyCamera.MyCamera;

public interface VideoPbView {
    int getSeekBarProgress();

    void setBottomBarVisibility(int i);

    void setLoadPercent(int i);

    void setPlayBtnSrc(int i);

    void setPlayCircleImageViewVisibility(int i);

    void setSeekBarMaxValue(int i);

    void setSeekBarProgress(int i);

    void setSeekBarSecondProgress(int i);

    void setSeekbarEnabled(boolean z);

    void setTimeDurationValue(String str);

    void setTimeLapsedValue(String str);

    void setTopBarVisibility(int i);

    void setVideoNameTxv(String str);

    void showLoadingCircle(boolean z);

    void startMPreview(MyCamera myCamera, int i);

    void stopMPreview();
}
