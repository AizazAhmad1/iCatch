package com.icatch.wificam.core;

import android.content.Context;

public class CoreContainer {
    private static CoreContainer instance = new CoreContainer();
    private Context applicationContext;

    private CoreContainer() {
    }

    public static CoreContainer getInstance() {
        return instance;
    }

    public boolean prepareComponents(Context applicationContext) {
        this.applicationContext = applicationContext;
        return true;
    }

    public boolean destroyComponents() {
        CoreLogger.logE("sdk container", "destroyComponents done");
        return true;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }
}
