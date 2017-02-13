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
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.R;
import java.util.LinkedList;
import java.util.List;

public class MultiPbPhotoWallListAdapter extends BaseAdapter {
    private String TAG = "MultiPbPhotoWallListAdapter";
    private Context context;
    private OperationMode curMode = OperationMode.MODE_BROWSE;
    private FileType fileType;
    private List<MultiPbItemInfo> list;
    LruCache<Integer, Bitmap> mLruCache;

    public MultiPbPhotoWallListAdapter(Context context, List<MultiPbItemInfo> list, LruCache<Integer, Bitmap> mLruCache, FileType fileType) {
        this.context = context;
        this.list = list;
        this.mLruCache = mLruCache;
        this.fileType = fileType;
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
        int fileHandle = ((MultiPbItemInfo) this.list.get(position)).getFileHandle();
        String curFileDate = ((MultiPbItemInfo) this.list.get(position)).getFileDate();
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
        ((TextView) view.findViewById(R.id.local_photo_name)).setText(((MultiPbItemInfo) this.list.get(position)).getFileName());
        imageSizeTextView.setText(((MultiPbItemInfo) this.list.get(position)).getFileSize());
        imageDateTextView.setText(((MultiPbItemInfo) this.list.get(position)).getFileDateMMSS());
        if (this.fileType == FileType.FILE_PHOTO) {
            videoSignImageView.setVisibility(8);
        } else {
            videoSignImageView.setVisibility(0);
        }
        if (this.curMode == OperationMode.MODE_EDIT) {
            mCheckImageView.setVisibility(0);
            if (((MultiPbItemInfo) this.list.get(position)).isItemChecked) {
                mCheckImageView.setImageResource(R.drawable.ic_check_box_blue);
            } else {
                mCheckImageView.setImageResource(R.drawable.ic_check_box_blank_grey);
            }
        } else {
            mCheckImageView.setVisibility(8);
        }
        imageView.setTag(Integer.valueOf(fileHandle));
        if (position == 0 || !((MultiPbItemInfo) this.list.get(position - 1)).getFileDate().equals(curFileDate)) {
            mLayout.setVisibility(0);
            mTextView.setText(((MultiPbItemInfo) this.list.get(position)).getFileDate());
        } else {
            mLayout.setVisibility(8);
        }
        Bitmap bitmap = (Bitmap) this.mLruCache.get(Integer.valueOf(fileHandle));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.pictures_no);
        }
        return view;
    }

    public void setOperationMode(OperationMode operationMode) {
        this.curMode = operationMode;
    }

    public void changeSelectionState(int position) {
        boolean z;
        MultiPbItemInfo multiPbItemInfo = (MultiPbItemInfo) this.list.get(position);
        if (((MultiPbItemInfo) this.list.get(position)).isItemChecked) {
            z = false;
        } else {
            z = true;
        }
        multiPbItemInfo.isItemChecked = z;
        notifyDataSetChanged();
    }

    public List<MultiPbItemInfo> getSelectedList() {
        LinkedList<MultiPbItemInfo> checkedList = new LinkedList();
        for (int ii = 0; ii < this.list.size(); ii++) {
            if (((MultiPbItemInfo) this.list.get(ii)).isItemChecked) {
                checkedList.add(this.list.get(ii));
            }
        }
        return checkedList;
    }

    public void quitEditMode() {
        this.curMode = OperationMode.MODE_BROWSE;
        for (int ii = 0; ii < this.list.size(); ii++) {
            ((MultiPbItemInfo) this.list.get(ii)).isItemChecked = false;
        }
        notifyDataSetChanged();
    }

    public void selectAllItems() {
        for (int ii = 0; ii < this.list.size(); ii++) {
            ((MultiPbItemInfo) this.list.get(ii)).isItemChecked = true;
        }
        notifyDataSetChanged();
    }

    public void cancelAllSelections() {
        for (int ii = 0; ii < this.list.size(); ii++) {
            ((MultiPbItemInfo) this.list.get(ii)).isItemChecked = false;
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        int checkedNum = 0;
        for (int ii = 0; ii < this.list.size(); ii++) {
            if (((MultiPbItemInfo) this.list.get(ii)).isItemChecked) {
                checkedNum++;
            }
        }
        return checkedNum;
    }
}
