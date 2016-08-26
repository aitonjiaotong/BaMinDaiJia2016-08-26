package com.aiton.bamin.bamindaijiadrier.constant;

/**
 * Created by zjb on 2016/6/12.
 */
public class Constant {
    //    http://192.168.1.112:8080/designated/app/customer/sendMessage?phone=15871105320&code=3666
//    public static final String HOST = "http://120.24.46.15:8080/aiton-designated-driver-app-webapp/designated/app/driver";
    public static final String HOST = "http://192.168.1.188:8080/driver/designated/app/driver";
    public static final int ABLEVERSION = 0 ;//可用版本号
//    private static String CHANNELID = "";
//
//    public static String getCHANNELID() {
//        return CHANNELID;
//    }
//
//    public static void setCHANNELID(String CHANNELID) {
//        Constant.CHANNELID = CHANNELID;
//    }

    public static class Url {
        public static final String UP_GRADE = "http://bmcx.oss-cn-shanghai.aliyuncs.com/bmzc/upgradea_daijiasiji.txt";
//        public static final String GET_SMS = HOST + "/sendMessage";
        public static final String GET_SMS = "http://120.55.166.203:8020/aiton-tickets-app-webapp/public/sendmessage";
        public static final String LOGIN = HOST + "/addDriver";//传入参数：name(名称),phone(电话),deviceId(手机ID)
        public static final String CHECK_LIVE = HOST + "/live/cheklive";
        public static final String LOADLOGINID = HOST+"/loadloginId";
        public static final String ACECPT_ORDER = HOST+"/order/driverGetOrder";
        public static final String DRIVER_GET_ORDER_IS_ING = HOST+"/order/getDriverOrderIsIng";
        public static final String DRIVER_FIND_NOT_FINISH_ORDER = HOST+"/order/driverFindNotFinishOrder";
        public static final String ORDER_START = HOST+"/order/orderStart";
        public static final String ORDER_COMPLETE = HOST+"/order/orderComplete";
        public static final String DRIVER_CHANGE_MILEAGE = HOST+"/order/driverChangeMileage";
        public static final String DRIVER_CONFIRM_ORDER = HOST+"/order/driverConfirmOrder";
        public static final String CHANGE_END = HOST+"/order/orderChange";//更改目的地
        public static final String CUSTOMERIMAGE = HOST+"/image/driverImage";
        public static final String USER_INFO = HOST + "/updataPersonal";//司机信息
        public static final String ALL_ORDER = HOST + "/order/driverOldOrder";//所有订单



    }

    public static class PERMISSION {
        public static final int ACCESS_COARSE_LOCATION = 0;
        public static final int ACCESS_FINE_LOCATION = 1;
        public static final int WRITE_EXTERNAL_STORAGE = 2;
        public static final int READ_EXTERNAL_STORAGE = 3;
        public static final int READ_PHONE_STATE = 4;
        public static final int PERMISSION_READ_SMS = 5;
        public static final int RECEIVE_BOOT_COMPLETED = 6;
        public static final int RECEIVE_WRITE_SETTINGS = 7;
        public static final int RECEIVE_VIBRATE = 8;
        public static final int RECEIVE_DISABLE_KEYGUARD = 9;
        public static final int CALL_PHONE = 10;
        public static final int SYSTEM_ALERT_WINDOW = 11;
    }

    public static class INTENT_KEY {
        public static final String SEND_ORDER_VALUE = "sendOrderValue";
        public static final String ORDER_ING = "orderIng";
        public static final String ENDLATLNG = "endLatLng";
        public static final String CANCELORDER = "cancelOrder";
        public static final String COMFIRM_COST = "comfirmCost";
        public static final String PAY_SUCCESS = "paysuccess";
        public static final String CITY = "city";
        public static final String ORDER_ID = "orderid";
        public static final String CHANGE_NAME  = "changeName";
        public static final String USER_NAME  = "user_name";
        public static final String USER_AVATAR  = "user_avatar";
        public static final String USER_ID  = "userId";
        public static final String UNFINISHED_ORDER  = " unfinishedOrder";


    }

    public static class REQUEST_RESULT_CODE {
        public static final int CHANGE_END  = 1;
        public static final int CHANGE_NAEM =2;
        public static final int QIAM_MING =3;
        public static final int USER_INFO =4;
        public static final int USER_ID =5;



    }

    public static class SP {
        public static final String SP_NAME = "spName";
        public static final String PHONE_NUM = "phoneNum";
        public static final String ID = "id";
        public static final String DEVICEID = "deviceId";
        public static final String IMAGE = "image";
    }

    public static class ACACHE{
        public static final String USER = "user";


    }

    public static class BROADCASTCODE{
        public static final String SEND_ORDER = "sendOrder";
        public static final String CANCEL_ORDER = "cancelOrder";
        public static final String PAY_SUCCESS = "paysuccess";

    }
}
