package com.icatch.ismartdv2016.AppInfo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Log.SdkLog;
import com.icatch.ismartdv2016.Tools.FileOpertion.FileOper;
import com.icatch.wificam.customer.ICatchWificamConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import uk.co.senab.photoview.BuildConfig;

public class ConfigureInfo {
    private static final String TAG = "ConfigureInfo";
    private static ConfigureInfo configureInfo;
    private final String[] cfgTopic = new String[]{"AppVersion=R1.4.7.2", "SupportAutoReconnection=false", "SaveStreamVideo=false", "SaveStreamAudio=false", "broadcast=false", "SupportSetting=false", "SaveAppLog=false", "SaveSDKLog=false", "disconnectRetry=3", "enableSoftwareDecoder=false", "disableAudio=true"};

    private ConfigureInfo() {
    }

    public static ConfigureInfo getInstance() {
        if (configureInfo == null) {
            configureInfo = new ConfigureInfo();
        }
        return configureInfo;
    }

    public void initCfgInfo(Context context) {
        IOException e;
        FileNotFoundException e2;
        CfgProperty cfgInfo;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String disableAudio;
        String streamOutputPath;
        Throwable th;
        AppLog.d(TAG, "readCfgInfo............");
        String directoryPath = context.getExternalCacheDir() + AppInfo.PROPERTY_CFG_DIRECTORY_PATH;
        String fileName = AppInfo.PROPERTY_CFG_FILE_NAME;
        String info = BuildConfig.FLAVOR;
        for (String str10 : this.cfgTopic) {
            info = info + str10 + "\n";
        }
        if (new File(directoryPath + fileName).exists()) {
            String cfgVersion = null;
            try {
                cfgVersion = new CfgProperty(directoryPath + fileName).getProperty("AppVersion");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e12) {
                e12.printStackTrace();
            }
            if (cfgVersion != null) {
                AppLog.d(TAG, "cfgVersion..........=" + cfgVersion);
                if (!cfgVersion.equals(com.icatch.ismartdv2016.BuildConfig.VERSION_NAME)) {
                    writeCfgInfo(directoryPath + fileName, info);
                }
                AppLog.d(TAG, "cfgVersion=" + cfgVersion + " appVersion=" + com.icatch.ismartdv2016.BuildConfig.VERSION_NAME);
            } else {
                writeCfgInfo(directoryPath + fileName, info);
                AppLog.d(TAG, "cfgVersion=" + cfgVersion + " appVersion=" + com.icatch.ismartdv2016.BuildConfig.VERSION_NAME);
            }
        } else {
            FileOper.createFile(directoryPath, fileName);
            FileOutputStream out = null;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(directoryPath + fileName);
                try {
                    fileOutputStream.write(info.getBytes());
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                            out = fileOutputStream;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            out = fileOutputStream;
                        }
                    } else {
                        out = fileOutputStream;
                    }
                } catch (FileNotFoundException e4) {
                    e2 = e4;
                    out = fileOutputStream;
                    try {
                        e2.printStackTrace();
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        cfgInfo = new CfgProperty(directoryPath + fileName);
                        str = null;
                        str2 = null;
                        str3 = null;
                        str4 = null;
                        str5 = null;
                        str6 = null;
                        str7 = null;
                        str8 = null;
                        str9 = null;
                        disableAudio = null;
                        str = cfgInfo.getProperty("SupportAutoReconnection");
                        str2 = cfgInfo.getProperty("SaveStreamVideo");
                        str3 = cfgInfo.getProperty("SaveStreamAudio");
                        str4 = cfgInfo.getProperty("broadcast");
                        str5 = cfgInfo.getProperty("SupportSetting");
                        str6 = cfgInfo.getProperty("SaveAppLog");
                        str7 = cfgInfo.getProperty("SaveSDKLog");
                        str8 = cfgInfo.getProperty("disconnectRetry");
                        str9 = cfgInfo.getProperty("enableSoftwareDecoder");
                        disableAudio = cfgInfo.getProperty("disableAudio");
                        streamOutputPath = Environment.getExternalStorageDirectory().toString() + AppInfo.STREAM_OUTPUT_DIRECTORY_PATH;
                        FileOper.createDirectory(streamOutputPath);
                        if (str6 != null) {
                            if (str6.equals("true")) {
                                AppLog.enableAppLog();
                            }
                            AppLog.i(TAG, "GlobalInfo.saveAppLog..........=" + str6);
                        }
                        if (str7 != null) {
                            if (str7.equals("true")) {
                                AppInfo.saveSDKLog = true;
                                SdkLog.getInstance().enableSDKLog();
                            }
                            AppLog.i(TAG, "GlobalInfo.saveSDKLog..........=" + AppInfo.saveSDKLog);
                        }
                        if (str != null) {
                            if (str.equals("true")) {
                                AppInfo.isSupportAutoReconnection = false;
                            } else {
                                AppInfo.isSupportAutoReconnection = true;
                            }
                            AppLog.i(TAG, " end isSupportAutoReconnection = " + AppInfo.isSupportAutoReconnection);
                        }
                        AppLog.d(TAG, "saveStreamVideo..........=true");
                        ICatchWificamConfig.getInstance().enableDumpMediaStream(true, streamOutputPath);
                        AppLog.d(TAG, "enableDumpMediaStream..........=false");
                        ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                        AppLog.d(TAG, "enableDumpMediaStream..........=false");
                        ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                        if (str4 != null) {
                            AppLog.d(TAG, "broadcast..........=" + str4);
                            if (str4.equals("true")) {
                                AppLog.d(TAG, "broadcast..........=" + str4);
                                AppInfo.isSupportBroadcast = true;
                            }
                            AppLog.d(TAG, "GlobalInfo.isSupportBroadcast..........=" + AppInfo.isSupportBroadcast);
                        }
                        if (str5 != null) {
                            AppLog.i(TAG, "supportSetting..........=" + str5);
                            if (str5.equals("true")) {
                                AppLog.i(TAG, "supportSetting..........=" + str5);
                                AppInfo.isSupportSetting = true;
                            }
                            AppLog.i(TAG, "GlobalInfo.isSupportSetting..........=" + AppInfo.isSupportSetting);
                        }
                        AppLog.d(TAG, "disconnectRetry=" + str8);
                        if (str8 != null) {
                            AppLog.d(TAG, "retryCount=" + Integer.parseInt(str8));
                        }
                        if (str9 != null) {
                            if (str9.equals("true")) {
                                ICatchWificamConfig.getInstance().enableSoftwareDecoder(false);
                                GlobalInfo.enableSoftwareDecoder = false;
                            } else {
                                ICatchWificamConfig.getInstance().enableSoftwareDecoder(true);
                                Log.d("1111", "open SoftwareDecoder");
                                GlobalInfo.enableSoftwareDecoder = true;
                            }
                        }
                        AppLog.i(TAG, "disableAudio=" + disableAudio);
                        if (disableAudio != null) {
                            if (disableAudio.equals("true")) {
                                AppInfo.disableAudio = false;
                            } else {
                                AppInfo.disableAudio = true;
                            }
                            AppLog.i(TAG, "AppInfo.disableAudio..........=" + AppInfo.disableAudio);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e322 = e5;
                    out = fileOutputStream;
                    e322.printStackTrace();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e3222) {
                            e3222.printStackTrace();
                        }
                    }
                    cfgInfo = new CfgProperty(directoryPath + fileName);
                    str = null;
                    str2 = null;
                    str3 = null;
                    str4 = null;
                    str5 = null;
                    str6 = null;
                    str7 = null;
                    str8 = null;
                    str9 = null;
                    disableAudio = null;
                    str = cfgInfo.getProperty("SupportAutoReconnection");
                    str2 = cfgInfo.getProperty("SaveStreamVideo");
                    str3 = cfgInfo.getProperty("SaveStreamAudio");
                    str4 = cfgInfo.getProperty("broadcast");
                    str5 = cfgInfo.getProperty("SupportSetting");
                    str6 = cfgInfo.getProperty("SaveAppLog");
                    str7 = cfgInfo.getProperty("SaveSDKLog");
                    str8 = cfgInfo.getProperty("disconnectRetry");
                    str9 = cfgInfo.getProperty("enableSoftwareDecoder");
                    disableAudio = cfgInfo.getProperty("disableAudio");
                    streamOutputPath = Environment.getExternalStorageDirectory().toString() + AppInfo.STREAM_OUTPUT_DIRECTORY_PATH;
                    FileOper.createDirectory(streamOutputPath);
                    if (str6 != null) {
                        if (str6.equals("true")) {
                            AppLog.enableAppLog();
                        }
                        AppLog.i(TAG, "GlobalInfo.saveAppLog..........=" + str6);
                    }
                    if (str7 != null) {
                        if (str7.equals("true")) {
                            AppInfo.saveSDKLog = true;
                            SdkLog.getInstance().enableSDKLog();
                        }
                        AppLog.i(TAG, "GlobalInfo.saveSDKLog..........=" + AppInfo.saveSDKLog);
                    }
                    if (str != null) {
                        if (str.equals("true")) {
                            AppInfo.isSupportAutoReconnection = true;
                        } else {
                            AppInfo.isSupportAutoReconnection = false;
                        }
                        AppLog.i(TAG, " end isSupportAutoReconnection = " + AppInfo.isSupportAutoReconnection);
                    }
                    AppLog.d(TAG, "saveStreamVideo..........=true");
                    ICatchWificamConfig.getInstance().enableDumpMediaStream(true, streamOutputPath);
                    AppLog.d(TAG, "enableDumpMediaStream..........=false");
                    ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                    AppLog.d(TAG, "enableDumpMediaStream..........=false");
                    ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                    if (str4 != null) {
                        AppLog.d(TAG, "broadcast..........=" + str4);
                        if (str4.equals("true")) {
                            AppLog.d(TAG, "broadcast..........=" + str4);
                            AppInfo.isSupportBroadcast = true;
                        }
                        AppLog.d(TAG, "GlobalInfo.isSupportBroadcast..........=" + AppInfo.isSupportBroadcast);
                    }
                    if (str5 != null) {
                        AppLog.i(TAG, "supportSetting..........=" + str5);
                        if (str5.equals("true")) {
                            AppLog.i(TAG, "supportSetting..........=" + str5);
                            AppInfo.isSupportSetting = true;
                        }
                        AppLog.i(TAG, "GlobalInfo.isSupportSetting..........=" + AppInfo.isSupportSetting);
                    }
                    AppLog.d(TAG, "disconnectRetry=" + str8);
                    if (str8 != null) {
                        AppLog.d(TAG, "retryCount=" + Integer.parseInt(str8));
                    }
                    if (str9 != null) {
                        if (str9.equals("true")) {
                            ICatchWificamConfig.getInstance().enableSoftwareDecoder(true);
                            Log.d("1111", "open SoftwareDecoder");
                            GlobalInfo.enableSoftwareDecoder = true;
                        } else {
                            ICatchWificamConfig.getInstance().enableSoftwareDecoder(false);
                            GlobalInfo.enableSoftwareDecoder = false;
                        }
                    }
                    AppLog.i(TAG, "disableAudio=" + disableAudio);
                    if (disableAudio != null) {
                        if (disableAudio.equals("true")) {
                            AppInfo.disableAudio = true;
                        } else {
                            AppInfo.disableAudio = false;
                        }
                        AppLog.i(TAG, "AppInfo.disableAudio..........=" + AppInfo.disableAudio);
                    }
                } catch (Throwable th3) {
                    th = th3;
                    out = fileOutputStream;
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e6) {
                e2 = e6;
                e2.printStackTrace();
                if (out != null) {
                    out.close();
                }
                cfgInfo = new CfgProperty(directoryPath + fileName);
                str = null;
                str2 = null;
                str3 = null;
                str4 = null;
                str5 = null;
                str6 = null;
                str7 = null;
                str8 = null;
                str9 = null;
                disableAudio = null;
                str = cfgInfo.getProperty("SupportAutoReconnection");
                str2 = cfgInfo.getProperty("SaveStreamVideo");
                str3 = cfgInfo.getProperty("SaveStreamAudio");
                str4 = cfgInfo.getProperty("broadcast");
                str5 = cfgInfo.getProperty("SupportSetting");
                str6 = cfgInfo.getProperty("SaveAppLog");
                str7 = cfgInfo.getProperty("SaveSDKLog");
                str8 = cfgInfo.getProperty("disconnectRetry");
                str9 = cfgInfo.getProperty("enableSoftwareDecoder");
                disableAudio = cfgInfo.getProperty("disableAudio");
                streamOutputPath = Environment.getExternalStorageDirectory().toString() + AppInfo.STREAM_OUTPUT_DIRECTORY_PATH;
                FileOper.createDirectory(streamOutputPath);
                if (str6 != null) {
                    if (str6.equals("true")) {
                        AppLog.enableAppLog();
                    }
                    AppLog.i(TAG, "GlobalInfo.saveAppLog..........=" + str6);
                }
                if (str7 != null) {
                    if (str7.equals("true")) {
                        AppInfo.saveSDKLog = true;
                        SdkLog.getInstance().enableSDKLog();
                    }
                    AppLog.i(TAG, "GlobalInfo.saveSDKLog..........=" + AppInfo.saveSDKLog);
                }
                if (str != null) {
                    if (str.equals("true")) {
                        AppInfo.isSupportAutoReconnection = false;
                    } else {
                        AppInfo.isSupportAutoReconnection = true;
                    }
                    AppLog.i(TAG, " end isSupportAutoReconnection = " + AppInfo.isSupportAutoReconnection);
                }
                AppLog.d(TAG, "saveStreamVideo..........=true");
                ICatchWificamConfig.getInstance().enableDumpMediaStream(true, streamOutputPath);
                AppLog.d(TAG, "enableDumpMediaStream..........=false");
                ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                AppLog.d(TAG, "enableDumpMediaStream..........=false");
                ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                if (str4 != null) {
                    AppLog.d(TAG, "broadcast..........=" + str4);
                    if (str4.equals("true")) {
                        AppLog.d(TAG, "broadcast..........=" + str4);
                        AppInfo.isSupportBroadcast = true;
                    }
                    AppLog.d(TAG, "GlobalInfo.isSupportBroadcast..........=" + AppInfo.isSupportBroadcast);
                }
                if (str5 != null) {
                    AppLog.i(TAG, "supportSetting..........=" + str5);
                    if (str5.equals("true")) {
                        AppLog.i(TAG, "supportSetting..........=" + str5);
                        AppInfo.isSupportSetting = true;
                    }
                    AppLog.i(TAG, "GlobalInfo.isSupportSetting..........=" + AppInfo.isSupportSetting);
                }
                AppLog.d(TAG, "disconnectRetry=" + str8);
                if (str8 != null) {
                    AppLog.d(TAG, "retryCount=" + Integer.parseInt(str8));
                }
                if (str9 != null) {
                    if (str9.equals("true")) {
                        ICatchWificamConfig.getInstance().enableSoftwareDecoder(false);
                        GlobalInfo.enableSoftwareDecoder = false;
                    } else {
                        ICatchWificamConfig.getInstance().enableSoftwareDecoder(true);
                        Log.d("1111", "open SoftwareDecoder");
                        GlobalInfo.enableSoftwareDecoder = true;
                    }
                }
                AppLog.i(TAG, "disableAudio=" + disableAudio);
                if (disableAudio != null) {
                    if (disableAudio.equals("true")) {
                        AppInfo.disableAudio = false;
                    } else {
                        AppInfo.disableAudio = true;
                    }
                    AppLog.i(TAG, "AppInfo.disableAudio..........=" + AppInfo.disableAudio);
                }
            } catch (IOException e7) {
                e3222 = e7;
                e3222.printStackTrace();
                if (out != null) {
                    out.close();
                }
                cfgInfo = new CfgProperty(directoryPath + fileName);
                str = null;
                str2 = null;
                str3 = null;
                str4 = null;
                str5 = null;
                str6 = null;
                str7 = null;
                str8 = null;
                str9 = null;
                disableAudio = null;
                str = cfgInfo.getProperty("SupportAutoReconnection");
                str2 = cfgInfo.getProperty("SaveStreamVideo");
                str3 = cfgInfo.getProperty("SaveStreamAudio");
                str4 = cfgInfo.getProperty("broadcast");
                str5 = cfgInfo.getProperty("SupportSetting");
                str6 = cfgInfo.getProperty("SaveAppLog");
                str7 = cfgInfo.getProperty("SaveSDKLog");
                str8 = cfgInfo.getProperty("disconnectRetry");
                str9 = cfgInfo.getProperty("enableSoftwareDecoder");
                disableAudio = cfgInfo.getProperty("disableAudio");
                streamOutputPath = Environment.getExternalStorageDirectory().toString() + AppInfo.STREAM_OUTPUT_DIRECTORY_PATH;
                FileOper.createDirectory(streamOutputPath);
                if (str6 != null) {
                    if (str6.equals("true")) {
                        AppLog.enableAppLog();
                    }
                    AppLog.i(TAG, "GlobalInfo.saveAppLog..........=" + str6);
                }
                if (str7 != null) {
                    if (str7.equals("true")) {
                        AppInfo.saveSDKLog = true;
                        SdkLog.getInstance().enableSDKLog();
                    }
                    AppLog.i(TAG, "GlobalInfo.saveSDKLog..........=" + AppInfo.saveSDKLog);
                }
                if (str != null) {
                    if (str.equals("true")) {
                        AppInfo.isSupportAutoReconnection = true;
                    } else {
                        AppInfo.isSupportAutoReconnection = false;
                    }
                    AppLog.i(TAG, " end isSupportAutoReconnection = " + AppInfo.isSupportAutoReconnection);
                }
                AppLog.d(TAG, "saveStreamVideo..........=true");
                ICatchWificamConfig.getInstance().enableDumpMediaStream(true, streamOutputPath);
                AppLog.d(TAG, "enableDumpMediaStream..........=false");
                ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                AppLog.d(TAG, "enableDumpMediaStream..........=false");
                ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
                if (str4 != null) {
                    AppLog.d(TAG, "broadcast..........=" + str4);
                    if (str4.equals("true")) {
                        AppLog.d(TAG, "broadcast..........=" + str4);
                        AppInfo.isSupportBroadcast = true;
                    }
                    AppLog.d(TAG, "GlobalInfo.isSupportBroadcast..........=" + AppInfo.isSupportBroadcast);
                }
                if (str5 != null) {
                    AppLog.i(TAG, "supportSetting..........=" + str5);
                    if (str5.equals("true")) {
                        AppLog.i(TAG, "supportSetting..........=" + str5);
                        AppInfo.isSupportSetting = true;
                    }
                    AppLog.i(TAG, "GlobalInfo.isSupportSetting..........=" + AppInfo.isSupportSetting);
                }
                AppLog.d(TAG, "disconnectRetry=" + str8);
                if (str8 != null) {
                    AppLog.d(TAG, "retryCount=" + Integer.parseInt(str8));
                }
                if (str9 != null) {
                    if (str9.equals("true")) {
                        ICatchWificamConfig.getInstance().enableSoftwareDecoder(true);
                        Log.d("1111", "open SoftwareDecoder");
                        GlobalInfo.enableSoftwareDecoder = true;
                    } else {
                        ICatchWificamConfig.getInstance().enableSoftwareDecoder(false);
                        GlobalInfo.enableSoftwareDecoder = false;
                    }
                }
                AppLog.i(TAG, "disableAudio=" + disableAudio);
                if (disableAudio != null) {
                    if (disableAudio.equals("true")) {
                        AppInfo.disableAudio = true;
                    } else {
                        AppInfo.disableAudio = false;
                    }
                    AppLog.i(TAG, "AppInfo.disableAudio..........=" + AppInfo.disableAudio);
                }
            }
        }
        cfgInfo = new CfgProperty(directoryPath + fileName);
        str = null;
        str2 = null;
        str3 = null;
        str4 = null;
        str5 = null;
        str6 = null;
        str7 = null;
        str8 = null;
        str9 = null;
        disableAudio = null;
        try {
            str = cfgInfo.getProperty("SupportAutoReconnection");
            str2 = cfgInfo.getProperty("SaveStreamVideo");
            str3 = cfgInfo.getProperty("SaveStreamAudio");
            str4 = cfgInfo.getProperty("broadcast");
            str5 = cfgInfo.getProperty("SupportSetting");
            str6 = cfgInfo.getProperty("SaveAppLog");
            str7 = cfgInfo.getProperty("SaveSDKLog");
            str8 = cfgInfo.getProperty("disconnectRetry");
            str9 = cfgInfo.getProperty("enableSoftwareDecoder");
            disableAudio = cfgInfo.getProperty("disableAudio");
        } catch (FileNotFoundException e22) {
            e22.printStackTrace();
        } catch (IOException e32222) {
            e32222.printStackTrace();
        }
        streamOutputPath = Environment.getExternalStorageDirectory().toString() + AppInfo.STREAM_OUTPUT_DIRECTORY_PATH;
        FileOper.createDirectory(streamOutputPath);
        if (str6 != null) {
            if (str6.equals("true")) {
                AppLog.enableAppLog();
            }
            AppLog.i(TAG, "GlobalInfo.saveAppLog..........=" + str6);
        }
        if (str7 != null) {
            if (str7.equals("true")) {
                AppInfo.saveSDKLog = true;
                SdkLog.getInstance().enableSDKLog();
            }
            AppLog.i(TAG, "GlobalInfo.saveSDKLog..........=" + AppInfo.saveSDKLog);
        }
        if (str != null) {
            if (str.equals("true")) {
                AppInfo.isSupportAutoReconnection = true;
            } else {
                AppInfo.isSupportAutoReconnection = false;
            }
            AppLog.i(TAG, " end isSupportAutoReconnection = " + AppInfo.isSupportAutoReconnection);
        }
        if (str2 != null && str2.equals("true")) {
            AppLog.d(TAG, "saveStreamVideo..........=true");
            ICatchWificamConfig.getInstance().enableDumpMediaStream(true, streamOutputPath);
        }
        if (str3 != null && str3.equals("true")) {
            AppLog.d(TAG, "enableDumpMediaStream..........=false");
            ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
        }
        if (str3 != null && str3.equals("true")) {
            AppLog.d(TAG, "enableDumpMediaStream..........=false");
            ICatchWificamConfig.getInstance().enableDumpMediaStream(false, streamOutputPath);
        }
        if (str4 != null) {
            AppLog.d(TAG, "broadcast..........=" + str4);
            if (str4.equals("true")) {
                AppLog.d(TAG, "broadcast..........=" + str4);
                AppInfo.isSupportBroadcast = true;
            }
            AppLog.d(TAG, "GlobalInfo.isSupportBroadcast..........=" + AppInfo.isSupportBroadcast);
        }
        if (str5 != null) {
            AppLog.i(TAG, "supportSetting..........=" + str5);
            if (str5.equals("true")) {
                AppLog.i(TAG, "supportSetting..........=" + str5);
                AppInfo.isSupportSetting = true;
            }
            AppLog.i(TAG, "GlobalInfo.isSupportSetting..........=" + AppInfo.isSupportSetting);
        }
        AppLog.d(TAG, "disconnectRetry=" + str8);
        if (str8 != null) {
            AppLog.d(TAG, "retryCount=" + Integer.parseInt(str8));
        }
        if (str9 != null) {
            if (str9.equals("true")) {
                ICatchWificamConfig.getInstance().enableSoftwareDecoder(true);
                Log.d("1111", "open SoftwareDecoder");
                GlobalInfo.enableSoftwareDecoder = true;
            } else {
                ICatchWificamConfig.getInstance().enableSoftwareDecoder(false);
                GlobalInfo.enableSoftwareDecoder = false;
            }
        }
        AppLog.i(TAG, "disableAudio=" + disableAudio);
        if (disableAudio != null) {
            if (disableAudio.equals("true")) {
                AppInfo.disableAudio = true;
            } else {
                AppInfo.disableAudio = false;
            }
            AppLog.i(TAG, "AppInfo.disableAudio..........=" + AppInfo.disableAudio);
        }
    }

    private void writeCfgInfo(String path, String cfgInfo) {
        FileOutputStream fileOutputStream;
        IOException e1;
        try {
            FileOutputStream out = new FileOutputStream(path);
            try {
                out.write(cfgInfo.getBytes(), 0, cfgInfo.getBytes().length);
                out.close();
                fileOutputStream = out;
            } catch (IOException e) {
                e1 = e;
                fileOutputStream = out;
                AppLog.i(TAG, "end writeCfgInfo :IOException ");
                e1.printStackTrace();
            }
        } catch (IOException e2) {
            e1 = e2;
            AppLog.i(TAG, "end writeCfgInfo :IOException ");
            e1.printStackTrace();
        }
    }
}
