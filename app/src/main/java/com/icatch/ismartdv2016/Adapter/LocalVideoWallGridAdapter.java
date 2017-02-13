package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.Listener.OnAddAsytaskListener;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.R;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import java.util.List;

public class LocalVideoWallGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private String TAG = "LocalVideoWallGridAdapter";
    private Context context;
    private boolean[] isItemChecked;
    private List<LocalPbItemInfo> list;
    private OnAddAsytaskListener listener;
    private LayoutInflater mInflater;
    LruCache<String, Bitmap> mLruCache;
    private OperationMode operationMode = OperationMode.MODE_BROWSE;
    private int width;

    public static class HeaderViewHolder {
        public TextView mTextView;
    }

    public static class ViewHolder {
        public ImageView mCheckImageView;
        public ImageView mImageView;
    }

    public LocalVideoWallGridAdapter(Context context, List<LocalPbItemInfo> list, int width, LruCache<String, Bitmap> mLruCache, OnAddAsytaskListener listener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.width = width;
        this.mLruCache = mLruCache;
        this.isItemChecked = new boolean[list.size()];
        this.listener = listener;
        for (int ii = 0; ii < list.size(); ii++) {
            this.isItemChecked[ii] = false;
        }
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.iten_local_video_wall_grid, parent, false);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.local_video_thumbnail_grid);
            mViewHolder.mCheckImageView = (ImageView) convertView.findViewById(R.id.local_video_wall_grid_edit);
            LayoutParams photoLayoutParams = mViewHolder.mImageView.getLayoutParams();
            photoLayoutParams.width = (this.width - 3) / 4;
            photoLayoutParams.height = (this.width - 3) / 4;
            mViewHolder.mImageView.setLayoutParams(photoLayoutParams);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (this.operationMode == OperationMode.MODE_EDIT) {
            mViewHolder.mCheckImageView.setVisibility(0);
            if (this.isItemChecked[position]) {
                mViewHolder.mCheckImageView.setImageResource(R.drawable.ic_check_box_blue);
            } else {
                mViewHolder.mCheckImageView.setImageResource(R.drawable.ic_check_box_blank_grey);
            }
        } else {
            mViewHolder.mCheckImageView.setVisibility(8);
        }
        String path = ((LocalPbItemInfo) this.list.get(position)).getFilePath();
        mViewHolder.mImageView.setTag(path);
        Bitmap bitmap = (Bitmap) this.mLruCache.get(path);
        if (bitmap != null) {
            mViewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            if (this.listener != null) {
                this.listener.addAsytask(position);
            }
            mViewHolder.mImageView.setImageResource(R.drawable.pictures_no);
        }
        return convertView;
    }

    public long getHeaderId(int i) {
        return (long) ((LocalPbItemInfo) this.list.get(i)).section;
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;
        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = this.mInflater.inflate(R.layout.item_local_wall_grid_header, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView.findViewById(R.id.photo_wall_header);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }
        mHeaderHolder.mTextView.setText(((LocalPbItemInfo) this.list.get(position)).getFileDate());
        return convertView;
    }

    public void changeCheckBoxState(int post, OperationMode operationMode) {
        boolean z = true;
        this.operationMode = operationMode;
        Log.d(this.TAG, "changeSelectionState positon=" + post + "state = " + this.isItemChecked[post]);
        boolean[] zArr = this.isItemChecked;
        if (this.isItemChecked[post]) {
            z = false;
        }
        zArr[post] = z;
        notifyDataSetChanged();
        Log.d(this.TAG, "end changeSelectionState positon=" + post + "state = " + this.isItemChecked[post]);
    }

    public boolean[] getCheckBoxState() {
        return this.isItemChecked;
    }

    public void initCheckBoxState() {
        this.operationMode = OperationMode.MODE_BROWSE;
        if (this.isItemChecked == null) {
            this.isItemChecked = new boolean[this.list.size()];
        }
        for (int ii = 0; ii < this.list.size(); ii++) {
            this.isItemChecked[ii] = false;
        }
        notifyDataSetChanged();
    }

    public void selectAllCheckBoxState() {
        if (this.isItemChecked == null) {
            this.isItemChecked = new boolean[this.list.size()];
        }
        for (int ii = 0; ii < this.list.size(); ii++) {
            this.isItemChecked[ii] = true;
        }
        notifyDataSetChanged();
    }

    public void cancalSelectAllCheckBoxState() {
        if (this.isItemChecked == null) {
            this.isItemChecked = new boolean[this.list.size()];
        }
        for (int ii = 0; ii < this.list.size(); ii++) {
            this.isItemChecked[ii] = false;
        }
        notifyDataSetChanged();
    }

    public int getCheckedBoxNum() {
        int checkedNum = 0;
        for (int ii = 0; ii < this.list.size(); ii++) {
            if (this.isItemChecked[ii]) {
                checkedNum++;
            }
        }
        return checkedNum;
    }

    public void getCheckedItemsList() {
    }
}
