package com.icatch.ismartdv2016.Tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import uk.co.senab.photoview.BuildConfig;

public class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler INSTANCE = new CrashHandler();
    public static final String TAG = "CrashHandler";
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Handler handler = new Handler();
    private Map<String, String> infos = new HashMap();
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                AppLog.e(TAG, e.toString());
            }
            AppLog.e(TAG, "....show custom process!");
            this.handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(GlobalInfo.getInstance().getCurrentApp(), R.string.app_exception, 1).show();
                }
            });
            Process.killProcess(Process.myPid());
            System.exit(1);
            return;
        }
        AppLog.e(TAG, "....default process!");
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            public void run() {
                Looper.prepare();
                MyToast.show(CrashHandler.this.mContext, CrashHandler.this.mContext.getResources().getString(R.string.app_exception) + "\n" + ex.toString());
                Looper.loop();
            }
        }.start();
        AppLog.e(TAG, ex.toString());
        collectDeviceInfo(this.mContext);
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                String versionCode = pi.versionCode + BuildConfig.FLAVOR;
                this.infos.put("versionName", pi.versionName == null ? "null" : pi.versionName);
                this.infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            AppLog.d(TAG, e.toString());
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
                AppLog.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                AppLog.e(TAG, e2.toString());
            }
        }
    }
}
