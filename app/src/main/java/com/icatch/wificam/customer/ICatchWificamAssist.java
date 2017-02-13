package com.icatch.wificam.customer;

import android.content.Context;
import android.util.Log;
import com.icatch.ismartdv2016.Android.NoDoubleClickListener;
import com.icatch.wificam.core.CoreLogger;
import com.icatch.wificam.core.CoreMulticast;
import com.icatch.wificam.core.jni.JWificamAssist;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNotSupportedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchTimeOutException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ICatchWificamAssist {
    private static ICatchWificamAssist instance = new ICatchWificamAssist();
    private byte[] __default_key = new byte[]{(byte) 33, (byte) 126, (byte) 26, (byte) 22, (byte) 40, (byte) -34, (byte) -46, (byte) -89, (byte) -85, (byte) -25, (byte) -123, (byte) -120, (byte) 9, (byte) -54, (byte) 64, (byte) 60};
    private MulticastReceiver multicastReceiver = null;
    private boolean multicastReceiverRun = false;
    private boolean receiverCanceled = true;
    private boolean tcpReceiverRun = false;

    private class MulticastReceiver extends Thread {
        private String content;
        private Context context;
        private CoreMulticast multicast = new CoreMulticast();
        private int timeout;

        public MulticastReceiver(Context context, int timeout) {
            this.context = context;
            this.timeout = timeout;
        }

        public String getContent() {
            return this.content;
        }

        public void run() {
            this.multicast.prepare(this.context);
            long time = System.currentTimeMillis();
            while (ICatchWificamAssist.this.multicastReceiverRun && System.currentTimeMillis() - time < ((long) (this.timeout * NoDoubleClickListener.MIN_CLICK_DELAY_TIME))) {
                this.content = this.multicast.receive();
                if (this.content != null) {
                    Log.i("multicast.receive", "received content: " + this.content);
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.multicast.release();
        }
    }

    public static ICatchWificamAssist getInstance() {
        return instance;
    }

    public static void destroy() {
    }

    public boolean supportLocalPlay(String file) {
        return JWificamAssist.supportLocalPlay_Jni(file);
    }

    public boolean loadLibrary(Context context, String libPath, String libName) {
        File libs_dir = context.getDir("libs", 0);
        Log.i("libs_dir", "absolute path: " + libs_dir.getAbsolutePath());
        String new_file_name = libs_dir.getPath() + "/" + libName;
        if (!copyLibrary(new_file_name, libPath + "/" + libName)) {
            return false;
        }
        try {
            System.load(new_file_name);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean copyLibrary(String new_file_name, String old_file_name) {
        Exception e;
        Throwable th;
        boolean z = false;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File new_file = new File(new_file_name);
            File old_file = new File(old_file_name);
            if (new_file.exists()) {
                new_file.delete();
            }
            FileInputStream fis2 = new FileInputStream(old_file);
            try {
                FileOutputStream fos2 = new FileOutputStream(new_file);
                try {
                    byte[] dataBuffer = new byte[2048];
                    while (true) {
                        int dataSize = fis2.read(dataBuffer);
                        if (dataSize == -1) {
                            break;
                        }
                        fos2.write(dataBuffer, 0, dataSize);
                    }
                    fos2.flush();
                    z = true;
                    if (fos2 != null) {
                        try {
                            fos2.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fis2 != null) {
                        try {
                            fis2.close();
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                    }
                    fos = fos2;
                    fis = fis2;
                } catch (Exception e3) {
                    e22 = e3;
                    fos = fos2;
                    fis = fis2;
                    try {
                        e22.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (Exception e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (Exception e2222) {
                                e2222.printStackTrace();
                            }
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (Exception e22222) {
                                e22222.printStackTrace();
                            }
                        }
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (Exception e222222) {
                                e222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    fis = fis2;
                    if (fos != null) {
                        fos.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e222222 = e4;
                fis = fis2;
                e222222.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
                return z;
            } catch (Throwable th4) {
                th = th4;
                fis = fis2;
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            e222222 = e5;
            e222222.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            if (fis != null) {
                fis.close();
            }
            return z;
        }
        return z;
    }

    public boolean updateFw(ICatchWificamSession session, String fwFile) throws IchInvalidSessionException, IchSocketException, IchCameraModeException, IchDevicePropException, IchTimeOutException, IchDeviceException, IchNotSupportedException {
        return JWificamAssist.updateFw_Jni(session.getSessionID(), fwFile);
    }

    public void notifyUpdateFw() {
        JWificamAssist.notifyUpdateFw_Jni();
    }

    public String simpleConfig(Context context, String essid, String passwd, String ipAddr, String macAddr) throws IchSocketException, IchTimeOutException, IchInvalidArgumentException {
        return simpleConfig(context, essid, passwd, this.__default_key, ipAddr, macAddr, 60);
    }

    public String simpleConfig(Context context, String essid, String passwd, byte[] key, String ipAddr, String macAddr, int timeout) throws IchSocketException, IchTimeOutException, IchInvalidArgumentException {
        if (key == null || key.length != 16) {
            throw new IchInvalidArgumentException("The length of key must be 16.");
        } else if (ipAddr == null || ipAddr.length() <= 0) {
            throw new IchInvalidArgumentException("No valid data in ipAddr.");
        } else if (macAddr == null || macAddr.length() <= 0) {
            throw new IchInvalidArgumentException("No valid data in macAddr.");
        } else if (!this.receiverCanceled || (this.multicastReceiver != null && this.multicastReceiver.isAlive())) {
            throw new IchSocketException("Socket error, do you forget to stop previous simple config process.");
        } else if (!JWificamAssist.simpleConfig_Jni(essid, passwd, key, ipAddr, macAddr, timeout)) {
            return null;
        } else {
            this.receiverCanceled = false;
            this.tcpReceiverRun = true;
            this.multicastReceiverRun = true;
            this.multicastReceiver = new MulticastReceiver(context, timeout);
            this.multicastReceiver.start();
            String content = null;
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < ((long) (timeout * NoDoubleClickListener.MIN_CLICK_DELAY_TIME))) {
                if (this.tcpReceiverRun) {
                    try {
                        content = JWificamAssist.simpleConfigGet_Jni();
                    } catch (Exception ex) {
                        CoreLogger.logI("simple_config", "receive sock response: " + ex.getMessage());
                    }
                    if (content != null) {
                        simpleConfigCancel();
                        CoreLogger.logI("simple_config", "tcp sock receive content");
                        break;
                    }
                }
                if (this.multicastReceiverRun) {
                    content = this.multicastReceiver.getContent();
                    if (content != null) {
                        simpleConfigCancel();
                        CoreLogger.logI("simple_config", "multi cast sock receive content");
                        break;
                    }
                }
                if (this.receiverCanceled || !(this.tcpReceiverRun || this.multicastReceiverRun)) {
                    CoreLogger.logI("simple_config", "loop control flag break.");
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CoreLogger.logI("simple_config", "timeout: " + (timeout * NoDoubleClickListener.MIN_CLICK_DELAY_TIME) + ", passed: " + (System.currentTimeMillis() - time));
            this.tcpReceiverRun = false;
            this.receiverCanceled = true;
            this.multicastReceiverRun = false;
            try {
                this.multicastReceiver.join();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            if (content != null) {
                return content;
            }
            throw new IchTimeOutException("Timeout to receive camera's content");
        }
    }

    public boolean simpleConfigCancel() {
        this.tcpReceiverRun = false;
        this.multicastReceiverRun = false;
        try {
            this.multicastReceiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.receiverCanceled = true;
        return JWificamAssist.simpleConfigCancel_Jni();
    }
}
