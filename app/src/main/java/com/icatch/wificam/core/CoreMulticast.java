package com.icatch.wificam.core;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;
import com.icatch.ismartdv2016.Message.AppMessage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import uk.co.senab.photoview.IPhotoView;

public class CoreMulticast {
    private static final String MULTI_CAST_ADDR = "234.168.168.168";
    private static final int MULTI_CAST_PORT = 5002;
    private MulticastLock multicastLock;
    private MulticastSocket multicastSocket;

    public boolean prepare(Context context) {
        this.multicastLock = ((WifiManager) context.getSystemService("wifi")).createMulticastLock("multicast.test");
        this.multicastLock.acquire();
        try {
            this.multicastSocket = new MulticastSocket(MULTI_CAST_PORT);
            try {
                try {
                    this.multicastSocket.joinGroup(InetAddress.getByName(MULTI_CAST_ADDR));
                    try {
                        this.multicastSocket.setSoTimeout(IPhotoView.DEFAULT_ZOOM_DURATION);
                        return true;
                    } catch (SocketException e) {
                        e.printStackTrace();
                        release();
                        return false;
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    release();
                    return false;
                }
            } catch (UnknownHostException e3) {
                e3.printStackTrace();
                release();
                return false;
            }
        } catch (IOException e22) {
            e22.printStackTrace();
            return false;
        }
    }

    public void release() {
        if (this.multicastSocket != null) {
            this.multicastSocket.close();
            this.multicastSocket = null;
        }
        if (this.multicastLock != null) {
            this.multicastLock.release();
            this.multicastLock = null;
        }
    }

    public String receive() {
        byte[] buffer = new byte[AppMessage.LOCAL_ACTIVITY];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            this.multicastSocket.receive(packet);
            String address = packet.getAddress().toString();
            String content = new String(packet.getData(), packet.getOffset(), packet.getLength());
            Log.i("__multi_cast_recv__", "address: " + address);
            Log.i("__multi_cast_recv__", "content: " + content);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
