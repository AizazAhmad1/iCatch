package com.icatch.ismartdv2016.View.Interface;

import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import com.icatch.ismartdv2016.Adapter.LocalPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.LocalPhotoWallListAdapter;

public interface LocalPhotoWallView {
    View gridViewFindViewWithTag(String str);

    View listViewFindViewWithTag(String str);

    void setGridViewAdapter(LocalPhotoWallGridAdapter localPhotoWallGridAdapter);

    void setGridViewOnScrollListener(OnScrollListener onScrollListener);

    void setGridViewSelection(int i);

    void setGridViewVisibility(int i);

    void setListViewAdapter(LocalPhotoWallListAdapter localPhotoWallListAdapter);

    void setListViewHeaderText(String str);

    void setListViewOnScrollListener(OnScrollListener onScrollListener);

    void setListViewSelection(int i);

    void setListViewVisibility(int i);

    void setMenuPhotoWallTypeIcon(int i);
}
