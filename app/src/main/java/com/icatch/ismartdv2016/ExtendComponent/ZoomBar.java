package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class ZoomBar extends SeekBar {
    private static int minValue = 0;

    public ZoomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMinValue(int minValue) {
        minValue = minValue;
    }

    public synchronized int getZoomProgress() {
        return super.getProgress() + minValue;
    }

    public synchronized void setMax(int max) {
        super.setMax(max - minValue);
    }

    public synchronized void setProgress(int progress) {
        super.setProgress(progress - minValue);
    }
}
