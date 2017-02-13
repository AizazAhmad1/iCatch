package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.icatch.ismartdv2016.BaseItems.DownloadInfo;
import com.icatch.ismartdv2016.ExtendComponent.NumberProgressBar;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.R;
import com.icatch.wificam.customer.type.ICatchFile;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class DownloadManagerAdapter extends BaseAdapter {
    private String TAG = "DownloadManagerAdapter";
    private List<ICatchFile> actList;
    private HashMap<ICatchFile, DownloadInfo> chooseListMap;
    private Context context;
    private Handler handler;
    private boolean isDownloadComplete = false;

    public DownloadManagerAdapter(Context context, HashMap<ICatchFile, DownloadInfo> downloadDataList, List<ICatchFile> actList, Handler handler) {
        this.context = context;
        this.chooseListMap = downloadDataList;
        this.actList = actList;
        this.handler = handler;
    }

    public int getCount() {
        return this.actList.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.download, null);
        }
        if (position < this.actList.size()) {
            this.isDownloadComplete = false;
            ImageButton cancelImv = (ImageButton) convertView.findViewById(R.id.doAction);
            TextView downloadStatus = (TextView) convertView.findViewById(R.id.downloadStatus);
            ((TextView) convertView.findViewById(R.id.fileName)).setText(((ICatchFile) this.actList.get(position)).getFileName());
            final ICatchFile downloadFile = (ICatchFile) this.actList.get(position);
            final DownloadInfo downloadInfo = (DownloadInfo) this.chooseListMap.get(downloadFile);
            ((NumberProgressBar) convertView.findViewById(R.id.numberbar)).setProgress(downloadInfo.progress);
            DecimalFormat df = new DecimalFormat("#.#");
            String curFileLength = df.format((((double) downloadInfo.curFileLength) / 1024.0d) / 1024.0d) + "M";
            String fileSize = df.format((((double) downloadInfo.fileSize) / 1024.0d) / 1024.0d) + "M";
            final int i = position;
            cancelImv.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    AppLog.d(DownloadManagerAdapter.this.TAG, "3322ee cancelImv setOnClickListener downloadInfo.progress=" + downloadInfo.progress + " position=" + i);
                    if (downloadInfo.progress < 100) {
                        DownloadManagerAdapter.this.handler.obtainMessage(AppMessage.MESSAGE_CANCEL_DOWNLOAD_SINGLE, 0, 0, downloadFile).sendToTarget();
                    }
                }
            });
            if (downloadInfo.progress >= 100) {
                downloadStatus.setText(curFileLength + "/" + fileSize);
                cancelImv.setImageResource(R.drawable.ic_done_cyan);
                cancelImv.setClickable(false);
                this.isDownloadComplete = true;
            } else {
                downloadStatus.setText(curFileLength + "/" + fileSize);
                cancelImv.setImageResource(R.drawable.cancel_task);
                cancelImv.setClickable(true);
                this.isDownloadComplete = false;
            }
        }
        return convertView;
    }
}
