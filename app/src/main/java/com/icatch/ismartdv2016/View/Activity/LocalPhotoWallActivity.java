package com.icatch.ismartdv2016.View.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.Adapter.LocalPhotoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.LocalPhotoWallListAdapter;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Presenter.LocalPhotoWallPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.LocalPhotoWallView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

public class LocalPhotoWallActivity extends AppCompatActivity implements LocalPhotoWallView {
    String TAG = "LocalPhotoWallActivity";
    StickyGridHeadersGridView localPhotoGridView;
    TextView localPhotoHeaderView;
    ListView localPhotoListView;
    FrameLayout localPhotoWallListLayout;
    MenuItem menuPhotoWallType;
    private LocalPhotoWallPresenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_photo_wall);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GlobalInfo.getInstance().setCurrentApp(this);
        this.localPhotoGridView = (StickyGridHeadersGridView) findViewById(R.id.local_photo_wall_grid_view);
        this.localPhotoListView = (ListView) findViewById(R.id.local_photo_wall_list_view);
        this.localPhotoHeaderView = (TextView) findViewById(R.id.photo_wall_header);
        this.localPhotoWallListLayout = (FrameLayout) findViewById(R.id.local_photo_wall_list_layout);
        this.presenter = new LocalPhotoWallPresenter(this);
        this.presenter.setView(this);
        this.localPhotoListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LocalPhotoWallActivity.this.presenter.redirectToAnotherActivity(LocalPhotoWallActivity.this, LocalPhotoPbActivity.class, position);
            }
        });
        this.localPhotoGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LocalPhotoWallActivity.this.presenter.redirectToAnotherActivity(LocalPhotoWallActivity.this, LocalPhotoPbActivity.class, position);
            }
        });
        this.localPhotoListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LocalPhotoWallActivity.this.presenter.listViewLoadThumbnails(scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                LocalPhotoWallActivity.this.presenter.listViewLoadOnceThumbnails(firstVisibleItem, visibleItemCount);
            }
        });
        this.localPhotoGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LocalPhotoWallActivity.this.presenter.gridViewLoadThumbnails(scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                LocalPhotoWallActivity.this.presenter.gridViewLoadOnceThumbnails(firstVisibleItem, visibleItemCount);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.presenter.loadLocalPhotoWall();
        this.presenter.submitAppInfo();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.presenter.clearResource();
        this.presenter.removeActivity();
    }

    protected void onStop() {
        super.onStop();
        this.presenter.isAppBackground();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_photo_wall, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_photo_wall_type) {
            this.menuPhotoWallType = item;
            this.presenter.changePreviewType();
        } else if (id == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setListViewVisibility(int visibility) {
        this.localPhotoWallListLayout.setVisibility(visibility);
    }

    public void setGridViewVisibility(int visibility) {
        this.localPhotoGridView.setVisibility(visibility);
    }

    public void setListViewAdapter(LocalPhotoWallListAdapter localPhotoWallListAdapter) {
        this.localPhotoListView.setAdapter(localPhotoWallListAdapter);
    }

    public void setGridViewAdapter(LocalPhotoWallGridAdapter localLocalPhotoWallGridAdapter) {
        this.localPhotoGridView.setAdapter(localLocalPhotoWallGridAdapter);
    }

    public void setListViewSelection(int position) {
        this.localPhotoListView.setSelection(position);
    }

    public void setGridViewSelection(int position) {
        this.localPhotoGridView.setSelection(position);
    }

    public void setListViewOnScrollListener(OnScrollListener onScrollListener) {
        this.localPhotoListView.setOnScrollListener(onScrollListener);
    }

    public void setGridViewOnScrollListener(OnScrollListener onScrollListener) {
        this.localPhotoGridView.setOnScrollListener(onScrollListener);
    }

    public void setListViewHeaderText(String headerText) {
        this.localPhotoHeaderView.setText(headerText);
    }

    public View listViewFindViewWithTag(String tag) {
        return this.localPhotoListView.findViewWithTag(tag);
    }

    public View gridViewFindViewWithTag(String tag) {
        return this.localPhotoGridView.findViewWithTag(tag);
    }

    public void setMenuPhotoWallTypeIcon(int id) {
        this.menuPhotoWallType.setIcon(id);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.presenter.loadLocalPhotoWall();
    }
}
