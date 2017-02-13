package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.Mode.VideoPbMode;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import com.icatch.ismartdv2016.Tools.ConvertTools;
import com.icatch.ismartdv2016.View.Interface.LocalVideoPbView;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.type.ICatchEvent;
import java.io.File;
import java.util.List;

public class LocalVideoPbPresenter extends BasePresenter {
    private String TAG = "LocalVideoPbPresenter";
    private Activity activity;
    private boolean cacheFlag = false;
    private CacheProgressListener cacheProgressListener;
    private CacheStateChangedListener cacheStateChangedListener;
    private CameraAction cameraAction;
    private String curLocalVideoPath;
    private int curVideoIdx = 0;
    private double currentTime = -1.0d;
    private VideoPbHandler handler = new VideoPbHandler();
    private int lastSeekBarPosition;
    public List<LocalPbItemInfo> localVideoList = GlobalInfo.getInstance().localVideoList;
    private LocalVideoPbView localVideoPbView;
    private MyCamera myCamera;
    private boolean needUpdateSeekBar = true;
    private int videoDuration = 0;
    private VideoIsEndListener videoIsEndListener;
    private VideoPbMode videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
    private VideoPlayback videoPlayback;
    private Boolean waitForCaching = Boolean.valueOf(false);

    public class CacheProgressListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            LocalVideoPbPresenter.this.handler.obtainMessage(AppMessage.EVENT_CACHE_PROGRESS_NOTIFY, arg0.getIntValue1(), new Double(arg0.getDoubleValue1() * 100.0d).intValue()).sendToTarget();
        }
    }

    public class CacheStateChangedListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            LocalVideoPbPresenter.this.handler.obtainMessage(AppMessage.EVENT_CACHE_STATE_CHANGED, arg0.getIntValue1(), 0).sendToTarget();
        }
    }

    public class VideoIsEndListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            LocalVideoPbPresenter.this.handler.obtainMessage(AppMessage.EVENT_VIDEO_PLAY_COMPLETED, 0, 0).sendToTarget();
        }
    }

    private class VideoPbHandler extends Handler {
        private VideoPbHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppMessage.EVENT_CACHE_STATE_CHANGED /*1537*/:
                    AppLog.i(LocalVideoPbPresenter.this.TAG, "receive EVENT_CACHE_STATE_CHANGED");
                    AppLog.i(LocalVideoPbPresenter.this.TAG, "EVENT_CACHE_STATE_CHANGED ---------msg.arg1 = " + msg.arg1);
                    if (!LocalVideoPbPresenter.this.cacheFlag || LocalVideoPbPresenter.this.videoPbMode != VideoPbMode.MODE_VIDEO_PLAY) {
                        return;
                    }
                    if (msg.arg1 == 1) {
                        LocalVideoPbPresenter.this.localVideoPbView.showLoadingCircle(true);
                        LocalVideoPbPresenter.this.waitForCaching = Boolean.valueOf(true);
                        return;
                    } else if (msg.arg1 == 2) {
                        LocalVideoPbPresenter.this.localVideoPbView.showLoadingCircle(false);
                        LocalVideoPbPresenter.this.waitForCaching = Boolean.valueOf(false);
                        return;
                    } else {
                        return;
                    }
                case AppMessage.EVENT_CACHE_PROGRESS_NOTIFY /*1538*/:
                    AppLog.i(LocalVideoPbPresenter.this.TAG, "receive EVENT_CACHE_PROGRESS_NOTIFY msg.arg1=" + msg.arg1 + "msg.arg2=" + msg.arg2);
                    if (LocalVideoPbPresenter.this.cacheFlag && LocalVideoPbPresenter.this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY) {
                        if (LocalVideoPbPresenter.this.waitForCaching.booleanValue()) {
                            LocalVideoPbPresenter.this.localVideoPbView.setLoadPercent(msg.arg1);
                        }
                        LocalVideoPbPresenter.this.localVideoPbView.setSeekBarSecondProgress(msg.arg2);
                        return;
                    }
                    return;
                case AppMessage.EVENT_VIDEO_PLAY_COMPLETED /*1539*/:
                    AppLog.i(LocalVideoPbPresenter.this.TAG, "receive EVENT_VIDEO_PLAY_COMPLETED");
                    if (LocalVideoPbPresenter.this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY) {
                        LocalVideoPbPresenter.this.stopVideoPb();
                        LocalVideoPbPresenter.this.videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
                        return;
                    }
                    return;
                case AppMessage.MESSAGE_UPDATE_VIDEOPB_BAR /*1540*/:
                    if (LocalVideoPbPresenter.this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY && LocalVideoPbPresenter.this.needUpdateSeekBar) {
                        LocalVideoPbPresenter.this.localVideoPbView.setSeekBarProgress(msg.arg1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public LocalVideoPbPresenter(Activity activity, String videoPath) {
        super(activity);
        this.activity = activity;
        this.curVideoIdx = activity.getIntent().getExtras().getInt("curfilePosition");
        AppLog.d(this.TAG, "curVideoIdx=" + this.curVideoIdx);
        this.curLocalVideoPath = ((LocalPbItemInfo) this.localVideoList.get(this.curVideoIdx)).getFilePath();
        initClint();
        addEventListener();
    }

    public void updatePbSeekbar(double pts) {
        if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY && this.needUpdateSeekBar) {
            this.currentTime = pts;
            this.handler.obtainMessage(AppMessage.MESSAGE_UPDATE_VIDEOPB_BAR, new Double(this.currentTime * 100.0d).intValue(), 0).sendToTarget();
        }
    }

    public void setView(LocalVideoPbView localVideoPbView) {
        this.localVideoPbView = localVideoPbView;
        initCfg();
        initView();
        play();
    }

    private void initView() {
        this.localVideoPbView.setVideoNameTxv(this.curLocalVideoPath.substring(this.curLocalVideoPath.lastIndexOf("/") + 1));
    }

    public void initClint() {
        this.myCamera = GlobalInfo.getInstance().getCurrentCamera();
        this.videoPlayback = VideoPlayback.getInstance();
        this.cameraAction = CameraAction.getInstance();
    }

    public void addEventListener() {
        this.cacheStateChangedListener = new CacheStateChangedListener();
        this.cacheProgressListener = new CacheProgressListener();
        this.videoIsEndListener = new VideoIsEndListener();
        this.cameraAction.addEventListener(70, this.cacheStateChangedListener);
        this.cameraAction.addEventListener(69, this.cacheProgressListener);
        this.cameraAction.addEventListener(67, this.videoIsEndListener);
    }

    public void removeEventListener() {
        if (this.cacheStateChangedListener != null) {
            this.cameraAction.delEventListener(70, this.cacheStateChangedListener);
        }
        if (this.cacheProgressListener != null) {
            this.cameraAction.delEventListener(69, this.cacheProgressListener);
        }
        if (this.videoIsEndListener != null) {
            this.cameraAction.delEventListener(67, this.videoIsEndListener);
        }
    }

    public void play() {
        if (this.videoPbMode == VideoPbMode.MODE_VIDEO_IDLE) {
            AppLog.i(this.TAG, "start play video");
            if (this.videoPlayback.startPlaybackStream(this.curLocalVideoPath)) {
                this.needUpdateSeekBar = true;
                AppLog.i(this.TAG, "seekBar.getProgress() =" + this.localVideoPbView.getSeekBarProgress());
                int tempDuration = this.videoPlayback.getVideoDuration();
                AppLog.i(this.TAG, "end getLength = " + tempDuration);
                this.localVideoPbView.setPlayBtnSrc(R.drawable.ic_pause_white_36dp);
                this.localVideoPbView.setTimeLapsedValue("00:00");
                this.localVideoPbView.setTimeDurationValue(ConvertTools.secondsToMinuteOrHours(tempDuration / 100));
                this.localVideoPbView.setSeekBarMaxValue(tempDuration);
                this.videoDuration = tempDuration;
                startVideoPb();
                AppLog.i(this.TAG, "has start the GetVideoFrameThread() to get play video");
                this.videoPbMode = VideoPbMode.MODE_VIDEO_PLAY;
                return;
            }
            MyToast.show(this.activity, (int) R.string.dialog_failed);
            AppLog.e(this.TAG, "failed to startPlaybackStream");
        } else if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PAUSE) {
            AppLog.i(this.TAG, "mode == MODE_VIDEO_PAUSE");
            if (this.videoPlayback.resumePlayback()) {
                this.needUpdateSeekBar = true;
                this.localVideoPbView.setPlayBtnSrc(R.drawable.ic_pause_white_36dp);
                this.videoPbMode = VideoPbMode.MODE_VIDEO_PLAY;
                return;
            }
            MyToast.show(this.activity, (int) R.string.dialog_failed);
            AppLog.i(this.TAG, "failed to resumePlayback");
        } else if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY) {
            AppLog.i(this.TAG, "begin pause the playing");
            if (this.videoPlayback.pausePlayback()) {
                this.localVideoPbView.setPlayBtnSrc(R.drawable.ic_play_arrow_white_36dp);
                this.videoPbMode = VideoPbMode.MODE_VIDEO_PAUSE;
                this.localVideoPbView.showLoadingCircle(false);
                return;
            }
            MyToast.show(this.activity, (int) R.string.dialog_failed);
            AppLog.i(this.TAG, "failed to pausePlayback");
        }
    }

    public void seekBarOnStopTrackingTouch() {
        this.lastSeekBarPosition = this.localVideoPbView.getSeekBarProgress();
        this.localVideoPbView.setTimeLapsedValue(ConvertTools.secondsToMinuteOrHours(this.lastSeekBarPosition / 100));
        if (this.videoPlayback.videoSeek(((double) this.lastSeekBarPosition) / 100.0d)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            this.localVideoPbView.setSeekBarProgress(this.lastSeekBarPosition);
            MyToast.show(this.activity, (int) R.string.dialog_failed);
        }
        this.needUpdateSeekBar = true;
    }

    public void seekBarOnStartTrackingTouch() {
        this.needUpdateSeekBar = false;
        this.lastSeekBarPosition = this.localVideoPbView.getSeekBarProgress();
    }

    public void setTimeLapsedValue(int progress) {
        this.localVideoPbView.setTimeLapsedValue(ConvertTools.secondsToMinuteOrHours(progress / 100));
    }

    private void startVideoPb() {
        AppLog.i(this.TAG, "startVideoPb");
        this.cacheFlag = true;
        this.waitForCaching = Boolean.valueOf(true);
        this.localVideoPbView.showLoadingCircle(true);
        this.localVideoPbView.startMPreview(this.myCamera, 1);
    }

    public void stopVideoPb() {
        this.cacheFlag = false;
        this.localVideoPbView.showLoadingCircle(false);
        this.localVideoPbView.setTimeLapsedValue("00:00");
        this.videoPlayback.stopPlaybackStream();
        this.localVideoPbView.setPlayBtnSrc(R.drawable.ic_play_arrow_white_36dp);
        this.localVideoPbView.setSeekBarProgress(0);
        this.localVideoPbView.setSeekBarSecondProgress(0);
        this.localVideoPbView.setTopBarVisibility(0);
        this.localVideoPbView.setBottomBarVisibility(0);
        this.localVideoPbView.stopMPreview();
        this.videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
    }

    public void showBar(boolean isShowBar) {
        if (!isShowBar) {
            this.localVideoPbView.setBottomBarVisibility(0);
            this.localVideoPbView.setTopBarVisibility(0);
        } else if (this.videoPbMode != VideoPbMode.MODE_VIDEO_IDLE) {
            this.localVideoPbView.setBottomBarVisibility(8);
            this.localVideoPbView.setTopBarVisibility(8);
        }
    }

    public void share() {
        if (this.videoPbMode != VideoPbMode.MODE_VIDEO_IDLE) {
            AppLog.i(this.TAG, "begin stop the playing");
            stopVideoPb();
        }
        Uri fileUri = Uri.fromFile(new File(this.curLocalVideoPath));
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.putExtra("android.intent.extra.STREAM", fileUri);
        shareIntent.setType("video/*");
        this.activity.startActivity(Intent.createChooser(shareIntent, this.activity.getResources().getString(R.string.gallery_share_to)));
    }

    public void delete() {
        if (this.videoPbMode != VideoPbMode.MODE_VIDEO_IDLE) {
            AppLog.i(this.TAG, "begin stop the playing");
            stopVideoPb();
        }
        showDeleteEnsureDialog();
    }

    private void showDeleteEnsureDialog() {
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setTitle(R.string.image_delete_des);
        builder.setNegativeButton(R.string.gallery_delete, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                LocalVideoPbPresenter.this.stopVideoPb();
                LocalVideoPbPresenter.this.localVideoList.remove(LocalVideoPbPresenter.this.curVideoIdx);
                File file = new File(LocalVideoPbPresenter.this.curLocalVideoPath);
                if (file.exists()) {
                    file.delete();
                }
                LocalVideoPbPresenter.this.activity.finish();
            }
        });
        builder.setPositiveButton(R.string.gallery_cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
