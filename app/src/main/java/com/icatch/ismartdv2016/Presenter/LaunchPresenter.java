package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.icatch.ismartdv2016.Adapter.CameraSlotAdapter;
import com.icatch.ismartdv2016.AppDialog.AppDialog;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.AppInfo.ConfigureInfo;
import com.icatch.ismartdv2016.AppInfo.UserMacPermition;
import com.icatch.ismartdv2016.Beans.CameraSlot;
import com.icatch.ismartdv2016.Beans.SearchedCameraInfo;
import com.icatch.ismartdv2016.Beans.SelectedCameraInfo;
import com.icatch.ismartdv2016.Dbl.CameraSlotSQLite;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.Mode.CameraNetworkMode;
import com.icatch.ismartdv2016.Model.Implement.SDKEvent;
import com.icatch.ismartdv2016.Model.Implement.SDKSession;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.SystemInfo.MWifiManager;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.ThumbnailGetting.ThumbnailOperation;
import com.icatch.ismartdv2016.Tools.BitmapTools;
import com.icatch.ismartdv2016.Tools.CrashHandler;
import com.icatch.ismartdv2016.Tools.FileOpertion.MFileTools;
import com.icatch.ismartdv2016.Tools.LruCacheTool;
import com.icatch.ismartdv2016.View.Activity.PreviewActivity;
import com.icatch.ismartdv2016.View.Fragment.AddNewCamFragment;
import com.icatch.ismartdv2016.View.Interface.LaunchView;
import com.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import uk.co.senab.photoview.BuildConfig;

public class LaunchPresenter extends BasePresenter {
    private static final String TAG = "LaunchPresenter";
    private Activity activity;
    private ArrayList<CameraSlot> camSlotList;
    private CameraSlotAdapter cameraSlotAdapter;
    private int cameraSlotPosition;
    private MyCamera currentCamera;
    private final LaunchHandler launchHandler = new LaunchHandler();
    private LaunchView launchView;
    private SDKEvent sdkEvent;
    private LinkedList<SelectedCameraInfo> searchCameraInfoList;

