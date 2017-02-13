package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.LruCache;
import android.view.View;
import com.icatch.ismartdv2016.Adapter.LocalPhotoPbViewPagerAdapter;
import com.icatch.ismartdv2016.Adapter.LocalPhotoPbViewPagerAdapter.OnPhotoTapListener;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.ExtendComponent.ProgressWheel;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.Tools.BitmapTools;
import com.icatch.ismartdv2016.View.Interface.LocalPhotoPbView;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uk.co.senab.photoview.PhotoView;

public class LocalPhotoPbPresenter extends BasePresenter {
    private static final int DIRECTION_LEFT = 2;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_UNKNOWN = 4;
    private String TAG = "LocalPhotoPbPresenter";
    private Activity activity;
    LinkedList<Asytask> asytaskList;
    Asytask curAsytask;
    private int curPhotoIdx;
    private ExecutorService executor;
    private Handler handler;
    private boolean isScrolling = false;
    private int lastItem = -1;
    public List<LocalPbItemInfo> localPhotoList = GlobalInfo.getInstance().localPhotoList;
    private LruCache<String, Bitmap> mLruCache;
    private LocalPhotoPbView photoPbView;
    private int slideDirection = DIRECTION_RIGHT;
    private int tempLastItem = -1;
    private List<View> viewList;
    private LocalPhotoPbViewPagerAdapter viewPagerAdapter;

    class Asytask extends AsyncTask<String, Integer, Bitmap> {
        File file;
        String filePath;
        boolean isZoom = false;
        int position;

