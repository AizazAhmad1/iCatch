package com.icatch.ismartdv2016.GlobalApp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import com.icatch.ismartdv2016.AppDialog.AppDialog;
import com.icatch.ismartdv2016.BaseItems.BluetoothAppDevice;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.PhotoWallPreviewType;
import com.icatch.ismartdv2016.Listener.ScreenListener;
import com.icatch.ismartdv2016.Listener.ScreenListener.ScreenStateListener;
import com.icatch.ismartdv2016.Listener.WifiListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.PreviewMode;
import com.icatch.ismartdv2016.Model.Implement.SDKEvent;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Tools.ConnectCheckTimer;
import com.icatch.ismartdv2016.Tools.WifiCheck;
import com.icatch.ismartdv2016.View.Activity.PreviewActivity;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.slidingmenu.lib.R;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class GlobalInfo {
    public static final double THRESHOLD_TIME = 0.1d;
    public static BluetoothAppDevice curBtDevice;
    public static double curFps = 30.0d;
    public static int curVisibleItem = 0;
    public static int currentViewpagerPosition = 0;
    public static boolean enableReconnect = false;
    public static boolean enableSoftwareDecoder = false;
    public static ICatchBluetoothClient iCatchBluetoothClient;
    private static GlobalInfo instance;
    public static boolean isBLE = false;
    public static boolean isNeedGetBTClient = true;
    public static boolean isReconnecting = false;
    public static boolean isReleaseBTClient = true;
    public static PhotoWallPreviewType photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
    public static int videoCacheNum = 0;
    private final String TAG = "GlobalInfo";
    private Activity activity;
    private Handler appStartHandler;
    private List<MyCamera> cameraList;
    private MyCamera currentCamera;
    private Handler globalHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.styleable.SlidingMenu_shadowDrawable /*8*/:
                    AppLog.i("GlobalInfo", "receive EVENT_CONNECTION_FAILURE AppInfo.isReconnecting=" + GlobalInfo.isReconnecting);
                    GlobalInfo.enableReconnect = false;
                    if (!GlobalInfo.isReconnecting) {
                        GlobalInfo.isReconnecting = true;
                        GlobalInfo.this.wifiCheck = new WifiCheck(GlobalInfo.this.activity);
                        GlobalInfo.this.wifiCheck.showAutoReconnectProgressDialog();
                        return;
                    }
                    return;
                case R.styleable.SherlockTheme_actionModeSplitBackground /*16*/:
                    AppLog.i("GlobalInfo", "receive EVENT_SDCARD_REMOVED");
                    AppDialog.showDialogWarn(GlobalInfo.this.activity, (int) com.icatch.ismartdv2016.R.string.dialog_card_removed);
                    return;
                case PreviewMode.APP_STATE_VIDEO_MODE /*4098*/:
                    AppLog.i("GlobalInfo", "receive MESSAGE_CONNECTED");
                    String ssid = msg.obj.getSSID().replaceAll("\"", BuildConfig.FLAVOR);
                    if (ssid != null && !ssid.equals("0x") && !ssid.equals(BuildConfig.FLAVOR) && !ssid.equals("<unknown ssid>") && !ssid.equals(GlobalInfo.getInstance().getSsid())) {
                        AppDialog.showDialogQuit(GlobalInfo.this.activity, (int) com.icatch.ismartdv2016.R.string.message_connected_other_wifi);
                    } else if (ssid != null && ssid.equals(GlobalInfo.getInstance().getSsid())) {
                        GlobalInfo.enableReconnect = true;
                    }
                    AppLog.i("GlobalInfo", "receive MESSAGE_CONNECTED ssid=" + ssid);
                    return;
                case ConnectCheckTimer.MESSAGE_CONNECT_DISCONNECTED /*5633*/:
                    AppLog.i("GlobalInfo", "receive ConnectCheckTimer.MESSAGE_CONNECT_DISCONNECTED");
                    GlobalInfo.enableReconnect = false;
                    if (!GlobalInfo.isReconnecting) {
                        GlobalInfo.isReconnecting = true;
                        GlobalInfo.this.wifiCheck = new WifiCheck(GlobalInfo.this.activity);
                        GlobalInfo.this.wifiCheck.showAutoReconnectProgressDialog();
                    }
                    if (GlobalInfo.this.activity instanceof PreviewActivity) {
                        AppLog.d("GlobalInfo", "stopStream");
                        ((PreviewActivity) GlobalInfo.this.activity).stopStream();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private Handler handler = new Handler();
    ScreenListener listener;
    public List<LocalPbItemInfo> localPhotoList;
    public List<LocalPbItemInfo> localVideoList;
    public LruCache<Integer, Bitmap> mLruCache;
    public List<MultiPbItemInfo> photoInfoList;
    private SDKEvent sdkEvent;
    private String ssid;
    public List<MultiPbItemInfo> videoInfoList;
    private WifiCheck wifiCheck;
    WifiListener wifiListener;

    public Handler getAppStartHandler() {
        return this.appStartHandler;
    }

    public void setAppStartHandler(Handler appStartHandler) {
        this.appStartHandler = appStartHandler;
    }

    private GlobalInfo() {
    }

    public static GlobalInfo getInstance() {
        if (instance == null) {
            instance = new GlobalInfo();
        }
        return instance;
    }

    public void setSsid(String ssid) {
        AppLog.d("GlobalInfo", "setSsid = " + ssid);
        this.ssid = ssid;
    }

    public String getSsid() {
        return this.ssid;
    }

    public Context getAppContext() {
        return this.activity;
    }

    public void setCurrentApp(Activity activity) {
        this.activity = activity;
    }

    public Activity getCurrentApp() {
        AppLog.d("GlobalInfo", "getCurrentApp activity=" + this.activity);
        return this.activity;
    }

    public void setCurrentCamera(MyCamera myCamera) {
        this.currentCamera = myCamera;
    }

    public MyCamera getCurrentCamera() {
        return this.currentCamera;
    }

    public List<MyCamera> getCameraList() {
        return this.cameraList;
    }

    public void startScreenListener() {
        this.listener = new ScreenListener(getCurrentApp());
        this.listener.begin(new ScreenStateListener() {
            public void onUserPresent() {
                AppLog.i("GlobalInfo", "onUserPresent");
            }

            public void onScreenOn() {
                AppLog.i("GlobalInfo", "onScreenOn");
            }

            public void onScreenOff() {
                AppLog.i("GlobalInfo", "onScreenOff,need to close app!");
                ExitApp.getInstance().finishAllActivity();
            }
        });
    }

    public void endSceenListener() {
        if (this.listener != null) {
            this.listener.unregisterListener();
        }
    }

    public void addEventListener(int eventId, boolean forAllSession) {
        if (this.sdkEvent == null) {
            this.sdkEvent = new SDKEvent(this.globalHandler);
        }
        this.sdkEvent.addGlobalEventListener(eventId, Boolean.valueOf(forAllSession));
    }

    public void delEventListener(int eventId, boolean forAllSession) {
        if (this.sdkEvent != null) {
            this.sdkEvent.delGlobalEventListener(eventId, Boolean.valueOf(forAllSession));
        }
    }

    public void startWifiListener() {
        this.wifiListener = new WifiListener(this.activity.getApplicationContext(), this.globalHandler);
        this.wifiListener.registerReceiver();
    }

    public void endWifiListener() {
        if (this.wifiListener != null) {
            this.wifiListener.unregisterReceiver();
        }
    }

    public void startConnectCheck() {
        ConnectCheckTimer.startCheck(this.globalHandler);
    }

    public void stopConnectCheck() {
        ConnectCheckTimer.stopCheck();
    }

    public void showExceptionInfoDialog(final int messageID) {
        if (this.activity != null) {
            AppLog.d("GlobalInfo", "showExceptionInfoToast");
            this.handler.post(new Runnable() {
                public void run() {
                    AppDialog.showDialogWarn(GlobalInfo.this.activity, messageID);
                }
            });
        }
    }

    public void showExceptionInfoDialog(final String message) {
        if (this.activity != null) {
            AppLog.d("GlobalInfo", "showExceptionInfoToast");
            this.handler.post(new Runnable() {
                public void run() {
                    AppDialog.showDialogWarn(GlobalInfo.this.activity, message);
                }
            });
        }
    }
}
