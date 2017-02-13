package com.icatch.ismartdv2016.View.Fragment;

import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.MultiPbPhotoWallListAdapter;
import com.icatch.ismartdv2016.BaseItems.MultiPbItemInfo;
import com.icatch.ismartdv2016.BaseItems.PhotoWallPreviewType;
import com.icatch.ismartdv2016.Listener.OnStatusChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Mode.OperationMode;
import com.icatch.ismartdv2016.Presenter.MultiPbVideoFragmentPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.MultiPbVideoFragmentView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import java.util.List;

public class MultiPbVideoFragment extends Fragment implements MultiPbVideoFragmentView {
    String TAG;
    StickyGridHeadersGridView gridView;
    TextView headerView;
    private boolean isCreated;
    private boolean isVisible;
    private PhotoWallPreviewType layoutType;
    FrameLayout listLayout;
    ListView listView;
    private OnStatusChangedListener modeChangedListener;
    MultiPbVideoFragmentPresenter presenter;

    public MultiPbVideoFragment() {
        this.TAG = "MultiPbVideoFragment";
        this.isCreated = false;
        this.isVisible = false;
        this.layoutType = PhotoWallPreviewType.PREVIEW_TYPE_GRID;
        this.layoutType = this.layoutType;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_pb_video, container, false);
        this.gridView = (StickyGridHeadersGridView) view.findViewById(R.id.multi_pb_video_grid_view);
        this.listView = (ListView) view.findViewById(R.id.multi_pb_video_list_view);
        this.headerView = (TextView) view.findViewById(R.id.photo_wall_header);
        this.listLayout = (FrameLayout) view.findViewById(R.id.multi_pb_video_list_layout);
        this.presenter = new MultiPbVideoFragmentPresenter(getActivity());
        this.presenter.setView(this);
        this.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                AppLog.d(MultiPbVideoFragment.this.TAG, "ListView start onScrollStateChanged");
                int firstVisible = MultiPbVideoFragment.this.listView.getFirstVisiblePosition();
                int lastVisible = MultiPbVideoFragment.this.listView.getLastVisiblePosition();
                AppLog.d(MultiPbVideoFragment.this.TAG, "listView onScrollStateChanged firstVisible=" + firstVisible + " lastVisible=" + lastVisible);
                if (MultiPbVideoFragment.this.isVisible) {
                    MultiPbVideoFragment.this.presenter.listViewLoadThumbnails(scrollState, firstVisible, (lastVisible - firstVisible) + 1);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                AppLog.d(MultiPbVideoFragment.this.TAG, "ListView start onScroll");
                if (MultiPbVideoFragment.this.isVisible) {
                    MultiPbVideoFragment.this.presenter.listViewLoadOnceThumbnails(firstVisibleItem, visibleItemCount);
                }
            }
        });
        this.gridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                AppLog.d(MultiPbVideoFragment.this.TAG, "GridView start onScrollStateChanged isVisible=" + MultiPbVideoFragment.this.isVisible);
                int firstVisible = MultiPbVideoFragment.this.gridView.getFirstVisiblePosition();
                int lastVisible = MultiPbVideoFragment.this.gridView.getLastVisiblePosition();
                AppLog.d(MultiPbVideoFragment.this.TAG, "gridView onScrollStateChanged firstVisible=" + firstVisible + " lastVisible=" + lastVisible);
                if (MultiPbVideoFragment.this.isVisible) {
                    MultiPbVideoFragment.this.presenter.gridViewLoadThumbnails(scrollState, firstVisible, (lastVisible - firstVisible) + 1);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                AppLog.d(MultiPbVideoFragment.this.TAG, "GridView start onScroll");
                if (MultiPbVideoFragment.this.isVisible) {
                    MultiPbVideoFragment.this.presenter.gridViewLoadOnceThumbnails(firstVisibleItem, visibleItemCount);
                }
            }
        });
        this.listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                MultiPbVideoFragment.this.presenter.listViewEnterEditMode(position);
                return true;
            }
        });
        this.gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                MultiPbVideoFragment.this.presenter.gridViewEnterEditMode(position);
                return true;
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("1111", "listView.setOnItemClickListener");
                MultiPbVideoFragment.this.presenter.listViewSelectOrCancelOnce(position);
            }
        });
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("1111", "multiPbPhotoGridView.setOnItemClickListener");
                MultiPbVideoFragment.this.presenter.gridViewSelectOrCancelOnce(position);
            }
        });
        this.isCreated = true;
        return view;
    }

    public void onResume() {
        AppLog.d(this.TAG, "start onResume isVisible=" + this.isVisible);
        if (this.isVisible) {
            this.presenter.loadVideoWall();
        }
        super.onResume();
    }

    public void onStop() {
        AppLog.d(this.TAG, "start onStop()");
        super.onStop();
    }

    public void onDestroy() {
        AppLog.d(this.TAG, "start onDestroy()");
        this.presenter.clealAsytaskList();
        this.presenter.emptyFileList();
        super.onDestroy();
    }

    public void changePreviewType() {
        AppLog.d(this.TAG, "start changePreviewType presenter=" + this.presenter);
        if (this.presenter != null) {
            this.presenter.changePreviewType();
        }
    }

    public void quitEditMode() {
        this.presenter.quitEditMode();
    }

    public void setListViewVisibility(int visibility) {
        this.listLayout.setVisibility(visibility);
    }

    public void setGridViewVisibility(int visibility) {
        this.gridView.setVisibility(visibility);
    }

    public void setListViewAdapter(MultiPbPhotoWallListAdapter multiPbPhotoWallListAdapter) {
        this.listView.setAdapter(multiPbPhotoWallListAdapter);
    }

    public void setGridViewAdapter(MultiPbPhotoWallGridAdapter multiPbPhotoWallGridAdapter) {
        this.gridView.setAdapter(multiPbPhotoWallGridAdapter);
    }

    public void setListViewSelection(int position) {
        this.listView.setSelection(position);
    }

    public void setGridViewSelection(int position) {
        this.gridView.setSelection(position);
    }

    public void setListViewHeaderText(String headerText) {
        this.headerView.setText(headerText);
    }

    public View listViewFindViewWithTag(int tag) {
        return this.listView.findViewWithTag(Integer.valueOf(tag));
    }

    public View gridViewFindViewWithTag(int tag) {
        return this.gridView.findViewWithTag(Integer.valueOf(tag));
    }

    public void changeMultiPbMode(OperationMode operationMode) {
        if (this.modeChangedListener != null) {
            this.modeChangedListener.onEnterEditMode(operationMode);
        }
    }

    public void setVideoSelectNumText(int selectNum) {
        if (this.modeChangedListener != null) {
            this.modeChangedListener.onSelectedItemsCountChanged(selectNum);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.presenter.refreshPhotoWall();
    }

    public void refreshPhotoWall() {
        this.presenter.refreshPhotoWall();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        AppLog.d(this.TAG, "setUserVisibleHint isVisibleToUser=" + isVisibleToUser);
        AppLog.d(this.TAG, "setUserVisibleHint isCreated=" + this.isCreated);
        this.isVisible = isVisibleToUser;
        if (!this.isCreated) {
            return;
        }
        if (isVisibleToUser) {
            this.presenter.loadVideoWall();
            return;
        }
        this.presenter.quitEditMode();
        this.presenter.clealAsytaskList();
    }

    public void setOperationListener(OnStatusChangedListener modeChangedListener) {
        this.modeChangedListener = modeChangedListener;
    }

    public void select(boolean isSelectAll) {
        this.presenter.selectOrCancelAll(isSelectAll);
    }

    public List<MultiPbItemInfo> getSelectedList() {
        return this.presenter.getSelectedList();
    }

    public void clealAsytaskList() {
        this.presenter.clealAsytaskList();
    }
}
