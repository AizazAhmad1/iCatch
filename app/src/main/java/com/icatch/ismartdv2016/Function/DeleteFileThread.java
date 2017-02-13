package com.icatch.ismartdv2016.Function;

import android.os.Handler;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import java.util.LinkedList;
import java.util.List;

public class DeleteFileThread implements Runnable {
    private String TAG = "DeleteFileThread";
    private List<MultiPbItemInfo> deleteFailedList;
    private List<MultiPbItemInfo> deletesucceedList;
    private List<MultiPbItemInfo> fileList;
    private FileOperation fileOperation;
    private Handler handler;
    private OnDeleteCompleteListener onDeleteCompleteListener;

    public interface OnDeleteCompleteListener {
        void onDeleteComplete(List<MultiPbItemInfo> list);
    }

    public DeleteFileThread(OnDeleteCompleteListener listener, List<MultiPbItemInfo> fileList) {
        this.fileList = fileList;
        this.onDeleteCompleteListener = listener;
        this.handler = new Handler();
        this.fileOperation = FileOperation.getInstance();
    }

    public void run() {
        AppLog.d(this.TAG, "DeleteThread");
        if (this.deleteFailedList == null) {
            this.deleteFailedList = new LinkedList();
        } else {
            this.deleteFailedList.clear();
        }
        for (MultiPbItemInfo tempFile : this.fileList) {
            AppLog.d(this.TAG, "deleteFile f.getFileHandle =" + tempFile.getFileHandle());
            if (!this.fileOperation.deleteFile(tempFile.iCatchFile)) {
                this.deleteFailedList.add(tempFile);
            }
        }
        this.handler.post(new Runnable() {
            public void run() {
                DeleteFileThread.this.onDeleteCompleteListener.onDeleteComplete(DeleteFileThread.this.deleteFailedList);
            }
        });
    }
}
