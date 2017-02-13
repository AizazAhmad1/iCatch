package com.icatch.ismartdv2016.Thread.Decoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioTrack;
import android.view.SurfaceHolder;
import com.icatch.ismartdv2016.Dbl.CameraSlotSQLite;
import com.icatch.ismartdv2016.ExtendComponent.MPreview;
import com.icatch.ismartdv2016.Listener.VideoFramePtsChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.SdkApi.PreviewStream;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import com.icatch.ismartdv2016.Tools.ScaleTool;
import com.icatch.ismartdv2016.test.test;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.ICatchWificamVideoPlayback;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchPbStreamPausedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import java.nio.ByteBuffer;

public class MjpgDecoderThread {
    private static final String TAG = "MjpgDecoderThread";
    private AudioThread audioThread;
    private Rect drawFrameRect;
    private int frameHeight;
    private int frameWidth;
    private final MPreview mPreview;
    private int previewLaunchMode;
    private PreviewStream previewStream = PreviewStream.getInstance();
    private final ICatchWificamPreview previewStreamControl;
    private SurfaceHolder surfaceHolder;
    private Bitmap videoFrameBitmap;
    private ICatchWificamVideoPlayback videoPbControl;
    private VideoFramePtsChangedListener videoPbUpdateBarLitener = null;
    private VideoPlayback videoPlayback = VideoPlayback.getInstance();
    private VideoThread videoThread;

    private class AudioThread extends Thread {
        private AudioTrack audioTrack;
        private boolean done;

        private AudioThread() {
            this.done = false;
        }

