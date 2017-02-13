package com.icatch.ismartdv2016.Model.Implement;

import android.util.Log;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Model.Interface.ISDKSession;
import com.icatch.wificam.customer.ICatchWificamConfig;
import com.icatch.wificam.customer.ICatchWificamSession;
import com.icatch.wificam.customer.exception.IchInvalidPasswdException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchPtpInitFailedException;

public class SDKSession implements ISDKSession {
    private static int scanflag = 0;
    private static final String tag = "SDKSession";
    private String ipAddress;
    private String password;
    private ICatchWificamSession session;
    private boolean sessionPrepared = false;
    private String uid;
    private String username;

    public SDKSession(String ipAddress, String uid, String username, String password) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    public boolean prepareSession() {
        ICatchWificamConfig.getInstance().enablePTPIP();
        this.sessionPrepared = true;
        this.session = new ICatchWificamSession();
        boolean retValue = false;
        try {
            retValue = this.session.prepareSession("192.168.1.1", "anonymous", "anonymous@icatchtek.com");
        } catch (IchInvalidPasswdException e) {
            e.printStackTrace();
        } catch (IchPtpInitFailedException e2) {
            e2.printStackTrace();
        }
        if (!retValue) {
            AppLog.e(tag, "failed to prepareSession");
            this.sessionPrepared = false;
            Log.v("1111", "SDKSession,prepareSession fail!");
        }
        AppLog.d(tag, "end prepareSession ret=" + this.sessionPrepared);
        return this.sessionPrepared;
    }

    public boolean prepareSession(String ip, boolean enablePTPIP) {
        if (enablePTPIP) {
            ICatchWificamConfig.getInstance().enablePTPIP();
        } else {
            ICatchWificamConfig.getInstance().disablePTPIP();
        }
        this.sessionPrepared = true;
        this.session = new ICatchWificamSession();
        boolean retValue = false;
        try {
            retValue = this.session.prepareSession("192.168.1.1", "anonymous", "anonymous@icatchtek.com");
        } catch (IchInvalidPasswdException e) {
            e.printStackTrace();
        } catch (IchPtpInitFailedException e2) {
            e2.printStackTrace();
        }
        if (!retValue) {
            AppLog.e(tag, "failed to prepareSession");
            this.sessionPrepared = false;
            Log.v("1111", "SDKSession,prepareSession fail!");
        }
        AppLog.d(tag, "end init local prepareSession ret=" + this.sessionPrepared);
        return this.sessionPrepared;
    }

    public boolean isSessionOK() {
        return this.sessionPrepared;
    }

    public ICatchWificamSession getSDKSession() {
        return this.session;
    }

    public boolean checkWifiConnection() {
        AppLog.i(tag, "Start checkWifiConnection");
        boolean retValue = false;
        try {
            retValue = this.session.checkConnection();
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        AppLog.i(tag, "End checkWifiConnection,retValue=" + retValue);
        return retValue;
    }

    public boolean destroySession() {
        AppLog.i(tag, "Start destroySession");
        Boolean retValue = Boolean.valueOf(false);
        try {
            retValue = Boolean.valueOf(this.session.destroySession());
            AppLog.i(tag, "End  destroySession,retValue=" + retValue);
            AppLog.i(tag, "start disableTutk");
            AppLog.i(tag, "End disableTutk,retValue=" + retValue);
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        return retValue.booleanValue();
    }

    public static boolean startDeviceScan() {
        AppLog.i(tag, "Start startDeviceScan");
        boolean tempStartDeviceScanValue = ICatchWificamSession.startDeviceScan();
        AppLog.i(tag, "End startDeviceScan,tempStartDeviceScanValue=" + tempStartDeviceScanValue);
        if (tempStartDeviceScanValue) {
            scanflag = 1;
        }
        return tempStartDeviceScanValue;
    }

    public boolean prepareSession(String ip) {
        Log.d("tigertiger", "start to enablePTPIP");
        ICatchWificamConfig.getInstance().enablePTPIP();
        this.sessionPrepared = true;
        this.session = new ICatchWificamSession();
        boolean retValue = false;
        Log.d("tigertiger", "-----start to prepareSession!");
        try {
            retValue = this.session.prepareSession(ip, "anonymous", "anonymous@icatchtek.com");
        } catch (IchInvalidPasswdException e) {
            e.printStackTrace();
        } catch (IchPtpInitFailedException e2) {
            e2.printStackTrace();
        }
        if (!retValue) {
            AppLog.e(tag, "failed to prepareSession");
            this.sessionPrepared = false;
            Log.v("1111", "SDKSession,prepareSession fail!");
        }
        AppLog.d(tag, "prepareSession =" + this.sessionPrepared);
        return this.sessionPrepared;
    }

    public static void stopDeviceScan() {
        boolean tempStopDeviceScanValue;
        AppLog.i(tag, "Start stopDeviceScan");
        if (scanflag == 1) {
            tempStopDeviceScanValue = ICatchWificamSession.stopDeviceScan();
        } else {
            tempStopDeviceScanValue = true;
        }
        scanflag = 0;
        AppLog.i(tag, "End stopDeviceScan,tempStopDeviceScanValue=" + tempStopDeviceScanValue);
    }
}
