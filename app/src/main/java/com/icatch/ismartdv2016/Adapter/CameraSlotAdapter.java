package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.icatch.ismartdv2016.Beans.CameraSlot;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import java.util.List;

public class CameraSlotAdapter extends BaseAdapter {
    private List<CameraSlot> camSlotList;
    private Context context;
    private int listViewHeight = 0;
    private Handler myHandler;
    private String tag = "CameraSlotAdapter";

    public CameraSlotAdapter(Context context, List<CameraSlot> camSlotList, Handler handler, int height) {
        this.context = context;
        this.camSlotList = camSlotList;
        this.listViewHeight = height;
        this.myHandler = handler;
    }

    public CameraSlotAdapter(Context context, List<CameraSlot> camSlotList, Handler handler) {
        this.context = context;
        this.camSlotList = camSlotList;
        SystemInfo.getInstance();
        this.listViewHeight = SystemInfo.getMetrics().heightPixels;
        this.myHandler = handler;
    }

    public int getCount() {
        return this.camSlotList.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        CameraSlot camSlotItem = (CameraSlot) this.camSlotList.get(arg0);
        final int arg = arg0;
        if (camSlotItem.isOccupied) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.cam_slot_item, null);
            TextView slotConnectStatusTV = (TextView) convertView.findViewById(R.id.slot_connect_state);
            ImageView slotConnectStatusIV = (ImageView) convertView.findViewById(R.id.slot_connect_sign);
            TextView slotCameraName = (TextView) convertView.findViewById(R.id.slot_camera_name);
            ImageView slotPhoto = (ImageView) convertView.findViewById(R.id.slotPhoto);
            LinearLayout slot_layout = (LinearLayout) convertView.findViewById(R.id.slot_layout);
            LayoutParams params = (LayoutParams) slot_layout.getLayoutParams();
            ((ImageView) convertView.findViewById(R.id.delete_camera)).setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    CameraSlotAdapter.this.myHandler.obtainMessage(1, arg, 0).sendToTarget();
                }
            });
            params.height = this.listViewHeight / 7;
            slot_layout.setLayoutParams(params);
            byte[] imageBuf = camSlotItem.cameraPhoto;
            Bitmap imageBitmap = null;
            if (imageBuf != null) {
                imageBitmap = BitmapFactory.decodeByteArray(imageBuf, 0, imageBuf.length);
            }
            if (camSlotItem.isWifiReady) {
                slotConnectStatusTV.setTextColor(this.context.getResources().getColor(R.color.cambridge_blue));
                slotConnectStatusTV.setText("Connected");
                slotConnectStatusIV.setImageDrawable(this.context.getResources().getDrawable(R.drawable.camera_wifi_connected));
            } else {
                slotConnectStatusTV.setTextColor(this.context.getResources().getColor(R.color.graywhite));
                slotConnectStatusTV.setText("Disconnect");
                slotConnectStatusIV.setImageDrawable(this.context.getResources().getDrawable(R.drawable.camera_wifi_disconnected));
            }
            slotCameraName.setText(camSlotItem.cameraName);
            slotPhoto.setImageBitmap(imageBitmap);
            return convertView;
        }
        convertView = LayoutInflater.from(this.context).inflate(R.layout.cam_slot_item_add, null);
        LinearLayout slot_layout_add = (LinearLayout) convertView.findViewById(R.id.slot_layout_add);
        params = (LayoutParams) slot_layout_add.getLayoutParams();
        params.height = this.listViewHeight / 7;
        slot_layout_add.setLayoutParams(params);
        return convertView;
    }
}
