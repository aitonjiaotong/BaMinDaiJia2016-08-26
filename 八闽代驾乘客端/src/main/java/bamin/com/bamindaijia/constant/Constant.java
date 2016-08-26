package bamin.com.bamindaijia.constant;

/**
 * Created by zjb on 2016/6/12.
 */
public class Constant {
    //    http://192.168.1.112:8080/designated/app/customer/sendMessage?phone=15871105320&code=3666
//    public static final String HOST = "http://120.24.46.15:8080/aiton-designated-passenager-app-webapp/designated/app/customer";
    public static final String HOST = "http://192.168.1.188:8080/designated/app/customer";
    public static final int ABLEVERSION = 0;//可用版本号
/*    //百度云推送的频道ID
    public static String CHANNELID = "";

    public static String getCHANNELID() {
        return CHANNELID;
    }

    public static void setCHANNELID(String CHANNELID) {
        Constant.CHANNELID = CHANNELID;
    }*/

    public static class Url {
        //更新
        public static final String UP_GRADE = "http://bmcx.oss-cn-shanghai.aliyuncs.com/bmzc/upgradea_daijiachengke.txt";
        //        public static final String GET_SMS = HOST + "/sendMessage";
        //短信
        public static final String GET_SMS = "http://120.55.166.203:8020/aiton-tickets-app-webapp/public/sendmessage";
        //获取司机信息
        public static final String GETDRIVERINFO = "http://192.168.1.188:8080/driver/designated/app/driver/loadloginId";
        /**
         * 传入参数
         * name(名称)
         * phone(电话)
         * deviceId(手机ID)
         */
        public static final String LOGIN = HOST + "/addCustomer";
        public static final String CHECK_LIVE = HOST + "/live/cheklive";//判断服务器
        public static final String LOADLOGINID = HOST + "/loadloginId";//传入参数：account_id（用户ID）
        /**
         * 传入参数：
         * customerId（顾客ID）
         * customerPhone（号码）
         * customerName（姓名）
         * startLocation（出发地）
         * endLocation（目的地）
         * startCoordinate（起点坐标）
         * endCoordinate（终点坐标）
         * orderCity（订单城市）
         * money（金额）
         *fee（小费）
         * mileage（公里数）
         * key（百度云推送ID）
         */
        public static final String CALLDRIVER = HOST + "/order/addOrder";
        /**
         * 传入参数:
         * start(司机起点坐标)
         */
        public static final String GETDRIVER = HOST + "/order/getDriver";
        /**
         * 花费
         * 传入参数：
         * start(起点坐标)
         * end（终点坐标）
         * city（当前城市）
         */
        public static final String GETCOST = HOST + "/order/estimateFee";
        public static final String FIND_NOT_FINISH_ORDER = HOST + "/order/findNotFinishOrder";
        public static final String GET_ORDER_IS_ING = HOST+"/order/getOrderIsIng";
        public static final String CUSTOMER_CANCEL_ORDER = HOST+"/order/customerCancelOrder";
        //获取支付宝签名
        public static final String GETSIGN = HOST + "/alipay/getsign";



        public static final String ISPAY = HOST + "/order/againPayPage"; //是否支付

        public static final String CUSTOMERIMAGE = HOST + "/image/customerImage";//用户头像
        public static final String USER_INFO = HOST + "/updataPersonal";//用户信息
        public static final String ALL_ORDER = HOST + "/order/customerOldOrder";//所有订单



    }

    public static class PERMISSION {
        public static final int ACCESS_COARSE_LOCATION = 0;
        public static final int ACCESS_FINE_LOCATION = 1;
        public static final int WRITE_EXTERNAL_STORAGE = 2;
        public static final int READ_EXTERNAL_STORAGE = 3;
        public static final int READ_PHONE_STATE = 4;
        public static final int READ_CONTACTS = 5;
        public static final int RECEIVE_BOOT_COMPLETED = 6;
        public static final int CALL_PHONE = 7;
        public static final int PERMISSION_READ_EXTERNAL_STORAGE =8;
        public static final int PERMISSION_CAMERA =9;
        public static final int SYSTEM_ALERT_WINDOW =10;


    }

    public static class INTENT_KEY {
        public static final String START_SITE = "startSite";
        public static final String CITY = "city";
        public static final String CITY_CODE = "cityCode";
        public static final String START_SITE_BACK = "startSiteBack";
        public static final String END_SITE_BACK = "endSiteBack";
        public static final String START_LATLNG_BACK = "startLatlngBack";
        public static final String END_LATLNG_BACK = "endLatlngBack";
        public static final String CHOSSE_TYPE = "chosse_type";
        public static final String START = "start";
        public static final String END = "end";
        public static final String THE_CONTACTS = "the_contacts";
        public static final String PUSH_ORDER_ING_VALUE = "pushOrderIngValue";
        public static final String GETPASSAGER = "getPassager";
        public static final String DRIVER_CHANGE_MILEAGE = "driverChangeMileage";
        public static final String COST = "cost";
        public static final String ORDER = "order";
        public static final String COMMONLY_ADDRESS = "commonly_address";
        public static final String COMMONLY_ADDRESS_HOME = "commonly_address_home";
        public static final String COMMONLY_ADDRESS_COMPANY = "commonly_address_company";
        public static final String CHOSSE_SITE_ADDRESS_START = "chosse_site_address_start";
        public static final String CHOSSE_SITE_ADDRESS_END = "chosse_site_address_end";
        public static final String CHANGE_NAME  = "changeName";
        public static final String QIAN_MING  = "qianming";
        public static final String USER_NAME  = "user_name";
        public static final String USER_AVATAR  = "user_avatar";
        public static final String CHANGE_END  = "changeEnd";
        public static final String USER_ID  = "userId";
        public static final String UNFINISHED_ORDER  = " unfinishedOrder";


    }

    public static class REQUEST_RESULT_CODE {
        public static final int CHOSSE_START_SITE = 1;
        public static final int CHOSSE_END_SITE = 2;
        public static final int PICK_CONTACT = 3;
        public static final int CHOSSE_CONTACTS = 4;
        public static final int SETTING = 5;
        public static final int CHOOSE_CITY = 6;
        public static final int COMMONLY_ADDRESS_HOME =7;
        public static final int COMMONLY_ADDRESS_COMPANY =8;
        public static final int CHANGE_NAEM =9;
        public static final int QIAM_MING =10;
        public static final int USER_INFO =11;
        public static final int USER_ID =12;
        public static final int GPS =13;
    }

    public static class SP {
        public static final String CONTACTS = "contacts";//存储常用联系电话
        public static final String SP_NAME = "spName";
        public static final String PHONE_NUM = "phoneNum";
        public static final String ID = "id";
        public static final String DEVICEID = "deviceId";
        public static final String IMAGE = "image";
    }

    public static class ACACHE{
        public static final String USER = "user";
        public static final String KEY = "key";
        public static final String TIP_HOME = "tip_home";

        public static final String TIP_COMPANY = "tip_company";
    }

    public static class BROADCASTCODE {
        public static final String HAS_DRIVER = "hasDiver";
        public static final String GETPASSAGER = "getPassager";
        public static final String DRIVER_CHANGE_MILEAGE = "driverChangeMileage";
        public static final String CHANGE_END = "changeEnd";
    }
}
