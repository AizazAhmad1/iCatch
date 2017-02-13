package com.icatch.ismartdv2016.Setting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.icatch.ismartdv2016.AppInfo.AppInfo;
import com.icatch.ismartdv2016.AppInfo.AppSharedPreferences;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.GlobalApp.ExitApp;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnSettingCompleteListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Model.Implement.SDKEvent;
import com.icatch.ismartdv2016.MyCamera.MyCamera;
import com.icatch.ismartdv2016.SdkApi.CameraAction;
import com.icatch.ismartdv2016.SdkApi.CameraProperties;
import com.icatch.ismartdv2016.SdkApi.FileOperation;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileTools;
import com.slidingmenu.lib.R;
import java.lang.reflect.Field;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class OptionSetting {
    private static final String TAG = "OptionSetting";
    private static AlertDialog alertDialog;
    static Context context;
    private static final SettingHander handler = new SettingHander();
    private static OnSettingCompleteListener onSettingCompleteListener;
    private static OptionSetting optionSetting;
    private static SDKEvent sdkEvent;

    private static class SettingHander extends Handler {
        private SettingHander() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.styleable.SlidingMenu_selectorDrawable /*13*/:
                    FileOperation.getInstance().closeFileTransChannel();
                    AppLog.d(OptionSetting.TAG, "receive EVENT_FW_UPDATE_COMPLETED");
                    MyProgressDialog.closeProgressDialog();
                    Builder builder2 = new Builder(OptionSetting.context);
                    builder2.setMessage(com.icatch.ismartdv2016.R.string.setting_updatefw_closeAppInfo);
                    builder2.setNegativeButton(com.icatch.ismartdv2016.R.string.ok, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("1111", "update FW completed!");
                        }
                    });
                    OptionSetting.alertDialog = builder2.create();
                    OptionSetting.alertDialog.setCancelable(false);
                    OptionSetting.alertDialog.show();
                    return;
                case R.styleable.SherlockTheme_actionModeCloseButtonStyle /*14*/:
                    AppLog.d(OptionSetting.TAG, "receive EVENT_FW_UPDATE_POWEROFF");
                    OptionSetting.sdkEvent.delEventListener(97);
                    OptionSetting.sdkEvent.delEventListener(98);
                    Builder builder3 = new Builder(OptionSetting.context);
                    builder3.setMessage(com.icatch.ismartdv2016.R.string.setting_updatefw_closeAppInfo);
                    builder3.setNegativeButton(com.icatch.ismartdv2016.R.string.dialog_btn_exit, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("1111", "App quit");
                            ExitApp.getInstance().exit();
                        }
                    });
                    OptionSetting.alertDialog = builder3.create();
                    OptionSetting.alertDialog.setCancelable(false);
                    OptionSetting.alertDialog.show();
                    return;
                default:
                    return;
            }
        }
    }

    public static OptionSetting getInstance() {
        if (optionSetting == null) {
            optionSetting = new OptionSetting();
        }
        return optionSetting;
    }

    public void addSettingCompleteListener(OnSettingCompleteListener onSettingCompleteListener) {
        onSettingCompleteListener = onSettingCompleteListener;
    }

    public void showSettingDialog(int nameId, Context context) {
        context = context;
        switch (nameId) {
            case com.icatch.ismartdv2016.R.string.camera_wifi_configuration /*2131230774*/:
                showCameraConfigurationDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_auto_download_size_limit /*2131230905*/:
                showSetDownloadSizeLimitDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_capture_delay /*2131230910*/:
                Log.d("1111", "setting_capture_delay");
                showDelayTimeOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_datestamp /*2131230913*/:
                showDateStampOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_format /*2131230915*/:
                if (CameraProperties.getInstance().isSDCardExist()) {
                    showFormatConfirmDialog(context);
                    return;
                } else {
                    sdCardIsNotReadyAlert(context);
                    return;
                }
            case com.icatch.ismartdv2016.R.string.setting_image_size /*2131230920*/:
                Log.d("1111", "setting_image_size");
                showImageSizeOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_power_supply /*2131230925*/:
                showElectricityFrequencyOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_time_lapse_duration /*2131230930*/:
                showTimeLapseDurationDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_time_lapse_interval /*2131230939*/:
                showTimeLapseIntervalDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.setting_update_fw /*2131230959*/:
                if (CameraProperties.getInstance().isSDCardExist()) {
                    showUpdateFWDialog(context);
                    return;
                } else {
                    sdCardIsNotReadyAlert(context);
                    return;
                }
            case com.icatch.ismartdv2016.R.string.setting_video_size /*2131230969*/:
                Log.d("1111", "setting_video_size");
                showVideoSizeOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.slowmotion /*2131230971*/:
                showSlowMotionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.title_awb /*2131231064*/:
                Log.d("1111", "showWhiteBalanceOptionDialog =");
                showWhiteBalanceOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.title_burst /*2131231065*/:
                showBurstOptionDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.title_timeLapse_mode /*2131231078*/:
                showTimeLapseModeDialog(context);
                return;
            case com.icatch.ismartdv2016.R.string.upside /*2131231082*/:
                showUpsideDialog(context);
                return;
            default:
                return;
        }
    }

    public static void showUpdateFWDialog(final Context context) {
        AppLog.i(TAG, "showUpdateFWDialog");
        Builder builder = new Builder(context);
        builder.setMessage(com.icatch.ismartdv2016.R.string.setting_updateFW_prompt);
        builder.setNegativeButton(com.icatch.ismartdv2016.R.string.setting_no, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton(com.icatch.ismartdv2016.R.string.setting_yes, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (OptionSetting.sdkEvent == null) {
                    OptionSetting.sdkEvent = new SDKEvent(OptionSetting.handler);
                }
                OptionSetting.sdkEvent.addEventListener(97);
                OptionSetting.sdkEvent.addEventListener(98);
                MyProgressDialog.showProgressDialog(context, (int) com.icatch.ismartdv2016.R.string.setting_update_fw);
                new Thread(new Runnable() {
                    public void run() {
                        FileTools.copyFile(com.icatch.ismartdv2016.R.raw.sphost);
                        String fileName = Environment.getExternalStorageDirectory().toString() + AppInfo.UPDATEFW_FILENAME;
                        FileOperation.getInstance().openFileTransChannel();
                        if (CameraAction.getInstance().updateFW(fileName)) {
                            FileOperation.getInstance().closeFileTransChannel();
                            return;
                        }
                        Builder updateFWFailedBuilder = new Builder(context);
                        updateFWFailedBuilder.setMessage(com.icatch.ismartdv2016.R.string.setting_updatefw_failedInfo);
                        updateFWFailedBuilder.setNegativeButton(com.icatch.ismartdv2016.R.string.dialog_btn_exit, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("1111", "update FW has failed,App quit");
                                ExitApp.getInstance().exit();
                            }
                        });
                        OptionSetting.alertDialog = updateFWFailedBuilder.create();
                        OptionSetting.alertDialog.setCancelable(false);
                        OptionSetting.alertDialog.show();
                    }
                }).start();
            }
        });
        builder.create().show();
    }

    public static void showCameraConfigurationDialog(Context context) {
        View textEntryView = LayoutInflater.from(context).inflate(com.icatch.ismartdv2016.R.layout.camera_name_password_set, null);
        final EditText cameraName = (EditText) textEntryView.findViewById(com.icatch.ismartdv2016.R.id.camera_name);
        final String name = CameraProperties.getInstance().getCameraSsid();
        cameraName.setText(name);
        final EditText cameraPassword = (EditText) textEntryView.findViewById(com.icatch.ismartdv2016.R.id.wifi_password);
        final String password = CameraProperties.getInstance().getCameraPassword();
        cameraPassword.setText(password);
        Builder ad1 = new Builder(context);
        ad1.setTitle(com.icatch.ismartdv2016.R.string.camera_wifi_configuration);
        ad1.setIcon(17301659);
        ad1.setView(textEntryView);
        ad1.setCancelable(true);
        ad1.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    Log.d("1111", "KeyEvent.KEYCODE_BACK");
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, Boolean.valueOf(true));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                return true;
            }
        });
        final Context context2 = context;
        ad1.setPositiveButton(com.icatch.ismartdv2016.R.string.camera_configuration_set, new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (cameraName.getText().toString().length() > 20) {
                    Toast.makeText(context2, com.icatch.ismartdv2016.R.string.camera_name_limit, 1).show();
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, Boolean.valueOf(false));
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                String temp = cameraPassword.getText().toString();
                if (temp.length() > 10 || temp.length() < 8) {
                    Toast.makeText(context2, com.icatch.ismartdv2016.R.string.password_limit, 1).show();
                    try {
                        field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, Boolean.valueOf(false));
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                try {
                    field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(dialog, Boolean.valueOf(true));
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
                if (!name.equals(cameraName.getText().toString())) {
                    CameraProperties.getInstance().setCameraSsid(cameraName.getText().toString());
                }
                if (!password.equals(temp)) {
                    CameraProperties.getInstance().setCameraPassword(cameraPassword.getText().toString());
                }
            }
        });
        ad1.show();
    }

    public static void showUpsideDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.upside);
        String[] upsideUIString = currCamera.getUpside().getValueList();
        if (upsideUIString == null) {
            AppLog.e(TAG, "upsideUIString == null");
            return;
        }
        int length = upsideUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (upsideUIString[i].equals(currCamera.getUpside().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, upsideUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getUpside().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showSlowMotionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.title_slow_motion);
        String[] slowmotionUIString = currCamera.getSlowMotion().getValueList();
        if (slowmotionUIString == null) {
            AppLog.e(TAG, "slowmotionUIString == null");
            return;
        }
        int length = slowmotionUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (slowmotionUIString[i].equals(currCamera.getSlowMotion().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, slowmotionUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getSlowMotion().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showTimeLapseModeDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.title_timeLapse_mode);
        String[] timeLapseModeString = currCamera.getTimeLapseMode().getValueList();
        if (timeLapseModeString == null) {
            AppLog.e(TAG, "timeLapseModeString == null");
            return;
        }
        int length = timeLapseModeString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (timeLapseModeString[i].equals(currCamera.getTimeLapseMode().getCurrentUiStringInSetting())) {
                Log.d("tigertiger", "timeLapseModeString[i] =" + timeLapseModeString[i]);
                curIdx = i;
            }
        }
        showOptionDialog(title, timeLapseModeString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.timeLapsePreviewMode = arg1;
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.settingTimeLapseModeComplete(arg1);
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
                Log.d("tigertiger", "showTimeLapseModeDialog  timeLapseMode =" + arg1);
            }
        }, true);
    }

    public static void showTimeLapseDurationDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.setting_time_lapse_duration);
        String[] videoTimeLapseDurationString = currCamera.gettimeLapseDuration().getValueStringList();
        if (videoTimeLapseDurationString == null) {
            AppLog.e(TAG, "videoTimeLapseDurationString == null");
            return;
        }
        int length = videoTimeLapseDurationString.length;
        int curIdx = 0;
        String temp = currCamera.gettimeLapseDuration().getCurrentValue();
        for (int i = 0; i < length; i++) {
            if (videoTimeLapseDurationString[i].equals(temp)) {
                curIdx = i;
            }
        }
        showOptionDialog(title, videoTimeLapseDurationString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.gettimeLapseDuration().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showTimeLapseIntervalDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.setting_time_lapse_interval);
        String[] videoTimeLapseIntervalString = currCamera.getTimeLapseInterval().getValueStringList();
        if (videoTimeLapseIntervalString == null) {
            AppLog.e(TAG, "videoTimeLapseIntervalString == null");
            return;
        }
        int length = videoTimeLapseIntervalString.length;
        int curIdx = 0;
        String temp = currCamera.getTimeLapseInterval().getCurrentValue();
        for (int i = 0; i < length; i++) {
            if (videoTimeLapseIntervalString[i].equals(temp)) {
                curIdx = i;
            }
        }
        showOptionDialog(title, videoTimeLapseIntervalString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getTimeLapseInterval().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showDelayTimeOptionDialog(Context context, final OnSettingCompleteListener settingCompleteListener) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.stream_set_timer);
        String[] delayTimeUIString = currCamera.getCaptureDelay().getValueList();
        if (delayTimeUIString == null) {
            AppLog.e(TAG, "delayTimeUIString == null");
            return;
        }
        int length = delayTimeUIString.length;
        int curIdx = 0;
        String temp = currCamera.getCaptureDelay().getCurrentUiStringInPreview();
        for (int i = 0; i < length; i++) {
            if (delayTimeUIString[i].equals(temp)) {
                curIdx = i;
            }
        }
        showOptionDialog(title, delayTimeUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getCaptureDelay().setValueByPosition(arg1);
                arg0.dismiss();
                settingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showVideoSizeOptionDialog(Context context, final OnSettingCompleteListener settingCompleteListener) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.stream_set_res_vid);
        String[] videoSizeUIString = currCamera.getVideoSize().getValueArrayString();
        final List<String> videoSizeValueString = currCamera.getVideoSize().getValueList();
        if (videoSizeUIString == null) {
            AppLog.e(TAG, "videoSizeUIString == null");
            return;
        }
        int length = videoSizeUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (videoSizeUIString[i].equals(currCamera.getVideoSize().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, videoSizeUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getVideoSize().setValue((String) videoSizeValueString.get(arg1));
                arg0.dismiss();
                settingCompleteListener.onOptionSettingComplete();
            }
        }, false);
    }

    public static void showImageSizeOptionDialog(Context context, final OnSettingCompleteListener settingCompleteListener) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.stream_set_res_photo);
        String[] imageSizeUIString = currCamera.getImageSize().getValueArrayString();
        if (imageSizeUIString == null) {
            AppLog.e(TAG, "imageSizeUIString == null");
            return;
        }
        int length = imageSizeUIString.length;
        int curIdx = 0;
        for (int ii = 0; ii < length; ii++) {
            if (imageSizeUIString[ii].equals(currCamera.getImageSize().getCurrentUiStringInSetting())) {
                curIdx = ii;
            }
        }
        showOptionDialog(title, imageSizeUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getImageSize().setValueByPosition(arg1);
                arg0.dismiss();
                settingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showImageSizeOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.stream_set_res_photo);
        String[] imageSizeUIString = currCamera.getImageSize().getValueArrayString();
        if (imageSizeUIString == null) {
            AppLog.e(TAG, "imageSizeUIString == null");
            return;
        }
        int length = imageSizeUIString.length;
        int curIdx = 0;
        for (int ii = 0; ii < length; ii++) {
            if (imageSizeUIString[ii].equals(currCamera.getImageSize().getCurrentUiStringInSetting())) {
                curIdx = ii;
            }
        }
        showOptionDialog(title, imageSizeUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getImageSize().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showDelayTimeOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.stream_set_timer);
        String[] delayTimeUIString = currCamera.getCaptureDelay().getValueList();
        if (delayTimeUIString == null) {
            AppLog.e(TAG, "delayTimeUIString == null");
            return;
        }
        int length = delayTimeUIString.length;
        int curIdx = 0;
        String temp = currCamera.getCaptureDelay().getCurrentUiStringInPreview();
        for (int i = 0; i < length; i++) {
            if (delayTimeUIString[i].equals(temp)) {
                curIdx = i;
            }
        }
        showOptionDialog(title, delayTimeUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getCaptureDelay().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showVideoSizeOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.stream_set_res_vid);
        String[] videoSizeUIString = currCamera.getVideoSize().getValueArrayString();
        final List<String> videoSizeValueString = currCamera.getVideoSize().getValueList();
        if (videoSizeUIString == null) {
            AppLog.e(TAG, "videoSizeUIString == null");
            return;
        }
        int length = videoSizeUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (videoSizeUIString[i].equals(currCamera.getVideoSize().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, videoSizeUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getVideoSize().setValue((String) videoSizeValueString.get(arg1));
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.settingVideoSizeComplete();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, false);
    }

    public static void showFormatConfirmDialog(final Context context) {
        Builder builder = new Builder(context);
        builder.setMessage(com.icatch.ismartdv2016.R.string.setting_format_desc);
        builder.setNegativeButton(com.icatch.ismartdv2016.R.string.setting_no, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton(com.icatch.ismartdv2016.R.string.setting_yes, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final Handler handler = new Handler();
                MyProgressDialog.showProgressDialog(context, (int) com.icatch.ismartdv2016.R.string.setting_format);
                new Thread(new Runnable() {
                    public void run() {
                        int messageId;
                        if (CameraAction.getInstance().formatStorage()) {
                            messageId = com.icatch.ismartdv2016.R.string.text_operation_success;
                        } else {
                            messageId = com.icatch.ismartdv2016.R.string.text_operation_failed;
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                                MyToast.show(context, messageId);
                            }
                        });
                    }
                }).start();
            }
        });
        builder.create().show();
    }

    public static void showDateStampOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.setting_datestamp);
        String[] dateStampUIString = currCamera.getDateStamp().getValueList();
        if (dateStampUIString == null) {
            AppLog.e(TAG, "dateStampUIString == null");
            return;
        }
        int length = dateStampUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (dateStampUIString[i].equals(currCamera.getDateStamp().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, dateStampUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getDateStamp().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showElectricityFrequencyOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.setting_power_supply);
        String[] eleFreUIString = currCamera.getElectricityFrequency().getValueList();
        if (eleFreUIString == null) {
            AppLog.e(TAG, "eleFreUIString == null");
            return;
        }
        int length = eleFreUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (eleFreUIString[i].equals(currCamera.getElectricityFrequency().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, eleFreUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getElectricityFrequency().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showWhiteBalanceOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.title_awb);
        String[] whiteBalanceUIString = currCamera.getWhiteBalance().getValueList();
        if (whiteBalanceUIString == null) {
            AppLog.e(TAG, "whiteBalanceUIString == null");
            return;
        }
        int length = whiteBalanceUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (whiteBalanceUIString[i].equals(currCamera.getWhiteBalance().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, whiteBalanceUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getWhiteBalance().setValueByPosition(arg1);
                arg0.dismiss();
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public void showBurstOptionDialog(Context context) {
        final MyCamera currCamera = GlobalInfo.getInstance().getCurrentCamera();
        CharSequence title = context.getResources().getString(com.icatch.ismartdv2016.R.string.title_burst);
        String[] burstUIString = currCamera.getBurst().getValueList();
        if (burstUIString == null) {
            AppLog.e(TAG, "burstUIString == null");
            return;
        }
        int length = burstUIString.length;
        int curIdx = 0;
        for (int i = 0; i < length; i++) {
            if (burstUIString[i].equals(currCamera.getBurst().getCurrentUiStringInSetting())) {
                curIdx = i;
            }
        }
        showOptionDialog(title, burstUIString, curIdx, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                currCamera.getBurst().setValueByPosition(arg1);
                arg0.dismiss();
                Log.d("1111", "refresh optionListAdapter!");
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        }, true);
    }

    public static void showOptionDialog(CharSequence title, CharSequence[] items, int checkedItem, OnClickListener listener, boolean cancelable) {
        Builder optionDialog = new Builder(GlobalInfo.getInstance().getCurrentApp());
        optionDialog.setTitle(title).setSingleChoiceItems(items, checkedItem, listener).create();
        optionDialog.show();
        optionDialog.setCancelable(cancelable);
    }

    public void sdCardIsNotReadyAlert(Context context) {
        Builder builder = new Builder(context);
        builder.setMessage(com.icatch.ismartdv2016.R.string.dialog_no_sd);
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void showSetDownloadSizeLimitDialog(final Context context) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        View contentView = View.inflate(context, com.icatch.ismartdv2016.R.layout.content_download_size_dialog, null);
        final EditText resetTxv = (EditText) contentView.findViewById(com.icatch.ismartdv2016.R.id.download_size);
        resetTxv.setHint(AppInfo.autoDownloadSizeLimit + BuildConfig.FLAVOR);
        builder.setTitle(com.icatch.ismartdv2016.R.string.setting_auto_download_size_limit);
        builder.setView(contentView);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(com.icatch.ismartdv2016.R.string.action_save), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (resetTxv.getText().toString().equals(BuildConfig.FLAVOR)) {
                    AppLog.d("3322", "ret dddddddddd");
                    return;
                }
                float sizeLimit = Float.parseFloat(resetTxv.getText().toString());
                AppSharedPreferences.writeDataByName(context, AppSharedPreferences.OBJECT_NAME, String.valueOf(sizeLimit));
                AppInfo.autoDownloadSizeLimit = sizeLimit;
                OptionSetting.onSettingCompleteListener.onOptionSettingComplete();
            }
        });
        builder.setNegativeButton(context.getResources().getString(com.icatch.ismartdv2016.R.string.gallery_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
}
