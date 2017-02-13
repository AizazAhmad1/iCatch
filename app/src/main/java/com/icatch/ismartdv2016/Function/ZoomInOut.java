package com.icatch.ismartdv2016.Function;

import com.icatch.ismartdv2016.ExtendComponent.ZoomView;
import com.icatch.ismartdv2016.Presenter.PreviewPresenter;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;

public class ZoomInOut {
    private static ZoomInOut zoomInOut;
    private int lastZoomRate = 10;
    private ZoomCompletedListener zoomCompletedListener;

    public interface ZoomCompletedListener {
        void onCompleted(int i);
    }

    public static synchronized ZoomInOut getInstance() {
        ZoomInOut zoomInOut;
        synchronized (ZoomInOut.class) {
            if (zoomInOut == null) {
                zoomInOut = new ZoomInOut();
            }
            zoomInOut = zoomInOut;
        }
        return zoomInOut;
    }

    private ZoomInOut() {
    }

    public void zoomIn() {
        if (this.lastZoomRate <= ZoomView.MAX_VALUE) {
            CameraAction.getInstance().zoomIn();
        }
        this.lastZoomRate = CameraProperties.getInstance().getCurrentZoomRatio();
    }

    public void zoomOut() {
        if (this.lastZoomRate >= 10) {
            CameraAction.getInstance().zoomOut();
        }
        this.lastZoomRate = CameraProperties.getInstance().getCurrentZoomRatio();
    }

    public void startZoomInOutThread(final PreviewPresenter presenter) {
        new Thread(new Runnable() {
            public void run() {
                ZoomInOut.this.zoom(presenter);
            }
        }).start();
    }

    private void zoom(PreviewPresenter presenter) {
        int maxZoomCount = 50;
        this.lastZoomRate = CameraProperties.getInstance().getCurrentZoomRatio();
        int maxZoomCount2;
        if (this.lastZoomRate > presenter.getZoomViewProgress()) {
            while (this.lastZoomRate > presenter.getZoomViewProgress() && this.lastZoomRate > 10) {
                maxZoomCount2 = maxZoomCount - 1;
                if (maxZoomCount > 0) {
                    CameraAction.getInstance().zoomOut();
                    this.lastZoomRate = CameraProperties.getInstance().getCurrentZoomRatio();
                    maxZoomCount = maxZoomCount2;
                }
            }
            this.zoomCompletedListener.onCompleted(this.lastZoomRate);
        }
        while (this.lastZoomRate < presenter.getZoomViewProgress() && this.lastZoomRate < ZoomView.MAX_VALUE) {
            maxZoomCount2 = maxZoomCount - 1;
            if (maxZoomCount > 0) {
                CameraAction.getInstance().zoomIn();
                this.lastZoomRate = CameraProperties.getInstance().getCurrentZoomRatio();
                maxZoomCount = maxZoomCount2;
            }
        }
        this.zoomCompletedListener.onCompleted(this.lastZoomRate);
        this.zoomCompletedListener.onCompleted(this.lastZoomRate);
    }

    public void addZoomCompletedListener(ZoomCompletedListener zoomCompletedListener) {
        this.zoomCompletedListener = zoomCompletedListener;
    }
}
