package com.icatch.wificam.core.util.type;

import com.icatch.wificam.customer.type.ICatchPreviewMode;

public class NativePreviewMode {
    private static final int STILL_PREVIEW_MODE = 1;
    private static final int TIMELAPSE_STILL_PREVIEW_MODE = 3;
    private static final int TIMELAPSE_VIDEO_PREVIEW_MODE = 4;
    private static final int VIDEO_PREVIEW_MODE = 2;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$icatch$wificam$customer$type$ICatchPreviewMode = new int[ICatchPreviewMode.values().length];

        static {
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchPreviewMode[ICatchPreviewMode.ICH_STILL_PREVIEW_MODE.ordinal()] = NativePreviewMode.STILL_PREVIEW_MODE;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchPreviewMode[ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE.ordinal()] = NativePreviewMode.VIDEO_PREVIEW_MODE;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchPreviewMode[ICatchPreviewMode.ICH_TIMELAPSE_STILL_PREVIEW_MODE.ordinal()] = NativePreviewMode.TIMELAPSE_STILL_PREVIEW_MODE;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$icatch$wificam$customer$type$ICatchPreviewMode[ICatchPreviewMode.ICH_TIMELAPSE_VIDEO_PREVIEW_MODE.ordinal()] = NativePreviewMode.TIMELAPSE_VIDEO_PREVIEW_MODE;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public static int convertValue(ICatchPreviewMode pvMode) {
        switch (AnonymousClass1.$SwitchMap$com$icatch$wificam$customer$type$ICatchPreviewMode[pvMode.ordinal()]) {
            case VIDEO_PREVIEW_MODE /*2*/:
                return VIDEO_PREVIEW_MODE;
            case TIMELAPSE_STILL_PREVIEW_MODE /*3*/:
                return TIMELAPSE_STILL_PREVIEW_MODE;
            case TIMELAPSE_VIDEO_PREVIEW_MODE /*4*/:
                return TIMELAPSE_VIDEO_PREVIEW_MODE;
            default:
                return STILL_PREVIEW_MODE;
        }
    }
}
