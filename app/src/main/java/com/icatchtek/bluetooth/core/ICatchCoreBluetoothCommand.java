package com.icatchtek.bluetooth.core;

import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class ICatchCoreBluetoothCommand {
    public static final String BT_CMD_BT_REQ_INFORM = "{\"mode\": \"bt\", \"action\": \"info\"}";
    public static final String BT_CMD_BT_REQ_INFORM_NAME_PWD = "{\"mode\": \"bt\", \"action\": \"info\", \"name\": \"\", \"pwd\": \"\"}";
    public static final String BT_CMD_BT_SET_INFORM = "{\"mode\": \"bt\", \"action\": \"set\"}";
    public static final String BT_CMD_EVENT_KEY_CAPTURE = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"s2\"}";
    public static final String BT_CMD_EVENT_KEY_CAPTURE_HALF_PRESS = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"s1\"}";
    public static final String BT_CMD_EVENT_KEY_DEL = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"del\"}";
    public static final String BT_CMD_EVENT_KEY_DOWN = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"down\"}";
    public static final String BT_CMD_EVENT_KEY_LEFT = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"left\"}";
    public static final String BT_CMD_EVENT_KEY_MENU = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"menu\"}";
    public static final String BT_CMD_EVENT_KEY_MODE = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"mode\"}";
    public static final String BT_CMD_EVENT_KEY_RIGHT = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"right\"}";
    public static final String BT_CMD_EVENT_KEY_SET = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"set\"}";
    public static final String BT_CMD_EVENT_KEY_TELE = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"tele\"}";
    public static final String BT_CMD_EVENT_KEY_UP = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"up\"}";
    public static final String BT_CMD_EVENT_KEY_WIDE = "{\"mode\": \"event\", \"action\": \"key\", \"type\": \"wide\"}";
    public static final String BT_CMD_REPLY_ERR = "err=1";
    public static final String BT_CMD_REPLY_NOERR = "err=0";
    public static final String BT_CMD_SYSTEM_POWER_DOWN = "{\"mode\": \"system\", \"action\": \"power\", \"type\": \"down\"}";
    public static final String BT_CMD_SYSTEM_POWER_HIBER = "{\"mode\": \"system\", \"action\": \"power\", \"type\": \"hiber\"}";
    public static final char BT_CMD_TAIL = '\u0000';
    public static final String BT_CMD_WIFI_REQ_DISABLE = "{\"mode\": \"wifi\", \"action\": \"disable\"}";
    public static final String BT_CMD_WIFI_REQ_ENABLE = "{\"mode\": \"wifi\", \"action\": \"enable\", \"type\": \"ap\"}";
    public static final String BT_CMD_WIFI_REQ_INFORM = "{\"mode\": \"wifi\", \"action\": \"info\"}";
    public static final String BT_CMD_WIFI_REQ_INFORM_ESSID_PWD_IP = "{\"mode\": \"wifi\", \"action\": \"info\", \"essid\": \"\", \"pwd\": \"\", \"ipaddr\": \"\"}";
    public static final String BT_CMD_WIFI_SET_INFORM = "{\"mode\": \"wifi\", \"action\": \"set\"}";
    private static final String icatch_bluetooth_tag = ICatchCoreBluetoothCommand.class.getSimpleName();

    public static boolean executeRequest(ICatchBluetoothClient bluetoothClient, String requestID, String parameter) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return sendRequestToRemote(bluetoothClient, requestID, parameter) != null;
    }

    public static String executeRequestWithResponseValue(ICatchBluetoothClient bluetoothClient, String requestID, String parameter) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return sendRequestToRemote(bluetoothClient, requestID, parameter);
    }

    private static String sendRequestToRemote(ICatchBluetoothClient bluetoothClient, String requestID, String parameter) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        bluetoothClient.sendRequest(requestID, parameter, 60000);
        String reply = bluetoothClient.receiveReply(requestID, 60000);
        if (reply == null) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(reply);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            return null;
        }
        int retValue = -1;
        try {
            retValue = jsonObject.getInt("err");
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        if (retValue == 0) {
            return reply;
        }
        BluetoothLogger.getInstance().logE(icatch_bluetooth_tag, BT_CMD_REPLY_ERR);
        return null;
    }
}
