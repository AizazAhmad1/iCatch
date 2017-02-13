package com.icatch.wificam.customer;

import com.icatch.wificam.core.jni.JWificamSession;
import com.icatch.wificam.core.util.SDKEventHandleAPI;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidPasswdException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.exception.IchPtpInitFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;

public class ICatchWificamSession {
    private ICatchWificamControl controlClient;
    private ICatchWificamInfo infoClient;
    private ICatchWificamPlayback playbackClient;
    private ICatchWificamPreview previewClient;
    private ICatchWificamProperty propertyClient;
    private int sessionID = JWificamSession.createJniSession_Jni();
    private ICatchWificamState stateClient;
    private ICatchWificamVideoPlayback videoPlaybackClient;

    public void finalize() {
        JWificamSession.deleteJniSession_Jni(this.sessionID);
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public static boolean startDeviceScan() {
        return JWificamSession.startDeviceScan_Jni();
    }

    public static boolean stopDeviceScan() {
        return JWificamSession.stopDeviceScan_Jni();
    }

    public static boolean deviceInit(String ipAddr) {
        return JWificamSession.deviceInit_Jni(ipAddr);
    }

    public static boolean addEventListener(int icatchEvtID, ICatchWificamListener listener) throws IchListenerExistsException {
        try {
            SDKEventHandleAPI.getInstance().addStandardEventListener(icatchEvtID, -1, listener);
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delEventListener(int icatchEvtID, ICatchWificamListener listener) throws IchListenerNotExistsException {
        try {
            SDKEventHandleAPI.getInstance().removeStandardEventListener(icatchEvtID, -1, listener);
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addEventListener(int icatchEvtID, ICatchWificamListener listener, boolean forAllSession) throws IchListenerExistsException {
        try {
            SDKEventHandleAPI.getInstance().addStandardEventListener(icatchEvtID, forAllSession ? -2 : -1, listener);
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delEventListener(int icatchEvtID, ICatchWificamListener listener, boolean forAllSession) throws IchListenerNotExistsException {
        try {
            SDKEventHandleAPI.getInstance().removeStandardEventListener(icatchEvtID, forAllSession ? -2 : -1, listener);
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean prepareSession(String ipAddr, String username, String password) throws IchInvalidPasswdException, IchPtpInitFailedException {
        this.sessionID = JWificamSession.prepareSession_Jni(this.sessionID, ipAddr, username, password);
        if (this.sessionID < 0) {
            return false;
        }
        SDKEventHandleAPI.getInstance().addWatchedSession(this.sessionID);
        this.infoClient = new ICatchWificamInfo(this.sessionID);
        this.stateClient = new ICatchWificamState(this.sessionID);
        this.previewClient = new ICatchWificamPreview(this.sessionID);
        this.controlClient = new ICatchWificamControl(this.sessionID, ipAddr);
        this.playbackClient = new ICatchWificamPlayback(this.sessionID);
        this.propertyClient = new ICatchWificamProperty(this.sessionID);
        this.videoPlaybackClient = new ICatchWificamVideoPlayback(this.sessionID);
        return true;
    }

    public boolean destroySession() throws IchInvalidSessionException {
        if (this.sessionID < 0) {
            return true;
        }
        boolean retVal = JWificamSession.destroySession_Jni(this.sessionID);
        SDKEventHandleAPI.getInstance().removeWatchedSession(this.sessionID);
        return retVal;
    }

    public boolean checkConnection() throws IchInvalidSessionException {
        checkSession();
        return JWificamSession.checkConnection_Jni(this.sessionID);
    }

    public static boolean wakeUpCamera(String macAddress) throws IchSocketException, IchInvalidArgumentException {
        return JWificamSession.wakeUpCamera_Jni(macAddress);
    }

    public ICatchWificamInfo getInfoClient() throws IchInvalidSessionException {
        checkSession();
        return this.infoClient;
    }

    public ICatchWificamState getStateClient() throws IchInvalidSessionException {
        checkSession();
        return this.stateClient;
    }

    public ICatchWificamPreview getPreviewClient() throws IchInvalidSessionException {
        checkSession();
        return this.previewClient;
    }

    public ICatchWificamControl getControlClient() throws IchInvalidSessionException {
        checkSession();
        return this.controlClient;
    }

    public ICatchWificamProperty getPropertyClient() throws IchInvalidSessionException {
        checkSession();
        return this.propertyClient;
    }

    public ICatchWificamPlayback getPlaybackClient() throws IchInvalidSessionException {
        checkSession();
        return this.playbackClient;
    }

    public ICatchWificamVideoPlayback getVideoPlaybackClient() throws IchInvalidSessionException {
        checkSession();
        return this.videoPlaybackClient;
    }

    private void checkSession() throws IchInvalidSessionException {
        if (this.sessionID < 0) {
            throw new IchInvalidSessionException();
        }
    }
}
