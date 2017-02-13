package com.icatch.ismartdv2016.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.icatch.ismartdv2016.BaseItems.BluetoothAppDevice;
import com.icatch.ismartdv2016.R;
import java.util.List;

public class BlueToothListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater;
    private boolean isBLE;
    private List<BluetoothAppDevice> list;

    public BlueToothListAdapter(Context context, List<BluetoothAppDevice> list, boolean isBLE) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.isBLE = isBLE;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.inflater.inflate(R.layout.bluetooth_status, null);
        String name = ((BluetoothAppDevice) this.list.get(position)).getBluetoothName();
        if (name == null) {
            name = "Unknown";
        }
        ((TextView) view.findViewById(R.id.bluetooth_name)).setText(name);
        ((TextView) view.findViewById(R.id.bluetooth_mac)).setText(((BluetoothAppDevice) this.list.get(position)).getBluetoothAddr());
        TextView bluetoothConnect = (TextView) view.findViewById(R.id.bluetooth_connect);
        if (this.isBLE) {
            bluetoothConnect.setVisibility(8);
        } else {
            bluetoothConnect.setVisibility(0);
            if (((BluetoothAppDevice) this.list.get(position)).getBluetoothConnect()) {
                bluetoothConnect.setText("Binded");
                bluetoothConnect.setTextColor(this.context.getResources().getColor(R.color.green));
            } else {
                bluetoothConnect.setText("Unbinded");
                bluetoothConnect.setTextColor(this.context.getResources().getColor(R.color.red));
            }
        }
        return view;
    }
}
