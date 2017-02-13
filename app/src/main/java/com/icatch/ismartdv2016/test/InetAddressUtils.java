package com.icatch.ismartdv2016.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtils {
    public static boolean isReachable(String ipAddress) {
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Sending Ping Request to " + ipAddress);
        boolean value = false;
        try {
            value = inet.isReachable(5000);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        System.out.println(value ? "Host is reachable" : "Host is NOT reachable");
        return value;
    }
}
