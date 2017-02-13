package com.icatch.ismartdv2016.ExtendComponent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.icatch.ismartdv2016.R;
import uk.co.senab.photoview.BuildConfig;

public class MyProgressDialog {
    private static boolean hasInit = false;
    private static Dialog mDialog = null;
    private static ProgressWheel mProgressWheel = null;

    public static void showProgressDialog(Context context, String text) {
        closeProgressDialog();
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        mDialog = new Dialog(context, R.style.Dialog);
        mDialog.setCancelable(false);
        View layout = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_progress_wheel_small, null);
        mDialog.setContentView(layout);
        mProgressWheel = (ProgressWheel) layout.findViewById(R.id.pw_spinner);
        if (text != null) {
            mProgressWheel.setText(text);
        } else {
            mProgressWheel.setText(BuildConfig.FLAVOR);
        }
        Window dialogWindow = mDialog.getWindow();
        LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = 17;
        dialogWindow.setAttributes(lp);
        mProgressWheel.startSpinning();
        mDialog.show();
    }

    public static void showProgressDialog(Context context, int stringID) {
        closeProgressDialog();
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        mDialog = new Dialog(context, R.style.Dialog);
        mDialog.setCancelable(false);
        View layout = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_progress_wheel_small, null);
        mDialog.setContentView(layout);
        mProgressWheel = (ProgressWheel) layout.findViewById(R.id.pw_spinner);
        String text = context.getResources().getString(stringID);
        if (text != null) {
            mProgressWheel.setText(text);
        } else {
            mProgressWheel.setText(BuildConfig.FLAVOR);
        }
        Window dialogWindow = mDialog.getWindow();
        LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = 17;
        dialogWindow.setAttributes(lp);
        mProgressWheel.startSpinning();
        mDialog.show();
    }

    public static void closeProgressDialog() {
        if (mProgressWheel != null) {
            mProgressWheel.stopSpinning();
            mProgressWheel = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
