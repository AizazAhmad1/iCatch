package com.icatchtek.bluetooth.core.client.transfer.btle;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.core.client.transfer.BluetoothTextTransfer;
import com.icatchtek.bluetooth.core.event.BluetoothCoreEventManager;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiver;
import com.icatchtek.bluetooth.customer.listener.ICatchBroadcastReceiverID;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.senab.photoview.BuildConfig;

public class ICatchCoreBluetoothLeTextTransfer implements BluetoothTextTransfer {
    private static final String icatch_bluetooth_tag = ICatchCoreBluetoothLeTextTransfer.class.getSimpleName();
    private StringBuilder __notify_reply_builder = new StringBuilder();
    private boolean __write_done = false;
    private boolean __write_relt = false;
    private BluetoothGatt bluetoothLeGatt;
    private BluetoothGattCharacteristic characteristic_read;
    private BluetoothGattCharacteristic characteristic_write;
    private BluetoothCoreEventManager eventManager;
    private ICatchBroadcastReceiver readResultReceiver = new ICatchBroadcastReceiver() {
        public void onReceive(Intent intent) {
            if (intent.getAction() == ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_DATA_AVAILABLE) {
                int eventID = intent.getIntExtra(ICatchBroadcastReceiverID.BT_LE_GATT_DATA_TYPE, 39);
                if (eventID == 37 || eventID == 39) {
                    int status = intent.getIntExtra("status", 0);
                    if (status != 0) {
                        BluetoothLogger.getInstance().logE(ICatchCoreBluetoothLeTextTransfer.icatch_bluetooth_tag, "Read status not succeed: " + status);
                        return;
                    }
                    String data = intent.getStringExtra(ICatchBroadcastReceiverID.BT_LE_GATT_DATA);
                    if (data == null) {
                        BluetoothLogger.getInstance().logE(ICatchCoreBluetoothLeTextTransfer.icatch_bluetooth_tag, "extra data is null");
                        return;
                    }
                    String receivedReply = data.toString();
                    ICatchCoreBluetoothLeTextTransfer.this.__notify_reply_builder.append(receivedReply);
                    BluetoothLogger.getInstance().logE(ICatchCoreBluetoothLeTextTransfer.icatch_bluetooth_tag, "receivedReply: " + receivedReply);
                    String __notify_reply = ICatchCoreBluetoothLeTextTransfer.this.__notify_reply_builder.toString();
                    String matchRequest = null;
                    Iterator it = ICatchCoreBluetoothLeTextTransfer.this.sendQueue.iterator();
                    while (it.hasNext()) {
                        String request = (String) it.next();
                        if (ICatchCoreBluetoothLeTextTransfer.this.__do_request_reply_matched(request, __notify_reply, true, false)) {
                            matchRequest = request;
                            break;
                        }
                    }
                    if (matchRequest != null) {
                        ICatchCoreBluetoothLeTextTransfer.this.recvQueue.offer(__notify_reply);
                    }
                    ICatchCoreBluetoothLeTextTransfer.this.__notify_reply_builder.delete(0, ICatchCoreBluetoothLeTextTransfer.this.__notify_reply_builder.length());
                    return;
                }
                BluetoothLogger.getInstance().logE(ICatchCoreBluetoothLeTextTransfer.icatch_bluetooth_tag, "Not matched readed eventID: " + eventID);
            }
        }
    };
    private LinkedList<String> recvQueue;
    private LinkedList<String> sendQueue;
    private ICatchBroadcastReceiver writeResultReceiver = new ICatchBroadcastReceiver() {
        public void onReceive(Intent intent) {
            boolean z = false;
            if (intent.getAction() == ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_DATA_AVAILABLE && intent.getIntExtra(ICatchBroadcastReceiverID.BT_LE_GATT_DATA_TYPE, 37) == 38) {
                int status = intent.getIntExtra("status", 0);
                if (status != 0) {
                    BluetoothLogger.getInstance().logE(ICatchCoreBluetoothLeTextTransfer.icatch_bluetooth_tag, "write failed with error: " + status);
                }
                ICatchCoreBluetoothLeTextTransfer iCatchCoreBluetoothLeTextTransfer = ICatchCoreBluetoothLeTextTransfer.this;
                if (status == 0) {
                    z = true;
                }
                iCatchCoreBluetoothLeTextTransfer.__write_relt = z;
                ICatchCoreBluetoothLeTextTransfer.this.__write_done = true;
            }
        }
    };

