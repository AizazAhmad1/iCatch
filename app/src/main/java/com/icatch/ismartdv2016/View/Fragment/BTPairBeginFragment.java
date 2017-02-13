package com.icatch.ismartdv2016.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnFragmentInteractionListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.Presenter.BTPairBeginPresenter;
import com.icatch.ismartdv2016.R;
import com.icatch.ismartdv2016.View.Interface.BTPairBeginFragmentView;

public class BTPairBeginFragment extends Fragment implements BTPairBeginFragmentView {
    private String TAG = "BTPairBeginFragment";
    private Handler appStartHandler = GlobalInfo.getInstance().getAppStartHandler();
    private ImageButton backBtn;
    private ListView bluetoothListView;
    private Button btnSearchBLE;
    private Button btnSearchBluetooth;
    private TextView listHeader;
    private OnFragmentInteractionListener mListener;
    private View myView;
    BTPairBeginPresenter presenter;
    private ImageButton refreshBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppLog.d(this.TAG, "onCreateView myView=" + this.myView);
        if (this.myView != null) {
            return this.myView;
        }
        this.myView = inflater.inflate(R.layout.fragment_btpair_begin, container, false);
        View headerView = inflater.inflate(R.layout.bluetooth_header, null);
        this.listHeader = (TextView) headerView.findViewById(R.id.bt_header);
        this.btnSearchBluetooth = (Button) this.myView.findViewById(R.id.button_bluetooth_search);
        if (GlobalInfo.isBLE) {
            this.listHeader.setText(R.string.text_ble_devices);
            this.btnSearchBluetooth.setText(R.string.text_btpair_search_ble);
        } else {
            this.listHeader.setText(R.string.text_classic_bluetooth_devices);
            this.btnSearchBluetooth.setText(R.string.text_btpair_search_camera);
        }
        this.bluetoothListView = (ListView) this.myView.findViewById(R.id.choose_blutTooth_list);
        this.bluetoothListView.addHeaderView(headerView);
        this.bluetoothListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BTPairBeginFragment.this.presenter.connectBT(position);
            }
        });
        this.btnSearchBluetooth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BTPairBeginFragment.this.presenter.searchBluetooth();
            }
        });
        this.presenter = new BTPairBeginPresenter(getActivity(), getActivity().getApplicationContext(), this.appStartHandler, getFragmentManager());
        this.presenter.setView(this);
        this.presenter.loadBtList();
        this.backBtn = (ImageButton) this.myView.findViewById(R.id.back_btn);
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BTPairBeginFragment.this.mListener != null) {
                    BTPairBeginFragment.this.mListener.removeFragment();
                }
            }
        });
        this.refreshBtn = (ImageButton) this.myView.findViewById(R.id.refresh_btn);
        this.refreshBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BTPairBeginFragment.this.presenter.searchBluetooth();
            }
        });
        return this.myView;
    }

    public void onCreate(Bundle savedInstanceState) {
        AppLog.d(this.TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Context context) {
        AppLog.d(this.TAG, "onAttach");
        super.onAttach(context);
        try {
            this.mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        AppLog.d(this.TAG, "onDetach");
        this.mListener = null;
    }

    public void onResume() {
        AppLog.d(this.TAG, "onResume");
        if (this.mListener != null) {
            this.mListener.submitFragmentInfo(BTPairBeginFragment.class.getSimpleName(), R.string.title_fragment_btpair_begin);
        }
        GlobalInfo.isReleaseBTClient = true;
        super.onResume();
    }

    public void setBTListViewAdapter(BaseAdapter adapter) {
        if (this.bluetoothListView != null) {
            this.bluetoothListView.setAdapter(adapter);
        }
    }

    public void setListHeader(int resId) {
        this.listHeader.setText(resId);
    }

    public void onDestroy() {
        AppLog.d(this.TAG, "onDestroy");
        this.presenter.unregister();
        super.onDestroy();
    }
}
