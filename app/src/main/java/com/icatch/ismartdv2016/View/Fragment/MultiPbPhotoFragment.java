package com.icatch.ismartdv2016.View.Fragment;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallListAdapter;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.Listener.OnStatusChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.Presenter.MultiPbPhotoFragmentPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.MultiPbPhotoFragmentView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import java.util.List;

public class MultiPbPhotoFragment extends Fragment implements MultiPbPhotoFragmentView {
    private static final String TAG = "MultiPbPhotoFragment";
    TextView headerView;
    private boolean isCreated = false;
    private boolean isVisible = false;
    ListView listView;
    private OnStatusChangedListener modeChangedListener;
    StickyGridHeadersGridView multiPbPhotoGridView;
    FrameLayout multiPbPhotoListLayout;
    MultiPbPhotoFragmentPresenter presenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppLog.d(TAG, "MultiPbPhotoFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_multi_pb_photo, container, false);
        this.multiPbPhotoGridView = (StickyGridHeadersGridView) view.findViewById(R.id.multi_pb_photo_grid_view);
        this.listView = (ListView) view.findViewById(R.id.multi_pb_photo_list_view);
        this.headerView = (TextView) view.findViewById(R.id.photo_wall_header);
        this.multiPbPhotoListLayout = (FrameLayout) view.findViewById(R.id.multi_pb_photo_list_layout);
        this.presenter = new MultiPbPhotoFragmentPresenter(getActivity());
        this.presenter.setView(this);
        this.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int firstVisible = MultiPbPhotoFragment.this.listView.getFirstVisiblePosition();
                int lastVisible = MultiPbPhotoFragment.this.listView.getLastVisiblePosition();
                AppLog.d(MultiPbPhotoFragment.TAG, "listView onScrollStateChanged firstVisible=" + firstVisible + " lastVisible=" + lastVisible);
                if (MultiPbPhotoFragment.this.isVisible) {
                    MultiPbPhotoFragment.this.presenter.listViewLoadThumbnails(scrollState, firstVisible, (lastVisible - firstVisible) + 1);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (MultiPbPhotoFragment.this.isVisible) {
                    MultiPbPhotoFragment.this.presenter.listViewLoadOnceThumbnails(firstVisibleItem, visibleItemCount);
                }
            }
        });
        this.multiPbPhotoGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                AppLog.d(MultiPbPhotoFragment.TAG, "11333 view onScrollStateChanged firstVisible=" + view.getFirstVisiblePosition() + " lastVisible=" + view.getLastVisiblePosition());
                int firstVisible = MultiPbPhotoFragment.this.multiPbPhotoGridView.getFirstVisiblePosition();
                int lastVisible = MultiPbPhotoFragment.this.multiPbPhotoGridView.getLastVisiblePosition();
                AppLog.d(MultiPbPhotoFragment.TAG, "11333 onScrollStateChanged firstVisible=" + firstVisible + " lastVisible=" + lastVisible);
                if (MultiPbPhotoFragment.this.isVisible) {
                    MultiPbPhotoFragment.this.presenter.gridViewLoadThumbnails(scrollState, firstVisible, (lastVisible - firstVisible) + 1);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                AppLog.d(MultiPbPhotoFragment.TAG, "1122 onScroll firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount);
                if (MultiPbPhotoFragment.this.isVisible) {
                    MultiPbPhotoFragment.this.presenter.gridViewLoadOnceThumbnails(firstVisibleItem, visibleItemCount);
                }
            }
        });
        this.listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (MultiPbPhotoFragment.this.isVisible) {
                    MultiPbPhotoFragment.this.presenter.listViewEnterEditMode(position);
                }
                return true;
            }
        });
        this.multiPbPhotoGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                MultiPbPhotoFragment.this.presenter.gridViewEnterEditMode(position);
                return true;
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("1111", "listView.setOnItemClickListener");
                MultiPbPhotoFragment.this.presenter.listViewSelectOrCancelOnce(position);
            }
        });
        this.multiPbPhotoGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("1111", "multiPbPhotoGridView.setOnItemClickListener");
                MultiPbPhotoFragment.this.presenter.gridViewSelectOrCancelOnce(position);
            }
        });
        this.isCreated = true;
        return view;
    }

    public void onResume() {
        super.onResume();
        AppLog.d(TAG, "start onResume() isVisible=" + this.isVisible + " presenter=" + this.presenter);
        if (this.isVisible) {
            this.presenter.loadPhotoWall();
        }
        AppLog.d(TAG, "end onResume");
    }

    public void onStop() {
        AppLog.d(TAG, "start onStop()");
        super.onStop();
    }

    public void onDestroy() {
        AppLog.d(TAG, "start onDestroy()");
        super.onDestroy();
        this.presenter.clealAsytaskList();
        this.presenter.emptyFileList();
    }

    public void changePreviewType() {
        AppLog.d(TAG, "start changePreviewType presenter=" + this.presenter);
        if (this.presenter != null) {
            this.presenter.changePreviewType();
        }
    }

    public void quitEditMode() {
        this.presenter.quitEditMode();
    }

    public void setListViewVisibility(int visibility) {
        this.multiPbPhotoListLayout.setVisibility(visibility);
    }

    public void setGridViewVisibility(int visibility) {
        this.multiPbPhotoGridView.setVisibility(visibility);
    }

    public void setListViewAdapter(MultiPbPhotoWallListAdapter photoWallListAdapter) {
        this.listView.setAdapter(photoWallListAdapter);
    }

    public void setGridViewAdapter(MultiPbPhotoWallGridAdapter photoWallGridAdapter) {
        this.multiPbPhotoGridView.setAdapter(photoWallGridAdapter);
    }

    public void setListViewSelection(int position) {
        this.listView.setSelection(position);
    }

    public void setGridViewSelection(int position) {
        this.multiPbPhotoGridView.setSelection(position);
    }

    public void setListViewHeaderText(String headerText) {
        this.headerView.setText(headerText);
    }

    public View listViewFindViewWithTag(int tag) {
        return this.listView.findViewWithTag(Integer.valueOf(tag));
    }

    public View gridViewFindViewWithTag(int tag) {
        return this.multiPbPhotoGridView.findViewWithTag(Integer.valueOf(tag));
    }

    public void updateGridViewBitmaps(String tag, Bitmap bitmap) {
        ImageView imageView = (ImageView) this.multiPbPhotoGridView.findViewWithTag(tag);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void notifyChangeMultiPbMode(OperationMode operationMode) {
        if (this.modeChangedListener != null) {
            this.modeChangedListener.onEnterEditMode(operationMode);
        }
    }

    public void setPhotoSelectNumText(int selectNum) {
        if (this.modeChangedListener != null) {
            this.modeChangedListener.onSelectedItemsCountChanged(selectNum);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("1122", "MultiPbPhotoFragment onConfigurationChanged");
        this.presenter.refreshPhotoWall();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        AppLog.d(TAG, "setUserVisibleHint isVisibleToUser=" + isVisibleToUser);
        AppLog.d(TAG, "setUserVisibleHint isCreated=" + this.isCreated);
        this.isVisible = isVisibleToUser;
        if (!this.isCreated) {
            return;
        }
        if (isVisibleToUser) {
            this.presenter.loadPhotoWall();
            return;
        }
        this.presenter.quitEditMode();
        this.presenter.clealAsytaskList();
    }

    public void refreshPhotoWall() {
        this.presenter.refreshPhotoWall();
    }

    public void setOperationListener(OnStatusChangedListener modeChangedListener) {
        this.modeChangedListener = modeChangedListener;
    }

    public void selectOrCancelAll(boolean isSelectAll) {
        this.presenter.selectOrCancelAll(isSelectAll);
    }

    public List<MultiPbItemInfo> getSelectedList() {
        return this.presenter.getSelectedList();
    }

    public void clealAsytaskList() {
        this.presenter.clealAsytaskList();
    }
}
