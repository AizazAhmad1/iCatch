package com.icatch.ismartdv2016.Model;

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
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MultiPbThumbnailLoader {
    private static final int DEAFULT_THREAD_COUNT = 1;
    private static final String TAG = "ImageLoader";
    private static MultiPbThumbnailLoader mInstance;
    private FileOperation fileOperation = FileOperation.getInstance();
    private boolean isDiskCacheEnable = true;
    private LruCache<Integer, Bitmap> mLruCache;
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
        int fileHandle;
        ImageView imageView;

        private ImgBeanHolder() {
        }
    }

    public enum Type {
        FIFO,
        LIFO
    }

    private MultiPbThumbnailLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        initBackThread();
        int cacheMemory = ((int) Runtime.getRuntime().maxMemory()) / 8;
        AppLog.d("cacheMemory", " cacheMemory=" + cacheMemory);
        this.mLruCache = new LruCache<Integer, Bitmap>(cacheMemory) {
            protected int sizeOf(Integer key, Bitmap value) {
                AppLog.d("cacheMemory", " value.getByteCount()=" + value.getByteCount());
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
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
                AppLog.d("1111", "mPoolThreadHandler Looper.prepare()");
                MultiPbThumbnailLoader.this.mPoolThreadHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        AppLog.d("1111", "mPoolThreadHandler acquire()");
                        try {
                            MultiPbThumbnailLoader.this.mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                        }
                        AppLog.d("1111", "mPoolThreadHandler getTask()");
                        Runnable runnable = MultiPbThumbnailLoader.this.getTask();
                        if (runnable != null) {
                            MultiPbThumbnailLoader.this.mThreadPool.execute(runnable);
                        }
                    }
                };
                MultiPbThumbnailLoader.this.mSemaphorePoolThreadHandler.release();
                AppLog.d("1111", "mPoolThreadHandler Looper.loop()");
                Looper.loop();
            }
        };
        this.mPoolThread.start();
    }

    public static MultiPbThumbnailLoader getInstance() {
        if (mInstance == null) {
            synchronized (MultiPbThumbnailLoader.class) {
                if (mInstance == null) {
                    mInstance = new MultiPbThumbnailLoader(DEAFULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    public static MultiPbThumbnailLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (MultiPbThumbnailLoader.class) {
                if (mInstance == null) {
                    mInstance = new MultiPbThumbnailLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    public void loadImage(int fileHandle, LoadImageType loadImageType, UpdateImageViewListener updateImageViewListener) {
        this.updateImageViewListener = updateImageViewListener;
        if (this.mUIHandler == null) {
            this.mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    ImgBeanHolder holder = msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    int fileHandle = holder.fileHandle;
                }
            };
        }
        Bitmap bm = getBitmapFromLruCache(fileHandle);
        if (bm != null) {
            refreashBitmap(fileHandle, null, bm);
        } else {
            addTask(buildTask(fileHandle, null, loadImageType));
        }
    }

    public void clearTasksQueue() {
        AppLog.d("1111", "mTaskQueue.size() = " + this.mTaskQueue.size());
        if (this.mTaskQueue != null && this.mTaskQueue.size() > 0) {
            this.mTaskQueue.clear();
        }
    }

    private Runnable buildTask(final int fileHandle, final ImageView imageView, final LoadImageType loadImageType) {
        return new Runnable() {
            public void run() {
                Bitmap bm = MultiPbThumbnailLoader.this.getBitmap(fileHandle, loadImageType);
                MultiPbThumbnailLoader.this.addBitmapToLruCache(fileHandle, bm);
                MultiPbThumbnailLoader.this.refreashBitmap(fileHandle, imageView, bm);
                MultiPbThumbnailLoader.this.mSemaphoreThreadPool.release();
                AppLog.d("1111", "mPoolThreadHandler release()");
            }
        };
    }

    private Bitmap getBitmap(int fileHandle, LoadImageType loadImageType) {
        return null;
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

    private void refreashBitmap(int fileHandle, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.fileHandle = fileHandle;
        holder.imageView = imageView;
        message.obj = holder;
        this.mUIHandler.sendMessage(message);
    }

    protected void addBitmapToLruCache(int fileHandle, Bitmap bm) {
        if (getBitmapFromLruCache(fileHandle) == null && bm != null && fileHandle != 0) {
            AppLog.d("test", "addBitmapToLruCache fileHandle=" + fileHandle);
            AppLog.d("test", "addBitmapToLruCache bitmap=" + bm);
            this.mLruCache.put(Integer.valueOf(fileHandle), bm);
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
        AppLog.d("1111", " addTask");
        this.mPoolThreadHandler.sendEmptyMessage(272);
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

    public Bitmap getBitmapFromLruCache(int key, int position) {
        return (Bitmap) this.mLruCache.get(Integer.valueOf(key));
    }

    public Bitmap getBitmapFromLruCache(int key) {
        return (Bitmap) this.mLruCache.get(Integer.valueOf(key));
    }
}
