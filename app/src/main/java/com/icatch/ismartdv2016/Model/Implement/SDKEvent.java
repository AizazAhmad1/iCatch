package com.icatch.ismartdv2016.Model.Implement;

import android.os.Handler;
import android.util.Log;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.Beans.SearchedCameraInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import com.icatch.wificam.customer.type.ICatchEvent;
import com.icatch.wificam.customer.type.ICatchEventID;
import com.slidingmenu.lib.R;

public class SDKEvent {
    public static final int EVENT_BATTERY_ELETRIC_CHANGED = 0;
    public static final int EVENT_CAPTURE_COMPLETED = 1;
    public static final int EVENT_CAPTURE_START = 3;
    public static final int EVENT_CONNECTION_FAILURE = 8;
    public static final int EVENT_FILE_ADDED = 7;
    public static final int EVENT_FILE_DOWNLOAD = 11;
    public static final int EVENT_FW_UPDATE_COMPLETED = 13;
    public static final int EVENT_FW_UPDATE_POWEROFF = 14;
    public static final int EVENT_SDCARD_INSERT = 17;
    public static final int EVENT_SDCARD_REMOVED = 16;
    public static final int EVENT_SD_CARD_FULL = 4;
    public static final int EVENT_SEARCHED_NEW_CAMERA = 15;
    public static final int EVENT_SERVER_STREAM_ERROR = 10;
    public static final int EVENT_TIME_LAPSE_STOP = 9;
    public static final int EVENT_VIDEO_OFF = 5;
    public static final int EVENT_VIDEO_ON = 6;
    public static final int EVENT_VIDEO_RECORDING_TIME = 12;
    private static final String TAG = "SDKEvent";
    private BatteryStateListener batteryStateListener;
    private CameraAction cameraAction = CameraAction.getInstance();
    private CaptureDoneListener captureDoneListener;
    private CaptureStartListener captureStartListener;
    private ConnectionFailureListener connectionFailureListener;
    private FileAddedListener fileAddedListener;
    private FileDownloadListener fileDownloadListener;
    private Handler handler;
    private InsertSdcardListener insertSdcardListener;
    private NoSdcardListener noSdcardListener;
    private ScanCameraListener scanCameraListener;
    private SdcardStateListener sdcardStateListener;
    private ServerStreamErrorListener serverStreamErrorListener;
    private TimeLapseStopListener timeLapseStopListener;
    private UpdateFWCompletedListener updateFWCompletedListener;
    private UpdateFWPoweroffListener updateFWPoweroffListener;
    private VideoOffListener videoOffListener;
    private VideoOnListener videoOnListener;
    private VideoRecordingTimeStartListener videoRecordingTimeStartListener;

