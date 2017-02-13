package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.LruCache;
import com.icatch.ismartdv2016.Adapter.ViewPagerAdapter;
import com.icatch.ismartdv2016.BaseItems.FileType;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.PhotoWallPreviewType;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.Function.PbDownloadManager;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnStatusChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.Presenter.Interface.BasePresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatch.ismartdv2016.View.Fragment.MultiPbPhotoFragment;
import com.icatch.ismartdv2016.View.Fragment.MultiPbVideoFragment;
import com.icatch.ismartdv2016.View.Interface.MultiPbView;
import com.icatch.wificam.customer.type.ICatchFile;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiPbPresenter extends BasePresenter {
    private static final String TAG = "MultiPbPresenter";
    private Activity activity;
    ViewPagerAdapter adapter;
    OperationMode curOperationMode = OperationMode.MODE_BROWSE;
    private boolean curSelectAll = false;
    private Executor executor;
    private FragmentManager fragmentManager;
    private boolean isBitmapRecycled = false;
    MultiPbPhotoFragment multiPbPhotoFragment;
    MultiPbVideoFragment multiPbVideoFragment;
    private MultiPbView multiPbView;
    private int offset = 0;

    class DeleteFileThread implements Runnable {
        private List<MultiPbItemInfo> deleteFailedList;
        private List<MultiPbItemInfo> deleteSucceedList;
        private List<MultiPbItemInfo> fileList;
        private FileOperation fileOperation = FileOperation.getInstance();
        private FileType fileType;
        private Handler handler = new Handler();

        public DeleteFileThread(List<MultiPbItemInfo> fileList, FileType fileType) {
            this.fileList = fileList;
            this.fileType = fileType;
        }

        public void run() {
            AppLog.d(MultiPbPresenter.TAG, "DeleteThread");
            if (this.deleteFailedList == null) {
                this.deleteFailedList = new LinkedList();
            } else {
                this.deleteFailedList.clear();
            }
            if (this.deleteSucceedList == null) {
                this.deleteSucceedList = new LinkedList();
            } else {
                this.deleteSucceedList.clear();
            }
            for (MultiPbItemInfo tempFile : this.fileList) {
                AppLog.d(MultiPbPresenter.TAG, "deleteFile f.getFileHandle =" + tempFile.getFileHandle());
                if (this.fileOperation.deleteFile(tempFile.iCatchFile)) {
                    this.deleteSucceedList.add(tempFile);
                } else {
                    this.deleteFailedList.add(tempFile);
                }
            }
            this.handler.post(new Runnable() {
                public void run() {
                    MyProgressDialog.closeProgressDialog();
                    if (DeleteFileThread.this.fileType == FileType.FILE_PHOTO) {
                        GlobalInfo.getInstance().photoInfoList.removeAll(DeleteFileThread.this.deleteSucceedList);
                        MultiPbPresenter.this.multiPbPhotoFragment.refreshPhotoWall();
                        MultiPbPresenter.this.multiPbPhotoFragment.quitEditMode();
                    } else if (DeleteFileThread.this.fileType == FileType.FILE_VIDEO) {
                        GlobalInfo.getInstance().videoInfoList.removeAll(DeleteFileThread.this.deleteSucceedList);
                        MultiPbPresenter.this.multiPbVideoFragment.refreshPhotoWall();
                        MultiPbPresenter.this.multiPbVideoFragment.quitEditMode();
                    }
                    if (!DeleteFileThread.this.deleteFailedList.isEmpty()) {
                    }
                }
            });
        }
    }

    public MultiPbPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        this.executor = Executors.newSingleThreadExecutor();
        initLruCache();
    }

    public void setView(MultiPbView multiPbView) {
        this.multiPbView = multiPbView;
        initCfg();
    }

    public void loadViewPage() {
        initViewpager();
    }

    private void initLruCache() {
        int cacheMemory = ((int) Runtime.getRuntime().maxMemory()) / 4;
        AppLog.d(TAG, "initLruCache cacheMemory=" + cacheMemory);
        GlobalInfo.getInstance().mLruCache = new LruCache<Integer, Bitmap>(cacheMemory) {
            protected int sizeOf(Integer key, Bitmap value) {
                AppLog.d("cacheMemory", " value.getByteCount()=" + value.getByteCount());
                return value.getByteCount();
            }

            protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    AppLog.d("cacheMemory", "entryRemoved key=" + key);
                    oldValue.recycle();
                }
            }
        };
    }

    public void clearCache() {
        this.isBitmapRecycled = true;
        GlobalInfo.getInstance().mLruCache.evictAll();
    }

    public void reset() {
        GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
        GlobalInfo.currentViewpagerPosition = 0;
        GlobalInfo.curVisibleItem = 0;
    }

    private void initViewpager() {
        if (this.multiPbPhotoFragment == null) {
            this.multiPbPhotoFragment = new MultiPbPhotoFragment();
        }
        this.multiPbPhotoFragment.setOperationListener(new OnStatusChangedListener() {
            public void onEnterEditMode(OperationMode operationMode) {
                MultiPbPresenter.this.curOperationMode = operationMode;
                if (MultiPbPresenter.this.curOperationMode == OperationMode.MODE_BROWSE) {
                    MultiPbPresenter.this.multiPbView.setViewPagerScanScroll(true);
                    MultiPbPresenter.this.multiPbView.setTabLayoutClickable(true);
                    MultiPbPresenter.this.multiPbView.setSelectBtnVisibility(8);
                    MultiPbPresenter.this.multiPbView.setSelectNumTextVisibility(8);
                    MultiPbPresenter.this.multiPbView.setSelectBtnIcon(R.drawable.ic_select_all_white_24dp);
                    MultiPbPresenter.this.curSelectAll = false;
                    AppLog.d(MultiPbPresenter.TAG, "multiPbPhotoFragment quit EditMode");
                    return;
                }
                MultiPbPresenter.this.multiPbView.setViewPagerScanScroll(false);
                MultiPbPresenter.this.multiPbView.setTabLayoutClickable(false);
                MultiPbPresenter.this.multiPbView.setSelectBtnVisibility(0);
                MultiPbPresenter.this.multiPbView.setSelectNumTextVisibility(0);
            }

            public void onSelectedItemsCountChanged(int SelectedNum) {
                MultiPbPresenter.this.multiPbView.setSelectNumText("Selected(" + SelectedNum + ")");
            }
        });
        if (this.multiPbVideoFragment == null) {
            this.multiPbVideoFragment = new MultiPbVideoFragment();
        }
        this.multiPbVideoFragment.setOperationListener(new OnStatusChangedListener() {
            public void onEnterEditMode(OperationMode operationMode) {
                MultiPbPresenter.this.curOperationMode = operationMode;
                if (MultiPbPresenter.this.curOperationMode == OperationMode.MODE_BROWSE) {
                    MultiPbPresenter.this.multiPbView.setViewPagerScanScroll(true);
                    MultiPbPresenter.this.multiPbView.setTabLayoutClickable(true);
                    MultiPbPresenter.this.multiPbView.setSelectBtnVisibility(8);
                    MultiPbPresenter.this.multiPbView.setSelectNumTextVisibility(8);
                    MultiPbPresenter.this.multiPbView.setSelectBtnIcon(R.drawable.ic_select_all_white_24dp);
                    MultiPbPresenter.this.curSelectAll = false;
                    AppLog.d(MultiPbPresenter.TAG, "multiPbVideoFragment quit EditMode");
                    return;
                }
                MultiPbPresenter.this.multiPbView.setViewPagerScanScroll(false);
                MultiPbPresenter.this.multiPbView.setTabLayoutClickable(false);
                MultiPbPresenter.this.multiPbView.setSelectBtnVisibility(0);
                MultiPbPresenter.this.multiPbView.setSelectNumTextVisibility(0);
            }

            public void onSelectedItemsCountChanged(int SelectedNum) {
                MultiPbPresenter.this.multiPbView.setSelectNumText("Selected(" + SelectedNum + ")");
            }
        });
        this.adapter = new ViewPagerAdapter(((FragmentActivity) this.activity).getSupportFragmentManager());
        this.adapter.addFragment(this.multiPbPhotoFragment, this.activity.getResources().getString(R.string.title_photo));
        this.adapter.addFragment(this.multiPbVideoFragment, this.activity.getResources().getString(R.string.title_video));
        this.multiPbView.setViewPageAdapter(this.adapter);
        this.multiPbView.setViewPageCurrentItem(GlobalInfo.currentViewpagerPosition);
    }

    public void updateViewpagerStatus(int arg0) {
        AppLog.d(TAG, "updateViewpagerStatus arg0=" + arg0);
        GlobalInfo.currentViewpagerPosition = arg0;
    }

    public void changePreviewType() {
        if (this.curOperationMode == OperationMode.MODE_BROWSE) {
            clealAsytaskList();
            if (GlobalInfo.photoWallPreviewType == PhotoWallPreviewType.PREVIEW_TYPE_LIST) {
                GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
                this.multiPbView.setMenuPhotoWallTypeIcon(R.drawable.ic_view_grid_white_24dp);
            } else {
                GlobalInfo.photoWallPreviewType = PhotoWallPreviewType.PREVIEW_TYPE_LIST;
                this.multiPbView.setMenuPhotoWallTypeIcon(R.drawable.ic_view_list_white_24dp);
            }
            this.multiPbPhotoFragment.changePreviewType();
            this.multiPbVideoFragment.changePreviewType();
            AppLog.d(TAG, " changePreviewType AppInfo.photoWallPreviewType");
        }
    }

    public void reback() {
        if (this.curOperationMode == OperationMode.MODE_BROWSE) {
            this.activity.finish();
        } else if (this.curOperationMode == OperationMode.MODE_EDIT) {
            this.curOperationMode = OperationMode.MODE_BROWSE;
            if (GlobalInfo.currentViewpagerPosition == 0) {
                this.multiPbPhotoFragment.quitEditMode();
            } else if (GlobalInfo.currentViewpagerPosition == 1) {
                this.multiPbVideoFragment.quitEditMode();
            }
        }
    }

    public void selectOrCancel() {
        if (this.curSelectAll) {
            this.multiPbView.setSelectBtnIcon(R.drawable.ic_select_all_white_24dp);
            this.curSelectAll = false;
        } else {
            this.multiPbView.setSelectBtnIcon(R.drawable.ic_unselected_white_24dp);
            this.curSelectAll = true;
        }
        if (GlobalInfo.currentViewpagerPosition == 0) {
            this.multiPbPhotoFragment.selectOrCancelAll(this.curSelectAll);
        } else if (GlobalInfo.currentViewpagerPosition == 1) {
            this.multiPbVideoFragment.select(this.curSelectAll);
        }
    }

    public void delete() {
        List<MultiPbItemInfo> list = null;
        FileType fileType = null;
        AppLog.d(TAG, "delete AppInfo.currentViewpagerPosition=" + GlobalInfo.currentViewpagerPosition);
        if (GlobalInfo.currentViewpagerPosition == 0) {
            list = this.multiPbPhotoFragment.getSelectedList();
            fileType = FileType.FILE_PHOTO;
        } else if (GlobalInfo.currentViewpagerPosition == 1) {
            list = this.multiPbVideoFragment.getSelectedList();
            fileType = FileType.FILE_VIDEO;
        }
        if (list == null || list.size() <= 0) {
            AppLog.d(TAG, "asytaskList size=" + list.size());
            MyToast.show(this.activity, (int) R.string.gallery_no_file_selected);
            return;
        }
        CharSequence what = this.activity.getResources().getString(R.string.gallery_delete_des).replace("$1$", String.valueOf(list.size()));
        Builder builder = new Builder(this.activity);
        builder.setCancelable(false);
        builder.setMessage(what);
        builder.setPositiveButton(this.activity.getResources().getString(R.string.gallery_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final List<MultiPbItemInfo> finalList = list;
        final FileType finalFileType = fileType;
        builder.setNegativeButton(this.activity.getResources().getString(R.string.gallery_delete), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyProgressDialog.showProgressDialog(MultiPbPresenter.this.activity, (int) R.string.dialog_deleting);
                new DeleteFileThread(finalList, finalFileType).run();
            }
        });
        builder.create().show();
    }

    public void download() {
        List<MultiPbItemInfo> list = null;
        LinkedList<ICatchFile> linkedList = new LinkedList();
        long fileSizeTotal = 0;
        AppLog.d(TAG, "delete currentViewpagerPosition=" + GlobalInfo.currentViewpagerPosition);
        if (GlobalInfo.currentViewpagerPosition == 0) {
            list = this.multiPbPhotoFragment.getSelectedList();
        } else if (GlobalInfo.currentViewpagerPosition == 1) {
            list = this.multiPbVideoFragment.getSelectedList();
        }
        if (list == null || list.size() <= 0) {
            AppLog.d(TAG, "asytaskList size=" + list.size());
            MyToast.show(this.activity, (int) R.string.gallery_no_file_selected);
            return;
        }
        for (MultiPbItemInfo temp : list) {
            linkedList.add(temp.iCatchFile);
            fileSizeTotal += temp.getFileSizeInteger();
        }
        if (SystemInfo.getSDFreeSize() < fileSizeTotal) {
            MyToast.show(this.activity, (int) R.string.text_sd_card_memory_shortage);
        } else {
            new PbDownloadManager(this.activity, linkedList).show();
        }
    }

    public void clealAsytaskList() {
        this.multiPbPhotoFragment.clealAsytaskList();
        this.multiPbVideoFragment.clealAsytaskList();
    }
}
