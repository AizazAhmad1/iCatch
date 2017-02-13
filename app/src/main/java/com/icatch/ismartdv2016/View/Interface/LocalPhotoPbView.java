package com.icatch.ismartdv2016.View.Interface;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public interface LocalPhotoPbView {
    int getTopBarVisibility();

    int getViewPagerCurrentItem();

    void setBottomBarVisibility(int i);

    void setIndexInfoTxv(String str);

    void setOnPageChangeListener(OnPageChangeListener onPageChangeListener);

    void setTopBarVisibility(int i);

    void setViewPagerAdapter(PagerAdapter pagerAdapter);

    void setViewPagerCurrentItem(int i);

    void updateViewPagerBitmap(int i, Bitmap bitmap);
}
