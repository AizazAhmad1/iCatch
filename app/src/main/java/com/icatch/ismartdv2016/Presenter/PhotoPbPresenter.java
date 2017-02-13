package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.LruCache;
import android.view.View;
import com.icatch.ismartdv2016.Adapter.PhotoPbViewPagerAdapter;
import com.icatch.ismartdv2016.Adapter.PhotoPbViewPagerAdapter.OnPhotoTapListener;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.ExtendComponent.ProgressWheel;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.Tools.BitmapTools;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileOper;
import com.icatch.ismartdv2016.Tools.MediaRefresh;
import com.icatch.ismartdv2016.View.Interface.PhotoPbView;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import uk.co.senab.photoview.PhotoView;

public class PhotoPbPresenter extends BasePresenter {
    private static final int DIRECTION_LEFT = 2;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_UNKNOWN = 4;
    private String TAG = "PhotoPbPresenter";
    private Activity activity;
    LinkedList<Asytask> asytaskList;
    Asytask curAsytask;
    private int curPhotoIdx;
    public long downloadProcess;
    public String downloadingFilename;
    public long downloadingFilesize;
    private ExecutorService executor;
    private List<MultiPbItemInfo> fileList = GlobalInfo.getInstance().photoInfoList;
    private FileOperation fileOperation = FileOperation.getInstance();
    private Future<Object> future;
    private Handler handler;
    private boolean isScrolling = false;
    private int lastItem = -1;
    private LruCache<Integer, Bitmap> mLruCache;
    private PhotoPbView photoPbView;
    private int slideDirection = DIRECTION_RIGHT;
    private int tempLastItem = -1;
    private List<View> viewList;
    private PhotoPbViewPagerAdapter viewPagerAdapter;

    class Asytask extends AsyncTask<String, Integer, Bitmap> {
        ICatchFrameBuffer buffer;
        int fileHandle;
        ICatchFile iCatchFile;
        boolean isZoom = false;
        int position;

