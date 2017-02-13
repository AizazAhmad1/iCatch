package com.icatchtek.bluetooth.core.ctrl;

import com.icatchtek.bluetooth.core.ICatchCoreBluetoothCommand;
import com.icatchtek.bluetooth.core.base.BluetoothLogger;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothClient;
import com.icatchtek.bluetooth.customer.client.ICatchBluetoothSystemControl;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import com.icatchtek.bluetooth.customer.type.ICatchBtInfomation;
import com.icatchtek.bluetooth.customer.type.ICatchWifiEncType;
import com.icatchtek.bluetooth.customer.type.ICatchWifiInformation;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class ICatchCoreBluetoothSystemControl implements ICatchBluetoothSystemControl {
    private ICatchBluetoothClient bluetoothClient;

    public ICatchCoreBluetoothSystemControl(ICatchBluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    public ICatchWifiInformation getWifiInformation() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        ICatchWifiInformation iCatchWifiInformation = null;
        BluetoothLogger.getInstance().logE("new jar", "new jar=================");
        String result = ICatchCoreBluetoothCommand.executeRequestWithResponseValue(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_WIFI_REQ_INFORM_ESSID_PWD_IP, null);
        if (result == null) {
            BluetoothLogger.getInstance().logE("getWifiInfo", "get wifi info error, 111");
        } else {
            BluetoothLogger.getInstance().logE("getWifiInfo", "wifi info string: " + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                iCatchWifiInformation = new ICatchWifiInformation();
                try {
                    iCatchWifiInformation.setWifiSSID(jsonObject.getString("essid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    iCatchWifiInformation.setWifiPassword(jsonObject.getString("pwd"));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                iCatchWifiInformation.setWifiEncType(ICatchWifiEncType.ICATCH_WIFI_AP_ENC_TYPE_WPA);
                BluetoothLogger.getInstance().logE("getWifiInfo", "get wifi info succeed, essid: " + iCatchWifiInformation.getWifiSSID() + "pwd: " + iCatchWifiInformation.getWifiPassword());
            } catch (JSONException e22) {
                e22.printStackTrace();
                BluetoothLogger.getInstance().logE("JSONObject", "JSONObject error");
            }
        }
        return iCatchWifiInformation;
    }

    public ICatchBtInfomation getBtInformation() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        ICatchBtInfomation iCatchBtInfomation = null;
        String result = ICatchCoreBluetoothCommand.executeRequestWithResponseValue(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_BT_REQ_INFORM_NAME_PWD, null);
        if (result != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                iCatchBtInfomation = new ICatchBtInfomation();
                try {
                    iCatchBtInfomation.setBtSSID(jsonObject.getString("name"));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                try {
                    iCatchBtInfomation.setWifiPassword(jsonObject.getString("pwd"));
                } catch (JSONException e22) {
                    e22.printStackTrace();
                }
            }
        }
        return iCatchBtInfomation;
    }

    public boolean setWifiInformation(ICatchWifiInformation wifiInfo) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        String essid = wifiInfo.getWifiSSID();
        String pwd = wifiInfo.getWifiPassword();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "wifi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("action", "set");
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        try {
            jsonObject.put("essid", essid);
        } catch (JSONException e22) {
            e22.printStackTrace();
        }
        try {
            jsonObject.put("pwd", pwd);
        } catch (JSONException e222) {
            e222.printStackTrace();
        }
        String result = ICatchCoreBluetoothCommand.executeRequestWithResponseValue(this.bluetoothClient, jsonObject.toString(), null);
        if (result == null) {
            return false;
        }
        JSONObject resultObject = null;
        try {
            resultObject = new JSONObject(result);
        } catch (JSONException e2222) {
            e2222.printStackTrace();
        }
        if (resultObject == null) {
            return false;
        }
        int resultValue = -1;
        try {
            resultValue = resultObject.getInt("err");
        } catch (JSONException e22222) {
            e22222.printStackTrace();
        }
        if (resultValue == 0) {
            return true;
        }
        return false;
    }

    public boolean setBtInformation(ICatchBtInfomation btInfo) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        String essid = btInfo.getBtSSID();
        String pwd = btInfo.getBtPassword();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "bt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("action", "set");
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        try {
            jsonObject.put("essid", essid);
        } catch (JSONException e22) {
            e22.printStackTrace();
        }
        try {
            jsonObject.put("pwd", pwd);
        } catch (JSONException e222) {
            e222.printStackTrace();
        }
        String result = ICatchCoreBluetoothCommand.executeRequestWithResponseValue(this.bluetoothClient, jsonObject.toString(), null);
        if (result == null) {
            return false;
        }
        JSONObject resultObject = null;
        try {
            resultObject = new JSONObject(result);
        } catch (JSONException e2222) {
            e2222.printStackTrace();
        }
        if (resultObject == null) {
            return false;
        }
        int resultValue = -1;
        try {
            resultValue = resultObject.getInt("err");
        } catch (JSONException e22222) {
            e22222.printStackTrace();
        }
        if (resultValue == 0) {
            return true;
        }
        return false;
    }

    public boolean enableWifi() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_WIFI_REQ_ENABLE, null);
    }

    public boolean disableWifi() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_WIFI_REQ_DISABLE, null);
    }

    public boolean powerOff() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_SYSTEM_POWER_DOWN, null);
    }

    public boolean hibernation() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException {
        return ICatchCoreBluetoothCommand.executeRequest(this.bluetoothClient, ICatchCoreBluetoothCommand.BT_CMD_SYSTEM_POWER_HIBER, null);
    }
}
