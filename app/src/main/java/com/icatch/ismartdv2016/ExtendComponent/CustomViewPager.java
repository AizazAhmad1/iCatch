package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public void scrollTo(int x, int y) {
        if (this.isCanScroll) {
            super.scrollTo(x, y);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.isCanScroll) {
            return super.onTouchEvent(ev);
        }
        return true;
    }
}
