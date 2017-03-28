package com.futsal.manager.MakeVideoModule;

import com.futsal.manager.BluetoothModule.BluetoothCommunication;
import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

/**
 * Created by stories2 on 2017. 3. 28..
 */

public class BluetoothDeviceControlProcesser extends Thread {

    BluetoothCommunication bluetoothCommunication;
    Thread testControlThread;
    boolean orderSwap, threadControl;

    public BluetoothDeviceControlProcesser(BluetoothCommunication bluetoothCommunication) {
        bluetoothCommunication.ConnectToTargetBluetoothDevice(bluetoothCommunication.GetBluetoothAdapter(), bluetoothCommunication.GetSelectedDeviceAddress());
        this.bluetoothCommunication = bluetoothCommunication;
        orderSwap = false;
        threadControl = true;
        testControlThread = new Thread(this);
        testControlThread.start();
        LogManager.PrintLog("BluetoothDeviceControlProcesser", "BluetoothDeviceControlProcesser", "Ready to Communicate with Embedded System", DefineManager.LOG_LEVEL_INFO);
    }

    @Override
    public void run() {
        super.run();
        while(threadControl) {
            try {
                orderSwap = !orderSwap;
                if(orderSwap) {
                    SendMove();
                }
                else {
                    SendStop();
                }
                Thread.sleep(1000);
            }
            catch (Exception err) {
                LogManager.PrintLog("BluetoothDeviceControlProcesser", "run", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
        }
    }

    void SendMove() {
        try {
            bluetoothCommunication.SetOrder("A");
            bluetoothCommunication.TryToCommunication(1);
            LogManager.PrintLog("BluetoothDeviceControlProcesser", "SendMove", "Move Order Sended", DefineManager.LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("BluetoothDeviceControlProcesser", "SendMove", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    void SendStop() {
        try {
            bluetoothCommunication.SetOrder("B");
            bluetoothCommunication.TryToCommunication(1);
            LogManager.PrintLog("BluetoothDeviceControlProcesser", "SendStop", "Stop Order Sended", DefineManager.LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("BluetoothDeviceControlProcesser", "SendStop", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    public void StopProcess() {
        threadControl = false;
        testControlThread.interrupt();
    }
}
