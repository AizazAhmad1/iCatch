package com.icatch.ismartdv2016.View.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.Adapter.LocalVideoWallGridAdapter;
import com.icatch.ismartdv2016.Adapter.LocalVideoWallListAdapter;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.LocalVideoWallPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.LocalVideoWallView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

public class LocalVideoWallActivity extends AppCompatActivity implements LocalVideoWallView {
    private String TAG = "LocalVideoWallActivity";
    StickyGridHeadersGridView gridView;
    TextView headerView;
    FrameLayout listLayout;
    ListView listView;
    private MenuItem menuVideoWallType;
    private LocalVideoWallPresenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video_wall);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GlobalInfo.getInstance().setCurrentApp(this);
        this.gridView = (StickyGridHeadersGridView) findViewById(R.id.local_video_wall_grid_view);
        this.listView = (ListView) findViewById(R.id.local_video_wall_list_view);
        this.headerView = (TextView) findViewById(R.id.photo_wall_header);
        this.listLayout = (FrameLayout) findViewById(R.id.local_video_wall_list_layout);
        this.presenter = new LocalVideoWallPresenter(this);
        this.presenter.setView(this);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LocalVideoWallActivity.this.presenter.redirectToAnotherActivity(LocalVideoWallActivity.this, LocalVideoPbActivity.class, position);
            }
        });
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LocalVideoWallActivity.this.presenter.redirectToAnotherActivity(LocalVideoWallActivity.this, LocalVideoPbActivity.class, position);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        GlobalInfo.getInstance().setCurrentApp(this);
        this.presenter.loadLocalVideoWall();
        this.presenter.submitAppInfo();
    }

    protected void onDestroy() {
        super.onDestroy();
        AppLog.d(this.TAG, "onDestroy");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.presenter.loadLocalVideoWall();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                Log.d("AppStart", "home");
                break;
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                this.presenter.clearResource();
                this.presenter.removeActivity();
                this.presenter.destroyCamera();
                finish();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_video_wall, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_video_wall_type) {
            this.menuVideoWallType = item;
            this.presenter.changePreviewType();
        } else if (id == 16908332) {
            this.presenter.clearResource();
            this.presenter.removeActivity();
            this.presenter.destroyCamera();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setListViewVisibility(int visibility) {
        this.listLayout.setVisibility(visibility);
    }

    public void setGridViewVisibility(int visibility) {
        this.gridView.setVisibility(visibility);
    }

    public void setListViewAdapter(LocalVideoWallListAdapter localVideoWallListAdapter) {
        this.listView.setAdapter(localVideoWallListAdapter);
    }

    public void setGridViewAdapter(LocalVideoWallGridAdapter localVideoWallGridAdapter) {
        this.gridView.setAdapter(localVideoWallGridAdapter);
    }

    public void setListViewOnScrollListener(OnScrollListener onScrollListener) {
        this.listView.setOnScrollListener(onScrollListener);
    }

    public void setGridViewOnScrollListener(OnScrollListener onScrollListener) {
        this.gridView.setOnScrollListener(onScrollListener);
    }

    public void setListViewHeaderText(String headerText) {
        this.headerView.setText(headerText);
    }

    public View listViewFindViewWithTag(String tag) {
        return this.listView.findViewWithTag(tag);
    }

    public View gridViewFindViewWithTag(String tag) {
        return this.gridView.findViewWithTag(tag);
    }

    public void setMenuPreviewTypeIcon(int iconRes) {
        this.menuVideoWallType.setIcon(iconRes);
    }

    public int getGridViewNumColumns() {
        int num = this.gridView.getNumColumns();
        AppLog.d(this.TAG, "getGridViewNumColumns num=" + num);
        return num;
    }

    protected void onStop() {
        super.onStop();
    }
}
