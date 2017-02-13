package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import com.icatch.ismartdv2016.BaseItems.LocalPbItemInfo;
import com.icatch.ismartdv2016.ExtendComponent.ProgressWheel;
import com.icatch.ismartdv2016.R;
import java.util.List;
import uk.co.senab.photoview.PhotoView;

public class LocalPhotoPbViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "PhotoPbViewPagerAdapter";
    private Context context;
    private List<LocalPbItemInfo> filesList;
    LruCache<String, Bitmap> mLruCache;
    private OnPhotoTapListener onPhotoTapListener;
    private List<View> viewList;

    public interface OnPhotoTapListener {
        void onPhotoTap();
    }

    public LocalPhotoPbViewPagerAdapter(Context context, List<LocalPbItemInfo> filesList, List<View> viewList, LruCache<String, Bitmap> mLruCache) {
        this.filesList = filesList;
        this.context = context;
        this.viewList = viewList;
        this.mLruCache = mLruCache;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < this.filesList.size()) {
            container.removeView((View) object);
        }
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View v = View.inflate(this.context, R.layout.pb_photo_item, null);
        Bitmap bitmap = (Bitmap) this.mLruCache.get(((LocalPbItemInfo) this.filesList.get(position)).file.getPath());
        PhotoView photoView = (PhotoView) v.findViewById(R.id.photo);
        ProgressWheel progressBar = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        if (photoView != null) {
            if (bitmap != null) {
                photoView.setImageBitmap(bitmap);
            }
            photoView.setOnPhotoTapListener(new uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener() {
                public void onPhotoTap(View view, float v, float v1) {
                    if (LocalPhotoPbViewPagerAdapter.this.onPhotoTapListener != null) {
                        LocalPhotoPbViewPagerAdapter.this.onPhotoTapListener.onPhotoTap();
                    }
                }

                public void onOutsidePhotoTap() {
                }
            });
        }
        this.viewList.set(position, v);
        container.addView(v, 0);
        return v;
    }

    public int getCount() {
        return this.filesList.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.onPhotoTapListener = onPhotoTapListener;
    }
}