        public void run() {
            ICatchAudioFormat audioFormat;
            int i = 12;
            AppLog.i(MjpgDecoderThread.TAG, "Run AudioThread");
            if (MjpgDecoderThread.this.previewLaunchMode == 2) {
                audioFormat = MjpgDecoderThread.this.previewStream.getAudioFormat(MjpgDecoderThread.this.previewStreamControl);
            } else {
                audioFormat = MjpgDecoderThread.this.videoPlayback.getAudioFormat();
            }
            if (audioFormat == null) {
                AppLog.e(MjpgDecoderThread.TAG, "Run AudioThread audioFormat is null!");
                return;
            }
            int i2;
            int i3;
            int i4;
            int frequency = audioFormat.getFrequency();
            if (audioFormat.getNChannels() == 2) {
                i2 = 12;
            } else {
                i2 = 4;
            }
            if (audioFormat.getSampleBits() == 16) {
                i3 = 2;
            } else {
                i3 = 3;
            }
            int bufferSize = AudioTrack.getMinBufferSize(frequency, i2, i3);
            i3 = audioFormat.getFrequency();
            if (audioFormat.getNChannels() != 2) {
                i = 4;
            }
            if (audioFormat.getSampleBits() == 16) {
                i4 = 2;
            } else {
                i4 = 3;
            }
            this.audioTrack = new AudioTrack(3, i3, i, i4, bufferSize, 1);
            this.audioTrack.play();
            AppLog.i(MjpgDecoderThread.TAG, "Run AudioThread 3");
            byte[] audioBuffer = new byte[51200];
            ICatchFrameBuffer icatchBuffer = new ICatchFrameBuffer(51200);
            icatchBuffer.setBuffer(audioBuffer);
            while (!this.done) {
                boolean temp = false;
                try {
                    temp = MjpgDecoderThread.this.previewLaunchMode == 2 ? MjpgDecoderThread.this.previewStreamControl.getNextAudioFrame(icatchBuffer) : MjpgDecoderThread.this.videoPbControl.getNextAudioFrame(icatchBuffer);
                } catch (IchSocketException e) {
                    AppLog.e(MjpgDecoderThread.TAG, " getNextAudioFrame IchSocketException");
                    e.printStackTrace();
                    return;
                } catch (IchBufferTooSmallException e2) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchBufferTooSmallException");
                    e2.printStackTrace();
                    return;
                } catch (IchCameraModeException e3) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchCameraModeException");
                    e3.printStackTrace();
                    return;
                } catch (IchInvalidSessionException e4) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchInvalidSessionException");
                    e4.printStackTrace();
                    return;
                } catch (IchTryAgainException e5) {
                    e5.printStackTrace();
                } catch (IchStreamNotRunningException e6) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchStreamNotRunningException");
                    e6.printStackTrace();
                    return;
                } catch (IchInvalidArgumentException e7) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchInvalidArgumentException");
                    e7.printStackTrace();
                    return;
                } catch (IchAudioStreamClosedException e8) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchAudioStreamClosedException");
                    e8.printStackTrace();
                } catch (IchPbStreamPausedException e9) {
                    AppLog.e(MjpgDecoderThread.TAG, "getNextAudioFrame IchPbStreamPausedException");
                    e9.printStackTrace();
                }
                if (temp) {
                    this.audioTrack.write(icatchBuffer.getBuffer(), 0, icatchBuffer.getFrameSize());
                }
            }
            this.audioTrack.stop();
            this.audioTrack.release();
            AppLog.i(MjpgDecoderThread.TAG, "stopMPreview audio thread");
        }

        public void requestExitAndWait() {
            this.done = true;
            try {
                join();
            } catch (InterruptedException e) {
            }
        }
    }

    private class VideoThread extends Thread {
        private ByteBuffer bmpBuf;
        private boolean done = false;
        private byte[] pixelBuf;

        VideoThread() {
            this.pixelBuf = new byte[((MjpgDecoderThread.this.frameWidth * MjpgDecoderThread.this.frameHeight) * 4)];
            this.bmpBuf = ByteBuffer.wrap(this.pixelBuf);
            MjpgDecoderThread.this.videoFrameBitmap = Bitmap.createBitmap(MjpgDecoderThread.this.frameWidth, MjpgDecoderThread.this.frameHeight, Config.ARGB_8888);
            MjpgDecoderThread.this.drawFrameRect = new Rect(0, 0, MjpgDecoderThread.this.frameWidth, MjpgDecoderThread.this.frameHeight);
        }

        public void run() {
            AppLog.i(MjpgDecoderThread.TAG, "start running video thread");
            ICatchFrameBuffer buffer = new ICatchFrameBuffer((MjpgDecoderThread.this.frameWidth * MjpgDecoderThread.this.frameHeight) * 4);
            buffer.setBuffer(this.pixelBuf);
            boolean isSaveBitmapToDb = false;
            AppLog.i(MjpgDecoderThread.TAG, "start running preview thread done=" + this.done);
            while (!this.done) {
                boolean temp = false;
                try {
                    temp = MjpgDecoderThread.this.previewLaunchMode == 2 ? MjpgDecoderThread.this.previewStreamControl.getNextVideoFrame(buffer) : MjpgDecoderThread.this.videoPbControl.getNextVideoFrame(buffer);
                } catch (IchSocketException e) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchSocketException");
                    e.printStackTrace();
                    return;
                } catch (IchBufferTooSmallException e2) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchBufferTooSmallException");
                    e2.printStackTrace();
                    return;
                } catch (IchCameraModeException e3) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchCameraModeException");
                    e3.printStackTrace();
                    return;
                } catch (IchInvalidSessionException e4) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchInvalidSessionException");
                    e4.printStackTrace();
                    return;
                } catch (IchTryAgainException e5) {
                    e5.printStackTrace();
                } catch (IchStreamNotRunningException e6) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchStreamNotRunningException");
                    e6.printStackTrace();
                    return;
                } catch (IchInvalidArgumentException e7) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchInvalidArgumentException");
                    e7.printStackTrace();
                    return;
                } catch (IchVideoStreamClosedException e8) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchVideoStreamClosedException");
                    test.saveImage(MjpgDecoderThread.this.videoFrameBitmap, System.currentTimeMillis());
                    e8.printStackTrace();
                    return;
                } catch (IchPbStreamPausedException e9) {
                    AppLog.e(MjpgDecoderThread.TAG, "IchPbStreamPausedException");
                    e9.printStackTrace();
                }
                if (temp) {
                    if (buffer == null || buffer.getFrameSize() == 0) {
                        AppLog.e(MjpgDecoderThread.TAG, "getNextVideoFrame buffer == null\n");
                    } else {
                        this.bmpBuf.rewind();
                        if (MjpgDecoderThread.this.videoFrameBitmap != null) {
                            MjpgDecoderThread.this.videoFrameBitmap.copyPixelsFromBuffer(this.bmpBuf);
                            if (!(isSaveBitmapToDb || MjpgDecoderThread.this.videoFrameBitmap == null || MjpgDecoderThread.this.previewLaunchMode != 2)) {
                                CameraSlotSQLite.getInstance().updateImage(MjpgDecoderThread.this.videoFrameBitmap);
                                isSaveBitmapToDb = true;
                            }
                            Canvas canvas = MjpgDecoderThread.this.surfaceHolder.lockCanvas();
                            if (canvas != null) {
                                MjpgDecoderThread.this.drawFrameRect = ScaleTool.getScaledPosition(MjpgDecoderThread.this.frameWidth, MjpgDecoderThread.this.frameHeight, MjpgDecoderThread.this.mPreview.getWidth(), MjpgDecoderThread.this.mPreview.getHeight());
                                canvas.drawBitmap(MjpgDecoderThread.this.videoFrameBitmap, null, MjpgDecoderThread.this.drawFrameRect, null);
                                MjpgDecoderThread.this.surfaceHolder.unlockCanvasAndPost(canvas);
                                if (MjpgDecoderThread.this.previewLaunchMode == 1 && MjpgDecoderThread.this.videoPbUpdateBarLitener != null) {
                                    MjpgDecoderThread.this.videoPbUpdateBarLitener.onFramePtsChanged(buffer.getPresentationTime());
                                }
                            }
                        }
                    }
                }
            }
            if (MjpgDecoderThread.this.videoFrameBitmap != null && MjpgDecoderThread.this.previewLaunchMode == 2) {
                CameraSlotSQLite.getInstance().updateImage(MjpgDecoderThread.this.videoFrameBitmap);
            }
            AppLog.i(MjpgDecoderThread.TAG, "stopMPreview video thread");
        }

        public void requestExitAndWait() {
            this.done = true;
            try {
                join();
            } catch (InterruptedException e) {
            }
        }
    }

    public MjpgDecoderThread(MyCamera mCamera, SurfaceHolder holder, MPreview mPreview, int previewLaunchMode, VideoFramePtsChangedListener videoPbUpdateBarLitener) {
        this.surfaceHolder = holder;
        this.mPreview = mPreview;
        this.previewLaunchMode = previewLaunchMode;
        this.previewStreamControl = mCamera.getpreviewStreamClient();
        this.videoPbControl = mCamera.getVideoPlaybackClint();
        this.videoPbUpdateBarLitener = videoPbUpdateBarLitener;
        if (previewLaunchMode == 1) {
            while (true) {
                if (this.frameWidth != 0 && this.frameHeight != 0) {
                    break;
                }
                this.frameWidth = this.videoPlayback.getVideoFormat().getVideoW();
                this.frameHeight = this.videoPlayback.getVideoFormat().getVideoH();
                AppLog.d(TAG, "init preview, VideoW =" + this.frameWidth);
                AppLog.d(TAG, "init preview, VideoH =" + this.frameHeight);
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (previewLaunchMode == 2) {
            this.frameWidth = this.previewStream.getVideoWidth(mCamera.getpreviewStreamClient());
            this.frameHeight = this.previewStream.getVideoHeigth(mCamera.getpreviewStreamClient());
        }
        holder.setFormat(1);
    }

    public void start(boolean enableAudio, boolean enableVideo) {
        AppLog.i(TAG, "start");
        if (enableAudio) {
            this.audioThread = new AudioThread();
            this.audioThread.start();
        }
        if (enableVideo) {
            this.videoThread = new VideoThread();
            this.videoThread.start();
        }
    }

    public boolean isAlive() {
        if (this.videoThread != null && this.videoThread.isAlive()) {
            return true;
        }
        if (this.audioThread == null || !this.audioThread.isAlive()) {
            return false;
        }
        return true;
    }

    public void stop() {
        if (this.audioThread != null) {
            this.audioThread.requestExitAndWait();
        }
        if (this.videoThread != null) {
            this.videoThread.requestExitAndWait();
        }
    }

    public void redrawBitmap() {
        AppLog.i(TAG, "redrawBitmap");
        if (this.videoFrameBitmap != null) {
            Canvas canvas = this.surfaceHolder.lockCanvas();
            if (canvas != null) {
                int w = this.mPreview.getWidth();
                int h = this.mPreview.getHeight();
                AppLog.i(TAG, "redrawBitmap mPreview.getWidth()=" + this.mPreview.getWidth());
                AppLog.i(TAG, "redrawBitmap mPreview.getHeight()=" + this.mPreview.getHeight());
                canvas.drawBitmap(this.videoFrameBitmap, null, ScaleTool.getScaledPosition(this.frameWidth, this.frameHeight, w, h), null);
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void redrawBitmap(SurfaceHolder holder, int w, int h) {
        SurfaceHolder surfaceHolder = holder;
        AppLog.d(TAG, "redrawBitmap w=" + w + " h=" + h);
        AppLog.d(TAG, "redrawBitmap frameWidth=" + this.frameWidth + " frameHeight=" + this.frameHeight);
        AppLog.d(TAG, "redrawBitmap videoFrameBitmap=" + this.videoFrameBitmap);
        if (this.videoFrameBitmap != null) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                this.drawFrameRect = ScaleTool.getScaledPosition(this.frameWidth, this.frameHeight, w, h);
                canvas.drawBitmap(this.videoFrameBitmap, null, this.drawFrameRect, null);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
