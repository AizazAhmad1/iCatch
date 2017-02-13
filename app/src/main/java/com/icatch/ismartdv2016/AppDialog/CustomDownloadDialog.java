package com.icatch.ismartdv2016.AppDialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.Adapter.DownloadManagerAdapter;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;

public class CustomDownloadDialog {
    private static String TAG = "CustomDownloadDialog";
    AlertDialog alertDialog;
    Builder builder;
    private ListView downloadStatus;
    private ImageButton exit;
    private TextView message;

    public void showDownloadDialog(Context context, DownloadManagerAdapter adapter) {
        this.builder = new Builder(context);
        View contentView = View.inflate(context, R.layout.download_content_dialog, null);
        View titleView = View.inflate(context, R.layout.download_dialog_title, null);
        this.exit = (ImageButton) titleView.findViewById(R.id.exit);
        this.downloadStatus = (ListView) contentView.findViewById(R.id.downloadStatus);
        this.downloadStatus.setItemsCanFocus(true);
        this.downloadStatus.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AppLog.d(CustomDownloadDialog.TAG, "3322ee downloadStatus ListView ClickListener position=" + position);
            }
        });
        this.message = (TextView) contentView.findViewById(R.id.message);
        this.downloadStatus.setAdapter(adapter);
        this.builder.setCustomTitle(titleView);
        this.builder.setView(contentView);
        this.builder.setCancelable(false);
        this.alertDialog = this.builder.create();
        this.alertDialog.show();
    }

    public void dismissDownloadDialog() {
        if (this.alertDialog != null) {
            this.alertDialog.dismiss();
        }
    }

    public void setMessage(String myMessage) {
        this.message.setText(myMessage);
    }

    public void setBackBtnOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            this.exit.setOnClickListener(onClickListener);
        }
    }

    public void setAdapter(DownloadManagerAdapter adapter) {
        this.downloadStatus.setAdapter(adapter);
    }
}
