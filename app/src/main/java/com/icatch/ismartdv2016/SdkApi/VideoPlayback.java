package com.icatch.ismartdv2016.SdkApi;

import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamVideoPlayback;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchPauseFailedException;
import com.icatch.wificam.customer.exception.IchPbStreamPausedException;
import com.icatch.wificam.customer.exception.IchResumeFailedException;
import com.icatch.wificam.customer.exception.IchSeekFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchVideoFormat;
import uk.co.senab.photoview.BuildConfig;

public class VideoPlayback {
    private static VideoPlayback instance;
    private final String tag = "VideoPlayback";
    private ICatchWificamVideoPlayback videoPlayback;

    public static VideoPlayback getInstance() {
        if (instance == null) {
            instance = new VideoPlayback();
        }
        return instance;
    }

    private VideoPlayback() {
    }

    public void initVideoPlayback() {
        this.videoPlayback = GlobalInfo.getInstance().getCurrentCamera().getVideoPlaybackClint();
    }

    public void initVideoPlayback(ICatchWificamVideoPlayback videoPlayback) {
        this.videoPlayback = videoPlayback;
    }

    public boolean stopPlaybackStream() {
        AppLog.i("VideoPlayback", "start stopPlaybackStream ");
        String sdkApi = "ICatchWificamVideoPlayback.stop() ";
        if (this.videoPlayback == null) {
            return true;
        }
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.stop();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("VideoPlayback", "stopPlaybackStream =" + retValue);
        return retValue;
    }

