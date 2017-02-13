package com.icatchtek.bluetooth.customer.client;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import com.icatchtek.bluetooth.customer.type.ICatchBtInfomation;
import com.icatchtek.bluetooth.customer.type.ICatchWifiInformation;
import java.io.IOException;

public interface ICatchBluetoothSystemControl {
    boolean enableWifi() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    ICatchBtInfomation getBtInformation() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    ICatchWifiInformation getWifiInformation() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean hibernation() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean powerOff() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean setBtInformation(ICatchBtInfomation iCatchBtInfomation) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean setWifiInformation(ICatchWifiInformation iCatchWifiInformation) throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;
}
