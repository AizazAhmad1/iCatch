package com.icatchtek.bluetooth.core.client.transfer.bt;

import android.bluetooth.BluetoothSocket;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.core.client.transfer.BluetoothTextTransfer;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.senab.photoview.BuildConfig;

public class ICatchCoreBluetoothTextTransfer implements BluetoothTextTransfer, Runnable {
    private static final String icatch_bluetooth_tag = ICatchCoreBluetoothTextTransfer.class.getSimpleName();
    private BluetoothBinaryTransfer binaryTransfer;
    private LinkedList<String> recvQueue = new LinkedList();
    private boolean recvRunning = true;
    private Thread recvThread = new Thread(this);
    private int replyCount = 0;
    private LinkedList<String> sendQueue = new LinkedList();

    public ICatchCoreBluetoothTextTransfer(BluetoothSocket bluetoothSocket) throws IOException {
        this.binaryTransfer = new BluetoothBinaryTransfer(bluetoothSocket);
        this.recvThread.start();
    }

    public void release() throws IOException {
        if (this.recvRunning && this.recvThread != null && this.recvThread.isAlive()) {
            this.recvRunning = false;
            try {
                this.recvThread.interrupt();
                this.recvThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.binaryTransfer != null) {
            this.binaryTransfer.release();
        }
    }

    public void sendRequest(String request, String parameter, long timeout) throws IOException, IchBluetoothTimeoutException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request);
        if (parameter != null && parameter.length() > 0) {
            stringBuilder.append(parameter);
        }
        stringBuilder.append('\u0000');
        byte[] rawdata = stringBuilder.toString().getBytes();
        this.binaryTransfer.writeData(rawdata, rawdata.length);
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "the request send to peer is: " + stringBuilder.toString().toString());
        this.sendQueue.add(request);
        this.replyCount++;
    }

    public String receiveReply(String request, long timeout) throws IOException, IchBluetoothTimeoutException {
        String matchedReply = null;
        long query_start_time = System.currentTimeMillis();
        do {
            Iterator it = this.recvQueue.iterator();
            while (it.hasNext()) {
                String reply = (String) it.next();
                if (__do_request_reply_matched(request, reply, false, true)) {
                    matchedReply = reply;
                    break;
                }
            }
            if (matchedReply != null) {
                break;
            } else if (this.recvThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IOException("receive thread not exists, the socket may be closed.");
            }
        } while (System.currentTimeMillis() < query_start_time + timeout);
        if (matchedReply != null) {
            return matchedReply;
        }
        throw new IchBluetoothTimeoutException("receive reply for [" + request + "] failed in " + timeout + " millionseconds");
    }

    private String __read_line_from_peer(int timeout) throws IOException, IchBluetoothTimeoutException {
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "recv reply.");
        byte[] rawdata = new byte[2048];
        String receivedReply = new String(rawdata, 0, this.binaryTransfer.readData(rawdata, rawdata.length, timeout));
        BluetoothLogger.getInstance().logI(icatch_bluetooth_tag, "received reply:" + receivedReply.trim());
        return receivedReply.trim();
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

    public void run() {
        do {
            try {
                Thread.sleep(50);
                if (this.replyCount > 0) {
                    try {
                        String receivedReply = __read_line_from_peer(500);
                        if (receivedReply != null) {
                            if (receivedReply.startsWith("eof")) {
                                break;
                            }
                            String matchRequest = null;
                            Iterator it = this.sendQueue.iterator();
                            while (it.hasNext()) {
                                String request = (String) it.next();
                                if (__do_request_reply_matched(request, receivedReply, true, false)) {
                                    matchRequest = request;
                                    break;
                                }
                            }
                            if (matchRequest != null) {
                                this.recvQueue.offer(receivedReply);
                                this.replyCount--;
                            }
                        } else {
                            BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "receive no message");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IchBluetoothTimeoutException e2) {
                        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, e2.getMessage());
                    }
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } while (this.recvRunning);
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, "receive thread quit, " + this.recvRunning);
        this.recvRunning = false;
    }
}
