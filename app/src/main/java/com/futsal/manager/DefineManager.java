package com.futsal.manager;

/**
 * Created by stories2 on 2017. 3. 24..
 */

public class DefineManager {
    public static final int LOG_LEVEL_VERBOSE = 0, LOG_LEVEL_DEBUG = 1, LOG_LEVEL_INFO = 2, LOG_LEVEL_WARN = 3, LOG_LEVEL_ERROR = 4,

                            MAKE_NEW_VIDEO_ITEM = 0, SHOW_VIDEO_ITEM = 1,

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
                            EACH_BLUR_BLOCK_SIZE = 11, NOT_AVAILABLE = -1, MINIMUM_CIRCLE_RADIUS = 10;
    public static final String APP_NAME = "FutsalManager", TEST_ACCOUNT = "yoohoogun116@naver.com", TEST_ACCOUNT_PASSWORD = "rlagusdn123",
                        SERVER_DOMAIN_NAME = "ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com";
}