        public Asytask(ICatchFile iCatchFile, int position) {
            this.iCatchFile = iCatchFile;
            this.fileHandle = iCatchFile.getFileHandle();
            this.position = position;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bm = PhotoPbPresenter.this.getBitmapFromLruCache(this.fileHandle);
            AppLog.d(PhotoPbPresenter.this.TAG, "getBitmapFromLruCache bm=" + bm);
            if (bm != null) {
                return bm;
            }
            this.buffer = PhotoPbPresenter.this.fileOperation.getQuickview(this.iCatchFile);
            if (this.buffer == null || this.buffer.getFrameSize() <= 0) {
                AppLog.e(PhotoPbPresenter.this.TAG, "buffer == null  send _LOAD_BITMAP_FAILED 01");
                this.buffer = PhotoPbPresenter.this.fileOperation.downloadFile(this.iCatchFile);
                this.isZoom = true;
            }
            if (this.buffer == null || this.buffer.getFrameSize() <= 0) {
                AppLog.e(PhotoPbPresenter.this.TAG, "buffer == null  send _LOAD_BITMAP_FAILED 02");
                return null;
            }
            bm = BitmapTools.decodeByteArray(this.buffer.getBuffer(), 1080, 720);
            AppLog.d(PhotoPbPresenter.this.TAG, "position=" + this.position + " decodeByteArray bm=" + bm);
            if (bm == null) {
                return null;
            }
            AppLog.d(PhotoPbPresenter.this.TAG, "11 position=" + this.position + "filePath=" + this.fileHandle + " buffer size=" + this.buffer.getFrameSize() + " bm size=" + bm.getByteCount());
            PhotoPbPresenter.this.addBitmapToLruCache(this.fileHandle, bm);
            return bm;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                View view = (View) PhotoPbPresenter.this.viewList.get(this.position);
                if (view != null) {
                    PhotoView photoView = (PhotoView) view.findViewById(R.id.photo);
                    ProgressWheel progressBar = (ProgressWheel) view.findViewById(R.id.progress_wheel);
                    AppLog.d(PhotoPbPresenter.this.TAG, "onPostExecute position=" + this.position + " filePath=" + this.fileHandle + " size=" + result.getByteCount() + " result.isRecycled()=" + result.isRecycled() + " photoView=" + photoView);
                    if (!(photoView == null || result.isRecycled())) {
                        photoView.setImageBitmap(result);
                    }
                    if (progressBar != null) {
                        progressBar.setVisibility(8);
                    }
                }
            }
            if (PhotoPbPresenter.this.asytaskList != null && PhotoPbPresenter.this.asytaskList.size() > 0) {
                PhotoPbPresenter.this.curAsytask = (Asytask) PhotoPbPresenter.this.asytaskList.removeFirst();
                PhotoPbPresenter.this.curAsytask.execute(new String[0]);
            }
        }
    }

    private class DeleteThread implements Runnable {
        private DeleteThread() {
        }

        public void run() {
            PhotoPbPresenter.this.curPhotoIdx = PhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
            ICatchFile curFile = ((MultiPbItemInfo) PhotoPbPresenter.this.fileList.get(PhotoPbPresenter.this.curPhotoIdx)).iCatchFile;
            Boolean retValue = Boolean.valueOf(false);
            if (Boolean.valueOf(PhotoPbPresenter.this.fileOperation.deleteFile(curFile)).booleanValue()) {
                PhotoPbPresenter.this.handler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                        PhotoPbPresenter.this.fileList.remove(PhotoPbPresenter.this.curPhotoIdx);
                        PhotoPbPresenter.this.viewList.remove(PhotoPbPresenter.this.curPhotoIdx);
                        PhotoPbPresenter.this.viewPagerAdapter.notifyDataSetChanged();
                        PhotoPbPresenter.this.photoPbView.setViewPagerAdapter(PhotoPbPresenter.this.viewPagerAdapter);
                        int photoNums = PhotoPbPresenter.this.fileList.size();
                        if (photoNums == 0) {
                            PhotoPbPresenter.this.activity.finish();
                            return;
                        }
                        if (PhotoPbPresenter.this.curPhotoIdx == photoNums) {
                            PhotoPbPresenter.this.curPhotoIdx = PhotoPbPresenter.this.curPhotoIdx - 1;
                        }
                        AppLog.d(PhotoPbPresenter.this.TAG, "photoNums=" + photoNums + " curPhotoIdx=" + PhotoPbPresenter.this.curPhotoIdx);
                        PhotoPbPresenter.this.photoPbView.setViewPagerCurrentItem(PhotoPbPresenter.this.curPhotoIdx);
                        PhotoPbPresenter.this.ShowCurPageNum();
                        PhotoPbPresenter.this.loadBitmaps(PhotoPbPresenter.this.curPhotoIdx);
                    }
                });
            } else {
                PhotoPbPresenter.this.handler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                        MyToast.show(PhotoPbPresenter.this.activity, (int) R.string.dialog_delete_failed_single);
                    }
                });
            }
            AppLog.d(PhotoPbPresenter.this.TAG, "end DeleteThread");
        }
    }

    private class DownloadThread implements Runnable {
        private String TAG;
        private int curIdx;

        private DownloadThread() {
            this.TAG = "DownloadThread";
            this.curIdx = PhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
        }

        public void run() {
            AppLog.d(this.TAG, "begin DownloadThread");
            AppInfo.isDownloading = true;
            if (Environment.getExternalStorageState().equals("mounted")) {
                String path = Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH;
                String fileName = ((MultiPbItemInfo) PhotoPbPresenter.this.fileList.get(this.curIdx)).getFileName();
                AppLog.d(this.TAG, "------------fileName =" + fileName);
                FileOper.createDirectory(path);
                PhotoPbPresenter.this.downloadingFilename = path + fileName;
                PhotoPbPresenter.this.downloadingFilesize = ((MultiPbItemInfo) PhotoPbPresenter.this.fileList.get(this.curIdx)).iCatchFile.getFileSize();
                if (new File(PhotoPbPresenter.this.downloadingFilename).exists()) {
                    PhotoPbPresenter.this.handler.post(new Runnable() {
                        public void run() {
                            MyProgressDialog.closeProgressDialog();
                            MyToast.show(PhotoPbPresenter.this.activity, "Downloaded to/DCIM/iSmartDV/");
                        }
                    });
                } else {
                    boolean temp = PhotoPbPresenter.this.fileOperation.downloadFile(((MultiPbItemInfo) PhotoPbPresenter.this.fileList.get(this.curIdx)).iCatchFile, PhotoPbPresenter.this.downloadingFilename);
                    if (temp) {
                        MediaRefresh.scanFileAsync(PhotoPbPresenter.this.activity, PhotoPbPresenter.this.downloadingFilename);
                        AppLog.d(this.TAG, "end downloadFile temp =" + temp);
                        AppInfo.isDownloading = false;
                        PhotoPbPresenter.this.handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                                MyToast.show(PhotoPbPresenter.this.activity, "Downloaded to/DCIM/iSmartDV/");
                            }
                        });
                    } else {
                        PhotoPbPresenter.this.handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                                MyToast.show(PhotoPbPresenter.this.activity, "Download failed");
                            }
                        });
                        AppInfo.isDownloading = false;
                        return;
                    }
                }
                AppLog.d(this.TAG, "end DownloadThread");
                return;
            }
            PhotoPbPresenter.this.handler.post(new Runnable() {
                public void run() {
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(PhotoPbPresenter.this.activity, "Download failed");
                }
            });
        }
    }

    private class MyViewPagerOnPagerChangeListener implements OnPageChangeListener {
        private MyViewPagerOnPagerChangeListener() {
        }

        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case PhotoPbPresenter.DIRECTION_RIGHT /*1*/:
                    PhotoPbPresenter.this.isScrolling = true;
                    PhotoPbPresenter.this.tempLastItem = PhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
                    return;
                case PhotoPbPresenter.DIRECTION_LEFT /*2*/:
                    if (!(!PhotoPbPresenter.this.isScrolling || PhotoPbPresenter.this.tempLastItem == -1 || PhotoPbPresenter.this.tempLastItem == PhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem())) {
                        PhotoPbPresenter.this.lastItem = PhotoPbPresenter.this.tempLastItem;
                    }
                    PhotoPbPresenter.this.curPhotoIdx = PhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem();
                    PhotoPbPresenter.this.isScrolling = false;
                    PhotoPbPresenter.this.loadBitmaps(PhotoPbPresenter.this.photoPbView.getViewPagerCurrentItem());
                    PhotoPbPresenter.this.ShowCurPageNum();
                    return;
                default:
                    return;
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (PhotoPbPresenter.this.isScrolling) {
                if (PhotoPbPresenter.this.lastItem > arg2) {
                    PhotoPbPresenter.this.slideDirection = PhotoPbPresenter.DIRECTION_RIGHT;
                } else if (PhotoPbPresenter.this.lastItem < arg2) {
                    PhotoPbPresenter.this.slideDirection = PhotoPbPresenter.DIRECTION_LEFT;
                } else if (PhotoPbPresenter.this.lastItem == arg2) {
                    PhotoPbPresenter.this.slideDirection = PhotoPbPresenter.DIRECTION_RIGHT;
                }
            }
            PhotoPbPresenter.this.lastItem = arg2;
        }

        public void onPageSelected(int arg0) {
            PhotoPbPresenter.this.ShowCurPageNum();
        }
    }

    public PhotoPbPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.handler = new Handler();
        this.viewList = new LinkedList();
        initLruCache();
        this.slideDirection = DIRECTION_UNKNOWN;
    }

    public void setView(PhotoPbView photoPbView) {
        this.photoPbView = photoPbView;
        initCfg();
    }

    private void initLruCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        AppLog.d(this.TAG, "initLruCache maxMemory=" + maxMemory);
        AppLog.d(this.TAG, "initLruCache cacheMemory=" + cacheMemory);
        this.mLruCache = new LruCache<Integer, Bitmap>(cacheMemory) {
            protected int sizeOf(Integer key, Bitmap value) {
                AppLog.d(PhotoPbPresenter.this.TAG, "cacheMemory value.getByteCount()=" + value.getByteCount());
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    AppLog.d(PhotoPbPresenter.this.TAG, "cacheMemory entryRemoved key=" + key);
                    oldValue.recycle();
                }
            }
        };
    }

    public Bitmap getBitmapFromLruCache(int fileHandle) {
        AppLog.d(this.TAG, "getBitmapFromLruCache filePath=" + fileHandle);
        return (Bitmap) this.mLruCache.get(Integer.valueOf(fileHandle));
    }

    protected void addBitmapToLruCache(int fileHandle, Bitmap bm) {
        if (bm.getByteCount() > this.mLruCache.maxSize()) {
            AppLog.d(this.TAG, "addBitmapToLruCache greater than mLruCache size filePath=" + fileHandle);
        } else if (getBitmapFromLruCache(fileHandle) == null && bm != null && fileHandle != 0) {
            AppLog.d(this.TAG, "addBitmapToLruCache filePath=" + fileHandle);
            this.mLruCache.put(Integer.valueOf(fileHandle), bm);
        }
    }

    public void loadImage() {
        this.curPhotoIdx = this.activity.getIntent().getExtras().getInt("curfilePosition");
        for (int ii = 0; ii < this.fileList.size(); ii += DIRECTION_RIGHT) {
            this.viewList.add(ii, null);
        }
        this.viewPagerAdapter = new PhotoPbViewPagerAdapter(this.activity, this.fileList, this.viewList, this.mLruCache);
        this.viewPagerAdapter.setOnPhotoTapListener(new OnPhotoTapListener() {
            public void onPhotoTap() {
                PhotoPbPresenter.this.showBar();
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

    public void delete() {
        showDeleteEnsureDialog();
    }

    public void download() {
        showDownloadEnsureDialog();
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
            if (this.fileList == null || this.fileList.size() < 0) {
                AppLog.e(this.TAG, "fileList is null or size < 0");
                return;
            }
            if (curPhotoIdx == 0) {
                this.asytaskList.add(new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx)).iCatchFile, curPhotoIdx));
                if (this.fileList.size() > DIRECTION_RIGHT) {
                    this.asytaskList.add(new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx + DIRECTION_RIGHT)).iCatchFile, curPhotoIdx + DIRECTION_RIGHT));
                }
            } else if (curPhotoIdx == this.fileList.size() - 1) {
                task1 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx)).iCatchFile, curPhotoIdx);
                task2 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx - 1)).iCatchFile, curPhotoIdx - 1);
                this.asytaskList.add(task1);
                this.asytaskList.add(task2);
            } else {
                AppLog.d(this.TAG, "loadBitmaps slideDirection=" + this.slideDirection);
                Asytask task3;
                if (this.slideDirection == DIRECTION_RIGHT) {
                    task1 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx)).iCatchFile, curPhotoIdx);
                    task2 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx - 1)).iCatchFile, curPhotoIdx - 1);
                    task3 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx + DIRECTION_RIGHT)).iCatchFile, curPhotoIdx + DIRECTION_RIGHT);
                    this.asytaskList.add(task1);
                    this.asytaskList.add(task2);
                    this.asytaskList.add(task3);
                } else {
                    task1 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx)).iCatchFile, curPhotoIdx);
                    task2 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx + DIRECTION_RIGHT)).iCatchFile, curPhotoIdx + DIRECTION_RIGHT);
                    task3 = new Asytask(((MultiPbItemInfo) this.fileList.get(curPhotoIdx - 1)).iCatchFile, curPhotoIdx - 1);
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
        this.photoPbView.setIndexInfoTxv((this.photoPbView.getViewPagerCurrentItem() + DIRECTION_RIGHT) + "/" + this.fileList.size());
    }

    public void showDownloadEnsureDialog() {
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_downloading_single);
        long videoFileSize = (((MultiPbItemInfo) this.fileList.get(this.curPhotoIdx)).getFileSizeInteger() / 1024) / 1024;
        long seconds = videoFileSize % 60;
        builder.setMessage(this.activity.getResources().getString(R.string.gallery_download_with_vid_msg).replace("$1$", "1").replace("$3$", String.valueOf(seconds)).replace("$2$", String.valueOf(videoFileSize / 60)));
        builder.setNegativeButton(R.string.gallery_download, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AppLog.d(PhotoPbPresenter.this.TAG, "showProgressDialog");
                PhotoPbPresenter.this.downloadProcess = 0;
                if (SystemInfo.getSDFreeSize() < ((MultiPbItemInfo) PhotoPbPresenter.this.fileList.get(PhotoPbPresenter.this.curPhotoIdx)).getFileSizeInteger()) {
                    dialog.dismiss();
                    MyToast.show(PhotoPbPresenter.this.activity, (int) R.string.text_sd_card_memory_shortage);
                    return;
                }
                MyProgressDialog.showProgressDialog(PhotoPbPresenter.this.activity, (int) R.string.dialog_downloading_single);
                PhotoPbPresenter.this.executor = Executors.newSingleThreadExecutor();
                PhotoPbPresenter.this.future = PhotoPbPresenter.this.executor.submit(new DownloadThread(), null);
            }
        });
        builder.setPositiveButton(R.string.gallery_cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showDeleteEnsureDialog() {
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setTitle(R.string.image_delete_des);
        builder.setNegativeButton(R.string.gallery_delete, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MyProgressDialog.showProgressDialog(PhotoPbPresenter.this.activity, (int) R.string.dialog_deleting);
                PhotoPbPresenter.this.asytaskList.clear();
                PhotoPbPresenter.this.executor = Executors.newSingleThreadExecutor();
                PhotoPbPresenter.this.future = PhotoPbPresenter.this.executor.submit(new DeleteThread(), null);
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
