package com.icatch.ismartdv2016.Tools;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.icatch.ismartdv2016.Log.AppLog;

public class LruCacheTool {
    private static final String TAG = "LruCacheTool";
    private static LruCacheTool instance;
    private LruCache<String, Bitmap> localThumbnailLruCache;

    public static LruCacheTool getInstance() {
        if (instance == null) {
            instance = new LruCacheTool();
        }
        return instance;
    }

    private LruCacheTool() {
    }

    public LruCache<String, Bitmap> getLruCache() {
        return this.localThumbnailLruCache;
    }

    public void initLruCache() {
        int cacheMemory = ((int) Runtime.getRuntime().maxMemory()) / 8;
        AppLog.d(TAG, "initLruCache cacheMemory=" + cacheMemory);
        this.localThumbnailLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            protected int sizeOf(String key, Bitmap value) {
                AppLog.d(LruCacheTool.TAG, "cacheMemory value.getByteCount()=" + value.getByteCount() + " key=" + key);
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    AppLog.d(LruCacheTool.TAG, "cacheMemory entryRemoved key=" + key);
                    oldValue.recycle();
                }
            }
        };
    }

    public void clearCache() {
        AppLog.d(TAG, "clearCache");
        this.localThumbnailLruCache.evictAll();
    }

    public Bitmap getBitmapFromLruCache(String file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Bitmap bitmap = (Bitmap) this.localThumbnailLruCache.get(file);
        AppLog.d(TAG, "getBitmapFromLruCache key=" + file + " bitmap=" + bitmap);
        return bitmap;
    }

    public void addBitmapToLruCache(String key, Bitmap bm) {
        if (getBitmapFromLruCache(key) == null && bm != null && key != null) {
            AppLog.d(TAG, "addBitmapToLruCache key=" + key + " size=" + bm.getByteCount() + " bitmap=" + bm);
            this.localThumbnailLruCache.put(key, bm);
        }
    }
}
