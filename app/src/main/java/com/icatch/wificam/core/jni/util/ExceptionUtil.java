package com.icatch.wificam.core.jni.util;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

public class ExceptionUtil {
    private static Map<Integer, String> innerMsgMapping;

    public static String getErrorMessage(int errorID) {
        if (innerMsgMapping == null) {
            initMsgMapping();
        }
        if (innerMsgMapping == null) {
            return "unknown exception description, innerMsgMapping is null";
        }
        String message = (String) innerMsgMapping.get(Integer.valueOf(errorID));
        if (message == null) {
            return "unknown exception information, id: " + errorID;
        }
        return message;
    }

    @SuppressLint({"UseSparseArrays"})
    private static void initMsgMapping() {
        innerMsgMapping = new HashMap();
        innerMsgMapping.put(Integer.valueOf(-1), ExceptionMsg.ICH_DEVICE_BUSY_MSG);
        innerMsgMapping.put(Integer.valueOf(-2), ExceptionMsg.ICH_DEVICE_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-3), ExceptionMsg.ICH_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-4), ExceptionMsg.ICH_NOT_IMPLEMENTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-5), ExceptionMsg.ICH_TRY_AGAIN_MSG);
        innerMsgMapping.put(Integer.valueOf(-6), ExceptionMsg.ICH_BUF_TOO_SMALL_MSG);
        innerMsgMapping.put(Integer.valueOf(-7), ExceptionMsg.ICH_OUT_OF_MEMORY_MSG);
        innerMsgMapping.put(Integer.valueOf(-8), ExceptionMsg.ICH_FILE_NOT_FOUND_MSG);
        innerMsgMapping.put(Integer.valueOf(-9), ExceptionMsg.ICH_PATH_NOT_FOUND_MSG);
        innerMsgMapping.put(Integer.valueOf(-11), ExceptionMsg.ICH_INVALID_SESSION_MSG);
        innerMsgMapping.put(Integer.valueOf(-12), ExceptionMsg.ICH_INVALID_ARGUMENT_MSG);
        innerMsgMapping.put(Integer.valueOf(-66), ExceptionMsg.ICH_IMAGE_SIZE_FORMAT_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-67), ExceptionMsg.ICH_VIDEO_SIZE_FORMAT_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-14), ExceptionMsg.ICH_TIME_OUT_MSG);
        innerMsgMapping.put(Integer.valueOf(-15), ExceptionMsg.ICH_SOCKET_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-19), ExceptionMsg.ICH_DIS_CONNECTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-16), ExceptionMsg.ICH_PERMISSION_DENIED_MSG);
        innerMsgMapping.put(Integer.valueOf(-17), ExceptionMsg.ICH_UNKNOWN_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-18), ExceptionMsg.ICH_STREAM_NOT_RUNNING_MSG);
        innerMsgMapping.put(Integer.valueOf(-20), ExceptionMsg.ICH_STREAM_NOT_SUPPORT_MSG);
        innerMsgMapping.put(Integer.valueOf(-85), ExceptionMsg.ICH_VIDEO_STREAM_CLOSED_MSG);
        innerMsgMapping.put(Integer.valueOf(-86), ExceptionMsg.ICH_AUDIO_STREAM_CLOSED_MSG);
        innerMsgMapping.put(Integer.valueOf(-41), ExceptionMsg.ICH_MODE_NOT_SUPPORT_MSG);
        innerMsgMapping.put(Integer.valueOf(-42), ExceptionMsg.ICH_MODE_SET_ILLEGAL_MSG);
        innerMsgMapping.put(Integer.valueOf(-43), ExceptionMsg.ICH_MODE_CAMERA_BUSY_MSG);
        innerMsgMapping.put(Integer.valueOf(-44), ExceptionMsg.ICH_MODE_PTP_CLIENT_INVALID_MSG);
        innerMsgMapping.put(Integer.valueOf(-45), ExceptionMsg.ICH_MODE_CHANGE_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-64), ExceptionMsg.ICH_CAPTURE_IMAGE_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-65), ExceptionMsg.ICH_STORAGE_FORMAT_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-68), ExceptionMsg.ICH_SD_CARD_NOT_EXIST_MSG);
        innerMsgMapping.put(Integer.valueOf(-46), ExceptionMsg.ICH_WB_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-47), ExceptionMsg.ICH_WB_GET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-48), ExceptionMsg.ICH_WB_SET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-49), ExceptionMsg.ICH_CAP_DELAY_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-50), ExceptionMsg.ICH_CAP_DELAY_GET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-50), ExceptionMsg.ICH_CAP_DELAY_SET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-52), ExceptionMsg.ICH_IMAGE_SIZE_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-53), ExceptionMsg.ICH_IMAGE_SIZE_GET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-54), ExceptionMsg.ICH_IMAGE_SIZE_SET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-55), ExceptionMsg.ICH_VIDEO_SIZE_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-56), ExceptionMsg.ICH_VIDEO_SIZE_GET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-57), ExceptionMsg.ICH_VIDEO_SIZE_SET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-58), ExceptionMsg.ICH_LIGHT_FREQ_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-59), ExceptionMsg.ICH_LIGHT_FREQ_GET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-60), ExceptionMsg.ICH_LIGHT_FREQ_SET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-61), ExceptionMsg.ICH_BURST_NUMBER_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-62), ExceptionMsg.ICH_BURST_NUMBER_GET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-63), ExceptionMsg.ICH_BURST_NUMBER_SET_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-40), ExceptionMsg.ICH_BATTERY_LEVEL_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-69), ExceptionMsg.ICH_REMAIN_RECORD_TIME_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-70), ExceptionMsg.ICH_REMAIN_RECORD_TIME_NOT_SUPPORTED_MSG);
        innerMsgMapping.put(Integer.valueOf(-71), ExceptionMsg.ICH_MTP_GET_OBJECTS_ERROR_MSG);
        innerMsgMapping.put(Integer.valueOf(-72), ExceptionMsg.ICH_LISTENER_EXISTS_MSG);
        innerMsgMapping.put(Integer.valueOf(-73), ExceptionMsg.ICH_LISTENER_NOT_EXISTS_MSG);
        innerMsgMapping.put(Integer.valueOf(-78), ExceptionMsg.ICH_SEEK_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-79), ExceptionMsg.ICH_PAUSE_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-80), ExceptionMsg.ICH_RESUME_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-90), ExceptionMsg.ICH_SESSION_PASSWORD_ERR_MSG);
        innerMsgMapping.put(Integer.valueOf(-91), ExceptionMsg.ICH_SESSION_PTP_INIT_FAILED_MSG);
        innerMsgMapping.put(Integer.valueOf(-92), ExceptionMsg.ICH_SESSION_TUTK_INIT_FAILED_MSG);
    }
}
