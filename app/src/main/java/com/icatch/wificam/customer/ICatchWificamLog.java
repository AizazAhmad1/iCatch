package com.icatch.wificam.customer;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.JWificamLog;
import com.icatch.wificam.customer.type.ICatchLogLevel;
import com.slidingmenu.lib.R;
import com.slidingmenu.lib.SlidingMenu;

public class ICatchWificamLog {
    private static ICatchWificamLog instance = new ICatchWificamLog();

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel = new int[ICatchLogLevel.values().length];

        static {
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel[ICatchLogLevel.ICH_LOG_LEVEL_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel[ICatchLogLevel.ICH_LOG_LEVEL_WARN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel[ICatchLogLevel.ICH_LOG_LEVEL_INFO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel[ICatchLogLevel.ICH_LOG_LEVEL_CONNECT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private ICatchWificamLog() {
    }

    public static synchronized ICatchWificamLog getInstance() {
        ICatchWificamLog iCatchWificamLog;
        synchronized (ICatchWificamLog.class) {
            iCatchWificamLog = instance;
        }
        return iCatchWificamLog;
    }

    public void setSystemLogOutput(boolean bOutput) {
        JWificamLog.setSystemLogOutput(bOutput);
    }

    public void setFileLogPath(String path) {
        JWificamLog.setFileLogPath(path);
    }

    public void setFileLogOutput(boolean bOutput) {
        JWificamLog.setFileLogOutput(bOutput);
    }

    public void setRtpLog(boolean enable) {
        JWificamLog.setRtpLog(enable);
    }

    public void setPtpLog(boolean enable) {
        JWificamLog.setPtpLog(enable);
    }

    public void setPtpLogLevel(ICatchLogLevel level) {
        int ntvLevel = 1;
        switch (AnonymousClass1.$SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel[level.ordinal()]) {
            case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                ntvLevel = 3;
                break;
            case SlidingMenu.TOUCHMODE_NONE /*2*/:
                ntvLevel = 2;
                break;
            case R.styleable.SlidingMenu_behindOffset /*3*/:
                ntvLevel = 1;
                break;
            case R.styleable.SlidingMenu_behindWidth /*4*/:
                ntvLevel = 0;
                break;
        }
        JWificamLog.setPtpLogLevel(ntvLevel);
    }

    public void setRtpLogLevel(ICatchLogLevel level) {
        int ntvLevel = 1;
        switch (AnonymousClass1.$SwitchMap$com$icatch$wificam$customer$type$ICatchLogLevel[level.ordinal()]) {
            case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                ntvLevel = 3;
                break;
            case SlidingMenu.TOUCHMODE_NONE /*2*/:
                ntvLevel = 2;
                break;
            case R.styleable.SlidingMenu_behindOffset /*3*/:
                ntvLevel = 1;
                break;
            case R.styleable.SlidingMenu_behindWidth /*4*/:
                ntvLevel = 0;
                break;
        }
        JWificamLog.setRtpLogLevel(ntvLevel);
    }

    public void logI(String subTag, String message) {
        CoreLogger.logI(subTag, message);
    }

    public void setDebugMode(boolean debugMode) {
        JWificamLog.setDebugMode(debugMode);
    }
}