    public class BatteryStateListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_BATTERY_ELETRIC_CHANGED).sendToTarget();
        }
    }

    public class CaptureDoneListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:capture done");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_CAPTURE_COMPLETED).sendToTarget();
        }
    }

    public class CaptureStartListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:capture start");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_CAPTURE_START).sendToTarget();
        }
    }

    public class ConnectionFailureListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:ConnectionFailureListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_CONNECTION_FAILURE).sendToTarget();
        }
    }

    public class FileAddedListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:FileAddedListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_FILE_ADDED).sendToTarget();
        }
    }

    public class FileDownloadListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:FileDownloadListener");
            Log.d("1111", "receive event:FileDownloadListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_FILE_DOWNLOAD, arg0.getFileValue1()).sendToTarget();
        }
    }

    public class InsertSdcardListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive InsertSdcardListener");
            AppInfo.isSdCardExist = true;
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_SDCARD_INSERT).sendToTarget();
            AppLog.i(SDKEvent.TAG, "receive InsertSdcardListener GlobalInfo.isSdCard = " + AppInfo.isSdCardExist);
        }
    }

    public class NoSdcardListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive NoSdcardListener");
            AppInfo.isSdCardExist = false;
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_SDCARD_REMOVED).sendToTarget();
            AppLog.i(SDKEvent.TAG, "receive NoSdcardListener GlobalInfo.isSdCard = " + AppInfo.isSdCardExist);
        }
    }

    public class ScanCameraListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "Send..........EVENT_SEARCHED_NEW_CAMERA");
            Log.d("1111", "get a uid arg0.getgetStringValue3() ==" + arg0.getStringValue3());
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_SEARCHED_NEW_CAMERA, new SearchedCameraInfo(arg0.getStringValue2(), arg0.getStringValue1(), arg0.getIntValue1(), arg0.getStringValue3())).sendToTarget();
        }
    }

    public class SdcardStateListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_SD_CARD_FULL).sendToTarget();
            AppLog.i(SDKEvent.TAG, "event: EVENT_SD_CARD_FULL");
        }
    }

    public class ServerStreamErrorListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:ServerStreamErrorListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_SERVER_STREAM_ERROR).sendToTarget();
        }
    }

    public class TimeLapseStopListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:TimeLapseStopListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_TIME_LAPSE_STOP).sendToTarget();
        }
    }

    public class UpdateFWCompletedListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive UpdateFWCompletedListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_FW_UPDATE_COMPLETED).sendToTarget();
        }
    }

    public class UpdateFWPoweroffListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive UpdateFWPoweroffListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_FW_UPDATE_POWEROFF).sendToTarget();
        }
    }

    public class VideoOffListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:videooff");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_VIDEO_OFF).sendToTarget();
        }
    }

    public class VideoOnListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive event:videoON");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_VIDEO_ON).sendToTarget();
        }
    }

    public class VideoRecordingTimeStartListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            AppLog.i(SDKEvent.TAG, "--------------receive VideoRecordingTimeStartListener");
            SDKEvent.this.handler.obtainMessage(SDKEvent.EVENT_VIDEO_RECORDING_TIME).sendToTarget();
        }
    }

    public SDKEvent(Handler handler) {
        this.handler = handler;
    }

    public void addEventListener(int iCatchEventID) {
        if (iCatchEventID == EVENT_SDCARD_INSERT) {
            this.sdcardStateListener = new SdcardStateListener();
            this.cameraAction.addEventListener(EVENT_SDCARD_INSERT, this.sdcardStateListener);
        }
        if (iCatchEventID == 36) {
            this.batteryStateListener = new BatteryStateListener();
            this.cameraAction.addEventListener(36, this.batteryStateListener);
        }
        if (iCatchEventID == 82) {
            this.captureStartListener = new CaptureStartListener();
            this.cameraAction.addEventListener(82, this.captureStartListener);
        }
        if (iCatchEventID == 35) {
            this.captureDoneListener = new CaptureDoneListener();
            this.cameraAction.addEventListener(35, this.captureDoneListener);
        }
        if (iCatchEventID == 34) {
            this.videoOffListener = new VideoOffListener();
            this.cameraAction.addEventListener(34, this.videoOffListener);
        }
        if (iCatchEventID == EVENT_CAPTURE_COMPLETED) {
            this.fileAddedListener = new FileAddedListener();
            this.cameraAction.addEventListener(EVENT_CAPTURE_COMPLETED, this.fileAddedListener);
        }
        if (iCatchEventID == 33) {
            this.videoOnListener = new VideoOnListener();
            this.cameraAction.addEventListener(33, this.videoOnListener);
        }
        if (iCatchEventID == 81) {
            this.timeLapseStopListener = new TimeLapseStopListener();
            this.cameraAction.addEventListener(81, this.timeLapseStopListener);
        }
        if (iCatchEventID == 65) {
            this.serverStreamErrorListener = new ServerStreamErrorListener();
            this.cameraAction.addEventListener(65, this.serverStreamErrorListener);
        }
        if (iCatchEventID == 99) {
            this.fileDownloadListener = new FileDownloadListener();
            this.cameraAction.addEventListener(99, this.fileDownloadListener);
        }
        if (iCatchEventID == 97) {
            this.updateFWCompletedListener = new UpdateFWCompletedListener();
            this.cameraAction.addEventListener(97, this.updateFWCompletedListener);
        }
        if (iCatchEventID == 98) {
            this.updateFWPoweroffListener = new UpdateFWPoweroffListener();
            this.cameraAction.addEventListener(98, this.updateFWPoweroffListener);
        }
        if (iCatchEventID == 19) {
            this.noSdcardListener = new NoSdcardListener();
            this.cameraAction.addEventListener(19, this.noSdcardListener);
        }
        if (iCatchEventID == 74) {
            this.connectionFailureListener = new ConnectionFailureListener();
            this.cameraAction.addEventListener(74, this.connectionFailureListener);
        }
    }

    public void addGlobalEventListener(int iCatchEventID, Boolean forAllSession) {
        AppLog.i(TAG, "Start addGlobalEventListener  iCatchEventID=" + iCatchEventID);
        switch (iCatchEventID) {
            case R.styleable.SherlockTheme_actionModePopupWindowStyle /*19*/:
                this.noSdcardListener = new NoSdcardListener();
                CameraAction.addGlobalEventListener(19, this.noSdcardListener, forAllSession);
                break;
            case ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED /*74*/:
                this.connectionFailureListener = new ConnectionFailureListener();
                CameraAction.addGlobalEventListener(74, this.connectionFailureListener, forAllSession);
                break;
            case ICatchEventID.ICATCH_EVENT_DEVICE_SCAN_ADD /*85*/:
                this.scanCameraListener = new ScanCameraListener();
                CameraAction.addGlobalEventListener(85, this.scanCameraListener, forAllSession);
                break;
        }
        AppLog.i(TAG, "End addGlobalEventListener");
    }

    public void delGlobalEventListener(int iCatchEventID, Boolean forAllSession) {
        AppLog.i(TAG, "Start delGlobalEventListener iCatchEventID=" + iCatchEventID);
        switch (iCatchEventID) {
            case R.styleable.SherlockTheme_actionModePopupWindowStyle /*19*/:
                if (this.noSdcardListener != null) {
                    CameraAction.delGlobalEventListener(19, this.noSdcardListener, forAllSession);
                    break;
                }
                break;
            case ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED /*74*/:
                if (this.connectionFailureListener != null) {
                    CameraAction.delGlobalEventListener(74, this.connectionFailureListener, forAllSession);
                    break;
                }
                break;
            case ICatchEventID.ICATCH_EVENT_DEVICE_SCAN_ADD /*85*/:
                if (this.scanCameraListener != null) {
                    CameraAction.delGlobalEventListener(85, this.scanCameraListener, forAllSession);
                    break;
                }
                break;
        }
        AppLog.i(TAG, "End delGlobalEventListener");
    }

    public void addCustomizeEvent(int eventID) {
        switch (eventID) {
            case 14081:
                this.insertSdcardListener = new InsertSdcardListener();
                this.cameraAction.addCustomEventListener(eventID, this.insertSdcardListener);
                return;
            case ICatchCameraProperty.ICH_CAP_BATTERY_LEVEL /*20481*/:
                this.videoRecordingTimeStartListener = new VideoRecordingTimeStartListener();
                this.cameraAction.addCustomEventListener(eventID, this.videoRecordingTimeStartListener);
                return;
            default:
                return;
        }
    }

    public void delCustomizeEventListener(int eventID) {
        switch (eventID) {
            case 14081:
                if (this.insertSdcardListener != null) {
                    this.cameraAction.delCustomEventListener(eventID, this.insertSdcardListener);
                    return;
                }
                return;
            case ICatchCameraProperty.ICH_CAP_BATTERY_LEVEL /*20481*/:
                if (this.videoRecordingTimeStartListener != null) {
                    this.cameraAction.delCustomEventListener(eventID, this.videoRecordingTimeStartListener);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void delEventListener(int iCatchEventID) {
        if (iCatchEventID == EVENT_SDCARD_INSERT && this.sdcardStateListener != null) {
            this.cameraAction.delEventListener(EVENT_SDCARD_INSERT, this.sdcardStateListener);
        }
        if (iCatchEventID == 36 && this.batteryStateListener != null) {
            this.cameraAction.delEventListener(36, this.batteryStateListener);
        }
        if (iCatchEventID == 35 && this.captureDoneListener != null) {
            this.cameraAction.delEventListener(35, this.captureDoneListener);
        }
        if (iCatchEventID == 82 && this.captureStartListener != null) {
            this.cameraAction.delEventListener(82, this.captureStartListener);
        }
        if (iCatchEventID == 34 && this.videoOffListener != null) {
            this.cameraAction.delEventListener(34, this.videoOffListener);
        }
        if (iCatchEventID == EVENT_CAPTURE_COMPLETED && this.fileAddedListener != null) {
            this.cameraAction.delEventListener(EVENT_CAPTURE_COMPLETED, this.fileAddedListener);
        }
        if (iCatchEventID == 33 && this.videoOnListener != null) {
            this.cameraAction.delEventListener(33, this.videoOnListener);
        }
        if (iCatchEventID == 81 && this.timeLapseStopListener != null) {
            this.cameraAction.delEventListener(81, this.timeLapseStopListener);
        }
        if (iCatchEventID == 65 && this.serverStreamErrorListener != null) {
            this.cameraAction.delEventListener(65, this.serverStreamErrorListener);
        }
        if (iCatchEventID == 99 && this.fileDownloadListener != null) {
            this.cameraAction.delEventListener(99, this.fileDownloadListener);
        }
        if (iCatchEventID == 97 && this.updateFWCompletedListener != null) {
            this.cameraAction.delEventListener(97, this.updateFWCompletedListener);
        }
        if (iCatchEventID == 98 && this.updateFWPoweroffListener != null) {
            this.cameraAction.delEventListener(98, this.updateFWPoweroffListener);
        }
        if (iCatchEventID == 19 && this.noSdcardListener != null) {
            this.cameraAction.delEventListener(19, this.noSdcardListener);
        }
        if (iCatchEventID == 74 && this.connectionFailureListener != null) {
            Log.d("1111", "connectionFailureListener != null");
            this.cameraAction.delEventListener(74, this.connectionFailureListener);
        }
    }
}
