package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.R;

public class DownloadDialog extends AlertDialog {
    private Context context;
    private ListView downloadStatus;
    private TextView exit;
    private TextView message;

    public DownloadDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(this.context, R.layout.download_content_dialog, null);
        View titleView = View.inflate(this.context, R.layout.download_dialog_title, null);
        setCustomTitle(titleView);
        setView(contentView);
        this.exit = (TextView) titleView.findViewById(R.id.exit);
        this.downloadStatus = (ListView) contentView.findViewById(R.id.downloadStatus);
        this.message = (TextView) contentView.findViewById(R.id.message);
    }

    public void setMessage(String myMessage) {
        this.message.setText(myMessage);
    }

    public void setAdapter(ListAdapter adapter) {
        this.downloadStatus.setAdapter(adapter);
    }

    public TextView getDrawBackButton() {
        return this.exit;
    }
}
