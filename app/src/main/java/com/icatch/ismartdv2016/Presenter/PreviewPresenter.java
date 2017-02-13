package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.icatch.ismartdv2016.Adapter.SettingListAdapter;
import com.icatch.ismartdv2016.Adapter.SettingListAdapter.OnItemClickListener;
import com.icatch.ismartdv2016.AppDialog.AppDialog;
import com.icatch.ismartdv2016.AppDialog.AppToast;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.SlowMotion;
import com.icatch.ismartdv2016.BaseItems.Tristate;
import com.icatch.ismartdv2016.Beans.SettingMenu;
import com.icatch.ismartdv2016.Beans.StreamInfo;
import com.icatch.ismartdv2016.CustomException.NullPointerException;
import com.icatch.ismartdv2016.DataConvert.StreamInfoConvert;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.Function.PhotoCapture;
import com.icatch.ismartdv2016.Function.PhotoCapture.OnStopPreviewListener;
import com.icatch.ismartdv2016.Function.ZoomInOut;
import com.icatch.ismartdv2016.Function.ZoomInOut.ZoomCompletedListener;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnSettingCompleteListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.Mode.PreviewMode;
import com.icatch.ismartdv2016.Model.Implement.SDKEvent;
import com.icatch.ismartdv2016.Model.UIDisplaySource;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.SdkApi.CameraState;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SdkApi.PreviewStream;
import com.icatch.ismartdv2016.Setting.OptionSetting;
import com.icatch.ismartdv2016.ThumbnailGetting.ThumbnailOperation;
import com.icatch.ismartdv2016.Tools.BitmapTools;
import com.icatch.ismartdv2016.Tools.ConvertTools;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileOper;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileTools;
import com.icatch.ismartdv2016.Tools.TimeTools;
import com.icatch.ismartdv2016.View.Interface.PreviewView;
import com.icatch.wificam.customer.ICatchWificamConfig;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import com.icatch.wificam.customer.type.ICatchCustomerStreamParam;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchMJPGStreamParam;
import com.icatch.wificam.customer.type.ICatchMode;
import com.icatch.wificam.customer.type.ICatchPreviewMode;
import com.slidingmenu.lib.SlidingMenu;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import uk.co.senab.photoview.BuildConfig;
import uk.co.senab.photoview.IPhotoView;

public class PreviewPresenter extends BasePresenter {
    private static final String TAG = "PreviewPresenter";
    private Activity activity;
    private boolean allowClickButtoms = true;
    private CameraAction cameraAction;
    private ICatchWificamPreview cameraPreviewStreamClint;
    private CameraProperties cameraProperties;
    private CameraState cameraState;
    private MediaPlayer continuousCaptureBeep;
    private int curMode = 0;
    private MyCamera currentCamera;
    private int currentSettingMenuMode;
    private FileOperation fileOperation;
    private int lapseTime = 0;
    private long lastCilckTime = 0;
    private long lastRecodeTime;
    private MediaPlayer modeSwitchBeep;
    private PreviewHandler previewHandler;
    private PreviewStream previewStream;
    private PreviewView previewView;
    private Timer recordingLapseTimeTimer;
    private Tristate ret;
    private SDKEvent sdkEvent;
    private SettingListAdapter settingListAdapter;
    private List<SettingMenu> settingMenuList;
    private MediaPlayer stillCaptureStartBeep;
    private Boolean supportStreaming = Boolean.valueOf(true);
    public boolean videoCaptureButtomChangeFlag = true;
    private Timer videoCaptureButtomChangeTimer;
    private MediaPlayer videoCaptureStartBeep;
    private WifiSSReceiver wifiSSReceiver;

    private class PreviewHandler extends Handler {
        private PreviewHandler() {
        }

