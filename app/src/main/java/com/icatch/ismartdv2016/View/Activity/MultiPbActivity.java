package com.icatch.ismartdv2016.View.Activity;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.MultiPbPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.Tools.FixedSpeedScroller;
import com.icatch.ismartdv2016.View.Interface.MultiPbView;
import java.lang.reflect.Field;

public class MultiPbActivity extends AppCompatActivity implements MultiPbView {
    private String TAG = "MultiPbActivity";
    ImageButton deleteBtn;
    ImageButton downloadBtn;
    MenuItem menuPhotoWallType;
    LinearLayout multiPbEditLayout;
    private MultiPbPresenter presenter;
    ImageButton selectBtn;
    TextView selectedNumTxv;
    TabLayout tabLayout;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pb);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.viewPager = (ViewPager) findViewById(R.id.vPager);
        this.selectBtn = (ImageButton) findViewById(R.id.action_select);
        this.deleteBtn = (ImageButton) findViewById(R.id.action_delete);
        this.downloadBtn = (ImageButton) findViewById(R.id.action_download);
        this.selectedNumTxv = (TextView) findViewById(R.id.info_selected_num);
        this.multiPbEditLayout = (LinearLayout) findViewById(R.id.edit_layout);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.presenter = new MultiPbPresenter(this);
        this.presenter.setView(this);
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                MultiPbActivity.this.presenter.updateViewpagerStatus(position);
            }

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });
        this.selectBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MultiPbActivity.this.presenter.selectOrCancel();
            }
        });
        this.deleteBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MultiPbActivity.this.presenter.delete();
            }
        });
        this.downloadBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MultiPbActivity.this.presenter.download();
            }
        });
        this.presenter.loadViewPage();
        this.tabLayout.setupWithViewPager(this.viewPager);
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this.viewPager.getContext(), new AccelerateInterpolator());
            field.set(this.viewPager, scroller);
            scroller.setmDuration(280);
        } catch (Exception e) {
            AppLog.e(this.TAG, "FixedSpeedScroller Exception");
        }
    }

    protected void onResume() {
        super.onResume();
        this.presenter.submitAppInfo();
        AppLog.d(this.TAG, "onResume()");
    }

    protected void onStop() {
        super.onStop();
        this.presenter.isAppBackground();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.presenter.clearCache();
        this.presenter.reset();
        this.presenter.removeActivity();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_multi_pb, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_multi_pb_preview_type) {
            this.menuPhotoWallType = item;
            this.presenter.changePreviewType();
        } else if (id == 16908332) {
            this.presenter.reback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                Log.d("AppStart", "home");
                break;
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                Log.d("AppStart", "back");
                this.presenter.reback();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void setViewPageAdapter(FragmentPagerAdapter adapter) {
        this.viewPager.setAdapter(adapter);
    }

    public void setViewPageCurrentItem(int item) {
        AppLog.d(this.TAG, "setViewPageCurrentItem item=" + item);
        this.viewPager.setCurrentItem(item);
    }

    public void setMenuPhotoWallTypeIcon(int iconRes) {
        this.menuPhotoWallType.setIcon(iconRes);
    }

    public void setViewPagerScanScroll(boolean isCanScroll) {
    }

    public void setSelectNumText(String text) {
        this.selectedNumTxv.setText(text);
    }

    public void setSelectBtnVisibility(int visibility) {
        this.selectBtn.setVisibility(visibility);
    }

    public void setSelectBtnIcon(int icon) {
        this.selectBtn.setImageResource(icon);
    }

    public void setSelectNumTextVisibility(int visibility) {
        this.selectedNumTxv.setVisibility(visibility);
    }

    public void setTabLayoutClickable(boolean value) {
        AppLog.d(this.TAG, "setTabLayoutClickable value=" + value);
        this.tabLayout.setClickable(value);
        if (VERSION.SDK_INT >= 23) {
            this.tabLayout.setContextClickable(value);
        }
        this.tabLayout.setFocusable(value);
        this.tabLayout.setLongClickable(value);
        this.tabLayout.setEnabled(value);
    }
}
