package com.icatch.ismartdv2016.SdkApi;

import com.icatch.ismartdv2016.BaseItems.Tristate;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchStreamNotSupportException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchAudioFormat;
import com.icatch.wificam.customer.type.ICatchCustomerStreamParam;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchH264StreamParam;
import com.icatch.wificam.customer.type.ICatchMJPGStreamParam;
import com.icatch.wificam.customer.type.ICatchPreviewMode;

public class PreviewStream {
    private static PreviewStream instance;
    private final String tag = "PreviewStream";

    public static PreviewStream getInstance() {
        if (instance == null) {
            instance = new PreviewStream();
        }
        return instance;
    }

    public boolean stopMediaStream(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin stopMediaStream");
        boolean retValue = false;
        try {
            retValue = previewStreamControl.stop();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("PreviewStream", "end stopMediaStream =" + retValue);
        return retValue;
    }

    public boolean supportAudio(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin supportAudio");
        boolean retValue = false;
        try {
            retValue = previewStreamControl.containsAudioStream();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end supportAudio retValue =" + retValue);
        return retValue;
    }

    public boolean getNextVideoFrame(ICatchFrameBuffer buffer, ICatchWificamPreview previewStreamControl) {
        boolean retValue = false;
        try {
            retValue = previewStreamControl.getNextVideoFrame(buffer);
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchBufferTooSmallException e2) {
            AppLog.e("PreviewStream", "IchBufferTooSmallException");
            e2.printStackTrace();
        } catch (IchCameraModeException e3) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e4.printStackTrace();
        } catch (IchTryAgainException e5) {
            AppLog.e("PreviewStream", "IchTryAgainException");
            e5.printStackTrace();
        } catch (IchStreamNotRunningException e6) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e6.printStackTrace();
        } catch (IchInvalidArgumentException e7) {
            AppLog.e("PreviewStream", "IchInvalidArgumentException");
            e7.printStackTrace();
        } catch (IchVideoStreamClosedException e8) {
            AppLog.e("PreviewStream", "IchVideoStreamClosedException");
            e8.printStackTrace();
        }
        return retValue;
    }

