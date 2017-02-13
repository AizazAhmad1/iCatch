package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.util.NativeLibraryLoader;

public class JWificamMediaServer {
    public static native boolean closeMediaServer();

    public static native boolean startMediaServerA(String str);

    public static native boolean startMediaServerB(boolean z, int i, boolean z2, int i2, int i3, int i4, int i5);

    public static native boolean writeAudioFrame(byte[] bArr, int i, double d);

    public static native boolean writeVideoFrame(byte[] bArr, int i, double d);

    static {
        NativeLibraryLoader.loadLibrary();
    }
}
