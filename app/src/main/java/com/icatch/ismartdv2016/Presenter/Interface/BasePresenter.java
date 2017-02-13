package com.icatch.ismartdv2016.Presenter.Interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.GlobalApp.ExitApp;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import java.lang.reflect.Method;

public abstract class BasePresenter {
    protected Activity activity;
    private final String tag = "BasePresenter";

    public BasePresenter(Activity activity) {
        this.activity = activity;
    }

    public void initCfg() {
        GlobalInfo.getInstance().setCurrentApp(this.activity);
        this.activity.getWindow().setFlags(128, 128);
        this.activity.getWindow().addFlags(AppMessage.PHOTO_PBACTIVITY);
    }

    public void setCurrentApp() {
        GlobalInfo.getInstance().setCurrentApp(this.activity);
    }

    public void redirectToAnotherActivity(Context context, Class<?> cls) {
        Intent intent = new Intent();
        AppLog.i("BasePresenter", "intent:start redirectToAnotherActivity class =" + cls.getName());
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public void finishActivity() {
        this.activity.finish();
    }

    public void isAppBackground() {
        if (AppInfo.isAppSentToBackground(this.activity)) {
            AppLog.d("BasePresenter", "is background activity=" + this.activity);
            ExitApp.getInstance().exit();
            return;
        }
        AppLog.d("BasePresenter", "not is background activity=" + this.activity);
    }

    public void submitAppInfo() {
        GlobalInfo.getInstance().setCurrentApp(this.activity);
        ExitApp.getInstance().addActivity(this.activity);
    }

    public void removeActivity() {
        if (this.activity != null) {
            ExitApp.getInstance().removeActivity(this.activity);
        }
    }

    public void showOptionIcon(Menu menu) {
        if (menu != null && menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", new Class[]{Boolean.TYPE});
                m.setAccessible(true);
                m.invoke(menu, new Object[]{Boolean.valueOf(true)});
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
            }
        }
    }
}
