package com.icatch.ismartdv2016.Hash;

import android.annotation.SuppressLint;
import com.icatch.ismartdv2016.BaseItems.SlowMotion;
import com.icatch.ismartdv2016.Beans.ItemInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import com.icatch.wificam.customer.type.ICatchTimeLapseDuration;
import java.util.HashMap;

public class PropertyHashMapStatic {
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> burstMap = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> dateStampMap = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> electricityFrequencyMap = new HashMap();
    public static PropertyHashMapStatic propertyHashMap;
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> slowMotionMap = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> timeLapseDurationMap = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> timeLapseIntervalMap = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> timeLapseMode = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> upsideMap = new HashMap();
    @SuppressLint({"UseSparseArrays"})
    public static HashMap<Integer, ItemInfo> whiteBalanceMap = new HashMap();
    private final String tag = "PropertyHashMapStatic";

    public static PropertyHashMapStatic getInstance() {
        if (propertyHashMap == null) {
            propertyHashMap = new PropertyHashMapStatic();
        }
        return propertyHashMap;
    }

    public void initPropertyHashMap() {
        AppLog.i("PropertyHashMapStatic", "Start initPropertyHashMap");
        initWhiteBalanceMap();
        initTimeLapseDuration();
        initSlowMotion();
        initUpside();
        initBurstMap();
        initElectricityFrequencyMap();
        initDateStampMap();
        ininTimeLapseMode();
        AppLog.i("PropertyHashMapStatic", "End initPropertyHashMap");
    }

    private void ininTimeLapseMode() {
        timeLapseMode.put(Integer.valueOf(0), new ItemInfo((int) R.string.timeLapse_capture_mode, null, 0));
        timeLapseMode.put(Integer.valueOf(1), new ItemInfo((int) R.string.timeLapse_video_mode, null, 0));
    }

    public void initWhiteBalanceMap() {
        whiteBalanceMap.put(Integer.valueOf(1), new ItemInfo((int) R.string.wb_auto, null, (int) R.drawable.awb_auto));
        whiteBalanceMap.put(Integer.valueOf(3), new ItemInfo((int) R.string.wb_cloudy, null, (int) R.drawable.awb_cloudy));
        whiteBalanceMap.put(Integer.valueOf(2), new ItemInfo((int) R.string.wb_daylight, null, (int) R.drawable.awb_daylight));
        whiteBalanceMap.put(Integer.valueOf(4), new ItemInfo((int) R.string.wb_fluorescent, null, (int) R.drawable.awb_fluoresecent));
        whiteBalanceMap.put(Integer.valueOf(5), new ItemInfo((int) R.string.wb_incandescent, null, (int) R.drawable.awb_incadescent));
    }

    private void initTimeLapseDuration() {
        timeLapseIntervalMap.put(Integer.valueOf(2), new ItemInfo((int) R.string.setting_time_lapse_duration_2M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(5), new ItemInfo((int) R.string.setting_time_lapse_duration_5M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(10), new ItemInfo((int) R.string.setting_time_lapse_duration_10M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(15), new ItemInfo((int) R.string.setting_time_lapse_duration_15M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(20), new ItemInfo((int) R.string.setting_time_lapse_duration_20M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(30), new ItemInfo((int) R.string.setting_time_lapse_duration_30M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(60), new ItemInfo((int) R.string.setting_time_lapse_duration_60M, null, 0));
        timeLapseIntervalMap.put(Integer.valueOf(ICatchTimeLapseDuration.ICH_TIMELPS_DURATION_UNLMT), new ItemInfo((int) R.string.setting_time_lapse_duration_unlimit, null, 0));
    }

    private void initSlowMotion() {
        slowMotionMap.put(Integer.valueOf(SlowMotion.SLOW_MOTION_OFF), new ItemInfo((int) R.string.setting_off, null, 0));
        slowMotionMap.put(Integer.valueOf(SlowMotion.SLOW_MOTION_ON), new ItemInfo((int) R.string.setting_on, null, 0));
    }

    private void initUpside() {
        upsideMap.put(Integer.valueOf(0), new ItemInfo((int) R.string.setting_off, null, 0));
        upsideMap.put(Integer.valueOf(1), new ItemInfo((int) R.string.setting_on, null, 0));
    }

    public void initBurstMap() {
        burstMap.put(Integer.valueOf(1), new ItemInfo((int) R.string.burst_off, null, 0));
        burstMap.put(Integer.valueOf(2), new ItemInfo((int) R.string.burst_3, null, (int) R.drawable.continuous_shot_1));
        burstMap.put(Integer.valueOf(3), new ItemInfo((int) R.string.burst_5, null, (int) R.drawable.continuous_shot_2));
        burstMap.put(Integer.valueOf(4), new ItemInfo((int) R.string.burst_10, null, (int) R.drawable.continuous_shot_3));
        burstMap.put(Integer.valueOf(0), new ItemInfo((int) R.string.burst_hs, null, 0));
    }

    public void initElectricityFrequencyMap() {
        electricityFrequencyMap.put(Integer.valueOf(0), new ItemInfo((int) R.string.frequency_50HZ, null, 0));
        electricityFrequencyMap.put(Integer.valueOf(1), new ItemInfo((int) R.string.frequency_60HZ, null, 0));
    }

    public void initDateStampMap() {
        dateStampMap.put(Integer.valueOf(1), new ItemInfo((int) R.string.dateStamp_off, null, 0));
        dateStampMap.put(Integer.valueOf(2), new ItemInfo((int) R.string.dateStamp_date, null, 0));
        dateStampMap.put(Integer.valueOf(3), new ItemInfo((int) R.string.dateStamp_date_and_time, null, 0));
    }
}
