package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.icatch.ismartdv2016.Adapter.LocalPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.LocalPhotoWallListAdapter;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.FileType;
import com.icatch.ismartdv2016.BaseItems.LimitQueue;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.PhotoWallPreviewType;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnAddAsytaskListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.Tools.BitmapTools;
import com.icatch.ismartdv2016.Tools.FileOpertion.MFileTools;
import com.icatch.ismartdv2016.View.Interface.LocalPhotoWallView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalPhotoWallPresenter extends BasePresenter {
    private static int NUM_COLUMNS = 4;
    private String TAG = "LocalPhotoWallPresenter";
    private Activity activity;
    private LimitQueue<Asytask> asytaskLimitQueue;
    private boolean isFirstEnterThisActivity = true;
    private PhotoWallPreviewType layoutType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
    private LocalPhotoWallGridAdapter localLocalPhotoWallGridAdapter;
    private LocalPhotoWallListAdapter localPhotoWallListAdapter;
    private LocalPhotoWallView localPhotoWallView;
    private int mFirstVisibleItem = 0;
    private LruCache<String, Bitmap> mLruCache;
    private int mVisibleItemCount;
    private List<LocalPbItemInfo> photoList;
    private Map<String, Integer> sectionMap = new HashMap();
    private int topVisiblePosition = -1;
    private int width;

    public class Asytask extends AsyncTask<String, Integer, Bitmap> {
        String filePath;

        public Asytask(String path) {
            this.filePath = path;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bm = LocalPhotoWallPresenter.this.getBitmapFromLruCache(this.filePath);
            if (bm != null) {
                return bm;
            }
            bm = BitmapTools.getImageByPath(this.filePath, BitmapTools.THUMBNAIL_WIDTH, BitmapTools.THUMBNAIL_WIDTH);
            LocalPhotoWallPresenter.this.addBitmapToLruCache(this.filePath, bm);
            return bm;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap result) {
            ImageView imageView;
            if (LocalPhotoWallPresenter.this.layoutType == PhotoWallPreviewType.PREVIEW_TYPE_GRID) {
                imageView = (ImageView) LocalPhotoWallPresenter.this.localPhotoWallView.gridViewFindViewWithTag(this.filePath);
            } else {
                imageView = (ImageView) LocalPhotoWallPresenter.this.localPhotoWallView.listViewFindViewWithTag(this.filePath);
            }
            AppLog.i(LocalPhotoWallPresenter.this.TAG, "loadBitmaps imageView=" + imageView);
            if (imageView != null) {
                imageView.setImageBitmap(result);
            }
            if (LocalPhotoWallPresenter.this.asytaskLimitQueue != null && LocalPhotoWallPresenter.this.asytaskLimitQueue.size() > 0) {
                ((Asytask) LocalPhotoWallPresenter.this.asytaskLimitQueue.poll()).execute(new String[0]);
            }
        }

        protected void onPreExecute() {
        }

        protected void onCancelled() {
        }
    }

    public LocalPhotoWallPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        initData();
    }

    public void setView(LocalPhotoWallView localPhotoWallView) {
        this.localPhotoWallView = localPhotoWallView;
        initCfg();
    }

    private List<LocalPbItemInfo> getPhotoList() {
        String filePath = Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH;
        int section = 1;
        List<LocalPbItemInfo> photoList = new ArrayList();
        List<File> fileList = MFileTools.getPhotosOrderByDate(filePath);
        AppLog.i(this.TAG, "fileList=" + fileList);
        Log.d(this.TAG, "fileList=" + fileList);
        Log.d(this.TAG, "fileList size=" + fileList.size());
        AppLog.i(this.TAG, "fileList size=" + fileList.size());
        for (int ii = 0; ii < fileList.size(); ii++) {
            long time = ((File) fileList.get(ii)).lastModified();
            AppLog.i(this.TAG, "file.lastModified()" + time);
            String fileDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
            if (this.sectionMap.containsKey(fileDate)) {
                photoList.add(new LocalPbItemInfo((File) fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
            } else {
                this.sectionMap.put(fileDate, Integer.valueOf(section));
                photoList.add(new LocalPbItemInfo((File) fileList.get(ii), section));
                section++;
            }
        }
        return photoList;
    }

    public void loadLocalPhotoWall() {
        if (this.photoList == null) {
            this.photoList = getPhotoList();
        }
        if (this.photoList != null && this.photoList.size() > 0) {
            String fileDate = ((LocalPbItemInfo) this.photoList.get(0)).getFileDate();
            AppLog.i(this.TAG, "fileDate=" + fileDate);
            this.localPhotoWallView.setListViewHeaderText(fileDate);
        }
        GlobalInfo.getInstance().localPhotoList = this.photoList;
        this.localPhotoWallView.setListViewSelection(this.mFirstVisibleItem);
        this.localPhotoWallView.setGridViewSelection(this.mFirstVisibleItem);
        this.isFirstEnterThisActivity = true;
        if (this.layoutType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            this.localPhotoWallView.setGridViewVisibility(8);
            this.localPhotoWallView.setListViewVisibility(0);
            this.localPhotoWallListAdapter = new LocalPhotoWallListAdapter(this.activity, this.photoList, this.mLruCache, FileType.FILE_PHOTO);
            this.localPhotoWallView.setListViewAdapter(this.localPhotoWallListAdapter);
            return;
        }
        this.width = SystemInfo.getMetrics().widthPixels;
        this.localPhotoWallView.setGridViewVisibility(0);
        this.localPhotoWallView.setListViewVisibility(8);
        AppLog.i(this.TAG, "width=" + 0);
        this.localLocalPhotoWallGridAdapter = new LocalPhotoWallGridAdapter(this.activity, this.photoList, this.width, this.mLruCache, FileType.FILE_PHOTO, new OnAddAsytaskListener() {
            public void addAsytask(int position) {
                AppLog.d(LocalPhotoWallPresenter.this.TAG, "addAsytask position=" + position);
                LocalPhotoWallPresenter.this.asytaskLimitQueue.offer(new Asytask(((LocalPbItemInfo) LocalPhotoWallPresenter.this.photoList.get(position)).getFilePath()));
            }
        });
        this.localPhotoWallView.setGridViewAdapter(this.localLocalPhotoWallGridAdapter);
    }

    public void changePreviewType() {
        if (this.layoutType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            this.layoutType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
            this.localPhotoWallView.setMenuPhotoWallTypeIcon(R.drawable.ic_view_grid_white_24dp);
        } else {
            this.layoutType = PhotoWallPreviewType.PREVIEW_TYPE_LIST;
            this.localPhotoWallView.setMenuPhotoWallTypeIcon(R.drawable.ic_view_list_white_24dp);
        }
        loadLocalPhotoWall();
    }

    public void listViewLoadThumbnails(int scrollState) {
        AppLog.i(this.TAG, "onScrollStateChanged");
        if (scrollState == 0) {
            AppLog.i(this.TAG, "onScrollStateChanged firstVisibleItem=" + this.mFirstVisibleItem + " visibleItemCount=" + this.mVisibleItemCount);
            this.asytaskLimitQueue.clear();
            loadBitmaps(this.mFirstVisibleItem, this.mVisibleItemCount);
            return;
        }
        this.asytaskLimitQueue.clear();
    }

    public void listViewLoadOnceThumbnails(int firstVisibleItem, int visibleItemCount) {
        AppLog.i(this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem);
        if (firstVisibleItem != this.topVisiblePosition) {
            this.topVisiblePosition = firstVisibleItem;
            if (this.photoList != null && this.photoList.size() > 0) {
                String fileDate = ((LocalPbItemInfo) this.photoList.get(firstVisibleItem)).getFileDate();
                AppLog.i(this.TAG, "fileDate=" + fileDate);
                this.localPhotoWallView.setListViewHeaderText(fileDate);
            }
        }
        this.mFirstVisibleItem = firstVisibleItem;
        this.mVisibleItemCount = visibleItemCount;
        if (this.isFirstEnterThisActivity && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount);
            this.isFirstEnterThisActivity = false;
        }
    }

    public void gridViewLoadThumbnails(int scrollState) {
        AppLog.i(this.TAG, "onScrollStateChanged scrollState=" + scrollState);
        if (scrollState == 0) {
            AppLog.i(this.TAG, "onScrollStateChanged firstVisibleItem=" + this.mFirstVisibleItem + " visibleItemCount=" + this.mVisibleItemCount);
            AppLog.i(this.TAG, "onScrollStateChanged asytaskLimitQueue size=" + this.asytaskLimitQueue.size());
            if (this.asytaskLimitQueue != null && this.asytaskLimitQueue.size() > 0) {
                ((Asytask) this.asytaskLimitQueue.poll()).execute(new String[0]);
            }
        }
    }

    public void gridViewLoadOnceThumbnails(int firstVisibleItem, int visibleItemCount) {
        AppLog.i(this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
        if (this.isFirstEnterThisActivity && visibleItemCount > 0 && this.asytaskLimitQueue != null && this.asytaskLimitQueue.size() > 0) {
            ((Asytask) this.asytaskLimitQueue.poll()).execute(new String[0]);
            this.isFirstEnterThisActivity = false;
        }
    }

    void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        AppLog.i(this.TAG, "add task loadBitmaps firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
        int ii = firstVisibleItem;
        while (ii < firstVisibleItem + visibleItemCount) {
            if (this.photoList != null && this.photoList.size() > 0 && ii < this.photoList.size()) {
                this.asytaskLimitQueue.offer(new Asytask(((LocalPbItemInfo) this.photoList.get(ii)).getFilePath()));
                AppLog.i(this.TAG, "add task loadBitmaps ii=" + ii);
            }
            ii++;
        }
        if (this.asytaskLimitQueue != null && this.asytaskLimitQueue.size() > 0) {
            ((Asytask) this.asytaskLimitQueue.poll()).execute(new String[0]);
        }
    }

    public void redirectToAnotherActivity(Context context, Class<?> cls, int position) {
        AppLog.i(this.TAG, "redirectToAnotherActivity position=" + position);
        clealAsytaskList();
        Intent intent = new Intent();
        this.isFirstEnterThisActivity = true;
        intent.putExtra("curfilePosition", position);
        AppLog.i(this.TAG, "intent:start redirectToAnotherActivity class =" + cls.getName());
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public void clearResource() {
        this.asytaskLimitQueue.clear();
        this.mLruCache.evictAll();
    }

    private void initData() {
        this.asytaskLimitQueue = new LimitQueue(SystemInfo.getWindowVisibleCountMax(NUM_COLUMNS));
        int cacheMemory = ((int) Runtime.getRuntime().maxMemory()) / 8;
        AppLog.d("cacheMemory", " cacheMemory=" + cacheMemory);
        this.mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    oldValue.recycle();
                }
            }
        };
    }

    public Bitmap getBitmapFromLruCache(String key) {
        return (Bitmap) this.mLruCache.get(key);
    }

    protected void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null && bm != null && path != null) {
            AppLog.d("test", "addBitmapToLruCache path=" + path);
            AppLog.d("test", "addBitmapToLruCache bitmap=" + bm);
            this.mLruCache.put(path, bm);
        }
    }

    public void clealAsytaskList() {
        AppLog.d(this.TAG, "clealAsytaskList");
        if (this.asytaskLimitQueue != null && this.asytaskLimitQueue.size() > 0) {
            AppLog.d(this.TAG, "clealAsytaskList size=" + this.asytaskLimitQueue.size());
            this.asytaskLimitQueue.clear();
        }
    }
}
