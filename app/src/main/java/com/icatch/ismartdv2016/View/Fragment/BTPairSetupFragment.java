package com.icatch.ismartdv2016.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.icatch.ismartdv2016.AppDialog.AppDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnFragmentInteractionListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.SystemInfo.SystemInfo;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import com.icatchtek.bluetooth.customer.type.ICatchWifiInformation;
import java.io.IOException;

public class BTPairSetupFragment extends Fragment {
    private static String TAG = "BTPairSetupFragment";
    private Handler appStartHandler = GlobalInfo.getInstance().getAppStartHandler();
    private ImageButton backBtn;
    private Button btnSetup;
    private EditText cameraPassword;
    private EditText cameraSsid;
    private Handler handler = new Handler();
    private ICatchWifiInformation iCatchWifiInformation;
    private OnFragmentInteractionListener mListener;
    private View myView;
    private TextView skipTxv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.myView != null) {
            return this.myView;
        }
        this.myView = inflater.inflate(R.layout.fragment_btpair_setup, container, false);
        this.btnSetup = (Button) this.myView.findViewById(R.id.bt_wifisetup);
        this.cameraSsid = (EditText) this.myView.findViewById(R.id.bt_wifisetup_camera_ssid);
        this.cameraPassword = (EditText) this.myView.findViewById(R.id.bt_wifisetup_camera_password);
        this.backBtn = (ImageButton) this.myView.findViewById(R.id.back_btn);
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BTPairSetupFragment.this.mListener != null) {
                    BTPairSetupFragment.this.mListener.removeFragment();
                }
            }
        });
        this.skipTxv = (TextView) this.myView.findViewById(R.id.skip_txv);
        this.skipTxv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BTPairCompletedFragment BTPairCompleted = new BTPairCompletedFragment();
                FragmentTransaction ft = BTPairSetupFragment.this.getFragmentManager().beginTransaction();
                ft.replace(R.id.launch_setting_frame, BTPairCompleted);
                ft.addToBackStack("BTPairCompletedFragment");
                ft.commit();
            }
        });
        new Thread(new Runnable() {
            public void run() {
                AppLog.d(BTPairSetupFragment.TAG, "start getWifiInformation");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BTPairSetupFragment.this.iCatchWifiInformation = new ICatchWifiInformation();
                try {
                    BTPairSetupFragment.this.iCatchWifiInformation = GlobalInfo.iCatchBluetoothClient.getSystemControl().getWifiInformation();
                } catch (IOException e1) {
                    AppLog.d(BTPairSetupFragment.TAG, "getWifiInformation IOException");
                    e1.printStackTrace();
                } catch (IchBluetoothTimeoutException e12) {
                    AppLog.d(BTPairSetupFragment.TAG, "getWifiInformation IchBluetoothTimeoutException");
                    e12.printStackTrace();
                } catch (IchBluetoothDeviceBusyException e13) {
                    AppLog.d(BTPairSetupFragment.TAG, "getWifiInformation IchBluetoothDeviceBusyException");
                    e13.printStackTrace();
                }
                AppLog.d(BTPairSetupFragment.TAG, "end getWifiInformation iCatchWifiInformation=" + BTPairSetupFragment.this.iCatchWifiInformation);
                if (BTPairSetupFragment.this.iCatchWifiInformation != null) {
                    final String ssid = BTPairSetupFragment.this.iCatchWifiInformation.getWifiSSID();
                    final String password = BTPairSetupFragment.this.iCatchWifiInformation.getWifiPassword();
                    AppLog.d(BTPairSetupFragment.TAG, "getWifiInformation ssid=" + ssid);
                    AppLog.d(BTPairSetupFragment.TAG, "getWifiInformation password=" + password);
                    BTPairSetupFragment.this.handler.post(new Runnable() {
                        public void run() {
                            AppLog.d(BTPairSetupFragment.TAG, "handler.post setText");
                            MyProgressDialog.closeProgressDialog();
                            if (ssid != null) {
                                BTPairSetupFragment.this.cameraSsid.setText(ssid);
                            }
                            if (password != null) {
                                BTPairSetupFragment.this.cameraPassword.setText(password);
                            }
                        }
                    });
                    return;
                }
                BTPairSetupFragment.this.handler.post(new Runnable() {
                    public void run() {
                        MyProgressDialog.closeProgressDialog();
                        AppDialog.showDialogWarn(BTPairSetupFragment.this.getActivity(), "get Wifi information is null!");
                    }
                });
            }
        }).start();
        this.btnSetup.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AppLog.d(BTPairSetupFragment.TAG, "start btnSetup onClick");
                SystemInfo.hideInputMethod(BTPairSetupFragment.this.getActivity());
                boolean retValue = true;
                String ssid = BTPairSetupFragment.this.cameraSsid.getText().toString();
                String password = BTPairSetupFragment.this.cameraPassword.getText().toString();
                AppLog.d(BTPairSetupFragment.TAG, "btnSetup onClick ssid=[" + ssid + "]");
                AppLog.d(BTPairSetupFragment.TAG, "btnSetup onClick password=[" + password + "]");
                if (ssid != null && password != null) {
                    if (ssid.length() > 20) {
                        BTPairSetupFragment.this.cameraSsid.setError(BTPairSetupFragment.this.getText(R.string.camera_name_limit));
                        retValue = false;
                    }
                    if (BTPairSetupFragment.this.cameraPassword.length() > 10 || BTPairSetupFragment.this.cameraPassword.length() < 8) {
                        BTPairSetupFragment.this.cameraPassword.setError(BTPairSetupFragment.this.getText(R.string.password_limit));
                        retValue = false;
                    }
                    if (retValue) {
                        boolean ret = false;
                        ICatchWifiInformation iCatchWifiAPInformation = new ICatchWifiInformation();
                        iCatchWifiAPInformation.setWifiSSID(ssid);
                        iCatchWifiAPInformation.setWifiPassword(password);
                        AppLog.d(BTPairSetupFragment.TAG, "setWifiInformation ssid=" + ssid);
                        AppLog.d(BTPairSetupFragment.TAG, "setWifiInformation password=" + password);
                        try {
                            ret = GlobalInfo.iCatchBluetoothClient.getSystemControl().setWifiInformation(iCatchWifiAPInformation);
                        } catch (IOException e) {
                            AppLog.d(BTPairSetupFragment.TAG, "setWifiInformation IOException");
                            e.printStackTrace();
                        } catch (IchBluetoothTimeoutException e2) {
                            AppLog.d(BTPairSetupFragment.TAG, "setWifiInformation IOException");
                            e2.printStackTrace();
                        } catch (IchBluetoothDeviceBusyException e3) {
                            AppLog.d(BTPairSetupFragment.TAG, "setWifiInformation IOException");
                            e3.printStackTrace();
                        }
                        AppLog.d(BTPairSetupFragment.TAG, "setWifiInformation ret=" + ret);
                        if (ret) {
                            BTPairCompletedFragment BTPairCompleted = new BTPairCompletedFragment();
                            FragmentTransaction ft = BTPairSetupFragment.this.getFragmentManager().beginTransaction();
                            ft.replace(R.id.launch_setting_frame, BTPairCompleted);
                            ft.addToBackStack("tag");
                            ft.commit();
                            return;
                        }
                        MyToast.show(BTPairSetupFragment.this.getActivity(), "Setup false!");
                    }
                }
            }
        });
        MyProgressDialog.showProgressDialog(getActivity(), "init...");
        return this.myView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnFragmentInteractionListener) getActivity();
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
            this.mListener.submitFragmentInfo(BTPairSetupFragment.class.getSimpleName(), R.string.title_fragment_btpair_wifisetup);
        }
        super.onResume();
    }

    public void onDestroy() {
        if (GlobalInfo.iCatchBluetoothClient != null) {
            if (GlobalInfo.isReleaseBTClient) {
                try {
                    AppLog.d(TAG, "onDestroy() iCatchBluetoothClient.release()");
                    GlobalInfo.iCatchBluetoothClient.release();
                } catch (IOException e) {
                    AppLog.d(TAG, "iCatchBluetoothClient.release() IOException");
                    e.printStackTrace();
                }
            }
            GlobalInfo.isNeedGetBTClient = true;
            super.onDestroy();
        }
    }
}
