package com.icatch.ismartdv2016.AppDialog;

import android.content.Context;
import android.widget.Toast;

public class AppToast {
    private static Toast toast;

    public static void show(Context context, CharSequence text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, "\u9ed8\u8ba4\u7684Toast", 0);
        }
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    public static void show(Context context, int stringId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, "\u9ed8\u8ba4\u7684Toast", 0);
        }
        toast.setText(stringId);
        toast.setDuration(duration);
        toast.show();
    }
}
