package com.icatch.ismartdv2016.Model;

import android.util.Log;
import com.icatch.ismartdv2016.Beans.SettingMenu;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraFixedInfo;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.SdkApi.CameraState;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import com.icatch.wificam.customer.type.ICatchMode;
import java.util.LinkedList;
import uk.co.senab.photoview.BuildConfig;

public class UIDisplaySource {
    public static final int CAPTURE_SETTING_MENU = 1;
    public static final int TIMELAPSE_SETTING_MENU = 3;
    public static final int VIDEO_SETTING_MENU = 2;
    private static UIDisplaySource uiDisplayResource;
    private CameraState cameraState = CameraState.getInstance();
    private LinkedList<SettingMenu> settingMenuList;

    public static UIDisplaySource getinstance() {
        if (uiDisplayResource == null) {
            uiDisplayResource = new UIDisplaySource();
        }
        return uiDisplayResource;
    }

    public static void createInstance() {
        uiDisplayResource = new UIDisplaySource();
    }

    public LinkedList<SettingMenu> getList(int type, MyCamera currCamera) {
        switch (type) {
            case CAPTURE_SETTING_MENU /*1*/:
                return getForCaptureMode(currCamera);
            case VIDEO_SETTING_MENU /*2*/:
                return getForVideoMode(currCamera);
            case TIMELAPSE_SETTING_MENU /*3*/:
                return getForTimelapseMode(currCamera);
            default:
                return null;
        }
    }

