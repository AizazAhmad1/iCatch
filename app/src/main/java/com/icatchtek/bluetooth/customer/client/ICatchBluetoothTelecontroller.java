package com.icatchtek.bluetooth.customer.client;

import com.icatchtek.bluetooth.customer.exception.IchBluetoothDeviceBusyException;
import com.icatchtek.bluetooth.customer.exception.IchBluetoothTimeoutException;
import java.io.IOException;

public interface ICatchBluetoothTelecontroller {
    @Deprecated
    boolean btKeyDel() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean capture() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean captureHalfPress() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean changeMode() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean execute() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean moveDown() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean moveLeft() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean moveRight() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean moveUp() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean openMenu() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean zoomIn() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;

    boolean zoomOut() throws IOException, IchBluetoothTimeoutException, IchBluetoothDeviceBusyException;
}
