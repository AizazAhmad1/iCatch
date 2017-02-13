package com.icatch.ismartdv2016.SdkApi;

import android.util.Log;
import com.icatch.ismartdv2016.DataConvert.BurstConvert;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.R;
import com.icatch.wificam.customer.ICatchWificamControl;
import com.icatch.wificam.customer.ICatchWificamProperty;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSDCardException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.icatch.wificam.customer.type.ICatchMode;
import com.icatch.wificam.customer.type.ICatchVideoFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class CameraProperties {
    private static CameraProperties instance;
    private final String TAG = "CameraProperties";
    private ICatchWificamControl cameraAction;
    private ICatchWificamProperty cameraConfiguration;
    private List<Integer> fuction;
    private List<ICatchMode> modeList;

    public static CameraProperties getInstance() {
        if (instance == null) {
            instance = new CameraProperties();
        }
        return instance;
    }

    private CameraProperties() {
    }

    public void initCameraProperties() {
        this.cameraConfiguration = GlobalInfo.getInstance().getCurrentCamera().getCameraPropertyClint();
        this.cameraAction = GlobalInfo.getInstance().getCurrentCamera().getcameraActionClient();
    }

    public List<String> getSupportedImageSizes() {
        List<String> list = null;
        try {
            list = this.cameraConfiguration.getSupportedImageSizes();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        if (list != null) {
            AppLog.i("CameraProperties", "end getSupportedImageSizes list.size =" + list.size());
            for (String temp : list) {
                AppLog.i("CameraProperties", "end getSupportedImageSizes value=" + temp);
            }
        }
        return list;
    }

    public List<String> getSupportedVideoSizes() {
        AppLog.i("CameraProperties", "begin getSupportedVideoSizes");
        List<String> list = null;
        try {
            list = this.cameraConfiguration.getSupportedVideoSizes();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "begin getSupportedVideoSizes list=" + list);
        return list;
    }

    public List<Integer> getSupportedWhiteBalances() {
        AppLog.i("CameraProperties", "begin getSupportedWhiteBalances");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedWhiteBalances();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportedWhiteBalances list=" + list);
        return list;
    }

    public List<Integer> getSupportedCaptureDelays() {
        AppLog.i("CameraProperties", "begin getSupportedCaptureDelays");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedCaptureDelays();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportedCaptureDelays list=" + list);
        if (list != null) {
            for (Integer temp : list) {
                AppLog.i("CameraProperties", "end getSupportedCaptureDelays list value=" + temp);
            }
        }
        return list;
    }

    public List<Integer> getSupportedLightFrequencys() {
        AppLog.i("CameraProperties", "begin getSupportedLightFrequencys");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedLightFrequencies();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportedLightFrequencys list=" + list);
        if (list != null) {
            for (int ii = 0; ii < list.size(); ii++) {
                if (((Integer) list.get(ii)).intValue() == 2) {
                    list.remove(ii);
                }
            }
        }
        return list;
    }

    public boolean setImageSize(String value) {
        AppLog.i("CameraProperties", "begin setImageSize set value =" + value);
        boolean retVal = false;
        try {
            retVal = this.cameraConfiguration.setImageSize(value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setImageSize retVal=" + retVal);
        return retVal;
    }

    public boolean setVideoSize(String value) {
        AppLog.i("CameraProperties", "begin setVideoSize set value =" + value);
        boolean retVal = false;
        try {
            retVal = this.cameraConfiguration.setVideoSize(value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setVideoSize retVal=" + retVal);
        return retVal;
    }

    public boolean setWhiteBalance(int value) {
        AppLog.i("CameraProperties", "begin setWhiteBalanceset value =" + value);
        boolean retVal = false;
        if (value < 0 || value == ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            return false;
        }
        try {
            retVal = this.cameraConfiguration.setWhiteBalance(value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setWhiteBalance retVal=" + retVal);
        return retVal;
    }

    public boolean setLightFrequency(int value) {
        AppLog.i("CameraProperties", "begin setLightFrequency set value =" + value);
        boolean retVal = false;
        if (value < 0 || value == ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            return false;
        }
        try {
            retVal = this.cameraConfiguration.setLightFrequency(value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setLightFrequency retVal=" + retVal);
        return retVal;
    }

    public String getCurrentImageSize() {
        AppLog.i("CameraProperties", "begin getCurrentImageSize");
        String value = "unknown";
        try {
            value = this.cameraConfiguration.getCurrentImageSize();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentImageSize value =" + value);
        return value;
    }

    public String getCurrentVideoSize() {
        AppLog.i("CameraProperties", "begin getCurrentVideoSize");
        String value = "unknown";
        try {
            value = this.cameraConfiguration.getCurrentVideoSize();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentVideoSize value =" + value);
        return value;
    }

    public int getCurrentWhiteBalance() {
        AppLog.i("CameraProperties", "begin getCurrentWhiteBalance");
        int value = ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED;
        try {
            AppLog.i("CameraProperties", "******value=   " + value);
            value = this.cameraConfiguration.getCurrentWhiteBalance();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentWhiteBalance retvalue =" + value);
        return value;
    }

    public int getCurrentLightFrequency() {
        AppLog.i("CameraProperties", "begin getCurrentLightFrequency");
        int value = ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED;
        try {
            value = this.cameraConfiguration.getCurrentLightFrequency();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentLightFrequency value =" + value);
        return value;
    }

    public boolean setCaptureDelay(int value) {
        AppLog.i("CameraProperties", "begin setCaptureDelay set value =" + value);
        boolean retVal = false;
        try {
            AppLog.i("CameraProperties", "start setCaptureDelay ");
            retVal = this.cameraConfiguration.setCaptureDelay(value);
            AppLog.i("CameraProperties", "end setCaptureDelay ");
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCaptureDelay retVal =" + retVal);
        return retVal;
    }

    public int getCurrentCaptureDelay() {
        AppLog.i("CameraProperties", "begin getCurrentCaptureDelay");
        int retVal = 0;
        try {
            retVal = this.cameraConfiguration.getCurrentCaptureDelay();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentCaptureDelay retVal=" + retVal);
        return retVal;
    }

    public int getCurrentDateStamp() {
        AppLog.i("CameraProperties", "begin getCurrentDateStampType");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentDateStamp();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "getCurrentDateStampType retValue =" + retValue);
        return retValue;
    }

    public boolean setDateStamp(int dateStamp) {
        AppLog.i("CameraProperties", "begin setDateStampType set value = " + dateStamp);
        Boolean retValue = Boolean.valueOf(false);
        try {
            retValue = Boolean.valueOf(this.cameraConfiguration.setDateStamp(dateStamp));
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setDateStampType retValue =" + retValue);
        return retValue.booleanValue();
    }

    public List<Integer> getDateStampList() {
        AppLog.i("CameraProperties", "begin getDateStampList");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedDateStamps();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getDateStampList list.size ==" + list.size());
        return list;
    }

    public List<Integer> getSupportFuction() {
        AppLog.i("CameraProperties", "begin getSupportFuction");
        List<Integer> fuction = null;
        try {
            fuction = this.cameraConfiguration.getSupportedProperties();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportFuction fuction.size() =" + fuction.size());
        for (Integer temp : fuction) {
            AppLog.i("CameraProperties", "end getSupportFuction fuction value=" + temp);
        }
        return fuction;
    }

    public int getCurrentBurstNum() {
        AppLog.i("CameraProperties", "begin getCurrentBurstNum");
        int number = ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED;
        try {
            number = this.cameraConfiguration.getCurrentBurstNumber();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "getCurrentBurstNum num =" + number);
        return number;
    }

    public int getCurrentAppBurstNum() {
        AppLog.i("CameraProperties", "begin getCurrentAppBurstNum");
        int number = ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED;
        try {
            number = this.cameraConfiguration.getCurrentBurstNumber();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        number = BurstConvert.getInstance().getBurstConverFromFw(number);
        AppLog.i("CameraProperties", "getCurrentAppBurstNum num =" + number);
        return number;
    }

    public boolean setCurrentBurst(int burstNum) {
        AppLog.i("CameraProperties", "begin setCurrentBurst set value = " + burstNum);
        if (burstNum < 0 || burstNum == ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            return false;
        }
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setBurstNumber(burstNum);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCurrentBurst retValue =" + retValue);
        return retValue;
    }

    public int getRemainImageNum() {
        AppLog.i("CameraProperties", "begin getRemainImageNum");
        int num = -1;
        try {
            num = this.cameraAction.getFreeSpaceInImages();
            AppLog.i("CameraProperties", "return getRemainImageNum num =" + num);
        } catch (Exception e) {
            AppLog.e("CameraProperties", BuildConfig.FLAVOR + e.getClass().getSimpleName());
            e.printStackTrace();
            GlobalInfo.getInstance().showExceptionInfoDialog((int) R.string.text_get_data_exception);
        }
        AppLog.i("CameraProperties", "end getRemainImageNum num =" + num);
        return num;
    }

    public int getRecordingRemainTime() {
        AppLog.i("CameraProperties", "begin getRecordingRemainTimeInt");
        int recordingTime = -1;
        try {
            recordingTime = this.cameraAction.getRemainRecordingTime();
            AppLog.i("CameraProperties", "return recordingTime =" + recordingTime);
        } catch (Exception e) {
            AppLog.e("CameraProperties", BuildConfig.FLAVOR + e.getClass().getSimpleName());
            e.printStackTrace();
            GlobalInfo.getInstance().showExceptionInfoDialog((int) R.string.text_device_exception);
        }
        AppLog.i("CameraProperties", "end getRecordingRemainTimeInt recordingTime =" + recordingTime);
        return recordingTime;
    }

    public boolean isSDCardExist() {
        AppLog.i("CameraProperties", "begin isSDCardExist");
        Boolean isReady = Boolean.valueOf(false);
        try {
            isReady = Boolean.valueOf(this.cameraAction.isSDCardExist());
        } catch (Exception e) {
            AppLog.e("CameraProperties", e.getClass().getSimpleName());
            e.printStackTrace();
            GlobalInfo.getInstance().showExceptionInfoDialog((int) R.string.text_device_exception);
        }
        AppLog.i("CameraProperties", "end isSDCardExist isReady =" + isReady);
        return isReady.booleanValue();
    }

    public int getBatteryElectric() {
        AppLog.i("CameraProperties", "start getBatteryElectric");
        int electric = 0;
        try {
            electric = this.cameraAction.getCurrentBatteryLevel();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getBatteryElectric electric =" + electric);
        return electric;
    }

    public boolean supportVideoPlayback() {
        AppLog.i("CameraProperties", "begin hasVideoPlaybackFuction");
        boolean retValue = false;
        try {
            retValue = this.cameraAction.supportVideoPlayback();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        } catch (IchNoSDCardException e5) {
            AppLog.e("CameraProperties", "IchNoSDCardException");
            e5.printStackTrace();
        }
        AppLog.i("CameraProperties", "hasVideoPlaybackFuction retValue =" + retValue);
        return retValue;
    }

    public boolean cameraModeSupport(ICatchMode mode) {
        AppLog.i("CameraProperties", "begin cameraModeSupport  mode=" + mode);
        Boolean retValue = Boolean.valueOf(false);
        this.modeList = getSupportedModes();
        if (this.modeList == null) {
            return false;
        }
        if (this.modeList.contains(mode)) {
            retValue = Boolean.valueOf(true);
        }
        AppLog.i("CameraProperties", "end cameraModeSupport retValue =" + retValue);
        return retValue.booleanValue();
    }

    public String getCameraMacAddress() {
        AppLog.i("CameraProperties", "begin getCameraMacAddress macAddress macAddress ");
        String macAddress = this.cameraAction.getMacAddress();
        AppLog.i("CameraProperties", "end getCameraMacAddress macAddress =" + macAddress);
        return macAddress;
    }

    public boolean hasFuction(int fuc) {
        AppLog.i("CameraProperties", "begin hasFuction query fuction = " + fuc);
        if (this.fuction == null) {
            this.fuction = getSupportFuction();
        }
        Boolean retValue = Boolean.valueOf(false);
        if (this.fuction.contains(Integer.valueOf(fuc))) {
            retValue = Boolean.valueOf(true);
        }
        AppLog.i("CameraProperties", "end hasFuction retValue =" + retValue);
        return retValue.booleanValue();
    }

    public List<Integer> getsupportedDateStamps() {
        AppLog.i("CameraProperties", "begin getsupportedDateStamps");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedDateStamps();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getsupportedDateStamps list.size() =" + list.size());
        return list;
    }

    public List<Integer> getsupportedBurstNums() {
        AppLog.i("CameraProperties", "begin getsupportedBurstNums");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedBurstNumbers();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getsupportedBurstNums list.size() =" + list.size());
        return list;
    }

    public List<Integer> getSupportedFrequencies() {
        AppLog.i("CameraProperties", "begin getSupportedFrequencies");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedLightFrequencies();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportedFrequencies list.size() =" + list.size());
        return list;
    }

    public List<ICatchMode> getSupportedModes() {
        AppLog.i("CameraProperties", "begin getSupportedModes");
        List<ICatchMode> list = null;
        try {
            list = this.cameraAction.getSupportedModes();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportedModes list=" + list);
        return list;
    }

    public List<Integer> getSupportedTimeLapseDurations() {
        AppLog.i("CameraProperties", "begin getSupportedTimeLapseDurations");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedTimeLapseDurations();
        } catch (IchSocketException e) {
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            e2.printStackTrace();
        } catch (IchDevicePropException e3) {
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            e4.printStackTrace();
        }
        if (list != null) {
            for (int ii = 0; ii < list.size(); ii++) {
                AppLog.i("CameraProperties", "list.get(ii) =" + list.get(ii));
            }
        }
        AppLog.i("CameraProperties", "end getSupportedTimeLapseDurations list=" + list);
        return list;
    }

    public List<Integer> getSupportedTimeLapseIntervals() {
        AppLog.i("CameraProperties", "begin getSupportedTimeLapseIntervals");
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedTimeLapseIntervals();
        } catch (IchSocketException e) {
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            e2.printStackTrace();
        } catch (IchDevicePropException e3) {
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            e4.printStackTrace();
        }
        if (list != null) {
            for (int ii = 0; ii < list.size(); ii++) {
                AppLog.i("CameraProperties", "list.get(ii) =" + list.get(ii));
            }
        }
        AppLog.i("CameraProperties", "end getSupportedTimeLapseIntervals list=" + list);
        return list;
    }

    public boolean setTimeLapseDuration(int timeDuration) {
        AppLog.i("CameraProperties", "begin setTimeLapseDuration videoDuration =" + timeDuration);
        boolean retVal = false;
        if (timeDuration < 0 || timeDuration == ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED) {
            return false;
        }
        try {
            retVal = this.cameraConfiguration.setTimeLapseDuration(timeDuration);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setTimeLapseDuration retVal=" + retVal);
        return retVal;
    }

    public int getCurrentTimeLapseDuration() {
        AppLog.i("CameraProperties", "begin getCurrentTimeLapseDuration");
        int retVal = ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED;
        try {
            retVal = this.cameraConfiguration.getCurrentTimeLapseDuration();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentTimeLapseDuration retVal=" + retVal);
        return retVal;
    }

    public boolean setTimeLapseInterval(int timeInterval) {
        AppLog.i("CameraProperties", "begin setTimeLapseInterval videoDuration =" + timeInterval);
        boolean retVal = false;
        try {
            retVal = this.cameraConfiguration.setTimeLapseInterval(timeInterval);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setTimeLapseInterval retVal=" + retVal);
        return retVal;
    }

    public int getCurrentTimeLapseInterval() {
        AppLog.i("CameraProperties", "begin getCurrentTimeLapseInterval");
        int retVal = ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED;
        try {
            retVal = this.cameraConfiguration.getCurrentTimeLapseInterval();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentTimeLapseInterval retVal=" + retVal);
        return retVal;
    }

    public int getMaxZoomRatio() {
        AppLog.i("CameraProperties", "start getMaxZoomRatio");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getMaxZoomRatio();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getMaxZoomRatio retValue =" + retValue);
        return retValue;
    }

    public int getCurrentZoomRatio() {
        AppLog.i("CameraProperties", "start getCurrentZoomRatio");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentZoomRatio();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentZoomRatio retValue =" + retValue);
        return retValue;
    }

    public int getCurrentUpsideDown() {
        AppLog.i("CameraProperties", "start getCurrentUpsideDown");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentUpsideDown();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentUpsideDown retValue =" + retValue);
        return retValue;
    }

    public boolean setUpsideDown(int upside) {
        AppLog.i("CameraProperties", "start setUpsideDown upside = " + upside);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setUpsideDown(upside);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setUpsideDown retValue =" + retValue);
        return retValue;
    }

    public int getCurrentSlowMotion() {
        AppLog.i("CameraProperties", "start getCurrentSlowMotion");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentSlowMotion();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentSlowMotion retValue =" + retValue);
        return retValue;
    }

    public boolean setSlowMotion(int slowMotion) {
        AppLog.i("CameraProperties", "start setSlowMotion slowMotion = " + slowMotion);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setSlowMotion(slowMotion);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setSlowMotion retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraDate() {
        String tempDate = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date(System.currentTimeMillis())).replaceAll(" ", "T") + ".0";
        AppLog.i("CameraProperties", "start setCameraDate date = " + tempDate);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.CAMERA_DATE, tempDate);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCameraDate retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraEssidName(String ssidName) {
        AppLog.i("CameraProperties", "start setCameraEssidName date = " + ssidName);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.ESSID_NAME, ssidName);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCameraEssidName retValue =" + retValue);
        return retValue;
    }

    public String getCameraEssidName() {
        AppLog.i("CameraProperties", "start getCameraEssidName");
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(PropertyId.ESSID_NAME);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraEssidName retValue =" + retValue);
        return retValue;
    }

    public String getCameraEssidPassword() {
        AppLog.i("CameraProperties", "start getCameraEssidPassword");
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(PropertyId.ESSID_PASSWORD);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraEssidPassword retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraEssidPassword(String ssidPassword) {
        AppLog.i("CameraProperties", "start setStringPropertyValue date = " + ssidPassword);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.ESSID_PASSWORD, ssidPassword);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCameraSsid retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraSsid(String ssid) {
        AppLog.i("CameraProperties", "start setCameraSsid date = " + ssid);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.CAMERA_ESSID, ssid);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCameraSsid retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraName(String cameraName) {
        AppLog.i("CameraProperties", "start setStringPropertyValue cameraName = " + cameraName);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.CAMERA_NAME, cameraName);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setStringPropertyValue retValue =" + retValue);
        return retValue;
    }

    public String getCameraName() {
        AppLog.i("CameraProperties", "start getCameraName");
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(PropertyId.CAMERA_NAME);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraName retValue =" + retValue);
        return retValue;
    }

    public String getCameraName(ICatchWificamProperty cameraConfiguration1) {
        AppLog.i("CameraProperties", "start getCameraName");
        String retValue = null;
        try {
            retValue = cameraConfiguration1.getCurrentStringPropertyValue(PropertyId.CAMERA_NAME);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraName retValue =" + retValue);
        return retValue;
    }

    public String getCameraPasswordNew() {
        AppLog.i("CameraProperties", "start getCameraPassword");
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(PropertyId.CAMERA_PASSWORD_NEW);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraPassword retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraPasswordNew(String cameraNamePassword) {
        AppLog.i("CameraProperties", "start setCameraPasswordNew cameraName = " + cameraNamePassword);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.CAMERA_PASSWORD_NEW, cameraNamePassword);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCameraPasswordNew retValue =" + retValue);
        return retValue;
    }

    public String getCameraSsid() {
        AppLog.i("CameraProperties", "start getCameraSsid date = ");
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(PropertyId.CAMERA_ESSID);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraSsid retValue =" + retValue);
        return retValue;
    }

    public boolean setCameraPassword(String password) {
        AppLog.i("CameraProperties", "start setCameraSsid date = " + password);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.CAMERA_PASSWORD, password);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCameraSsid retValue =" + retValue);
        return retValue;
    }

    public String getCameraPassword() {
        AppLog.i("CameraProperties", "start getCameraPassword date = ");
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(PropertyId.CAMERA_PASSWORD);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCameraPassword retValue =" + retValue);
        return retValue;
    }

    public boolean setCaptureDelayMode(int value) {
        AppLog.i("CameraProperties", "start setCaptureDelayMode value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setPropertyValue(PropertyId.CAPTURE_DELAY_MODE, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setCaptureDelayMode retValue =" + retValue);
        return retValue;
    }

    public int getVideoRecordingTime() {
        AppLog.i("CameraProperties", "start getRecordingTime");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentPropertyValue(PropertyId.VIDEO_RECORDING_TIME);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getRecordingTime retValue =" + retValue);
        return retValue;
    }

    public boolean setServiceEssid(String value) {
        AppLog.i("CameraProperties", "start setServiceEssid value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.SERVICE_ESSID, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setServiceEssid retValue =" + retValue);
        return retValue;
    }

    public boolean setServicePassword(String value) {
        AppLog.i("CameraProperties", "start setServicePassword value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(PropertyId.SERVICE_PASSWORD, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setServicePassword retValue =" + retValue);
        return retValue;
    }

    public boolean notifyFwToShareMode(int value) {
        AppLog.i("CameraProperties", "start notifyFwToShareMode value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setPropertyValue(PropertyId.NOTIFY_FW_TO_SHARE_MODE, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end notifyFwToShareMode retValue =" + retValue);
        return retValue;
    }

    public List<Integer> getSupportedPropertyValues(int propertyId) {
        AppLog.i("CameraProperties", "begin getSupportedPropertyValues propertyId =" + propertyId);
        List<Integer> list = null;
        try {
            list = this.cameraConfiguration.getSupportedPropertyValues(propertyId);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getSupportedPropertyValues list.size() =" + list.size());
        return list;
    }

    public int getCurrentPropertyValue(int propertyId) {
        AppLog.i("CameraProperties", "start getCurrentPropertyValue propertyId = " + propertyId);
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentPropertyValue(propertyId);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentPropertyValue retValue =" + retValue);
        return retValue;
    }

    public String getCurrentStringPropertyValue(int propertyId) {
        AppLog.i("CameraProperties", "start getCurrentStringPropertyValue propertyId = " + propertyId);
        String retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStringPropertyValue(propertyId);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getCurrentStringPropertyValue retValue =" + retValue);
        return retValue;
    }

    public boolean setPropertyValue(int propertyId, int value) {
        AppLog.i("CameraProperties", "start setPropertyValue value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setPropertyValue(propertyId, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setPropertyValue retValue =" + retValue);
        return retValue;
    }

    public boolean setStringPropertyValue(int propertyId, String value) {
        AppLog.i("CameraProperties", "start setStringPropertyValue value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStringPropertyValue(propertyId, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setStringPropertyValue retValue =" + retValue);
        return retValue;
    }

    public int getVideoSizeFlow() {
        AppLog.i("CameraProperties", "start getVideoSizeFlow");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getCurrentPropertyValue(PropertyId.VIDEO_SIZE_FLOW);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getVideoSizeFlow retValue =" + retValue);
        return retValue;
    }

    public boolean notifyCameraConnectChnage(int value) {
        AppLog.i("CameraProperties", "start notifyCameraConnectChnage value = " + value);
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setPropertyValue(PropertyId.CAMERA_CONNECT_CHANGE, value);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end notifyCameraConnectChnage retValue =" + retValue);
        return retValue;
    }

    public List<ICatchVideoFormat> getResolutionList() {
        AppLog.i("CameraProperties", "start getResolution");
        List<ICatchVideoFormat> retList = null;
        try {
            retList = this.cameraConfiguration.getSupportedStreamingInfos();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getResolution retList.size() =" + retList.size());
        return retList;
    }

    public String getBestResolution() {
        AppLog.i("CameraProperties", "start getBestResolution");
        String bestResolution = null;
        List<ICatchVideoFormat> tempList = getResolutionList(this.cameraConfiguration);
        if (tempList == null || tempList.size() == 0) {
            return null;
        }
        int ii;
        int tempHeigth;
        Log.d("1111", "getResolutionList() tempList.size() = " + tempList.size());
        int tempWidth = 0;
        for (ii = 0; ii < tempList.size(); ii++) {
            ICatchVideoFormat temp = (ICatchVideoFormat) tempList.get(ii);
            if (temp.getCodec() == 41) {
                if (bestResolution == null) {
                    bestResolution = "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                }
                if (temp.getVideoW() == 640 && temp.getVideoH() == 360) {
                    return "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                }
                if (temp.getVideoW() == 640 && temp.getVideoH() == 480) {
                    if (tempWidth != 640) {
                        bestResolution = "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                        tempWidth = 640;
                    }
                } else if (temp.getVideoW() == 720) {
                    if (tempWidth != 640) {
                        if (temp.getVideoW() * 9 == temp.getVideoH() * 16) {
                            bestResolution = "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                            tempWidth = 720;
                            tempHeigth = temp.getVideoH();
                        } else if (temp.getVideoW() * 3 == temp.getVideoH() * 4) {
                            if (tempWidth != 720) {
                                bestResolution = "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                            }
                            tempWidth = 720;
                            tempHeigth = temp.getVideoH();
                        }
                    }
                } else if (temp.getVideoW() < tempWidth) {
                    if (temp.getVideoW() * 9 == temp.getVideoH() * 16) {
                        bestResolution = "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                        tempWidth = temp.getVideoW();
                        tempHeigth = temp.getVideoH();
                    } else if (temp.getVideoW() * 3 == temp.getVideoH() * 4) {
                        if (tempWidth != temp.getVideoW()) {
                            bestResolution = "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                        }
                        tempWidth = temp.getVideoW();
                        tempHeigth = temp.getVideoH();
                    }
                }
            }
        }
        if (bestResolution != null) {
            return bestResolution;
        }
        for (ii = 0; ii < tempList.size(); ii++) {
            temp = (ICatchVideoFormat) tempList.get(ii);
            if (temp.getCodec() == 64) {
                if (bestResolution == null) {
                    bestResolution = "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                }
                if (temp.getVideoW() == 640 && temp.getVideoH() == 360) {
                    return "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                }
                if (temp.getVideoW() == 640 && temp.getVideoH() == 480) {
                    if (tempWidth != 640) {
                        bestResolution = "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                        tempWidth = 640;
                    }
                } else if (temp.getVideoW() == 720) {
                    if (tempWidth != 640) {
                        if (temp.getVideoW() * 9 == temp.getVideoH() * 16) {
                            bestResolution = "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                            tempWidth = 720;
                            tempHeigth = temp.getVideoH();
                        } else if (temp.getVideoW() * 3 == temp.getVideoH() * 4) {
                            if (tempWidth != 720) {
                                bestResolution = "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                            }
                            tempWidth = 720;
                            tempHeigth = temp.getVideoH();
                        }
                    }
                } else if (temp.getVideoW() < tempWidth) {
                    if (temp.getVideoW() * 9 == temp.getVideoH() * 16) {
                        bestResolution = "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                        tempWidth = temp.getVideoW();
                        tempHeigth = temp.getVideoH();
                    } else if (temp.getVideoW() * 3 == temp.getVideoH() * 4) {
                        if (tempWidth != temp.getVideoW()) {
                            bestResolution = "MJPG?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
                        }
                        tempWidth = temp.getVideoW();
                        tempHeigth = temp.getVideoH();
                    }
                }
            }
        }
        AppLog.i("CameraProperties", "end getBestResolution");
        return bestResolution;
    }

    public List<ICatchVideoFormat> getResolutionList(ICatchWificamProperty cameraConfiguration) {
        AppLog.i("CameraProperties", "start getResolutionList");
        List<ICatchVideoFormat> retList = null;
        try {
            retList = cameraConfiguration.getSupportedStreamingInfos();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        for (int ii = 0; ii < retList.size(); ii++) {
            Log.d("1111", " retList.get(ii)==" + retList.get(ii));
        }
        AppLog.i("CameraProperties", "end getResolutionList retList.size() =" + retList.size());
        return retList;
    }

    public String getAppDefaultResolution() {
        AppLog.i("CameraProperties", "start getAppDefaultResolution");
        List<ICatchVideoFormat> tempList = getResolutionList(this.cameraConfiguration);
        if (tempList == null || tempList.size() == 0) {
            return null;
        }
        Log.d("1111", "getResolutionList() tempList.size() = " + tempList.size());
        for (int ii = 0; ii < tempList.size(); ii++) {
            ICatchVideoFormat temp = (ICatchVideoFormat) tempList.get(ii);
            if (temp.getCodec() == 41 && temp.getVideoW() == AppMessage.VIDEO_PBACTIVITY && temp.getVideoH() == 720 && temp.getBitrate() == 500000) {
                return "H264?W=" + temp.getVideoW() + "&H=" + temp.getVideoH() + "&BR=" + temp.getBitrate() + "&";
            }
        }
        AppLog.i("CameraProperties", "end getAppDefaultResolution");
        return null;
    }

    public String getFWDefaultResolution() {
        AppLog.i("CameraProperties", "start getFWDefaultResolution");
        String resolution = null;
        ICatchVideoFormat retValue = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStreamingInfo();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        if (retValue != null) {
            if (retValue.getCodec() == 41) {
                resolution = "H264?W=" + retValue.getVideoW() + "&H=" + retValue.getVideoH() + "&BR=" + retValue.getBitrate() + "&";
            } else if (retValue.getCodec() == 64) {
                resolution = "MJPG?W=" + retValue.getVideoW() + "&H=" + retValue.getVideoH() + "&BR=" + retValue.getBitrate() + "&";
            }
        }
        AppLog.i("CameraProperties", "end getFWDefaultResolution");
        return resolution;
    }

    public boolean setStreamingInfo(ICatchVideoFormat iCatchVideoFormat) {
        AppLog.i("CameraProperties", "start setStreamingInfo");
        boolean retValue = false;
        try {
            retValue = this.cameraConfiguration.setStreamingInfo(iCatchVideoFormat);
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end setStreamingInfo");
        return retValue;
    }

    public String getCurrentStreamInfo() {
        AppLog.i("CameraProperties", "start getCurrentStreamInfo cameraConfiguration=" + this.cameraConfiguration);
        ICatchVideoFormat retValue = null;
        String bestResolution = null;
        try {
            retValue = this.cameraConfiguration.getCurrentStreamingInfo();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        if (retValue == null) {
            AppLog.i("CameraProperties", "end getCurrentStreamInfo retValue = " + retValue);
            return null;
        }
        if (hasFuction(55214)) {
            if (retValue.getCodec() == 41) {
                bestResolution = "H264?W=" + retValue.getVideoW() + "&H=" + retValue.getVideoH() + "&BR=" + retValue.getBitrate() + "&FPS=" + retValue.getFps() + "&";
            } else if (retValue.getCodec() == 64) {
                bestResolution = "MJPG?W=" + retValue.getVideoW() + "&H=" + retValue.getVideoH() + "&BR=" + retValue.getBitrate() + "&FPS=" + retValue.getFps() + "&";
            }
        } else if (retValue.getCodec() == 41) {
            bestResolution = "H264?W=" + retValue.getVideoW() + "&H=" + retValue.getVideoH() + "&BR=" + retValue.getBitrate();
        } else if (retValue.getCodec() == 64) {
            bestResolution = "MJPG?W=" + retValue.getVideoW() + "&H=" + retValue.getVideoH() + "&BR=" + retValue.getBitrate();
        }
        AppLog.i("CameraProperties", "end getCurrentStreamInfo bestResolution =" + bestResolution);
        return bestResolution;
    }

    public int getPreviewCacheTime() {
        AppLog.i("CameraProperties", "start getPreviewCacheTime");
        int retValue = 0;
        try {
            retValue = this.cameraConfiguration.getPreviewCacheTime();
        } catch (IchSocketException e) {
            AppLog.e("CameraProperties", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("CameraProperties", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("CameraProperties", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDevicePropException e4) {
            AppLog.e("CameraProperties", "IchDevicePropException");
            e4.printStackTrace();
        }
        AppLog.i("CameraProperties", "end getPreviewCacheTime retValue =" + retValue);
        return retValue;
    }
}