    public LinkedList<SettingMenu> getForCaptureMode(MyCamera currCamera) {
        if (this.settingMenuList == null) {
            this.settingMenuList = new LinkedList();
        } else {
            this.settingMenuList.clear();
        }
        if (this.cameraState.isSupportImageAutoDownload()) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_auto_download, BuildConfig.FLAVOR, R.string.setting_title_switch));
            this.settingMenuList.add(new SettingMenu(R.string.setting_auto_download_size_limit, BuildConfig.FLAVOR, R.string.setting_title_switch));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
            this.settingMenuList.add(new SettingMenu(R.string.title_awb, currCamera.getWhiteBalance().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_power_supply, currCamera.getElectricityFrequency().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_datestamp, currCamera.getDateStamp().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN)) {
            this.settingMenuList.add(new SettingMenu(R.string.upside, currCamera.getUpside().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(PropertyId.CAMERA_ESSID)) {
            this.settingMenuList.add(new SettingMenu(R.string.camera_wifi_configuration, BuildConfig.FLAVOR, R.string.setting_title_general));
        }
        this.settingMenuList.add(new SettingMenu(R.string.setting_format, BuildConfig.FLAVOR, R.string.setting_title_general));
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_image_size, currCamera.getImageSize().getCurrentUiStringInSetting(), R.string.setting_title_specific));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_capture_delay, currCamera.getCaptureDelay().getCurrentUiStringInPreview(), R.string.setting_title_specific));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER)) {
            this.settingMenuList.add(new SettingMenu(R.string.title_burst, currCamera.getBurst().getCurrentUiStringInSetting(), R.string.setting_title_specific));
        }
        this.settingMenuList.add(new SettingMenu(R.string.setting_app_version, com.icatch.ismartdv2016.BuildConfig.VERSION_NAME, R.string.setting_title_other));
        this.settingMenuList.add(new SettingMenu(R.string.setting_product_name, CameraFixedInfo.getInstance().getCameraName(), R.string.setting_title_other));
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_firmware_version, CameraFixedInfo.getInstance().getCameraVersion(), R.string.setting_title_other));
        }
        return this.settingMenuList;
    }

    public LinkedList<SettingMenu> getForVideoMode(MyCamera currCamera) {
        Log.d("1111", "currCamera ==" + currCamera);
        if (this.settingMenuList == null) {
            this.settingMenuList = new LinkedList();
        } else {
            this.settingMenuList.clear();
        }
        if (this.cameraState.isSupportImageAutoDownload()) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_auto_download, BuildConfig.FLAVOR, R.string.setting_title_switch));
            this.settingMenuList.add(new SettingMenu(R.string.setting_auto_download_size_limit, BuildConfig.FLAVOR, R.string.setting_title_switch));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
            this.settingMenuList.add(new SettingMenu(R.string.title_awb, currCamera.getWhiteBalance().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_power_supply, currCamera.getElectricityFrequency().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_datestamp, currCamera.getDateStamp().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN)) {
            this.settingMenuList.add(new SettingMenu(R.string.upside, currCamera.getUpside().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(PropertyId.CAMERA_ESSID)) {
            this.settingMenuList.add(new SettingMenu(R.string.camera_wifi_configuration, BuildConfig.FLAVOR, R.string.setting_title_general));
        }
        this.settingMenuList.add(new SettingMenu(R.string.setting_format, BuildConfig.FLAVOR, R.string.setting_title_general));
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_video_size, currCamera.getVideoSize().getCurrentUiStringInSetting(), R.string.setting_title_specific));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_SLOW_MOTION)) {
            this.settingMenuList.add(new SettingMenu(R.string.slowmotion, currCamera.getSlowMotion().getCurrentUiStringInSetting(), R.string.setting_title_specific));
        }
        this.settingMenuList.add(new SettingMenu(R.string.setting_app_version, com.icatch.ismartdv2016.BuildConfig.VERSION_NAME, R.string.setting_title_other));
        this.settingMenuList.add(new SettingMenu(R.string.setting_product_name, CameraFixedInfo.getInstance().getCameraName(), R.string.setting_title_other));
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_firmware_version, CameraFixedInfo.getInstance().getCameraVersion(), R.string.setting_title_other));
        }
        return this.settingMenuList;
    }

    public LinkedList<SettingMenu> getForTimelapseMode(MyCamera currCamera) {
        if (this.settingMenuList == null) {
            this.settingMenuList = new LinkedList();
        } else {
            this.settingMenuList.clear();
        }
        if (this.cameraState.isSupportImageAutoDownload()) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_auto_download, BuildConfig.FLAVOR, R.string.setting_title_switch));
            this.settingMenuList.add(new SettingMenu(R.string.setting_auto_download_size_limit, BuildConfig.FLAVOR, R.string.setting_title_switch));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
            this.settingMenuList.add(new SettingMenu(R.string.title_awb, currCamera.getWhiteBalance().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_power_supply, currCamera.getElectricityFrequency().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN)) {
            this.settingMenuList.add(new SettingMenu(R.string.upside, currCamera.getUpside().getCurrentUiStringInSetting(), R.string.setting_title_general));
        }
        if (CameraProperties.getInstance().hasFuction(PropertyId.CAMERA_ESSID)) {
            this.settingMenuList.add(new SettingMenu(R.string.camera_wifi_configuration, BuildConfig.FLAVOR, R.string.setting_title_general));
        }
        this.settingMenuList.add(new SettingMenu(R.string.setting_format, BuildConfig.FLAVOR, R.string.setting_title_general));
        if (currCamera.timeLapsePreviewMode == 0) {
            if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE)) {
                this.settingMenuList.add(new SettingMenu(R.string.setting_image_size, currCamera.getImageSize().getCurrentUiStringInSetting(), R.string.setting_title_specific));
            }
        } else if (currCamera.timeLapsePreviewMode == CAPTURE_SETTING_MENU && CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_video_size, currCamera.getVideoSize().getCurrentUiStringInSetting(), R.string.setting_title_specific));
        }
        if (CameraProperties.getInstance().cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)) {
            this.settingMenuList.add(new SettingMenu(R.string.title_timeLapse_mode, currCamera.getTimeLapseMode().getCurrentUiStringInSetting(), R.string.setting_title_specific));
            this.settingMenuList.add(new SettingMenu(R.string.setting_time_lapse_interval, currCamera.getTimeLapseInterval().getCurrentValue(), R.string.setting_title_specific));
            this.settingMenuList.add(new SettingMenu(R.string.setting_time_lapse_duration, currCamera.gettimeLapseDuration().getCurrentValue(), R.string.setting_title_specific));
        }
        this.settingMenuList.add(new SettingMenu(R.string.setting_app_version, com.icatch.ismartdv2016.BuildConfig.VERSION_NAME, R.string.setting_title_other));
        this.settingMenuList.add(new SettingMenu(R.string.setting_product_name, CameraFixedInfo.getInstance().getCameraName(), R.string.setting_title_other));
        if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
            this.settingMenuList.add(new SettingMenu(R.string.setting_firmware_version, CameraFixedInfo.getInstance().getCameraVersion(), R.string.setting_title_other));
        }
        return this.settingMenuList;
    }
}
