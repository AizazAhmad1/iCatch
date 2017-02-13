package com.icatch.wificam.customer;

import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.jni.JWificamPlayback;
import com.icatch.wificam.core.util.type.NativeFile;
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

public class ICatchWificamPlayback {
    private int sessionID;

    ICatchWificamPlayback(int sessionID) {
        this.sessionID = sessionID;
    }

    public List<ICatchFile> listFiles(ICatchFileType type) throws IchSocketException, IchCameraModeException, IchNoSuchPathException, IchInvalidSessionException {
        String files = JWificamPlayback.listFiles_Jni(this.sessionID, NativeFile.convertValue(type));
        CoreLogger.logI("playback", "filesStr: " + files);
        return NativeFile.toIcatchFiles(files);
    }

    public List<ICatchFile> listFiles(ICatchFileType type, int timeoutInSecs) throws IchSocketException, IchCameraModeException, IchNoSuchPathException, IchInvalidSessionException {
        return NativeFile.toIcatchFiles(JWificamPlayback.listFiles_Jni(this.sessionID, NativeFile.convertValue(type), timeoutInSecs));
    }

    public boolean openFileTransChannel() throws IchSocketException, IchInvalidSessionException {
        return JWificamPlayback.openFileTransChannel_Jni(this.sessionID);
    }

    public boolean downloadFileQuick(ICatchFile file, String path) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.downloadFileQuick_Jni(this.sessionID, NativeFile.toICatchFile(file), path);
    }

    public boolean closeFileTransChannel() throws IchSocketException, IchInvalidSessionException {
        return JWificamPlayback.closeFileTransChannel_Jni(this.sessionID);
    }

    public ICatchFrameBuffer downloadFile(ICatchFile file) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchBufferTooSmallException, IchInvalidSessionException, IchDeviceException {
        int bufferSize = 9980928;
        ICatchFrameBuffer buffer = new ICatchFrameBuffer(9980928);
        String icatchFile = NativeFile.toICatchFile(file);
        while (true) {
            try {
                buffer.setFrameSize(JWificamPlayback.downloadImage_Jni(this.sessionID, icatchFile, buffer.getBuffer()));
                break;
            } catch (IchBufferTooSmallException e) {
                bufferSize += 1048576;
                buffer = new ICatchFrameBuffer(bufferSize);
            }
        }
        return buffer;
    }

    public boolean uploadFile(String localPath, String remotePath) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.uploadFile_Jni(this.sessionID, localPath, remotePath);
    }

    public boolean uploadFileQuick(String localPath, String remotePath) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.uploadFileQuick_Jni(this.sessionID, localPath, remotePath);
    }

    public boolean cancelFileDownload() throws IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.cancelFileDownload_Jni(this.sessionID);
    }

    public boolean downloadFile(ICatchFile file, String path) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.downloadFile_Jni(this.sessionID, NativeFile.toICatchFile(file), path);
    }

    public boolean downloadFile(String srcPath, String dstPath) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.downloadFile1_Jni(this.sessionID, srcPath, dstPath);
    }

    public boolean deleteFile(ICatchFile file) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchInvalidSessionException, IchDeviceException {
        return JWificamPlayback.deleteFile_Jni(this.sessionID, NativeFile.toICatchFile(file));
    }

    public ICatchFrameBuffer getThumbnail(ICatchFile file) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchBufferTooSmallException, IchInvalidSessionException, IchDeviceException {
        ICatchFrameBuffer buffer = new ICatchFrameBuffer(2097152);
        buffer.setFrameSize(JWificamPlayback.getThumbnail_Jni(this.sessionID, NativeFile.toICatchFile(file), buffer.getBuffer()));
        return buffer;
    }

    public ICatchFrameBuffer getQuickview(ICatchFile file) throws IchNoSuchFileException, IchSocketException, IchCameraModeException, IchBufferTooSmallException, IchInvalidSessionException, IchDeviceException {
        ICatchFrameBuffer buffer = new ICatchFrameBuffer(1843200);
        buffer.setFrameSize(JWificamPlayback.getQuickView_Jni(this.sessionID, NativeFile.toICatchFile(file), buffer.getBuffer()));
        return buffer;
    }
}
