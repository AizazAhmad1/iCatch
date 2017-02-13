package com.icatch.ismartdv2016.ThumbnailGetting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Model.Implement.SDKSession;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.Tools.BitmapTools;
import com.icatch.wificam.customer.ICatchWificamPlayback;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

public class ThumbnailOperation {
    private static String TAG = "ThumbnailOperation";
    private static FileOperation fileOperation = FileOperation.getInstance();

    public static Bitmap getVideoThumbnailFromSdk(String videoPath) {
        if (videoPath == null) {
            return null;
        }
        AppLog.d(TAG, "start getVideoThumbnailFromSdk");
        ICatchWificamPlayback cameraPlayback = null;
        Bitmap bitmap = null;
        SDKSession sdkSession = new SDKSession();
        if (sdkSession.prepareSession("192.168.1.1", false)) {
            try {
                cameraPlayback = sdkSession.getSDKSession().getPlaybackClient();
            } catch (IchInvalidSessionException e) {
                e.printStackTrace();
            }
            ICatchFrameBuffer frameBuffer = FileOperation.getInstance().getThumbnail(cameraPlayback, videoPath);
            if (frameBuffer != null) {
                byte[] buffer = frameBuffer.getBuffer();
                if (frameBuffer.getFrameSize() > 0) {
                    bitmap = BitmapTools.decodeByteArray(buffer, BitmapTools.THUMBNAIL_WIDTH, BitmapTools.THUMBNAIL_WIDTH);
                }
            }
            sdkSession.destroySession();
            AppLog.d(TAG, "end getVideoThumbnailFromSdk bitmap=" + bitmap);
            return bitmap;
        }
        AppLog.d(TAG, "getVideoThumbnailFromSdk false");
        return null;
    }

    public static Bitmap getLocalVideoThumbnail(String videoPath) {
        AppLog.d(TAG, "start getLocalVideoThumbnail");
        Bitmap bitmap = null;
        ICatchFrameBuffer frameBuffer = FileOperation.getInstance().getThumbnail(videoPath);
        if (frameBuffer != null) {
            byte[] buffer = frameBuffer.getBuffer();
            if (frameBuffer.getFrameSize() > 0) {
                bitmap = BitmapTools.decodeByteArray(buffer, 160, 160);
            }
        }
        AppLog.d(TAG, "end getLocalVideoThumbnail bitmap=" + bitmap);
        return bitmap;
    }

    public static Bitmap getVideoThumbnail(String videoPath) {
        AppLog.d(TAG, "start getVideoThumbnail");
        Bitmap bitmap = BitmapTools.getVideoThumbnail(videoPath, BitmapTools.THUMBNAIL_WIDTH, BitmapTools.THUMBNAIL_WIDTH);
        if (bitmap == null) {
            bitmap = getVideoThumbnailFromSdk(videoPath);
        }
        AppLog.d(TAG, "end getVideoThumbnail bitmap=" + bitmap);
        return bitmap;
    }

    public static Bitmap getlocalVideoWallThumbnail(String videoPath) {
        AppLog.d(TAG, "start getVideoThumbnail");
        Bitmap bitmap = BitmapTools.getVideoThumbnail(videoPath, BitmapTools.THUMBNAIL_WIDTH, BitmapTools.THUMBNAIL_WIDTH);
        if (bitmap == null) {
            bitmap = getLocalVideoThumbnail(videoPath);
        }
        AppLog.d(TAG, "end getVideoThumbnail bitmap=" + bitmap);
        return bitmap;
    }

    public static Bitmap getPhotoThumbnail(int fileHandle) {
        AppLog.i(TAG, "start getPhotoThumbnail  fileHandle=" + fileHandle);
        ICatchFrameBuffer buffer = FileOperation.getInstance().getThumbnail(new ICatchFile(fileHandle));
        if (buffer == null) {
            AppLog.i("[Error] -- BitmapsLoad:", "buffer == null  send _LOAD_BITMAP_FAILED");
            return null;
        }
        int datalength = buffer.getFrameSize();
        if (datalength > 0) {
            return BitmapFactory.decodeByteArray(buffer.getBuffer(), 0, datalength);
        }
        AppLog.i(TAG, "datalength <= 0  LOAD_BITMAP_FAILED");
        return null;
    }

    public static int getBatteryLevelIcon() {
        int current = CameraProperties.getInstance().getBatteryElectric();
        AppLog.d(TAG, "current setBatteryLevelIcon= " + current);
        if (current < 20 && current >= 0) {
            return R.drawable.ic_battery_alert_green_24dp;
        }
        if (current == 33) {
            return R.drawable.ic_battery_30_green_24dp;
        }
        if (current == 66) {
            return R.drawable.ic_battery_60_green_24dp;
        }
        if (current == 100) {
            return R.drawable.ic_battery_full_green_24dp;
        }
        if (current > 100) {
            return R.drawable.ic_battery_charging_full_green_24dp;
        }
        return -1;
    }
}
