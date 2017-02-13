package com.icatch.ismartdv2016.Model.Interface;

import com.icatch.wificam.customer.ICatchWificamSession;

public interface ISDKSession {
    boolean checkWifiConnection();

    boolean destroySession();

    ICatchWificamSession getSDKSession();

    boolean isSessionOK();

    boolean prepareSession();

    boolean prepareSession(String str);

    boolean prepareSession(String str, boolean z);
}
