package com.icatch.ismartdv2016.View.Activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.icatch.ismartdv2016.Adapter.SettingListAdapter;
import com.icatch.ismartdv2016.ExtendComponent.MPreview;
import com.icatch.ismartdv2016.ExtendComponent.ZoomView;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.PreviewMode;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.PreviewPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.View.Interface.PreviewView;

public class PreviewActivity extends AppCompatActivity implements OnClickListener, PreviewView {
    private static final String TAG = "PreviewActivity";
    private ActionBar actionBar;
    private ImageView autoDownloadImagview;
    private ImageView batteryStatus;
    private ImageView burstStatus;
    private ImageButton captureBtn;
    private RadioButton captureRadioBtn;
    private ImageView carMode;
    private View contentView;
    private RelativeLayout delayCaptureLayout;
    private TextView delayCaptureText;
    private RelativeLayout imageSizeLayout;
    private TextView imageSizeTxv;
    private MPreview mPreview;
    private ListView mainMenuList;
    private TextView noSupportPreviewTxv;
    private ImageButton pbBtn;
    private PreviewPresenter presenter;
    private ImageButton pvModeBtn;
    private PopupWindow pvModePopupWindow;
    private TextView recordingTime;
    private TextView remainCaptureCountText;
    private TextView remainRecordingTimeText;
    private MenuItem settingMenu;
    private RelativeLayout setupMainMenu;
    private ImageView slowMotion;
    private ImageView timelapseMode;
    private RadioButton timepLapseRadioBtn;
    private Toolbar toolbar;
    private RadioButton videoRadioBtn;
    private RelativeLayout videoSizeLayout;
    private TextView videoSizeTxv;
    private ImageView wbStatus;
    private ImageView wifiStatus;
    private ZoomView zoomView;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "1122 onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayShowHomeEnabled(true);
        this.actionBar.setDisplayShowTitleEnabled(true);
        this.actionBar.setTitle(R.string.title_preview);
        this.actionBar.setHomeButtonEnabled(true);
        this.actionBar.setDisplayHomeAsUpEnabled(false);
        this.presenter = new PreviewPresenter(this);
        this.presenter.setView(this);
        this.mPreview = (MPreview) findViewById(R.id.m_preview);
        this.mPreview.setOnClickListener(this);
        this.pbBtn = (ImageButton) findViewById(R.id.multi_pb);
        this.pbBtn.setOnClickListener(this);
        this.captureBtn = (ImageButton) findViewById(R.id.doCapture);
        this.captureBtn.setOnClickListener(this);
        this.wbStatus = (ImageView) findViewById(R.id.wb_status);
        this.burstStatus = (ImageView) findViewById(R.id.burst_status);
        this.wifiStatus = (ImageView) findViewById(R.id.wifi_status);
        this.batteryStatus = (ImageView) findViewById(R.id.battery_status);
        this.timelapseMode = (ImageView) findViewById(R.id.timelapse_mode);
        this.slowMotion = (ImageView) findViewById(R.id.slow_motion);
        this.carMode = (ImageView) findViewById(R.id.car_mode);
        this.recordingTime = (TextView) findViewById(R.id.recording_time);
        this.autoDownloadImagview = (ImageView) findViewById(R.id.auto_download_imageview);
        this.delayCaptureText = (TextView) findViewById(R.id.delay_capture_text);
        this.delayCaptureLayout = (RelativeLayout) findViewById(R.id.delay_capture_layout);
        this.imageSizeLayout = (RelativeLayout) findViewById(R.id.image_size_layout);
        this.imageSizeTxv = (TextView) findViewById(R.id.image_size_txv);
        this.remainCaptureCountText = (TextView) findViewById(R.id.remain_capture_count_text);
        this.videoSizeLayout = (RelativeLayout) findViewById(R.id.video_size_layout);
        this.videoSizeTxv = (TextView) findViewById(R.id.video_size_txv);
        this.remainRecordingTimeText = (TextView) findViewById(R.id.remain_recording_time_text);
        this.setupMainMenu = (RelativeLayout) findViewById(R.id.setupMainMenu);
        this.mainMenuList = (ListView) findViewById(R.id.setup_menu_listView);
        this.noSupportPreviewTxv = (TextView) findViewById(R.id.not_support_preview_txv);
        this.pvModeBtn = (ImageButton) findViewById(R.id.pv_mode);
        this.contentView = LayoutInflater.from(this).inflate(R.layout.camer_mode_switch_layout, null);
        this.pvModePopupWindow = new PopupWindow(this.contentView, -2, -2, true);
        this.captureRadioBtn = (RadioButton) this.contentView.findViewById(R.id.capture_radio);
        this.videoRadioBtn = (RadioButton) this.contentView.findViewById(R.id.video_radio);
        this.timepLapseRadioBtn = (RadioButton) this.contentView.findViewById(R.id.timeLapse_radio);
        this.contentView.setFocusable(true);
        this.contentView.setFocusableInTouchMode(true);
        this.contentView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                AppLog.d(PreviewActivity.TAG, "contentView onKey");
                switch (keyCode) {
                    case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                        AppLog.d("AppStart", "contentView back");
                        if (PreviewActivity.this.pvModePopupWindow != null && PreviewActivity.this.pvModePopupWindow.isShowing()) {
                            AppLog.d("AppStart", "dismiss pvModePopupWindow");
                            PreviewActivity.this.pvModePopupWindow.dismiss();
                            break;
                        }
                }
                return true;
            }
        });
        this.zoomView = (ZoomView) findViewById(R.id.zoom_view);
        this.zoomView.setZoomInOnclickListener(new OnClickListener() {
            public void onClick(View v) {
                PreviewActivity.this.presenter.zoomIn();
            }
        });
        this.zoomView.setZoomOutOnclickListener(new OnClickListener() {
            public void onClick(View v) {
                PreviewActivity.this.presenter.zoomOut();
            }
        });
        this.zoomView.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                PreviewActivity.this.presenter.zoomBySeekBar();
            }
        });
        this.pvModeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PreviewActivity.this.presenter.showPvModePopupWindow();
            }
        });
        this.captureRadioBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PreviewActivity.this.presenter.changePreviewMode(PreviewMode.APP_STATE_STILL_MODE);
            }
        });
        this.videoRadioBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PreviewActivity.this.presenter.changePreviewMode(PreviewMode.APP_STATE_VIDEO_MODE);
            }
        });
        this.timepLapseRadioBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PreviewActivity.this.presenter.changePreviewMode(PreviewMode.APP_STATE_TIMELAPSE_MODE);
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        Log.d(TAG, "1122 onResume");
        super.onResume();
        this.presenter.initData();
        this.presenter.submitAppInfo();
        this.presenter.initPreview();
        this.presenter.initStatus();
        this.presenter.addEvent();
    }

    protected void onStop() {
        AppLog.d(TAG, "1122 onStop");
        super.onStop();
        this.presenter.isAppBackground();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                Log.d("AppStart", "home");
                break;
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                AppLog.d("AppStart", "back");
                if (this.pvModePopupWindow != null && this.pvModePopupWindow.isShowing()) {
                    AppLog.d("AppStart", "dismiss pvModePopupWindow");
                    this.pvModePopupWindow.dismiss();
                    break;
                }
                this.presenter.finishActivity();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    protected void onDestroy() {
        Log.d(TAG, "1122 onDestroy");
        super.onDestroy();
        this.presenter.removeActivity();
        this.presenter.stopPreview();
        this.presenter.delEvent();
        this.presenter.stopMediaStream();
        this.presenter.destroyCamera();
        this.presenter.unregisterWifiSSReceiver();
        this.presenter.endWifiListener();
        this.presenter.stopConnectCheck();
    }

    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPrepareOptionsPanel(view, menu);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            AppLog.e("tigertiger", "id == android.R.id.home");
            if (this.pvModePopupWindow == null || !this.pvModePopupWindow.isShowing()) {
                this.presenter.finishActivity();
            } else {
                this.pvModePopupWindow.dismiss();
            }
        } else if (id == R.id.action_setting) {
            this.settingMenu = item;
            this.presenter.loadSettingMenuList();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        AppLog.i(TAG, "click the v.getId() =" + v.getId());
        switch (v.getId()) {
            case R.id.m_preview /*2131689653*/:
                AppLog.i(TAG, "click the m_preview");
                this.presenter.showZoomView();
                return;
            case R.id.multi_pb /*2131689680*/:
                AppLog.i(TAG, "click the multi_pb");
                this.presenter.redirectToAnotherActivity(this, MultiPbActivity.class);
                return;
            case R.id.doCapture /*2131689681*/:
                AppLog.i(TAG, "click the doCapture");
                this.presenter.startOrStopCapture();
                return;
            case R.id.stillToggle /*2131689683*/:
                AppLog.i(TAG, "click the stillToggle");
                this.presenter.changePreviewMode(PreviewMode.APP_STATE_STILL_MODE);
                return;
            case R.id.videoToggle /*2131689684*/:
                AppLog.i(TAG, "click the videoToggle");
                this.presenter.changePreviewMode(PreviewMode.APP_STATE_VIDEO_MODE);
                return;
            case R.id.timeLapseToggle /*2131689685*/:
                AppLog.i(TAG, "click the timeLapseToggle");
                this.presenter.changePreviewMode(PreviewMode.APP_STATE_TIMELAPSE_MODE);
                return;
            default:
                return;
        }
    }

    public void setmPreviewVisibility(int visibility) {
        this.mPreview.setVisibility(visibility);
    }

    public void setWbStatusVisibility(int visibility) {
        this.wbStatus.setVisibility(visibility);
    }

    public void setBurstStatusVisibility(int visibility) {
        this.burstStatus.setVisibility(visibility);
    }

    public void setWifiStatusVisibility(int visibility) {
        this.wifiStatus.setVisibility(visibility);
    }

    public void setWifiIcon(int drawableId) {
        this.wifiStatus.setBackgroundResource(drawableId);
    }

    public void setBatteryStatusVisibility(int visibility) {
        this.batteryStatus.setVisibility(visibility);
    }

    public void setBatteryIcon(int drawableId) {
        this.batteryStatus.setBackgroundResource(drawableId);
    }

    public void settimeLapseModeVisibility(int visibility) {
        this.timelapseMode.setVisibility(visibility);
    }

    public void settimeLapseModeIcon(int drawableId) {
        this.timelapseMode.setBackgroundResource(drawableId);
    }

    public void setSlowMotionVisibility(int visibility) {
        this.slowMotion.setVisibility(visibility);
    }

    public void setCarModeVisibility(int visibility) {
        this.carMode.setVisibility(visibility);
    }

    public void setRecordingTimeVisibility(int visibility) {
        this.recordingTime.setVisibility(visibility);
    }

    public void setAutoDownloadVisibility(int visibility) {
        this.autoDownloadImagview.setVisibility(visibility);
    }

    public void setCaptureBtnBackgroundResource(int id) {
        this.captureBtn.setBackgroundResource(id);
    }

    public void setRecordingTime(String laspeTime) {
        this.recordingTime.setText(laspeTime);
    }

    public void setDelayCaptureLayoutVisibility(int visibility) {
        this.delayCaptureLayout.setVisibility(visibility);
    }

    public void setDelayCaptureTextTime(String delayCaptureTime) {
        this.delayCaptureText.setText(delayCaptureTime);
    }

    public void setImageSizeLayoutVisibility(int visibility) {
        this.imageSizeLayout.setVisibility(visibility);
    }

    public void setRemainCaptureCount(String remainCaptureCount) {
        this.remainCaptureCountText.setText(remainCaptureCount);
    }

    public void setVideoSizeLayoutVisibility(int visibility) {
        this.videoSizeLayout.setVisibility(visibility);
    }

    public void setRemainRecordingTimeText(String remainRecordingTime) {
        this.remainRecordingTimeText.setText(remainRecordingTime);
    }

    public void setBurstStatusIcon(int drawableId) {
        this.burstStatus.setBackgroundResource(drawableId);
    }

    public void setWbStatusIcon(int drawableId) {
        this.wbStatus.setBackgroundResource(drawableId);
    }

    public void setUpsideVisibility(int visibility) {
        this.carMode.setVisibility(visibility);
    }

    public void startMPreview(MyCamera myCamera) {
        AppLog.d(TAG, "startMPreview");
        if (this.noSupportPreviewTxv.getVisibility() == 0) {
            this.noSupportPreviewTxv.setVisibility(8);
        }
        this.mPreview.setVisibility(0);
        this.mPreview.start(myCamera, 2);
    }

    public void stopMPreview(MyCamera myCamera) {
        this.mPreview.stop();
    }

    public void setCaptureBtnEnability(boolean enablity) {
        this.captureBtn.setEnabled(enablity);
    }

    public void setVideoSizeInfo(String sizeInfo) {
        AppLog.i(TAG, "sizeInfo = " + sizeInfo);
        this.videoSizeTxv.setText(sizeInfo);
    }

    public void setImageSizeInfo(String sizeInfo) {
        AppLog.i(TAG, "sizeInfo = " + sizeInfo);
        this.imageSizeTxv.setText(sizeInfo);
    }

    public void showZoomView() {
        this.zoomView.startDisplay();
    }

    public void setMaxZoomRate(int maxZoomRate) {
        this.zoomView.setMaxValue(maxZoomRate);
    }

    public int getZoomViewProgress() {
        return this.zoomView.getProgress();
    }

    public int getZoomViewMaxZoomRate() {
        return ZoomView.MAX_VALUE;
    }

    public void updateZoomViewProgress(int currentZoomRatio) {
        this.zoomView.updateZoomBarValue(currentZoomRatio);
    }

    public int getSetupMainMenuVisibility() {
        return this.setupMainMenu.getVisibility();
    }

    public void setSetupMainMenuVisibility(int visibility) {
        this.setupMainMenu.setVisibility(visibility);
    }

    public void setAutoDownloadBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.autoDownloadImagview.setImageBitmap(bitmap);
        }
    }

    public void setActionBarTitle(int resId) {
        this.actionBar.setTitle(resId);
    }

    public void setSettingBtnVisible(boolean isVisible) {
        this.settingMenu.setVisible(isVisible);
    }

    public void setBackBtnVisibility(boolean isVisible) {
        this.actionBar.setDisplayHomeAsUpEnabled(isVisible);
    }

    public void setSettingMenuListAdapter(SettingListAdapter settingListAdapter) {
        this.mainMenuList.setAdapter(settingListAdapter);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dismissPopupWindow();
        AppLog.d(TAG, "onConfigurationChanged newConfig Orientation=" + newConfig.orientation);
    }

    public void setSupportPreviewTxvVisibility(int visibility) {
        this.noSupportPreviewTxv.setVisibility(visibility);
    }

    public void setPvModeBtnBackgroundResource(int drawableId) {
        this.pvModeBtn.setBackgroundResource(drawableId);
    }

    public void setTimepLapseRadioBtnVisibility(int visibility) {
        this.timepLapseRadioBtn.setVisibility(visibility);
    }

    public void setCaptureRadioBtnVisibility(int visibility) {
        this.captureRadioBtn.setVisibility(visibility);
    }

    public void setVideoRadioBtnVisibility(int visibility) {
        this.videoRadioBtn.setVisibility(visibility);
    }

    public void setTimepLapseRadioChecked(boolean checked) {
        this.timepLapseRadioBtn.setChecked(checked);
    }

    public void setCaptureRadioBtnChecked(boolean checked) {
        this.captureRadioBtn.setChecked(checked);
    }

    public void setVideoRadioBtnChecked(boolean checked) {
        this.videoRadioBtn.setChecked(checked);
    }

    public void showPopupWindow(int curMode) {
        if (this.pvModePopupWindow != null) {
            AppLog.d(TAG, "showPopupWindow height = " + SystemInfo.getMetrics().heightPixels);
            AppLog.d(TAG, "showPopupWindow pvModeBtn.getWidth() = " + this.pvModeBtn.getWidth());
            AppLog.d(TAG, "showPopupWindow pvModeBtn.getHeight() = " + this.pvModeBtn.getHeight());
            AppLog.d(TAG, "showPopupWindow contentView.getHeight() = " + this.contentView.getHeight());
            int contentViewH = this.contentView.getHeight();
            if (contentViewH == 0) {
                contentViewH = this.pvModeBtn.getHeight() * 5;
            }
            this.pvModePopupWindow.showAsDropDown(this.pvModeBtn, 0, (-this.pvModeBtn.getHeight()) - contentViewH);
        }
    }

    public void dismissPopupWindow() {
        if (this.pvModePopupWindow != null && this.pvModePopupWindow.isShowing()) {
            this.pvModePopupWindow.dismiss();
        }
    }

    public void refresh() {
        this.presenter.refresh();
    }

    public void stopStream() {
        this.presenter.stopStream();
    }
}
