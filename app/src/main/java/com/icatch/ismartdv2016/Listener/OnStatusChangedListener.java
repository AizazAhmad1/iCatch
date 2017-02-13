package com.icatch.ismartdv2016.Listener;

import com.icatch.ismartdv2016.Mode.OperationMode;

public interface OnStatusChangedListener {
    void onEnterEditMode(OperationMode operationMode);

    void onSelectedItemsCountChanged(int i);
}
