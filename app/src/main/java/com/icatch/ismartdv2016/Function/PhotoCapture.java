package com.icatch.ismartdv2016.Function;

import android.content.Context;
import android.media.MediaPlayer;
import com.icatch.ismartdv2016.Android.NoDoubleClickListener;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import java.util.Timer;
import java.util.TimerTask;

public class PhotoCapture {
    private static final String TAG = "PhotoCapture";
    private static final int TYPE_BURST_CAPTURE = 1;
    private static final int TYPE_NORMAL_CAPTURE = 2;
    private static PhotoCapture photoCapture;
    private MediaPlayer continuousCaptureBeep;
    private MediaPlayer delayBeep;
    private OnStopPreviewListener onStopPreviewListener;
    private MediaPlayer stillCaptureStartBeep;

    private class CaptureAudioTask extends TimerTask {
        private int burstNumber;
        private int type = PhotoCapture.TYPE_NORMAL_CAPTURE;

        public CaptureAudioTask(int burstNumber, int type) {
            this.burstNumber = burstNumber;
            this.type = type;
        }

        public void run() {
            if (this.type == PhotoCapture.TYPE_NORMAL_CAPTURE) {
                if (this.burstNumber > 0) {
                    AppLog.i(PhotoCapture.TAG, "CaptureAudioTask remainBurstNumer =" + this.burstNumber);
                    PhotoCapture.this.stillCaptureStartBeep.start();
                    this.burstNumber--;
                    return;
                }
                cancel();
            } else if (this.burstNumber > 0) {
                AppLog.i(PhotoCapture.TAG, "CaptureAudioTask remainBurstNumer =" + this.burstNumber);
                PhotoCapture.this.continuousCaptureBeep.start();
                this.burstNumber--;
            } else {
                cancel();
            }
        }
    }

    class CaptureThread implements Runnable {
        CaptureThread() {
        }

        public void run() {
            int delayTime;
            int timerDelay;
            AppLog.i(PhotoCapture.TAG, "start CameraCaptureThread");
            if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY)) {
                delayTime = CameraProperties.getInstance().getCurrentCaptureDelay();
            } else {
                delayTime = 0;
            }
            if (delayTime < NoDoubleClickListener.MIN_CLICK_DELAY_TIME) {
                PhotoCapture.this.onStopPreviewListener.onStop();
            } else if (CameraProperties.getInstance().hasFuction(PropertyId.CAPTURE_DELAY_MODE)) {
                TimerTask anonymousClass1 = new TimerTask() {
                    public void run() {
                        PhotoCapture.this.onStopPreviewListener.onStop();
                    }
                };
                new Timer(true).schedule(anonymousClass1, (long) (delayTime - 500));
            } else {
                PhotoCapture.this.onStopPreviewListener.onStop();
            }
            int needCaptureCount = PhotoCapture.TYPE_BURST_CAPTURE;
            if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER)) {
                needCaptureCount = CameraProperties.getInstance().getCurrentAppBurstNum();
            }
            if (needCaptureCount == PhotoCapture.TYPE_BURST_CAPTURE) {
                new Timer(true).schedule(new CaptureAudioTask(needCaptureCount, PhotoCapture.TYPE_NORMAL_CAPTURE), (long) delayTime);
            } else {
                new Timer(true).schedule(new CaptureAudioTask(needCaptureCount, PhotoCapture.TYPE_BURST_CAPTURE), (long) delayTime, 280);
            }
            int count = delayTime / NoDoubleClickListener.MIN_CLICK_DELAY_TIME;
            if (delayTime >= 5000) {
                Timer delayTimer = new Timer(true);
                delayTimer.schedule(new DelayTimerTask(count / PhotoCapture.TYPE_NORMAL_CAPTURE, delayTimer), 0, 1000);
                timerDelay = delayTime;
            } else {
                timerDelay = 0;
                count = delayTime / 500;
            }
            if (delayTime >= 3000) {
                Timer delayTimer1 = new Timer(true);
                delayTimer1.schedule(new DelayTimerTask(count / PhotoCapture.TYPE_NORMAL_CAPTURE, delayTimer1), (long) (timerDelay / PhotoCapture.TYPE_NORMAL_CAPTURE), 500);
                timerDelay = delayTime;
            } else {
                timerDelay = 0;
                count = delayTime / 250;
            }
            Timer delayTimer2 = new Timer(true);
            delayTimer2.schedule(new DelayTimerTask(count, delayTimer2), (long) (timerDelay - (timerDelay / 4)), 250);
            CameraAction.getInstance().triggerCapturePhoto();
            AppLog.i(PhotoCapture.TAG, "delayTime = " + delayTime + " needCaptureCount=" + needCaptureCount);
            AppLog.i(PhotoCapture.TAG, "end CameraCaptureThread");
        }
    }

    private class DelayTimerTask extends TimerTask {
        private int count;
        private Timer timer;

        public DelayTimerTask(int count, Timer timer) {
            this.count = count;
            this.timer = timer;
        }

        public void run() {
            int i = this.count;
            this.count = i - 1;
            if (i > 0) {
                PhotoCapture.this.delayBeep.start();
            } else if (this.timer != null) {
                this.timer.cancel();
            }
        }
    }

    public interface OnStopPreviewListener {
        void onStop();
    }

    public static PhotoCapture getInstance() {
        if (photoCapture == null) {
            photoCapture = new PhotoCapture();
        }
        return photoCapture;
    }

    private PhotoCapture() {
        Context context = GlobalInfo.getInstance().getCurrentApp();
        this.stillCaptureStartBeep = MediaPlayer.create(context, R.raw.captureshutter);
        this.delayBeep = MediaPlayer.create(context, R.raw.delay_beep);
        this.continuousCaptureBeep = MediaPlayer.create(context, R.raw.captureburst);
    }

    public void startCapture() {
        new CaptureThread().run();
    }

    public void addOnStopPreviewListener(OnStopPreviewListener onStopPreviewListener) {
        this.onStopPreviewListener = onStopPreviewListener;
    }
}
