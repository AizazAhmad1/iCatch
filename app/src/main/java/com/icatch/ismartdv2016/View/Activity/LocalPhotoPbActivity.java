package com.icatch.ismartdv2016.View.Activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.icatch.ismartdv2016.ExtendComponent.HackyViewPager;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.LocalPhotoPbPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.LocalPhotoPbView;
import uk.co.senab.photoview.PhotoView;

public class LocalPhotoPbActivity extends AppCompatActivity implements LocalPhotoPbView {
    private static final String TAG = LocalPhotoPbActivity.class.getSimpleName();
    private ImageButton back;
    private LinearLayout bottomBar;
    private ImageButton deleteBtn;
    private TextView indexInfoTxv;
    private LocalPhotoPbPresenter presenter;
    private ImageButton shareBtn;
    private RelativeLayout topBar;
    private HackyViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_photo_pb);
        this.viewPager = (HackyViewPager) findViewById(R.id.viewpager);
        this.indexInfoTxv = (TextView) findViewById(R.id.pb_index_info);
        this.shareBtn = (ImageButton) findViewById(R.id.photo_pb_share);
        this.deleteBtn = (ImageButton) findViewById(R.id.photo_pb_delete);
        this.topBar = (RelativeLayout) findViewById(R.id.pb_top_layout);
        this.bottomBar = (LinearLayout) findViewById(R.id.pb_bottom_layout);
        this.back = (ImageButton) findViewById(R.id.pb_back);
        this.presenter = new LocalPhotoPbPresenter(this);
        this.presenter.setView(this);
        this.viewPager.setPageMargin(30);
        this.viewPager.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AppLog.d(LocalPhotoPbActivity.TAG, "viewPager.setOnClickListener");
                LocalPhotoPbActivity.this.presenter.showBar();
            }
        });
        this.shareBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalPhotoPbActivity.this.presenter.share();
            }
        });
        this.deleteBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalPhotoPbActivity.this.presenter.delete();
            }
        });
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalPhotoPbActivity.this.finish();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.presenter.loadImage();
        this.presenter.submitAppInfo();
    }

    public void setViewPagerAdapter(PagerAdapter adapter) {
        if (adapter != null) {
            this.viewPager.setAdapter(adapter);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.presenter.removeActivity();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.presenter.reloadBitmap();
    }

    public void setTopBarVisibility(int visibility) {
        this.topBar.setVisibility(visibility);
    }

    public void setBottomBarVisibility(int visibility) {
        this.bottomBar.setVisibility(visibility);
    }

    public void setIndexInfoTxv(String indexInfo) {
        this.indexInfoTxv.setText(indexInfo);
    }

    public void setViewPagerCurrentItem(int position) {
        this.viewPager.setCurrentItem(position);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.viewPager.addOnPageChangeListener(listener);
    }

    public int getViewPagerCurrentItem() {
        return this.viewPager.getCurrentItem();
    }

    public void updateViewPagerBitmap(int position, Bitmap bitmap) {
        View view = this.viewPager.getChildAt(position);
        AppLog.d(TAG, "updateViewPagerBitmap position=" + position + " view=" + view);
        if (view != null) {
            PhotoView photoView = (PhotoView) view.findViewById(R.id.photo);
            ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar1);
            if (photoView != null) {
                photoView.setImageBitmap(bitmap);
            }
            if (bar != null) {
                bar.setVisibility(8);
            }
        }
    }

    public int getTopBarVisibility() {
        return this.topBar.getVisibility();
    }
}
