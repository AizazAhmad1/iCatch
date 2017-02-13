package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.icatch.ismartdv2016.AppDialog.SingleDownloadDialog;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.DownloadInfo;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.Mode.VideoPbMode;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.Tools.ConvertTools;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileOper;
import com.icatch.ismartdv2016.Tools.MediaRefresh;
import com.icatch.ismartdv2016.View.Interface.VideoPbView;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.type.ICatchEvent;
import com.icatch.wificam.customer.type.ICatchFile;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uk.co.senab.photoview.BuildConfig;

public class VideoPbPresenter extends BasePresenter {
    private String TAG = "VideoPbPresenter";
    private Activity activity;
    private boolean cacheFlag = false;
    private CacheProgressListener cacheProgressListener;
    private CacheStateChangedListener cacheStateChangedListener;
    private CameraAction cameraAction;
    private ICatchFile curVideoFile;
    private int curVideoPosition;
    private double currentTime = -1.0d;
    protected Timer downloadProgressTimer;
    private String downloadingFilename = BuildConfig.FLAVOR;
    private ExecutorService executor;
    private List<MultiPbItemInfo> fileList = GlobalInfo.getInstance().videoInfoList;
    private FileOperation fileOperation;
    private VideoPbHandler handler = new VideoPbHandler();
    private int lastSeekBarPosition;
    private MyCamera myCamera;
    private boolean needUpdateSeekBar = true;
    private SingleDownloadDialog singleDownloadDialog;
    private int videoDuration = 0;
    private VideoIsEndListener videoIsEndListener;
    private VideoPbMode videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
    private VideoPbView videoPbView;
    private VideoPlayback videoPlayback;
    private Boolean waitForCaching = Boolean.valueOf(false);

