package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.icatch.ismartdv2016.R;

public class MyToast {
    private static Toast toast;

    public static void show(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.message_text);
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        textView.setText(message);
        toast.setDuration(1);
        toast.setView(view);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public static void show(Context context, int stringId) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.message_text);
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        textView.setText(stringId);
        toast.setDuration(1);
        toast.setView(view);
        toast.setGravity(17, 0, 0);
        toast.show();
    }
}