        public Asytask(LocalPbItemInfo localPbItemInfo, int position) {
            this.file = localPbItemInfo.file;
            this.filePath = this.file.getPath();
            this.position = position;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bm = LocalPhotoPbPresenter.this.getBitmapFromLruCache(this.filePath);
            if (bm != null) {
                return bm;
            }
            bm = BitmapTools.getImageByPath(this.filePath, 1080, 720);
            if (bm != null) {
                AppLog.d(LocalPhotoPbPresenter.this.TAG, "11 position=" + this.position + "filePath=" + this.filePath + " bm size=" + bm.getByteCount());
            }
            LocalPhotoPbPresenter.this.addBitmapToLruCache(this.filePath, bm);
            return bm;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                View view = (View) LocalPhotoPbPresenter.this.viewList.get(this.position);
                if (view != null) {
                    PhotoView photoView = (PhotoView) view.findViewById(R.id.photo);
                    ProgressWheel progressBar = (ProgressWheel) view.findViewById(R.id.progress_wheel);
                    AppLog.d(LocalPhotoPbPresenter.this.TAG, "onPostExecute position=" + this.position + " filePath=" + this.filePath + " size=" + result.getByteCount() + " result.isRecycled()=" + result.isRecycled() + " photoView=" + photoView);
                    if (!(photoView == null || result.isRecycled())) {
                        photoView.setImageBitmap(result);
                    }
                    if (progressBar != null) {
                        progressBar.setVisibility(8);
                    }
                }
            }
            if (LocalPhotoPbPresenter.this.asytaskList != null && LocalPhotoPbPresenter.this.asytaskList.size() > 0) {
                LocalPhotoPbPresenter.this.curAsytask = (Asytask) LocalPhotoPbPresenter.this.asytaskList.removeFirst();
                LocalPhotoPbPresenter.this.curAsytask.execute(new String[0]);
            }
        }
    }

    private class DeleteThread implements Runnable {
        private DeleteThread() {
        }

        public void run() {
            LocalPhotoPbPresenter.this.curPhotoIdx = LocalPhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
            LocalPbItemInfo curFile = (LocalPbItemInfo) LocalPhotoPbPresenter.this.localPhotoList.get(LocalPhotoPbPresenter.this.curPhotoIdx);
            if (curFile.file.exists()) {
                curFile.file.delete();
            }
            LocalPhotoPbPresenter.this.handler.post(new Runnable() {
                public void run() {
                    MyProgressDialog.closeProgressDialog();
                    LocalPhotoPbPresenter.this.viewList.remove(LocalPhotoPbPresenter.this.curPhotoIdx);
                    LocalPhotoPbPresenter.this.localPhotoList.remove(LocalPhotoPbPresenter.this.curPhotoIdx);
                    LocalPhotoPbPresenter.this.viewPagerAdapter.notifyDataSetChanged();
                    LocalPhotoPbPresenter.this.photoPbView.setViewPagerAdapter(LocalPhotoPbPresenter.this.viewPagerAdapter);
                    int photoNums = LocalPhotoPbPresenter.this.localPhotoList.size();
                    if (photoNums == 0) {
                        LocalPhotoPbPresenter.this.activity.finish();
                        return;
                    }
                    if (LocalPhotoPbPresenter.this.curPhotoIdx == photoNums) {
                        LocalPhotoPbPresenter.this.curPhotoIdx = LocalPhotoPbPresenter.this.curPhotoIdx - 1;
                    }
                    AppLog.d(LocalPhotoPbPresenter.this.TAG, "photoNums=" + photoNums + " curPhotoIdx=" + LocalPhotoPbPresenter.this.curPhotoIdx);
                    LocalPhotoPbPresenter.this.photoPbView.setViewPagerCurrentItem(LocalPhotoPbPresenter.this.curPhotoIdx);
                    LocalPhotoPbPresenter.this.ShowCurPageNum();
                    LocalPhotoPbPresenter.this.loadBitmaps(LocalPhotoPbPresenter.this.curPhotoIdx);
                }
            });
            AppLog.d(LocalPhotoPbPresenter.this.TAG, "end DeleteThread");
        }
    }

    private class MyViewPagerOnPagerChangeListener implements OnPageChangeListener {
        private MyViewPagerOnPagerChangeListener() {
        }

        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case LocalPhotoPbPresenter.DIRECTION_RIGHT /*1*/:
                    LocalPhotoPbPresenter.this.isScrolling = true;
                    LocalPhotoPbPresenter.this.tempLastItem = LocalPhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
                    return;
                case LocalPhotoPbPresenter.DIRECTION_LEFT /*2*/:
                    if (!(!LocalPhotoPbPresenter.this.isScrolling || LocalPhotoPbPresenter.this.tempLastItem == -1 || LocalPhotoPbPresenter.this.tempLastItem == LocalPhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem())) {
                        LocalPhotoPbPresenter.this.lastItem = LocalPhotoPbPresenter.this.tempLastItem;
                    }
                    LocalPhotoPbPresenter.this.curPhotoIdx = LocalPhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
                    LocalPhotoPbPresenter.this.isScrolling = false;
                    LocalPhotoPbPresenter.this.loadBitmaps(LocalPhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem());
                    LocalPhotoPbPresenter.this.ShowCurPageNum();
                    return;
                default:
                    return;
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (LocalPhotoPbPresenter.this.isScrolling) {
                if (LocalPhotoPbPresenter.this.lastItem > arg2) {
                    LocalPhotoPbPresenter.this.slideDirection = LocalPhotoPbPresenter.DIRECTION_RIGHT;
                } else if (LocalPhotoPbPresenter.this.lastItem < arg2) {
                    LocalPhotoPbPresenter.this.slideDirection = LocalPhotoPbPresenter.DIRECTION_LEFT;
                } else if (LocalPhotoPbPresenter.this.lastItem == arg2) {
                    LocalPhotoPbPresenter.this.slideDirection = LocalPhotoPbPresenter.DIRECTION_RIGHT;
                }
            }
            LocalPhotoPbPresenter.this.lastItem = arg2;
        }

        public void onPageSelected(int arg0) {
            LocalPhotoPbPresenter.this.ShowCurPageNum();
        }
    }

    public LocalPhotoPbPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.viewList = new LinkedList();
        this.handler = new Handler();
        initLruCache();
        this.slideDirection = DIRECTION_UNKNOWN;
    }

    public void setView(LocalPhotoPbView localPhotoPbView) {
        this.photoPbView = localPhotoPbView;
        initCfg();
    }

    private void initLruCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 16;
        AppLog.d(this.TAG, "initLruCache maxMemory=" + maxMemory);
        AppLog.d(this.TAG, "initLruCache cacheMemory=" + cacheMemory);
        this.mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            protected int sizeOf(String key, Bitmap value) {
                AppLog.d(LocalPhotoPbPresenter.this.TAG, "cacheMemory value.getByteCount()=" + value.getByteCount());
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    AppLog.d(LocalPhotoPbPresenter.this.TAG, "cacheMemory entryRemoved key=" + key);
                    oldValue.recycle();
                }
            }
        };
    }

    public Bitmap getBitmapFromLruCache(String fileName) {
        AppLog.d(this.TAG, "getBitmapFromLruCache filePath=" + fileName);
        return (Bitmap) this.mLruCache.get(fileName);
    }

    protected void addBitmapToLruCache(String fileName, Bitmap bm) {
        if (bm != null) {
            if (bm.getByteCount() > this.mLruCache.maxSize()) {
                AppLog.d(this.TAG, "addBitmapToLruCache greater than mLruCache size filePath=" + fileName);
            } else if (getBitmapFromLruCache(fileName) == null && bm != null && fileName != null) {
                AppLog.d(this.TAG, "addBitmapToLruCache filePath=" + fileName);
                this.mLruCache.put(fileName, bm);
            }
        }
    }

    public void loadImage() {
        String filePath = Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH;
        this.localPhotoList = GlobalInfo.getInstance().localPhotoList;
        this.curPhotoIdx = this.activity.getIntent().getExtras().getInt("curfilePosition");
        for (int ii = 0; ii < this.localPhotoList.size(); ii += DIRECTION_RIGHT) {
            this.viewList.add(ii, null);
        }
        this.viewPagerAdapter = new LocalPhotoPbViewPagerAdapter(this.activity, this.localPhotoList, this.viewList, this.mLruCache);
        this.viewPagerAdapter.setOnPhotoTapListener(new OnPhotoTapListener() {
            public void onPhotoTap() {
                LocalPhotoPbPresenter.this.showBar();
            }
        });
        this.photoPbView.setViewPagerAdapter(this.viewPagerAdapter);
        this.photoPbView.setViewPagerCurrentItem(this.curPhotoIdx);
        ShowCurPageNum();
        loadBitmaps(this.curPhotoIdx);
        this.photoPbView.setOnPageChangeListener(new MyViewPagerOnPagerChangeListener());
    }

    public void showBar() {
        boolean isShowBar;
        if (this.photoPbView.getTopBarVisibility() == 0) {
            isShowBar = true;
        } else {
            isShowBar = false;
        }
        AppLog.d(this.TAG, "showBar isShowBar=" + isShowBar);
        if (isShowBar) {
            this.photoPbView.setTopBarVisibility(8);
            this.photoPbView.setBottomBarVisibility(8);
            return;
        }
        this.photoPbView.setTopBarVisibility(0);
        this.photoPbView.setBottomBarVisibility(0);
    }

    void loadBitmaps(int curPhotoIdx) {
        AppLog.i(this.TAG, "add task loadBitmaps curPhotoIdx=" + curPhotoIdx);
        if (curPhotoIdx >= 0) {
            if (!(this.curAsytask == null || this.curAsytask.isCancelled())) {
                AppLog.i(this.TAG, "add task curAsytask cancel curAsytask position" + this.curAsytask.position);
                this.curAsytask.cancel(true);
            }
            if (this.asytaskList == null) {
                this.asytaskList = new LinkedList();
            } else {
                this.asytaskList.clear();
            }
            if (this.localPhotoList == null || this.localPhotoList.size() < 0) {
                AppLog.e(this.TAG, "localPhotoList is null or size < 0");
                return;
            }
            if (curPhotoIdx == 0) {
                this.asytaskList.add(new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx), curPhotoIdx));
                if (this.localPhotoList.size() > DIRECTION_RIGHT) {
                    this.asytaskList.add(new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx + DIRECTION_RIGHT), curPhotoIdx + DIRECTION_RIGHT));
                }
            } else if (curPhotoIdx == this.localPhotoList.size() - 1) {
                task1 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx), curPhotoIdx);
                task2 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx - 1), curPhotoIdx - 1);
                this.asytaskList.add(task1);
                this.asytaskList.add(task2);
            } else {
                AppLog.d(this.TAG, "loadBitmaps slideDirection=" + this.slideDirection);
                Asytask task3;
                if (this.slideDirection == DIRECTION_RIGHT) {
                    task1 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx), curPhotoIdx);
                    task2 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx - 1), curPhotoIdx - 1);
                    task3 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx + DIRECTION_RIGHT), curPhotoIdx + DIRECTION_RIGHT);
                    this.asytaskList.add(task1);
                    this.asytaskList.add(task2);
                    this.asytaskList.add(task3);
                } else {
                    task1 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx), curPhotoIdx);
                    task2 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx + DIRECTION_RIGHT), curPhotoIdx + DIRECTION_RIGHT);
                    task3 = new Asytask((LocalPbItemInfo) this.localPhotoList.get(curPhotoIdx - 1), curPhotoIdx - 1);
                    this.asytaskList.add(task1);
                    this.asytaskList.add(task2);
                    this.asytaskList.add(task3);
                }
            }
            if (this.asytaskList != null && this.asytaskList.size() > 0) {
                this.curAsytask = (Asytask) this.asytaskList.removeFirst();
                this.curAsytask.execute(new String[0]);
            }
        }
    }

    public void reloadBitmap() {
        this.photoPbView.setViewPagerAdapter(this.viewPagerAdapter);
        this.photoPbView.setViewPagerCurrentItem(this.curPhotoIdx);
        ShowCurPageNum();
        loadBitmaps(this.curPhotoIdx);
    }

    private void ShowCurPageNum() {
        this.photoPbView.setIndexInfoTxv((this.photoPbView.getViewPagerCurrentItem() + DIRECTION_RIGHT) + "/" + this.localPhotoList.size());
    }

    public void delete() {
        showDeleteEnsureDialog();
    }

    public void share() {
        int curPosition = this.photoPbView.getViewPagerCurrentItem();
        String photoPath = ((LocalPbItemInfo) this.localPhotoList.get(curPosition)).file.getPath();
        AppLog.d(this.TAG, "share curPosition=" + curPosition + " photoPath=" + photoPath);
        Uri imageUri = Uri.fromFile(new File(photoPath));
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.putExtra("android.intent.extra.STREAM", imageUri);
        shareIntent.setType("image/*");
        this.activity.startActivity(Intent.createChooser(shareIntent, this.activity.getResources().getString(R.string.gallery_share_to)));
    }

    public void info() {
        MyToast.show(this.activity, "info photo");
    }

    private void showDeleteEnsureDialog() {
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setTitle(R.string.image_delete_des);
        builder.setNegativeButton(R.string.gallery_delete, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MyProgressDialog.showProgressDialog(LocalPhotoPbPresenter.this.activity, (int) R.string.dialog_deleting);
                LocalPhotoPbPresenter.this.asytaskList.clear();
                LocalPhotoPbPresenter.this.executor = Executors.newSingleThreadExecutor();
                LocalPhotoPbPresenter.this.executor.submit(new DeleteThread(), null);
            }
        });
        builder.setPositiveButton(R.string.gallery_cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
