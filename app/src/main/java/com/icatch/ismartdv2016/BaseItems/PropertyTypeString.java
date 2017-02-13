package com.icatch.ismartdv2016.BaseItems;

import android.content.Context;
import com.icatch.ismartdv2016.Beans.ItemInfo;
import com.icatch.ismartdv2016.Hash.PropertyHashMapDynamic;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PropertyTypeString {
    private HashMap<String, ItemInfo> hashMap;
    private int propertyId;
    private String[] valueArrayString;
    private List<String> valueListString;
    private List<String> valueListStringUI;

    public PropertyTypeString(int propertyId, Context context) {
        this.propertyId = propertyId;
        initItem();
    }

    public void initItem() {
        if (this.hashMap == null) {
            this.hashMap = PropertyHashMapDynamic.getInstance().getDynamicHashString(this.propertyId);
        }
        if (this.propertyId == ICatchCameraProperty.ICH_CAP_IMAGE_SIZE) {
            this.valueListString = CameraProperties.getInstance().getSupportedImageSizes();
        }
        if (this.propertyId == ICatchCameraProperty.ICH_CAP_VIDEO_SIZE) {
            this.valueListString = CameraProperties.getInstance().getSupportedVideoSizes();
        }
        int ii = 0;
        while (ii < this.valueListString.size()) {
            if (!this.hashMap.containsKey(this.valueListString.get(ii))) {
                this.valueListString.remove(ii);
                ii--;
            }
            ii++;
        }
        this.valueListStringUI = new LinkedList();
        this.valueArrayString = new String[this.valueListString.size()];
        if (this.valueListString != null) {
            for (ii = 0; ii < this.valueListString.size(); ii++) {
                this.valueListStringUI.add(ii, ((ItemInfo) this.hashMap.get(this.valueListString.get(ii))).uiStringInSettingString);
                this.valueArrayString[ii] = ((ItemInfo) this.hashMap.get(this.valueListString.get(ii))).uiStringInSettingString;
            }
        }
    }

    public String getCurrentValue() {
        return CameraProperties.getInstance().getCurrentStringPropertyValue(this.propertyId);
    }

    public String getCurrentUiStringInSetting() {
        ItemInfo itemInfo = (ItemInfo) this.hashMap.get(getCurrentValue());
        if (itemInfo == null) {
            return "Unknown";
        }
        return itemInfo.uiStringInSettingString;
    }

    public String getCurrentUiStringInPreview() {
        ItemInfo itemInfo = (ItemInfo) this.hashMap.get(getCurrentValue());
        if (itemInfo == null) {
            return "Unknown";
        }
        return itemInfo.uiStringInPreview;
    }

    public String getCurrentUiStringInSetting(int position) {
        return (String) this.valueListString.get(position);
    }

    public List<String> getValueList() {
        return this.valueListString;
    }

    public List<String> getValueListUI() {
        return this.valueListString;
    }

    public Boolean setValue(String value) {
        return Boolean.valueOf(CameraProperties.getInstance().setStringPropertyValue(this.propertyId, value));
    }

    public boolean setValueByPosition(int position) {
        return CameraProperties.getInstance().setStringPropertyValue(this.propertyId, (String) this.valueListString.get(position));
    }

    public String[] getValueArrayString() {
        return this.valueArrayString;
    }

    public Boolean needDisplayByMode(int previewMode) {
        boolean retValue = false;
        switch (this.propertyId) {
            case ICatchCameraProperty.ICH_CAP_IMAGE_SIZE /*20483*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE) && (previewMode == 1 || previewMode == 2 || previewMode == 2 || previewMode == 8 || previewMode == 6)) {
                    retValue = true;
                    break;
                }
            case ICatchCameraProperty.ICH_CAP_VIDEO_SIZE /*54789*/:
                if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE) && (previewMode == 3 || previewMode == 4 || previewMode == 4 || previewMode == 7 || previewMode == 5)) {
                    retValue = true;
                    break;
                }
        }
        return Boolean.valueOf(retValue);
    }
}
