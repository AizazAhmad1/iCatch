package com.icatch.ismartdv2016.Function;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.icatch.ismartdv2016.Adapter.DownloadManagerAdapter;
import com.icatch.ismartdv2016.AppDialog.CustomDownloadDialog;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.BaseItems.DownloadInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.Tools.MediaRefresh;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PbDownloadManager {
    private static String TAG = "PbDownloadManager";
    private Builder builder;
    private Context context;
    private DownloadAsytask curDownloadAsytask;
    private ICatchFile currentDownloadFile;
    private CustomDownloadDialog customDownloadDialog;
    private LinkedList<ICatchFile> downloadChooseList;
    private int downloadFailed = 0;
    private HashMap<ICatchFile, DownloadInfo> downloadInfoMap = new HashMap();
    private DownloadManagerAdapter downloadManagerAdapter;
    Handler downloadManagerHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppMessage.MESSAGE_CANCEL_DOWNLOAD_SINGLE /*769*/:
                    ICatchFile temp = msg.obj;
                    AppLog.d(PbDownloadManager.TAG, "1122 receive MESSAGE_CANCEL_DOWNLOAD_SINGLE");
                    if (PbDownloadManager.this.currentDownloadFile == temp) {
                        if (PbDownloadManager.this.fileOperation.cancelDownload()) {
                            String filePath = Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH + PbDownloadManager.this.currentDownloadFile.getFileName();
                            File file = new File(filePath);
                            if (file != null && file.exists()) {
                                if (file.delete()) {
                                    Log.d("2222", "delete file success == " + filePath);
                                }
                            } else {
                                return;
                            }
                        }
                        Toast.makeText(PbDownloadManager.this.context, R.string.dialog_cancel_downloading_failed, 0).show();
                        return;
                    }
                    Toast.makeText(PbDownloadManager.this.context, R.string.dialog_cancel_downloading_succeeded, 0).show();
                    PbDownloadManager.this.downloadInfoMap.remove(temp);
                    PbDownloadManager.this.downloadChooseList.remove(temp);
                    PbDownloadManager.this.downloadTaskList.remove(temp);
                    AppLog.d(PbDownloadManager.TAG, "1122 receive MESSAGE_CANCEL_DOWNLOAD_SINGLE downloadChooseList size=" + PbDownloadManager.this.downloadChooseList.size() + "downloadInfoMap size=" + PbDownloadManager.this.downloadInfoMap.size());
                    PbDownloadManager.this.downloadManagerAdapter = new DownloadManagerAdapter(PbDownloadManager.this.context, PbDownloadManager.this.downloadInfoMap, PbDownloadManager.this.downloadChooseList, PbDownloadManager.this.downloadManagerHandler);
                    PbDownloadManager.this.customDownloadDialog.setAdapter(PbDownloadManager.this.downloadManagerAdapter);
                    PbDownloadManager.this.downloadManagerAdapter.notifyDataSetChanged();
                    PbDownloadManager.this.updateDownloadMessage();
                    if (PbDownloadManager.this.downloadTaskList.size() <= 0 && PbDownloadManager.this.customDownloadDialog != null) {
                        PbDownloadManager.this.customDownloadDialog.dismissDownloadDialog();
                        return;
                    }
                    return;
                case AppMessage.UPDATE_LOADING_PROGRESS /*770*/:
                    PbDownloadManager.this.downloadInfoMap.put(((DownloadInfo) msg.obj).file, (DownloadInfo) msg.obj);
                    PbDownloadManager.this.downloadManagerAdapter.notifyDataSetChanged();
                    return;
                case AppMessage.CANCEL_DOWNLOAD_ALL /*772*/:
                    AppLog.d(PbDownloadManager.TAG, "receive CANCEL_DOWNLOAD_ALL");
                    PbDownloadManager.this.alertForQuitDownload();
                    return;
                case AppMessage.DOWNLOAD_SUCCEED /*777*/:
                    PbDownloadManager.this.downloadSucceed = PbDownloadManager.this.downloadSucceed + 1;
                    PbDownloadManager.this.updateDownloadMessage();
                    return;
                case AppMessage.DOWNLOAD_FAILURE /*778*/:
                    AppLog.d(PbDownloadManager.TAG, "receive DOWNLOAD_FAILURE downloadFailed=" + PbDownloadManager.this.downloadFailed);
                    PbDownloadManager.this.downloadFailed = PbDownloadManager.this.downloadFailed + 1;
                    PbDownloadManager.this.updateDownloadMessage();
                    return;
                default:
                    return;
            }
        }
    };
    public long downloadProgress;
    private LinkedList<ICatchFile> downloadProgressList;
    private Timer downloadProgressTimer;
    private int downloadSucceed = 0;
    private LinkedList<ICatchFile> downloadTaskList;
    private int downloadTotal = 0;
    private ExecutorService executor;
    public FileOperation fileOperation = FileOperation.getInstance();
    private String filePath = (Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH);
    private Lock lock = new ReentrantLock();

    class DownloadAsytask extends AsyncTask<String, Integer, Boolean> {
        private String TAG = "DownloadAsytask";
        ICatchFile downloadFile;
        private String fileName;
        private String fileType = null;

        public DownloadAsytask(ICatchFile iCatchFile) {
            this.downloadFile = iCatchFile;
            PbDownloadManager.this.downloadProgressList.addLast(this.downloadFile);
        }

        protected Boolean doInBackground(String... params) {
            this.fileName = this.downloadFile.getFileName();
            File file = new File(PbDownloadManager.this.filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File tempFile = new File(PbDownloadManager.this.filePath + this.fileName);
            if (tempFile.exists() && tempFile.length() == this.downloadFile.getFileSize()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PbDownloadManager.this.downloadTaskList.remove(this.downloadFile);
                return Boolean.valueOf(true);
            }
            AppLog.d(this.TAG, "start downloadFile=" + PbDownloadManager.this.filePath + this.fileName);
            boolean retvalue = PbDownloadManager.this.fileOperation.downloadFileQuick(this.downloadFile, PbDownloadManager.this.filePath + this.fileName);
            AppLog.d(this.TAG, "end downloadFile retvalue =" + retvalue);
            if (retvalue) {
                if (this.downloadFile.getFileType() == ICatchFileType.ICH_TYPE_VIDEO) {
                    AppLog.d(this.TAG, "fileName = " + this.fileName);
                    if (this.fileName.endsWith(".mov") || this.fileName.endsWith(".MOV")) {
                        this.fileType = "video/mov";
                    } else {
                        this.fileType = "video/mp4";
                    }
                    MediaRefresh.addMediaToDB(PbDownloadManager.this.context, PbDownloadManager.this.filePath + this.downloadFile.getFileName(), this.fileType);
                } else if (this.downloadFile.getFileType() == ICatchFileType.ICH_TYPE_IMAGE) {
                    MediaRefresh.scanFileAsync(PbDownloadManager.this.context, PbDownloadManager.this.filePath + this.downloadFile.getFileName());
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            return Boolean.valueOf(retvalue);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                PbDownloadManager.this.downloadSucceed = PbDownloadManager.this.downloadSucceed + 1;
                PbDownloadManager.this.updateDownloadMessage();
            } else {
                AppLog.d(this.TAG, "receive DOWNLOAD_FAILURE downloadFailed=" + PbDownloadManager.this.downloadFailed);
                PbDownloadManager.this.downloadProgressList.remove(this.downloadFile);
                PbDownloadManager.this.downloadFailed = PbDownloadManager.this.downloadFailed + 1;
                PbDownloadManager.this.updateDownloadMessage();
            }
            PbDownloadManager.this.downloadTaskList.remove(this.downloadFile);
            if (PbDownloadManager.this.downloadTaskList.size() > 0) {
                PbDownloadManager.this.currentDownloadFile = (ICatchFile) PbDownloadManager.this.downloadTaskList.getFirst();
                new DownloadAsytask(PbDownloadManager.this.currentDownloadFile).execute(new String[0]);
                return;
            }
            if (PbDownloadManager.this.customDownloadDialog != null) {
                PbDownloadManager.this.customDownloadDialog.dismissDownloadDialog();
            }
            if (PbDownloadManager.this.downloadProgressTimer != null) {
                PbDownloadManager.this.downloadProgressTimer.cancel();
            }
            PbDownloadManager.this.fileOperation.closeFileTransChannel();
            PbDownloadManager.this.downloadCompleted();
        }
    }

    class DownloadProgressTask extends TimerTask {
        DownloadProgressTask() {
        }

        public void run() {
            if (!PbDownloadManager.this.downloadProgressList.isEmpty()) {
                String TAG = "DownloadProgressTask";
                final ICatchFile iCatchFile = (ICatchFile) PbDownloadManager.this.downloadProgressList.getFirst();
                File file = new File((Environment.getExternalStorageDirectory().toString() + AppInfo.DOWNLOAD_PATH) + iCatchFile.getFileName());
                AppLog.d(TAG, "filename = " + file);
                if (PbDownloadManager.this.downloadInfoMap.containsKey(iCatchFile)) {
                    long fileLength = file.length();
                    if (file == null) {
                        PbDownloadManager.this.downloadProgress = 0;
                    } else if (fileLength == iCatchFile.getFileSize()) {
                        PbDownloadManager.this.downloadProgress = 100;
                        PbDownloadManager.this.downloadProgressList.removeFirst();
                    } else {
                        PbDownloadManager.this.downloadProgress = (file.length() * 100) / iCatchFile.getFileSize();
                    }
                    if (PbDownloadManager.this.downloadInfoMap.containsKey(iCatchFile)) {
                        final DownloadInfo downloadInfo = (DownloadInfo) PbDownloadManager.this.downloadInfoMap.get(iCatchFile);
                        downloadInfo.curFileLength = fileLength;
                        downloadInfo.progress = (int) PbDownloadManager.this.downloadProgress;
                        AppLog.d(TAG, "downloadProgress = " + PbDownloadManager.this.downloadProgress);
                        PbDownloadManager.this.downloadManagerHandler.post(new Runnable() {
                            public void run() {
                                PbDownloadManager.this.downloadInfoMap.put(iCatchFile, downloadInfo);
                                PbDownloadManager.this.downloadManagerAdapter.notifyDataSetChanged();
                            }
                        });
                        return;
                    }
                    return;
                }
                PbDownloadManager.this.downloadProgressList.removeFirst();
            }
        }
    }

    public PbDownloadManager(Context context, LinkedList<ICatchFile> downloadList) {
        this.context = context;
        this.downloadTaskList = downloadList;
        this.downloadChooseList = new LinkedList();
        this.downloadProgressList = new LinkedList();
        this.downloadChooseList.addAll(this.downloadTaskList);
        for (int ii = 0; ii < this.downloadChooseList.size(); ii++) {
            this.downloadInfoMap.put(this.downloadChooseList.get(ii), new DownloadInfo((ICatchFile) this.downloadChooseList.get(ii), ((ICatchFile) this.downloadChooseList.get(ii)).getFileSize(), 0, 0, false));
        }
    }

    public void show() {
        showDownloadManagerDialog();
        this.executor = Executors.newSingleThreadExecutor();
        if (this.downloadTaskList.size() > 0) {
            this.downloadTotal = this.downloadTaskList.size();
            this.fileOperation.openFileTransChannel();
            this.currentDownloadFile = (ICatchFile) this.downloadTaskList.getFirst();
            new DownloadAsytask(this.currentDownloadFile).execute(new String[0]);
            this.downloadProgressTimer = new Timer();
            this.downloadProgressTimer.schedule(new DownloadProgressTask(), 0, 400);
        }
    }

    public void showDownloadManagerDialog() {
        this.downloadManagerAdapter = new DownloadManagerAdapter(this.context, this.downloadInfoMap, this.downloadChooseList, this.downloadManagerHandler);
        this.customDownloadDialog = new CustomDownloadDialog();
        this.customDownloadDialog.showDownloadDialog(this.context, this.downloadManagerAdapter);
        this.customDownloadDialog.setBackBtnOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PbDownloadManager.this.downloadManagerHandler.obtainMessage(AppMessage.CANCEL_DOWNLOAD_ALL).sendToTarget();
            }
        });
        updateDownloadMessage();
    }

    public void alertForQuitDownload() {
        if (this.builder == null) {
            this.builder = new Builder(this.context);
            this.builder.setIcon(R.drawable.warning).setTitle(R.string.dialog_btn_exit).setMessage(R.string.downloading_quit);
            this.builder.setPositiveButton(R.string.dialog_btn_exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PbDownloadManager.this.downloadTaskList.clear();
                    if (PbDownloadManager.this.fileOperation.cancelDownload()) {
                        PbDownloadManager.this.customDownloadDialog.dismissDownloadDialog();
                        if (PbDownloadManager.this.downloadProgressTimer != null) {
                            PbDownloadManager.this.downloadProgressTimer.cancel();
                        }
                        Toast.makeText(PbDownloadManager.this.context, R.string.dialog_cancel_downloading_succeeded, 0).show();
                        AppLog.d(PbDownloadManager.TAG, "cancel download task and quit download manager");
                        return;
                    }
                    Toast.makeText(PbDownloadManager.this.context, R.string.dialog_cancel_downloading_failed, 0).show();
                }
            });
            this.builder.setNegativeButton(R.string.gallery_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PbDownloadManager.this.builder = null;
                }
            });
            AlertDialog dialog = this.builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void downloadCompleted() {
        Builder builder = new Builder(this.context);
        builder.setTitle(this.context.getResources().getString(R.string.download_manager));
        builder.setMessage(this.context.getResources().getString(R.string.download_complete_result).replace("$1$", String.valueOf(this.downloadSucceed)).replace("$2$", String.valueOf(this.downloadTotal - this.downloadSucceed)));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void updateDownloadMessage() {
        this.customDownloadDialog.setMessage(this.context.getResources().getString(R.string.download_progress).replace("$1$", String.valueOf(this.downloadSucceed)).replace("$2$", String.valueOf(this.downloadChooseList.size() - this.downloadSucceed)).replace("$3$", String.valueOf(this.downloadFailed)));
    }
}
