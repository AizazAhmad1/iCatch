package com.icatch.ismartdv2016.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.GlobalApp.ExitApp;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.View.Activity.MultiPbActivity;
import com.icatch.ismartdv2016.View.Activity.PhotoPbActivity;
import com.icatch.ismartdv2016.View.Activity.PreviewActivity;
import com.icatch.ismartdv2016.View.Activity.VideoPbActivity;
import com.icatch.wificam.customer.type.ICatchCaptureDelay;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import uk.co.senab.photoview.BuildConfig;

public class WifiCheck {
    private static final int CONNECT_FAILED = 2;
    private static final int IN_BACKGROUND = 3;
    private static final int MESSAGE_CAMERA_CONNECT_FAIL = 10;
    private static final int RECONNECT_CAMERA = 9;
    private static int RECONNECT_CHECKING_PERIOD = 3000;
    private static final int RECONNECT_FAILED = 5;
    private static final int RECONNECT_SUCCESS = 4;
    private static int RECONNECT_TIME = 15;
    private static int RECONNECT_WAITING = ICatchCaptureDelay.ICH_CAP_DELAY_10S;
    public static final int WIFICIPHER_NOPASS = 6;
    public static final int WIFICIPHER_WAP = 8;
    public static final int WIFICIPHER_WEP = 7;
    private String TAG = "WifiCheck";
    private Activity activity;
    private int curReconnectTime = 0;
    private MyCamera currentCamera;
    private AlertDialog dialog;
    ExecutorService executor;
    private Handler handler;
    private Boolean isShowed = Boolean.valueOf(false);
    private WifiInfo mWifiInfo;
    private WifiManager mWifiManager;
    protected AlertDialog reconnectDialog;
    private Timer reconnectTimer;
    private final Handler wifiCheckHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WifiCheck.RECONNECT_CAMERA /*9*/:
                    WifiCheck.this.showReconnectDialog();
                    return;
                case WifiCheck.MESSAGE_CAMERA_CONNECT_FAIL /*10*/:
                    WifiCheck.this.showReconnectFailedDialog(R.string.message_reconnect_failed);
                    return;
                default:
                    return;
            }
        }
    };

    private class ReconnectTask extends TimerTask {
        private ReconnectTask() {
        }

        public void run() {
            WifiCheck.this.mWifiManager = (WifiManager) GlobalInfo.getInstance().getAppContext().getSystemService("wifi");
            WifiCheck.this.mWifiInfo = WifiCheck.this.mWifiManager.getConnectionInfo();
            final String ssid = WifiCheck.this.mWifiInfo.getSSID().replaceAll("\"", BuildConfig.FLAVOR);
            AppLog.d(WifiCheck.this.TAG, "reconnect mWifiInfo.getSSID()=" + ssid + " enableReconnect=" + GlobalInfo.enableReconnect);
            AppLog.d(WifiCheck.this.TAG, "reconnect GlobalInfo.getInstance().getSsid()=" + GlobalInfo.getInstance().getSsid());
            if (ssid.equals(GlobalInfo.getInstance().getSsid()) && GlobalInfo.enableReconnect) {
                AppLog.d(WifiCheck.this.TAG, "reconnect success!");
                if (WifiCheck.this.reconnectTimer != null) {
                    WifiCheck.this.reconnectTimer.cancel();
                }
                new Timer(true).schedule(new TimerTask() {
                    public void run() {
                        if (WifiCheck.this.reconnectDialog != null) {
                            WifiCheck.this.reconnectDialog.dismiss();
                        }
                        WifiCheck.this.reconnectCam();
                        MyProgressDialog.closeProgressDialog();
                    }
                }, 1000);
            }
            WifiCheck.this.curReconnectTime = WifiCheck.this.curReconnectTime + 1;
            AppLog.d(WifiCheck.this.TAG, "reconnect curReconnectTime=" + WifiCheck.this.curReconnectTime);
            if (WifiCheck.this.curReconnectTime > WifiCheck.RECONNECT_TIME) {
                if (WifiCheck.this.reconnectDialog != null) {
                    WifiCheck.this.reconnectDialog.dismiss();
                }
                if (WifiCheck.this.reconnectTimer != null) {
                    WifiCheck.this.reconnectTimer.cancel();
                }
                GlobalInfo.isReconnecting = false;
                MyProgressDialog.closeProgressDialog();
                WifiCheck.this.wifiCheckHandler.post(new Runnable() {
                    public void run() {
                        if (ssid == null || ssid.equals("0x") || ssid.equals("<unknown ssid>") || ssid.equals(BuildConfig.FLAVOR) || ssid.equals(GlobalInfo.getInstance().getSsid())) {
                            WifiCheck.this.showReconnectTimeoutDialog(R.string.message_reconnect_failed);
                        } else {
                            WifiCheck.this.showReconnectTimeoutDialog(R.string.message_connected_other_wifi);
                        }
                    }
                });
            }
        }
    }

    public WifiCheck(Activity activity) {
        this.activity = activity;
        this.mWifiManager = (WifiManager) activity.getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public WifiCheck(Activity activity, Handler handler) {
        this.handler = handler;
        this.activity = activity;
        this.mWifiManager = (WifiManager) activity.getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public void openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public int checkState() {
        return this.mWifiManager.getWifiState();
    }

    private WifiConfiguration isExsits(String SSID) {
        for (WifiConfiguration existingConfig : ((WifiManager) this.activity.getSystemService("wifi")).getConfiguredNetworks()) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public boolean connectWifi(String SSID, String Password, int Type) {
        AppLog.d(this.TAG, "connectWifi SSID=" + SSID + " Password=" + Password + " activity=" + this.activity);
        WifiManager wifiManager = (WifiManager) this.activity.getSystemService("wifi");
        int netID = wifiManager.addNetwork(CreateWifiInfo(SSID, Password, Type));
        AppLog.d(this.TAG, "connectWifi start enableNetwork netID=" + netID);
        boolean bRet = wifiManager.enableNetwork(netID, true);
        AppLog.d(this.TAG, "connectWifi end----bRet =" + bRet);
        return bRet;
    }

    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
        AppLog.d(this.TAG, "start CreateWifiInfo SSID=" + SSID);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WIFICIPHER_NOPASS) {
            config.wepKeys[0] = BuildConfig.FLAVOR;
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WIFICIPHER_WEP) {
            if (Password.length() != 0) {
                int length = Password.length();
                if ((length == MESSAGE_CAMERA_CONNECT_FAIL || length == 26 || length == 58) && Password.matches("[0-9A-Fa-f]*")) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = '\"' + Password + '\"';
                }
            }
            config.allowedKeyManagement.set(0);
            config.allowedAuthAlgorithms.set(0);
            config.allowedAuthAlgorithms.set(1);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WIFICIPHER_WAP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(CONNECT_FAILED);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedGroupCiphers.set(IN_BACKGROUND);
            config.allowedPairwiseCiphers.set(CONNECT_FAILED);
            config.status = CONNECT_FAILED;
        }
        AppLog.d(this.TAG, "end CreateWifiInfo config=" + config);
        return config;
    }

    public boolean isWifiConnected(Context context, String nameFilter) {
        if (!(context == null || ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1) == null)) {
            WifiInfo wifiInfo = ((WifiManager) this.activity.getSystemService("wifi")).getConnectionInfo();
            AppLog.d(this.TAG, "isWifiConnected ssid=" + nameFilter + " getSSID()=" + wifiInfo.getSSID());
            if (wifiInfo.getIpAddress() != 0 && wifiInfo.getSSID().contains(nameFilter)) {
                return true;
            }
        }
        return false;
    }

    public void showConectFailureWarningDlg(Context context) {
        if (!this.isShowed.booleanValue()) {
            Builder builder = new Builder(context);
            builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(R.string.dialog_timeout);
            builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AppLog.d(WifiCheck.this.TAG, "showConectFailureWarningDlg exit connect");
                    ExitApp.getInstance().finishAllActivity();
                }
            });
            builder.setNegativeButton(R.string.dialog_btn_reconnect, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    WifiCheck.this.curReconnectTime = 0;
                    WifiCheck.this.reconnectTimer = new Timer(true);
                    WifiCheck.this.reconnectTimer.schedule(new ReconnectTask(), 3000, (long) WifiCheck.RECONNECT_CHECKING_PERIOD);
                    WifiCheck.this.wifiCheckHandler.obtainMessage(WifiCheck.RECONNECT_CAMERA).sendToTarget();
                }
            });
            if (this.dialog == null) {
                this.dialog = builder.create();
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }
    }

    private void showReconnectDialog() {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        Builder builder = new Builder(GlobalInfo.getInstance().getAppContext());
        builder.setIcon(R.drawable.warning).setTitle(R.string.dialog_btn_reconnect).setMessage(R.string.message_reconnect);
        builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppLog.d(WifiCheck.this.TAG, "showReconnectDialog exit connect");
                ExitApp.getInstance().finishAllActivity();
            }
        });
        this.reconnectDialog = builder.create();
        this.reconnectDialog.setCancelable(false);
        this.reconnectDialog.show();
    }

    private void showReconnectTimeoutDialog(int messageId) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        Builder builder = new Builder(GlobalInfo.getInstance().getAppContext());
        builder.setIcon(R.drawable.warning).setTitle(R.string.text_reconnect_timeout).setMessage(messageId);
        builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppLog.d(WifiCheck.this.TAG, "showReconnectTimeoutDialog exit connect");
                ExitApp.getInstance().finishAllActivity();
            }
        });
        this.reconnectDialog = builder.create();
        this.reconnectDialog.setCancelable(false);
        this.reconnectDialog.show();
    }

    private void showReconnectFailedDialog(int messageId) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        Builder builder = new Builder(GlobalInfo.getInstance().getAppContext());
        builder.setIcon(R.drawable.warning).setTitle(R.string.text_reconnect_failed).setMessage(messageId);
        builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppLog.d(WifiCheck.this.TAG, "showReconnectTimeoutDialog exit connect");
                ExitApp.getInstance().finishAllActivity();
            }
        });
        this.reconnectDialog = builder.create();
        this.reconnectDialog.setCancelable(false);
        this.reconnectDialog.show();
    }

    public void showAutoReconnectDialog() {
        this.reconnectTimer = new Timer(true);
        this.reconnectTimer.schedule(new ReconnectTask(), 0, (long) RECONNECT_CHECKING_PERIOD);
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        Builder builder = new Builder(GlobalInfo.getInstance().getAppContext());
        builder.setIcon(R.drawable.warning).setTitle(R.string.dialog_btn_reconnect).setMessage(R.string.message_reconnect);
        builder.setPositiveButton(R.string.dialog_btn_exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppLog.d(WifiCheck.this.TAG, "showAutoReconnectDialog exit connect");
                ExitApp.getInstance().finishAllActivity();
            }
        });
        this.reconnectDialog = builder.create();
        this.reconnectDialog.setCancelable(false);
        this.reconnectDialog.show();
    }

    public void showAutoReconnectProgressDialog() {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        closeWifi();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openWifi();
        MyProgressDialog.showProgressDialog(GlobalInfo.getInstance().getAppContext(), (int) R.string.text_reconnection);
        this.reconnectTimer = new Timer(true);
        this.reconnectTimer.schedule(new ReconnectTask(), 3000, (long) RECONNECT_CHECKING_PERIOD);
    }

    private void reconnectCam() {
        AppLog.d(this.TAG, "start reconnectCam");
        MyCamera myCamera = new MyCamera();
        if (!myCamera.getSDKsession().prepareSession("192.168.1.1")) {
            GlobalInfo.isReconnecting = false;
            this.wifiCheckHandler.obtainMessage(MESSAGE_CAMERA_CONNECT_FAIL).sendToTarget();
        } else if (myCamera.getSDKsession().checkWifiConnection()) {
            GlobalInfo.getInstance().setCurrentCamera(myCamera);
            myCamera.initCamera();
            if (CameraProperties.getInstance().hasFuction(PropertyId.CAMERA_DATE)) {
                CameraProperties.getInstance().setCameraDate();
            }
            GlobalInfo.isReconnecting = false;
            myCamera.setMyMode(1);
            Activity activity = GlobalInfo.getInstance().getCurrentApp();
            AppLog.d(this.TAG, "reconnectCam curActivity=" + activity.getClass().getSimpleName());
            GlobalInfo.getInstance().startConnectCheck();
            if (activity instanceof PreviewActivity) {
                AppLog.d(this.TAG, "reconnectCam 1");
                ((PreviewActivity) activity).refresh();
            } else if (activity instanceof MultiPbActivity) {
                AppLog.d(this.TAG, "reconnectCam 2");
            } else if (activity instanceof VideoPbActivity) {
                AppLog.d(this.TAG, "reconnectCam 3");
                ((VideoPbActivity) activity).refresh();
            } else if (activity instanceof PhotoPbActivity) {
                AppLog.d(this.TAG, "reconnectCam 4");
            } else {
                AppLog.d(this.TAG, "reconnectCam 5");
            }
        } else {
            GlobalInfo.isReconnecting = false;
            this.wifiCheckHandler.obtainMessage(MESSAGE_CAMERA_CONNECT_FAIL).sendToTarget();
        }
    }
}
