package com.slidingmenu.lib;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface MenuInterface {
    void drawFade(Canvas canvas, int i, CustomViewBehind customViewBehind, View view);

    void drawSelector(View view, Canvas canvas, float f);

    void drawShadow(Canvas canvas, Drawable drawable, int i);

    int getAbsLeftBound(CustomViewBehind customViewBehind, View view);

    int getAbsRightBound(CustomViewBehind customViewBehind, View view);

    int getMenuLeft(CustomViewBehind customViewBehind, View view);

    boolean marginTouchAllowed(View view, int i, int i2);

    boolean menuClosedSlideAllowed(int i);

    boolean menuOpenSlideAllowed(int i);

    boolean menuOpenTouchAllowed(View view, int i, int i2);

    boolean menuTouchInQuickReturn(View view, int i, int i2);

    void scrollBehindTo(int i, int i2, CustomViewBehind customViewBehind, float f);
}
