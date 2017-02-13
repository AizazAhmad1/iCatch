package com.icatch.ismartdv2016.View.Fragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.icatch.ismartdv2016.AppDialog.AppDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnFragmentInteractionListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.Tools.WifiCheck;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import com.icatchtek.bluetooth.customer.type.ICatchWifiInformation;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uk.co.senab.photoview.BuildConfig;

public class BTPairCompletedFragment extends Fragment {
    private static final int CONNECT_CAMERA_FAILED = 17;
    private static final int CONNECT_WIFI_FAILED = 16;
    private static final int ENABLE_WIFI_FAILED = 14;
    private static final int START_CHECK_CONNECT_WIFI = 19;
    private String TAG = "BTPairCompletedFragment";
    private Handler appStartHandler = GlobalInfo.getInstance().getAppStartHandler();
    private ImageButton backBtn;
    private Timer connectTimer;
    private ExecutorService executor;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BTPairCompletedFragment.ENABLE_WIFI_FAILED /*14*/:
                    MyProgressDialog.closeProgressDialog();
                    Toast.makeText(BTPairCompletedFragment.this.getActivity(), "enable wifi failed.", 1).show();
                    return;
                case BTPairCompletedFragment.CONNECT_WIFI_FAILED /*16*/:
                    MyProgressDialog.closeProgressDialog();
                    Toast.makeText(BTPairCompletedFragment.this.getActivity(), "failed to connect wifi.", 1).show();
                    return;
                case BTPairCompletedFragment.CONNECT_CAMERA_FAILED /*17*/:
                    MyProgressDialog.closeProgressDialog();
                    Toast.makeText(BTPairCompletedFragment.this.getActivity(), "failed to connect camera.", 1).show();
                    return;
                case BTPairCompletedFragment.START_CHECK_CONNECT_WIFI /*19*/:
                    BTPairCompletedFragment.this.connectTimer = new Timer();
                    BTPairCompletedFragment.this.connectTimer.schedule(new WifiCheckTask(), 0, 6000);
                    return;
                default:
                    return;
            }
        }
    };
    private ICatchWifiInformation iCatchWifiAPInformation;
    private OnFragmentInteractionListener mListener;
    private View myView;
    String password;
    String ssid = BuildConfig.FLAVOR;
    private TextView txvPairCompleted;
    private WifiCheck wifiCheck;

    class ConnectWifiThread implements Runnable {
        int connectNum = 10;

        ConnectWifiThread() {
        }

        public void run() {
            if (BTPairCompletedFragment.this.enableWifi()) {
                if (BTPairCompletedFragment.this.iCatchWifiAPInformation == null) {
                    try {
                        AppLog.d(BTPairCompletedFragment.this.TAG, "------start getWifiInformation");
                        BTPairCompletedFragment.this.iCatchWifiAPInformation = GlobalInfo.iCatchBluetoothClient.getSystemControl().getWifiInformation();
                        AppLog.d(BTPairCompletedFragment.this.TAG, "------end getWifiInformation iCatchWifiAPInformation=" + BTPairCompletedFragment.this.iCatchWifiAPInformation);
                        if (BTPairCompletedFragment.this.iCatchWifiAPInformation == null) {
                            BTPairCompletedFragment.this.handler.post(new Runnable() {
                                public void run() {
                                    MyProgressDialog.closeProgressDialog();
                                    AppDialog.showDialogWarn(BTPairCompletedFragment.this.getActivity(), "get Wifi information is null!");
                                }
                            });
                            if (BTPairCompletedFragment.this.connectTimer != null) {
                                BTPairCompletedFragment.this.connectTimer.cancel();
                                return;
                            }
                            return;
                        }
                    } catch (IOException e) {
                        AppLog.d(BTPairCompletedFragment.this.TAG, "getWifiInformation IOException");
                        e.printStackTrace();
                        BTPairCompletedFragment.this.handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                                Toast.makeText(BTPairCompletedFragment.this.getActivity(), "connent IOException!", 1).show();
                            }
                        });
                        if (BTPairCompletedFragment.this.connectTimer != null) {
                            BTPairCompletedFragment.this.connectTimer.cancel();
                            return;
                        }
                        return;
                    } catch (IchBluetoothTimeoutException e2) {
                        AppLog.d(BTPairCompletedFragment.this.TAG, "getWifiInformation IchBluetoothTimeoutException");
                        e2.printStackTrace();
                        BTPairCompletedFragment.this.handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                                Toast.makeText(BTPairCompletedFragment.this.getActivity(), "connent timeout!", 1).show();
                            }
                        });
                        if (BTPairCompletedFragment.this.connectTimer != null) {
                            BTPairCompletedFragment.this.connectTimer.cancel();
                            return;
                        }
                        return;
                    } catch (IchBluetoothDeviceBusyException e3) {
                        AppLog.d(BTPairCompletedFragment.this.TAG, "getWifiInformation IchBluetoothDeviceBusyException");
                        e3.printStackTrace();
                        BTPairCompletedFragment.this.handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                                Toast.makeText(BTPairCompletedFragment.this.getActivity(), "Device busy!", 1).show();
                            }
                        });
                        if (BTPairCompletedFragment.this.connectTimer != null) {
                            BTPairCompletedFragment.this.connectTimer.cancel();
                            return;
                        }
                        return;
                    }
                }
                BTPairCompletedFragment.this.ssid = BTPairCompletedFragment.this.iCatchWifiAPInformation.getWifiSSID();
                BTPairCompletedFragment.this.password = BTPairCompletedFragment.this.iCatchWifiAPInformation.getWifiPassword();
                AppLog.d(BTPairCompletedFragment.this.TAG, "connectWifi encType=[" + BTPairCompletedFragment.this.iCatchWifiAPInformation.getWifiEncType() + "]");
                BTPairCompletedFragment.this.handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(BTPairCompletedFragment.this.getActivity(), "ssid=[" + BTPairCompletedFragment.this.ssid + "],pwd=[" + BTPairCompletedFragment.this.password + "]", 1).show();
                    }
                });
                AppLog.d(BTPairCompletedFragment.this.TAG, "connectWifi ssid=[" + BTPairCompletedFragment.this.ssid + "]");
                AppLog.d(BTPairCompletedFragment.this.TAG, "connectWifi password=[" + BTPairCompletedFragment.this.password + "]");
                BTPairCompletedFragment.this.wifiCheck.openWifi();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
                WifiManager wifiManager = (WifiManager) BTPairCompletedFragment.this.getActivity().getSystemService("wifi");
                while (wifiManager.getWifiState() == 2) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e5) {
                    }
                }
                do {
                    BTPairCompletedFragment.this.wifiCheck.connectWifi(BTPairCompletedFragment.this.ssid, BTPairCompletedFragment.this.password, 8);
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e42) {
                        e42.printStackTrace();
                    }
                    if (BTPairCompletedFragment.this.wifiCheck.isWifiConnected(BTPairCompletedFragment.this.getActivity(), BTPairCompletedFragment.this.ssid)) {
                        BTPairCompletedFragment.this.handler.post(new Runnable() {
                            public void run() {
                                MyProgressDialog.closeProgressDialog();
                            }
                        });
                        if (GlobalInfo.iCatchBluetoothClient != null) {
                            try {
                                AppLog.d(BTPairCompletedFragment.this.TAG, "iCatchBluetoothClient.release()");
                                GlobalInfo.iCatchBluetoothClient.release();
                            } catch (IOException e6) {
                                AppLog.d(BTPairCompletedFragment.this.TAG, "iCatchBluetoothClient.release() IOException");
                                e6.printStackTrace();
                            }
                        }
                        BTPairCompletedFragment.this.appStartHandler.obtainMessage(5).sendToTarget();
                        return;
                    }
                    this.connectNum--;
                } while (this.connectNum >= 0);
                BTPairCompletedFragment.this.handler.obtainMessage(BTPairCompletedFragment.CONNECT_WIFI_FAILED).sendToTarget();
                return;
            }
            BTPairCompletedFragment.this.handler.obtainMessage(BTPairCompletedFragment.ENABLE_WIFI_FAILED).sendToTarget();
        }
    }

    class WifiCheckTask extends TimerTask {
        int reconnectTime = 0;

        WifiCheckTask() {
        }

        public void run() {
            AppLog.d(BTPairCompletedFragment.this.TAG, "WifiCheckTask ssid=" + BTPairCompletedFragment.this.ssid);
            if (BTPairCompletedFragment.this.wifiCheck.isWifiConnected(BTPairCompletedFragment.this.getActivity(), BTPairCompletedFragment.this.ssid)) {
                AppLog.d(BTPairCompletedFragment.this.TAG, "isWifiConnect() == true");
                GlobalInfo.isReleaseBTClient = false;
                if (BTPairCompletedFragment.this.connectTimer != null) {
                    BTPairCompletedFragment.this.connectTimer.cancel();
                }
                BTPairCompletedFragment.this.handler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                    }
                });
                if (GlobalInfo.iCatchBluetoothClient != null) {
                    try {
                        AppLog.d(BTPairCompletedFragment.this.TAG, "iCatchBluetoothClient.release()");
                        GlobalInfo.iCatchBluetoothClient.release();
                    } catch (IOException e) {
                        AppLog.d(BTPairCompletedFragment.this.TAG, "iCatchBluetoothClient.release() IOException");
                        e.printStackTrace();
                    }
                }
                BTPairCompletedFragment.this.appStartHandler.obtainMessage(5).sendToTarget();
                BTPairCompletedFragment.this.executor.shutdown();
                return;
            }
            AppLog.d(BTPairCompletedFragment.this.TAG, "isWifiConnect() == false  reconnectTime =" + this.reconnectTime);
            this.reconnectTime++;
            if (this.reconnectTime >= 15) {
                if (BTPairCompletedFragment.this.connectTimer != null) {
                    BTPairCompletedFragment.this.connectTimer.cancel();
                }
                BTPairCompletedFragment.this.handler.obtainMessage(BTPairCompletedFragment.CONNECT_WIFI_FAILED).sendToTarget();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.myView = inflater.inflate(R.layout.fragment_btpair_completed, container, false);
        this.txvPairCompleted = (TextView) this.myView.findViewById(R.id.done_txv);
        this.txvPairCompleted.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyProgressDialog.showProgressDialog(BTPairCompletedFragment.this.getActivity(), "Connecting..");
                BTPairCompletedFragment.this.executor = Executors.newSingleThreadExecutor();
                BTPairCompletedFragment.this.executor.submit(new ConnectWifiThread(), null);
            }
        });
        this.backBtn = (ImageButton) this.myView.findViewById(R.id.back_btn);
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BTPairCompletedFragment.this.mListener != null) {
                    BTPairCompletedFragment.this.mListener.removeFragment();
                }
            }
        });
        return this.myView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnFragmentInteractionListener) getActivity();
            this.wifiCheck = new WifiCheck(getActivity());
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onResume() {
        if (this.mListener != null) {
            this.mListener.submitFragmentInfo(BTPairCompletedFragment.class.getSimpleName(), R.string.title_fragment_btpair_completed);
        }
        super.onResume();
    }

    private boolean enableWifi() {
        Boolean retValue = Boolean.valueOf(false);
        try {
            AppLog.d(this.TAG, "start enableWifi");
            retValue = Boolean.valueOf(GlobalInfo.iCatchBluetoothClient.getSystemControl().enableWifi());
            AppLog.d(this.TAG, "end  enableWifi retValue =" + retValue);
        } catch (IOException e) {
            AppLog.d(this.TAG, "enableWifi() IOException");
            retValue = Boolean.valueOf(false);
            e.printStackTrace();
        } catch (IchBluetoothTimeoutException e2) {
            AppLog.d(this.TAG, "enableWifi() IchBluetoothTimeoutException");
            retValue = Boolean.valueOf(false);
            e2.printStackTrace();
        } catch (IchBluetoothDeviceBusyException e3) {
            AppLog.d(this.TAG, "enableWifi() IchBluetoothDeviceBusyException");
            e3.printStackTrace();
        }
        AppLog.d(this.TAG, "enableWifi ret=" + retValue);
        return retValue.booleanValue();
    }
}
