package com.icatch.ismartdv2016.SdkApi;

import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.wificam.customer.ICatchWificamPlayback;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchNoSuchPathException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class FileOperation {
    private static FileOperation instance;
    private ICatchWificamPlayback cameraPlayback;
    private final String tag = "FileOperation";

    public static FileOperation getInstance() {
        if (instance == null) {
            instance = new FileOperation();
        }
        return instance;
    }

    private FileOperation() {
    }

    public void initICatchWificamPlayback() {
        this.cameraPlayback = GlobalInfo.getInstance().getCurrentCamera().getplaybackClient();
    }

    public boolean cancelDownload(ICatchWificamPlayback playback) {
        AppLog.i("FileOperation", "begin cancelDownload");
        if (playback == null) {
            return true;
        }
        boolean retValue = false;
        try {
            retValue = playback.cancelFileDownload();
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDeviceException e4) {
            AppLog.e("FileOperation", "IchDeviceException");
            e4.printStackTrace();
        }
        AppLog.i("FileOperation", "end cancelDownload retValue =" + retValue);
        return retValue;
    }

    public boolean cancelDownload() {
        AppLog.i("FileOperation", "begin cancelDownload");
        if (this.cameraPlayback == null) {
            AppLog.i("FileOperation", "cameraPlayback is null");
            return true;
        }
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.cancelFileDownload();
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
            e3.printStackTrace();
        } catch (IchDeviceException e4) {
            AppLog.e("FileOperation", "IchDeviceException");
            e4.printStackTrace();
        }
        AppLog.i("FileOperation", "end cancelDownload retValue =" + retValue);
        return retValue;
    }

    public List<ICatchFile> getFileList(ICatchFileType type) {
        AppLog.i("FileOperation", "begin getFileList timeout 20s---");
        List<ICatchFile> list = null;
        if (this.cameraPlayback == null) {
            AppLog.i("FileOperation", "cameraPlayback is null");
            return null;
        }
        try {
            list = this.cameraPlayback.listFiles(type, 20);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchNoSuchPathException e3) {
            AppLog.e("FileOperation", "IchNoSuchPathException");
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
            e4.printStackTrace();
        }
        AppLog.i("FileOperation", "end getFileList list=" + list);
        return list;
    }

    public boolean deleteFile(ICatchFile file) {
        AppLog.i("FileOperation", "begin deleteFile filename =" + file.getFileName());
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.deleteFile(file);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.e("FileOperation", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        }
        AppLog.i("FileOperation", "end deleteFile retValue=" + retValue);
        return retValue;
    }

    public boolean downloadFileQuick(ICatchFile file, String path) {
        AppLog.i("FileOperation", "begin downloadFileQuick filename =" + file.getFileName());
        AppLog.i("FileOperation", "begin downloadFileQuick path =" + path);
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.downloadFileQuick(file, path);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.e("FileOperation", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        }
        AppLog.i("FileOperation", "end downloadFileQuick retValue =" + retValue);
        return retValue;
    }

    public boolean downloadFile(ICatchFile file, String path) {
        AppLog.i("FileOperation", "begin downloadFile filename =" + file.getFileName());
        AppLog.i("FileOperation", "begin downloadFile path =" + path);
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.downloadFile(file, path);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.e("FileOperation", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        }
        AppLog.i("FileOperation", "end downloadFile retValue =" + retValue);
        return retValue;
    }

    public ICatchFrameBuffer downloadFile(ICatchFile curFile) {
        AppLog.i("FileOperation", "begin downloadFile for buffer filename =" + curFile.getFileName());
        ICatchFrameBuffer buffer = null;
        try {
            buffer = this.cameraPlayback.downloadFile(curFile);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.e("FileOperation", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        } catch (IchBufferTooSmallException e6) {
            AppLog.e("FileOperation", "IchBufferTooSmallException");
            e6.printStackTrace();
        }
        AppLog.i("FileOperation", "end downloadFile for buffer, buffer =" + buffer);
        return buffer;
    }

    public ICatchFrameBuffer getQuickview(ICatchFile curFile) {
        AppLog.i("FileOperation", "begin getQuickview for buffer filename =" + curFile.getFileName());
        ICatchFrameBuffer buffer = null;
        try {
            buffer = this.cameraPlayback.getQuickview(curFile);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        } catch (IchDeviceException e4) {
            AppLog.e("FileOperation", "IchDeviceException");
            e4.printStackTrace();
        } catch (IchBufferTooSmallException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        } catch (IchNoSuchFileException e6) {
            AppLog.e("FileOperation", "IchDeviceException");
            e6.printStackTrace();
        }
        AppLog.i("FileOperation", "end getQuickview for buffer, buffer =" + buffer);
        return buffer;
    }

    public ICatchFrameBuffer getThumbnail(ICatchFile file) {
        AppLog.i("FileOperation", "begin getThumbnail file=" + file);
        ICatchFrameBuffer frameBuffer = null;
        try {
            frameBuffer = this.cameraPlayback.getThumbnail(file);
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.e("FileOperation", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        } catch (IchBufferTooSmallException e6) {
            AppLog.e("FileOperation", "IchBufferTooSmallException");
            e6.printStackTrace();
        }
        AppLog.i("FileOperation", "end getThumbnail frameBuffer=" + frameBuffer);
        return frameBuffer;
    }

    public ICatchFrameBuffer getThumbnail(String filePath) {
        AppLog.d("[Normal] -- FileOperation: ", "begin getThumbnail");
        ICatchFile icathfile = new ICatchFile(33, ICatchFileType.ICH_TYPE_VIDEO, filePath, BuildConfig.FLAVOR, 0);
        AppLog.d("[Normal] -- FileOperation: ", "begin getThumbnail file=" + filePath);
        AppLog.d("[Normal] -- FileOperation: ", "begin getThumbnail cameraPlayback=" + this.cameraPlayback);
        ICatchFrameBuffer frameBuffer = null;
        try {
            AppLog.d("test", "start cameraPlayback.getThumbnail(file) cameraPlayback=" + this.cameraPlayback);
            frameBuffer = this.cameraPlayback.getThumbnail(icathfile);
        } catch (IchSocketException e) {
            AppLog.d("[Error] -- FileOperation: ", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.d("[Error] -- FileOperation: ", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.d("[Error] -- FileOperation: ", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.d("[Error] -- FileOperation: ", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.d("[Error] -- FileOperation: ", "IchDeviceException");
            e5.printStackTrace();
        } catch (IchBufferTooSmallException e6) {
            AppLog.d("[Error] -- FileOperation: ", "IchBufferTooSmallException");
            e6.printStackTrace();
        }
        AppLog.d("[Normal] -- FileOperation: ", "end getThumbnail frameBuffer=" + frameBuffer);
        return frameBuffer;
    }

    public ICatchFrameBuffer getThumbnail(ICatchWificamPlayback wificamPlayback, String filePath) {
        AppLog.d("[Normal] -- FileOperation: ", "begin getThumbnail");
        ICatchFile icathfile = new ICatchFile(33, ICatchFileType.ICH_TYPE_VIDEO, filePath, BuildConfig.FLAVOR, 0);
        AppLog.d("[Normal] -- FileOperation: ", "begin getThumbnail file=" + filePath);
        AppLog.d("[Normal] -- FileOperation: ", "begin getThumbnail cameraPlayback=" + wificamPlayback);
        ICatchFrameBuffer frameBuffer = null;
        try {
            AppLog.d("test", "start cameraPlayback.getThumbnail(file) cameraPlayback=" + wificamPlayback);
            frameBuffer = wificamPlayback.getThumbnail(icathfile);
        } catch (IchSocketException e) {
            AppLog.d("[Error] -- FileOperation: ", "IchSocketException");
            e.printStackTrace();
        } catch (IchCameraModeException e2) {
            AppLog.d("[Error] -- FileOperation: ", "IchCameraModeException");
            e2.printStackTrace();
        } catch (IchInvalidSessionException e3) {
            AppLog.d("[Error] -- FileOperation: ", "IchInvalidSessionException");
        } catch (IchNoSuchFileException e4) {
            AppLog.d("[Error] -- FileOperation: ", "IchNoSuchFileException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.d("[Error] -- FileOperation: ", "IchDeviceException");
            e5.printStackTrace();
        } catch (IchBufferTooSmallException e6) {
            AppLog.d("[Error] -- FileOperation: ", "IchBufferTooSmallException");
            e6.printStackTrace();
        }
        AppLog.d("[Normal] -- FileOperation: ", "end getThumbnail frameBuffer=" + frameBuffer);
        return frameBuffer;
    }

    public boolean openFileTransChannel() {
        AppLog.i("FileOperation", "begin openFileTransChannel");
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.openFileTransChannel();
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchInvalidSessionException e2) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        }
        AppLog.i("FileOperation", "end openFileTransChannel retValue=" + retValue);
        return retValue;
    }

    public boolean closeFileTransChannel() {
        AppLog.i("FileOperation", "begin closeFileTransChannel");
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.closeFileTransChannel();
        } catch (IchSocketException e) {
            AppLog.e("FileOperation", "IchSocketException");
            e.printStackTrace();
        } catch (IchInvalidSessionException e2) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
        }
        AppLog.i("FileOperation", "end closeFileTransChannel retValue=" + retValue);
        return retValue;
    }

    public boolean uploadFile(String localPath, String remotePath) {
        AppLog.i("FileOperation", "begin uploadFile");
        boolean retValue = false;
        try {
            retValue = this.cameraPlayback.uploadFile(localPath, remotePath);
        } catch (IchNoSuchFileException e) {
            AppLog.e("FileOperation", "IchNoSuchFileException");
            e.printStackTrace();
        } catch (IchSocketException e2) {
            AppLog.e("FileOperation", "IchSocketException");
            e2.printStackTrace();
        } catch (IchCameraModeException e3) {
            AppLog.e("FileOperation", "IchCameraModeException");
            e3.printStackTrace();
        } catch (IchInvalidSessionException e4) {
            AppLog.e("FileOperation", "IchInvalidSessionException");
            e4.printStackTrace();
        } catch (IchDeviceException e5) {
            AppLog.e("FileOperation", "IchDeviceException");
            e5.printStackTrace();
        }
        AppLog.i("FileOperation", "End uploadFile retValue=" + retValue);
        return retValue;
    }
}