    private class LaunchHandler extends Handler {
        private LaunchHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                    LaunchPresenter.this.removeCamera(msg.arg1);
                    return;
                case SlidingMenu.TOUCHMODE_NONE /*2*/:
                    AppLog.i(LaunchPresenter.TAG, "MESSAGE_CAMERA_SCAN_TIME_OUT:count =" + LaunchPresenter.this.searchCameraInfoList.size());
                    SDKSession.stopDeviceScan();
                    LaunchPresenter.this.sdkEvent.delGlobalEventListener(85, Boolean.valueOf(false));
                    MyProgressDialog.closeProgressDialog();
                    if (LaunchPresenter.this.searchCameraInfoList.isEmpty()) {
                        Toast.makeText(LaunchPresenter.this.activity, R.string.alert_no_camera_found, 1).show();
                        return;
                    } else {
                        LaunchPresenter.this.showSearchCameraListSingleDialog();
                        return;
                    }
                case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                    MyProgressDialog.closeProgressDialog();
                    AppDialog.showDialogWarn(LaunchPresenter.this.activity, (int) R.string.dialog_timeout);
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                    MyProgressDialog.closeProgressDialog();
                    LaunchPresenter.this.redirectToAnotherActivity(LaunchPresenter.this.activity, PreviewActivity.class);
                    return;
                case com.slidingmenu.lib.R.styleable.SlidingMenu_behindScrollScale /*5*/:
                    AppLog.i(LaunchPresenter.TAG, "MESSAGE_CAMERA_CONNECTING_START");
                    LaunchPresenter.this.launchView.fragmentPopStackOfAll();
                    LaunchPresenter.this.launchCamera(LaunchPresenter.this.cameraSlotPosition);
                    return;
                case com.slidingmenu.lib.R.styleable.SherlockTheme_actionModeBackground /*15*/:
                    SearchedCameraInfo temp = msg.obj;
                    LaunchPresenter.this.searchCameraInfoList.addLast(new SelectedCameraInfo(temp.cameraName, temp.cameraIp, temp.cameraMode, temp.uid));
                    return;
                default:
                    return;
            }
        }
    }

    public LaunchPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setView(LaunchView launchView) {
        this.launchView = launchView;
        initCfg();
    }

    public void initCfg() {
        GlobalInfo.getInstance().setCurrentApp(this.activity);
        this.activity.getWindow().setFlags(128, 128);
        this.activity.getWindow().addFlags(AppMessage.PHOTO_PBACTIVITY);
        CrashHandler.getInstance().init(this.activity);
        ConfigureInfo.getInstance().initCfgInfo(this.activity.getApplicationContext());
        GlobalInfo.getInstance().startScreenListener();
        AppInfo.initAppdata(this.activity);
    }

    public void addGlobalLisnter(int eventId, boolean forAllSession) {
        if (this.sdkEvent == null) {
            this.sdkEvent = new SDKEvent(this.launchHandler);
        }
        this.sdkEvent.addGlobalEventListener(eventId, Boolean.valueOf(forAllSession));
    }

    public void launchCamera(final int position) {
        String wifiSsid = MWifiManager.getSsid(this.activity);
        if (((CameraSlot) this.camSlotList.get(position)).isWifiReady) {
            MyProgressDialog.showProgressDialog(this.activity, this.activity.getResources().getString(R.string.action_processing));
            new Thread(new Runnable() {
                public void run() {
                    LaunchPresenter.this.beginConnectCamera(position, "192.168.1.1");
                }
            }).start();
        } else if (((CameraSlot) this.camSlotList.get(position)).isOccupied) {
            AppDialog.showDialogWarn(this.activity, "Please connect camera wifi " + ((CameraSlot) this.camSlotList.get(position)).cameraName);
        } else if (isRegistered(MWifiManager.getSsid(this.activity))) {
            AppDialog.showDialogWarn(this.activity, "Camera " + wifiSsid + " has been registered");
        } else {
            MyProgressDialog.showProgressDialog(this.activity, this.activity.getResources().getString(R.string.action_processing));
            new Thread(new Runnable() {
                public void run() {
                    LaunchPresenter.this.beginConnectCamera(position, "192.168.1.1");
                }
            }).start();
        }
    }

    public void launchCamera(final int position, FragmentManager fm) {
        this.cameraSlotPosition = position;
        String wifiSsid = MWifiManager.getSsid(this.activity);
        if (((CameraSlot) this.camSlotList.get(position)).isWifiReady) {
            MyProgressDialog.showProgressDialog(this.activity, this.activity.getResources().getString(R.string.action_processing));
            new Thread(new Runnable() {
                public void run() {
                    LaunchPresenter.this.beginConnectCamera(position, "192.168.1.1");
                }
            }).start();
        } else if (((CameraSlot) this.camSlotList.get(position)).isOccupied) {
            AppDialog.showDialogWarn(this.activity, "Please connect camera wifi " + ((CameraSlot) this.camSlotList.get(position)).cameraName);
        } else if (isRegistered(MWifiManager.getSsid(this.activity))) {
            AppDialog.showDialogWarn(this.activity, "Camera " + wifiSsid + " has been registered");
        } else {
            this.launchView.setLaunchLayoutVisibility(8);
            this.launchView.setLaunchSettingFrameVisibility(0);
            this.launchView.setNavigationTitle(BuildConfig.FLAVOR);
            this.launchView.setBackBtnVisibility(true);
            GlobalInfo.getInstance().setAppStartHandler(this.launchHandler);
            AddNewCamFragment addNewCamFragment = new AddNewCamFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.launch_setting_frame, addNewCamFragment, "other");
            ft.addToBackStack("tag");
            ft.commit();
        }
    }

    public void removeCamera(int position) {
        AppLog.i(TAG, "remove camera position = " + position);
        CameraSlotSQLite.getInstance().deleteByPosition(position);
        loadListview();
    }

    public void loadListview() {
        this.camSlotList = CameraSlotSQLite.getInstance().getAllCameraSlotFormDb();
        if (this.cameraSlotAdapter != null) {
            this.cameraSlotAdapter.notifyDataSetInvalidated();
        }
        Context appContext = GlobalInfo.getInstance().getAppContext();
        List list = this.camSlotList;
        Handler handler = this.launchHandler;
        SystemInfo.getInstance();
        this.cameraSlotAdapter = new CameraSlotAdapter(appContext, list, handler, SystemInfo.getMetrics().heightPixels);
        this.launchView.setListviewAdapter(this.cameraSlotAdapter);
    }

    public void UserMacPermitionCheck() {
        if (!UserMacPermition.getInstance().isAllowedMac(SystemInfo.getInstance().getLocalMacAddress(this.activity))) {
            AppDialog.showDialogQuit(this.activity, (int) R.string.check_mac_fail);
        }
    }

    public void loadLocalThumbnails() {
        String path = MFileTools.getNewestPhotoFromDirectory(Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH);
        Bitmap thumbnail = LruCacheTool.getInstance().getBitmapFromLruCache(path);
        if (thumbnail == null) {
            thumbnail = BitmapTools.getImageByPath(path, 300, 300);
        }
        if (thumbnail == null) {
            this.launchView.loadDefaultLocalPhotoThumbnail();
            this.launchView.setNoPhotoFilesFoundVisibility(0);
            this.launchView.setPhotoClickable(false);
        } else {
            this.launchView.setLocalPhotoThumbnail(thumbnail);
            this.launchView.setNoPhotoFilesFoundVisibility(8);
            this.launchView.setPhotoClickable(true);
            LruCacheTool.getInstance().addBitmapToLruCache(path, thumbnail);
        }
        path = MFileTools.getNewestVideoFromDirectory(Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH);
        thumbnail = LruCacheTool.getInstance().getBitmapFromLruCache(path);
        if (thumbnail == null) {
            thumbnail = ThumbnailOperation.getVideoThumbnail(path);
        }
        if (thumbnail == null) {
            this.launchView.loadDefaultLocalVideooThumbnail();
            this.launchView.setNoVideoFilesFoundVisibility(0);
            this.launchView.setVideoClickable(false);
            return;
        }
        this.launchView.setLocalVideoThumbnail(thumbnail);
        this.launchView.setNoVideoFilesFoundVisibility(8);
        this.launchView.setVideoClickable(true);
        LruCacheTool.getInstance().addBitmapToLruCache(path, thumbnail);
    }

    public void startSearchCamera() {
        if (this.searchCameraInfoList != null) {
            this.searchCameraInfoList.clear();
        }
        this.searchCameraInfoList = new LinkedList();
        addGlobalLisnter(85, false);
        SDKSession.stopDeviceScan();
        SDKSession.startDeviceScan();
        startSearchTimeoutTimer();
        MyProgressDialog.showProgressDialog(this.activity, "Waiting...");
    }

    private void startSearchTimeoutTimer() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                LaunchPresenter.this.launchHandler.obtainMessage(2).sendToTarget();
            }
        }, 5000);
    }

    private void showSearchCameraListSingleDialog() {
        if (!this.searchCameraInfoList.isEmpty()) {
            CharSequence title = "Please selectOrCancelAll camera";
            CharSequence[] tempsearchCameraInfoList = new CharSequence[this.searchCameraInfoList.size()];
            for (int ii = 0; ii < tempsearchCameraInfoList.length; ii++) {
                tempsearchCameraInfoList[ii] = ((SelectedCameraInfo) this.searchCameraInfoList.get(ii)).cameraName + "\n" + ((SelectedCameraInfo) this.searchCameraInfoList.get(ii)).cameraIp + "          " + CameraNetworkMode.getModeConvert(((SelectedCameraInfo) this.searchCameraInfoList.get(ii)).cameraMode);
            }
            showOptionDialogSingle(title, tempsearchCameraInfoList, 0, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, true);
        }
    }

    private void showOptionDialogSingle(CharSequence title, CharSequence[] items, int checkedItem, OnClickListener listener, boolean cancelable) {
        AlertDialog optionDialog = new Builder(this.activity).setTitle(title).setSingleChoiceItems(items, checkedItem, listener).create();
        optionDialog.setCancelable(cancelable);
        optionDialog.show();
    }

    private void beginConnectCamera(int position, String ip) {
        AppLog.i(TAG, "isWifiConnect() == true");
        this.currentCamera = new MyCamera();
        if (!this.currentCamera.getSDKsession().prepareSession(ip)) {
            this.launchHandler.obtainMessage(3).sendToTarget();
        } else if (this.currentCamera.getSDKsession().checkWifiConnection()) {
            GlobalInfo.getInstance().setCurrentCamera(this.currentCamera);
            this.currentCamera.initCamera();
            if (CameraProperties.getInstance().hasFuction(PropertyId.CAMERA_DATE)) {
                CameraProperties.getInstance().setCameraDate();
            }
            this.currentCamera.setMyMode(1);
            CameraSlotSQLite.getInstance().curSlotPosition = position;
            CameraSlotSQLite.getInstance().curWifiSsid = MWifiManager.getSsid(this.activity);
            GlobalInfo.getInstance().setSsid(MWifiManager.getSsid(this.activity));
            CameraSlotSQLite.getInstance().update(new CameraSlot(position, true, MWifiManager.getSsid(this.activity), null, true));
            this.launchHandler.obtainMessage(4).sendToTarget();
        } else {
            AppLog.i(TAG, "..........checkWifiConnection  fail");
            this.launchHandler.obtainMessage(3).sendToTarget();
        }
    }

    private boolean isRegistered(String ssid) {
        Iterator it = this.camSlotList.iterator();
        while (it.hasNext()) {
            CameraSlot camSlot = (CameraSlot) it.next();
            if (camSlot.cameraName != null && camSlot.cameraName.equals(ssid)) {
                return true;
            }
        }
        return false;
    }
}
