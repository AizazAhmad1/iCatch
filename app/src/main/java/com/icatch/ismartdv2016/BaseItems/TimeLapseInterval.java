package com.icatch.ismartdv2016.BaseItems;

import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchMode;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class TimeLapseInterval {
    public static final int TIME_LAPSE_INTERVAL_OFF = 0;
    private CameraProperties cameraProperty = CameraProperties.getInstance();
    private final String tag = "TimeLapseInterval";
    private int[] valueListInt;
    private String[] valueListString;

    public TimeLapseInterval() {
        initTimeLapseInterval();
    }

    public String getCurrentValue() {
        return convertTimeLapseInterval(this.cameraProperty.getCurrentTimeLapseInterval());
    }

    public String[] getValueStringList() {
        return this.valueListString;
    }

    public int[] getValueStringInt() {
        return this.valueListInt;
    }

    public boolean setValueByPosition(int position) {
        return CameraProperties.getInstance().setTimeLapseInterval(this.valueListInt[position]);
    }

    public void initTimeLapseInterval() {
        AppLog.i("TimeLapseInterval", "begin initTimeLapseInterval");
        if (this.cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)) {
            int ii;
            List<Integer> list = this.cameraProperty.getSupportedTimeLapseIntervals();
            int length = list.size();
            ArrayList<String> tempArrayList = new ArrayList();
            this.valueListInt = new int[length];
            for (ii = 0; ii < length; ii++) {
                tempArrayList.add(convertTimeLapseInterval(((Integer) list.get(ii)).intValue()));
                this.valueListInt[ii] = ((Integer) list.get(ii)).intValue();
            }
            this.valueListString = new String[tempArrayList.size()];
            for (ii = 0; ii < tempArrayList.size(); ii++) {
                this.valueListString[ii] = (String) tempArrayList.get(ii);
            }
            AppLog.i("TimeLapseInterval", "end initTimeLapseInterval timeLapseInterval =" + this.valueListString.length);
        }
    }

    public Boolean needDisplayByMode(int previewMode) {
        if (CameraProperties.getInstance().cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) && (previewMode == 8 || previewMode == 6 || previewMode == 7 || previewMode == 5)) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public static String convertTimeLapseInterval(int value) {
        if (value == 0) {
            return "OFF";
        }
        String time = BuildConfig.FLAVOR;
        if (value == -2) {
            return "0.5 Sec";
        }
        int h = value / 3600;
        int m = (value % 3600) / 60;
        int s = value % 60;
        if (h > 0) {
            time = time + h + " HR ";
        }
        if (m > 0) {
            time = time + m + " Min ";
        }
        if (s > 0) {
            return time + s + " Sec";
        }
        return time;
    }
}
