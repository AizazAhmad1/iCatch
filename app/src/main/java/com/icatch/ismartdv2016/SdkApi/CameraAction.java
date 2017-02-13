package com.icatch.ismartdv2016.SdkApi;

import android.util.Log;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamAssist;
import com.icatch.wificam.customer.ICatchWificamControl;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.ICatchWificamSession;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchCaptureImageException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.exception.IchNotSupportedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStorageFormatException;
import com.icatch.wificam.customer.exception.IchTimeOutException;
import uk.co.senab.photoview.BuildConfig;

public class CameraAction {
    private static CameraAction instance;
    private ICatchWificamControl cameraAction;
    public ICatchWificamAssist cameraAssist;
    private final String tag = "CameraAction";

    public static CameraAction getInstance() {
        if (instance == null) {
            instance = new CameraAction();
        }
        return instance;
    }

    private CameraAction() {
    }

    public void initCameraAction() {
        Log.d("1111", "GlobalInfo.getInstance().getCurrentCamera() =" + GlobalInfo.getInstance().getCurrentCamera());
        this.cameraAction = GlobalInfo.getInstance().getCurrentCamera().getcameraActionClient();
        this.cameraAssist = GlobalInfo.getInstance().getCurrentCamera().getCameraAssistClint();
    }

    public void initCameraAction(ICatchWificamControl myWificamControl) {
        this.cameraAction = myWificamControl;
    }

    public boolean capturePhoto() {
        AppLog.i("CameraAction", "begin doStillCapture");
        boolean ret = false;
        try {
            ret = this.cameraAction.capturePhoto();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchCaptureImageException e3) {
            AppLog.e("CameraAction", "IchCaptureImageException");
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e4.printStackTrace();
        }
        AppLog.i("CameraAction", "end doStillCapture ret = " + ret);
        return ret;
    }

    public boolean triggerCapturePhoto() {
        AppLog.i("CameraAction", "begin triggerCapturePhoto");
        boolean ret = false;
        try {
            ret = this.cameraAction.triggerCapturePhoto();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchCaptureImageException e3) {
            AppLog.e("CameraAction", "IchCaptureImageException");
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e4.printStackTrace();
        }
        AppLog.i("CameraAction", "end triggerCapturePhoto ret = " + ret);
        return ret;
    }