    public ICatchCoreBluetoothLeTextTransfer(BluetoothCoreEventManager eventManager, BluetoothGatt bluetoothLeGatt, BluetoothGattCharacteristic characteristic_read, BluetoothGattCharacteristic characteristic_write) throws IOException {
        this.eventManager = eventManager;
        List<String> filters = new LinkedList();
        filters.add(ICatchBroadcastReceiverID.BT_LE_GATT_ACTION_DATA_AVAILABLE);
        this.eventManager.registerBroadcastReceiver(this.readResultReceiver, filters);
        this.eventManager.registerBroadcastReceiver(this.writeResultReceiver, filters);
        this.bluetoothLeGatt = bluetoothLeGatt;
        this.characteristic_read = characteristic_read;
        this.characteristic_write = characteristic_write;
        this.sendQueue = new LinkedList();
        this.recvQueue = new LinkedList();
    }

    public void release() throws IOException {
        this.eventManager.unregisterBroadcastReceiver(this.readResultReceiver);
        this.eventManager.unregisterBroadcastReceiver(this.writeResultReceiver);
    }

    public void sendRequest(String request, String parameter, long timeout) throws IOException, IchBluetoothTimeoutException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request);
        if (parameter != null && parameter.length() > 0) {
            stringBuilder.append(parameter);
        }
        stringBuilder.append('\u0000');
        this.__write_done = false;
        this.__write_relt = false;
        this.characteristic_write.setValue(stringBuilder.toString());
        ICatchCoreBluetoothLeGattTransfer.writeCharacteristic(this.bluetoothLeGatt, this.characteristic_write);
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "the request send to peer is: " + stringBuilder.toString().toString());
        while (!this.__write_done) {
            try {
                Thread.sleep(5);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (this.__write_relt) {
            this.sendQueue.add(request);
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "the request send to peer succeed");
        }
    }

    public String receiveReply(String request, long timeout) throws IOException, IchBluetoothTimeoutException {
        String matchedReply = null;
        long query_start_time = System.currentTimeMillis();
        do {
            Iterator it = this.recvQueue.iterator();
            while (it.hasNext()) {
                String reply = (String) it.next();
                BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "request: " + request);
                BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "replay: " + reply);
                if (__do_request_reply_matched(request, reply, false, true)) {
                    matchedReply = reply;
                    break;
                }
            }
            if (matchedReply != null) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (System.currentTimeMillis() < query_start_time + timeout);
        if (matchedReply != null) {
            return matchedReply;
        }
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "throw timeout exception");
        throw new IchBluetoothTimeoutException("receive reply for [" + request + "] failed in " + timeout + " millionseconds");
    }

    private void __trigger_read_from_peer() {
        ICatchCoreBluetoothLeGattTransfer.readCharacteristic(this.bluetoothLeGatt, this.characteristic_read);
    }

    private boolean __do_request_reply_matched(String request, String reply, boolean removeRequest, boolean removeReply) {
        JSONObject requestObject = null;
        JSONObject replyObject = null;
        try {
            requestObject = new JSONObject(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            replyObject = new JSONObject(reply);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        if (requestObject == null || replyObject == null) {
            return false;
        }
        Iterator iterator1 = requestObject.keys();
        Iterator iterator2 = replyObject.keys();
        String iterator2String = BuildConfig.FLAVOR;
        while (iterator2.hasNext()) {
            iterator2String = iterator2String + iterator2.next().toString();
        }
        if (iterator1.hasNext() && iterator2.hasNext() && !iterator2String.contains(iterator1.next().toString())) {
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "request string: " + request);
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "no match for reply: " + reply);
            return false;
        }
        if (removeRequest) {
            this.sendQueue.remove(request);
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "remove request [" + request + "] from sendQueue");
        }
        if (removeReply) {
            this.recvQueue.remove(reply);
            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "remove reply [" + reply + "] from recvQueue");
        }
        return true;
    }
}
