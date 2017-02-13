package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.icatch.ismartdv2016.BaseItems.FileType;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.R;
import java.util.List;

public class LocalPhotoWallListAdapter extends BaseAdapter {
    private String TAG = "LocalPhotoWallListAdapter";
    private Context context;
    private FileType fileType;
    private boolean[] isItemChecked;
    private List<LocalPbItemInfo> list;
    LruCache<String, Bitmap> mLruCache;
    private OperationMode operationMode = OperationMode.MODE_BROWSE;

    public LocalPhotoWallListAdapter(Context context, List<LocalPbItemInfo> list, LruCache<String, Bitmap> mLruCache, FileType fileType) {
        this.context = context;
        this.list = list;
        this.mLruCache = mLruCache;
        this.fileType = fileType;
        this.isItemChecked = new boolean[list.size()];
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
        View view;
        String path = ((LocalPbItemInfo) this.list.get(position)).getFilePath();
        String curFileDate = ((LocalPbItemInfo) this.list.get(position)).getFileDate();
        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_local_photo_wall_list, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.local_photo_thumbnail_list);
        TextView mTextView = (TextView) view.findViewById(R.id.photo_wall_header);
        RelativeLayout mLayout = (RelativeLayout) view.findViewById(R.id.local_photo_wall_header_layout);
        TextView imageSizeTextView = (TextView) view.findViewById(R.id.local_photo_size);
        TextView imageDateTextView = (TextView) view.findViewById(R.id.local_photo_date);
        ImageView mCheckImageView = (ImageView) view.findViewById(R.id.local_photo_wall_list_edit);
        ImageView videoSignImageView = (ImageView) view.findViewById(R.id.video_sign);
        ((TextView) view.findViewById(R.id.local_photo_name)).setText(((LocalPbItemInfo) this.list.get(position)).getFileName());
        imageSizeTextView.setText(((LocalPbItemInfo) this.list.get(position)).getFileSize());
        imageDateTextView.setText(((LocalPbItemInfo) this.list.get(position)).getFileDateMMSS());
        if (this.fileType == FileType.FILE_PHOTO) {
            videoSignImageView.setVisibility(8);
        } else {
            videoSignImageView.setVisibility(0);
        }
        if (this.operationMode == OperationMode.MODE_EDIT) {
            mCheckImageView.setVisibility(0);
            if (this.isItemChecked[position]) {
                mCheckImageView.setImageResource(R.drawable.ic_check_box_blue);
            } else {
                mCheckImageView.setImageResource(R.drawable.ic_check_box_blank_grey);
            }
        } else {
            mCheckImageView.setVisibility(8);
        }
        imageView.setTag(path);
        if (position == 0 || !((LocalPbItemInfo) this.list.get(position - 1)).getFileDate().equals(curFileDate)) {
            mLayout.setVisibility(0);
            mTextView.setText(((LocalPbItemInfo) this.list.get(position)).getFileDate());
        } else {
            mLayout.setVisibility(8);
        }
        Bitmap bitmap = (Bitmap) this.mLruCache.get(path);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.pictures_no);
        }
        return view;
    }
}
