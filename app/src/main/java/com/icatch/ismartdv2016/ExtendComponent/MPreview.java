package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.Listener.VideoFramePtsChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.SdkApi.PreviewStream;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import com.icatch.ismartdv2016.Thread.Decoder.H264DecoderThread;
import com.icatch.ismartdv2016.Thread.Decoder.MjpgDecoderThread;
import com.icatch.wificam.customer.type.ICatchCodec;
import com.slidingmenu.lib.R;

public class MPreview extends SurfaceView implements Callback {
    private static final String TAG = "MPreview";
    private Object abc;
    private int frmH = 0;
    private int frmW = 0;
    private H264DecoderThread h264DecoderThread;
    private boolean hasSurface = false;
    private SurfaceHolder holder;
    private MyCamera mCamera;
    private MjpgDecoderThread mjpgDecoderThread;
    private boolean needStart = false;
    private int parentHeight = 0;
    private int parentWidth = 0;
    private int previewCodec;
    private int previewLaunchMode;
    private PreviewStream previewStream = PreviewStream.getInstance();
    private VideoFramePtsChangedListener videoPbUpdateBarLitener = null;
    private VideoPlayback videoPlayback = VideoPlayback.getInstance();

    public MPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        AppLog.i(TAG, "create MPreview");
        this.holder = getHolder();
        this.holder.addCallback(this);
    }

    public void start(MyCamera mCamera, int previewLaunchMode) {
        AppLog.i(TAG, "start preview hasSurface =" + this.hasSurface + " previewLaunchMode=" + previewLaunchMode);
        this.previewLaunchMode = previewLaunchMode;
        if (previewLaunchMode == 1) {
            while (true) {
                if (this.frmW != 0 && this.frmH != 0) {
                    break;
                }
                this.frmW = this.videoPlayback.getVideoFormat().getVideoW();
                this.frmH = this.videoPlayback.getVideoFormat().getVideoH();
                AppLog.d(TAG, "init preview, VideoW =" + this.frmW);
                AppLog.d(TAG, "init preview, VideoH =" + this.frmH);
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (previewLaunchMode == 2) {
            this.frmW = this.previewStream.getVideoWidth(mCamera.getpreviewStreamClient());
            this.frmH = this.previewStream.getVideoHeigth(mCamera.getpreviewStreamClient());
            AppLog.d(TAG, "RT_PREVIEW_MODE init preview, VideoW =" + this.frmW);
            AppLog.d(TAG, "RT_PREVIEW_MODE init preview, VideoH =" + this.frmH);
        }
        if (this.frmH * this.frmW != 0) {
            this.mCamera = mCamera;
            if (this.hasSurface) {
                startDecoderThread(previewLaunchMode);
            } else {
                this.needStart = true;
            }
        }
    }

    public boolean stop() {
        AppLog.i(TAG, "stopMPreview preview");
        if (this.mjpgDecoderThread != null) {
            this.mjpgDecoderThread.stop();
            postInvalidate();
            AppLog.i(TAG, "start stopMPreview mjpgDecoderThread.isAlive() =" + this.mjpgDecoderThread.isAlive());
        }
        if (this.h264DecoderThread != null) {
            this.h264DecoderThread.stop();
            AppLog.i(TAG, "start stopMPreview h264DecoderThread.isAlive() =" + this.h264DecoderThread.isAlive());
        }
        this.needStart = false;
        AppLog.i(TAG, "end preview");
        return true;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        AppLog.i(TAG, "surfaceCreated hasSurface =" + this.hasSurface);
        this.hasSurface = true;
        if (this.needStart) {
            startDecoderThread(this.previewLaunchMode);
        }
    }

    public void startDecoderThread(int previewLaunchMode) {
        AppLog.i(TAG, "start startDecoderThread");
        boolean enableAudio = true;
        if (previewLaunchMode == 1) {
            this.previewCodec = this.videoPlayback.getVideoFormat().getCodec();
            enableAudio = true;
        } else if (previewLaunchMode == 2) {
            this.previewCodec = this.previewStream.getCodec(this.mCamera.getpreviewStreamClient());
            enableAudio = this.previewStream.supportAudio(this.mCamera.getpreviewStreamClient()) && !AppInfo.disableAudio;
        }
        AppLog.i(TAG, "start startDecoderThread enableAudio=" + enableAudio);
        switch (this.previewCodec) {
            case R.styleable.SherlockTheme_textColorSearchUrl /*41*/:
                this.parentWidth = 0;
                this.parentHeight = 0;
                getHandler().post(new Runnable() {
                    public void run() {
                        AppLog.i(MPreview.TAG, "start startDecoderThread.I'm coming......");
                    }
                });
                this.h264DecoderThread = new H264DecoderThread(this.mCamera, this.holder, this, previewLaunchMode, this.videoPbUpdateBarLitener);
                this.h264DecoderThread.start(enableAudio, true);
                setSurfaceViewArea();
                return;
            case ICatchCodec.ICH_CODEC_RGBA_8888 /*134*/:
                this.mjpgDecoderThread = new MjpgDecoderThread(this.mCamera, this.holder, this, previewLaunchMode, this.videoPbUpdateBarLitener);
                this.mjpgDecoderThread.start(enableAudio, true);
                setSurfaceViewArea();
                return;
            default:
                return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        AppLog.i(TAG, " surfaceDestroyed hasSurface =" + this.hasSurface);
        this.hasSurface = false;
        stop();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        AppLog.d(TAG, " redrawBitmap");
        AppLog.d(TAG, "start startDecoderThread.I'm coming......");
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        AppLog.d(TAG, " change ....onLayout");
        getHandler().post(new Runnable() {
            public void run() {
                MPreview.this.setSurfaceViewArea();
            }
        });
    }

    public void setSurfaceViewArea() {
        AppLog.i(TAG, "setSurfaceViewArea frmW=" + this.frmW);
        AppLog.i(TAG, "setSurfaceViewArea frmH=" + this.frmH);
        if (this.frmH != 0 && this.frmW != 0) {
            View parentView = (View) getParent();
            int mWidth = parentView.getWidth();
            int mHeigth = parentView.getHeight();
            AppLog.i(TAG, "setSurfaceViewArea mWidth=" + mWidth);
            AppLog.i(TAG, "setSurfaceViewArea mHeigth=" + mHeigth);
            if (this.frmH > 1080) {
                AppLog.e(TAG, "Illegal frmW frmW!!!");
            } else if (this.frmH <= 0 || this.frmW <= 0) {
                AppLog.e(TAG, "setSurfaceViewArea frmW or frmH <= 0!!!");
                this.holder.setFixedSize(mWidth, (mWidth * 9) / 16);
            } else if (mWidth != 0 && mHeigth != 0) {
                if (this.previewCodec == ICatchCodec.ICH_CODEC_RGBA_8888) {
                    if (this.mjpgDecoderThread != null && this.previewLaunchMode == 1) {
                        this.mjpgDecoderThread.redrawBitmap(this.holder, mWidth, mHeigth);
                    }
                } else if (this.previewCodec == 41) {
                    if ((this.frmH * mWidth) / this.frmW <= mHeigth) {
                        this.holder.setFixedSize(mWidth, (this.frmH * mWidth) / this.frmW);
                    } else {
                        this.holder.setFixedSize((this.frmW * mHeigth) / this.frmH, mHeigth);
                    }
                }
                AppLog.d(TAG, "end setSurfaceViewArea");
            }
        }
    }

    public void addVideoFramePtsChangedListener(VideoFramePtsChangedListener videoPbUpdateBarLitener) {
        this.videoPbUpdateBarLitener = videoPbUpdateBarLitener;
    }
}
