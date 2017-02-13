package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import com.icatch.ismartdv2016.Adapter.LocalVideoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.LocalVideoWallListAdapter;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.LimitQueue;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.PhotoWallPreviewType;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnAddAsytaskListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.ThumbnailGetting.ThumbnailOperation;
import com.icatch.ismartdv2016.Tools.FileOpertion.MFileTools;
import com.icatch.ismartdv2016.Tools.LruCacheTool;
import com.icatch.ismartdv2016.View.Interface.LocalVideoWallView;
import com.icatch.wificam.customer.ICatchWificamAssist;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LocalVideoWallPresenter extends BasePresenter {
    private static int NUM_COLUMNS = 4;
    private static int section = 1;
    private String TAG = "LocalVideoWallPresenter";
    private Activity activity;
    private LimitQueue<Asytask> asytaskList;
    List<File> fileList;
    private OnScrollListener gridViewOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            AppLog.i(LocalVideoWallPresenter.this.TAG, "onScrollStateChanged scrollState=" + scrollState);
            if (scrollState == 0) {
                AppLog.i(LocalVideoWallPresenter.this.TAG, "onScrollStateChanged firstVisibleItem=" + LocalVideoWallPresenter.this.mFirstVisibleItem + " visibleItemCount=" + LocalVideoWallPresenter.this.mVisibleItemCount);
                if (LocalVideoWallPresenter.this.asytaskList != null && LocalVideoWallPresenter.this.asytaskList.size() > 0) {
                    ((Asytask) LocalVideoWallPresenter.this.asytaskList.poll()).execute(new String[0]);
                }
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            AppLog.i(LocalVideoWallPresenter.this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount + " isFirstEnterThisActivity=" + LocalVideoWallPresenter.this.isFirstEnterThisActivity);
            LocalVideoWallPresenter.this.mFirstVisibleItem = firstVisibleItem;
            LocalVideoWallPresenter.this.mVisibleItemCount = visibleItemCount;
            if (LocalVideoWallPresenter.this.isFirstEnterThisActivity && visibleItemCount > 0 && LocalVideoWallPresenter.this.asytaskList != null && LocalVideoWallPresenter.this.asytaskList.size() > 0) {
                ((Asytask) LocalVideoWallPresenter.this.asytaskList.poll()).execute(new String[0]);
                LocalVideoWallPresenter.this.isFirstEnterThisActivity = false;
            }
        }
    };
    private int height;
    private boolean isFirstEnterThisActivity = true;
    private PhotoWallPreviewType layoutType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
    private OnScrollListener listViewOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            AppLog.i(LocalVideoWallPresenter.this.TAG, "onScrollStateChanged");
            if (scrollState == 0) {
                AppLog.i(LocalVideoWallPresenter.this.TAG, "onScrollStateChanged firstVisibleItem=" + LocalVideoWallPresenter.this.mFirstVisibleItem + " visibleItemCount=" + LocalVideoWallPresenter.this.mVisibleItemCount);
                LocalVideoWallPresenter.this.asytaskList.clear();
                LocalVideoWallPresenter.this.loadBitmaps(LocalVideoWallPresenter.this.mFirstVisibleItem, LocalVideoWallPresenter.this.mVisibleItemCount);
                return;
            }
            LocalVideoWallPresenter.this.asytaskList.clear();
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            AppLog.i(LocalVideoWallPresenter.this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem);
            if (firstVisibleItem != LocalVideoWallPresenter.this.topVisiblePosition) {
                LocalVideoWallPresenter.this.topVisiblePosition = firstVisibleItem;
                String fileDate = ((LocalPbItemInfo) LocalVideoWallPresenter.this.mGirdList.get(firstVisibleItem)).getFileDate();
                AppLog.i(LocalVideoWallPresenter.this.TAG, "fileDate=" + fileDate);
                LocalVideoWallPresenter.this.localVideoWallView.setListViewHeaderText(fileDate);
            }
            LocalVideoWallPresenter.this.mFirstVisibleItem = firstVisibleItem;
            LocalVideoWallPresenter.this.mVisibleItemCount = visibleItemCount;
            if (LocalVideoWallPresenter.this.isFirstEnterThisActivity && visibleItemCount > 0) {
                LocalVideoWallPresenter.this.loadBitmaps(firstVisibleItem, visibleItemCount);
                LocalVideoWallPresenter.this.isFirstEnterThisActivity = false;
            }
        }
    };
    private LocalVideoWallGridAdapter localVideoWallGridAdapter;
    private LocalVideoWallListAdapter localVideoWallListAdapter;
    private LocalVideoWallView localVideoWallView;
    private int mFirstVisibleItem;
    private List<LocalPbItemInfo> mGirdList;
    private LruCache<String, Bitmap> mLruCache = LruCacheTool.getInstance().getLruCache();
    private int mVisibleItemCount;
    private MyCamera myCamera;
    private Map<String, Integer> sectionMap = new HashMap();
    private int topVisiblePosition = -1;
    private int width;

    class Asytask extends AsyncTask<String, Integer, Bitmap> {
        String filePath;

        public Asytask(String path) {
            this.filePath = path;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bm = LruCacheTool.getInstance().getBitmapFromLruCache(this.filePath);
            AppLog.d(LocalVideoWallPresenter.this.TAG, "Asytask doInBackground filePath=" + this.filePath + " bm=" + bm);
            if (bm != null) {
                return bm;
            }
            bm = ThumbnailOperation.getlocalVideoWallThumbnail(this.filePath);
            LruCacheTool.getInstance().addBitmapToLruCache(this.filePath, bm);
            return bm;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                AppLog.d(LocalVideoWallPresenter.this.TAG, "Asytask onPostExecute result is null");
                return;
            }
            ImageView imageView;
            AppLog.d(LocalVideoWallPresenter.this.TAG, "Asytask onPostExecute result size=" + result.getByteCount());
            if (LocalVideoWallPresenter.this.layoutType == PhotoWallPreviewType.PREVIEW_TYPE_GRID) {
                imageView = (ImageView) LocalVideoWallPresenter.this.localVideoWallView.gridViewFindViewWithTag(this.filePath);
            } else {
                imageView = (ImageView) LocalVideoWallPresenter.this.localVideoWallView.listViewFindViewWithTag(this.filePath);
            }
            AppLog.i(LocalVideoWallPresenter.this.TAG, "loadBitmaps filePath=" + this.filePath + "result.isRecycled=" + result.isRecycled() + " imageView=" + imageView);
            if (!(imageView == null || result.isRecycled())) {
                imageView.setImageBitmap(result);
            }
            if (LocalVideoWallPresenter.this.asytaskList != null && LocalVideoWallPresenter.this.asytaskList.size() > 0) {
                ((Asytask) LocalVideoWallPresenter.this.asytaskList.poll()).execute(new String[0]);
            }
        }
    }

    public LocalVideoWallPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.asytaskList = new LimitQueue(SystemInfo.getWindowVisibleCountMax(NUM_COLUMNS));
        initCamera();
    }

    public void setView(LocalVideoWallView localVideoWallView) {
        this.localVideoWallView = localVideoWallView;
        initCfg();
    }

    public boolean initCamera() {
        this.myCamera = new MyCamera();
        if (this.myCamera.getSDKsession().prepareSession("192.168.1.1", false)) {
            GlobalInfo.getInstance().setCurrentCamera(this.myCamera);
            AppLog.i(this.TAG, "Set CurrentCamera");
            this.myCamera.initCameraForLocalPB();
            return true;
        }
        AppLog.e(this.TAG, "prepareSession failed!");
        return false;
    }

    public void destroyCamera() {
        this.myCamera.destroyCamera();
    }

    private List<LocalPbItemInfo> getVideoList() {
        List<LocalPbItemInfo> tempList = new ArrayList();
        this.width = SystemInfo.getMetrics().widthPixels;
        this.fileList = MFileTools.getVideosOrderByDate(Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH);
        for (int ii = 0; ii < this.fileList.size(); ii++) {
            long time = ((File) this.fileList.get(ii)).lastModified();
            AppLog.i(this.TAG, "file.lastModified()" + time);
            String fileDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
            if (this.sectionMap.containsKey(fileDate)) {
                tempList.add(new LocalPbItemInfo((File) this.fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
            } else {
                this.sectionMap.put(fileDate, Integer.valueOf(section));
                tempList.add(new LocalPbItemInfo((File) this.fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
                section++;
            }
        }
        return tempList;
    }

    public void loadLocalVideoWall() {
        if (this.mGirdList == null || this.mGirdList.size() < 0) {
            this.mGirdList = getVideoList();
        }
        GlobalInfo.getInstance().localVideoList = this.mGirdList;
        if (this.layoutType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            this.localVideoWallView.setGridViewVisibility(8);
            this.localVideoWallView.setListViewVisibility(0);
            this.localVideoWallListAdapter = new LocalVideoWallListAdapter(this.activity, this.mGirdList, this.mLruCache);
            this.localVideoWallView.setListViewAdapter(this.localVideoWallListAdapter);
            this.localVideoWallView.setListViewOnScrollListener(this.listViewOnScrollListener);
            return;
        }
        this.width = SystemInfo.getMetrics().widthPixels;
        this.localVideoWallView.setGridViewVisibility(0);
        this.localVideoWallView.setListViewVisibility(8);
        AppLog.i(this.TAG, "width=" + 0);
        this.localVideoWallGridAdapter = new LocalVideoWallGridAdapter(this.activity, this.mGirdList, this.width, this.mLruCache, new OnAddAsytaskListener() {
            public void addAsytask(int position) {
                AppLog.d(LocalVideoWallPresenter.this.TAG, "addAsytask position" + position);
                LocalVideoWallPresenter.this.asytaskList.offer(new Asytask(((LocalPbItemInfo) LocalVideoWallPresenter.this.mGirdList.get(position)).getFilePath()));
            }
        });
        this.localVideoWallView.setGridViewAdapter(this.localVideoWallGridAdapter);
        this.localVideoWallView.setGridViewOnScrollListener(this.gridViewOnScrollListener);
    }

    public void changePreviewType() {
        if (this.layoutType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            this.layoutType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
            this.localVideoWallView.setMenuPreviewTypeIcon(R.drawable.ic_view_grid_white_24dp);
        } else {
            this.layoutType = PhotoWallPreviewType.PREVIEW_TYPE_LIST;
            this.localVideoWallView.setMenuPreviewTypeIcon(R.drawable.ic_view_list_white_24dp);
        }
        loadLocalVideoWall();
    }

    public void redirectToAnotherActivity(Context context, Class<?> cls, int position) {
        AppLog.i(this.TAG, "redirectToAnotherActivity position=" + position);
        clealAsytaskList();
        final String videoPath = ((LocalPbItemInfo) this.mGirdList.get(position)).getFilePath();
        final Uri uri = Uri.fromFile(new File(videoPath));
        this.isFirstEnterThisActivity = true;
        final int i = position;
        final Class<?> cls2 = cls;
        final Context context2 = context;
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (ICatchWificamAssist.getInstance().supportLocalPlay(videoPath)) {
                    AppLog.d(LocalVideoWallPresenter.this.TAG, "supportLocalPlay videoPath=" + videoPath);
                    Intent intent = new Intent();
                    intent.putExtra("curfilePath", videoPath);
                    intent.putExtra("curfilePosition", i);
                    AppLog.i(LocalVideoWallPresenter.this.TAG, "intent:start redirectToAnotherActivity class =" + cls2.getName());
                    intent.setClass(context2, cls2);
                    context2.startActivity(intent);
                    return;
                }
                AppLog.d(LocalVideoWallPresenter.this.TAG, "not supportLocalPlay");
                intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(uri, "video/mp4");
                context2.startActivity(intent);
            }
        }, 300);
    }

    void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        for (int ii = firstVisibleItem; ii < firstVisibleItem + visibleItemCount; ii++) {
            if (ii < this.mGirdList.size()) {
                this.asytaskList.offer(new Asytask(((LocalPbItemInfo) this.mGirdList.get(ii)).getFilePath()));
                AppLog.i(this.TAG, "add task loadBitmaps ii=" + ii);
            }
        }
        if (this.asytaskList != null && this.asytaskList.size() > 0) {
            ((Asytask) this.asytaskList.poll()).execute(new String[0]);
        }
    }

    public void clearResource() {
        this.asytaskList.clear();
    }

    public void clealAsytaskList() {
        AppLog.d(this.TAG, "clealAsytaskList");
        if (this.asytaskList != null && this.asytaskList.size() > 0) {
            AppLog.d(this.TAG, "clealAsytaskList size=" + this.asytaskList.size());
            this.asytaskList.clear();
        }
    }
}
