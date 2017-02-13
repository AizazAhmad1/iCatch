package com.icatch.wificam.core.jni.extractor;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.util.ExceptionErr;
import com.icatch.wificam.core.jni.util.ExceptionUtil;
import com.icatch.wificam.core.util.SDKEventHandleAPI;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchCaptureImageException;
import com.icatch.wificam.customer.exception.IchDeviceBusyException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidPasswdException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.exception.IchNoSDCardException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchNoSuchPathException;
import com.icatch.wificam.customer.exception.IchNotImplementedException;
import com.icatch.wificam.customer.exception.IchNotSupportedException;
import com.icatch.wificam.customer.exception.IchOutOfMemoryException;
import com.icatch.wificam.customer.exception.IchPauseFailedException;
import com.icatch.wificam.customer.exception.IchPbStreamPausedException;
import com.icatch.wificam.customer.exception.IchPermissionDeniedException;
import com.icatch.wificam.customer.exception.IchPtpInitFailedException;
import com.icatch.wificam.customer.exception.IchResumeFailedException;
import com.icatch.wificam.customer.exception.IchSeekFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStorageFormatException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchStreamNotSupportException;
import com.icatch.wificam.customer.exception.IchTimeOutException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchTutkInitFailedption;
import com.icatch.wificam.customer.exception.IchUnknownException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;

public class NativeValueExtractor {
    public static String extractNativeStringValue(String content) throws IchInvalidSessionException, IchListenerNotExistsException, IchDeviceBusyException, IchDeviceException, IchNotSupportedException, IchNotImplementedException, IchTryAgainException, IchBufferTooSmallException, IchOutOfMemoryException, IchNoSuchFileException, IchNoSuchPathException, IchInvalidArgumentException, IchTimeOutException, IchSocketException, IchPermissionDeniedException, IchStreamNotRunningException, IchCameraModeException, IchUnknownException, IchDevicePropException, IchCaptureImageException, IchStorageFormatException, IchListenerExistsException, IchNoSDCardException, IchSeekFailedException, IchPauseFailedException, IchResumeFailedException, IchVideoStreamClosedException, IchAudioStreamClosedException, IchInvalidPasswdException, IchPtpInitFailedException, IchTutkInitFailedption, IchPbStreamPausedException, IchStreamNotSupportException {
        if (content.contains(NativeValueTag.RETURN_ERR_TAG)) {
            errorToException(NativeValueUtil.getErrValue(content));
        }
        if (content.contains(NativeValueTag.RETURN_STRING_TAG)) {
            return NativeValueUtil.getStringValue(content);
        }
        CoreLogger.logE("NativeValueExtractor", "Fatal error, invalid data from jni layer." + content);
        return null;
    }

    public static int extractNativeIntValue(String content) throws IchInvalidSessionException, IchListenerNotExistsException, IchDeviceBusyException, IchDeviceException, IchNotSupportedException, IchNotImplementedException, IchTryAgainException, IchBufferTooSmallException, IchOutOfMemoryException, IchNoSuchFileException, IchNoSuchPathException, IchInvalidArgumentException, IchTimeOutException, IchSocketException, IchPermissionDeniedException, IchStreamNotRunningException, IchCameraModeException, IchUnknownException, IchDevicePropException, IchCaptureImageException, IchStorageFormatException, IchListenerExistsException, IchNoSDCardException, IchSeekFailedException, IchPauseFailedException, IchResumeFailedException, IchVideoStreamClosedException, IchAudioStreamClosedException, IchInvalidPasswdException, IchPtpInitFailedException, IchTutkInitFailedption, IchPbStreamPausedException, IchStreamNotSupportException {
        if (content.contains(NativeValueTag.RETURN_ERR_TAG)) {
            errorToException(NativeValueUtil.getErrValue(content));
        }
        if (content.contains(NativeValueTag.RETURN_INT_TAG)) {
            return NativeValueUtil.getIntValue(content);
        }
        CoreLogger.logE("NativeValueExtractor", "Fatal error, invalid data from jni layer." + content);
        return -1;
    }

    public static long extractNativeLongValue(String content) throws IchInvalidSessionException, IchListenerNotExistsException, IchDeviceBusyException, IchDeviceException, IchNotSupportedException, IchNotImplementedException, IchTryAgainException, IchBufferTooSmallException, IchOutOfMemoryException, IchNoSuchFileException, IchNoSuchPathException, IchInvalidArgumentException, IchTimeOutException, IchSocketException, IchPermissionDeniedException, IchStreamNotRunningException, IchCameraModeException, IchUnknownException, IchDevicePropException, IchCaptureImageException, IchStorageFormatException, IchListenerExistsException, IchNoSDCardException, IchSeekFailedException, IchPauseFailedException, IchResumeFailedException, IchVideoStreamClosedException, IchAudioStreamClosedException, IchInvalidPasswdException, IchPtpInitFailedException, IchTutkInitFailedption, IchPbStreamPausedException, IchStreamNotSupportException {
        if (content.contains(NativeValueTag.RETURN_ERR_TAG)) {
            errorToException(NativeValueUtil.getErrValue(content));
        }
        if (content.contains(NativeValueTag.RETURN_LONG_TAG)) {
            return NativeValueUtil.getLongValue(content);
        }
        CoreLogger.logE("NativeValueExtractor", "Fatal error, invalid data from jni layer." + content);
        return -1;
    }

