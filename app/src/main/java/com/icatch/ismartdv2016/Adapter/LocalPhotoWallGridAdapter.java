package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.icatch.ismartdv2016.BaseItems.FileType;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.Listener.OnAddAsytaskListener;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import java.util.LinkedList;
import java.util.List;

public class LocalPhotoWallGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private String TAG = "LocalPhotoWallGridAdapter";
    private Context context;
    private FileType fileType;
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
        private ImageView mVideoSign;
    }

    public LocalPhotoWallGridAdapter(Context context, List<LocalPbItemInfo> list, int width, LruCache<String, Bitmap> mLruCache, FileType fileType, OnAddAsytaskListener listener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.mLruCache = mLruCache;
        this.fileType = fileType;
        this.width = SystemInfo.getMetrics().widthPixels;
        this.listener = listener;
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
        LayoutParams photoLayoutParams;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.item_local_photo_wall_grid, parent, false);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.local_photo_wall_grid_item);
            mViewHolder.mCheckImageView = (ImageView) convertView.findViewById(R.id.local_photo_wall_grid_edit);
            mViewHolder.mVideoSign = (ImageView) convertView.findViewById(R.id.video_sign);
            photoLayoutParams = mViewHolder.mImageView.getLayoutParams();
            photoLayoutParams.width = (this.width - 3) / 4;
            photoLayoutParams.height = (this.width - 3) / 4;
            mViewHolder.mImageView.setLayoutParams(photoLayoutParams);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            photoLayoutParams = mViewHolder.mImageView.getLayoutParams();
            photoLayoutParams.width = (this.width - 3) / 4;
            photoLayoutParams.height = (this.width - 3) / 4;
            mViewHolder.mImageView.setLayoutParams(photoLayoutParams);
        }
        if (this.fileType == FileType.FILE_PHOTO) {
            mViewHolder.mVideoSign.setVisibility(8);
        } else {
            mViewHolder.mVideoSign.setVisibility(0);
        }
        if (this.operationMode == OperationMode.MODE_EDIT) {
            mViewHolder.mCheckImageView.setVisibility(0);
            if (((LocalPbItemInfo) this.list.get(position)).isItemChecked) {
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

    public void notifyDataSetChanged() {
        this.width = SystemInfo.getMetrics().widthPixels;
        super.notifyDataSetChanged();
    }

    public void changeCheckBoxState(int position, OperationMode operationMode) {
        boolean z;
        this.operationMode = operationMode;
        LocalPbItemInfo localPbItemInfo = (LocalPbItemInfo) this.list.get(position);
        if (((LocalPbItemInfo) this.list.get(position)).isItemChecked) {
            z = false;
        } else {
            z = true;
        }
        localPbItemInfo.isItemChecked = z;
        notifyDataSetChanged();
    }

    public List<LocalPbItemInfo> getCheckedItemsList() {
        LinkedList<LocalPbItemInfo> checkedList = new LinkedList();
        for (int ii = 0; ii < this.list.size(); ii++) {
            if (((LocalPbItemInfo) this.list.get(ii)).isItemChecked) {
                checkedList.add(this.list.get(ii));
            }
        }
        return checkedList;
    }

    public void quitEditMode() {
        this.operationMode = OperationMode.MODE_BROWSE;
        for (int ii = 0; ii < this.list.size(); ii++) {
            ((LocalPbItemInfo) this.list.get(ii)).isItemChecked = false;
        }
        notifyDataSetChanged();
    }

    public void selectAllCheckBoxState() {
        for (int ii = 0; ii < this.list.size(); ii++) {
            ((LocalPbItemInfo) this.list.get(ii)).isItemChecked = true;
        }
        notifyDataSetChanged();
    }

    public void cancalSelectAllCheckBoxState() {
        for (int ii = 0; ii < this.list.size(); ii++) {
            ((LocalPbItemInfo) this.list.get(ii)).isItemChecked = false;
        }
        notifyDataSetChanged();
    }

    public int getCheckedBoxNum() {
        int checkedNum = 0;
        for (int ii = 0; ii < this.list.size(); ii++) {
            if (((LocalPbItemInfo) this.list.get(ii)).isItemChecked) {
                checkedNum++;
            }
        }
        return checkedNum;
    }
}
