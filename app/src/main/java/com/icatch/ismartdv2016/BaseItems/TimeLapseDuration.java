package com.icatch.ismartdv2016.BaseItems;

import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchMode;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class TimeLapseDuration {
    public static final int TIME_LAPSE_DURATION_10MIN = 10;
    public static final int TIME_LAPSE_DURATION_15MIN = 15;
    public static final int TIME_LAPSE_DURATION_20MIN = 20;
    public static final int TIME_LAPSE_DURATION_2MIN = 2;
    public static final int TIME_LAPSE_DURATION_30MIN = 30;
    public static final int TIME_LAPSE_DURATION_5MIN = 5;
    public static final int TIME_LAPSE_DURATION_60MIN = 60;
    public static final int TIME_LAPSE_DURATION_UNLIMITED = 65535;
    private CameraProperties cameraProperty = CameraProperties.getInstance();
    private final String tag = "TimeLapseDuration";
    private int[] valueListInt;
    private String[] valueListString;

    public TimeLapseDuration() {
        initTimeLapseDuration();
    }

    public String getCurrentValue() {
        return convertTimeLapseDuration(this.cameraProperty.getCurrentTimeLapseDuration());
    }

    public String[] getValueStringList() {
        return this.valueListString;
    }

    public int[] getValueStringInt() {
        return this.valueListInt;
    }

    public boolean setValueByPosition(int position) {
        return CameraProperties.getInstance().setTimeLapseDuration(this.valueListInt[position]);
    }

    public void initTimeLapseDuration() {
        AppLog.i("TimeLapseDuration", "begin initTimeLapseDuration");
        if (this.cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)) {
            int ii;
            List<Integer> list = this.cameraProperty.getSupportedTimeLapseDurations();
            int length = list.size();
            ArrayList<String> tempArrayList = new ArrayList();
            this.valueListInt = new int[length];
            for (ii = 0; ii < length; ii++) {
                tempArrayList.add(convertTimeLapseDuration(((Integer) list.get(ii)).intValue()));
                this.valueListInt[ii] = ((Integer) list.get(ii)).intValue();
            }
            this.valueListString = new String[tempArrayList.size()];
            for (ii = 0; ii < tempArrayList.size(); ii++) {
                this.valueListString[ii] = (String) tempArrayList.get(ii);
            }
            AppLog.i("TimeLapseDuration", "end initTimeLapseDuration timeLapseDuration =" + this.valueListString.length);
        }
    }

    public Boolean needDisplayByMode(int previewMode) {
        if (CameraProperties.getInstance().cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) && (previewMode == 8 || previewMode == 6 || previewMode == 7 || previewMode == TIME_LAPSE_DURATION_5MIN)) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public static String convertTimeLapseDuration(int value) {
        if (value == TIME_LAPSE_DURATION_UNLIMITED) {
            return GlobalInfo.getInstance().getCurrentApp().getResources().getString(R.string.setting_time_lapse_duration_unlimit);
        }
        String time = BuildConfig.FLAVOR;
        int h = value / TIME_LAPSE_DURATION_60MIN;
        int m = value % TIME_LAPSE_DURATION_60MIN;
        if (h > 0) {
            time = time + h + "HR";
        }
        if (m > 0) {
            return time + m + "Min";
        }
        return time;
    }
}