    public static boolean extractNativeBoolValue(String content) throws IchInvalidSessionException, IchListenerNotExistsException, IchDeviceBusyException, IchDeviceException, IchNotSupportedException, IchNotImplementedException, IchTryAgainException, IchBufferTooSmallException, IchOutOfMemoryException, IchNoSuchFileException, IchNoSuchPathException, IchInvalidArgumentException, IchTimeOutException, IchSocketException, IchPermissionDeniedException, IchStreamNotRunningException, IchCameraModeException, IchUnknownException, IchDevicePropException, IchPauseFailedException, IchResumeFailedException, IchCaptureImageException, IchStorageFormatException, IchListenerExistsException, IchNoSDCardException, IchSeekFailedException, IchVideoStreamClosedException, IchAudioStreamClosedException, IchInvalidPasswdException, IchPtpInitFailedException, IchTutkInitFailedption, IchPbStreamPausedException, IchStreamNotSupportException {
        if (content.contains(NativeValueTag.RETURN_ERR_TAG)) {
            errorToException(NativeValueUtil.getErrValue(content));
        }
        if (content.contains(NativeValueTag.RETURN_BOOL_TAG)) {
            return NativeValueUtil.getBoolValue(content);
        }
        CoreLogger.logE("NativeValueExtractor", "Fatal error, invalid data from jni layer." + content);
        return false;
    }

    public static double extractNativeDoubleValue(String content) throws IchInvalidSessionException, IchListenerNotExistsException, IchDeviceBusyException, IchDeviceException, IchNotSupportedException, IchNotImplementedException, IchTryAgainException, IchBufferTooSmallException, IchOutOfMemoryException, IchNoSuchFileException, IchNoSuchPathException, IchInvalidArgumentException, IchTimeOutException, IchSocketException, IchPermissionDeniedException, IchStreamNotRunningException, IchCameraModeException, IchUnknownException, IchDevicePropException, IchPauseFailedException, IchResumeFailedException, IchCaptureImageException, IchStorageFormatException, IchListenerExistsException, IchNoSDCardException, IchSeekFailedException, IchVideoStreamClosedException, IchAudioStreamClosedException, IchInvalidPasswdException, IchPtpInitFailedException, IchTutkInitFailedption, IchPbStreamPausedException, IchStreamNotSupportException {
        if (content.contains(NativeValueTag.RETURN_ERR_TAG)) {
            errorToException(NativeValueUtil.getErrValue(content));
        }
        if (content.contains(NativeValueTag.RETURN_DOUBLE_TAG)) {
            return NativeValueUtil.getDoubleValue(content);
        }
        CoreLogger.logE("NativeValueExtractor", "Fatal error, invalid data from jni layer. " + content);
        return -1.0d;
    }