    public boolean startMovieRecord() {
        AppLog.i("CameraAction", "begin startVideoCapture");
        boolean ret = false;
        try {
            ret = this.cameraAction.startMovieRecord();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("CameraAction", "end startVideoCapture ret =" + ret);
        return ret;
    }

    public boolean startTimeLapse() {
        AppLog.i("CameraAction", "begin startTimeLapse");
        boolean ret = false;
        try {
            ret = this.cameraAction.startTimeLapse();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("CameraAction", "end startTimeLapse ret =" + ret);
        return ret;
    }

    public boolean stopTimeLapse() {
        AppLog.i("CameraAction", "begin stopMovieRecordTimeLapse");
        boolean ret = false;
        try {
            ret = this.cameraAction.stopTimeLapse();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("CameraAction", "end stopMovieRecordTimeLapse ret =" + ret);
        return ret;
    }

    public boolean stopVideoCapture() {
        AppLog.i("CameraAction", "begin stopVideoCapture");
        boolean ret = false;
        try {
            ret = this.cameraAction.stopMovieRecord();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("CameraAction", "end stopVideoCapture ret =" + ret);
        return ret;
    }

    public boolean formatStorage() {
        AppLog.i("CameraAction", "begin formatSD");
        boolean retVal = false;
        try {
            retVal = this.cameraAction.formatStorage();
        } catch (IchSocketException e) {
            AppLog.e("CameraAction", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStorageFormatException e4) {
            AppLog.e("CameraAction", "IchStorageFormatException");
            e4.printStackTrace();
        }
        AppLog.i("CameraAction", "begin formatSD retVal =" + retVal);
        return retVal;
    }

    public boolean sleepCamera() {
        AppLog.i("CameraAction", "begin sleepCamera");
        boolean retValue = false;
        try {
            retValue = this.cameraAction.toStandbyMode();
        } catch (IchDeviceException e) {
            try {
                AppLog.e("CameraAction", "IchDeviceException");
                e.printStackTrace();
            } catch (IchSocketException e2) {
                e2.printStackTrace();
            }
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("CameraAction", "end sleepCamera retValue =" + retValue);
        return retValue;
    }

    public boolean addCustomEventListener(int eventID, ICatchWificamListener listener) {
        AppLog.i("CameraAction", "begin addEventListener eventID=" + eventID);
        boolean retValue = false;
        try {
            retValue = this.cameraAction.addCustomEventListener(eventID, listener);
        } catch (IchListenerExistsException e) {
            e.printStackTrace();
        } catch (IchInvalidSessionException e2) {
            e2.printStackTrace();
        }
        AppLog.i("CameraAction", "end addEventListener retValue = " + retValue);
        return retValue;
    }

    public boolean delCustomEventListener(int eventID, ICatchWificamListener listener) {
        AppLog.i("CameraAction", "begin delEventListener eventID=" + eventID);
        boolean retValue = false;
        try {
            retValue = this.cameraAction.delCustomEventListener(eventID, listener);
        } catch (IchListenerNotExistsException e) {
            e.printStackTrace();
        } catch (IchInvalidSessionException e2) {
            e2.printStackTrace();
        }
        AppLog.i("CameraAction", "end delEventListener retValue = " + retValue);
        return retValue;
    }

    public boolean addEventListener(int eventID, ICatchWificamListener listener) {
        AppLog.i("CameraAction", "begin addEventListener eventID=" + eventID);
        boolean retValue = false;
        try {
            retValue = this.cameraAction.addEventListener(eventID, listener);
        } catch (IchListenerExistsException e) {
            AppLog.e("CameraAction", "IchListenerExistsException");
            e.printStackTrace();
        } catch (IchInvalidSessionException e2) {
            e2.printStackTrace();
        }
        AppLog.i("CameraAction", "end addEventListener retValue = " + retValue);
        return retValue;
    }

    public boolean delEventListener(int eventID, ICatchWificamListener listener) {
        AppLog.i("CameraAction", "begin delEventListener eventID=" + eventID);
        boolean retValue = false;
        try {
            retValue = this.cameraAction.delEventListener(eventID, listener);
        } catch (IchListenerNotExistsException e) {
            AppLog.e("CameraAction", "IchListenerExistsException");
            e.printStackTrace();
        } catch (IchInvalidSessionException e2) {
            e2.printStackTrace();
        }
        AppLog.i("CameraAction", "end delEventListener retValue = " + retValue);
        return retValue;
    }

    public static boolean addScanEventListener(ICatchWificamListener listener) {
        boolean z = false;
        if (listener != null) {
            z = false;
            try {
                z = ICatchWificamSession.addEventListener(85, listener, false);
            } catch (IchListenerExistsException e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public static boolean delScanEventListener(ICatchWificamListener listener) {
        if (listener == null) {
            return true;
        }
        boolean retValue = false;
        try {
            return ICatchWificamSession.delEventListener(85, listener, false);
        } catch (IchListenerNotExistsException e) {
            e.printStackTrace();
            return retValue;
        }
    }

    public String getCameraMacAddress() {
        String macAddress = BuildConfig.FLAVOR;
        return this.cameraAction.getMacAddress();
    }

    public boolean zoomIn() {
        AppLog.i("CameraAction", "begin zoomIn");
        boolean retValue = false;
        try {
            retValue = this.cameraAction.zoomIn();
        } catch (IchSocketException e) {
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            e2.printStackTrace();
        } catch (IchStorageFormatException e3) {
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            e4.printStackTrace();
        }
        AppLog.i("CameraAction", "end zoomIn retValue = " + retValue);
        return retValue;
    }

    public boolean zoomOut() {
        AppLog.i("CameraAction", "begin zoomOut");
        boolean retValue = false;
        try {
            retValue = this.cameraAction.zoomOut();
        } catch (IchSocketException e) {
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            e2.printStackTrace();
        } catch (IchStorageFormatException e3) {
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            e4.printStackTrace();
        }
        AppLog.i("CameraAction", "end zoomOut retValue = " + retValue);
        return retValue;
    }

    public boolean updateFW(String fileName) {
        boolean ret = false;
        AppLog.i("CameraAction", "begin update FW");
        try {
            ret = this.cameraAssist.updateFw(GlobalInfo.getInstance().getCurrentCamera().getSDKsession().getSDKSession(), fileName);
        } catch (IchInvalidSessionException e) {
            AppLog.e("CameraAction", "IchInvalidSessionException");
            e.printStackTrace();
        } catch (IchSocketException e2) {
            AppLog.e("CameraAction", "IchSocketException");
            e2.printStackTrace();
        } catch (IchCameraModeException e3) {
            AppLog.e("CameraAction", "IchCameraModeException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraAction", "IchDevicePropException");
            e4.printStackTrace();
        } catch (IchTimeOutException e5) {
            AppLog.e("CameraAction", "IchTimeOutException");
            e5.printStackTrace();
        } catch (IchDeviceException e6) {
            AppLog.e("CameraAction", "IchDeviceException");
            e6.printStackTrace();
        } catch (IchNotSupportedException e7) {
            AppLog.e("CameraAction", "IchNotSupportedException");
            e7.printStackTrace();
        }
        AppLog.i("CameraAction", "end updateFW ret=" + ret);
        return ret;
    }

    public static boolean addGlobalEventListener(int iCatchEventID, ICatchWificamListener listener, Boolean forAllSession) {
        boolean retValue = false;
        try {
            retValue = ICatchWificamSession.addEventListener(iCatchEventID, listener, forAllSession.booleanValue());
        } catch (IchListenerExistsException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    public static boolean delGlobalEventListener(int iCatchEventID, ICatchWificamListener listener, Boolean forAllSession) {
        boolean retValue = false;
        try {
            retValue = ICatchWificamSession.delEventListener(iCatchEventID, listener, forAllSession.booleanValue());
        } catch (IchListenerNotExistsException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    public boolean previewMove(int xshift, int yshfit) {
        AppLog.i("CameraAction", "begin previewMove");
        boolean ret = this.cameraAction.pan(xshift, yshfit);
        AppLog.i("CameraAction", "end previewMove ret = " + ret);
        return ret;
    }

    public boolean resetPreviewMove() {
        AppLog.i("CameraAction", "begin resetPreviewMove");
        boolean ret = this.cameraAction.panReset();
        AppLog.i("CameraAction", "end resetPreviewMove ret = " + ret);
        return ret;
    }
}