        public void handleMessage(Message msg) {
            Tristate ret = Tristate.FALSE;
            switch (msg.what) {
                case SlidingMenu.TOUCHMODE_MARGIN /*0*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_BATTERY_ELETRIC_CHANGED power =" + msg.arg1);
                    return;
                case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_CAPTURE_COMPLETED:curMode=" + PreviewPresenter.this.curMode);
                    if (PreviewPresenter.this.curMode == 2) {
                        ret = PreviewPresenter.this.startMediaStream(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
                        if (ret != Tristate.FALSE) {
                            if (ret == Tristate.NORMAL) {
                                PreviewPresenter.this.previewView.startMPreview(PreviewPresenter.this.currentCamera);
                            }
                            PreviewPresenter.this.previewView.setCaptureBtnEnability(true);
                            PreviewPresenter.this.previewView.setCaptureBtnBackgroundResource(R.drawable.still_capture_btn);
                            PreviewPresenter.this.previewView.setRemainCaptureCount(new Integer(PreviewPresenter.this.cameraProperties.getRemainImageNum()).toString());
                            PreviewPresenter.this.curMode = 1;
                            return;
                        }
                        return;
                    } else if (PreviewPresenter.this.curMode == 6) {
                        PreviewPresenter.this.previewView.setCaptureBtnEnability(true);
                        PreviewPresenter.this.previewView.setCaptureBtnBackgroundResource(R.drawable.still_capture_btn);
                        PreviewPresenter.this.previewView.setRemainCaptureCount(new Integer(PreviewPresenter.this.cameraProperties.getRemainImageNum()).toString());
                        MyToast.show(PreviewPresenter.this.activity, (int) R.string.capture_completed);
                        return;
                    } else {
                        return;
                    }
                case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_CAPTURE_START:curMode=" + PreviewPresenter.this.curMode);
                    if (PreviewPresenter.this.curMode == 6) {
                        PreviewPresenter.this.continuousCaptureBeep.start();
                        MyToast.show(PreviewPresenter.this.activity, (int) R.string.capture_start);
                        return;
                    }
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_SD_CARD_FULL");
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_behindScrollScale /*5*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_VIDEO_OFF:curMode=" + PreviewPresenter.this.curMode);
                    if (PreviewPresenter.this.curMode == 4 || PreviewPresenter.this.curMode == 5) {
                        if (PreviewPresenter.this.curMode == 4) {
                            PreviewPresenter.this.curMode = 3;
                        } else {
                            PreviewPresenter.this.curMode = 7;
                        }
                        PreviewPresenter.this.stopVideoCaptureButtomChangeTimer();
                        PreviewPresenter.this.stopRecordingLapseTimeTimer();
                        PreviewPresenter.this.previewView.setRemainRecordingTimeText(ConvertTools.secondsToMinuteOrHours(PreviewPresenter.this.cameraProperties.getRecordingRemainTime()));
                        return;
                    }
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_touchModeAbove /*6*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_VIDEO_ON:curMode =" + PreviewPresenter.this.curMode);
                    if (PreviewPresenter.this.curMode == 3) {
                        PreviewPresenter.this.curMode = 4;
                        PreviewPresenter.this.startVideoCaptureButtomChangeTimer();
                        PreviewPresenter.this.startRecordingLapseTimeTimer(0);
                        return;
                    } else if (PreviewPresenter.this.curMode == 7) {
                        PreviewPresenter.this.curMode = 5;
                        PreviewPresenter.this.startVideoCaptureButtomChangeTimer();
                        PreviewPresenter.this.startRecordingLapseTimeTimer(0);
                        return;
                    } else {
                        return;
                    }
                case com.slidingmenu.lib.R.styleable.SlidingMenu_touchModeBehind /*7*/:
                    AppLog.i(PreviewPresenter.TAG, "EVENT_FILE_ADDED");
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_shadowDrawable /*8*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_CONNECTION_FAILURE");
                    PreviewPresenter.this.stopMediaStream();
                    PreviewPresenter.this.delEvent();
                    PreviewPresenter.this.previewView.stopMPreview(PreviewPresenter.this.currentCamera);
                    PreviewPresenter.this.destroyCamera();
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_shadowWidth /*9*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_TIME_LAPSE_STOP:curMode=" + PreviewPresenter.this.curMode);
                    if (PreviewPresenter.this.curMode == 5) {
                        if (PreviewPresenter.this.cameraAction.stopTimeLapse()) {
                            PreviewPresenter.this.stopVideoCaptureButtomChangeTimer();
                            PreviewPresenter.this.stopRecordingLapseTimeTimer();
                            PreviewPresenter.this.previewView.setRemainCaptureCount(new Integer(PreviewPresenter.this.cameraProperties.getRemainImageNum()).toString());
                            PreviewPresenter.this.curMode = 7;
                            return;
                        }
                        return;
                    } else if (PreviewPresenter.this.curMode == 6 && PreviewPresenter.this.cameraAction.stopTimeLapse()) {
                        PreviewPresenter.this.stopRecordingLapseTimeTimer();
                        PreviewPresenter.this.previewView.setRemainCaptureCount(new Integer(PreviewPresenter.this.cameraProperties.getRemainImageNum()).toString());
                        PreviewPresenter.this.curMode = 8;
                        return;
                    } else {
                        return;
                    }
                case com.slidingmenu.lib.R.styleable.SlidingMenu_fadeDegree /*11*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_FILE_DOWNLOAD");
                    AppLog.d(PreviewPresenter.TAG, "receive EVENT_FILE_DOWNLOAD  msg.arg1 =" + msg.arg1);
                    if (!AppInfo.autoDownloadAllow) {
                        AppLog.d(PreviewPresenter.TAG, "GlobalInfo.autoDownload == false");
                        return;
                    } else if (Environment.getExternalStorageState().equals("mounted")) {
                        final String path = Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH;
                        if (((float) (FileTools.getFileSize(new File(path)) / 1024)) >= (AppInfo.autoDownloadSizeLimit * 1024.0f) * 1024.0f) {
                            AppLog.d(PreviewPresenter.TAG, "can not download because size limit");
                            return;
                        }
                        final ICatchFile file = msg.obj;
                        FileOper.createDirectory(path);
                        new Thread() {
                            public void run() {
                                AppLog.d(PreviewPresenter.TAG, "receive downloadFile file =" + file);
                                AppLog.d(PreviewPresenter.TAG, "receive downloadFile path =" + path);
                                boolean retvalue = PreviewPresenter.this.fileOperation.downloadFile(file, path + file.getFileName());
                                if (retvalue) {
                                    PreviewPresenter.this.previewHandler.post(new Runnable() {
                                        public void run() {
                                            PreviewPresenter.this.previewView.setAutoDownloadBitmap(BitmapTools.getImageByPath(path + file.getFileName(), BitmapTools.THUMBNAIL_WIDTH, BitmapTools.THUMBNAIL_WIDTH));
                                        }
                                    });
                                }
                                AppLog.d(PreviewPresenter.TAG, "receive downloadFile retvalue =" + retvalue);
                            }
                        }.start();
                        return;
                    } else {
                        return;
                    }
                case com.slidingmenu.lib.R.styleable.SlidingMenu_selectorEnabled /*12*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_VIDEO_RECORDING_TIME");
                    PreviewPresenter.this.startRecordingLapseTimeTimer(0);
                    return;
                case com.slidingmenu.lib.R.styleable.SherlockTheme_actionModeCloseDrawable /*17*/:
                    AppLog.i(PreviewPresenter.TAG, "receive EVENT_SDCARD_INSERT");
                    AppDialog.showDialogWarn(PreviewPresenter.this.activity, (int) R.string.dialog_card_inserted);
                    return;
                case AppMessage.SETTING_OPTION_AUTO_DOWNLOAD /*513*/:
                    AppLog.d(PreviewPresenter.TAG, "receive SETTING_OPTION_AUTO_DOWNLOAD");
                    if (msg.obj.booleanValue()) {
                        AppInfo.autoDownloadAllow = true;
                        PreviewPresenter.this.previewView.setAutoDownloadVisibility(0);
                        return;
                    }
                    AppInfo.autoDownloadAllow = false;
                    PreviewPresenter.this.previewView.setAutoDownloadVisibility(8);
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    }

    private class WifiSSReceiver extends BroadcastReceiver {
        private WifiManager wifi;

        public WifiSSReceiver() {
            this.wifi = (WifiManager) PreviewPresenter.this.activity.getSystemService("wifi");
            changeWifiStatusIcon();
        }

        public void onReceive(Context arg0, Intent arg1) {
            changeWifiStatusIcon();
        }

        private void changeWifiStatusIcon() {
            WifiInfo info = this.wifi.getConnectionInfo();
            String ssid = info.getSSID().replaceAll("\"", BuildConfig.FLAVOR);
            if (info.getBSSID() != null && ssid != null && ssid.equals(GlobalInfo.getInstance().getSsid())) {
                int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
                AppLog.d(PreviewPresenter.TAG, "change Wifi Status\uff1a" + strength);
                switch (strength) {
                    case SlidingMenu.TOUCHMODE_MARGIN /*0*/:
                        PreviewPresenter.this.previewView.setWifiIcon(R.drawable.ic_signal_wifi_0_bar_green_24dp);
                        return;
                    case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                        PreviewPresenter.this.previewView.setWifiIcon(R.drawable.ic_signal_wifi_1_bar_green_24dp);
                        return;
                    case SlidingMenu.TOUCHMODE_NONE /*2*/:
                        PreviewPresenter.this.previewView.setWifiIcon(R.drawable.ic_signal_wifi_2_bar_green_24dp);
                        return;
                    case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                        PreviewPresenter.this.previewView.setWifiIcon(R.drawable.ic_signal_wifi_3_bar_green_24dp);
                        return;
                    case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                        PreviewPresenter.this.previewView.setWifiIcon(R.drawable.ic_signal_wifi_4_bar_green_24dp);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public PreviewPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setView(PreviewView previewView) {
        this.previewView = previewView;
        initCfg();
    }

    public void initData() {
        this.cameraProperties = CameraProperties.getInstance();
        this.cameraAction = CameraAction.getInstance();
        this.cameraState = CameraState.getInstance();
        this.previewStream = PreviewStream.getInstance();
        this.fileOperation = FileOperation.getInstance();
        this.currentCamera = GlobalInfo.getInstance().getCurrentCamera();
        this.cameraPreviewStreamClint = this.currentCamera.getpreviewStreamClient();
        this.videoCaptureStartBeep = MediaPlayer.create(this.activity, R.raw.camera_timer);
        this.stillCaptureStartBeep = MediaPlayer.create(this.activity, R.raw.captureshutter);
        this.continuousCaptureBeep = MediaPlayer.create(this.activity, R.raw.captureburst);
        this.modeSwitchBeep = MediaPlayer.create(this.activity, R.raw.focusbeep);
        this.previewHandler = new PreviewHandler();
        this.sdkEvent = new SDKEvent(this.previewHandler);
        if (this.cameraProperties.hasFuction(PropertyId.CAPTURE_DELAY_MODE)) {
            this.cameraProperties.setCaptureDelayMode(1);
        }
        AppLog.i(TAG, "cameraProperties.getMaxZoomRatio() =" + this.cameraProperties.getMaxZoomRatio());
    }

    public void initStatus() {
        int resid = ThumbnailOperation.getBatteryLevelIcon();
        if (resid > 0) {
            this.previewView.setBatteryIcon(resid);
            if (resid == R.drawable.ic_battery_charging_green24dp) {
                AppDialog.showLowBatteryWarning(this.activity);
            }
        }
        IntentFilter wifiSSFilter = new IntentFilter("android.net.wifi.RSSI_CHANGED");
        this.wifiSSReceiver = new WifiSSReceiver();
        this.activity.registerReceiver(this.wifiSSReceiver, wifiSSFilter);
        GlobalInfo.getInstance().startWifiListener();
        GlobalInfo.getInstance().startConnectCheck();
    }

    public void changeCameraMode(final int previewMode, final ICatchPreviewMode ichVideoPreviewMode) {
        AppLog.i(TAG, "start changeCameraMode ichVideoPreviewMode=" + ichVideoPreviewMode);
        AppLog.i(TAG, "start changeCameraMode previewMode=" + previewMode);
        this.ret = Tristate.FALSE;
        this.previewView.setmPreviewVisibility(8);
        MyProgressDialog.showProgressDialog(this.activity, "processing..");
        new Thread(new Runnable() {
            public void run() {
                PreviewPresenter.this.ret = PreviewPresenter.this.startMediaStream(ichVideoPreviewMode);
                if (PreviewPresenter.this.ret == Tristate.NORMAL) {
                    PreviewPresenter.this.previewHandler.post(new Runnable() {
                        public void run() {
                            PreviewPresenter.this.curMode = previewMode;
                            PreviewPresenter.this.previewView.startMPreview(PreviewPresenter.this.currentCamera);
                            PreviewPresenter.this.createUIByMode(PreviewPresenter.this.curMode);
                            AppLog.i(PreviewPresenter.TAG, "startMPreview");
                            PreviewPresenter.this.supportStreaming = Boolean.valueOf(true);
                            MyProgressDialog.closeProgressDialog();
                            PreviewPresenter.this.previewView.dismissPopupWindow();
                        }
                    });
                } else if (PreviewPresenter.this.ret == Tristate.ABNORMAL) {
                    PreviewPresenter.this.previewHandler.post(new Runnable() {
                        public void run() {
                            PreviewPresenter.this.curMode = previewMode;
                            PreviewPresenter.this.createUIByMode(PreviewPresenter.this.curMode);
                            PreviewPresenter.this.previewView.setmPreviewVisibility(8);
                            PreviewPresenter.this.previewView.setSupportPreviewTxvVisibility(0);
                            PreviewPresenter.this.supportStreaming = Boolean.valueOf(false);
                            MyProgressDialog.closeProgressDialog();
                            PreviewPresenter.this.previewView.dismissPopupWindow();
                        }
                    });
                } else if (PreviewPresenter.this.ret == Tristate.FALSE) {
                    PreviewPresenter.this.previewHandler.post(new Runnable() {
                        public void run() {
                            MyToast.show(PreviewPresenter.this.activity, (int) R.string.stream_set_error);
                            PreviewPresenter.this.curMode = previewMode;
                            PreviewPresenter.this.createUIByMode(PreviewPresenter.this.curMode);
                            PreviewPresenter.this.supportStreaming = Boolean.valueOf(false);
                            MyProgressDialog.closeProgressDialog();
                            PreviewPresenter.this.previewView.dismissPopupWindow();
                        }
                    });
                }
            }
        }).start();
    }

    public boolean stopMediaStream() {
        return this.previewStream.stopMediaStream(this.cameraPreviewStreamClint);
    }

    public Tristate startMediaStream(ICatchPreviewMode ichVideoPreviewMode) {
        AppLog.d(TAG, "start startMediaStream");
        Tristate ret = Tristate.FALSE;
        String streamUrl = this.cameraProperties.getCurrentStreamInfo();
        AppLog.d(TAG, "start startMediaStream streamUrl=[" + streamUrl + "]");
        int cacheTime;
        if (streamUrl == null) {
            cacheTime = this.cameraProperties.getPreviewCacheTime();
            if (cacheTime > 0 && cacheTime < IPhotoView.DEFAULT_ZOOM_DURATION) {
                cacheTime = IPhotoView.DEFAULT_ZOOM_DURATION;
            }
            AppLog.d(TAG, "start startMediaStream setCacheRet=" + Boolean.valueOf(ICatchWificamConfig.getInstance().setPreviewCacheParam(cacheTime, IPhotoView.DEFAULT_ZOOM_DURATION)) + " cacheTime=" + cacheTime);
            return this.previewStream.startMediaStream(this.cameraPreviewStreamClint, new ICatchMJPGStreamParam(), ichVideoPreviewMode, AppInfo.disableAudio);
        }
        StreamInfo streamInfo = StreamInfoConvert.convertToStreamInfoBean(streamUrl);
        GlobalInfo.curFps = (double) streamInfo.fps;
        if (streamInfo.mediaCodecType.equals("MJPG")) {
            cacheTime = this.cameraProperties.getPreviewCacheTime();
            if (cacheTime > 0 && cacheTime < IPhotoView.DEFAULT_ZOOM_DURATION) {
                cacheTime = IPhotoView.DEFAULT_ZOOM_DURATION;
            }
            ICatchWificamConfig.getInstance().setPreviewCacheParam(cacheTime, IPhotoView.DEFAULT_ZOOM_DURATION);
            ICatchMJPGStreamParam param = new ICatchMJPGStreamParam(streamInfo.width, streamInfo.height, streamInfo.bitrate, 50);
            AppLog.i(TAG, "begin startMediaStream MJPG");
            ret = this.previewStream.startMediaStream(this.cameraPreviewStreamClint, param, ichVideoPreviewMode, AppInfo.disableAudio);
            AppLog.i(TAG, "end  startMediaStream ret = " + ret);
        } else if (streamInfo.mediaCodecType.equals("H264")) {
            if (this.cameraProperties.getPreviewCacheTime() < IPhotoView.DEFAULT_ZOOM_DURATION) {
                ICatchWificamConfig.getInstance().setPreviewCacheParam(500, IPhotoView.DEFAULT_ZOOM_DURATION);
            }
            if (GlobalInfo.enableSoftwareDecoder) {
                streamUrl = ConvertTools.resolutionConvert(streamUrl);
            }
            ICatchCustomerStreamParam param2 = new ICatchCustomerStreamParam(554, streamUrl);
            AppLog.i(TAG, "begin startMediaStream H264");
            ret = this.previewStream.startMediaStream(this.cameraPreviewStreamClint, param2, ichVideoPreviewMode, AppInfo.disableAudio);
            AppLog.i(TAG, "end  startMediaStream ret = " + ret);
        }
        AppInfo.disableAudio = !this.previewStream.supportAudio(this.cameraPreviewStreamClint);
        AppLog.d(TAG, "start startMediaStream disableAudio=" + AppInfo.disableAudio);
        AppLog.d(TAG, "start startMediaStream supportAudio=" + this.previewStream.supportAudio(this.cameraPreviewStreamClint));
        AppLog.i(TAG, "startMediaStream ret = " + ret);
        return ret;
    }

    public void startOrStopCapture() {
        AppLog.d(TAG, "begin startOrStopCapture curMode=" + this.curMode);
        if (!TimeTools.isFastClick()) {
            if (this.curMode == 3) {
                if (this.cameraProperties.isSDCardExist()) {
                    int remainTime = this.cameraProperties.getRecordingRemainTime();
                    if (remainTime == 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_sd_card_is_full);
                        return;
                    } else if (remainTime < 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.text_get_data_exception);
                        return;
                    } else {
                        this.videoCaptureStartBeep.start();
                        this.lastRecodeTime = System.currentTimeMillis();
                        if (this.cameraAction.startMovieRecord()) {
                            AppLog.i(TAG, "startRecordingLapseTimeTimer(0)");
                            this.curMode = 4;
                            startVideoCaptureButtomChangeTimer();
                            startRecordingLapseTimeTimer(0);
                        }
                    }
                } else {
                    AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_card_not_exist);
                    return;
                }
            } else if (this.curMode == 4) {
                this.videoCaptureStartBeep.start();
                if (System.currentTimeMillis() - this.lastRecodeTime < 2000) {
                    MyToast.show(this.activity, "Operation Frequent!");
                    return;
                } else if (this.cameraAction.stopVideoCapture()) {
                    this.curMode = 3;
                    stopVideoCaptureButtomChangeTimer();
                    stopRecordingLapseTimeTimer();
                    this.previewView.setRemainRecordingTimeText(ConvertTools.secondsToMinuteOrHours(this.cameraProperties.getRecordingRemainTime()));
                }
            } else if (this.curMode == 1) {
                if (this.cameraProperties.isSDCardExist()) {
                    remainImageNum = this.cameraProperties.getRemainImageNum();
                    if (remainImageNum == 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_sd_card_is_full);
                        return;
                    } else if (remainImageNum < 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.text_get_data_exception);
                        return;
                    } else {
                        this.curMode = 2;
                        startPhotoCapture();
                    }
                } else {
                    AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_card_not_exist);
                    return;
                }
            } else if (this.curMode == 8) {
                if (this.cameraProperties.isSDCardExist()) {
                    remainImageNum = this.cameraProperties.getRemainImageNum();
                    if (remainImageNum == 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_sd_card_is_full);
                        return;
                    } else if (remainImageNum < 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.text_get_data_exception);
                        return;
                    } else if (this.cameraProperties.getCurrentTimeLapseInterval() == 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.timeLapse_not_allow);
                        return;
                    } else {
                        this.continuousCaptureBeep.start();
                        if (this.cameraAction.startTimeLapse()) {
                            this.previewView.setCaptureBtnBackgroundResource(R.drawable.still_capture_btn_off);
                            this.curMode = 6;
                        } else {
                            AppLog.e(TAG, "failed to start startTimeLapse");
                            return;
                        }
                    }
                }
                AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_card_not_exist);
                return;
            } else if (this.curMode == 6) {
                AppLog.d(TAG, "curMode == PreviewMode.APP_STATE_TIMELAPSE_STILL_CAPTURE");
                if (this.cameraAction.stopTimeLapse()) {
                    stopRecordingLapseTimeTimer();
                    this.curMode = 8;
                } else {
                    AppLog.e(TAG, "failed to stopTimeLapse");
                    return;
                }
            } else if (this.curMode == 7) {
                AppLog.d(TAG, "curMode == PreviewMode.APP_STATE_TIMELAPSE_PREVIEW_VIDEO");
                if (this.cameraProperties.isSDCardExist()) {
                    remainImageNum = this.cameraProperties.getRemainImageNum();
                    if (remainImageNum == 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_sd_card_is_full);
                        return;
                    } else if (remainImageNum < 0) {
                        AppDialog.showDialogWarn(this.activity, (int) R.string.text_get_data_exception);
                        return;
                    } else if (this.cameraProperties.getCurrentTimeLapseInterval() == 0) {
                        AppLog.d(TAG, "time lapse is not allowed because of timelapse interval is OFF");
                        AppDialog.showDialogWarn(this.activity, (int) R.string.timeLapse_not_allow);
                        return;
                    } else {
                        this.videoCaptureStartBeep.start();
                        if (this.cameraAction.startTimeLapse()) {
                            this.curMode = 5;
                            startVideoCaptureButtomChangeTimer();
                            startRecordingLapseTimeTimer(0);
                        } else {
                            AppLog.e(TAG, "failed to start startTimeLapse");
                            return;
                        }
                    }
                }
                AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_card_not_exist);
                return;
            } else if (this.curMode == 5) {
                AppLog.d(TAG, "curMode == PreviewMode.APP_STATE_TIMELAPSE_VIDEO_CAPTURE");
                this.videoCaptureStartBeep.start();
                if (this.cameraAction.stopTimeLapse()) {
                    stopVideoCaptureButtomChangeTimer();
                    stopRecordingLapseTimeTimer();
                    this.curMode = 7;
                } else {
                    AppLog.e(TAG, "failed to stopTimeLapse");
                    return;
                }
            }
            AppLog.d(TAG, "end processing for responsing captureBtn clicking");
        }
    }

    public void createUIByMode(int previewMode) {
        AppLog.i(TAG, "start createUIByMode previewMode=" + previewMode);
        if (this.cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_VIDEO) && (previewMode == 3 || previewMode == 4)) {
            this.previewView.setPvModeBtnBackgroundResource(R.drawable.video_toggle_btn_on);
        }
        if (previewMode == 1 || previewMode == 2) {
            this.previewView.setPvModeBtnBackgroundResource(R.drawable.capture_toggle_btn_on);
        }
        AppLog.i(TAG, "cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)=" + this.cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE));
        if (this.cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) && (previewMode == 6 || previewMode == 8 || previewMode == 5 || previewMode == 7)) {
            this.previewView.setPvModeBtnBackgroundResource(R.drawable.timelapse_toggle_btn_on);
        }
        if (previewMode == 2 || previewMode == 6 || previewMode == 8 || previewMode == 1) {
            this.previewView.setCaptureBtnBackgroundResource(R.drawable.still_capture_btn);
        } else if (previewMode == 4 || previewMode == 5 || previewMode == 7 || previewMode == 3) {
            this.previewView.setCaptureBtnBackgroundResource(R.drawable.video_recording_btn_on);
        }
        if (this.currentCamera.getCaptureDelay().needDisplayByMode(previewMode).booleanValue()) {
            this.previewView.setDelayCaptureLayoutVisibility(0);
            this.previewView.setDelayCaptureTextTime(this.currentCamera.getCaptureDelay().getCurrentUiStringInPreview());
        } else {
            this.previewView.setDelayCaptureLayoutVisibility(8);
        }
        if (this.currentCamera.getImageSize().needDisplayByMode(previewMode).booleanValue()) {
            this.previewView.setImageSizeLayoutVisibility(0);
            this.previewView.setImageSizeInfo(this.currentCamera.getImageSize().getCurrentUiStringInPreview());
            this.previewView.setRemainCaptureCount(new Integer(this.cameraProperties.getRemainImageNum()).toString());
        } else {
            this.previewView.setImageSizeLayoutVisibility(8);
        }
        if (this.currentCamera.getVideoSize().needDisplayByMode(previewMode).booleanValue()) {
            this.previewView.setVideoSizeLayoutVisibility(0);
            this.previewView.setVideoSizeInfo(this.currentCamera.getVideoSize().getCurrentUiStringInPreview());
            this.previewView.setRemainRecordingTimeText(ConvertTools.secondsToMinuteOrHours(this.cameraProperties.getRecordingRemainTime()));
        } else {
            this.previewView.setVideoSizeLayoutVisibility(8);
        }
        if (this.currentCamera.getBurst().needDisplayByMode(previewMode).booleanValue()) {
            this.previewView.setBurstStatusVisibility(0);
            try {
                this.previewView.setBurstStatusIcon(this.currentCamera.getBurst().getCurrentIcon());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            this.previewView.setBurstStatusVisibility(8);
        }
        if (this.currentCamera.getWhiteBalance().needDisplayByMode(previewMode).booleanValue()) {
            this.previewView.setWbStatusVisibility(0);
            try {
                this.previewView.setWbStatusIcon(this.currentCamera.getWhiteBalance().getCurrentIcon());
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            }
        } else {
            this.previewView.setWbStatusVisibility(8);
        }
        if (this.currentCamera.getUpside().needDisplayByMode(previewMode).booleanValue() && this.cameraProperties.getCurrentUpsideDown() == 1) {
            this.previewView.setUpsideVisibility(0);
        } else {
            this.previewView.setUpsideVisibility(8);
        }
        if (this.currentCamera.getSlowMotion().needDisplayByMode(previewMode).booleanValue() && this.cameraProperties.getCurrentSlowMotion() == SlowMotion.SLOW_MOTION_ON) {
            this.previewView.setSlowMotionVisibility(0);
        } else {
            this.previewView.setSlowMotionVisibility(8);
        }
        if (this.currentCamera.getTimeLapseMode().needDisplayByMode(previewMode).booleanValue()) {
            this.previewView.settimeLapseModeVisibility(0);
            try {
                this.previewView.settimeLapseModeIcon(this.currentCamera.getTimeLapseMode().getCurrentIcon());
                return;
            } catch (NullPointerException e22) {
                e22.printStackTrace();
                return;
            }
        }
        this.previewView.settimeLapseModeVisibility(8);
    }

    public void startVideoCaptureButtomChangeTimer() {
        AppLog.d(TAG, "startVideoCaptureButtomChangeTimer videoCaptureButtomChangeTimer=" + this.videoCaptureButtomChangeTimer);
        TimerTask task = new TimerTask() {
            public void run() {
                if (PreviewPresenter.this.videoCaptureButtomChangeFlag) {
                    PreviewPresenter.this.videoCaptureButtomChangeFlag = false;
                    PreviewPresenter.this.previewHandler.post(new Runnable() {
                        public void run() {
                            if (PreviewPresenter.this.curMode == 4 || PreviewPresenter.this.curMode == 5) {
                                PreviewPresenter.this.previewView.setCaptureBtnBackgroundResource(R.drawable.video_recording_btn_on);
                            }
                        }
                    });
                    return;
                }
                PreviewPresenter.this.videoCaptureButtomChangeFlag = true;
                PreviewPresenter.this.previewHandler.post(new Runnable() {
                    public void run() {
                        if (PreviewPresenter.this.curMode == 4 || PreviewPresenter.this.curMode == 5) {
                            PreviewPresenter.this.previewView.setCaptureBtnBackgroundResource(R.drawable.video_recording_btn_off);
                        }
                    }
                });
            }
        };
        this.videoCaptureButtomChangeTimer = new Timer(true);
        this.videoCaptureButtomChangeTimer.schedule(task, 0, 1000);
    }

    public void initPreview() {
        AppLog.i(TAG, "initPreview curMode=" + this.curMode);
        this.previewView.setMaxZoomRate(this.cameraProperties.getMaxZoomRatio());
        this.previewView.updateZoomViewProgress(this.cameraProperties.getCurrentZoomRatio());
        if (this.cameraState.isMovieRecording()) {
            AppLog.i(TAG, "camera is recording...");
            changeCameraMode(4, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
            this.curMode = 4;
            startVideoCaptureButtomChangeTimer();
            startRecordingLapseTimeTimer(this.cameraProperties.getVideoRecordingTime());
        } else if (this.cameraState.isTimeLapseVideoOn()) {
            AppLog.i(TAG, "camera is TimeLapseVideoOn...");
            this.currentCamera.timeLapsePreviewMode = 1;
            changeCameraMode(5, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
            this.curMode = 5;
            startVideoCaptureButtomChangeTimer();
            startRecordingLapseTimeTimer(this.cameraProperties.getVideoRecordingTime());
        } else if (this.cameraState.isTimeLapseStillOn()) {
            AppLog.i(TAG, "camera is TimeLapseStillOn...");
            this.currentCamera.timeLapsePreviewMode = 0;
            changeCameraMode(6, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE);
            this.curMode = 6;
            startVideoCaptureButtomChangeTimer();
            startRecordingLapseTimeTimer(this.cameraProperties.getVideoRecordingTime());
        } else if (this.curMode == 0) {
            if (this.cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_VIDEO)) {
                this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
                changeCameraMode(3, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
                return;
            }
            changeCameraMode(1, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
        } else if (this.curMode == 3) {
            AppLog.i(TAG, "initPreview curMode == PreviewMode.APP_STATE_VIDEO_PREVIEW");
            this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
            changeCameraMode(this.curMode, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
        } else if (this.curMode == 7) {
            AppLog.i(TAG, "initPreview curMode == PreviewMode.APP_STATE_TIMELAPSE_PREVIEW_VIDEO");
            this.currentCamera.timeLapsePreviewMode = 1;
            this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
            changeCameraMode(this.curMode, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
        } else if (this.curMode == 8) {
            AppLog.i(TAG, "initPreview curMode == PreviewMode.APP_STATE_TIMELAPSE_PREVIEW_STILL");
            this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE);
            this.currentCamera.timeLapsePreviewMode = 0;
            changeCameraMode(this.curMode, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE);
        } else if (this.curMode == 1) {
            AppLog.i(TAG, "initPreview curMode == ICH_STILL_PREVIEW_MODE");
            changeCameraMode(this.curMode, ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
        }
    }

    public void stopVideoCaptureButtomChangeTimer() {
        AppLog.d(TAG, "stopVideoCaptureButtomChangeTimer videoCaptureButtomChangeTimer=" + this.videoCaptureButtomChangeTimer);
        if (this.videoCaptureButtomChangeTimer != null) {
            this.videoCaptureButtomChangeTimer.cancel();
        }
        this.previewView.setCaptureBtnBackgroundResource(R.drawable.video_recording_btn_on);
    }

    private void startRecordingLapseTimeTimer(int startTime) {
        if (this.cameraProperties.hasFuction(PropertyId.VIDEO_RECORDING_TIME)) {
            AppLog.i(TAG, "startRecordingLapseTimeTimer curMode=" + this.curMode);
            if (this.curMode == 4 || this.curMode == 5 || this.curMode == 6) {
                AppLog.i(TAG, "startRecordingLapseTimeTimer");
                if (this.recordingLapseTimeTimer != null) {
                    this.recordingLapseTimeTimer.cancel();
                }
                this.lapseTime = startTime;
                this.recordingLapseTimeTimer = new Timer(true);
                this.previewView.setRecordingTimeVisibility(0);
                this.recordingLapseTimeTimer.schedule(new TimerTask() {
                    public void run() {
                        PreviewPresenter.this.previewHandler.post(new Runnable() {
                            public void run() {
                                PreviewPresenter.this.previewView.setRecordingTime(ConvertTools.secondsToHours(PreviewPresenter.this.lapseTime = PreviewPresenter.this.lapseTime + 1));
                            }
                        });
                    }
                }, 0, 1000);
            }
        }
    }

    private void stopRecordingLapseTimeTimer() {
        if (this.recordingLapseTimeTimer != null) {
            this.recordingLapseTimeTimer.cancel();
        }
        this.previewView.setRecordingTime("00:00:00");
        this.previewView.setRecordingTimeVisibility(8);
    }

    public void changePreviewMode(int previewMode) {
        AppLog.d(TAG, "changePreviewMode previewMode=" + previewMode);
        AppLog.d(TAG, "changePreviewMode curMode=" + this.curMode);
        AppLog.d(TAG, "repeat click: timeInterval=" + (System.currentTimeMillis() - this.lastCilckTime));
        if (System.currentTimeMillis() - this.lastCilckTime < 2000) {
            AppLog.d(TAG, "repeat click: timeInterval < 2000");
            return;
        }
        this.lastCilckTime = System.currentTimeMillis();
        this.modeSwitchBeep.start();
        if (previewMode == PreviewMode.APP_STATE_VIDEO_MODE) {
            if (this.curMode == 2 || this.curMode == 5 || this.curMode == 6) {
                if (this.curMode == 2 || this.curMode == 6) {
                    MyToast.show(this.activity, (int) R.string.stream_error_capturing);
                } else if (this.curMode == 4 || this.curMode == 5) {
                    MyToast.show(this.activity, (int) R.string.stream_error_recording);
                }
            } else if (this.curMode == 1 || this.curMode == 8 || this.curMode == 7) {
                this.previewView.stopMPreview(this.currentCamera);
                stopMediaStream();
                this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
                changeCameraMode(3, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
            }
        } else if (previewMode == PreviewMode.APP_STATE_STILL_MODE) {
            if (this.curMode == 4 || this.curMode == 6 || this.curMode == 5) {
                if (this.curMode == 2 || this.curMode == 6) {
                    MyToast.show(this.activity, (int) R.string.stream_error_capturing);
                } else if (this.curMode == 4 || this.curMode == 5) {
                    MyToast.show(this.activity, (int) R.string.stream_error_recording);
                }
            } else if (this.curMode == 3 || this.curMode == 8 || this.curMode == 7) {
                this.previewView.stopMPreview(this.currentCamera);
                stopMediaStream();
                this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
                changeCameraMode(1, ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
            }
        } else if (previewMode != PreviewMode.APP_STATE_TIMELAPSE_MODE) {
        } else {
            if (this.curMode == 2 || this.curMode == 4) {
                if (this.curMode == 2) {
                    MyToast.show(this.activity, (int) R.string.stream_error_capturing);
                } else if (this.curMode == 4) {
                    MyToast.show(this.activity, (int) R.string.stream_error_recording);
                }
            } else if (this.curMode == 1 || this.curMode == 3) {
                this.previewView.stopMPreview(this.currentCamera);
                stopMediaStream();
                if (this.currentCamera.timeLapsePreviewMode == 1) {
                    this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
                    changeCameraMode(7, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
                } else if (this.currentCamera.timeLapsePreviewMode == 0) {
                    this.previewStream.changePreviewMode(this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE);
                    changeCameraMode(8, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE);
                }
            }
        }
    }

    private void startPhotoCapture() {
        this.previewView.setCaptureBtnEnability(false);
        this.previewView.setCaptureBtnBackgroundResource(R.drawable.still_capture_btn_off);
        if (this.cameraProperties.hasFuction(PropertyId.CAPTURE_DELAY_MODE)) {
            PhotoCapture.getInstance().addOnStopPreviewListener(new OnStopPreviewListener() {
                public void onStop() {
                    PreviewPresenter.this.stopPreview();
                    if (!PreviewPresenter.this.stopMediaStream()) {
                    }
                }
            });
            PhotoCapture.getInstance().startCapture();
            return;
        }
        this.stillCaptureStartBeep.start();
        stopPreview();
        if (stopMediaStream()) {
            CameraAction.getInstance().capturePhoto();
        }
    }

    public boolean destroyCamera() {
        return this.currentCamera.destroyCamera().booleanValue();
    }

    public void unregisterWifiSSReceiver() {
        if (this.wifiSSReceiver != null) {
            this.activity.unregisterReceiver(this.wifiSSReceiver);
        }
    }

    public void zoomIn() {
        if (this.curMode != 2 && this.curMode != 6) {
            ZoomInOut.getInstance().zoomIn();
            this.previewView.updateZoomViewProgress(this.cameraProperties.getCurrentZoomRatio());
        }
    }

    public void zoomOut() {
        if (this.curMode != 2 && this.curMode != 6) {
            ZoomInOut.getInstance().zoomOut();
            this.previewView.updateZoomViewProgress(this.cameraProperties.getCurrentZoomRatio());
        }
    }

    public void zoomBySeekBar() {
        ZoomInOut.getInstance().startZoomInOutThread(this);
        ZoomInOut.getInstance().addZoomCompletedListener(new ZoomCompletedListener() {
            public void onCompleted(final int currentZoomRate) {
                PreviewPresenter.this.previewHandler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                        PreviewPresenter.this.previewView.updateZoomViewProgress(currentZoomRate);
                    }
                });
            }
        });
        MyProgressDialog.showProgressDialog(this.activity, null);
    }

    public void showZoomView() {
        if (this.curMode != 2 && this.curMode != 6 && this.curMode != 5) {
            if (!this.cameraProperties.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) || 1 == this.cameraProperties.getCurrentDateStamp()) {
                this.previewView.showZoomView();
            }
        }
    }

    public int getMaxZoomRate() {
        return this.previewView.getZoomViewMaxZoomRate();
    }

    public int getZoomViewProgress() {
        AppLog.d(TAG, "getZoomViewProgress value=" + this.previewView.getZoomViewProgress());
        return this.previewView.getZoomViewProgress();
    }

    public void showSettingDialog(int position) {
        if (this.settingMenuList != null && this.settingMenuList.size() > 0) {
            OptionSetting.getInstance().addSettingCompleteListener(new OnSettingCompleteListener() {
                public void onOptionSettingComplete() {
                    PreviewPresenter.this.settingMenuList = UIDisplaySource.getinstance().getList(PreviewPresenter.this.currentSettingMenuMode, PreviewPresenter.this.currentCamera);
                    PreviewPresenter.this.settingListAdapter.notifyDataSetChanged();
                }

                public void settingVideoSizeComplete() {
                    AppLog.d(PreviewPresenter.TAG, "settingVideoSizeComplete curMode=" + PreviewPresenter.this.curMode);
                    if (PreviewPresenter.this.curMode == 7) {
                        PreviewPresenter.this.previewStream.changePreviewMode(PreviewPresenter.this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
                    } else if (PreviewPresenter.this.curMode == 3) {
                        PreviewPresenter.this.previewStream.changePreviewMode(PreviewPresenter.this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
                    }
                }

                public void settingTimeLapseModeComplete(int timeLapseMode) {
                    if (timeLapseMode == 0) {
                        if (PreviewPresenter.this.previewStream.changePreviewMode(PreviewPresenter.this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE)) {
                            PreviewPresenter.this.curMode = 8;
                        }
                    } else if (timeLapseMode == 1 && PreviewPresenter.this.previewStream.changePreviewMode(PreviewPresenter.this.cameraPreviewStreamClint, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE)) {
                        PreviewPresenter.this.curMode = 7;
                    }
                }
            });
            OptionSetting.getInstance().showSettingDialog(((SettingMenu) this.settingMenuList.get(position)).name, this.activity);
        }
    }

    public void showPvModePopupWindow() {
        AppLog.d(TAG, "showPvModePopupWindow curMode=" + this.curMode);
        if (this.curMode != 2 && this.curMode != 5 && this.curMode != 6 && this.curMode != 4) {
            this.previewView.showPopupWindow(this.curMode);
            if (this.cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_VIDEO)) {
                this.previewView.setVideoRadioBtnVisibility(0);
            }
            if (this.cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)) {
                this.previewView.setTimepLapseRadioBtnVisibility(0);
            }
            if (this.curMode == 1) {
                this.previewView.setCaptureRadioBtnChecked(true);
            } else if (this.curMode == 3) {
                this.previewView.setVideoRadioBtnChecked(true);
            } else if (this.curMode == 8 || this.curMode == 7) {
                this.previewView.setTimepLapseRadioChecked(true);
            }
        } else if (this.curMode == 2 || this.curMode == 6) {
            MyToast.show(this.activity, (int) R.string.stream_error_capturing);
        } else if (this.curMode == 4 || this.curMode == 5) {
            MyToast.show(this.activity, (int) R.string.stream_error_recording);
        }
    }

    public void refresh() {
        this.previewHandler.post(new Runnable() {
            public void run() {
                PreviewPresenter.this.initData();
                PreviewPresenter.this.initPreview();
                PreviewPresenter.this.addEvent();
            }
        });
    }

    public void stopStream() {
        AppLog.i(TAG, "start stopStream");
        stopMediaStream();
        delEvent();
        this.previewView.stopMPreview(this.currentCamera);
        destroyCamera();
    }

    public void endWifiListener() {
        GlobalInfo.getInstance().endWifiListener();
    }

    public void stopConnectCheck() {
        GlobalInfo.getInstance().stopConnectCheck();
    }

    public void addEvent() {
        this.sdkEvent.addEventListener(17);
        this.sdkEvent.addEventListener(36);
        this.sdkEvent.addEventListener(34);
        this.sdkEvent.addEventListener(33);
        this.sdkEvent.addEventListener(82);
        this.sdkEvent.addEventListener(35);
        this.sdkEvent.addEventListener(1);
        this.sdkEvent.addEventListener(81);
        this.sdkEvent.addCustomizeEvent(ICatchCameraProperty.ICH_CAP_BATTERY_LEVEL);
        this.sdkEvent.addEventListener(99);
        this.sdkEvent.addCustomizeEvent(14081);
    }

    public void delEvent() {
        this.sdkEvent.delEventListener(17);
        this.sdkEvent.delEventListener(36);
        this.sdkEvent.delEventListener(35);
        this.sdkEvent.delEventListener(82);
        this.sdkEvent.delEventListener(34);
        this.sdkEvent.delEventListener(1);
        this.sdkEvent.delEventListener(33);
        this.sdkEvent.delEventListener(81);
        this.sdkEvent.delCustomizeEventListener(ICatchCameraProperty.ICH_CAP_BATTERY_LEVEL);
        this.sdkEvent.delEventListener(99);
        this.sdkEvent.delCustomizeEventListener(14081);
    }

    public void stopPreview() {
        this.previewView.stopMPreview(this.currentCamera);
    }

    public void loadSettingMenuList() {
        AppLog.i(TAG, "setupBtn is clicked:allowClickButtoms=" + this.allowClickButtoms);
        if (this.allowClickButtoms) {
            this.allowClickButtoms = false;
            if (this.curMode == 4) {
                AppToast.show(this.activity, (int) R.string.stream_error_recording, 0);
            } else if (this.curMode == 2) {
                AppToast.show(this.activity, (int) R.string.stream_error_capturing, 0);
            } else if (this.curMode == 1) {
                this.previewView.setSetupMainMenuVisibility(0);
                this.currentSettingMenuMode = 1;
                if (this.settingMenuList != null) {
                    this.settingMenuList.clear();
                }
                if (this.settingListAdapter != null) {
                    this.settingListAdapter.notifyDataSetChanged();
                }
                this.previewView.setSettingBtnVisible(false);
                this.previewView.setBackBtnVisibility(true);
                this.previewView.setActionBarTitle(R.string.title_setting);
                MyProgressDialog.showProgressDialog(this.activity, (int) R.string.action_processing);
                new Thread(new Runnable() {
                    public void run() {
                        PreviewPresenter.this.previewHandler.post(new Runnable() {
                            public void run() {
                                PreviewPresenter.this.settingMenuList = UIDisplaySource.getinstance().getList(1, PreviewPresenter.this.currentCamera);
                                PreviewPresenter.this.settingListAdapter = new SettingListAdapter(PreviewPresenter.this.activity, PreviewPresenter.this.settingMenuList, PreviewPresenter.this.previewHandler, new OnItemClickListener() {
                                    public void onItemClick(int position) {
                                        PreviewPresenter.this.showSettingDialog(position);
                                    }
                                });
                                PreviewPresenter.this.previewView.setSettingMenuListAdapter(PreviewPresenter.this.settingListAdapter);
                                PreviewPresenter.this.stopPreview();
                                MyProgressDialog.closeProgressDialog();
                            }
                        });
                    }
                }).start();
            } else if (this.curMode == 3) {
                this.previewView.setSetupMainMenuVisibility(0);
                this.currentSettingMenuMode = 2;
                MyProgressDialog.showProgressDialog(this.activity, (int) R.string.action_processing);
                if (this.settingMenuList != null) {
                    this.settingMenuList.clear();
                }
                if (this.settingListAdapter != null) {
                    this.settingListAdapter.notifyDataSetChanged();
                }
                this.previewView.setSettingBtnVisible(false);
                this.previewView.setBackBtnVisibility(true);
                this.previewView.setActionBarTitle(R.string.title_setting);
                new Thread(new Runnable() {
                    public void run() {
                        PreviewPresenter.this.previewHandler.post(new Runnable() {
                            public void run() {
                                PreviewPresenter.this.settingMenuList = UIDisplaySource.getinstance().getList(2, PreviewPresenter.this.currentCamera);
                                PreviewPresenter.this.settingListAdapter = new SettingListAdapter(PreviewPresenter.this.activity, PreviewPresenter.this.settingMenuList, PreviewPresenter.this.previewHandler, new OnItemClickListener() {
                                    public void onItemClick(int position) {
                                        PreviewPresenter.this.showSettingDialog(position);
                                    }
                                });
                                PreviewPresenter.this.previewView.setSettingMenuListAdapter(PreviewPresenter.this.settingListAdapter);
                                PreviewPresenter.this.stopPreview();
                                PreviewPresenter.this.stopMediaStream();
                                MyProgressDialog.closeProgressDialog();
                            }
                        });
                    }
                }).start();
            } else if (this.curMode == 8 || this.curMode == 7) {
                this.previewView.setSetupMainMenuVisibility(0);
                this.currentSettingMenuMode = 3;
                MyProgressDialog.showProgressDialog(this.activity, (int) R.string.action_processing);
                if (this.settingMenuList != null) {
                    this.settingMenuList.clear();
                }
                if (this.settingListAdapter != null) {
                    this.settingListAdapter.notifyDataSetChanged();
                }
                this.previewView.setSettingBtnVisible(false);
                this.previewView.setBackBtnVisibility(true);
                this.previewView.setActionBarTitle(R.string.title_setting);
                new Thread(new Runnable() {
                    public void run() {
                        PreviewPresenter.this.previewHandler.post(new Runnable() {
                            public void run() {
                                PreviewPresenter.this.settingMenuList = UIDisplaySource.getinstance().getList(3, PreviewPresenter.this.currentCamera);
                                PreviewPresenter.this.settingListAdapter = new SettingListAdapter(PreviewPresenter.this.activity, PreviewPresenter.this.settingMenuList, PreviewPresenter.this.previewHandler, new OnItemClickListener() {
                                    public void onItemClick(int position) {
                                        PreviewPresenter.this.showSettingDialog(position);
                                    }
                                });
                                PreviewPresenter.this.previewView.setSettingMenuListAdapter(PreviewPresenter.this.settingListAdapter);
                                PreviewPresenter.this.stopPreview();
                                PreviewPresenter.this.stopMediaStream();
                                MyProgressDialog.closeProgressDialog();
                            }
                        });
                    }
                }).start();
            } else if (this.curMode == 5) {
                AppToast.show(this.activity, (int) R.string.stream_error_recording, 0);
            } else if (this.curMode == 6) {
                AppToast.show(this.activity, (int) R.string.stream_error_capturing, 0);
            }
            this.allowClickButtoms = true;
        }
    }

    public void finishActivity() {
        Tristate ret = Tristate.NORMAL;
        if (this.previewView.getSetupMainMenuVisibility() == 0) {
            AppLog.i(TAG, "onKeyDown curMode==" + this.curMode);
            this.previewView.setSetupMainMenuVisibility(8);
            this.previewView.setSettingBtnVisible(true);
            this.previewView.setBackBtnVisibility(false);
            this.previewView.setActionBarTitle(R.string.title_preview);
            if (this.curMode == 3) {
                AppLog.i(TAG, "onKeyDown curMode == APP_STATE_VIDEO_PREVIEW");
                changeCameraMode(this.curMode, ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
                return;
            } else if (this.curMode == 7) {
                AppLog.i(TAG, "onKeyDown curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO");
                this.currentCamera.timeLapsePreviewMode = 1;
                changeCameraMode(this.curMode, ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE);
                return;
            } else if (this.curMode == 8) {
                AppLog.i(TAG, "onKeyDown curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL");
                this.currentCamera.timeLapsePreviewMode = 0;
                changeCameraMode(this.curMode, ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE);
                return;
            } else {
                if (this.supportStreaming.booleanValue()) {
                    this.previewView.startMPreview(this.currentCamera);
                }
                createUIByMode(this.curMode);
                return;
            }
        }
        super.finishActivity();
    }

    public void redirectToAnotherActivity(Context context, Class<?> cls) {
        AppLog.i(TAG, "pbBtn is clicked curMode=" + this.curMode);
        if (this.allowClickButtoms) {
            this.allowClickButtoms = false;
            if (this.cameraProperties.isSDCardExist()) {
                AppLog.i(TAG, "curMode =" + this.curMode);
                if (this.curMode == 1 || this.curMode == 3 || this.curMode == 7 || this.curMode == 8) {
                    stopPreview();
                    if (!this.supportStreaming.booleanValue() || stopMediaStream()) {
                        delEvent();
                        this.allowClickButtoms = true;
                        Intent intent = new Intent();
                        AppLog.i(TAG, "intent:start PbMainActivity.class");
                        intent.setClass(context, cls);
                        context.startActivity(intent);
                        AppLog.i(TAG, "intent:end start PbMainActivity.class");
                        return;
                    }
                    AppLog.i("[Error] -- Main: ", "failed to stopMediaStream");
                    this.allowClickButtoms = true;
                    return;
                }
                if (this.curMode == 4 || this.curMode == 5) {
                    MyToast.show(this.activity, (int) R.string.stream_error_recording);
                } else if (this.curMode == 2 || this.curMode == 6) {
                    MyToast.show(this.activity, (int) R.string.stream_error_capturing);
                }
                this.allowClickButtoms = true;
                AppLog.i(TAG, "end processing for responsing pbBtn clicking");
                return;
            }
            AppDialog.showDialogWarn(this.activity, (int) R.string.dialog_card_lose);
            this.allowClickButtoms = true;
            return;
        }
        AppLog.i(TAG, "do not allow to response button clicking");
    }

    public void showVideoSizeOptionDialog(Context context, final OnSettingCompleteListener settingCompleteListener) {
        MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(R.string.stream_set_res_vid);
        String[] videoSizeUIString = currCamera.getVideoSize().getValueArrayString();
        final List<String> videoSizeValueString = currCamera.getVideoSize().getValueList();
        if (videoSizeUIString == null) {
            AppLog.e(TAG, "videoSizeUIString == null");
            return;
        }
        int length = videoSizeUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (videoSizeUIString[i].equals(currCamera.getVideoSize().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String value = (String) videoSizeValueString.get(arg1);
                AppLog.d(PreviewPresenter.TAG, " begin stopMPreview preview!!!!!");
                PreviewPresenter.this.stopPreview();
                if (PreviewPresenter.this.cameraProperties.getVideoSizeFlow() == 1) {
                    PreviewPresenter.this.stopMediaStream();
                }
                if (!PreviewPresenter.this.cameraProperties.setVideoSize(value)) {
                    ProgressDialog progressDialog = new ProgressDialog(PreviewPresenter.this.activity);
                    progressDialog.setProgressStyle(0);
                    progressDialog.setMessage(PreviewPresenter.this.activity.getResources().getString(R.string.text_operation_failed));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
                if (PreviewPresenter.this.cameraProperties.hasFuction(ICatchCameraProperty.ICH_CAP_SLOW_MOTION) && PreviewPresenter.this.cameraProperties.getCurrentSlowMotion() == SlowMotion.SLOW_MOTION_ON && PreviewPresenter.this.curMode == 3) {
                    PreviewPresenter.this.previewView.setSlowMotionVisibility(0);
                } else {
                    PreviewPresenter.this.previewView.setSlowMotionVisibility(8);
                }
                arg0.dismiss();
                settingCompleteListener.onOptionSettingComplete();
            }
        };
        Builder optionDialog = new Builder(GlobalInfo.getInstance().getCurrentApp());
        optionDialog.setTitle(title).setSingleChoiceItems(videoSizeUIString, curIdx, listener).create();
        optionDialog.setCancelable(true);
        optionDialog.show();
    }
}
