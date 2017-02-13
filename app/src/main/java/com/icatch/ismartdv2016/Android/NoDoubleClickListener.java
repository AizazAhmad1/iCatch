package com.icatch.ismartdv2016.Android;

import android.view.View;
import android.view.View.OnClickListener;
import java.util.Calendar;

public abstract class NoDoubleClickListener implements OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public abstract void onNoDoubleClick(View view);

    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - this.lastClickTime > 1000) {
            this.lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}
