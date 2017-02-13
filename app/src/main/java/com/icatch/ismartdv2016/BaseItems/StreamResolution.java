package com.icatch.ismartdv2016.BaseItems;

import android.util.Log;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchVideoFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StreamResolution {
    public static String currentResolutionCmd = null;
    public static ICatchVideoFormat currentResolutionVideoFormat = null;
    private HashMap<String, String> hashMap;
    private String[] resolutionStrategyList;
    private ICatchVideoFormat[] resolutionVideoFormatsList;
    private String[] valueArrayString;
    private String[] valueArrayStringUI;

    public StreamResolution() {
        initItem();
    }

    public void initItem() {
        currentResolutionCmd = null;
        this.hashMap = new HashMap();
        List<ICatchVideoFormat> tempList = CameraProperties.getInstance().getResolutionList();
        this.valueArrayStringUI = new String[tempList.size()];
        this.valueArrayString = new String[tempList.size()];
        for (int ii = 0; ii < tempList.size(); ii++) {
            ICatchVideoFormat temp = (ICatchVideoFormat) tempList.get(ii);
            if (temp.getCodec() == 64) {
                this.valueArrayStringUI[ii] = "MJPG/";
                this.valueArrayString[ii] = "MJPG?";
            } else if (temp.getCodec() == 41) {
                this.valueArrayStringUI[ii] = "H264/";
                this.valueArrayString[ii] = "H264?";
            }
            this.valueArrayStringUI[ii] = this.valueArrayStringUI[ii] + temp.getVideoW() + " X " + temp.getVideoH() + "/";
            this.valueArrayString[ii] = this.valueArrayString[ii] + "W=" + temp.getVideoW() + "&H=" + temp.getVideoH();
            this.valueArrayStringUI[ii] = this.valueArrayStringUI[ii] + temp.getBitrate();
            this.valueArrayString[ii] = this.valueArrayString[ii] + "&BR=" + temp.getBitrate() + "&";
            Log.d("1111", "valueArrayString[ii] =" + this.valueArrayString[ii]);
            this.hashMap.put(this.valueArrayStringUI[ii], this.valueArrayString[ii]);
        }
        initStreamStrategy();
    }

    public String getCurrentValue() {
        return currentResolutionCmd;
    }

    public String getValueByPositon(int position) {
        return this.valueArrayString[position];
    }

    public String[] getValueList() {
        return this.valueArrayStringUI;
    }

    public int getCurrentPositon() {
        for (int ii = 0; ii < this.valueArrayString.length; ii++) {
            if (this.valueArrayString[ii].equals(getCurrentValue())) {
                return ii;
            }
        }
        return 0;
    }

    public void initStreamStrategy() {
        ICatchVideoFormat temp;
        List<ICatchVideoFormat> tempList = CameraProperties.getInstance().getResolutionList();
        List<ICatchVideoFormat> tempList1 = new LinkedList();
        for (ICatchVideoFormat temp2 : tempList) {
            if (temp2.getCodec() == 41) {
                tempList1.add(temp2);
            }
        }
        this.resolutionStrategyList = new String[tempList1.size()];
        this.resolutionVideoFormatsList = new ICatchVideoFormat[tempList1.size()];
        for (int ii = 0; ii < tempList1.size(); ii++) {
            temp2 = (ICatchVideoFormat) tempList1.get(ii);
            this.resolutionStrategyList[ii] = "H264?W=" + temp2.getVideoW() + "&H=" + temp2.getVideoH() + "&BR=" + temp2.getBitrate() + "&";
            this.resolutionVideoFormatsList[ii] = temp2;
        }
    }

    public String getAutoChangeStream() {
        if (currentResolutionCmd == null) {
            return null;
        }
        int ii = 0;
        while (ii < this.resolutionStrategyList.length) {
            if (currentResolutionCmd.equals(this.resolutionStrategyList[ii]) && ii != this.resolutionStrategyList.length - 1) {
                return this.resolutionStrategyList[ii + 1];
            }
            ii++;
        }
        return null;
    }

    public String getStreamDown() {
        if (currentResolutionCmd == null) {
            return null;
        }
        int ii = 0;
        while (ii < this.resolutionStrategyList.length) {
            if (currentResolutionCmd.equals(this.resolutionStrategyList[ii]) && ii != this.resolutionStrategyList.length - 1) {
                return this.resolutionStrategyList[ii + 1];
            }
            ii++;
        }
        return null;
    }

    public String getStreamUp() {
        if (currentResolutionCmd == null) {
            return null;
        }
        int ii = 0;
        while (ii < this.resolutionStrategyList.length) {
            if (currentResolutionCmd.equals(this.resolutionStrategyList[ii]) && ii != 0) {
                return this.resolutionStrategyList[ii - 1];
            }
            ii++;
        }
        return null;
    }

    public ICatchVideoFormat getStreamDownVideoFormat() {
        if (currentResolutionCmd == null) {
            return null;
        }
        int ii = 0;
        while (ii < this.resolutionStrategyList.length) {
            if (currentResolutionCmd.equals(this.resolutionStrategyList[ii]) && ii != this.resolutionStrategyList.length - 1) {
                return this.resolutionVideoFormatsList[ii + 1];
            }
            ii++;
        }
        return null;
    }

    public ICatchVideoFormat getStreamUpVideoFormat() {
        if (currentResolutionCmd == null) {
            return null;
        }
        int ii = 0;
        while (ii < this.resolutionStrategyList.length) {
            if (currentResolutionCmd.equals(this.resolutionStrategyList[ii]) && ii != 0) {
                return this.resolutionVideoFormatsList[ii - 1];
            }
            ii++;
        }
        return null;
    }
}
