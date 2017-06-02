package com.futsal.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.futsal.manager.BluetoothModule.BluetoothCommunication;
import com.futsal.manager.EmbeddedCommunicationModule.BluetoothDeviceItemModel;

import java.util.List;
import java.util.UUID;

/**
 * Created by stories2 on 2017. 3. 24..
 */

public class DefineManager {
    public static final int LOG_LEVEL_VERBOSE = 0, LOG_LEVEL_DEBUG = 1, LOG_LEVEL_INFO = 2, LOG_LEVEL_WARN = 3, LOG_LEVEL_ERROR = 4,

                            MAKE_NEW_VIDEO_ITEM = 0, SHOW_VIDEO_ITEM = 1, DEV_OPTION_ITEM = 2, LIBRARY_ITEM = 3,

                            CALLED_BY_FUTSAL_MAIN_ACTIVITY = 0, CALLED_BY_SERVER_SAVED_LIST_ACTIVITY = 1,

                            WAIT_FOR_LOGIN = 0, WAIT_FOR_GET_FILE_LIST = 1, WAIT_FOR_UPLOAD_VIDEO = 2,

                            /*
                            PermissionManager
                            Android API Version
                            Requested Permission Order Number
                            */
                            ANDROID_VERSION_OF_MARSHMALLOW = 23, PERMISSION_REQUESTED_ORDER = 1,

                            /*
                            OpenCVModuleProcesser
                            Blur Process Parameter
                            */
                            EACH_BLUR_BLOCK_SIZE = 11, NOT_AVAILABLE = -1, MINIMUM_CIRCLE_RADIUS = 10,

                            /*

                             */
                            CAMERA_WIDTH_RESOLUTION = 640, CAMERA_HEIGHT_RESOLUTION = 480,
                            CAMERA_RECORD_WIDTH_RESOLUTION = 1280, CAMERA_RECORD_HEIGHT_RESOLUTION = 720,

                            SCREEN_RESOLUTION_320_X_240 = 0, SCREEN_RESOLUTION_640_X_480 = 1, SCREEN_RESOLUTION_1280_X_720 = 2,
                            SCREEN_RESOLUTION_1920_X_1080 = 3,

                            SCREEN_WIDTH = 0, SCREEN_HEIGHT = 1,

                            ENABLE_BLUETOOTH_MODULE_USER_ACCESS_ACCEPT = 1, SEARCH_EMBEDDED_SYSTEM = 2,

                            VIDEO_RECORD_TIME_DATA = 0;
    public static final String APP_NAME = "FutsalManager", TEST_ACCOUNT = "yoohoogun116@naver.com", TEST_ACCOUNT_PASSWORD = "rlagusdn123",
                        SERVER_DOMAIN_NAME = "ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com",

                        AVAILABLE_BLUETOOTH_NAME = "HC-06";

    public static final boolean NOT_WORKING = false;

    public static boolean BLUETOOTH_CONNECTION_FAILURE = false, BLUR_MODE_OPTION = false;

    public static int MINIMUM_DETECT_COLOR_H = 0, MINIMUM_DETECT_COLOR_S = 150, MINIMUM_DETECT_COLOR_V = 150,
                    MAXIMUM_DETECT_COLOR_H = 25, MAXIMUM_DETECT_COLOR_S = 255, MAXIMUM_DETECT_COLOR_V = 255,

                    VIDEO_RECORD_BIT_RATE = 6000000,

                    PICTURE_RESOLUTION_SETTING = 1, RECORD_RESOLUTION_SETTING = 2,

                    BLUETOOTH_SEND_SPEED = 500;

    public static int[][] AVAILABLE_SCREEN_RESOLUTION_LIST = new int[][]{
            {320, 240},
            {640, 480},
            {1280, 720},
            {1920, 1080}
    };

    public static BluetoothCommunication BLUETOOTH_COMMUNICATION_TEMP;

    public static BluetoothAdapter EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER = null;

    public static List<BluetoothDeviceItemModel> EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST = null;

    public static BluetoothDevice EMBEDDED_SYSTEM_DEVICE = null;

    public static BluetoothSocket EMBEDDED_SYSTEM_DEVICE_SOCKET = null;

    public static UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
}
