package com.icatch.ismartdv2016.Tools;

import android.graphics.Rect;

public class ScaleTool {
    public static Rect getScaledPosition(int frmW, int frmH, int wndW, int wndH) {
        int rectLeft;
        int rectRigth;
        int rectTop;
        int rectBottom;
        Rect rect = new Rect();
        if (wndW * frmH < wndH * frmW) {
            rectLeft = 0;
            rectRigth = wndW;
            rectTop = (wndH - ((wndW * frmH) / frmW)) / 2;
            rectBottom = wndH - rectTop;
        } else if (wndW * frmH > wndH * frmW) {
            rectLeft = (wndW - ((wndH * frmW) / frmH)) / 2;
            rectRigth = wndW - rectLeft;
            rectTop = 0;
            rectBottom = wndH;
        } else {
            rectLeft = 0;
            rectRigth = wndW;
            rectTop = 0;
            rectBottom = wndH;
        }
        return new Rect(rectLeft, rectTop, rectRigth, rectBottom);
    }
}
