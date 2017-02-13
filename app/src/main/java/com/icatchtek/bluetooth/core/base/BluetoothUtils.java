package com.icatchtek.bluetooth.core.base;

import android.text.TextUtils;
import com.icatch.ismartdv2016.Message.AppMessage;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uk.co.senab.photoview.BuildConfig;

public class BluetoothUtils {
    private static HashMap<Integer, String> charPermissions = new HashMap();
    private static HashMap<Integer, String> charProperties = new HashMap();
    private static HashMap<Integer, String> descPermissions = new HashMap();
    private static HashMap<Integer, String> serviceTypes = new HashMap();

    static {
        serviceTypes.put(Integer.valueOf(0), "PRIMARY");
        serviceTypes.put(Integer.valueOf(1), "SECONDARY");
        charPermissions.put(Integer.valueOf(0), "UNKNOW");
        charPermissions.put(Integer.valueOf(1), "READ");
        charPermissions.put(Integer.valueOf(2), "READ_ENCRYPTED");
        charPermissions.put(Integer.valueOf(4), "READ_ENCRYPTED_MITM");
        charPermissions.put(Integer.valueOf(16), "WRITE");
        charPermissions.put(Integer.valueOf(32), "WRITE_ENCRYPTED");
        charPermissions.put(Integer.valueOf(64), "WRITE_ENCRYPTED_MITM");
        charPermissions.put(Integer.valueOf(128), "WRITE_SIGNED");
        charPermissions.put(Integer.valueOf(AppMessage.LOCAL_ACTIVITY), "WRITE_SIGNED_MITM");
        charProperties.put(Integer.valueOf(1), "BROADCAST");
        charProperties.put(Integer.valueOf(128), "EXTENDED_PROPS");
        charProperties.put(Integer.valueOf(32), "INDICATE");
        charProperties.put(Integer.valueOf(16), "NOTIFY");
        charProperties.put(Integer.valueOf(2), "READ");
        charProperties.put(Integer.valueOf(64), "SIGNED_WRITE");
        charProperties.put(Integer.valueOf(8), "WRITE");
        charProperties.put(Integer.valueOf(4), "WRITE_NO_RESPONSE");
        descPermissions.put(Integer.valueOf(0), "UNKNOW");
        descPermissions.put(Integer.valueOf(1), "READ");
        descPermissions.put(Integer.valueOf(2), "READ_ENCRYPTED");
        descPermissions.put(Integer.valueOf(4), "READ_ENCRYPTED_MITM");
        descPermissions.put(Integer.valueOf(16), "WRITE");
        descPermissions.put(Integer.valueOf(32), "WRITE_ENCRYPTED");
        descPermissions.put(Integer.valueOf(64), "WRITE_ENCRYPTED_MITM");
        descPermissions.put(Integer.valueOf(128), "WRITE_SIGNED");
        descPermissions.put(Integer.valueOf(AppMessage.LOCAL_ACTIVITY), "WRITE_SIGNED_MITM");
    }

    public static String getServiceType(int type) {
        return (String) serviceTypes.get(Integer.valueOf(type));
    }

    public static String getCharPermission(int permission) {
        return getHashMapValue(charPermissions, permission);
    }

    public static String getCharPropertie(int property) {
        return getHashMapValue(charProperties, property);
    }

    public static String getDescPermission(int property) {
        return getHashMapValue(descPermissions, property);
    }

    private static String getHashMapValue(HashMap<Integer, String> hashMap, int number) {
        String result = (String) hashMap.get(Integer.valueOf(number));
        if (TextUtils.isEmpty(result)) {
            List<Integer> numbers = getElement(number);
            result = BuildConfig.FLAVOR;
            for (int i = 0; i < numbers.size(); i++) {
                result = result + ((String) hashMap.get(numbers.get(i))) + "|";
            }
        }
        return result;
    }

    private static List<Integer> getElement(int number) {
        List<Integer> result = new ArrayList();
        for (int i = 0; i < 32; i++) {
            int b = 1 << i;
            if ((number & b) > 0) {
                result.add(Integer.valueOf(b));
            }
        }
        return result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder(BuildConfig.FLAVOR);
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & ICatchLightFrequency.ICH_LIGHT_FREQUENCY_UNDEFINED);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String bytesToString(byte[] src) {
        return new String(src);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
