package com.icatch.ismartdv2016.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import com.icatch.ismartdv2016.Log.AppLog;
import uk.co.senab.photoview.IPhotoView;

public class BitmapTools {
    private static final long LIMITED_IMGAE_SIZE = 10485760;
    private static final int ORIGINAL_HEIGHT = 720;
    private static final int ORIGINAL_WIDTH = 1080;
    private static String TAG = BitmapTools.class.getSimpleName();
    public static final int THUMBNAIL_HEIGHT = 180;
    public static final int THUMBNAIL_WIDTH = 180;

    public static Bitmap getImageByPath(String imagePath, int width, int height) {
        AppLog.d(TAG, "Start getImageByPath imagePath=" + imagePath);
        if (imagePath == null) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, width, height);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        AppLog.d(TAG, "End getImageByPath bitMap=" + bitmap);
        return zoomBitmap(bitmap, (float) width, (float) height);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (true) {
            if (height / inSampleSize <= reqHeight && width / inSampleSize <= reqWidth) {
                break;
            }
            inSampleSize *= 2;
        }
        AppLog.d(TAG, "height * width /(inSampleSize * inSampleSize) *4 =" + (((height * width) / (inSampleSize * inSampleSize)) * 4));
        while (((long) (((height * width) / (inSampleSize * inSampleSize)) * 4)) > LIMITED_IMGAE_SIZE) {
            inSampleSize *= 2;
        }
        AppLog.d(TAG, "calculateInSampleSize return inSampleSize=" + inSampleSize);
        return inSampleSize;
    }

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
        AppLog.i(TAG, "start getVideoThumbnail videoPath=" + videoPath);
        if (videoPath == null) {
            return null;
        }
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 2);
        AppLog.i(TAG, "End getVideoThumbnail bitmap=" + bitmap);
        return zoomBitmap(bitmap, (float) width, (float) height);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float width, float heigth) {
        if (bitmap != null) {
            float zoomRate = IPhotoView.DEFAULT_MIN_SCALE;
            if (((float) bitmap.getWidth()) < width && ((float) bitmap.getHeight()) < heigth) {
                if (((float) bitmap.getHeight()) * width > ((float) bitmap.getWidth()) * heigth) {
                    zoomRate = heigth / ((float) bitmap.getHeight());
                } else if (((float) bitmap.getHeight()) * width <= ((float) bitmap.getWidth()) * heigth) {
                    zoomRate = width / ((float) bitmap.getWidth());
                }
            }
            Matrix matrix = new Matrix();
            matrix.postScale(zoomRate, zoomRate);
            try {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (OutOfMemoryError e) {
                AppLog.e(TAG, "zoomBitmap OutOfMemoryError");
                bitmap.recycle();
                System.gc();
            }
        }
        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data) {
        AppLog.d(TAG, "start decodeByteArray");
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int sampleSize = calculateInSampleSize(options, options.outWidth, options.outHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        AppLog.d(TAG, "end decodeByteArray bitmap=" + bitmap);
        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data, int reqWidth, int reqHeight) {
        AppLog.d(TAG, "start decodeByteArray");
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        AppLog.d(TAG, "end decodeByteArray");
        return zoomBitmap(bitmap, (float) reqWidth, (float) reqHeight);
    }
}
