package com.icatch.ismartdv2016.AppDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.icatch.ismartdv2016.GlobalApp.ExitApp;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;

public class AppDialog {
    private static AlertDialog dialog = null;
    private static boolean needShown = true;
    private static final String tag = "AppDialog";

    public void showDialog(String title, String message, boolean cancelable) {
    }

    public static void showDialogQuit(final Context context, final int messageID) {
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(messageID);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppLog.i(AppDialog.tag, "ExitApp because of " + context.getResources().getString(messageID));
                ExitApp.getInstance().exit();
            }
        });
        builder.create().show();
    }

    public static void showDialogQuit(Context context, final String message) {
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppLog.i(AppDialog.tag, "ExitApp because of " + message);
                ExitApp.getInstance().exit();
            }
        });
        builder.create().show();
    }

    public static void showDialogWarn(Context context, String message) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public static void showDialogWarn(Context context, int messageID) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(messageID);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public static void showConectFailureWarning(Context context) {
        if (needShown) {
            Builder builder = new Builder(context);
            builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(R.string.dialog_timeout);
            builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ExitApp.getInstance().exit();
                }
            });
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.dialog_btn_reconnect, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AppDialog.needShown = true;
                }
            });
            builder.create().show();
        }
    }

    public static void showLowBatteryWarning(Context context) {
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(R.string.low_battery);
        builder.setPositiveButton(R.string.ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
