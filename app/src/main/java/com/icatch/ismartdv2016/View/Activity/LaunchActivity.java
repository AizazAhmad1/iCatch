package com.icatch.ismartdv2016.View.Activity;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.icatch.ismartdv2016.Adapter.CameraSlotAdapter;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnFragmentInteractionListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.LaunchPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.Tools.LruCacheTool;
import com.icatch.ismartdv2016.Tools.PermissionTools;
import com.icatch.ismartdv2016.View.Interface.LaunchView;
import com.icatch.wificam.customer.type.ICatchEventID;

public class LaunchActivity extends AppCompatActivity implements OnClickListener, OnFragmentInteractionListener, LaunchView {
    private static String TAG = "LaunchActivity";
    private String TEST = "LaunchActivityTEST";
    private ActionBar actionBar;
    private AppBarLayout appBarLayout;
    private ListView camSlotListView;
    private String currentFragment;
    private LinearLayout launchLayout;
    private FrameLayout launchSettingFrame;
    private ImageView localPhoto;
    private ImageView localVideo;
    private MenuItem menuDone;
    private MenuItem menuSearch;
    private TextView noPhotosFound;
    private TextView noVideosFound;
    private LaunchPresenter presenter;
    private final String tag = "LaunchActivity";

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(this.TEST, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(false);
        PermissionTools.RequestPermissions(this);
        this.appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        this.launchLayout = (LinearLayout) findViewById(R.id.launch_view);
        this.launchSettingFrame = (FrameLayout) findViewById(R.id.launch_setting_frame);
        this.noPhotosFound = (TextView) findViewById(R.id.no_local_photos);
        this.noVideosFound = (TextView) findViewById(R.id.no_local_videos);
        this.localVideo = (ImageView) findViewById(R.id.local_video);
        this.localVideo.setOnClickListener(this);
        this.localPhoto = (ImageView) findViewById(R.id.local_photo);
        this.localPhoto.setOnClickListener(this);
        this.presenter = new LaunchPresenter(this);
        this.presenter.setView(this);
        GlobalInfo.getInstance().addEventListener(19, false);
        this.camSlotListView = (ListView) findViewById(R.id.cam_slot_listview);
        this.camSlotListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                LaunchActivity.this.presenter.removeCamera(position);
                return false;
            }
        });
        this.camSlotListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FragmentManager fm = LaunchActivity.this.getSupportFragmentManager();
                LaunchActivity.this.getSupportFragmentManager();
                LaunchActivity.this.presenter.launchCamera(position, fm);
            }
        });
        LruCacheTool.getInstance().initLruCache();
    }

    protected void onStart() {
        super.onStart();
        Log.d(this.TEST, "onStart");
    }

    protected void onResume() {
        Log.d(this.TEST, "onResume");
        AppLog.i("LaunchActivity", "Start onResume");
        super.onResume();
        this.presenter.loadListview();
        if (VERSION.SDK_INT < 23 || PermissionTools.CheckSelfPermission(this)) {
            this.presenter.loadLocalThumbnails();
        }
        GlobalInfo.getInstance().setCurrentApp(this);
        AppLog.i("LaunchActivity", "End onResume");
    }

    public void reloadListView() {
        this.presenter.loadListview();
    }

    protected void onStop() {
        super.onStop();
        Log.d(this.TEST, "onStop");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                Log.d("AppStart", "home");
                break;
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                Log.d("AppStart", "back");
                removeFragment();
                break;
            case ICatchEventID.ICH_EVENT_CAPTURE_START /*82*/:
                Log.d("AppStart", "KEYCODE_MENU");
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    protected void onDestroy() {
        Log.d(this.TEST, "onDestroy");
        super.onDestroy();
        GlobalInfo.getInstance().delEventListener(19, false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        AppLog.d(TAG, "onPrepareOptionsMenu");
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AppLog.i("LaunchActivity", "id =" + id);
        AppLog.i("LaunchActivity", "R.id.home =2131689476");
        AppLog.i("LaunchActivity", "R.id.action_search =2131689803");
        if (id == R.id.action_search) {
            this.presenter.startSearchCamera();
        }
        if (id != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        removeFragment();
        return true;
    }

    public void onClick(View v) {
        AppLog.i("LaunchActivity", "click info:::v.getId() =" + v.getId());
        AppLog.i("LaunchActivity", "click info:::R.id.local_photo =2131689629");
        AppLog.i("LaunchActivity", "click info:::R.id.local_video =2131689631");
        switch (v.getId()) {
            case R.id.local_photo /*2131689629*/:
                AppLog.i("LaunchActivity", "click the local photo");
                this.presenter.redirectToAnotherActivity(this, LocalPhotoWallActivity.class);
                return;
            case R.id.local_video /*2131689631*/:
                this.presenter.redirectToAnotherActivity(this, LocalVideoWallActivity.class);
                return;
            default:
                return;
        }
    }

    public void setLocalPhotoThumbnail(Bitmap bitmap) {
        this.localPhoto.setImageBitmap(bitmap);
    }

    public void setLocalVideoThumbnail(Bitmap bitmap) {
        this.localVideo.setImageBitmap(bitmap);
    }

    public void loadDefaultLocalPhotoThumbnail() {
        this.localPhoto.setImageResource(R.drawable.local_default_thumbnail);
    }

    public void loadDefaultLocalVideooThumbnail() {
        this.localVideo.setImageResource(R.drawable.local_default_thumbnail);
    }

    public void setNoPhotoFilesFoundVisibility(int visibility) {
        this.noPhotosFound.setVisibility(visibility);
    }

    public void setNoVideoFilesFoundVisibility(int visibility) {
        this.noVideosFound.setVisibility(visibility);
    }

    public void setPhotoClickable(boolean clickable) {
        this.localPhoto.setEnabled(clickable);
    }

    public void setVideoClickable(boolean clickable) {
        this.localVideo.setEnabled(clickable);
    }

    public void setListviewAdapter(CameraSlotAdapter cameraSlotAdapter) {
        this.camSlotListView.setAdapter(cameraSlotAdapter);
    }

    public void setBackBtnVisibility(boolean visibility) {
        if (this.actionBar != null) {
            this.actionBar.setDisplayHomeAsUpEnabled(visibility);
        }
    }

    public void setNavigationTitle(int resId) {
        if (this.actionBar != null) {
            this.actionBar.setTitle(resId);
        }
    }

    public void setNavigationTitle(String res) {
        if (this.actionBar != null) {
            this.actionBar.setTitle(res);
        }
    }

    public void setLaunchLayoutVisibility(int visibility) {
        this.launchLayout.setVisibility(visibility);
        this.appBarLayout.setVisibility(visibility);
    }

    public void setLaunchSettingFrameVisibility(int visibility) {
        this.launchSettingFrame.setVisibility(visibility);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ICatchEventID.ICH_EVENT_VIDEO_TRIM_DONE /*102*/:
                AppLog.i("LaunchActivity", "permissions.length = " + permissions.length);
                AppLog.i("LaunchActivity", "grantResults.length = " + grantResults.length);
                boolean retValue = false;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == 0) {
                        Toast.makeText(this, "Request write storage ", 0).show();
                        retValue = true;
                    } else {
                        retValue = false;
                    }
                }
                if (retValue) {
                    this.presenter.loadLocalThumbnails();
                    return;
                } else {
                    Toast.makeText(this, "Request write storage failed!", 0).show();
                    return;
                }
            default:
                return;
        }
    }

    public void submitFragmentInfo(String fragment, int resId) {
        this.currentFragment = fragment;
    }

    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            setNavigationTitle((int) R.string.app_name);
            this.launchSettingFrame.setVisibility(8);
            this.launchLayout.setVisibility(0);
            this.appBarLayout.setVisibility(0);
            setBackBtnVisibility(false);
        }
        getSupportFragmentManager().popBackStack();
    }

    public void fragmentPopStackOfAll() {
        int fragmentBackStackNum = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentBackStackNum; i++) {
            getSupportFragmentManager().popBackStack();
        }
        setBackBtnVisibility(false);
        setNavigationTitle((int) R.string.app_name);
        this.launchSettingFrame.setVisibility(8);
        this.launchLayout.setVisibility(0);
        this.appBarLayout.setVisibility(0);
    }

    public void startScreenListener() {
        GlobalInfo.getInstance().startScreenListener();
    }
}
