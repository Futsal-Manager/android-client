package com.futsal.manager.EmbeddedCommunicationModule;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class BluetoothDeviceItemModel {

    public String deviceName, deviceMacAddress;
    public boolean isAlreadyPaired;

    public BluetoothDeviceItemModel(String deviceName, String deviceMacAddress, boolean isAlreadyPaired) {
        this.deviceName = deviceName;
        this.deviceMacAddress = deviceMacAddress;
        this.isAlreadyPaired = isAlreadyPaired;
    }

    public String GetDeviceName() {
        return deviceName;
    }

    public String GetDeviceMacAddress() {
        return deviceMacAddress;
    }

    public boolean GetIsAlreadyPaired() {
        return isAlreadyPaired;
    }
}
