package com.icatch.ismartdv2016.BaseItems;

import android.content.Context;
import android.content.res.Resources;
import com.icatch.ismartdv2016.Beans.ItemInfo;
import com.icatch.ismartdv2016.CustomException.NullPointerException;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Hash.PropertyHashMapDynamic;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.PropertyId.PropertyId;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class PropertyTypeInteger {
    private static final String TAG = "PropertyTypeInteger";
    private Context context;
    private HashMap<Integer, ItemInfo> hashMap;
    private int propertyId;
    private Resources res;
    private List<Integer> valueListInt;
    private String[] valueListString;

    public PropertyTypeInteger(HashMap<Integer, ItemInfo> hashMap, int propertyId, Context context) {
        this.hashMap = hashMap;
        this.propertyId = propertyId;
        this.context = context;
        initItem();
    }

    public PropertyTypeInteger(int propertyId, Context context) {
        this.propertyId = propertyId;
        this.context = context;
        initItem();
    }

    public void initItem() {
        if (this.hashMap == null) {
            this.hashMap = PropertyHashMapDynamic.getInstance().getDynamicHashInt(this.propertyId);
        }
        this.res = this.context.getResources();
        switch (this.propertyId) {
            case ICatchCameraProperty.ICH_CAP_WHITE_BALANCE /*20485*/:
                this.valueListInt = CameraProperties.getInstance().getSupportedWhiteBalances();
                break;
            case ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY /*20498*/:
                this.valueListInt = CameraProperties.getInstance().getSupportedCaptureDelays();
                break;
            case ICatchCameraProperty.ICH_CAP_BURST_NUMBER /*20504*/:
                this.valueListInt = CameraProperties.getInstance().getsupportedBurstNums();
                break;
            case ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY /*54790*/:
                this.valueListInt = CameraProperties.getInstance().getSupportedLightFrequencys();
                break;
            case ICatchCameraProperty.ICH_CAP_DATE_STAMP /*54791*/:
                this.valueListInt = CameraProperties.getInstance().getsupportedDateStamps();
                break;
            case ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN /*54804*/:
                this.valueListInt = new ArrayList();
                this.valueListInt.add(Integer.valueOf(0));
                this.valueListInt.add(Integer.valueOf(1));
                break;
            case ICatchCameraProperty.ICH_CAP_SLOW_MOTION /*54805*/:
                this.valueListInt = new ArrayList();
                this.valueListInt.add(Integer.valueOf(SlowMotion.SLOW_MOTION_OFF));
                this.valueListInt.add(Integer.valueOf(SlowMotion.SLOW_MOTION_ON));
                break;
            case PropertyId.TIMELAPSE_MODE /*60928*/:
                this.valueListInt = new ArrayList();
                this.valueListInt.add(Integer.valueOf(0));
                this.valueListInt.add(Integer.valueOf(1));
                break;
            default:
                this.valueListInt = CameraProperties.getInstance().getSupportedPropertyValues(this.propertyId);
                break;
        }
        this.valueListString = new String[this.valueListInt.size()];
        if (this.valueListInt != null) {
            for (int ii = 0; ii < this.valueListInt.size(); ii++) {
                if (this.propertyId == ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY) {
                    this.valueListString[ii] = ((ItemInfo) this.hashMap.get(this.valueListInt.get(ii))).uiStringInSettingString;
                } else {
                    this.valueListString[ii] = this.res.getString(((ItemInfo) this.hashMap.get(this.valueListInt.get(ii))).uiStringInSetting);
                }
            }
        }
    }

    public int getCurrentValue() {
        switch (this.propertyId) {
            case ICatchCameraProperty.ICH_CAP_WHITE_BALANCE /*20485*/:
                return CameraProperties.getInstance().getCurrentWhiteBalance();
            case ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY /*20498*/:
                return CameraProperties.getInstance().getCurrentCaptureDelay();
            case ICatchCameraProperty.ICH_CAP_BURST_NUMBER /*20504*/:
                return CameraProperties.getInstance().getCurrentBurstNum();
            case ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY /*54790*/:
                return CameraProperties.getInstance().getCurrentLightFrequency();
            case ICatchCameraProperty.ICH_CAP_DATE_STAMP /*54791*/:
                return CameraProperties.getInstance().getCurrentDateStamp();
            case ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN /*54804*/:
                return CameraProperties.getInstance().getCurrentUpsideDown();
            case ICatchCameraProperty.ICH_CAP_SLOW_MOTION /*54805*/:
                return CameraProperties.getInstance().getCurrentSlowMotion();
            case PropertyId.TIMELAPSE_MODE /*60928*/:
                return GlobalInfo.getInstance().getCurrentCamera().timeLapsePreviewMode;
            default:
                return CameraProperties.getInstance().getCurrentPropertyValue(this.propertyId);
        }
    }

    public String getCurrentUiStringInSetting() {
        if (((ItemInfo) this.hashMap.get(Integer.valueOf(getCurrentValue()))) == null) {
            return "Unknown";
        }
        return this.res.getString(((ItemInfo) this.hashMap.get(Integer.valueOf(getCurrentValue()))).uiStringInSetting);
    }

    public String getCurrentUiStringInPreview() {
        ItemInfo itemInfo = (ItemInfo) this.hashMap.get(Integer.valueOf(getCurrentValue()));
        if (itemInfo == null) {
            return "Unknown";
        }
        return itemInfo.uiStringInPreview;
    }

    public String getCurrentUiStringInSetting(int position) {
        return this.valueListString[position];
    }

    public int getCurrentIcon() throws NullPointerException {
        ItemInfo itemInfo = (ItemInfo) this.hashMap.get(Integer.valueOf(getCurrentValue()));
        AppLog.d(TAG, "itemInfo=" + itemInfo);
        if (itemInfo != null) {
            return itemInfo.iconID;
        }
        throw new NullPointerException(TAG, "getCurrentIcon itemInfo is null", BuildConfig.FLAVOR);
    }

    public String[] getValueList() {
        return this.valueListString;
    }

    public Boolean setValue(int value) {
        boolean retValue;
        switch (this.propertyId) {
            case ICatchCameraProperty.ICH_CAP_WHITE_BALANCE /*20485*/:
                retValue = CameraProperties.getInstance().setWhiteBalance(value);
                break;
            case ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY /*20498*/:
                retValue = CameraProperties.getInstance().setCaptureDelay(value);
                break;
            case ICatchCameraProperty.ICH_CAP_BURST_NUMBER /*20504*/:
                retValue = CameraProperties.getInstance().setCurrentBurst(value);
                break;
            case ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY /*54790*/:
                retValue = CameraProperties.getInstance().setLightFrequency(value);
                break;
            case ICatchCameraProperty.ICH_CAP_DATE_STAMP /*54791*/:
                retValue = CameraProperties.getInstance().setDateStamp(value);
                break;
            default:
                retValue = CameraProperties.getInstance().setPropertyValue(this.propertyId, value);
                break;
        }
        return Boolean.valueOf(retValue);
    }

    public Boolean setValueByPosition(int position) {
        boolean retValue;
        switch (this.propertyId) {
            case ICatchCameraProperty.ICH_CAP_WHITE_BALANCE /*20485*/:
                retValue = CameraProperties.getInstance().setWhiteBalance(((Integer) this.valueListInt.get(position)).intValue());
                break;
            case ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY /*20498*/:
                retValue = CameraProperties.getInstance().setCaptureDelay(((Integer) this.valueListInt.get(position)).intValue());
                break;
            case ICatchCameraProperty.ICH_CAP_BURST_NUMBER /*20504*/:
                retValue = CameraProperties.getInstance().setCurrentBurst(((Integer) this.valueListInt.get(position)).intValue());
                break;
            case ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY /*54790*/:
                retValue = CameraProperties.getInstance().setLightFrequency(((Integer) this.valueListInt.get(position)).intValue());
                break;
            case ICatchCameraProperty.ICH_CAP_DATE_STAMP /*54791*/:
                retValue = CameraProperties.getInstance().setDateStamp(((Integer) this.valueListInt.get(position)).intValue());
                break;
            case ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN /*54804*/:
                retValue = CameraProperties.getInstance().setUpsideDown(((Integer) this.valueListInt.get(position)).intValue());
                break;
            case ICatchCameraProperty.ICH_CAP_SLOW_MOTION /*54805*/:
                retValue = CameraProperties.getInstance().setSlowMotion(((Integer) this.valueListInt.get(position)).intValue());
                break;
            default:
                retValue = CameraProperties.getInstance().setPropertyValue(this.propertyId, ((Integer) this.valueListInt.get(position)).intValue());
                break;
        }
        return Boolean.valueOf(retValue);
    }

    public Boolean needDisplayByMode(int previewMode) {
        boolean retValue = false;
        switch (this.propertyId) {
            case ICatchCameraProperty.ICH_CAP_WHITE_BALANCE /*20485*/:
                if (!CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
                    retValue = true;
                    break;
                }
                retValue = true;
                break;
            case ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY /*20498*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE) && CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY) && previewMode == 1) {
                    retValue = true;
                    break;
                }
            case ICatchCameraProperty.ICH_CAP_BURST_NUMBER /*20504*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER) && previewMode == 1) {
                    retValue = true;
                    break;
                }
            case ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY /*54790*/:
                retValue = true;
                break;
            case ICatchCameraProperty.ICH_CAP_DATE_STAMP /*54791*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) && (previewMode == 1 || previewMode == 3)) {
                    retValue = true;
                    break;
                }
            case ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN /*54804*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN)) {
                    return Boolean.valueOf(true);
                }
                break;
            case ICatchCameraProperty.ICH_CAP_SLOW_MOTION /*54805*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_SLOW_MOTION) && (previewMode == 3 || previewMode == 4)) {
                    retValue = true;
                    break;
                }
            case PropertyId.TIMELAPSE_MODE /*60928*/:
                AppLog.i(TAG, "TIMELAPSE_MODE has this fucntion! =" + CameraProperties.getInstance().hasFuction(PropertyId.TIMELAPSE_MODE));
                if (CameraProperties.getInstance().hasFuction(PropertyId.TIMELAPSE_MODE)) {
                    AppLog.i(TAG, "TIMELAPSE_MODE has this fucntion!");
                    if (previewMode == 8 || previewMode == 6 || previewMode == 7 || previewMode == 5) {
                        retValue = true;
                        break;
                    }
                }
                break;
        }
        return Boolean.valueOf(retValue);
    }
}
