package com.icatch.ismartdv2016.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.icatch.ismartdv2016.Adapter.BlueToothListAdapter;
import com.icatch.ismartdv2016.BaseItems.BluetoothAppDevice;
import com.icatch.ismartdv2016.ExtendComponent.MyProgressDialog;
import com.icatch.ismartdv2016.ExtendComponent.MyToast;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Fragment.BTPairSetupFragment;
import com.icatch.ismartdv2016.View.Interface.BTPairBeginFragmentView;
import com.icatchtek.bluetooth.customer.ICatchBluetoothAdapter;
import com.icatchtek.bluetooth.customer.ICatchBluetoothManager;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothContextInvalidException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothNotSupportedException;
import com.icatchtek.bluetooth.customer.listener.ICatchBTDeviceDetectedListener;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiverID;
import com.icatchtek.bluetooth.customer.type.ICatchBluetoothDevice;
import com.slidingmenu.lib.SlidingMenu;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BTPairBeginPresenter {
    private static final int BLUETOOTH_SCAN_TIME_OUT = 4;
    private static final int CONNECT_CAMERA_FAILED = 7;
    private static final int CONNECT_WIFI_FAILED = 6;
    private static final int GET_BLUETOOTH_CLIENT = 3;
    private static final int GET_BLUETOOTH_CLIENT_FAILED = 9;
    private static final int GET_BLUETOOTH_CLIENT_SUCCESS = 8;
    private static final int GET_BLUETOOTH_DEVICE = 2;
    private static final int REQUEST_BLUETOOTH_SCAN = 0;
    private String TAG = "BTPairBeginPresenter";
    private Activity activity;
    private Context appContext;
    private BlueToothListAdapter blueToothListAdapter;
    private ICatchBluetoothAdapter bluetoothAdapter;
    private List<BluetoothAppDevice> bluetoothDeviceList;
    private BluetoothListener bluetoothListener = new BluetoothListener();
    private ICatchBluetoothManager bluetoothManager;
    private BluetoothLEConnectionStateReceiver connectionStateReceiver;
    private int curListViewPointer = 0;
    private ExecutorService executor;
    FragmentManager fragmentManager;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            BTPairSetupFragment fragment;
            FragmentTransaction ft;
            switch (msg.what) {
                case SlidingMenu.TOUCHMODE_MARGIN /*0*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "REQUEST_BLUETOOTH_SCAN");
                    BTPairBeginPresenter.this.startScan();
                    BTPairBeginPresenter.this.startSearchTimeoutTimer();
                    MyProgressDialog.showProgressDialog(BTPairBeginPresenter.this.activity, "Search...");
                    return;
                case BTPairBeginPresenter.GET_BLUETOOTH_DEVICE /*2*/:
                    MyProgressDialog.closeProgressDialog();
                    LinkedList<BluetoothAppDevice> tempDeviceList = new LinkedList();
                    for (BluetoothAppDevice temp : BTPairBeginPresenter.this.bluetoothDeviceList) {
                        if (temp.getBluetoothExist()) {
                            tempDeviceList.add(temp);
                        }
                    }
                    BTPairBeginPresenter.this.bluetoothDeviceList = tempDeviceList;
                    if (BTPairBeginPresenter.this.blueToothListAdapter == null) {
                        BTPairBeginPresenter.this.blueToothListAdapter = new BlueToothListAdapter(BTPairBeginPresenter.this.activity, BTPairBeginPresenter.this.bluetoothDeviceList, GlobalInfo.isBLE);
                    } else {
                        BTPairBeginPresenter.this.blueToothListAdapter.notifyDataSetInvalidated();
                        BTPairBeginPresenter.this.blueToothListAdapter = new BlueToothListAdapter(BTPairBeginPresenter.this.activity, BTPairBeginPresenter.this.bluetoothDeviceList, GlobalInfo.isBLE);
                    }
                    BTPairBeginPresenter.this.pairBeginFragmentView.setBTListViewAdapter(BTPairBeginPresenter.this.blueToothListAdapter);
                    return;
                case BTPairBeginPresenter.GET_BLUETOOTH_CLIENT /*3*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "GET_BLUETOOTH_CLIENT curListViewPointer=" + BTPairBeginPresenter.this.curListViewPointer);
                    MyProgressDialog.showProgressDialog(BTPairBeginPresenter.this.activity, "Connecting...");
                    if (GlobalInfo.isNeedGetBTClient) {
                        GlobalInfo.isNeedGetBTClient = false;
                        String mac = msg.obj;
                        BTPairBeginPresenter.this.executor = Executors.newSingleThreadExecutor();
                        BTPairBeginPresenter.this.executor.submit(new GetBtClientThread(BTPairBeginPresenter.this.handler, mac), null);
                        return;
                    }
                    AppLog.d(BTPairBeginPresenter.this.TAG, "isNeedGetBTClient =" + GlobalInfo.isNeedGetBTClient);
                    return;
                case BTPairBeginPresenter.BLUETOOTH_SCAN_TIME_OUT /*4*/:
                    BTPairBeginPresenter.this.closeScan();
                    MyProgressDialog.closeProgressDialog();
                    return;
                case BTPairBeginPresenter.CONNECT_WIFI_FAILED /*6*/:
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "failed to connect wifi.");
                    return;
                case BTPairBeginPresenter.CONNECT_CAMERA_FAILED /*7*/:
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "failed to connect camera.");
                    return;
                case BTPairBeginPresenter.GET_BLUETOOTH_CLIENT_SUCCESS /*8*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "Receive GET_BLUETOOTH_CLIENT_SUCCESS");
                    MyProgressDialog.closeProgressDialog();
                    if (!GlobalInfo.isBLE) {
                        fragment = new BTPairSetupFragment();
                        ft = BTPairBeginPresenter.this.fragmentManager.beginTransaction();
                        ft.replace(R.id.launch_setting_frame, fragment);
                        ft.addToBackStack("BTPairSetupFragment");
                        ft.commit();
                        return;
                    }
                    return;
                case BTPairBeginPresenter.GET_BLUETOOTH_CLIENT_FAILED /*9*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "Receive GET_BLUETOOTH_CLIENT_FAILED");
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "connect failed,please tryagain!");
                    return;
                case com.slidingmenu.lib.R.styleable.SherlockTheme_searchViewCloseIcon /*33*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "Receive BT_LE_GATT_CONNECTED");
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "ble device connected.");
                    if (GlobalInfo.isBLE) {
                        fragment = new BTPairSetupFragment();
                        ft = BTPairBeginPresenter.this.fragmentManager.beginTransaction();
                        ft.replace(R.id.launch_setting_frame, fragment);
                        ft.addToBackStack("BTPairSetupFragment");
                        ft.commit();
                        return;
                    }
                    return;
                case com.slidingmenu.lib.R.styleable.SherlockTheme_searchViewGoIcon /*34*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "Receive BT_LE_GATT_DISCONNECTED");
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "fatal error, ble device not connected, please tryagain.");
                    return;
                case com.slidingmenu.lib.R.styleable.SherlockTheme_searchViewSearchIcon /*35*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "Receive BT_LE_GATT_SERVICES_DISCOVERED");
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "ble service discovered.");
                    return;
                case com.slidingmenu.lib.R.styleable.SherlockTheme_searchViewVoiceIcon /*36*/:
                    AppLog.d(BTPairBeginPresenter.this.TAG, "Receive BT_LE_GATT_NO_SERVICES_DISCOVERED");
                    MyProgressDialog.closeProgressDialog();
                    MyToast.show(BTPairBeginPresenter.this.activity, "ble no service discovered.");
                    return;
                default:
                    return;
            }
        }
    };
    Handler launchHandler;
    private BTPairBeginFragmentView pairBeginFragmentView;
    private boolean scanning = false;
    private Timer searchTimer;
    protected ICatchBluetoothClient tempClient;
    private ICatchBroadcastReceiver userBroadcastReceiver = new ICatchBroadcastReceiver() {
        public void onReceive(Intent intent) {
            if (intent.getAction().equals(ICatchBroadcastReceiverID.BT_ACTION_BOND_STATE_CHANGED)) {
                int bondState = intent.getIntExtra(ICatchBroadcastReceiverID.BT_BOND_STATE, 1);
                String mac = intent.getStringExtra(ICatchBroadcastReceiverID.BT_ADAPTER_ADDRESS);
                AppLog.d(BTPairBeginPresenter.this.TAG, "blue tooth bondState =" + bondState);
                AppLog.d(BTPairBeginPresenter.this.TAG, "blue tooth mac =" + mac);
                BTPairBeginPresenter.this.applyUIBondState(bondState, mac);
            }
        }
    };

    public class BluetoothLEConnectionStateReceiver implements ICatchBroadcastReceiver {
        private Handler handler = null;

        public BluetoothLEConnectionStateReceiver(Handler handler) {
            this.handler = handler;
        }

        public void onReceive(Intent intent) {
            if (intent.getAction().equals(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_CONNECTION_STATE_CHANGED)) {
                int connectionState = intent.getIntExtra(ICatchBroadcastReceiverID.BT_LE_GATT_CONNECTION_STATE, 34);
                Message message = new Message();
                message.what = connectionState;
                this.handler.sendMessage(message);
            }
        }
    }

    private class BluetoothListener implements ICatchBTDeviceDetectedListener {
        private BluetoothListener() {
        }

        public void deviceDetected(ICatchBluetoothDevice arg0) {
            AppLog.d(BTPairBeginPresenter.this.TAG, "get new device name =" + arg0.getName());
            boolean isExistSameDevice = false;
            if (BTPairBeginPresenter.this.bluetoothDeviceList == null) {
                BTPairBeginPresenter.this.bluetoothDeviceList = new LinkedList();
            }
            for (BluetoothAppDevice temp : BTPairBeginPresenter.this.bluetoothDeviceList) {
                if (arg0.getAddress().equals(temp.getBluetoothAddr())) {
                    isExistSameDevice = true;
                    temp.setBluetoothExist(true);
                    break;
                }
            }
            if (!isExistSameDevice) {
                BTPairBeginPresenter.this.bluetoothDeviceList.add(new BluetoothAppDevice(arg0.getName(), arg0.getAddress(), arg0.isBonded(), true));
                BTPairBeginPresenter.this.handler.obtainMessage(BTPairBeginPresenter.GET_BLUETOOTH_DEVICE).sendToTarget();
            }
        }
    }

    class GetBtClientThread implements Runnable {
        Handler handler = null;
        String mac;

        GetBtClientThread(Handler handler, String mac) {
            this.handler = handler;
            this.mac = mac;
        }

        public void run() {
            AppLog.d(BTPairBeginPresenter.this.TAG, "start GetBtClientThread curListViewPointer=" + BTPairBeginPresenter.this.curListViewPointer);
            BTPairBeginPresenter.this.tempClient = null;
            try {
                BTPairBeginPresenter.this.tempClient = BTPairBeginPresenter.this.bluetoothManager.getBluetoothClient(BTPairBeginPresenter.this.appContext, this.mac, GlobalInfo.isBLE);
                AppLog.d(BTPairBeginPresenter.this.TAG, "end getBluetoothClient tempClient=" + BTPairBeginPresenter.this.tempClient);
            } catch (IOException e) {
                AppLog.d(BTPairBeginPresenter.this.TAG, "getBluetoothClient IOException--");
                e.printStackTrace();
            }
            if (BTPairBeginPresenter.this.tempClient == null) {
                AppLog.d(BTPairBeginPresenter.this.TAG, "getBluetoothClient is null");
                GlobalInfo.isNeedGetBTClient = true;
                this.handler.obtainMessage(BTPairBeginPresenter.GET_BLUETOOTH_CLIENT_FAILED, null).sendToTarget();
                return;
            }
            GlobalInfo.iCatchBluetoothClient = BTPairBeginPresenter.this.tempClient;
            GlobalInfo.curBtDevice = (BluetoothAppDevice) BTPairBeginPresenter.this.bluetoothDeviceList.get(BTPairBeginPresenter.this.curListViewPointer);
            this.handler.obtainMessage(BTPairBeginPresenter.GET_BLUETOOTH_CLIENT_SUCCESS, null).sendToTarget();
        }
    }

    public BTPairBeginPresenter(Activity activity, Context appContext, Handler launchHandler, FragmentManager fm) {
        this.activity = activity;
        this.launchHandler = launchHandler;
        this.appContext = appContext;
        this.fragmentManager = fm;
    }

    public void setView(BTPairBeginFragmentView pairBeginFragmentView) {
        this.pairBeginFragmentView = pairBeginFragmentView;
    }

    public void loadBtList() {
        getBluetoothManager();
        this.handler.obtainMessage(0).sendToTarget();
    }

    public void searchBluetooth() {
        updateBindedDeviceToUI();
        this.handler.obtainMessage(0).sendToTarget();
    }

    public void connectBT(int position) {
        position--;
        this.curListViewPointer = position;
        AppLog.d(this.TAG, "bluetoothListView OnItemClick position=" + position);
        AppLog.d(this.TAG, "bluetoothListView OnItemClick GlobalInfo.isBLE=" + GlobalInfo.isBLE);
        closeScan();
        if (GlobalInfo.isBLE) {
            this.handler.obtainMessage(GET_BLUETOOTH_CLIENT, ((BluetoothAppDevice) this.bluetoothDeviceList.get(position)).getBluetoothAddr()).sendToTarget();
            return;
        }
        try {
            if (((BluetoothAppDevice) this.bluetoothDeviceList.get(position)).getBluetoothConnect()) {
                this.handler.obtainMessage(GET_BLUETOOTH_CLIENT, ((BluetoothAppDevice) this.bluetoothDeviceList.get(position)).getBluetoothAddr()).sendToTarget();
                return;
            }
            AppLog.d("tigertiger", "GET_BLUETOOTH_CLIENT  position== " + position);
            AppLog.d("tigertiger", "GET_BLUETOOTH_CLIENT  bluetoothDeviceList.get(position).getBluetoothAddr()== " + ((BluetoothAppDevice) this.bluetoothDeviceList.get(position)).getBluetoothAddr());
            boolean retValue = this.bluetoothManager.createBond(((BluetoothAppDevice) this.bluetoothDeviceList.get(position)).getBluetoothAddr());
            AppLog.d("tigertiger", "GET_BLUETOOTH_CLIENT  retValue== " + retValue);
            if (retValue) {
                MyProgressDialog.showProgressDialog(this.activity, "blinding...");
            } else {
                MyToast.show(this.activity, "create bounded failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyToast.show(this.activity, "create bounded Exception.");
        }
    }

    public void unregister() {
        this.bluetoothManager.unregisterBroadcastReceiver(this.userBroadcastReceiver);
        this.bluetoothManager.unregisterBroadcastReceiver(this.connectionStateReceiver);
    }

    private void closeScan() {
        if (this.scanning) {
            this.scanning = false;
            this.bluetoothAdapter.stopDiscovery();
        }
    }

    private void startScan() {
        AppLog.d(this.TAG, "startScan  isBLE=" + GlobalInfo.isBLE);
        if (GlobalInfo.isBLE) {
            this.pairBeginFragmentView.setListHeader(R.string.text_ble_devices);
        } else {
            this.pairBeginFragmentView.setListHeader(R.string.text_classic_bluetooth_devices);
        }
        if (!this.scanning && this.bluetoothAdapter != null) {
            this.scanning = true;
            if (this.bluetoothDeviceList == null) {
                this.bluetoothDeviceList = new LinkedList();
            }
            if (this.blueToothListAdapter != null) {
                this.blueToothListAdapter.notifyDataSetChanged();
            }
            try {
                this.bluetoothAdapter.startDiscovery(this.bluetoothListener, GlobalInfo.isBLE);
            } catch (IchBluetoothDeviceBusyException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBindedDeviceToUI() {
        List<ICatchBluetoothDevice> devices = this.bluetoothManager.getBondedDevices();
        if (this.bluetoothDeviceList == null) {
            this.bluetoothDeviceList = new LinkedList();
        }
        if (this.bluetoothDeviceList.size() > 0) {
            this.bluetoothDeviceList.clear();
        }
        for (ICatchBluetoothDevice tempDevice : devices) {
            AppLog.d(this.TAG, "BT Name=" + tempDevice.getName());
            this.bluetoothDeviceList.add(new BluetoothAppDevice(tempDevice.getName(), tempDevice.getAddress(), true, false));
        }
        if (this.blueToothListAdapter == null) {
            this.blueToothListAdapter = new BlueToothListAdapter(this.activity, this.bluetoothDeviceList, GlobalInfo.isBLE);
        } else {
            this.blueToothListAdapter.notifyDataSetInvalidated();
            this.blueToothListAdapter = new BlueToothListAdapter(this.activity, this.bluetoothDeviceList, GlobalInfo.isBLE);
        }
        this.pairBeginFragmentView.setBTListViewAdapter(this.blueToothListAdapter);
    }

    private void getBluetoothManager() {
        try {
            this.bluetoothManager = ICatchBluetoothManager.getBluetoothManager(this.appContext);
        } catch (IchBluetoothNotSupportedException e) {
            e.printStackTrace();
        } catch (IchBluetoothContextInvalidException e2) {
            e2.printStackTrace();
        }
        AppLog.d(this.TAG, "End getBluetoothManager() bluetoothManager=" + this.bluetoothManager);
        this.bluetoothAdapter = this.bluetoothManager.getBluetoothAdapter();
        if (!this.bluetoothManager.isBluetoothEnabled()) {
            this.bluetoothManager.enableBluetooth();
        }
        while (!this.bluetoothManager.isBluetoothEnabled()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
        AppLog.d(this.TAG, "End sleep()");
        List<String> intentFilter = new LinkedList();
        intentFilter.add(ICatchBroadcastReceiverID.BT_ACTION_BOND_STATE_CHANGED);
        this.bluetoothManager.registerBroadcastReceiver(this.userBroadcastReceiver, intentFilter);
        this.connectionStateReceiver = new BluetoothLEConnectionStateReceiver(this.handler);
        List<String> intentFilter1 = new LinkedList();
        intentFilter1.add(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_CONNECTION_STATE_CHANGED);
        this.bluetoothManager.registerBroadcastReceiver(this.connectionStateReceiver, intentFilter1);
        if (!GlobalInfo.isBLE) {
            updateBindedDeviceToUI();
        }
        AppLog.d(this.TAG, "End registerBroadcastReceiver()");
    }

    private void startSearchTimeoutTimer() {
        if (this.searchTimer != null) {
            this.searchTimer.cancel();
        }
        this.searchTimer = new Timer();
        this.searchTimer.schedule(new TimerTask() {
            public void run() {
                BTPairBeginPresenter.this.handler.obtainMessage(BTPairBeginPresenter.BLUETOOTH_SCAN_TIME_OUT).sendToTarget();
            }
        }, 4000);
    }

    private void applyUIBondState(int bondState, String mac) {
        switch (bondState) {
            case SlidingMenu.TOUCHMODE_FULLSCREEN /*1*/:
                AppLog.d(this.TAG, "applyUIBondState BT_BOND_STATE_NONE");
                MyProgressDialog.closeProgressDialog();
                MyToast.show(this.activity, "failed to bounded.");
                return;
            case GET_BLUETOOTH_CLIENT /*3*/:
                AppLog.d(this.TAG, "applyUIBondState BT_BOND_STATE_BONDED");
                MyProgressDialog.closeProgressDialog();
                MyToast.show(this.activity, "Bounded is ok");
                for (BluetoothAppDevice temp : this.bluetoothDeviceList) {
                    if (mac.equals(temp.getBluetoothAddr())) {
                        temp.setBluetoothConnect(true);
                        if (this.blueToothListAdapter != null) {
                            this.blueToothListAdapter = new BlueToothListAdapter(this.activity, this.bluetoothDeviceList, GlobalInfo.isBLE);
                        } else {
                            this.blueToothListAdapter.notifyDataSetInvalidated();
                            this.blueToothListAdapter = new BlueToothListAdapter(this.activity, this.bluetoothDeviceList, GlobalInfo.isBLE);
                        }
                        this.pairBeginFragmentView.setBTListViewAdapter(this.blueToothListAdapter);
                        this.handler.obtainMessage(GET_BLUETOOTH_CLIENT, mac).sendToTarget();
                        return;
                    }
                }
                if (this.blueToothListAdapter != null) {
                    this.blueToothListAdapter.notifyDataSetInvalidated();
                    this.blueToothListAdapter = new BlueToothListAdapter(this.activity, this.bluetoothDeviceList, GlobalInfo.isBLE);
                } else {
                    this.blueToothListAdapter = new BlueToothListAdapter(this.activity, this.bluetoothDeviceList, GlobalInfo.isBLE);
                }
                this.pairBeginFragmentView.setBTListViewAdapter(this.blueToothListAdapter);
                this.handler.obtainMessage(GET_BLUETOOTH_CLIENT, mac).sendToTarget();
                return;
            default:
                return;
        }
    }
}
