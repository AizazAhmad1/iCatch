package com.icatch.ismartdv2016.AppDialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.icatch.ismartdv2016.BaseItems.DownloadInfo;
import com.icatch.ismartdv2016.ExtendComponent.NumberProgressBar;
import com.icatch.ismartdv2016.R;
import com.icatch.wificam.customer.type.ICatchFile;
import java.text.DecimalFormat;

public class SingleDownloadDialog {
    AlertDialog alertDialog = this.builder.create();
    Builder builder;
    Context context;
    ICatchFile curVideoFile;
    private ImageButton exitBtn;
    TextView fileDownloadStatus;
    TextView fileNameTxv;
    NumberProgressBar numberProgressBar;

    public SingleDownloadDialog(Context context, ICatchFile iCatchFile) {
        this.context = context;
        this.curVideoFile = iCatchFile;
        this.builder = new Builder(context);
        View contentView = View.inflate(context, R.layout.single_download_content_dialog, null);
        View titleView = View.inflate(context, R.layout.download_dialog_title, null);
        this.exitBtn = (ImageButton) titleView.findViewById(R.id.exit);
        this.fileNameTxv = (TextView) contentView.findViewById(R.id.fileName);
        this.fileDownloadStatus = (TextView) contentView.findViewById(R.id.downloadStatus);
        this.numberProgressBar = (NumberProgressBar) contentView.findViewById(R.id.numberbar);
        this.fileNameTxv.setText(this.curVideoFile.getFileName());
        this.builder.setCustomTitle(titleView);
        this.builder.setView(contentView);
        this.builder.setCancelable(false);
    }

    public void showDownloadDialog() {
        if (this.alertDialog != null) {
            this.alertDialog.show();
        }
    }

    public void dismissDownloadDialog() {
        if (this.alertDialog != null) {
            this.alertDialog.dismiss();
        }
    }

    public void setBackBtnOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            this.exitBtn.setOnClickListener(onClickListener);
        }
    }

    public void updateDownloadStatus(DownloadInfo downloadInfo) {
        this.numberProgressBar.setProgress(downloadInfo.progress);
        DecimalFormat df = new DecimalFormat("#.#");
        String curFileLength = df.format((((double) downloadInfo.curFileLength) / 1024.0d) / 1024.0d) + "M";
        this.fileDownloadStatus.setText(curFileLength + "/" + (df.format((((double) downloadInfo.fileSize) / 1024.0d) / 1024.0d) + "M"));
    }
}
