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
import com.icatch.ismartdv2016.Presenter.MultiPbPhotoFragmentPresenter.OnGetListCompleteListener;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.Tools.ConvertTools;
import com.icatch.ismartdv2016.View.Activity.VideoPbActivity;
import com.icatch.ismartdv2016.View.Interface.MultiPbVideoFragmentView;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MultiPbVideoFragmentPresenter extends BasePresenter {
    private static int section = 1;
    private String TAG = "MultiPbVideoFragmentPresenter";
    private Activity activity;
    private LimitQueue<Asytask> asytaskList;
    private Asytask curAsytask;
    private FileOperation fileOperation = FileOperation.getInstance();
    private Handler handler;
    private boolean isFirstEnterThisActivity = true;
    private boolean isSelectAll = false;
    private LruCache<Integer, Bitmap> mLruCache;
    private OperationMode operationMode = OperationMode.MODE_BROWSE;
    private MultiPbPhotoWallGridAdapter photoWallGridAdapter;
    private MultiPbPhotoWallListAdapter photoWallListAdapter;
    private Map<String, Integer> sectionMap = new HashMap();
    private int topVisiblePosition = -1;
    private List<MultiPbItemInfo> videoInfoList;
    private MultiPbVideoFragmentView videoView;
    private int width;

    class Asytask extends AsyncTask<String, Integer, Bitmap> {
        int fileHandle;

        public Asytask(int handle) {
            this.fileHandle = handle;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap bm = MultiPbVideoFragmentPresenter.this.getBitmapFromLruCache(this.fileHandle);
            if (bm != null) {
                return bm;
            }
            ICatchFrameBuffer buffer = FileOperation.getInstance().getThumbnail(new ICatchFile(this.fileHandle));
            AppLog.d(MultiPbVideoFragmentPresenter.this.TAG, "decodeByteArray buffer=" + buffer);
            AppLog.d(MultiPbVideoFragmentPresenter.this.TAG, "decodeByteArray filePath=" + this.fileHandle);
            if (buffer == null) {
                AppLog.e(MultiPbVideoFragmentPresenter.this.TAG, "buffer == null  send _LOAD_BITMAP_FAILED");
                return null;
            }
            int datalength = buffer.getFrameSize();
            if (datalength > 0) {
                bm = BitmapFactory.decodeByteArray(buffer.getBuffer(), 0, datalength);
            }
            AppLog.d(MultiPbVideoFragmentPresenter.this.TAG, "decodeByteArray bm=" + bm);
            MultiPbVideoFragmentPresenter.this.addBitmapToLruCache(this.fileHandle, bm);
            return bm;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                ImageView imageView;
                if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_GRID) {
                    imageView = (ImageView) MultiPbVideoFragmentPresenter.this.videoView.gridViewFindViewWithTag(this.fileHandle);
                } else {
                    imageView = (ImageView) MultiPbVideoFragmentPresenter.this.videoView.listViewFindViewWithTag(this.fileHandle);
                }
                AppLog.i(MultiPbVideoFragmentPresenter.this.TAG, "loadBitmaps filePath=" + this.fileHandle + " imageView=" + imageView);
                if (!(imageView == null || result.isRecycled())) {
                    imageView.setImageBitmap(result);
                }
                if (MultiPbVideoFragmentPresenter.this.asytaskList != null && MultiPbVideoFragmentPresenter.this.asytaskList.size() > 0) {
                    Log.i("1111", "eeeee");
                    MultiPbVideoFragmentPresenter.this.curAsytask = (Asytask) MultiPbVideoFragmentPresenter.this.asytaskList.poll();
                    MultiPbVideoFragmentPresenter.this.curAsytask.execute(new String[0]);
                }
            }
        }
    }

    public MultiPbVideoFragmentPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.asytaskList = new LimitQueue(SystemInfo.getWindowVisibleCountMax(4));
        this.mLruCache = GlobalInfo.getInstance().mLruCache;
        this.handler = new Handler();
    }

    public void setView(MultiPbVideoFragmentView multiPbVideoView) {
        this.videoView = multiPbVideoView;
        initCfg();
    }

    public void getVideoList(final OnGetListCompleteListener onGetListCompleteListener) {
        List<MultiPbItemInfo> videoInfoList = new ArrayList();
        if (GlobalInfo.getInstance().videoInfoList != null) {
            this.videoInfoList = GlobalInfo.getInstance().videoInfoList;
        } else {
            List<ICatchFile> fileList = this.fileOperation.getFileList(ICatchFileType.ICH_TYPE_VIDEO);
            if (fileList != null) {
                Log.d(this.TAG, "fileList size=" + fileList.size());
                AppLog.d(this.TAG, "fileList size=" + fileList.size());
                for (int ii = 0; ii < fileList.size(); ii++) {
                    String fileDate = ConvertTools.getTimeByfileDate(((ICatchFile) fileList.get(ii)).getFileDate());
                    if (fileDate != null) {
                        if (this.sectionMap.containsKey(fileDate)) {
                            videoInfoList.add(new MultiPbItemInfo((ICatchFile) fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
                        } else {
                            this.sectionMap.put(fileDate, Integer.valueOf(section));
                            videoInfoList.add(new MultiPbItemInfo((ICatchFile) fileList.get(ii), ((Integer) this.sectionMap.get(fileDate)).intValue()));
                            section++;
                        }
                    }
                }
                this.videoInfoList = videoInfoList;
                GlobalInfo.getInstance().videoInfoList = videoInfoList;
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

    public void loadVideoWall() {
        MyProgressDialog.showProgressDialog(this.activity, "Loading...");
        new Thread(new Runnable() {
            public void run() {
                MultiPbVideoFragmentPresenter.this.getVideoList(new OnGetListCompleteListener() {
                    public void onGetFileListComplete() {
                        MultiPbVideoFragmentPresenter.this.setAdaper();
                        MyProgressDialog.closeProgressDialog();
                    }
                });
            }
        }).start();
    }

    public void setAdaper() {
        this.operationMode = OperationMode.MODE_BROWSE;
        this.isFirstEnterThisActivity = true;
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            this.videoView.setGridViewVisibility(8);
            this.videoView.setListViewVisibility(0);
            this.photoWallListAdapter = new MultiPbPhotoWallListAdapter(this.activity, this.videoInfoList, this.mLruCache, FileType.FILE_VIDEO);
            this.videoView.setListViewAdapter(this.photoWallListAdapter);
            return;
        }
        this.width = SystemInfo.getMetrics().widthPixels;
        this.videoView.setGridViewVisibility(0);
        this.videoView.setListViewVisibility(8);
        AppLog.d(this.TAG, "width=" + 0);
        this.photoWallGridAdapter = new MultiPbPhotoWallGridAdapter(this.activity, this.videoInfoList, this.width, this.mLruCache, FileType.FILE_VIDEO, new OnAddAsytaskListener() {
            public void addAsytask(int position) {
                MultiPbVideoFragmentPresenter.this.asytaskList.offer(new Asytask(((MultiPbItemInfo) MultiPbVideoFragmentPresenter.this.videoInfoList.get(position)).getFileHandle()));
            }
        });
        this.videoView.setGridViewAdapter(this.photoWallGridAdapter);
    }

    public void changePreviewType() {
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
        } else {
            GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_LIST;
        }
        loadVideoWall();
    }

    public void refreshPhotoWall() {
        Log.i("1122", "refreshPhotoWall GlobalInfo.photoWallPreviewType=" + GlobalInfo.photoWallPreviewType);
        if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
            if (this.photoWallListAdapter != null) {
                this.photoWallListAdapter.notifyDataSetChanged();
            }
        } else if (this.photoWallGridAdapter != null) {
            this.photoWallGridAdapter.notifyDataSetChanged();
        }
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
        if (this.videoInfoList != null && this.videoInfoList.size() > 0) {
            if (firstVisibleItem != this.topVisiblePosition && this.videoInfoList.size() > 0) {
                this.topVisiblePosition = firstVisibleItem;
                String fileDate = ((MultiPbItemInfo) this.videoInfoList.get(firstVisibleItem)).getFileDate();
                AppLog.d(this.TAG, "fileDate=" + fileDate);
                this.videoView.setListViewHeaderText(fileDate);
            }
            if (this.isFirstEnterThisActivity && visibleItemCount > 0) {
                loadBitmaps(firstVisibleItem, visibleItemCount);
                this.isFirstEnterThisActivity = false;
            }
        }
    }

    public void gridViewLoadThumbnails(int scrollState, int firstVisibleItem, int visibleItemCount) {
        AppLog.d(this.TAG, "onScrollStateChanged scrollState=" + scrollState);
        if (scrollState == 0) {
            AppLog.d(this.TAG, "onScrollStateChanged firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
            if (this.asytaskList != null && this.asytaskList.size() > 0) {
                this.curAsytask = (Asytask) this.asytaskList.poll();
                this.curAsytask.execute(new String[0]);
            }
        }
    }

    public void gridViewLoadOnceThumbnails(int firstVisibleItem, int visibleItemCount) {
        AppLog.d(this.TAG, "onScroll firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
        if (this.videoInfoList != null && this.videoInfoList.size() > 0 && this.isFirstEnterThisActivity && visibleItemCount > 0) {
            if (this.asytaskList != null && this.asytaskList.size() > 0) {
                this.curAsytask = (Asytask) this.asytaskList.poll();
                this.curAsytask.execute(new String[0]);
            }
            this.isFirstEnterThisActivity = false;
        }
    }

    void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        AppLog.i(this.TAG, "add task loadBitmaps 111111 asytaskList=" + this.asytaskList);
        if (this.asytaskList == null) {
            this.asytaskList = new LimitQueue(SystemInfo.getWindowVisibleCountMax(4));
        }
        int ii = firstVisibleItem;
        while (ii < firstVisibleItem + visibleItemCount) {
            if (this.videoInfoList != null && this.videoInfoList.size() > 0 && ii < this.videoInfoList.size()) {
                this.asytaskList.offer(new Asytask(((MultiPbItemInfo) this.videoInfoList.get(ii)).getFileHandle()));
                AppLog.i(this.TAG, "add task loadBitmaps ii=" + ii);
            }
            ii++;
        }
        if (this.asytaskList != null && this.asytaskList.size() > 0) {
            this.curAsytask = (Asytask) this.asytaskList.poll();
            this.curAsytask.execute(new String[0]);
        }
    }

    public void redirectToAnotherActivity(Context context, Class<?> cls, int position) {
        Intent intent = new Intent();
        intent.putExtra("curfilePath", ((MultiPbItemInfo) this.videoInfoList.get(position)).getFilePath());
        AppLog.i(this.TAG, "intent:start redirectToAnotherActivity class =" + cls.getName());
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public void listViewEnterEditMode(int position) {
        if (this.operationMode == OperationMode.MODE_BROWSE) {
            this.operationMode = OperationMode.MODE_EDIT;
            this.videoView.changeMultiPbMode(this.operationMode);
            this.photoWallListAdapter.setOperationMode(this.operationMode);
            this.photoWallListAdapter.changeSelectionState(position);
            this.videoView.setVideoSelectNumText(this.photoWallListAdapter.getSelectedCount());
            Log.d(this.TAG, "gridViewSelectOrCancelOnce operationMode=" + this.operationMode);
        }
    }

    public void gridViewEnterEditMode(int position) {
        if (this.operationMode == OperationMode.MODE_BROWSE) {
            this.operationMode = OperationMode.MODE_EDIT;
            this.videoView.changeMultiPbMode(this.operationMode);
            this.photoWallGridAdapter.changeCheckBoxState(position, this.operationMode);
            this.videoView.setVideoSelectNumText(this.photoWallGridAdapter.getSelectedCount());
            Log.d(this.TAG, "gridViewSelectOrCancelOnce operationMode=" + this.operationMode);
        }
    }

    public void quitEditMode() {
        if (this.operationMode == OperationMode.MODE_EDIT) {
            this.operationMode = OperationMode.MODE_BROWSE;
            this.videoView.changeMultiPbMode(this.operationMode);
            if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                this.photoWallListAdapter.quitEditMode();
            } else {
                this.photoWallGridAdapter.quitEditMode();
            }
        }
    }

    public void listViewSelectOrCancelOnce(int position) {
        Log.d(this.TAG, "listViewSelectOrCancelOnce position=" + position + " operationMode=" + this.operationMode);
        if (this.operationMode == OperationMode.MODE_BROWSE) {
            clealAsytaskList();
            Intent intent = new Intent();
            intent.putExtra("curfilePosition", position);
            intent.setClass(this.activity, VideoPbActivity.class);
            this.activity.startActivity(intent);
            return;
        }
        this.photoWallListAdapter.changeSelectionState(position);
        this.videoView.setVideoSelectNumText(this.photoWallListAdapter.getSelectedCount());
    }

    public void gridViewSelectOrCancelOnce(final int position) {
        Log.d(this.TAG, "gridViewSelectOrCancelOnce positon=" + position + " GlobalInfo.photoWallPreviewType=" + GlobalInfo.photoWallPreviewType);
        if (this.operationMode == OperationMode.MODE_BROWSE) {
            Log.d(this.TAG, "gridViewSelectOrCancelOnce operationMode=" + this.operationMode);
            clealAsytaskList();
            if (this.curAsytask != null) {
                AppLog.d(this.TAG, "curAsytask cancal ret=" + this.curAsytask.cancel(true));
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    MultiPbVideoFragmentPresenter.this.handler.post(new Runnable() {
                        public void run() {
                            MyProgressDialog.closeProgressDialog();
                        }
                    });
                    AppLog.d(MultiPbVideoFragmentPresenter.this.TAG, "curAsytask Thread.sleep(500)");
                    Intent intent = new Intent();
                    intent.putExtra("curfilePosition", position);
                    intent.setClass(MultiPbVideoFragmentPresenter.this.activity, VideoPbActivity.class);
                    MultiPbVideoFragmentPresenter.this.activity.startActivity(intent);
                }
            }, 500);
            MyProgressDialog.showProgressDialog(this.activity, "Loading...");
            return;
        }
        this.photoWallGridAdapter.changeCheckBoxState(position, this.operationMode);
        this.videoView.setVideoSelectNumText(this.photoWallGridAdapter.getSelectedCount());
    }

    public void selectOrCancelAll(boolean isSelectAll) {
        if (this.operationMode != OperationMode.MODE_BROWSE) {
            int selectNum;
            if (isSelectAll) {
                if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                    this.photoWallListAdapter.selectAllItems();
                    selectNum = this.photoWallListAdapter.getSelectedCount();
                } else {
                    this.photoWallGridAdapter.selectAllItems();
                    selectNum = this.photoWallGridAdapter.getSelectedCount();
                }
                this.videoView.setVideoSelectNumText(selectNum);
                return;
            }
            if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                this.photoWallListAdapter.cancelAllSelections();
                selectNum = this.photoWallListAdapter.getSelectedCount();
            } else {
                this.photoWallGridAdapter.cancelAllSelections();
                selectNum = this.photoWallGridAdapter.getSelectedCount();
            }
            this.videoView.setVideoSelectNumText(selectNum);
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
            AppLog.d("test", "addBitmapToLruCache filePath=" + fileHandle);
            AppLog.d("test", "addBitmapToLruCache bitmap=" + bm);
            this.mLruCache.put(Integer.valueOf(fileHandle), bm);
        }
    }

    public void emptyFileList() {
        if (GlobalInfo.getInstance().videoInfoList != null) {
            GlobalInfo.getInstance().videoInfoList.clear();
            GlobalInfo.getInstance().videoInfoList = null;
        }
    }

    public void clealAsytaskList() {
        AppLog.d(this.TAG, "clealAsytaskList");
        if (this.asytaskList != null && this.asytaskList.size() > 0) {
            AppLog.d(this.TAG, "clealAsytaskList size=" + this.asytaskList.size());
            this.asytaskList.clear();
        }
    }
}
