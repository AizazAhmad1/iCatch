package com.icatch.ismartdv2016.MyCamera;

import com.icatch.ismartdv2016.BaseItems.PropertyTypeInteger;
import com.icatch.ismartdv2016.BaseItems.PropertyTypeString;
import com.icatch.ismartdv2016.BaseItems.StreamResolution;
import com.icatch.ismartdv2016.BaseItems.TimeLapseDuration;
import com.icatch.ismartdv2016.BaseItems.TimeLapseInterval;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Hash.PropertyHashMapStatic;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Model.Implement.SDKSession;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.CameraFixedInfo;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.SdkApi.CameraState;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import com.icatch.wificam.customer.ICatchWificamAssist;
import com.icatch.wificam.customer.ICatchWificamControl;
import com.icatch.wificam.customer.ICatchWificamInfo;
import com.icatch.wificam.customer.ICatchWificamPlayback;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.ICatchWificamProperty;
import com.icatch.wificam.customer.ICatchWificamState;
import com.icatch.wificam.customer.ICatchWificamVideoPlayback;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.type.ICatchCameraProperty;

public class MyCamera {
    private PropertyTypeInteger burst;
    private ICatchWificamControl cameraAction;
    private ICatchWificamAssist cameraAssist;
    private ICatchWificamInfo cameraInfo;
    public String cameraName;
    private ICatchWificamProperty cameraProperty;
    private ICatchWificamState cameraState;
    private PropertyTypeInteger captureDelay;
    private PropertyTypeInteger dateStamp;
    private PropertyTypeInteger electricityFrequency;
    private PropertyTypeString imageSize;
    public String inputPassword;
    public String ipAddress;
    private boolean isSdCardReady;
    public boolean isStreamReady;
    private SDKSession mSDKSession;
    public int mode;
    public boolean needInputPassword;
    private ICatchWificamPlayback photoPlayback;
    private ICatchWificamPreview previewStream;
    private PropertyTypeInteger slowMotion;
    private StreamResolution streamResolution;
    private final String tag;
    private TimeLapseDuration timeLapseDuration;
    private TimeLapseInterval timeLapseInterval;
    private PropertyTypeInteger timeLapseMode;
    public int timeLapsePreviewMode;
    public String uid;
    private PropertyTypeInteger upside;
    private ICatchWificamVideoPlayback videoPlayback;
    private PropertyTypeString videoSize;
    private PropertyTypeInteger whiteBalance;

    public MyCamera(String ipAddress, String uid, String username, String password) {
        this.tag = "MyCamera";
        this.isSdCardReady = false;
        this.timeLapsePreviewMode = 1;
        this.needInputPassword = true;
        this.isStreamReady = false;
        this.mSDKSession = new SDKSession(ipAddress, uid, username, password);
    }

    public MyCamera() {
        this.tag = "MyCamera";
        this.isSdCardReady = false;
        this.timeLapsePreviewMode = 1;
        this.needInputPassword = true;
        this.isStreamReady = false;
        this.mSDKSession = new SDKSession();
    }

    public MyCamera(String cameraName) {
        this.tag = "MyCamera";
        this.isSdCardReady = false;
        this.timeLapsePreviewMode = 1;
        this.needInputPassword = true;
        this.isStreamReady = false;
        this.mSDKSession = new SDKSession();
        this.cameraName = cameraName;
    }

    public MyCamera(String ipAddress, int mode, String uid) {
        this.tag = "MyCamera";
        this.isSdCardReady = false;
        this.timeLapsePreviewMode = 1;
        this.needInputPassword = true;
        this.isStreamReady = false;
        this.mSDKSession = new SDKSession();
        this.ipAddress = ipAddress;
        this.mode = mode;
        this.uid = uid;
    }