    private static void errorToException(int errorID) throws IchDeviceBusyException, IchDeviceException, IchNotSupportedException, IchNotImplementedException, IchTryAgainException, IchBufferTooSmallException, IchOutOfMemoryException, IchNoSuchFileException, IchNoSuchPathException, IchInvalidSessionException, IchInvalidArgumentException, IchTimeOutException, IchSocketException, IchPermissionDeniedException, IchStreamNotRunningException, IchCameraModeException, IchUnknownException, IchDevicePropException, IchCaptureImageException, IchStorageFormatException, IchListenerExistsException, IchListenerNotExistsException, IchNoSDCardException, IchSeekFailedException, IchPauseFailedException, IchResumeFailedException, IchVideoStreamClosedException, IchAudioStreamClosedException, IchInvalidPasswdException, IchPtpInitFailedException, IchTutkInitFailedption, IchPbStreamPausedException, IchStreamNotSupportException {
        switch (errorID) {
            case ExceptionErr.ICH_SESSION_TUTK_INIT_FAILED /*-92*/:
                throw new IchTutkInitFailedption(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_SESSION_PTP_INIT_FAILED /*-91*/:
                throw new IchPtpInitFailedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_SESSION_PASSWORD_ERR /*-90*/:
                throw new IchInvalidPasswdException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_AUDIO_STREAM_CLOSED /*-86*/:
                throw new IchAudioStreamClosedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_VIDEO_STREAM_CLOSED /*-85*/:
                throw new IchVideoStreamClosedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_PB_STREAM_PAUSED /*-84*/:
                throw new IchPbStreamPausedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_RESUME_FAILED /*-80*/:
                throw new IchResumeFailedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_PAUSE_FAILED /*-79*/:
                throw new IchPauseFailedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_SEEK_FAILED /*-78*/:
                throw new IchSeekFailedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_LISTENER_NOT_EXISTS /*-73*/:
                throw new IchListenerNotExistsException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_LISTENER_EXISTS /*-72*/:
                throw new IchListenerExistsException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_REMAIN_RECORD_TIME_NOT_SUPPORTED /*-70*/:
            case ExceptionErr.ICH_FREE_SPACE_IN_IMAGE_NOT_SUPPORTED /*-69*/:
            case ExceptionErr.ICH_BURST_NUMBER_SET_FAILED /*-63*/:
            case ExceptionErr.ICH_BURST_NUMBER_GET_FAILED /*-62*/:
            case ExceptionErr.ICH_BURST_NUMBER_NOT_SUPPORTED /*-61*/:
            case ExceptionErr.ICH_LIGHT_FREQ_SET_FAILED /*-60*/:
            case ExceptionErr.ICH_LIGHT_FREQ_GET_FAILED /*-59*/:
            case ExceptionErr.ICH_LIGHT_FREQ_NOT_SUPPORTED /*-58*/:
            case ExceptionErr.ICH_VIDEO_SIZE_SET_FAILED /*-57*/:
            case ExceptionErr.ICH_VIDEO_SIZE_GET_FAILED /*-56*/:
            case ExceptionErr.ICH_VIDEO_SIZE_NOT_SUPPORTED /*-55*/:
            case ExceptionErr.ICH_IMAGE_SIZE_SET_FAILED /*-54*/:
            case ExceptionErr.ICH_IMAGE_SIZE_GET_FAILED /*-53*/:
            case ExceptionErr.ICH_IMAGE_SIZE_NOT_SUPPORTED /*-52*/:
            case ExceptionErr.ICH_CAP_DELAY_SET_FAILED /*-51*/:
            case ExceptionErr.ICH_CAP_DELAY_GET_FAILED /*-50*/:
            case ExceptionErr.ICH_CAP_DELAY_NOT_SUPPORTED /*-49*/:
            case ExceptionErr.ICH_WB_SET_FAILED /*-48*/:
            case ExceptionErr.ICH_WB_GET_FAILED /*-47*/:
            case ExceptionErr.ICH_WB_NOT_SUPPORTED /*-46*/:
            case ExceptionErr.ICH_BATTERY_LEVEL_NOT_SUPPORTED /*-40*/:
                throw new IchDevicePropException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_SD_CARD_NOT_EXIST /*-68*/:
                throw new IchNoSDCardException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_VIDEO_SIZE_FORMAT_ERROR /*-67*/:
            case ExceptionErr.ICH_IMAGE_SIZE_FORMAT_ERROR /*-66*/:
            case ExceptionErr.ICH_INVALID_ARGUMENT /*-12*/:
                throw new IchInvalidArgumentException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_STORAGE_FORMAT_ERROR /*-65*/:
                throw new IchStorageFormatException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_CAPTURE_ERROR /*-64*/:
                throw new IchCaptureImageException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_MODE_CHANGE_FAILED /*-45*/:
            case ExceptionErr.ICH_MODE_PTP_CLIENT_INVALID /*-44*/:
            case ExceptionErr.ICH_MODE_CAMERA_BUSY /*-43*/:
            case ExceptionErr.ICH_MODE_SET_ILLEGAL /*-42*/:
            case ExceptionErr.ICH_MODE_NOT_SUPPORT /*-41*/:
                throw new IchCameraModeException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_STREAM_NOT_SUPPORT /*-20*/:
                throw new IchStreamNotSupportException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_WIFI_DISCONNECTED /*-19*/:
            case ExceptionErr.ICH_SOCKET_ERROR /*-15*/:
                throw new IchSocketException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_STREAM_NOT_RUNNING /*-18*/:
                throw new IchStreamNotRunningException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_PERMISSION_DENIED /*-16*/:
                throw new IchPermissionDeniedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_TIME_OUT /*-14*/:
                throw new IchTimeOutException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_INVALID_SESSION /*-11*/:
                throw new IchInvalidSessionException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_PATH_NOT_FOUND /*-9*/:
                throw new IchNoSuchPathException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_FILE_NOT_FOUND /*-8*/:
                throw new IchNoSuchFileException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_OUT_OF_MEMORY /*-7*/:
                throw new IchOutOfMemoryException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_BUF_TOO_SMALL /*-6*/:
                throw new IchBufferTooSmallException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_TRY_AGAIN /*-5*/:
                throw new IchTryAgainException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_NOT_IMPLEMENTED /*-4*/:
                throw new IchNotImplementedException(ExceptionUtil.getErrorMessage(errorID));
            case ExceptionErr.ICH_NOT_SUPPORTED /*-3*/:
                throw new IchNotSupportedException(ExceptionUtil.getErrorMessage(errorID));
            case SDKEventHandleAPI.__SESSION_ID_FOR_ALL /*-2*/:
                throw new IchDeviceException(ExceptionUtil.getErrorMessage(errorID));
            case SDKEventHandleAPI.__SESSION_ID_FOR_NON /*-1*/:
                throw new IchDeviceBusyException(ExceptionUtil.getErrorMessage(errorID));
            default:
                CoreLogger.logE("error Exception mapping", "unknown errorID: " + errorID);
                throw new IchUnknownException(ExceptionUtil.getErrorMessage(errorID));
        }
    }
}