    public class CacheProgressListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            VideoPbPresenter.this.handler.obtainMessage(AppMessage.EVENT_CACHE_PROGRESS_NOTIFY, arg0.getIntValue1(), new Double(arg0.getDoubleValue1() * 100.0d).intValue()).sendToTarget();
        }
    }

    public class CacheStateChangedListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            VideoPbPresenter.this.handler.obtainMessage(AppMessage.EVENT_CACHE_STATE_CHANGED, arg0.getIntValue1(), 0).sendToTarget();
        }
    }

    private class DeleteThread implements Runnable {
        private DeleteThread() {
        }

        public void run() {
            Boolean retValue = Boolean.valueOf(false);
            if (Boolean.valueOf(VideoPbPresenter.this.fileOperation.deleteFile(VideoPbPresenter.this.curVideoFile)).booleanValue()) {
                VideoPbPresenter.this.handler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                        GlobalInfo.getInstance().videoInfoList.remove(VideoPbPresenter.this.curVideoPosition);
                        VideoPbPresenter.this.activity.finish();
                    }
                });
            } else {
                VideoPbPresenter.this.handler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                        MyToast.show(VideoPbPresenter.this.activity, (int) R.string.dialog_delete_failed_single);
                    }
                });
            }
            AppLog.d(VideoPbPresenter.this.TAG, "end DeleteThread");
        }
    }

    class DownloadProcessTask extends TimerTask {
        long curFileLength;
        int downloadProgress = 0;
        long fileSize;

        DownloadProcessTask() {
        }

        public void run() {
            File file = new File((Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH) + VideoPbPresenter.this.curVideoFile.getFileName());
            this.fileSize = VideoPbPresenter.this.curVideoFile.getFileSize();
            this.curFileLength = file.length();
            if (file == null) {
                this.downloadProgress = 0;
            } else if (this.curFileLength == this.fileSize) {
                this.downloadProgress = 100;
            } else {
                this.downloadProgress = (int) ((file.length() * 100) / this.fileSize);
            }
            final DownloadInfo downloadInfo = new DownloadInfo(VideoPbPresenter.this.curVideoFile, this.fileSize, this.curFileLength, this.downloadProgress, false);
            VideoPbPresenter.this.handler.post(new Runnable() {
                public void run() {
                    if (VideoPbPresenter.this.singleDownloadDialog != null) {
                        VideoPbPresenter.this.singleDownloadDialog.updateDownloadStatus(downloadInfo);
                    }
                    AppLog.d(VideoPbPresenter.this.TAG, "update Process downloadProgress=" + DownloadProcessTask.this.downloadProgress);
                }
            });
            AppLog.d(VideoPbPresenter.this.TAG, "end DownloadProcessTask");
        }
    }

    private class DownloadThread implements Runnable {
        private String TAG;
        long fileSize;
        String fileType;

        private DownloadThread() {
            this.TAG = "DownloadThread";
        }

        public void run() {
            AppLog.d(this.TAG, "begin DownloadThread");
            AppInfo.isDownloading = true;
            if (Environment.getExternalStorageState().equals("mounted")) {
                String path = Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH;
                String fileName = VideoPbPresenter.this.curVideoFile.getFileName();
                AppLog.d(this.TAG, "------------fileName =" + fileName);
                FileOper.createDirectory(path);
                VideoPbPresenter.this.downloadingFilename = path + fileName;
                this.fileSize = VideoPbPresenter.this.curVideoFile.getFileSize();
                if (new File(VideoPbPresenter.this.downloadingFilename).exists()) {
                    VideoPbPresenter.this.handler.post(new Runnable() {
                        public void run() {
                            if (VideoPbPresenter.this.singleDownloadDialog != null) {
                                VideoPbPresenter.this.singleDownloadDialog.dismissDownloadDialog();
                            }
                            if (VideoPbPresenter.this.downloadProgressTimer != null) {
                                VideoPbPresenter.this.downloadProgressTimer.cancel();
                            }
                            MyToast.show(VideoPbPresenter.this.activity, "File already exists:/DCIM/iSmartDV/");
                        }
                    });
                } else {
                    boolean temp = VideoPbPresenter.this.fileOperation.downloadFile(VideoPbPresenter.this.curVideoFile, VideoPbPresenter.this.downloadingFilename);
                    if (temp) {
                        if (fileName.endsWith(".mov") || fileName.endsWith(".MOV")) {
                            this.fileType = "video/mov";
                        } else {
                            this.fileType = "video/mp4";
                        }
                        MediaRefresh.addMediaToDB(VideoPbPresenter.this.activity, VideoPbPresenter.this.downloadingFilename, this.fileType);
                        AppLog.d(this.TAG, "end downloadFile temp =" + temp);
                        AppInfo.isDownloading = false;
                        VideoPbPresenter.this.handler.post(new Runnable() {
                            public void run() {
                                if (VideoPbPresenter.this.singleDownloadDialog != null) {
                                    VideoPbPresenter.this.singleDownloadDialog.dismissDownloadDialog();
                                }
                                if (VideoPbPresenter.this.downloadProgressTimer != null) {
                                    VideoPbPresenter.this.downloadProgressTimer.cancel();
                                }
                                MyToast.show(VideoPbPresenter.this.activity, "Downloaded to/DCIM/iSmartDV/");
                            }
                        });
                    } else {
                        VideoPbPresenter.this.handler.post(new Runnable() {
                            public void run() {
                                if (VideoPbPresenter.this.singleDownloadDialog != null) {
                                    VideoPbPresenter.this.singleDownloadDialog.dismissDownloadDialog();
                                }
                                if (VideoPbPresenter.this.downloadProgressTimer != null) {
                                    VideoPbPresenter.this.downloadProgressTimer.cancel();
                                }
                                MyToast.show(VideoPbPresenter.this.activity, "Download failed");
                            }
                        });
                        AppInfo.isDownloading = false;
                        return;
                    }
                }
                AppLog.d(this.TAG, "end DownloadThread");
                return;
            }
            VideoPbPresenter.this.handler.post(new Runnable() {
                public void run() {
                    if (VideoPbPresenter.this.singleDownloadDialog != null) {
                        VideoPbPresenter.this.singleDownloadDialog.dismissDownloadDialog();
                    }
                    if (VideoPbPresenter.this.downloadProgressTimer != null) {
                        VideoPbPresenter.this.downloadProgressTimer.cancel();
                    }
                    MyToast.show(VideoPbPresenter.this.activity, "Download failed");
                }
            });
        }
    }

    public class VideoIsEndListener implements ICatchWificamListener {
        public void eventNotify(ICatchEvent arg0) {
            VideoPbPresenter.this.handler.obtainMessage(AppMessage.EVENT_VIDEO_PLAY_COMPLETED, 0, 0).sendToTarget();
        }
    }

    private class VideoPbHandler extends Handler {
        private VideoPbHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppMessage.EVENT_CACHE_STATE_CHANGED /*1537*/:
                    AppLog.i(VideoPbPresenter.this.TAG, "receive EVENT_CACHE_STATE_CHANGED ---------msg.arg1 = " + msg.arg1);
                    if (!VideoPbPresenter.this.cacheFlag || VideoPbPresenter.this.videoPbMode != VideoPbMode.MODE_VIDEO_PLAY) {
                        return;
                    }
                    if (msg.arg1 == 1) {
                        VideoPbPresenter.this.videoPbView.showLoadingCircle(true);
                        VideoPbPresenter.this.waitForCaching = Boolean.valueOf(true);
                        return;
                    } else if (msg.arg1 == 2) {
                        VideoPbPresenter.this.videoPbView.showLoadingCircle(false);
                        VideoPbPresenter.this.waitForCaching = Boolean.valueOf(false);
                        return;
                    } else {
                        return;
                    }
                case AppMessage.EVENT_CACHE_PROGRESS_NOTIFY /*1538*/:
                    if (VideoPbPresenter.this.cacheFlag && VideoPbPresenter.this.videoPbMode != VideoPbMode.MODE_VIDEO_IDLE && VideoPbPresenter.this.videoPbMode != VideoPbMode.MODE_VIDEO_PAUSE) {
                        if (VideoPbPresenter.this.waitForCaching.booleanValue()) {
                            VideoPbPresenter.this.videoPbView.setLoadPercent(msg.arg1);
                        }
                        VideoPbPresenter.this.videoPbView.setSeekBarSecondProgress(msg.arg2);
                        return;
                    }
                    return;
                case AppMessage.EVENT_VIDEO_PLAY_COMPLETED /*1539*/:
                    AppLog.i(VideoPbPresenter.this.TAG, "receive EVENT_VIDEO_PLAY_COMPLETED");
                    if (VideoPbPresenter.this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY || VideoPbPresenter.this.videoPbMode == VideoPbMode.MODE_VIDEO_PAUSE) {
                        VideoPbPresenter.this.cacheFlag = false;
                        VideoPbPresenter.this.stopVideoPb();
                        VideoPbPresenter.this.videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
                        return;
                    }
                    return;
                case AppMessage.MESSAGE_UPDATE_VIDEOPB_BAR /*1540*/:
                    if (VideoPbPresenter.this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY && VideoPbPresenter.this.needUpdateSeekBar) {
                        VideoPbPresenter.this.videoPbView.setSeekBarProgress(msg.arg1);
                        return;
                    }
                    return;
                case AppMessage.MESSAGE_CANCEL_VIDEO_DOWNLOAD /*1541*/:
                    AppLog.d(VideoPbPresenter.this.TAG, "receive CANCEL_VIDEO_DOWNLOAD_SUCCESS");
                    if (VideoPbPresenter.this.singleDownloadDialog != null) {
                        VideoPbPresenter.this.singleDownloadDialog.dismissDownloadDialog();
                    }
                    if (VideoPbPresenter.this.downloadProgressTimer != null) {
                        VideoPbPresenter.this.downloadProgressTimer.cancel();
                    }
                    if (VideoPbPresenter.this.fileOperation.cancelDownload()) {
                        try {
                            Thread.currentThread();
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        File file = new File(Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH + VideoPbPresenter.this.curVideoFile.getFileName());
                        if (file.exists()) {
                            file.delete();
                        }
                        VideoPbPresenter.this.videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
                        MyToast.show(VideoPbPresenter.this.activity, (int) R.string.dialog_cancel_downloading_succeeded);
                        return;
                    }
                    MyToast.show(VideoPbPresenter.this.activity, (int) R.string.dialog_cancel_downloading_failed);
                    return;
                default:
                    return;
            }
        }
    }

    public VideoPbPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.curVideoPosition = activity.getIntent().getExtras().getInt("curfilePosition");
        this.curVideoFile = ((MultiPbItemInfo) this.fileList.get(this.curVideoPosition)).iCatchFile;
        AppLog.i(this.TAG, "cur video position=" + this.curVideoPosition + " video name=" + this.curVideoFile.getFileName());
        initClint();
        addEventListener();
    }

    public void updatePbSeekbar(double pts) {
        if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY && this.needUpdateSeekBar) {
            this.currentTime = pts;
            this.handler.obtainMessage(AppMessage.MESSAGE_UPDATE_VIDEOPB_BAR, new Double(this.currentTime * 100.0d).intValue(), 0).sendToTarget();
        }
    }

    public void setView(VideoPbView videoPbView) {
        this.videoPbView = videoPbView;
        initCfg();
        initView();
        play();
    }

    private void initView() {
        String fileName = this.curVideoFile.getFileName();
        this.videoPbView.setVideoNameTxv(fileName.substring(fileName.lastIndexOf("/") + 1));
    }

    public void initClint() {
        this.myCamera = GlobalInfo.getInstance().getCurrentCamera();
        this.videoPlayback = VideoPlayback.getInstance();
        this.cameraAction = CameraAction.getInstance();
        this.fileOperation = FileOperation.getInstance();
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
            if (this.videoPlayback.startPlaybackStream(this.curVideoFile)) {
                this.videoPbMode = VideoPbMode.MODE_VIDEO_PLAY;
                startVideoPb();
                this.needUpdateSeekBar = true;
                AppLog.i(this.TAG, "seekBar.getProgress() =" + this.videoPbView.getSeekBarProgress());
                int tempDuration = this.videoPlayback.getVideoDuration();
                AppLog.i(this.TAG, "end getLength = " + tempDuration);
                this.videoPbView.setSeekbarEnabled(true);
                this.videoPbView.setPlayBtnSrc(R.drawable.ic_pause_white_36dp);
                this.videoPbView.setTimeLapsedValue("00:00");
                this.videoPbView.setTimeDurationValue(ConvertTools.secondsToMinuteOrHours(tempDuration / 100));
                this.videoPbView.setSeekBarMaxValue(tempDuration);
                this.videoDuration = tempDuration;
                AppLog.i(this.TAG, "has start the GetVideoFrameThread() to get play video");
                return;
            }
            MyToast.show(this.activity, (int) R.string.dialog_failed);
            AppLog.e(this.TAG, "failed to startPlaybackStream");
        } else if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PAUSE) {
            AppLog.i(this.TAG, "mode == MODE_VIDEO_PAUSE");
            if (this.videoPlayback.resumePlayback()) {
                this.videoPbMode = VideoPbMode.MODE_VIDEO_PLAY;
                this.needUpdateSeekBar = true;
                this.videoPbView.setPlayBtnSrc(R.drawable.ic_pause_white_36dp);
                this.videoPbView.setPlayCircleImageViewVisibility(8);
                return;
            }
            MyToast.show(this.activity, (int) R.string.dialog_failed);
            AppLog.i(this.TAG, "failed to resumePlayback");
        } else if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY) {
            AppLog.i(this.TAG, "begin pause the playing");
            if (this.videoPlayback.pausePlayback()) {
                this.videoPbMode = VideoPbMode.MODE_VIDEO_PAUSE;
                this.videoPbView.setPlayBtnSrc(R.drawable.ic_play_arrow_white_36dp);
                this.videoPbView.setPlayCircleImageViewVisibility(0);
                this.videoPbView.showLoadingCircle(false);
                return;
            }
            MyToast.show(this.activity, (int) R.string.dialog_failed);
            AppLog.i(this.TAG, "failed to pausePlayback");
        }
    }

    public void seekBarOnStopTrackingTouch() {
        this.lastSeekBarPosition = this.videoPbView.getSeekBarProgress();
        this.videoPbView.setTimeLapsedValue(ConvertTools.secondsToMinuteOrHours(this.lastSeekBarPosition / 100));
        if (this.videoPlayback.videoSeek(((double) this.lastSeekBarPosition) / 100.0d)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            this.videoPbView.setSeekBarProgress(this.lastSeekBarPosition);
            MyToast.show(this.activity, (int) R.string.dialog_failed);
        }
        this.needUpdateSeekBar = true;
    }

    public void seekBarOnStartTrackingTouch() {
        this.needUpdateSeekBar = false;
        this.lastSeekBarPosition = this.videoPbView.getSeekBarProgress();
    }

    public void setTimeLapsedValue(int progress) {
        this.videoPbView.setTimeLapsedValue(ConvertTools.secondsToMinuteOrHours(progress / 100));
    }

    private void startVideoPb() {
        AppLog.i(this.TAG, "startVideoPb");
        this.videoPbView.startMPreview(this.myCamera, 1);
        this.cacheFlag = true;
        this.waitForCaching = Boolean.valueOf(true);
        this.videoPbView.setPlayCircleImageViewVisibility(8);
        this.videoPbView.showLoadingCircle(true);
    }

    public void stopVideoPb() {
        this.videoPbView.showLoadingCircle(false);
        this.videoPbView.setTimeLapsedValue("00:00");
        this.videoPlayback.stopPlaybackStream();
        this.videoPbView.stopMPreview();
        this.videoPbView.setPlayBtnSrc(R.drawable.ic_play_arrow_white_36dp);
        this.videoPbView.setSeekBarProgress(0);
        this.videoPbView.setSeekBarSecondProgress(0);
        this.videoPbView.setTopBarVisibility(0);
        this.videoPbView.setBottomBarVisibility(0);
        this.videoPbView.setPlayCircleImageViewVisibility(0);
        this.videoPbView.setSeekbarEnabled(false);
        this.videoPbMode = VideoPbMode.MODE_VIDEO_IDLE;
    }

    public void delete() {
        if (this.videoPbMode == VideoPbMode.MODE_VIDEO_IDLE) {
            showDeleteEnsureDialog();
        } else {
            MyToast.show(this.activity, "Operation do not be allowed!Please stop playing video!");
        }
    }

    public void download() {
        if (this.videoPbMode == VideoPbMode.MODE_VIDEO_IDLE) {
            showDownloadEnsureDialog();
        } else {
            MyToast.show(this.activity, "Operation do not be allowed!Please stop playing video!");
        }
    }

    public void refresh() {
        AppLog.d(this.TAG, "refresh");
        initClint();
        addEventListener();
        this.handler.post(new Runnable() {
            public void run() {
                VideoPbPresenter.this.stopVideoPb();
            }
        });
    }

    public void showBar(boolean isShowBar) {
        if (!isShowBar) {
            this.videoPbView.setBottomBarVisibility(0);
            this.videoPbView.setTopBarVisibility(0);
            if (this.videoPbMode != VideoPbMode.MODE_VIDEO_PLAY) {
                this.videoPbView.setPlayCircleImageViewVisibility(0);
            }
        } else if (this.videoPbMode == VideoPbMode.MODE_VIDEO_PLAY) {
            this.videoPbView.setBottomBarVisibility(8);
            this.videoPbView.setTopBarVisibility(8);
            this.videoPbView.setPlayCircleImageViewVisibility(8);
        }
    }

    public void showDownloadEnsureDialog() {
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_downloading_single);
        long videoFileSize = (((MultiPbItemInfo) this.fileList.get(this.curVideoPosition)).getFileSizeInteger() / 1024) / 1024;
        AppLog.d(this.TAG, "video FileSize=" + videoFileSize);
        long seconds = videoFileSize % 60;
        builder.setMessage(this.activity.getResources().getString(R.string.gallery_download_with_vid_msg).replace("$1$", "1").replace("$3$", String.valueOf(seconds)).replace("$2$", String.valueOf(videoFileSize / 60)));
        builder.setNegativeButton(R.string.gallery_download, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AppLog.d(VideoPbPresenter.this.TAG, "showProgressDialog");
                if (SystemInfo.getSDFreeSize() < VideoPbPresenter.this.curVideoFile.getFileSize()) {
                    dialog.dismiss();
                    MyToast.show(VideoPbPresenter.this.activity, (int) R.string.text_sd_card_memory_shortage);
                    return;
                }
                VideoPbPresenter.this.singleDownloadDialog = new SingleDownloadDialog(VideoPbPresenter.this.activity, VideoPbPresenter.this.curVideoFile);
                VideoPbPresenter.this.singleDownloadDialog.setBackBtnOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        VideoPbPresenter.this.handler.obtainMessage(AppMessage.MESSAGE_CANCEL_VIDEO_DOWNLOAD, 0, 0).sendToTarget();
                    }
                });
                VideoPbPresenter.this.singleDownloadDialog.showDownloadDialog();
                VideoPbPresenter.this.executor = Executors.newSingleThreadExecutor();
                VideoPbPresenter.this.executor.submit(new DownloadThread(), null);
                VideoPbPresenter.this.downloadProgressTimer = new Timer();
                VideoPbPresenter.this.downloadProgressTimer.schedule(new DownloadProcessTask(), 0, 1000);
            }
        });
        builder.setPositiveButton(R.string.gallery_cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showDeleteEnsureDialog() {
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setTitle(this.activity.getResources().getString(R.string.gallery_delete_des).replace("$1$", "1"));
        builder.setNegativeButton(R.string.gallery_delete, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MyProgressDialog.showProgressDialog(VideoPbPresenter.this.activity, (int) R.string.dialog_deleting);
                VideoPbPresenter.this.executor = Executors.newSingleThreadExecutor();
                VideoPbPresenter.this.executor.submit(new DeleteThread(), null);
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
