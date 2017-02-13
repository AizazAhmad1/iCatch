package com.icatch.wificam.customer;

import com.icatch.wificam.core.jni.JWificamInfo;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

public class ICatchWificamInfo {
    private int sessionID;

    ICatchWificamInfo(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getSDKVersion() throws IchInvalidSessionException {
        return JWificamInfo.getSDKVersion_Jni(this.sessionID);
    }

    public String getCameraProductName() throws IchInvalidSessionException {
        return JWificamInfo.getCameraProductName_Jni(this.sessionID);
    }

    public String getCameraFWVersion() throws IchInvalidSessionException {
        return JWificamInfo.getCameraFWVersion_Jni(this.sessionID);
    }
}
