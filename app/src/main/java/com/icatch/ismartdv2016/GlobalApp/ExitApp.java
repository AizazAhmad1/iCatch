package com.icatch.ismartdv2016.GlobalApp;

import android.app.Activity;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.SdkApi.PreviewStream;
import com.icatch.ismartdv2016.SdkApi.VideoPlayback;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExitApp {
    private static ExitApp instance;
    private final String TAG = "ExitApp";
    private LinkedList<Activity> activityList = new LinkedList();

    public static ExitApp getInstance() {
        if (instance == null) {
            instance = new ExitApp();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (!this.activityList.contains(activity)) {
            this.activityList.addFirst(activity);
            AppLog.d("ExitApp", "addActivity activity=" + activity.getClass().getSimpleName());
            AppLog.d("ExitApp", "addActivity activityList size=" + this.activityList.size());
        }
    }

    public void removeActivity(Activity activity) {
        this.activityList.remove(activity);
        AppLog.d("ExitApp", "removeActivity activity=" + activity.getClass().getSimpleName());
        AppLog.d("ExitApp", "removeActivity activityList size=" + this.activityList.size());
    }

    public void exit() {
        AppLog.i("ExitApp", "start exit activity activityList size=" + this.activityList.size());
        if (!(this.activityList == null || this.activityList.isEmpty())) {
            Iterator it = this.activityList.iterator();
            while (it.hasNext()) {
                ((Activity) it.next()).finish();
            }
            this.activityList.clear();
        }
        AppLog.i("ExitApp", "end exit System.exit");
        System.exit(0);
    }

    public void exitWhenScreenOff() {
        Iterator it;
        List<MyCamera> cameraList = GlobalInfo.getInstance().getCameraList();
        if (!(cameraList == null || cameraList.isEmpty())) {
            PreviewStream previewStream = PreviewStream.getInstance();
            FileOperation fileOperation = FileOperation.getInstance();
            VideoPlayback videoPlayback = VideoPlayback.getInstance();
            for (MyCamera camera : cameraList) {
                if (camera.getSDKsession().isSessionOK()) {
                    fileOperation.cancelDownload(camera.getplaybackClient());
                    previewStream.stopMediaStream(camera.getpreviewStreamClient());
                    videoPlayback.stopPlaybackStream(camera.getVideoPlaybackClint());
                    camera.destroyCamera();
                }
            }
        }
        AppLog.i("ExitApp", "start finsh activity");
        if (this.activityList != null && !this.activityList.isEmpty()) {
            it = this.activityList.iterator();
            while (it.hasNext()) {
                ((Activity) it.next()).finish();
            }
            this.activityList.clear();
        }
    }

    public void finishAllActivity() {
        Iterator it;
        List<MyCamera> cameraList = GlobalInfo.getInstance().getCameraList();
        if (!(cameraList == null || cameraList.isEmpty())) {
            PreviewStream previewStream = PreviewStream.getInstance();
            FileOperation fileOperation = FileOperation.getInstance();
            VideoPlayback videoPlayback = VideoPlayback.getInstance();
            for (MyCamera camera : cameraList) {
                if (camera.getSDKsession().isSessionOK()) {
                    fileOperation.cancelDownload(camera.getplaybackClient());
                    previewStream.stopMediaStream(camera.getpreviewStreamClient());
                    videoPlayback.stopPlaybackStream(camera.getVideoPlaybackClint());
                    camera.destroyCamera();
                }
            }
        }
        AppLog.i("ExitApp", "start finsh activity");
        if (this.activityList != null && !this.activityList.isEmpty()) {
            it = this.activityList.iterator();
            while (it.hasNext()) {
                Activity activity = (Activity) it.next();
                AppLog.i("ExitApp", "finsh activity=" + activity);
                activity.finish();
            }
            this.activityList.clear();
        }
    }
}
