package com.icatch.ismartdv2016.View.Interface;

import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import com.icatch.ismartdv2016.Adapter.LocalVideoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.LocalVideoWallListAdapter;

public interface LocalVideoWallView {
    int getGridViewNumColumns();

    View gridViewFindViewWithTag(String str);

    View listViewFindViewWithTag(String str);

    void setGridViewAdapter(LocalVideoWallGridAdapter localVideoWallGridAdapter);

    void setGridViewOnScrollListener(OnScrollListener onScrollListener);

    void setGridViewVisibility(int i);

    void setListViewAdapter(LocalVideoWallListAdapter localVideoWallListAdapter);

    void setListViewHeaderText(String str);

    void setListViewOnScrollListener(OnScrollListener onScrollListener);

    void setListViewVisibility(int i);

    void setMenuPreviewTypeIcon(int i);
}
