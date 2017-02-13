package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallListAdapter;
import com.icatch.ismartdv2016.BaseItems.FileType;
import com.icatch.ismartdv2016.BaseItems.LimitQueue;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.PhotoWallPreviewType;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnAddAsytaskListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.Tools.ConvertTools;
import com.icatch.ismartdv2016.View.Activity.PhotoPbActivity;
import com.icatch.ismartdv2016.View.Interface.MultiPbPhotoFragmentView;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiPbPhotoFragmentPresenter extends BasePresenter {
    private static int section = 1;
    private String TAG = "MultiPbPhotoFragmentPresenter";
    private Activity activity;
    private LimitQueue<Asytask> asytaskList;
    private OperationMode curOperationMode = OperationMode.MODE_BROWSE;
    private FileOperation fileOperation = FileOperation.getInstance();
    private Handler handler;
    private boolean isFirstEnterThisActivity = true;
    private boolean isSelectAll = false;
    private LruCache<Integer, Bitmap> mLruCache;
    private MultiPbPhotoFragmentView multiPbPhotoView;
    private List<MultiPbItemInfo> pbItemInfoList;
    private MultiPbPhotoWallGridAdapter photoWallGridAdapter;
    private MultiPbPhotoWallListAdapter photoWallListAdapter;
    private Map<String, Integer> sectionMap = new HashMap();
    private int topVisiblePosition = -1;
    private int width;

    public interface OnGetListCompleteListener {
        void onGetFileListComplete();
    }

    class Asytask extends AsyncTask<String, Integer, Bitmap> {
        int fileHandle;

        public Asytask(int handle) {
            this.fileHandle = handle;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bm = MultiPbPhotoFragmentPresenter.this.getBitmapFromLruCache(this.fileHandle);
            AppLog.d(MultiPbPhotoFragmentPresenter.this.TAG, "getBitmapFromLruCache filePath=" + this.fileHandle + " bm=" + bm);
            if (bm != null) {
                return bm;
            }
            ICatchFrameBuffer buffer = FileOperation.getInstance().getThumbnail(new ICatchFile(this.fileHandle));
            AppLog.d(MultiPbPhotoFragmentPresenter.this.TAG, "decodeByteArray buffer=" + buffer);
            AppLog.d(MultiPbPhotoFragmentPresenter.this.TAG, "decodeByteArray filePath=" + this.fileHandle);
            if (buffer == null) {
                AppLog.e(MultiPbPhotoFragmentPresenter.this.TAG, "buffer == null  send _LOAD_BITMAP_FAILED");
                return null;
            }
            int datalength = buffer.getFrameSize();
            if (datalength > 0) {
                bm = BitmapFactory.decodeByteArray(buffer.getBuffer(), 0, datalength);
            }
            if (bm != null) {
                MultiPbPhotoFragmentPresenter.this.addBitmapToLruCache(this.fileHandle, bm);
            }
            return bm;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                ImageView imageView;
                if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_GRID) {
                    imageView = (ImageView) MultiPbPhotoFragmentPresenter.this.multiPbPhotoView.gridViewFindViewWithTag(this.fileHandle);
                } else {
                    imageView = (ImageView) MultiPbPhotoFragmentPresenter.this.multiPbPhotoView.listViewFindViewWithTag(this.fileHandle);
                }
                AppLog.i(MultiPbPhotoFragmentPresenter.this.TAG, "loadBitmaps imageView=" + imageView);
                if (!(imageView == null || result.isRecycled())) {
                    imageView.setImageBitmap(result);
                }
                if (MultiPbPhotoFragmentPresenter.this.asytaskList != null && MultiPbPhotoFragmentPresenter.this.asytaskList.size() > 0) {
                    Log.i("1111", "eeeee");
                    ((Asytask) MultiPbPhotoFragmentPresenter.this.asytaskList.poll()).execute(new String[0]);
                }
            }
        }
    }

    public MultiPbPhotoFragmentPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.asytaskList = new LimitQueue(SystemInfo.getWindowVisibleCountMax(4));
        this.mLruCache = GlobalInfo.getInstance().mLruCache;
        this.handler = new Handler();
    }

    public void setView(MultiPbPhotoFragmentView localPhotoWallView) {
        this.multiPbPhotoView = localPhotoWallView;
        initCfg();
    }

    public void getPhotoInfoList(final OnGetListCompleteListener onGetListCompleteListener) {
        List<MultiPbItemInfo> photoInfoList = new ArrayList();
        if (GlobalInfo.getInstance().photoInfoList != null) {
            this.pbItemInfoList = GlobalInfo.getInstance().photoInfoList;
        } else {
            List<ICatchFile> fileList = this.fileOperation.getFileList(ICatchFileType.ICH_TYPE_IMAGE);
            if (fileList != null) {
                AppLog.i(this.TAG, "fileList size=" + fileList.size());
                for (int ii = 0; ii < fileList.size(); ii++) {
                    String fileDate = ConvertTools.getTimeByfileDate(((ICatchFile) fileList.get(ii)).getFileDate());
                    if (fileDate != null) {
                        if (this.sectionMap.containsKey(fileDate)) {
                            photoInfoList.add(new MultiPbItemInfo((ICatchFile) fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
                        } else {
                            this.sectionMap.put(fileDate, Integer.valueOf(section));
                            photoInfoList.add(new MultiPbItemInfo((ICatchFile) fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
                            section++;
                        }
                    }
                }
                GlobalInfo.getInstance().photoInfoList = photoInfoList;
                this.pbItemInfoList = photoInfoList;
            } else {
                return;
            }
        }
        this.handler.post(new Runnable() {
            public void run() {
                onGetListCompleteListener.onGetFileListComplete();
            }
        });
    }

    public void loadPhotoWall() {
        MyProgressDialog.showProgressDialog(this.activity, "Loading...");
        new Thread(new Runnable() {
            public void run() {
                MultiPbPhotoFragmentPresenter.this.getPhotoInfoList(new OnGetListCompleteListener() {
                    public void onGetFileListComplete() {
                        MultiPbPhotoFragmentPresenter.this.setAdaper();
                        MyProgressDialog.closeProgressDialog();
                    }
                });
            }
        }).start();
    }

    public void setAdaper() {
        this.curOperationMode = OperationMode.MODE_BROWSE;
        if (this.pbItemInfoList != null && this.pbItemInfoList.size() > 0) {
            String fileDate = ((MultiPbItemInfo) this.pbItemInfoList.get(0)).getFileDate();
            AppLog.d(this.TAG, "fileDate=" + fileDate);
            this.multiPbPhotoView.setListViewHeaderText(fileDate);
        }
        this.isFirstEnterThisActivity = true;
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            this.multiPbPhotoView.setGridViewVisibility(8);
            this.multiPbPhotoView.setListViewVisibility(0);
            this.photoWallListAdapter = new MultiPbPhotoWallListAdapter(this.activity, this.pbItemInfoList, this.mLruCache, FileType.FILE_PHOTO);
            this.multiPbPhotoView.setListViewAdapter(this.photoWallListAdapter);
            return;
        }
        this.width = SystemInfo.getMetrics().widthPixels;
        this.multiPbPhotoView.setGridViewVisibility(0);
        this.multiPbPhotoView.setListViewVisibility(8);
        AppLog.d(this.TAG, "width=" + 0);
        this.photoWallGridAdapter = new MultiPbPhotoWallGridAdapter(this.activity, this.pbItemInfoList, this.width, this.mLruCache, FileType.FILE_PHOTO, new OnAddAsytaskListener() {
            public void addAsytask(int position) {
                MultiPbPhotoFragmentPresenter.this.asytaskList.offer(new Asytask(((MultiPbItemInfo) MultiPbPhotoFragmentPresenter.this.pbItemInfoList.get(position)).getFileHandle()));
            }
        });
        this.multiPbPhotoView.setGridViewAdapter(this.photoWallGridAdapter);
    }

    public void refreshPhotoWall() {
        Log.i("1122", "refreshPhotoWall layoutType=" + GlobalInfo.photoWallPreviewType);
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            if (this.photoWallListAdapter != null) {
                this.photoWallListAdapter.notifyDataSetChanged();
            }
        } else if (this.photoWallGridAdapter != null) {
            this.photoWallGridAdapter.notifyDataSetChanged();
        }
    }

    public void changePreviewType() {
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
        } else {
            GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_LIST;
        }
        loadPhotoWall();
    }

    public void listViewLoadThumbnails(int scrollState, int firstVisibleItem, int visibleItemCount) {
        AppLog.d(this.TAG, "onScrollStateChanged");
        if (scrollState == 0) {
            AppLog.d(this.TAG, "onScrollStateChanged firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
            this.asytaskList.clear();
            loadBitmaps(firstVisibleItem, visibleItemCount);
            return;
        }
        this.asytaskList.clear();
    }

    public void listViewLoadOnceThumbnails(int firstVisibleItem, int visibleItemCount) {
        AppLog.d(this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem);
        if (firstVisibleItem != this.topVisiblePosition) {
            this.topVisiblePosition = firstVisibleItem;
            if (this.pbItemInfoList != null && this.pbItemInfoList.size() > 0) {
                String fileDate = ((MultiPbItemInfo) this.pbItemInfoList.get(firstVisibleItem)).getFileDate();
                AppLog.d(this.TAG, "fileDate=" + fileDate);
                this.multiPbPhotoView.setListViewHeaderText(fileDate);
            }
        }
        if (this.isFirstEnterThisActivity && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount);
            this.isFirstEnterThisActivity = false;
        }
    }

    public void gridViewLoadThumbnails(int scrollState, int firstVisibleItem, int visibleItemCount) {
        AppLog.d(this.TAG, "onScrollStateChanged scrollState=" + scrollState);
        if (scrollState == 0 && this.asytaskList != null && this.asytaskList.size() > 0) {
            ((Asytask) this.asytaskList.poll()).execute(new String[0]);
        }
    }

    public void gridViewLoadOnceThumbnails(int firstVisibleItem, int visibleItemCount) {
        AppLog.d(this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
        AppLog.d(this.TAG, "onScroll isFirstEnterThisActivity=" + this.isFirstEnterThisActivity);
        if (this.isFirstEnterThisActivity && visibleItemCount > 0) {
            if (this.asytaskList != null && this.asytaskList.size() > 0) {
                ((Asytask) this.asytaskList.poll()).execute(new String[0]);
            }
            this.isFirstEnterThisActivity = false;
        }
    }

    void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        AppLog.i(this.TAG, "add task loadBitmaps firstVisibleItem=" + firstVisibleItem + " visibleItemCount" + visibleItemCount);
        if (this.asytaskList == null) {
            this.asytaskList = new LimitQueue(SystemInfo.getWindowVisibleCountMax(4));
        }
        int ii = firstVisibleItem;
        while (ii < firstVisibleItem + visibleItemCount) {
            if (this.pbItemInfoList != null && this.pbItemInfoList.size() > 0 && ii < this.pbItemInfoList.size()) {
                this.asytaskList.offer(new Asytask(((MultiPbItemInfo) this.pbItemInfoList.get(ii)).getFileHandle()));
                AppLog.i(this.TAG, "add task loadBitmaps ii=" + ii);
            }
            ii++;
        }
        if (this.asytaskList != null && this.asytaskList.size() > 0) {
            ((Asytask) this.asytaskList.poll()).execute(new String[0]);
        }
    }

    public void redirectToAnotherActivity(Context context, Class<?> cls, int position) {
        Intent intent = new Intent();
        intent.putExtra("curfilePath", ((MultiPbItemInfo) this.pbItemInfoList.get(position)).getFilePath());
        AppLog.i(this.TAG, "intent:start redirectToAnotherActivity class =" + cls.getName());
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public void listViewEnterEditMode(int position) {
        if (this.curOperationMode == OperationMode.MODE_BROWSE) {
            this.curOperationMode = OperationMode.MODE_EDIT;
            this.multiPbPhotoView.notifyChangeMultiPbMode(this.curOperationMode);
            this.photoWallListAdapter.setOperationMode(this.curOperationMode);
            this.photoWallListAdapter.changeSelectionState(position);
            this.multiPbPhotoView.setPhotoSelectNumText(this.photoWallListAdapter.getSelectedCount());
            AppLog.i(this.TAG, "gridViewSelectOrCancelOnce curOperationMode=" + this.curOperationMode);
        }
    }

    public void gridViewEnterEditMode(int position) {
        if (this.curOperationMode == OperationMode.MODE_BROWSE) {
            this.curOperationMode = OperationMode.MODE_EDIT;
            this.multiPbPhotoView.notifyChangeMultiPbMode(this.curOperationMode);
            this.photoWallGridAdapter.changeCheckBoxState(position, this.curOperationMode);
            this.multiPbPhotoView.setPhotoSelectNumText(this.photoWallGridAdapter.getSelectedCount());
            AppLog.i(this.TAG, "gridViewSelectOrCancelOnce curOperationMode=" + this.curOperationMode);
        }
    }

    public void quitEditMode() {
        if (this.curOperationMode == OperationMode.MODE_EDIT) {
            this.curOperationMode = OperationMode.MODE_BROWSE;
            this.multiPbPhotoView.notifyChangeMultiPbMode(this.curOperationMode);
            if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                this.photoWallListAdapter.quitEditMode();
            } else {
                this.photoWallGridAdapter.quitEditMode();
            }
        }
    }

    public void listViewSelectOrCancelOnce(int position) {
        AppLog.i(this.TAG, "listViewSelectOrCancelOnce positon=" + position + " AppInfo.photoWallPreviewType=" + GlobalInfo.photoWallPreviewType);
        if (this.curOperationMode == OperationMode.MODE_BROWSE) {
            AppLog.i(this.TAG, "listViewSelectOrCancelOnce curOperationMode=" + this.curOperationMode);
            clealAsytaskList();
            Intent intent = new Intent();
            intent.putExtra("curfilePosition", position);
            intent.setClass(this.activity, PhotoPbActivity.class);
            this.activity.startActivity(intent);
            return;
        }
        this.photoWallListAdapter.changeSelectionState(position);
        this.multiPbPhotoView.setPhotoSelectNumText(this.photoWallListAdapter.getSelectedCount());
    }

    public void gridViewSelectOrCancelOnce(int position) {
        AppLog.i(this.TAG, "gridViewSelectOrCancelOnce positon=" + position + " AppInfo.photoWallPreviewType=" + GlobalInfo.photoWallPreviewType);
        if (this.curOperationMode == OperationMode.MODE_BROWSE) {
            clealAsytaskList();
            Intent intent = new Intent();
            intent.putExtra("curfilePosition", position);
            intent.setClass(this.activity, PhotoPbActivity.class);
            this.activity.startActivity(intent);
            AppLog.i(this.TAG, "gridViewSelectOrCancelOnce curOperationMode=" + this.curOperationMode);
            return;
        }
        this.photoWallGridAdapter.changeCheckBoxState(position, this.curOperationMode);
        this.multiPbPhotoView.setPhotoSelectNumText(this.photoWallGridAdapter.getSelectedCount());
    }

    public void selectOrCancelAll(boolean isSelectAll) {
        if (this.curOperationMode != OperationMode.MODE_BROWSE) {
            int selectNum;
            if (isSelectAll) {
                if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                    this.photoWallListAdapter.selectAllItems();
                    selectNum = this.photoWallListAdapter.getSelectedCount();
                } else {
                    this.photoWallGridAdapter.selectAllItems();
                    selectNum = this.photoWallGridAdapter.getSelectedCount();
                }
                this.multiPbPhotoView.setPhotoSelectNumText(selectNum);
                return;
            }
            if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                this.photoWallListAdapter.cancelAllSelections();
                selectNum = this.photoWallListAdapter.getSelectedCount();
            } else {
                this.photoWallGridAdapter.cancelAllSelections();
                selectNum = this.photoWallGridAdapter.getSelectedCount();
            }
            this.multiPbPhotoView.setPhotoSelectNumText(selectNum);
        }
    }

    public List<MultiPbItemInfo> getSelectedList() {
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            return this.photoWallListAdapter.getSelectedList();
        }
        return this.photoWallGridAdapter.getCheckedItemsList();
    }

    public Bitmap getBitmapFromLruCache(int fileHandle) {
        return (Bitmap) this.mLruCache.get(Integer.valueOf(fileHandle));
    }

    protected void addBitmapToLruCache(int fileHandle, Bitmap bm) {
        if (getBitmapFromLruCache(fileHandle) == null && bm != null && fileHandle != 0) {
            AppLog.d(this.TAG, "addBitmapToLruCache filePath=" + fileHandle);
            this.mLruCache.put(Integer.valueOf(fileHandle), bm);
        }
    }

    public void emptyFileList() {
        if (GlobalInfo.getInstance().photoInfoList != null) {
            GlobalInfo.getInstance().photoInfoList.clear();
            GlobalInfo.getInstance().photoInfoList = null;
        }
    }

    public void clealAsytaskList() {
        AppLog.d(this.TAG, "clealAsytaskList");
        if (this.asytaskList != null && this.asytaskList.size() > 0) {
            this.asytaskList.clear();
        }
    }
}
