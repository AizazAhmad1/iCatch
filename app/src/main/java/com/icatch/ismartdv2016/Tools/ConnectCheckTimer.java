package com.icatch.ismartdv2016.Tools;

import android.os.Handler;
import com.icatch.ismartdv2016.test.InetAddressUtils;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectCheckTimer {
    private static final long DELAY = 3000;
    public static final int MESSAGE_CONNECT_DISCONNECTED = 5633;
    private static final long PERIOD = 1000;
    private static Timer connectChecktimer;

    public static void startCheck(final Handler handler) {
        if (connectChecktimer != null) {
            connectChecktimer.cancel();
        }
        connectChecktimer = new Timer(true);
        connectChecktimer.schedule(new TimerTask() {
            public void run() {
                if (!InetAddressUtils.isReachable("192.168.1.1")) {
                    ConnectCheckTimer.stopCheck();
                    handler.post(new Runnable() {
                        public void run() {
                            handler.obtainMessage(ConnectCheckTimer.MESSAGE_CONNECT_DISCONNECTED).sendToTarget();
                        }
                    });
                }
            }
        }, DELAY, PERIOD);
    }

    public static void stopCheck() {
        if (connectChecktimer != null) {
            connectChecktimer.cancel();
        }
    }
}
