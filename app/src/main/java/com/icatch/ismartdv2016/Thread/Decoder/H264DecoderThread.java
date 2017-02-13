package com.icatch.ismartdv2016.Thread.Decoder;

import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import com.icatch.ismartdv2016.ExtendComponent.MPreview;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.VideoFramePtsChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.SdkApi.PreviewStream;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import com.icatch.wificam.core.jni.util.ExceptionErr;
import com.icatch.wificam.core.util.SDKEventHandleAPI;
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
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchVideoFormat;
import com.slidingmenu.lib.SlidingMenu;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class H264DecoderThread {
    private static final String TAG = "H264DecoderThread--";
    private int BUFFER_LENGTH = 3686400;
    private boolean audioPlayFlag = false;
    private AudioThread audioThread;
    double curVideoPts = 0.0d;
    private MediaCodec decoder;
    private final MPreview mPreview;
    private int previewLaunchMode;
    private PreviewStream previewStream = PreviewStream.getInstance();
    private final ICatchWificamPreview previewStreamControl;
    private SurfaceHolder surfaceHolder;
    private int timeout = 60000;
    private ICatchWificamVideoPlayback videoPbControl;
    private VideoFramePtsChangedListener videoPbUpdateBarLitener;
    private VideoPlayback videoPlayback = VideoPlayback.getInstance();
    long videoShowtime = 0;
    private VideoThread videoThread;

    private class AudioThread extends Thread {
        private LinkedList<ICatchFrameBuffer> audioQueue;
        private AudioTrack audioTrack;
        private boolean done;
        boolean isFirstShow;

        private AudioThread() {
            this.done = false;
            this.isFirstShow = true;
        }

        public void run() {
            ICatchAudioFormat audioFormat;
            AppLog.i(H264DecoderThread.TAG, "start running AudioThread previewLaunchMode=" + H264DecoderThread.this.previewLaunchMode);
            if (H264DecoderThread.this.previewLaunchMode == 2) {
                audioFormat = H264DecoderThread.this.previewStream.getAudioFormat(H264DecoderThread.this.previewStreamControl);
            } else {
                audioFormat = H264DecoderThread.this.videoPlayback.getAudioFormat();
            }
            if (audioFormat == null) {
                AppLog.e(H264DecoderThread.TAG, "Run AudioThread audioFormat is null!");
                return;
            }
            AppLog.i(H264DecoderThread.TAG, "start running AudioThread audioFormat=" + audioFormat);
            this.audioTrack = new AudioTrack(3, audioFormat.getFrequency(), audioFormat.getNChannels() == 2 ? 12 : 4, audioFormat.getSampleBits() == 16 ? 2 : 3, AudioTrack.getMinBufferSize(audioFormat.getFrequency(), audioFormat.getNChannels() == 2 ? 12 : 4, audioFormat.getSampleBits() == 16 ? 2 : 3), 1);
            AppLog.i(H264DecoderThread.TAG, "start running AudioThread audioFormat.getFrequency()=" + audioFormat.getFrequency());
            AppLog.i(H264DecoderThread.TAG, "start running AudioThread audioFormat.getNChannels()=" + audioFormat.getNChannels());
            AppLog.i(H264DecoderThread.TAG, "start running AudioThread audioFormat.getSampleBits()=" + audioFormat.getSampleBits());
            this.audioTrack.play();
            this.audioQueue = new LinkedList();
            new ICatchFrameBuffer(51200).setBuffer(new byte[51200]);
            while (!this.done) {
                ICatchFrameBuffer icatchBuffer = new ICatchFrameBuffer(51200);
                icatchBuffer.setBuffer(new byte[51200]);
                boolean ret = false;
                try {
                    if (H264DecoderThread.this.previewLaunchMode == 2) {
                        ret = H264DecoderThread.this.previewStreamControl.getNextAudioFrame(icatchBuffer);
                    } else {
                        ret = H264DecoderThread.this.videoPbControl.getNextAudioFrame(icatchBuffer);
                    }
                } catch (IchSocketException e) {
                    AppLog.e(H264DecoderThread.TAG, " getNextAudioFrame IchSocketException");
                    e.printStackTrace();
                    return;
                } catch (IchBufferTooSmallException e2) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchBufferTooSmallException");
                    e2.printStackTrace();
                    return;
                } catch (IchCameraModeException e3) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchCameraModeException");
                    e3.printStackTrace();
                    return;
                } catch (IchInvalidSessionException e4) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchInvalidSessionException");
                    e4.printStackTrace();
                    return;
                } catch (IchTryAgainException e5) {
                    e5.printStackTrace();
                } catch (IchStreamNotRunningException e6) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchStreamNotRunningException");
                    e6.printStackTrace();
                    return;
                } catch (IchInvalidArgumentException e7) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchInvalidArgumentException");
                    e7.printStackTrace();
                    return;
                } catch (IchAudioStreamClosedException e8) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchAudioStreamClosedException");
                    e8.printStackTrace();
                    return;
                } catch (IchPbStreamPausedException e9) {
                    AppLog.e(H264DecoderThread.TAG, "getNextAudioFrame IchPbStreamPausedException");
                    e9.printStackTrace();
                    return;
                }
                if (ret) {
                    this.audioQueue.offer(icatchBuffer);
                    if (H264DecoderThread.this.audioPlayFlag) {
                        ICatchFrameBuffer temp = (ICatchFrameBuffer) this.audioQueue.poll();
                        if (temp != null) {
                            double tempPts = H264DecoderThread.this.curVideoPts;
                            double delayTime = 0.0d;
                            if (this.isFirstShow) {
                                delayTime = (1.0d / GlobalInfo.curFps) * ((double) GlobalInfo.videoCacheNum);
                                AppLog.d(H264DecoderThread.TAG, "delayTime=" + delayTime + " AppInfo.videoCacheNum=" + GlobalInfo.videoCacheNum + " AppInfo.curFps=" + GlobalInfo.curFps);
                                this.isFirstShow = false;
                            }
                            if (H264DecoderThread.this.curVideoPts != -1.0d) {
                                if (temp.getPresentationTime() - (tempPts - delayTime) > GlobalInfo.THRESHOLD_TIME) {
                                    this.audioQueue.addFirst(temp);
                                    AppLog.d(H264DecoderThread.TAG, "audioQueue.addFirst(temp);");
                                } else if (temp.getPresentationTime() - (tempPts - delayTime) < -0.1d && this.audioQueue.size() > 0) {
                                    while (temp.getPresentationTime() - (tempPts - delayTime) < 0.0d && this.audioQueue.size() > 0) {
                                        temp = (ICatchFrameBuffer) this.audioQueue.poll();
                                        if (temp != null) {
                                            AppLog.d(H264DecoderThread.TAG, "audioQueue.poll()----tempPts=" + tempPts + " curVideoPts=" + H264DecoderThread.this.curVideoPts + " curPts=" + temp.getPresentationTime() + " audioQueue size=" + this.audioQueue.size());
                                        }
                                    }
                                }
                            }
                            if (temp != null) {
                                this.audioTrack.write(temp.getBuffer(), 0, temp.getFrameSize());
                            }
                        }
                    }
                }
            }
            this.audioTrack.stop();
            this.audioTrack.release();
            AppLog.i(H264DecoderThread.TAG, "stopMPreview audio thread");
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
        private boolean done;
        int frameSize;
        private BufferInfo info;
        long startTime;

        VideoThread() {
            this.done = false;
            this.startTime = 0;
            this.frameSize = 0;
            this.done = false;
        }

        public void run() {
            AppLog.i(H264DecoderThread.TAG, "h264 run for gettting surface image");
            ByteBuffer[] inputBuffers = H264DecoderThread.this.decoder.getInputBuffers();
            this.info = new BufferInfo();
            byte[] mPixel = new byte[H264DecoderThread.this.BUFFER_LENGTH];
            ICatchFrameBuffer frameBuffer = new ICatchFrameBuffer();
            frameBuffer.setBuffer(mPixel);
            boolean isFirst = true;
            while (!this.done) {
                H264DecoderThread.this.curVideoPts = -1.0d;
                try {
                    boolean retvalue;
                    if (H264DecoderThread.this.previewLaunchMode == 2) {
                        retvalue = H264DecoderThread.this.previewStreamControl.getNextVideoFrame(frameBuffer);
                    } else {
                        retvalue = H264DecoderThread.this.videoPbControl.getNextVideoFrame(frameBuffer);
                    }
                    if (!retvalue) {
                        continue;
                    } else if (frameBuffer.getFrameSize() > 0 && frameBuffer != null) {
                        if (retvalue) {
                            int inIndex = H264DecoderThread.this.decoder.dequeueInputBuffer((long) H264DecoderThread.this.timeout);
                            H264DecoderThread.this.curVideoPts = frameBuffer.getPresentationTime();
                            this.frameSize++;
                            if (isFirst) {
                                isFirst = false;
                                this.startTime = System.currentTimeMillis();
                            }
                            if (inIndex >= 0) {
                                int sampleSize = frameBuffer.getFrameSize();
                                long pts = (long) ((frameBuffer.getPresentationTime() * 1000.0d) * 1000.0d);
                                ByteBuffer buffer = inputBuffers[inIndex];
                                buffer.clear();
                                buffer.rewind();
                                buffer.put(frameBuffer.getBuffer(), 0, sampleSize);
                                H264DecoderThread.this.decoder.queueInputBuffer(inIndex, 0, sampleSize, pts, 0);
                                dequeueAndRenderOutputBuffer(H264DecoderThread.this.timeout);
                            } else if (!(dequeueAndRenderOutputBuffer(H264DecoderThread.this.timeout) || dequeueAndRenderOutputBuffer(30000))) {
                            }
                            if ((this.info.flags & 4) != 0) {
                                break;
                            } else if (H264DecoderThread.this.previewLaunchMode == 1 && H264DecoderThread.this.videoPbUpdateBarLitener != null) {
                                H264DecoderThread.this.videoPbUpdateBarLitener.onFramePtsChanged(frameBuffer.getPresentationTime());
                            }
                        } else {
                            continue;
                        }
                    }
                } catch (IchTryAgainException ex) {
                    ex.printStackTrace();
                } catch (Exception ex2) {
                    AppLog.e(H264DecoderThread.TAG, "getNextVideoFrame Exception");
                    ex2.printStackTrace();
                }
            }
            H264DecoderThread.this.decoder.stop();
            H264DecoderThread.this.decoder.release();
            AppLog.i(H264DecoderThread.TAG, "stopMPreview video thread");
        }

        public boolean dequeueAndRenderOutputBuffer(int outtime) {
            int outIndex = H264DecoderThread.this.decoder.dequeueOutputBuffer(this.info, (long) outtime);
            switch (outIndex) {
                case ExceptionErr.ICH_NOT_SUPPORTED /*-3*/:
                case SDKEventHandleAPI.__SESSION_ID_FOR_ALL /*-2*/:
                case SDKEventHandleAPI.__SESSION_ID_FOR_NON /*-1*/:
                case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                    return false;
                default:
                    H264DecoderThread.this.decoder.releaseOutputBuffer(outIndex, true);
                    if (!H264DecoderThread.this.audioPlayFlag) {
                        H264DecoderThread.this.audioPlayFlag = true;
                        GlobalInfo.videoCacheNum = this.frameSize;
                        H264DecoderThread.this.videoShowtime = System.currentTimeMillis();
                        AppLog.d(H264DecoderThread.TAG, "ok show image!.....................startTime= " + (System.currentTimeMillis() - this.startTime) + " frameSize=" + this.frameSize + " curVideoPts=" + H264DecoderThread.this.curVideoPts);
                    }
                    return true;
            }
        }

        public void requestExitAndWait() {
            AppLog.e(H264DecoderThread.TAG, "H264Decoder requestExitAndWait");
            this.done = true;
            try {
                join();
            } catch (InterruptedException e) {
            }
        }
    }

    public H264DecoderThread(MyCamera mCamera, SurfaceHolder holder, MPreview mPreview, int previewLaunchMode, VideoFramePtsChangedListener videoPbUpdateBarLitener) {
        this.surfaceHolder = holder;
        this.mPreview = mPreview;
        this.previewLaunchMode = previewLaunchMode;
        this.previewStreamControl = mCamera.getpreviewStreamClient();
        this.videoPbControl = mCamera.getVideoPlaybackClint();
        this.videoPbUpdateBarLitener = videoPbUpdateBarLitener;
        holder.setFormat(1);
    }

    public void start(boolean enableAudio, boolean enableVideo) {
        AppLog.i(TAG, "start");
        setFormat();
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
        this.audioPlayFlag = false;
    }

    private void setFormat() {
        ICatchVideoFormat videoFormat = null;
        try {
            AppLog.i(TAG, "start videoFormat");
            if (this.previewLaunchMode == 2) {
                videoFormat = this.previewStreamControl.getVideoFormat();
            } else {
                videoFormat = this.videoPbControl.getVideoFormat();
            }
            AppLog.i(TAG, "end videoFormat");
        } catch (IchSocketException e1) {
            AppLog.e(TAG, "h264 IchSocketException");
            e1.printStackTrace();
        } catch (IchCameraModeException e12) {
            AppLog.e(TAG, "h264 IchCameraModeException");
            e12.printStackTrace();
        } catch (IchInvalidSessionException e13) {
            AppLog.e(TAG, "h264 IchInvalidSessionException");
            e13.printStackTrace();
        } catch (IchStreamNotRunningException e14) {
            AppLog.e(TAG, "h264 IchStreamNotRunningException");
            e14.printStackTrace();
        }
        AppLog.i(TAG, "create  MediaFormat");
        MediaFormat format = MediaFormat.createVideoFormat(videoFormat.getMineType(), videoFormat.getVideoW(), videoFormat.getVideoH());
        if (this.previewLaunchMode == 2) {
            format.setByteBuffer("csd-0", ByteBuffer.wrap(videoFormat.getCsd_0(), 0, videoFormat.getCsd_0_size()));
            format.setByteBuffer("csd-1", ByteBuffer.wrap(videoFormat.getCsd_1(), 0, videoFormat.getCsd_0_size()));
            format.setInteger("durationUs", videoFormat.getDurationUs());
            format.setInteger("max-input-size", videoFormat.getMaxInputSize());
        }
        String ret = videoFormat.getMineType();
        Log.i(TAG, "h264 videoFormat.getMineType()=" + ret);
        this.decoder = null;
        try {
            this.decoder = MediaCodec.createDecoderByType(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppLog.d(TAG, "MediaFormat format=" + format);
        AppLog.d(TAG, "MediaFormat surfaceHolder.getSurface()=" + this.surfaceHolder.getSurface());
        this.decoder.configure(format, this.surfaceHolder.getSurface(), null, 0);
        this.decoder.start();
    }
}