    public Boolean initCamera() {
        boolean retValue = false;
        AppLog.i("MyCamera", "Start initCamera");
        try {
            this.photoPlayback = this.mSDKSession.getSDKSession().getPlaybackClient();
            this.cameraAction = this.mSDKSession.getSDKSession().getControlClient();
            this.previewStream = this.mSDKSession.getSDKSession().getPreviewClient();
            this.videoPlayback = this.mSDKSession.getSDKSession().getVideoPlaybackClient();
            this.cameraProperty = this.mSDKSession.getSDKSession().getPropertyClient();
            this.cameraInfo = this.mSDKSession.getSDKSession().getInfoClient();
            this.cameraState = this.mSDKSession.getSDKSession().getStateClient();
            this.cameraAssist = ICatchWificamAssist.getInstance();
            retValue = true;
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        CameraAction.getInstance().initCameraAction();
        CameraFixedInfo.getInstance().initCameraFixedInfo();
        CameraProperties.getInstance().initCameraProperties();
        CameraState.getInstance().initCameraState();
        FileOperation.getInstance().initICatchWificamPlayback();
        VideoPlayback.getInstance().initVideoPlayback();
        PropertyHashMapStatic.getInstance().initPropertyHashMap();
        initProperty();
        return Boolean.valueOf(retValue);
    }

    public Boolean initCameraForLocalPB() {
        boolean retValue = false;
        AppLog.i("MyCamera", "Start initCamera");
        try {
            this.photoPlayback = this.mSDKSession.getSDKSession().getPlaybackClient();
            this.cameraAction = this.mSDKSession.getSDKSession().getControlClient();
            this.previewStream = this.mSDKSession.getSDKSession().getPreviewClient();
            this.videoPlayback = this.mSDKSession.getSDKSession().getVideoPlaybackClient();
            this.cameraProperty = this.mSDKSession.getSDKSession().getPropertyClient();
            this.cameraInfo = this.mSDKSession.getSDKSession().getInfoClient();
            this.cameraState = this.mSDKSession.getSDKSession().getStateClient();
            this.cameraAssist = ICatchWificamAssist.getInstance();
            retValue = true;
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        CameraAction.getInstance().initCameraAction();
        CameraFixedInfo.getInstance().initCameraFixedInfo();
        CameraProperties.getInstance().initCameraProperties();
        CameraState.getInstance().initCameraState();
        FileOperation.getInstance().initICatchWificamPlayback();
        VideoPlayback.getInstance().initVideoPlayback();
        PropertyHashMapStatic.getInstance().initPropertyHashMap();
        return Boolean.valueOf(retValue);
    }

    public Boolean initCameraByClint() {
        boolean retValue = false;
        AppLog.i("MyCamera", "Start initCamera");
        try {
            this.photoPlayback = this.mSDKSession.getSDKSession().getPlaybackClient();
            this.cameraAction = this.mSDKSession.getSDKSession().getControlClient();
            this.previewStream = this.mSDKSession.getSDKSession().getPreviewClient();
            this.videoPlayback = this.mSDKSession.getSDKSession().getVideoPlaybackClient();
            this.cameraProperty = this.mSDKSession.getSDKSession().getPropertyClient();
            this.cameraInfo = this.mSDKSession.getSDKSession().getInfoClient();
            this.cameraState = this.mSDKSession.getSDKSession().getStateClient();
            this.cameraAssist = ICatchWificamAssist.getInstance();
            retValue = true;
        } catch (IchInvalidSessionException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(retValue);
    }

    private void initProperty() {
        AppLog.i("MyCamera", "Start initProperty");
        this.whiteBalance = new PropertyTypeInteger(PropertyHashMapStatic.whiteBalanceMap, ICatchCameraProperty.ICH_CAP_WHITE_BALANCE, GlobalInfo.getInstance().getAppContext());
        this.burst = new PropertyTypeInteger(PropertyHashMapStatic.burstMap, ICatchCameraProperty.ICH_CAP_BURST_NUMBER, GlobalInfo.getInstance().getAppContext());
        this.dateStamp = new PropertyTypeInteger(PropertyHashMapStatic.dateStampMap, ICatchCameraProperty.ICH_CAP_DATE_STAMP, GlobalInfo.getInstance().getAppContext());
        this.slowMotion = new PropertyTypeInteger(PropertyHashMapStatic.slowMotionMap, ICatchCameraProperty.ICH_CAP_SLOW_MOTION, GlobalInfo.getInstance().getAppContext());
        this.upside = new PropertyTypeInteger(PropertyHashMapStatic.upsideMap, ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN, GlobalInfo.getInstance().getAppContext());
        this.electricityFrequency = new PropertyTypeInteger(PropertyHashMapStatic.electricityFrequencyMap, ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY, GlobalInfo.getInstance().getAppContext());
        this.captureDelay = new PropertyTypeInteger(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY, GlobalInfo.getInstance().getAppContext());
        this.videoSize = new PropertyTypeString(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE, GlobalInfo.getInstance().getAppContext());
        this.imageSize = new PropertyTypeString(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE, GlobalInfo.getInstance().getAppContext());
        this.streamResolution = new StreamResolution();
        this.timeLapseInterval = new TimeLapseInterval();
        this.timeLapseDuration = new TimeLapseDuration();
        this.timeLapseMode = new PropertyTypeInteger(PropertyHashMapStatic.timeLapseMode, PropertyId.TIMELAPSE_MODE, GlobalInfo.getInstance().getAppContext());
        AppLog.i("MyCamera", "End initProperty");
    }

    public void setMyMode(int mode) {
        this.mode = mode;
    }

    public int getMyMode() {
        return this.mode;
    }

    public Boolean destroyCamera() {
        return Boolean.valueOf(this.mSDKSession.destroySession());
    }

    public SDKSession getSDKsession() {
        return this.mSDKSession;
    }

    public ICatchWificamPlayback getplaybackClient() {
        return this.photoPlayback;
    }

    public ICatchWificamControl getcameraActionClient() {
        return this.cameraAction;
    }

    public ICatchWificamVideoPlayback getVideoPlaybackClint() {
        return this.videoPlayback;
    }

    public ICatchWificamPreview getpreviewStreamClient() {
        return this.previewStream;
    }

    public ICatchWificamInfo getCameraInfoClint() {
        return this.cameraInfo;
    }

    public ICatchWificamProperty getCameraPropertyClint() {
        return this.cameraProperty;
    }

    public ICatchWificamState getCameraStateClint() {
        return this.cameraState;
    }

    public ICatchWificamAssist getCameraAssistClint() {
        return this.cameraAssist;
    }

    public PropertyTypeInteger getWhiteBalance() {
        return this.whiteBalance;
    }

    public PropertyTypeInteger getBurst() {
        return this.burst;
    }

    public PropertyTypeInteger getDateStamp() {
        return this.dateStamp;
    }

    public PropertyTypeInteger getCaptureDelay() {
        return this.captureDelay;
    }

    public PropertyTypeInteger getSlowMotion() {
        return this.slowMotion;
    }

    public PropertyTypeInteger getUpside() {
        return this.upside;
    }

    public PropertyTypeString getVideoSize() {
        return this.videoSize;
    }

    public PropertyTypeString getImageSize() {
        return this.imageSize;
    }

    public PropertyTypeInteger getElectricityFrequency() {
        return this.electricityFrequency;
    }

    public StreamResolution getStreamResolution() {
        return this.streamResolution;
    }

    public TimeLapseInterval getTimeLapseInterval() {
        return this.timeLapseInterval;
    }

    public TimeLapseDuration gettimeLapseDuration() {
        return this.timeLapseDuration;
    }

    public PropertyTypeInteger getTimeLapseMode() {
        return this.timeLapseMode;
    }

    public boolean isSdCardReady() {
        return this.isSdCardReady;
    }

    public void setSdCardReady(boolean isSdReady) {
        this.isSdCardReady = isSdReady;
    }
}
