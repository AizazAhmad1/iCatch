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
import android.widget.ImageButton;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Listener.OnFragmentInteractionListener;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.R;

public class AddNewCamFragment extends Fragment {
    private Button BTPairBtn;
    private String TAG = "AddNewCamFragment";
    private Handler appStartHandler = GlobalInfo.getInstance().getAppStartHandler();
    private ImageButton backBtn;
    private Button connectCamBtn;
    private OnFragmentInteractionListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppLog.d(this.TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_add_new_cam, container, false);
        this.BTPairBtn = (Button) view.findViewById(R.id.bt_pair);
        this.connectCamBtn = (Button) view.findViewById(R.id.bt_connect_camera);
        this.connectCamBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AddNewCamFragment.this.appStartHandler.obtainMessage(5).sendToTarget();
            }
        });
        this.BTPairBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BTPairBeginFragment btPairBegin = new BTPairBeginFragment();
                FragmentTransaction ft = AddNewCamFragment.this.getFragmentManager().beginTransaction();
                ft.replace(R.id.launch_setting_frame, btPairBegin);
                ft.addToBackStack("BTPairBeginFragment");
                ft.commit();
            }
        });
        this.backBtn = (ImageButton) view.findViewById(R.id.back_btn);
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AddNewCamFragment.this.mListener != null) {
                    AddNewCamFragment.this.mListener.removeFragment();
                }
            }
        });
        return view;
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
        AppLog.d(this.TAG, "onDetach");
        super.onDetach();
        this.mListener = null;
    }

    public void onResume() {
        AppLog.d(this.TAG, "onResume");
        super.onResume();
        if (this.mListener != null) {
            this.mListener.submitFragmentInfo(AddNewCamFragment.class.getSimpleName(), R.string.title_activity_add_new_cam);
        }
    }

    public void onStart() {
        AppLog.d(this.TAG, "onStart");
        super.onStart();
    }

    public void onStop() {
        AppLog.d(this.TAG, "onStop");
        super.onStop();
    }

    public void onDestroy() {
        AppLog.d(this.TAG, "onDestroy");
        super.onDestroy();
    }
}
