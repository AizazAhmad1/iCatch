package com.icatch.ismartdv2016.View.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.icatch.ismartdv2016.ExtendComponent.MPreview;
import com.icatch.ismartdv2016.ExtendComponent.ProgressWheel;
import com.icatch.ismartdv2016.Listener.VideoFramePtsChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.VideoPbPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.VideoPbView;

public class VideoPbActivity extends AppCompatActivity implements VideoPbView {
    private String TAG = "VideoPbActivity";
    private ImageButton back;
    private RelativeLayout bottomBar;
    private ImageButton deleteBtn;
    private ImageButton downloadBtn;
    private boolean isShowBar = true;
    private ImageButton play;
    private VideoPbPresenter presenter;
    private ProgressWheel progressWheel;
    private SeekBar seekBar;
    private ImageButton stopBtn;
    private TextView timeDuration;
    private TextView timeLapsed;
    private LinearLayout topBar;
    private TextView videoNameTxv;
    private MPreview videoPbView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_pb);
        this.timeLapsed = (TextView) findViewById(R.id.video_pb_time_lapsed);
        this.timeDuration = (TextView) findViewById(R.id.video_pb_time_duration);
        this.seekBar = (SeekBar) findViewById(R.id.video_pb_seekBar);
        this.play = (ImageButton) findViewById(R.id.video_pb_play_btn);
        this.back = (ImageButton) findViewById(R.id.video_pb_back);
        this.stopBtn = (ImageButton) findViewById(R.id.video_pb_stop_btn);
        this.topBar = (LinearLayout) findViewById(R.id.video_pb_top_layout);
        this.bottomBar = (RelativeLayout) findViewById(R.id.video_pb_bottom_layout);
        this.videoPbView = (MPreview) findViewById(R.id.video_pb_view);
        this.videoNameTxv = (TextView) findViewById(R.id.video_pb_video_name);
        this.progressWheel = (ProgressWheel) findViewById(R.id.video_pb_spinner);
        this.deleteBtn = (ImageButton) findViewById(R.id.delete);
        this.downloadBtn = (ImageButton) findViewById(R.id.download);
        this.presenter = new VideoPbPresenter(this);
        this.presenter.setView(this);
        this.videoPbView.addVideoFramePtsChangedListener(new VideoFramePtsChangedListener() {
            public void onFramePtsChanged(double pts) {
                VideoPbActivity.this.presenter.updatePbSeekbar(pts);
            }
        });
        this.videoPbView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoPbActivity.this.presenter.showBar(VideoPbActivity.this.topBar.getVisibility() == 0);
            }
        });
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoPbActivity.this.presenter.stopVideoPb();
                VideoPbActivity.this.presenter.removeEventListener();
                VideoPbActivity.this.finish();
            }
        });
        this.play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoPbActivity.this.presenter.play();
            }
        });
        this.stopBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoPbActivity.this.presenter.stopVideoPb();
            }
        });
        this.deleteBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoPbActivity.this.presenter.delete();
            }
        });
        this.downloadBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoPbActivity.this.presenter.download();
            }
        });
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                VideoPbActivity.this.presenter.setTimeLapsedValue(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                VideoPbActivity.this.presenter.seekBarOnStartTrackingTouch();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                VideoPbActivity.this.presenter.seekBarOnStopTrackingTouch();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.presenter.submitAppInfo();
    }

    protected void onStop() {
        super.onStop();
        this.presenter.isAppBackground();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.presenter.removeActivity();
    }

    public void setTopBarVisibility(int visibility) {
        this.topBar.setVisibility(visibility);
    }

    public void setBottomBarVisibility(int visibility) {
        this.bottomBar.setVisibility(visibility);
    }

    public void setPlayCircleImageViewVisibility(int visibility) {
    }

    public void setTimeLapsedValue(String value) {
        this.timeLapsed.setText(value);
    }

    public void setTimeDurationValue(String value) {
        this.timeDuration.setText(value);
    }

    public void setSeekBarProgress(int value) {
        this.seekBar.setProgress(value);
    }

    public void setSeekBarMaxValue(int value) {
        this.seekBar.setMax(value);
    }

    public int getSeekBarProgress() {
        return this.seekBar.getProgress();
    }

    public void setSeekBarSecondProgress(int value) {
        this.seekBar.setSecondaryProgress(value);
    }

    public void setPlayBtnSrc(int resid) {
        this.play.setImageResource(resid);
    }

    public void showLoadingCircle(boolean isShow) {
        AppLog.d(this.TAG, "showLoadingCircle isShow=" + isShow);
        if (isShow) {
            this.progressWheel.setVisibility(0);
            this.progressWheel.setText("0%");
            this.progressWheel.startSpinning();
            return;
        }
        this.progressWheel.stopSpinning();
        this.progressWheel.setVisibility(8);
    }

    public void setLoadPercent(int value) {
        this.progressWheel.setText(value + "%");
    }

    public void setVideoNameTxv(String value) {
        this.videoNameTxv.setText(value);
    }

    public void startMPreview(MyCamera mCamera, int previewLaunchMode) {
        this.videoPbView.start(mCamera, previewLaunchMode);
    }

    public void stopMPreview() {
        this.videoPbView.stop();
    }

    public void setSeekbarEnabled(boolean enabled) {
        this.seekBar.setEnabled(enabled);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindOffset /*3*/:
                Log.d("AppStart", "home");
                break;
            case com.slidingmenu.lib.R.styleable.SlidingMenu_behindWidth /*4*/:
                Log.d("AppStart", "back");
                this.presenter.stopVideoPb();
                this.presenter.removeEventListener();
                finish();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AppLog.d(this.TAG, "onConfigurationChanged");
    }

    public void refresh() {
        this.presenter.refresh();
    }
}
