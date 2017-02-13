package com.slidingmenu.lib.app;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.slidingmenu.lib.SlidingMenu;

public interface SlidingActivityBase {
    SlidingMenu getSlidingMenu();

    void setBehindContentView(int i);

    void setBehindContentView(View view);

    void setBehindContentView(View view, LayoutParams layoutParams);

    void setSlidingActionBarEnabled(boolean z);

    void showContent();

    void showMenu();

    void showSecondaryMenu();

    void toggle();
}
