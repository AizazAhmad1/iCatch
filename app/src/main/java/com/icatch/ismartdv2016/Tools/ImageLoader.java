package com.icatch.ismartdv2016.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import com.icatch.ismartdv2016.BaseItems.LoadImageType;
import com.icatch.ismartdv2016.Listener.UpdateImageViewListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.ThumbnailGetting.ThumbnailOperation;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ImageLoader {
    private static final int DEAFULT_THREAD_COUNT = 1;
    private static final String TAG = "ImageLoader";
    private static ImageLoader mInstance;
    private boolean isDiskCacheEnable = true;
    private LruCache<String, Bitmap> mLruCache;
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;
    private LinkedList<Runnable> mTaskQueue;
    private ExecutorService mThreadPool;
    private Type mType = Type.FIFO;
    private Handler mUIHandler;
    private UpdateImageViewListener updateImageViewListener;

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;

        private ImgBeanHolder() {
        }
    }

    public enum Type {
        FIFO,
        LIFO
    }

    private ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        initBackThread();
        int cacheMemory = ((int) Runtime.getRuntime().maxMemory()) / 8;
        AppLog.d("cacheMemory", " cacheMemory=" + cacheMemory);
        this.mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            protected int sizeOf(String key, Bitmap value) {
                AppLog.d("cacheMemory", " value.getByteCount()=" + value.getByteCount());
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    oldValue.recycle();
                }
            }
        };
        this.mThreadPool = Executors.newFixedThreadPool(threadCount);
        this.mTaskQueue = new LinkedList();
        this.mType = type;
        this.mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    private void initBackThread() {
        this.mPoolThread = new Thread() {
            public void run() {
                Looper.prepare();
                AppLog.d(ImageLoader.TAG, "mPoolThreadHandler Looper.prepare()");
                ImageLoader.this.mPoolThreadHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        AppLog.d(ImageLoader.TAG, "mPoolThreadHandler acquire()");
                        try {
                            ImageLoader.this.mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                        }
                        AppLog.d(ImageLoader.TAG, "mPoolThreadHandler getTask()");
                        Runnable runnable = ImageLoader.this.getTask();
                        if (runnable != null) {
                            ImageLoader.this.mThreadPool.execute(runnable);
                        }
                    }
                };
                ImageLoader.this.mSemaphorePoolThreadHandler.release();
                AppLog.d(ImageLoader.TAG, "mPoolThreadHandler Looper.loop()");
                Looper.loop();
            }
        };
        this.mPoolThread.start();
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEAFULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    public void loadImage(String path, ImageView imageView, LoadImageType loadImageType) {
        if (this.mUIHandler == null) {
            this.mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    ImgBeanHolder holder = msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;
                    AppLog.i(ImageLoader.TAG, "handleMessage bm=" + bm + " path=" + path);
                    if (imageview.getTag().toString().equals(path)) {
                        AppLog.i(ImageLoader.TAG, "handleMessage setImageBitmap");
                        imageview.setImageBitmap(bm);
                    }
                }
            };
        }
        Bitmap bm = getBitmapFromLruCache(path);
        AppLog.i(TAG, "loadImage bm=" + bm);
        if (bm != null) {
            refreashBitmap(path, imageView, bm);
        } else {
            addTask(buildTask(path, imageView, loadImageType));
        }
    }

    public void loadImage(String path, LoadImageType loadImageType, final UpdateImageViewListener updateImageViewListener) {
        this.updateImageViewListener = updateImageViewListener;
        if (this.mUIHandler == null) {
            this.mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    ImgBeanHolder holder = msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    updateImageViewListener.onBitmapLoadComplete(holder.path, bm);
                }
            };
        }
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            refreashBitmap(path, null, bm);
        } else {
            addTask(buildTask(path, null, loadImageType));
        }
    }

    public void clearTasksQueue() {
        AppLog.d(TAG, "mTaskQueue.size() = " + this.mTaskQueue.size());
        if (this.mTaskQueue != null && this.mTaskQueue.size() > 0) {
            this.mTaskQueue.clear();
        }
    }

    private Runnable buildTask(final String path, final ImageView imageView, final LoadImageType loadImageType) {
        return new Runnable() {
            public void run() {
                Bitmap bm = ImageLoader.this.getBitmap(path, loadImageType);
                ImageLoader.this.addBitmapToLruCache(path, bm);
                ImageLoader.this.refreashBitmap(path, imageView, bm);
                ImageLoader.this.mSemaphoreThreadPool.release();
                AppLog.d(ImageLoader.TAG, "mPoolThreadHandler release()");
            }
        };
    }

    private Bitmap getBitmap(String path, LoadImageType loadImageType) {
        Bitmap newBitmap = null;
        if (loadImageType == LoadImageType.LOCAL_PHOTO) {
            newBitmap = BitmapTools.getImageByPath(path, BitmapTools.THUMBNAIL_WIDTH, BitmapTools.THUMBNAIL_WIDTH);
        } else if (loadImageType == LoadImageType.LOCAL_VIDEO) {
            newBitmap = ThumbnailOperation.getlocalVideoWallThumbnail(path);
        } else if (loadImageType == LoadImageType.CAMERA_VIDEO) {
        }
        AppLog.i(TAG, "getBitmap path=" + path);
        return newBitmap;
    }

    private Runnable getTask() {
        if (this.mTaskQueue.size() <= 0) {
            return null;
        }
        if (this.mType == Type.FIFO) {
            return (Runnable) this.mTaskQueue.removeFirst();
        }
        if (this.mType == Type.LIFO) {
            return (Runnable) this.mTaskQueue.removeLast();
        }
        return null;
    }

    public String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int length = bytes.length;
        for (int i = 0; i < length; i += DEAFULT_THREAD_COUNT) {
            String tmp = Integer.toHexString(bytes[i] & ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED);
            if (tmp.length() == DEAFULT_THREAD_COUNT) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString();
    }

    private void refreashBitmap(String path, ImageView imageView, Bitmap bm) {
        AppLog.i(TAG, "refreashBitmap path=" + path);
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        message.obj = holder;
        this.mUIHandler.sendMessage(message);
    }

    protected void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null && bm != null && path != null) {
            AppLog.d("test", "addBitmapToLruCache path=" + path);
            AppLog.d("test", "addBitmapToLruCache bitmap=" + bm);
            this.mLruCache.put(path, bm);
        }
    }

    private synchronized void addTask(Runnable runnable) {
        this.mTaskQueue.add(runnable);
        try {
            if (this.mPoolThreadHandler == null) {
                this.mSemaphorePoolThreadHandler.acquire();
            }
        } catch (InterruptedException e) {
        }
        AppLog.d(TAG, " addTask mTaskQueue size=" + this.mTaskQueue.size() + " mPoolThreadHandler=" + this.mPoolThreadHandler);
        this.mPoolThreadHandler.sendEmptyMessage(4386);
        Message message = Message.obtain();
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public Bitmap getBitmapFromLruCache(String key, int position) {
        return (Bitmap) this.mLruCache.get(key);
    }

    public Bitmap getBitmapFromLruCache(String key) {
        return (Bitmap) this.mLruCache.get(key);
    }

    public void clearCache() {
        this.mLruCache.evictAll();
        System.gc();
    }
}
