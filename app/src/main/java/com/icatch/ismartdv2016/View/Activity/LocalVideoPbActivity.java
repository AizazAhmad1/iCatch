package com.icatch.ismartdv2016.View.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.icatch.ismartdv2016.ExtendComponent.MPreview;
import com.icatch.ismartdv2016.ExtendComponent.ProgressWheel;
import com.icatch.ismartdv2016.Listener.VideoFramePtsChangedListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.Presenter.LocalVideoPbPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.LocalVideoPbView;

public class LocalVideoPbActivity extends AppCompatActivity implements LocalVideoPbView {
    private String TAG = "LocalVideoPbActivity";
    private ImageButton back;
    private RelativeLayout bottomBar;
    private ImageButton delete;
    private boolean isShowBar = true;
    private MPreview localPbView;
    private TextView localVideoNameTxv;
    private ImageButton play;
    private LocalVideoPbPresenter presenter;
    private ProgressWheel progressWheel;
    private SeekBar seekBar;
    private ImageButton share;
    private TextView timeDuration;
    private TextView timeLapsed;
    private RelativeLayout topBar;
    private String videoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_local_video);
        this.timeLapsed = (TextView) findViewById(R.id.local_pb_time_lapsed);
        this.timeDuration = (TextView) findViewById(R.id.local_pb_time_duration);
        this.seekBar = (SeekBar) findViewById(R.id.local_pb_seekBar);
        this.play = (ImageButton) findViewById(R.id.local_pb_play_btn);
        this.back = (ImageButton) findViewById(R.id.local_pb_back);
        this.share = (ImageButton) findViewById(R.id.shareBtn);
        this.delete = (ImageButton) findViewById(R.id.deleteBtn);
        this.topBar = (RelativeLayout) findViewById(R.id.local_pb_top_layout);
        this.bottomBar = (RelativeLayout) findViewById(R.id.local_pb_bottom_layout);
        this.localPbView = (MPreview) findViewById(R.id.local_pb_view);
        this.localVideoNameTxv = (TextView) findViewById(R.id.local_pb_video_name);
        this.progressWheel = (ProgressWheel) findViewById(R.id.local_pb_spinner);
        this.videoPath = getIntent().getExtras().getString("curfilePath");
        AppLog.i(this.TAG, "photoPath=" + this.videoPath);
        this.presenter = new LocalVideoPbPresenter(this, this.videoPath);
        this.presenter.setView(this);
        getWindow().setFlags(128, 128);
        getWindow().addFlags(AppMessage.PHOTO_PBACTIVITY);
        this.localPbView.addVideoFramePtsChangedListener(new VideoFramePtsChangedListener() {
            public void onFramePtsChanged(double pts) {
                LocalVideoPbActivity.this.presenter.updatePbSeekbar(pts);
            }
        });
        this.localPbView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalVideoPbActivity.this.presenter.showBar(LocalVideoPbActivity.this.topBar.getVisibility() == 0);
            }
        });
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalVideoPbActivity.this.presenter.stopVideoPb();
                LocalVideoPbActivity.this.presenter.removeEventListener();
                LocalVideoPbActivity.this.finish();
            }
        });
        this.play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalVideoPbActivity.this.presenter.play();
            }
        });
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LocalVideoPbActivity.this.presenter.setTimeLapsedValue(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                LocalVideoPbActivity.this.presenter.seekBarOnStartTrackingTouch();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                LocalVideoPbActivity.this.presenter.seekBarOnStopTrackingTouch();
            }
        });
        this.share.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalVideoPbActivity.this.presenter.share();
            }
        });
        this.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LocalVideoPbActivity.this.presenter.delete();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.presenter.submitAppInfo();
    }

    protected void onStop() {
        super.onStop();
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
        this.localVideoNameTxv.setText(value);
    }

    public void startMPreview(MyCamera mCamera, int previewLaunchMode) {
        this.localPbView.start(mCamera, previewLaunchMode);
    }

    public void stopMPreview() {
        this.localPbView.stop();
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
}
