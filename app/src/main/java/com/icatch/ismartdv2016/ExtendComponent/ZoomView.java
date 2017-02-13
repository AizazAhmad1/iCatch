package com.icatch.ismartdv2016.ExtendComponent;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import java.util.Timer;
import java.util.TimerTask;

public class ZoomView extends RelativeLayout {
    private static final int DISPLAY_DURATION = 5000;
    public static int MAX_VALUE = 0;
    public static final int MIN_VALUE = 10;
    private static final String TAG = "ZoomView";
    public boolean firstCreate = true;
    private Timer timer;
    private final ZoomBar zoomBar;
    private final ImageButton zoomIn;
    private final ImageButton zoomOut;
    private final TextView zoomRateText;

    public ZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View zoomView = LayoutInflater.from(context).inflate(R.layout.zoombar_view, this, true);
        this.zoomIn = (ImageButton) zoomView.findViewById(R.id.zoom_in);
        this.zoomOut = (ImageButton) zoomView.findViewById(R.id.zoom_out);
        this.zoomBar = (ZoomBar) zoomView.findViewById(R.id.zoomBar);
        this.zoomRateText = (TextView) zoomView.findViewById(R.id.zoom_rate);
        setMinValue(MIN_VALUE);
        post(new Runnable() {
            public void run() {
                ZoomView.this.startDisplay();
            }
        });
    }

    public void setZoomInOnclickListener(OnClickListener onclickListener) {
        this.zoomIn.setOnClickListener(onclickListener);
    }

    public void setZoomOutOnclickListener(OnClickListener onclickListener) {
        this.zoomOut.setOnClickListener(onclickListener);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.zoomBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    private void updateZoomRateText(float zoomRate) {
        this.zoomRateText.setText("x " + zoomRate);
    }

    public void updateZoomBarValue(int value) {
        AppLog.i(TAG, "updateZoomBarValue value =" + value);
        this.zoomBar.setProgress(value);
        updateZoomRateText(((float) value) / 10.0f);
    }

    public void setMinValue(int minValue) {
        this.zoomBar.setMinValue(minValue);
    }

    public void setMaxValue(int maxValue) {
        MAX_VALUE = maxValue;
        this.zoomBar.setMax(maxValue);
    }

    public int getProgress() {
        return this.zoomBar.getZoomProgress();
    }

    public void startDisplay() {
        if (this.firstCreate) {
            this.firstCreate = false;
        } else if (CameraProperties.getInstance().hasFuction(ICatchCameraProperty.ICH_CAP_DIGITAL_ZOOM)) {
            if (this.timer != null) {
                this.timer.cancel();
            }
            this.timer = new Timer(true);
            this.timer.schedule(new TimerTask() {
                public void run() {
                    Handler handler = ZoomView.this.getHandler();
                    if (handler != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                ZoomView.this.setVisibility(8);
                            }
                        });
                    }
                }
            }, 5000);
            setVisibility(0);
        }
    }
}