    public boolean stopPlaybackStream(ICatchWificamVideoPlayback videoPlayback) {
        AppLog.i("VideoPlayback", "start stopPlaybackStream ");
        String sdkApi = "ICatchWificamVideoPlayback.stop() ";
        if (videoPlayback == null) {
            return true;
        }
        boolean retValue = false;
        try {
            retValue = videoPlayback.stop();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("VideoPlayback", "stopPlaybackStream =" + retValue);
        return retValue;
    }

    public boolean startPlaybackStream(ICatchFile file) {
        boolean retValue = false;
        String sdkApi = "ICatchWificamVideoPlayback.play() ";
        try {
            retValue = this.videoPlayback.play(file);
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchNoSuchFileException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchNoSuchFileException");
            e4.printStackTrace();
        }
        AppLog.i("VideoPlayback", "-----------end startPlaybackStream retValue =" + retValue);
        return retValue;
    }

    public boolean startPlaybackStream(String fileName) {
        String sdkApi = "ICatchWificamVideoPlayback.play() ";
        boolean retValue = false;
        ICatchFile icathfile = new ICatchFile(33, ICatchFileType.ICH_TYPE_VIDEO, fileName, BuildConfig.FLAVOR, 0);
        AppLog.i("VideoPlayback", "begin startPlaybackStream file=" + fileName);
        try {
            retValue = this.videoPlayback.play(icathfile, false, false);
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchNoSuchFileException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchNoSuchFileException");
            e4.printStackTrace();
        }
        AppLog.i("VideoPlayback", "-----------end startPlaybackStream retValue =" + retValue);
        return retValue;
    }

    public boolean pausePlayback() {
        String sdkApi = "ICatchWificamVideoPlayback.pause() ";
        AppLog.i("VideoPlayback", "begin pausePlayback");
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.pause();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchPauseFailedException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchPauseFailedException");
            e4.printStackTrace();
        } catch (IchStreamNotRunningException e5) {
            AppLog.e("VideoPlayback", sdkApi + "IchStreamNotRunningException");
            e5.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end pausePlayback =" + retValue);
        return retValue;
    }

    public boolean resumePlayback() {
        String sdkApi = "ICatchWificamVideoPlayback.resume() ";
        AppLog.i("VideoPlayback", "begin resumePlayback");
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.resume();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchResumeFailedException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchResumeFailedException");
            e4.printStackTrace();
        } catch (IchStreamNotRunningException e5) {
            AppLog.e("VideoPlayback", sdkApi + "IchStreamNotRunningException");
            e5.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end resumePlayback retValue=" + retValue);
        return retValue;
    }

    public int getVideoDuration() {
        String sdkApi = "ICatchWificamVideoPlayback.getLength() ";
        AppLog.i("VideoPlayback", "begin getVideoDuration");
        double temp = 0.0d;
        try {
            temp = this.videoPlayback.getLength();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end getVideoDuration temp =" + temp);
        AppLog.i("VideoPlayback", "end getVideoDuration length =" + new Double(temp * 100.0d).intValue());
        return new Double(temp * 100.0d).intValue();
    }

    public boolean videoSeek(double position) {
        String sdkApi = "ICatchWificamVideoPlayback.seek() ";
        AppLog.i("VideoPlayback", "begin videoSeek position = " + position);
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.seek(position);
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchSeekFailedException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchSeekFailedException");
            e4.printStackTrace();
        } catch (IchStreamNotRunningException e5) {
            AppLog.e("VideoPlayback", sdkApi + "IchStreamNotRunningException");
            e5.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end videoSeek retValue=" + retValue);
        return retValue;
    }

    public boolean getNextVideoFrame(ICatchFrameBuffer buffer) {
        String sdkApi = "ICatchWificamVideoPlayback.getNextVideoFrame() ";
        AppLog.i("VideoPlayback", "begin getNextVideoFrame");
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.getNextVideoFrame(buffer);
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchStreamNotRunningException");
            e4.printStackTrace();
        } catch (IchBufferTooSmallException e5) {
            AppLog.e("VideoPlayback", sdkApi + "IchBufferTooSmallException");
            e5.printStackTrace();
        } catch (IchTryAgainException e6) {
            AppLog.e("VideoPlayback", sdkApi + "IchTryAgainException");
            e6.printStackTrace();
        } catch (IchInvalidArgumentException e7) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidArgumentException");
            e7.printStackTrace();
        } catch (IchVideoStreamClosedException e8) {
            AppLog.e("VideoPlayback", sdkApi + "IchVideoStreamClosedException");
            e8.printStackTrace();
        } catch (IchPbStreamPausedException e9) {
            AppLog.e("VideoPlayback", sdkApi + "IchPbStreamPausedException");
            e9.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end getNextVideoFrame  retValue= " + retValue);
        return retValue;
    }

    public boolean getNextAudioFrame(ICatchFrameBuffer buffer) {
        String sdkApi = "ICatchWificamVideoPlayback.getNextAudioFrame() ";
        AppLog.i("VideoPlayback", "begin getNextAudioFrame");
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.getNextAudioFrame(buffer);
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", sdkApi + "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", sdkApi + "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("VideoPlayback", sdkApi + "IchStreamNotRunningException");
            e4.printStackTrace();
        } catch (IchBufferTooSmallException e5) {
            AppLog.e("VideoPlayback", sdkApi + "IchBufferTooSmallException");
            e5.printStackTrace();
        } catch (IchTryAgainException e6) {
            AppLog.e("VideoPlayback", sdkApi + "IchTryAgainException");
            e6.printStackTrace();
        } catch (IchInvalidArgumentException e7) {
            AppLog.e("VideoPlayback", sdkApi + "IchInvalidArgumentException");
            e7.printStackTrace();
        } catch (IchAudioStreamClosedException e8) {
            AppLog.e("VideoPlayback", sdkApi + "IchAudioStreamClosedException");
            e8.printStackTrace();
        } catch (IchPbStreamPausedException e9) {
            AppLog.e("VideoPlayback", sdkApi + "IchPbStreamPausedException");
            e9.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end getNextAudioFrame  retValue= " + retValue);
        return retValue;
    }

    public boolean containsAudioStream() {
        AppLog.i("VideoPlayback", "begin containsAudioStream");
        boolean retValue = false;
        try {
            retValue = this.videoPlayback.containsAudioStream();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("VideoPlayback", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end containsAudioStream  retValue= " + retValue);
        return retValue;
    }

    public ICatchAudioFormat getAudioFormat() {
        AppLog.i("VideoPlayback", "begin getAudioFormat");
        ICatchAudioFormat retValue = null;
        try {
            retValue = this.videoPlayback.getAudioFormat();
        } catch (IchSocketException e) {
            AppLog.e("VideoPlayback", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("VideoPlayback", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("VideoPlayback", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("VideoPlayback", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end getAudioFormat  retValue= " + retValue);
        return retValue;
    }

    public ICatchVideoFormat getVideoFormat() {
        AppLog.i("VideoPlayback", "begin getVideoFormat");
        ICatchVideoFormat retValue = null;
        try {
            retValue = this.videoPlayback.getVideoFormat();
        } catch (IchSocketException e) {
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            e4.printStackTrace();
        }
        AppLog.i("VideoPlayback", "end getVideoFormat  retValue= " + retValue);
        return retValue;
    }
}
