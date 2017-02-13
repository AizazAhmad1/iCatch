package com.icatch.ismartdv2016.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.provider.MediaStore.Video.Media;
import com.icatch.ismartdv2016.Log.AppLog;
import java.io.File;
import uk.co.senab.photoview.BuildConfig;

public class MediaRefresh {
    private static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    private static final String TAG = "MediaRefresh";

    public static void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }

    public static void scanFileAsync(Context ctx, String filename) {
        AppLog.d(TAG, "scanFileAsync");
        MediaScannerConnection.scanFile(ctx, new String[]{filename}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
            }
        });
    }

    public static void addMediaToDB(Context ctx, String filePath, String fileType) {
        AppLog.d(TAG, "addMediaToDB filePath=" + filePath);
        AppLog.d(TAG, "addMediaToDB fileType=" + fileType);
        File videofile = new File(filePath);
        ContentValues values = new ContentValues(5);
        long current = System.currentTimeMillis();
        values.put("title", videofile.getName());
        values.put("date_added", Integer.valueOf((int) (current / 1000)));
        values.put("mime_type", fileType);
        values.put("_data", videofile.getAbsolutePath());
        values.put("album", BuildConfig.FLAVOR);
        ctx.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
    }
}
