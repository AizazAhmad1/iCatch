package com.icatch.ismartdv2016.View.Interface;

import android.graphics.Bitmap;
import android.view.View;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallListAdapter;
import com.icatch.ismartdv2016.Mode.OperationMode;

public interface MultiPbPhotoFragmentView {
    View gridViewFindViewWithTag(int i);

    View listViewFindViewWithTag(int i);

    void notifyChangeMultiPbMode(OperationMode operationMode);

    void setGridViewAdapter(MultiPbPhotoWallGridAdapter multiPbPhotoWallGridAdapter);

    void setGridViewSelection(int i);

    void setGridViewVisibility(int i);

    void setListViewAdapter(MultiPbPhotoWallListAdapter multiPbPhotoWallListAdapter);

    void setListViewHeaderText(String str);

    void setListViewSelection(int i);

    void setListViewVisibility(int i);

    void setPhotoSelectNumText(int i);

    void updateGridViewBitmaps(String str, Bitmap bitmap);
}
