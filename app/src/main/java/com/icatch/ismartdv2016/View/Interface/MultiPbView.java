package com.icatch.ismartdv2016.View.Interface;

import android.support.v4.app.FragmentPagerAdapter;

public interface MultiPbView {
    void setMenuPhotoWallTypeIcon(int i);

    void setSelectBtnIcon(int i);

    void setSelectBtnVisibility(int i);

    void setSelectNumText(String str);

    void setSelectNumTextVisibility(int i);

    void setTabLayoutClickable(boolean z);

    void setViewPageAdapter(FragmentPagerAdapter fragmentPagerAdapter);

    void setViewPageCurrentItem(int i);

    void setViewPagerScanScroll(boolean z);
}
