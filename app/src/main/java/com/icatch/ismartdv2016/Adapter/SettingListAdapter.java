package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.Beans.SettingMenu;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.R;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class SettingListAdapter extends BaseAdapter {
    private static final String TAG = "SettingListAdapter";
    private Context context;
    private Handler handler;
    private List<SettingMenu> menuList;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public final class ViewHolder {
        public LinearLayout itemLayout;
        public TextView settingTitle;
        public TextView text;
        public TextView title;
    }

    public SettingListAdapter(Context context, List<SettingMenu> menuList, Handler handler, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.menuList = menuList;
        this.handler = handler;
        this.onItemClickListener = onItemClickListener;
    }

    public int getCount() {
        return this.menuList.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CheckBox toggleButton;
        if (((SettingMenu) this.menuList.get(position)).name == R.string.setting_auto_download) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.auto_download_layout, null);
            toggleButton = (CheckBox) convertView.findViewById(R.id.switcher);
            toggleButton.setChecked(AppInfo.autoDownloadAllow);
            toggleButton.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    SettingListAdapter.this.handler.obtainMessage(AppMessage.SETTING_OPTION_AUTO_DOWNLOAD, Boolean.valueOf(toggleButton.isChecked())).sendToTarget();
                }
            });
            return convertView;
        } else if (((SettingMenu) this.menuList.get(position)).name == R.string.setting_audio_switch) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.audio_switch_layout, null);
            toggleButton = (CheckBox) convertView.findViewById(R.id.switcher);
            toggleButton.setChecked(!AppInfo.disableAudio);
            toggleButton.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    AppInfo.disableAudio = !AppInfo.disableAudio;
                    AppLog.d(SettingListAdapter.TAG, "toggleButton.setOnClickListener disableAudio=" + AppInfo.disableAudio);
                }
            });
            return convertView;
        } else if (((SettingMenu) this.menuList.get(position)).name == R.string.setting_auto_download_size_limit) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.auto_download_layout_size, null);
            TextView autoDownloadSize = (TextView) convertView.findViewById(R.id.download_size);
            ((RelativeLayout) convertView.findViewById(R.id.item_layout)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SettingListAdapter.this.onItemClickListener.onItemClick(position);
                }
            });
            autoDownloadSize.setText(AppInfo.autoDownloadSizeLimit + "GB");
            return convertView;
        } else {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.setting_menu_item, null);
            ViewHolder holder = new ViewHolder();
            int curTitleId = ((SettingMenu) this.menuList.get(position)).titleId;
            holder.title = (TextView) convertView.findViewById(R.id.item_text);
            holder.text = (TextView) convertView.findViewById(R.id.item_value);
            holder.settingTitle = (TextView) convertView.findViewById(R.id.setting_title);
            holder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            convertView.setTag(holder);
            if (position == 0 || ((SettingMenu) this.menuList.get(position - 1)).titleId != curTitleId) {
                holder.settingTitle.setVisibility(0);
            } else {
                holder.settingTitle.setVisibility(8);
            }
            holder.title.setText(((SettingMenu) this.menuList.get(position)).name);
            if (((SettingMenu) this.menuList.get(position)).value == BuildConfig.FLAVOR) {
                holder.text.setVisibility(8);
            } else {
                holder.text.setText(((SettingMenu) this.menuList.get(position)).value);
            }
            int tempName = ((SettingMenu) this.menuList.get(position)).name;
            if (tempName == R.string.setting_app_version || tempName == R.string.setting_product_name || tempName == R.string.setting_firmware_version) {
                holder.title.setTextColor(this.context.getResources().getColor(R.color.secondary_text));
                holder.text.setTextColor(this.context.getResources().getColor(R.color.secondary_text));
            } else {
                holder.title.setTextColor(this.context.getResources().getColor(R.color.primary_text));
                holder.text.setTextColor(this.context.getResources().getColor(R.color.cambridge_blue));
                holder.itemLayout.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        SettingListAdapter.this.onItemClickListener.onItemClick(position);
                    }
                });
            }
            return convertView;
        }
    }
}
