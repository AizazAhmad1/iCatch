package com.icatch.wificam.core.jni;

import com.icatch.wificam.core.jni.extractor.NativeValueExtractor;
import com.icatch.wificam.core.jni.util.NativeLibraryLoader;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;

public class JWificamUtil {
    private static native String convertImageSize(String str);

    private static native String convertVideoSize(String str);

    private static native String decodeAAC(byte[] bArr, int i, byte[] bArr2);

    private static native String decodeJPEG(byte[] bArr, int i, byte[] bArr2);

    private static native String executeFFMPEG(String str);

    static {
        NativeLibraryLoader.loadLibrary();
    }

    public static int convertImageSize_Jni(String size) throws IchInvalidArgumentException {
        try {
            return NativeValueExtractor.extractNativeIntValue(convertImageSize(size));
        } catch (IchInvalidArgumentException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return -1;
        }
    }

    public static int convertVideoSize_Jni(String size) throws IchInvalidArgumentException {
        try {
            return NativeValueExtractor.extractNativeIntValue(convertVideoSize(size));
        } catch (IchInvalidArgumentException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return -1;
        }
    }

    public static int decodeAAC_Jni(byte[] inputData, int inputDataSize, byte[] outputData) throws IchInvalidArgumentException {
        try {
            return NativeValueExtractor.extractNativeIntValue(decodeAAC(inputData, inputDataSize, outputData));
        } catch (IchInvalidArgumentException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return -1;
        }
    }

    public static int decodeJPEG_Jni(byte[] inputData, int inputDataSize, byte[] outputData) throws IchInvalidArgumentException {
        try {
            return NativeValueExtractor.extractNativeIntValue(decodeJPEG(inputData, inputDataSize, outputData));
        } catch (IchInvalidArgumentException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return -1;
        }
    }

    public static boolean executeFFMPEG_Jni(String commandArgs) throws IchInvalidArgumentException {
        try {
            return NativeValueExtractor.extractNativeBoolValue(executeFFMPEG(commandArgs));
        } catch (IchInvalidArgumentException ex) {
            throw ex;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }
}