    public boolean getNextAudioFrame(ICatchWificamPreview previewStreamControl, ICatchFrameBuffer icatchBuffer) {
        boolean retValue = false;
        try {
            retValue = previewStreamControl.getNextAudioFrame(icatchBuffer);
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchBufferTooSmallException e2) {
            AppLog.e("PreviewStream", "IchBufferTooSmallException");
            e2.printStackTrace();
        } catch (IchCameraModeException e3) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e4.printStackTrace();
        } catch (IchTryAgainException e5) {
            AppLog.e("PreviewStream", "IchTryAgainException");
            e5.printStackTrace();
        } catch (IchStreamNotRunningException e6) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e6.printStackTrace();
        } catch (IchInvalidArgumentException e7) {
            AppLog.e("PreviewStream", "IchInvalidArgumentException");
            e7.printStackTrace();
        } catch (IchAudioStreamClosedException e8) {
            AppLog.e("PreviewStream", "IchAudioStreamClosedException");
            e8.printStackTrace();
        }
        return retValue;
    }

    public Tristate startMediaStream(ICatchWificamPreview previewStreamControl, ICatchMJPGStreamParam param, ICatchPreviewMode previewMode, boolean disableAudio) {
        AppLog.i("PreviewStream", "begin startMediaStream disableAudio=" + disableAudio);
        Tristate retValue = Tristate.FALSE;
        try {
            retValue = previewStreamControl.start(param, previewMode, disableAudio) ? Tristate.NORMAL : Tristate.FALSE;
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchInvalidArgumentException e4) {
            AppLog.e("PreviewStream", "IchInvalidArgumentException");
            e4.printStackTrace();
        } catch (IchStreamNotSupportException e5) {
            e5.printStackTrace();
            AppLog.e("PreviewStream", "IchStreamNotSupportException");
            retValue = Tristate.ABNORMAL;
        }
        AppLog.i("PreviewStream", "param = " + param);
        AppLog.i("PreviewStream", "previewMode = " + previewMode);
        AppLog.i("PreviewStream", "end startMediaStream retValue =" + retValue);
        return retValue;
    }

    public Tristate startMediaStream(ICatchWificamPreview previewStreamControl, ICatchCustomerStreamParam param, ICatchPreviewMode previewMode, boolean disableAudio) {
        AppLog.i("PreviewStream", "begin startMediaStream disableAudio=" + disableAudio);
        Tristate retValue = Tristate.FALSE;
        try {
            retValue = previewStreamControl.start(param, previewMode, disableAudio) ? Tristate.NORMAL : Tristate.FALSE;
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchInvalidArgumentException e4) {
            AppLog.e("PreviewStream", "IchInvalidArgumentException");
            e4.printStackTrace();
        } catch (IchStreamNotSupportException e5) {
            e5.printStackTrace();
            AppLog.e("PreviewStream", "IchInvalidArgumentException");
            retValue = Tristate.ABNORMAL;
        }
        AppLog.i("PreviewStream", "end startMediaStream retValue =" + retValue);
        return retValue;
    }

    public Tristate startMediaStream(ICatchWificamPreview previewStreamControl, ICatchH264StreamParam param, ICatchPreviewMode previewMode, boolean disableAudio) {
        AppLog.i("PreviewStream", "begin startMediaStream");
        boolean temp = false;
        Tristate retValue = Tristate.FALSE;
        try {
            temp = previewStreamControl.start(param, previewMode, disableAudio);
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchInvalidArgumentException e4) {
            AppLog.e("PreviewStream", "IchInvalidArgumentException");
            e4.printStackTrace();
        } catch (IchStreamNotSupportException e5) {
            e5.printStackTrace();
        }
        if (temp) {
            retValue = Tristate.NORMAL;
        } else {
            retValue = Tristate.FALSE;
        }
        AppLog.i("PreviewStream", "end startMediaStream retValue =" + retValue);
        return retValue;
    }

    public boolean changePreviewMode(ICatchWificamPreview previewStreamControl, ICatchPreviewMode previewMode) {
        AppLog.i("PreviewStream", "begin changePreviewMode previewMode=" + previewMode);
        boolean retValue = false;
        try {
            retValue = previewStreamControl.changePreviewMode(previewMode);
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotSupportException e4) {
            AppLog.e("PreviewStream", "IchStreamNotSupportException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end changePreviewMode ret=" + retValue);
        return retValue;
    }

    public int getVideoWidth(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin getVideoWidth");
        int retValue = 0;
        try {
            retValue = previewStreamControl.getVideoFormat().getVideoW();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end getVideoWidth retValue =" + retValue);
        return retValue;
    }

    public int getVideoHeigth(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin getVideoHeigth");
        int retValue = 0;
        try {
            retValue = previewStreamControl.getVideoFormat().getVideoH();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end getVideoHeigth retValue =" + retValue);
        return retValue;
    }

    public int getCodec(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin getCodec previewStreamControl =" + previewStreamControl);
        int retValue = 0;
        try {
            retValue = previewStreamControl.getVideoFormat().getCodec();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end getCodec retValue =" + retValue);
        return retValue;
    }

    public int getBitrate(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin getBitrate");
        int retValue = 0;
        try {
            retValue = previewStreamControl.getVideoFormat().getBitrate();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end getBitrate retValue =" + retValue);
        return retValue;
    }

    public ICatchAudioFormat getAudioFormat(ICatchWificamPreview previewStreamControl) {
        AppLog.i("PreviewStream", "begin getAudioFormat");
        ICatchAudioFormat retValue = null;
        try {
            retValue = previewStreamControl.getAudioFormat();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotRunningException e4) {
            AppLog.e("PreviewStream", "IchStreamNotRunningException");
            e4.printStackTrace();
        }
        AppLog.i("PreviewStream", "end getAudioFormat retValue =" + retValue);
        return retValue;
    }

    public boolean enableAudio(ICatchWificamPreview previewStreamControl) {
        AppLog.d("PreviewStream", "start enableAudio");
        boolean value = false;
        try {
            value = previewStreamControl.enableAudio();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotSupportException e4) {
            AppLog.e("PreviewStream", "IchStreamNotSupportException");
            e4.printStackTrace();
        }
        AppLog.d("PreviewStream", "end enableAudio value = " + value);
        return value;
    }

    public boolean disableAudio(ICatchWificamPreview previewStreamControl) {
        AppLog.d("PreviewStream", "start disableAudio");
        boolean value = false;
        try {
            value = previewStreamControl.disableAudio();
        } catch (IchSocketException e) {
            AppLog.e("PreviewStream", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("PreviewStream", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("PreviewStream", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchStreamNotSupportException e4) {
            AppLog.e("PreviewStream", "IchStreamNotSupportException");
            e4.printStackTrace();
        }
        AppLog.d("PreviewStream", "end disableAudio value = " + value);
        return value;
    }
}
