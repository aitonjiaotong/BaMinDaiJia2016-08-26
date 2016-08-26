package bamin.com.bamindaijia.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.upgrade.UpgradeUtils;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.Installation;
import com.aiton.administrator.shane_library.shane.utils.IsMobileNOorPassword;
import com.aiton.administrator.shane_library.shane.utils.NetChecker;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.aiton.administrator.shane_library.shane.widget.AdDialog;
import com.aiton.administrator.shane_library.shane.widget.AnimCheckBox;
import com.aiton.administrator.shane_library.shane.widget.MyProgressDialog;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.nearby.NearbySearch;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.DriverList;
import bamin.com.bamindaijia.model.Sms;
import bamin.com.bamindaijia.model.User;
import bamin.com.bamindaijia.model.VersionAndHouTaiIsCanUse;
import bamin.com.bamindaijia.model.YuGuPrice;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends ZjbBaseActivity implements View.OnClickListener {
    MapView mMapView = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private TextView mTextView_end;
    private MyProgressDialog mProgressDialog;
    private MyProgressDialog mProgressDialogCall;
    private RelativeLayout mRela_xiaofei;
    private RelativeLayout mRela_daijiafei;
    private TextView mTextView_xiaofei;
    private TextView mTextView_daijiafei;
    private Button mButton_call;
    private AnimCheckBox mAnimCheckBox_pay_forMe;
    private TextView mTextView_contacts;
    private DrawerLayout drawer_layout;
    private LinearLayout list_left_drawer;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMap AMap;
    private MarkerOptions mMarkerOptions = null;
    private int mBasic_color;
    private int mText_gray;
    private TextView mTextView_forMe;
    private TextView mTextView_forOther;
    private TextView mTextView_line_forMe;
    private TextView mTextView_line_forOther;
    private RelativeLayout mRela_lianxiren;
    private RelativeLayout mRela_parForMe;
    private TextView mTextView_lianxiren;
    private TextView mTextView_payforme_line;
    //    private Marker mMarker;
//    private Marker mCenterMarker;
    private TextView mTextView_start;
    private LatLng mLocationLatLng;
    private LatLng mStartLatLng;
    private String mAmapLocationPoiName;
    private String mCityCode;
    private String mCity;
    private boolean IsLogin = false;
    private String mPhoneNum;
    private int[] mI;
    private Runnable mR;
    private AlertDialog mLoginDialog;
    private LinearLayout mLinear_inputPhone;
    private LinearLayout mLinear_sms;
    private EditText mEditText_sms01;
    private EditText mEditText_sms02;
    private EditText mEditText_sms03;
    private EditText mEditText_sms04;
    private EditText mEditText_phoneNum;
    private Button mButton_sendSms;
    private String mSuijiMath;
    private double fee = 0;
    private boolean hasDriver = false;
//    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
//    //开启悬浮窗的Service
//    Intent floatWinIntent;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();//定位后取消progressDialog
                        mProgressDialog = null;
                    }
                    mCityCode = amapLocation.getCityCode();
                    mCity = amapLocation.getCity();
                    //设置上车地址
                    mAmapLocationPoiName = amapLocation.getPoiName();
                    mAddress = amapLocation.getAddress();
                    mTextView_start.setText(mAmapLocationPoiName);
                    mLocationLatLng = new LatLng(latitude, longitude);
                    mStartLatLng = mLocationLatLng;
                    moveCenterMarker(mLocationLatLng);
                    getDriver(mAmapLocationPoiName, false);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };
    private ArrayList<Marker> mMarkersDriver = new ArrayList<>();
    private LatLng mEndlatlng;
    private TextView mTextView_yuguPrice;
    private double mYuguPrice;
    private YuGuPrice mYuGuPrice;
    private LatLonPoint mEndLatlngBack;
    private BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.BROADCASTCODE.HAS_DRIVER.equals(action)) {

                mProgressDialogCall.dismiss();
                String pushOrderIngValue = intent.getStringExtra(Constant.INTENT_KEY.PUSH_ORDER_ING_VALUE);

                Intent intent1 = new Intent();
                intent1.putExtra(Constant.INTENT_KEY.PUSH_ORDER_ING_VALUE, pushOrderIngValue);
                intent1.setClass(MainActivity.this, OrderIngActivity.class);
                startActivityTo(intent1);
                mProgressDialogCall.dismiss();
            }
        }
    };
    private TextView mTextView_phone_head;
    private AsyncHttpClient asyncHttpClient_callDriver;
    private MyProgressDialog myProgressDialog;
    private LinearLayout ll_order_detail;
    private TextView mOrderAddressDetail;
    //    private TextView mOrderDetail;
    private TextView mTv_time;
    private String mAddress;
    private LinearLayout mLinear_address;
    private RelativeLayout mRela_address;
    private ImageView mImg_Avatar;
    private String name;
    private ImageView mImg_mine;
    private boolean isForMe;
    private LinearLayout mLinear_topAddress;
    private TextView mTextView_topStart;
    private TextView mTextView_topEnd;
    private LinearLayout mLinear_btAddress;
    private boolean unfinishedOrder;

    //获取司机信息
    private void getDriver(final String startName, final boolean isCallDriver) {
        String url = Constant.Url.GETDRIVER;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("start", mStartLatLng.longitude + "," + mStartLatLng.latitude);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("onResponse ", "司机信息" + new String(responseBody));
                try {
                    DriverList driverList = GsonUtils.parseJSON(new String(responseBody), DriverList.class);
                    if (0 == driverList.getStatus()) {
                        AMap.clear();
                        DriverList.DatasEntity datas = driverList.getDatas();
                        List<String> drivers = datas.getDrivers();
                        if (drivers.size() > 0) {
                            hasDriver = true;
//                            mCenterMarker.setTitle(startName + "\n" + datas.getDriverTime() + "分钟，从这里出发");
                            mTv_time.setText(datas.getDriverTime() + "");
                            mOrderAddressDetail.setText(startName);
                            setAddressView(true);
//                            mCenterMarker.showInfoWindow();
                            addPoiMarker(drivers);
                            if (isCallDriver) {
                                if (!"重新选择出发地".equals(startName)) {
                                    if (!hasDriver) {
                                        Toast toast = new Toast(MainActivity.this);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        View toast_no_driver = getLayoutInflater().inflate(R.layout.toast_no_driver, null);
                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        toast.setView(toast_no_driver);
                                        toast.show();
                                    } else {
                                        callDriver(mStartLatLng);
                                    }
                                } else {
                                    Toast toast = new Toast(MainActivity.this);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    View toast_no_driver = getLayoutInflater().inflate(R.layout.toast_no_driver, null);
                                    TextView text_toast = (TextView) toast_no_driver.findViewById(R.id.text_toast);
                                    text_toast.setText("请重新选择出发\n地");
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setView(toast_no_driver);
                                    toast.show();
                                }
                            }
                        } else {
                            Log.e("onSuccess ", "没有司机");
                            hasDriver = false;
                            mTv_time.setText("- -");
                            mOrderAddressDetail.setText(startName);
                            setAddressView(true);
                            if (isCallDriver){
                                Toast toast = new Toast(MainActivity.this);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                View toast_no_driver = getLayoutInflater().inflate(R.layout.toast_no_driver, null);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(toast_no_driver);
                                toast.show();
                            }
//                            mCenterMarker.setTitle("附近没有司机，请稍后");
//                            mCenterMarker.showInfoWindow();
                        }
                    } else {
                        toast(driverList.getMes());
                        if (isCallDriver){
                            Toast toast = new Toast(MainActivity.this);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            View toast_no_driver = getLayoutInflater().inflate(R.layout.toast_no_driver, null);
                            TextView text_toast = (TextView) toast_no_driver.findViewById(R.id.text_toast);
                            text_toast.setText("呼叫代驾失败\n请重新呼叫");
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(toast_no_driver);
                            toast.show();
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                toast("获取司机信息失败");
            }
        });

    }


//    /**
//     * 请求用户给予悬浮窗的权限
//     */
//    @TargetApi(Build.VERSION_CODES.M)
//    public void askForPermission() {
//        if (!Settings.canDrawOverlays(this)) {
//            Toast.makeText(MainActivity.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
//        } else {
//            startService(floatWinIntent);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
//            if (!Settings.canDrawOverlays(this)) {
//                Toast.makeText(MainActivity.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(MainActivity.this, "权限授予成功！", Toast.LENGTH_SHORT).show();
//                //启动FxService
//                startService(floatWinIntent);
//            }
//
//        }
//    }


    private void setAddressView(boolean show) {
        if (show) {
            mLinear_address.setVisibility(View.VISIBLE);
            mRela_address.setVisibility(View.GONE);
        } else {
            mLinear_address.setVisibility(View.GONE);
            mRela_address.setVisibility(View.VISIBLE);
        }
    }

    private TextView mTextView_phone;
    private PopupWindow mPopupWindow;
    private TextView mTextView_fee;

    /**
     * 呼叫代驾
     *
     * @param location
     */
    private void callDriver(LatLng location) {
        asyncHttpClient_callDriver = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        setDriverProgressDialog("正在呼叫代驾……");
        String url = Constant.Url.CALLDRIVER;
        params.put("customerId", mUser.getDatas().getId() + "");
        params.put("customerPhone", mUser.getDatas().getPhone());
        params.put("customerName", mUser.getDatas().getName());
        params.put("startLocation", mTextView_start.getText().toString().trim());
        params.put("endLocation", mTextView_end.getText().toString().trim());
        params.put("startCoordinate", location.longitude + "," + location.latitude);
        params.put("endCoordinate", mEndLatlngBack.getLongitude() + "," + mEndLatlngBack.getLatitude());
        params.put("orderCity", mCity);
        params.put("money", mYuguPrice + "");
        params.put("fee", fee + "");
        params.put("mileage", mYuGuPrice.getDatas().getMileage() + "");
        params.put("key", PushManager.getInstance().getClientid(this));
        asyncHttpClient_callDriver.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("onResponse ", "向后台提交订单" + s);
                try {

                } catch (Exception e) {
                    toast("系统出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onErrorResponse ", "提交订单失败");
            }
        });

    }

    private User mUser;

    /**
     * 设置当前位置的Marker点的图标，坐标
     * <p/>
     * //     * @param latLng
     */
    private void moveCenterMarker(LatLng latLng) {
//        mMarkerOptions = new MarkerOptions();
//        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));
//        mMarkerOptions.position(latLng);
//        if (mMarker != null) {
//            mMarker.destroy();
//        }
//        mMarker = AMap.addMarker(mMarkerOptions);
        AMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        if (!NetChecker.getInstance(MainActivity.this).isNetworkOnline()) {
            final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(MainActivity.this, "请检查网络", "确认");
            singleBtnDialog.show();
            singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
                @Override
                public void doWhat() {
                    singleBtnDialog.dismiss();
                    SysApplication.getInstance().exit();
                }
            });
        }
//        askForPermission();
        init();
        initMap(savedInstanceState);
        String clientid = PushManager.getInstance().getClientid(this);
        Log.e("MainActivity", "个推CID: --->>" + clientid);
        initGPS();

    }
    /**
     * 判断GPS模块是否开启，如果没有则开启提示
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        //
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "为了定位准确，请打开GPS",
                    Toast.LENGTH_SHORT).show();


            final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "为了定位准确，请打开GPS", "确认", "取消");
            twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    twoBtnDialog.dismiss();
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, Constant.REQUEST_RESULT_CODE.GPS); // 设置完成后返回到原来的界面
                }

                @Override
                public void doCancel() {
                    twoBtnDialog.dismiss();
                }
            });
            twoBtnDialog.show();
        }
    }
    @Override
    protected void initData() {

    }

    /**
     * 检查更新
     */
    private void checkUpGrade() {
        UpgradeUtils.checkUpgrade(MainActivity.this, Constant.Url.UP_GRADE);
    }

    /**
     * 获取当前运行界面的名称
     *
     * @return 运行界面的名称
     */
    private String getRunningActivityName() {
        String contextString = this.toString();
        try {
            return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    protected void initViews() {
        forMeChangText();
        setProgressDialog("正在定位……");
        mRela_xiaofei.setVisibility(View.GONE);
        mRela_daijiafei.setVisibility(View.GONE);
        mTextView_xiaofei.setVisibility(View.GONE);
        mTextView_daijiafei.setVisibility(View.GONE);
        mButton_call.setVisibility(View.GONE);
        if (IsLogin = true && mUser != null) {
            mTextView_phone_head.setText(mUser.getDatas().getPhone());
        }
        if (mUser != null) {
            loadAvatarImg(mUser.getDatas().getImage());
            setUserName(mUser.getDatas().getName());
        }
        initDrawer();
    }

    private void initDrawer() {
        if (IsLogin) {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.rela_forMe).setOnClickListener(this);
        findViewById(R.id.rela_forOther).setOnClickListener(this);
        findViewById(R.id.imageView_location).setOnClickListener(this);
        findViewById(R.id.rela_start).setOnClickListener(this);
        findViewById(R.id.rela_endSite).setOnClickListener(this);
        findViewById(R.id.rela_lianxiren).setOnClickListener(this);
        findViewById(R.id.rela_parForMe).setOnClickListener(this);
        mImg_mine.setOnClickListener(this);
        findViewById(R.id.rela_setting).setOnClickListener(this);
        findViewById(R.id.button_call).setOnClickListener(this);
        findViewById(R.id.rela_xiaofei).setOnClickListener(this);
        findViewById(R.id.rela_daijiafei).setOnClickListener(this);
        findViewById(R.id.rela_all_order).setOnClickListener(this);
        mImg_Avatar.setOnClickListener(this);
        mMapView.setOnClickListener(this);
        findViewById(R.id.image_addialog).setOnClickListener(this);
        findViewById(R.id.rela_start_top).setOnClickListener(this);
        findViewById(R.id.rela_end_top).setOnClickListener(this);
    }

    @Override
    protected void initSP() {
        ACache aCache = ACache.get(MainActivity.this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);
//        SharedPreferences sp = getSharedPreferences(Constant.SP.SP_NAME, MODE_PRIVATE);
//        mPhoneNumLogin = sp.getString(Constant.SP.PHONE_NUM, "");
//        mDeviceId = sp.getString(Constant.SP.DEVICEID, "");
//        mId = sp.getString(Constant.SP.ID, "");
//        mImage = sp.getString(Constant.SP.IMAGE, "");
        if (mUser == null) {
            IsLogin = false;
            Log.e("getSP ", "user为空");
        } else {
            IsLogin = true;
            Log.e("getSP ", "存储的电话号码" + mUser.getDatas().getPhone());
            Log.e("getSP ", "user状态" + mUser.getStatus());
            //检查是否在其他设备上登录
        }
        checkVersionAndHouTaiIsCanUse();
    }

    @Override
    protected void initIntent() {

    }

    /**
     * 检查是否在其他设备上登录
     */
    private void checkVersionAndHouTaiIsCanUse() {
        String url = Constant.Url.CHECK_LIVE;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("onSuccess ", "检查服务器" + s);
                try {
                    VersionAndHouTaiIsCanUse versionAndHouTaiIsCanUse = GsonUtils.parseJSON(s, VersionAndHouTaiIsCanUse.class);
                    if (versionAndHouTaiIsCanUse.getStatus() == 0) {
                        int ableVersion = versionAndHouTaiIsCanUse.getDatas().getAbleVersion();
                        if (versionAndHouTaiIsCanUse.getDatas().isLive()) {
                            if (Constant.ABLEVERSION < ableVersion) {
                                setDialogCkeckAble("当前版本不可用，请更新到最新版本", "确认");
                            } else {
                                checkUpGrade();
                                if (mUser == null) {

                                } else {
                                    checkIsLoginOnOtherDevice();
                                }
                            }
                        } else {
                            setDialogCkeck(versionAndHouTaiIsCanUse.getDatas().getMessage(), "确认");
                        }
                    } else {
                        setDialogCkeck(versionAndHouTaiIsCanUse.getMes(), "确认");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                setDialogCkeck("服务器正在升级，暂停服务", "确认");
            }
        });

    }


    /**
     * 检查是否在其他设备上登录
     */
    private void checkIsLoginOnOtherDevice() {
        final String deviceid = mUser.getDatas().getDeviceid();
        if (!"".equals(deviceid)) {
            String url = Constant.Url.LOADLOGINID;

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("account_id", mUser.getDatas().getId() + "");
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        User user = GsonUtils.parseJSON(s, User.class);
                        if (user.getStatus() == 0) {
                            if (!(user.getDatas().getDeviceid()).equals(deviceid)) {
                                setDialog("你的账号登录异常\n请重新登录", "确定");
                            } else {
                                getOrderIsIng(false);
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        toast("服务器出错");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }

    /**
     * 上传顾客ID到服务器上，如果返回true，就跳转到订单界面
     */
    private void getOrderIsIng(final boolean callDriver) {
        if (mUser != null) {
            String url = Constant.Url.FIND_NOT_FINISH_ORDER;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("customerId", mUser.getDatas().getId());
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        if ("true".equals(s)) {
                            unfinishedOrder = true;
                            final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(MainActivity.this, "你有未完成的订单", "继续", "取消");
                            twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {
                                    twoBtnDialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, OrderIngActivity.class);
                                    startActivityTo(intent);

                                }

                                @Override
                                public void doCancel() {
                                    twoBtnDialog.dismiss();

                                }
                            });
                            twoBtnDialog.show();
                        } else {
                            if (callDriver){
                                getDriver(mTextView_start.getText().toString().trim(), true);
                                unfinishedOrder = false;
                            }
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }

    private void setDialogCkeckAble(String messageTxt, String iSeeTxt) {
        SingleBtnDialog singleBtnDialog = new SingleBtnDialog(MainActivity.this, messageTxt, iSeeTxt);
        singleBtnDialog.show();
        singleBtnDialog.setCancelable(false);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                UpgradeUtils.checkUpgradeIsAble(MainActivity.this, Constant.Url.UP_GRADE);
            }
        });
    }

    private void setDialogCkeck(String messageTxt, String iSeeTxt) {
        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(MainActivity.this, messageTxt, iSeeTxt);
        singleBtnDialog.show();
        singleBtnDialog.setCancelable(false);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();
                SysApplication.getInstance().exit();
            }
        });
    }

    /**
     * 弹出未登录按钮跳转登录界面并清除登录信息
     *
     * @param messageTxt
     * @param iSeeTxt
     */
    private void setDialog(String messageTxt, String iSeeTxt) {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        TextView message = (TextView) commit_dialog.findViewById(R.id.message);
        Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
        message.setText(messageTxt);
        ISee.setText(iSeeTxt);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        final android.app.AlertDialog dialog = builder.setView(commit_dialog)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //清除用户登录信息
                ACache aCache = ACache.get(MainActivity.this);
                aCache.clear();
                IsLogin = false;
                showLoginDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSP();
        IntentFilter intent = new IntentFilter();
        intent.addAction(Constant.BROADCASTCODE.HAS_DRIVER);
        registerReceiver(recevier, intent);


    }


    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 获取存储的用户信息
     */
    private void getSP() {

    }


    protected void findID() {
        mBasic_color = getResources().getColor(R.color.basic_color);
        mText_gray = getResources().getColor(R.color.text_gray);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_left_drawer = (LinearLayout) findViewById(R.id.list_left_drawer);
        mTextView_phone_head = (TextView) findViewById(R.id.textView_phone_head);
        mMapView = (MapView) findViewById(R.id.map);
        mTextView_forMe = (TextView) findViewById(R.id.textView_forMe);
        mTextView_forOther = (TextView) findViewById(R.id.textView_forOther);
        mTextView_line_forMe = (TextView) findViewById(R.id.textView_line_forMe);
        mTextView_line_forOther = (TextView) findViewById(R.id.textView_line_forOther);
        mRela_lianxiren = (RelativeLayout) findViewById(R.id.rela_lianxiren);
        mRela_parForMe = (RelativeLayout) findViewById(R.id.rela_parForMe);
        mTextView_lianxiren = (TextView) findViewById(R.id.textView_lianxiren);
        mTextView_payforme_line = (TextView) findViewById(R.id.textView_payforme_line);
        mTextView_start = (TextView) findViewById(R.id.textView_start);
        mTextView_end = (TextView) findViewById(R.id.textView_end);
        mRela_xiaofei = (RelativeLayout) findViewById(R.id.rela_xiaofei);
        mRela_daijiafei = (RelativeLayout) findViewById(R.id.rela_daijiafei);
        mTextView_xiaofei = (TextView) findViewById(R.id.textView_xiaofei);
        mTextView_daijiafei = (TextView) findViewById(R.id.textView_daijiafei);
        mButton_call = (Button) findViewById(R.id.button_call);
        mAnimCheckBox_pay_forMe = (AnimCheckBox) findViewById(R.id.AnimCheckBox_pay_forMe);
        mTextView_contacts = (TextView) findViewById(R.id.textView_contacts);
        mTextView_fee = (TextView) findViewById(R.id.textView_fee);
        mTextView_yuguPrice = (TextView) findViewById(R.id.textView_yuguPrice);
        //中心点marker信息
        ll_order_detail = (LinearLayout) findViewById(R.id.ll_order_detail);
        ll_order_detail.setOnClickListener(this);//设置跳转到手动选择定位位置页面
        mOrderAddressDetail = (TextView) findViewById(R.id.tv_address_dateil);//地图定位后所显示的较为具体的地理位置信息
//        mOrderDetail = (TextView) findViewById(R.id.tv_order_detail);//地图定位后所显示的地理位置（TextView）
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mLinear_address = (LinearLayout) findViewById(R.id.linear_address);
        mRela_address = (RelativeLayout) findViewById(R.id.rela_address);
        mImg_Avatar = (ImageView) findViewById(R.id.imageView_touXiang);
        mImg_mine = (ImageView) findViewById(R.id.imageView_mine);
        mLinear_topAddress = (LinearLayout) findViewById(R.id.linear_topAddress);
        mTextView_topStart = (TextView) findViewById(R.id.textView_topStart);
        mTextView_topEnd = (TextView) findViewById(R.id.textView_TopEnd);
        mLinear_btAddress = (LinearLayout) findViewById(R.id.linear_btAddress);
    }

    private void setProgressDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new MyProgressDialog(this, text);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        mProgressDialog.dismiss();
                        SysApplication.getInstance().exit();
                    }
                    return false;
                }
            });
        } else {

        }
    }

    private void setDriverProgressDialog(String text) {
        mProgressDialogCall = new MyProgressDialog(this, text);
        mProgressDialogCall.setCancelable(false);
        mProgressDialogCall.show();
        mProgressDialogCall.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    asyncHttpClient_callDriver.cancelRequests(MainActivity.this, false);
                    mProgressDialogCall.dismiss();
                }
                return false;
            }
        });

    }


    /**
     * 为自己叫车
     */
    private void forMeChangText() {
        isForMe = true;
        mTextView_forMe.setText("为自己叫代驾");
        mTextView_forOther.setText("为别人叫");
        mTextView_forMe.setTextColor(mBasic_color);
        mTextView_line_forMe.setVisibility(View.VISIBLE);
        mTextView_forOther.setTextColor(mText_gray);
        mTextView_line_forOther.setVisibility(View.INVISIBLE);
        mRela_lianxiren.setVisibility(View.GONE);
        mRela_parForMe.setVisibility(View.GONE);
        mTextView_lianxiren.setVisibility(View.GONE);
        mTextView_payforme_line.setVisibility(View.GONE);
    }

    /**
     * 为别人叫车
     */
    private void forOtherChangText() {
        isForMe = false;
        mTextView_forMe.setText("为自己叫");
        mTextView_forOther.setText("为别人叫代驾");
        mTextView_forMe.setTextColor(mText_gray);
        mTextView_line_forMe.setVisibility(View.INVISIBLE);
        mTextView_forOther.setTextColor(mBasic_color);
        mTextView_line_forOther.setVisibility(View.VISIBLE);
        mRela_lianxiren.setVisibility(View.VISIBLE);
        mRela_parForMe.setVisibility(View.VISIBLE);
        mTextView_lianxiren.setVisibility(View.VISIBLE);
        mTextView_payforme_line.setVisibility(View.VISIBLE);
    }

    private void initMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        AMap = mMapView.getMap();
        //设置默认定位按钮是否显示
        UiSettings uiSettings = AMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        AMap.setMyLocationEnabled(true);
        AMap.moveCamera(CameraUpdateFactory.zoomBy(6));

//        ViewTreeObserver vto = mMapView.getViewTreeObserver();
//        //测量地图控件的高宽
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                //没有这句话，里面会被执行三次
//                mMapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                //设置中点的地图标记
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bts_location_fixed));
//                mCenterMarker = AMap.addMarker(markerOptions);
//                mCenterMarker.setTitle("从这里出发……");
//                mCenterMarker.showInfoWindow();
//                mCenterMarker.setPositionByPixels(mMapView.getMeasuredWidth() / 2, mMapView.getMeasuredHeight() / 2);
//            }
//        });

        AMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View inflate = getLayoutInflater().inflate(R.layout.driver_marker, null);
                return inflate;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        AMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("onMarkerClick ", "onMarkerClick " + marker.getTitle());
                marker.showInfoWindow();
                return true;
            }
        });

        AMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LatLng position = AMap.getCameraPosition().target;
                    float distance = AMapUtils.calculateLineDistance(mLocationLatLng, position);//计算两点之间的距离
                    if (distance > 50) {
                        Log.e("onTouch ", "中心店距离定位的距离：" + distance);
                        float distance02 = AMapUtils.calculateLineDistance(mStartLatLng, position);//计算两点之间的距离
                        if (distance02 > 10) {
                            mStartLatLng = position;

                            GeocodeSearch geocodeSearch = new GeocodeSearch(MainActivity.this);
                            //地理编码查询结果监听接口设置
                            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                                @Override
                                //根据给定的经纬度和最大结果数返回逆地理编码的结果列表。
                                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();//返回逆地理编码搜索的地理结果
                                    //regeocodeAddress.getAois()
                                    //返回AOI（面状数据）的数据，如POI名称、区域编码、中心点坐标、POI类型等
                                    Log.e("onRegeocodeSearched ",
                                            "逆地理编码建筑名称：" + regeocodeAddress.getBuilding() +
                                                    "\n格式化地址：" + regeocodeAddress.getFormatAddress() +
                                                    "\n社区名称：" + regeocodeAddress.getNeighborhood() +
                                                    "\n乡镇街道编码" + regeocodeAddress.getTowncode() +
                                                    "\n乡镇名称" + regeocodeAddress.getTownship()
                                    );
                                    if (regeocodeAddress.getBusinessAreas().size() > 0) {
                                        Log.e("onRegeocodeSearched BUS", "商圈名称" + regeocodeAddress.getBusinessAreas().get(0).getName());
                                    }
                                    if (regeocodeAddress.getCrossroads().size() > 0) {
                                        Log.e("onRegeocodeSearched CRO",
                                                "第一条道路的名称" + regeocodeAddress.getCrossroads().get(0).getFirstRoadName() +
                                                        "第二条道路的名称" + regeocodeAddress.getCrossroads().get(0).getSecondRoadName()
                                        );
                                    }
                                    if (regeocodeAddress.getAois().size() > 0) {
                                        Log.e("onRegeocodeSearched AOI", "AOI名称" + regeocodeAddress.getAois().get(0).getAoiName());
                                        Log.e("onRegeocodeSearched ", "反编码地址大于0");
                                        AoiItem aoiItem = regeocodeAddress.getAois().get(0);
                                        String aoiName = aoiItem.getAoiName();
                                        getDriver(aoiName, false);
                                        mTextView_start.setText(aoiName);
                                    } else {
                                        setAddressView(false);
//                                    mOrderDetail.setText("");
//                                    mCenterMarker.showInfoWindow();
                                        mTextView_start.setText("重新选择出发地");
                                    }
                                }

                                @Override
                                //根据给定的地理名称和查询城市，返回地理编码的结果列表
                                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                                }
                            });
                            //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
                            LatLonPoint latLonPoint = new LatLonPoint(position.latitude, position.longitude);//LatLonPoint表示一对经、纬度值，以双精度形式存储
                            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 1000, GeocodeSearch.AMAP);
                            geocodeSearch.getFromLocationAsyn(query);
                        }
                    } else {
                        getDriver(mAmapLocationPoiName, false);
                        mTextView_start.setText(mAmapLocationPoiName);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    mCenterMarker.hideInfoWindow();
                    mTextView_start.setText("从这里出发……");
                }
            }
        });
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 添加司机在地图上的图标
     *
     * @param drivers
     */
    private void addPoiMarker(List<String> drivers) {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.clear();
        for (int i = 0; i < drivers.size(); i++) {
            String[] split = drivers.get(i).split(",");//将司机的坐标经纬度拆分
            double lng = Double.parseDouble(split[0]);
            double lat = Double.parseDouble(split[1]);
            latLngs.add(new LatLng(lat, lng));
        }
        ArrayList<MarkerOptions> arrayMarkrOption = new ArrayList<>();
        arrayMarkrOption.clear();
        for (int i = 0; i < latLngs.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("" + i);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_driver));
            markerOptions.position(latLngs.get(i));
            arrayMarkrOption.add(markerOptions);
        }
//        if (mMarkersDriver.size() > 0) {
//            for (int i = 0; i < mMarkersDriver.size(); i++) {
//                mMarkersDriver.get(i).destroy();
//            }
//        }
        mMarkersDriver = AMap.addMarkers(arrayMarkrOption, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。
        //调用销毁功能，在应用的合适生命周期需要销毁附近功能
        NearbySearch.destroy();
        unregisterReceiver(recevier);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
        MobclickAgent.onPageStart(getRunningActivityName());
        MobclickAgent.onResume(this);


    }

    /**
     * 设置用户姓名
     *
     * @param name
     */
    private void setUserName(String name) {
        if (name != null && !TextUtils.isEmpty(name) && !name.equals("请填写姓名")) {
            mTextView_phone_head.setText(name);
        } else {
            mTextView_phone_head.setText(mUser.getDatas().getPhone());
        }
    }

    /**
     * 加载头像
     *
     * @param mImage
     */
    private void loadAvatarImg(String mImage) {
        if (mImage != null && !TextUtils.isEmpty(mImage)) {
            ImageLoader.getInstance().clearDiskCache();
            Log.e("checkLogin", "图片URL" + mImage);
            UILUtils.displayImageNoAnim(mImage, mImg_Avatar);
            UILUtils.displayImageNoAnim(mImage, mImg_mine);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
        MobclickAgent.onPageEnd(getRunningActivityName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_RESULT_CODE.CHOSSE_START_SITE && resultCode == Constant.REQUEST_RESULT_CODE.CHOSSE_START_SITE) {
            //起点的地址
            String startSiteBack = data.getStringExtra(Constant.INTENT_KEY.START_SITE_BACK);
            LatLonPoint startLatlngBack = data.getParcelableExtra(Constant.INTENT_KEY.START_LATLNG_BACK);
            mStartLatLng = new LatLng(startLatlngBack.getLatitude(), startLatlngBack.getLongitude());
            mTextView_start.setText(startSiteBack);
            mTextView_topStart.setText(startSiteBack);
            getDriver(startSiteBack, false);
            LatLng latLng = new LatLng(startLatlngBack.getLatitude(), startLatlngBack.getLongitude());
            AMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//            mOrderDetail.setText(startSiteBack);
//            mCenterMarker.showInfoWindow();
        } else if (requestCode == Constant.REQUEST_RESULT_CODE.CHOSSE_END_SITE && resultCode == Constant.REQUEST_RESULT_CODE.CHOSSE_END_SITE) {
            //目的地
            String end_site_back = data.getStringExtra(Constant.INTENT_KEY.END_SITE_BACK);
            mTextView_end.setText(end_site_back);
            mEndLatlngBack = data.getParcelableExtra(Constant.INTENT_KEY.END_LATLNG_BACK);
            mEndlatlng = new LatLng(mEndLatlngBack.getLatitude(), mEndLatlngBack.getLongitude());
            mRela_xiaofei.setVisibility(View.VISIBLE);
            mRela_daijiafei.setVisibility(View.VISIBLE);
            mTextView_xiaofei.setVisibility(View.VISIBLE);
            mTextView_daijiafei.setVisibility(View.VISIBLE);
            mButton_call.setVisibility(View.VISIBLE);
            mLinear_btAddress.setVisibility(View.GONE);
            mTextView_topStart.setText(mTextView_start.getText().toString().trim());
            mTextView_topEnd.setText(end_site_back);
            mLinear_topAddress.setVisibility(View.VISIBLE);
            getCost();
        } else if (requestCode == Constant.REQUEST_RESULT_CODE.CHOSSE_CONTACTS && resultCode == Constant.REQUEST_RESULT_CODE.CHOSSE_CONTACTS) {
            //选择联系人
            String theContacts = data.getStringExtra(Constant.INTENT_KEY.THE_CONTACTS);
            mTextView_contacts.setText(theContacts);
        } else if (requestCode == Constant.REQUEST_RESULT_CODE.SETTING && resultCode == Constant.REQUEST_RESULT_CODE.SETTING) {
            //设置，注销
            Log.e("MainActivity", "SETTING: --->>");
            drawer_layout.closeDrawer(list_left_drawer);
            IsLogin = false;
            mImg_mine.setImageResource(R.mipmap.ic_avatar);
            initDrawer();
        } else if (requestCode == Constant.REQUEST_RESULT_CODE.USER_INFO && resultCode == Constant.REQUEST_RESULT_CODE.USER_INFO) {
            //用户信息
            String userName = data.getStringExtra(Constant.INTENT_KEY.USER_NAME);
            String userAvatar = data.getStringExtra(Constant.INTENT_KEY.USER_AVATAR);
            setUserName(userName);
            loadAvatarImg(userAvatar);
        }
    }

    /**
     * 获取花费
     */
    private void getCost() {
        String url = Constant.Url.GETCOST;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("start", mStartLatLng.longitude + "," + mStartLatLng.latitude);
        params.put("end", mEndlatlng.longitude + "," + mEndlatlng.latitude);
        params.put("city", mCity);

        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("onResponse ", "获取预估价格" + s);
                try {
                    mYuGuPrice = GsonUtils.parseJSON(s, YuGuPrice.class);
                    if (mYuGuPrice.getStatus() == 1) {
                        toast(mYuGuPrice.getMes());
                    } else {
                        mYuguPrice = mYuGuPrice.getDatas().getMoney();
                        mTextView_yuguPrice.setText(mYuguPrice + "");
                    }
                } catch (Exception e) {
                    toast("服务器出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 发送短信
     */
    private void sendSMS() {
        mButton_sendSms.removeCallbacks(mR);
        mPhoneNum = mEditText_phoneNum.getText().toString().trim();
        boolean mobileNO = IsMobileNOorPassword.isMobileNO(mPhoneNum);//判断手机格式
        if (mobileNO) {
            mTextView_phone.setText(mPhoneNum);
            mEditText_sms01.requestFocus();
            mLinear_inputPhone.setVisibility(View.GONE);
            mLinear_sms.setVisibility(View.VISIBLE);
            mButton_sendSms.setEnabled(false);
            mI = new int[]{60};
            mR = new Runnable() {
                @Override
                public void run() {
                    mButton_sendSms.setText((mI[0]--) + "秒后重发");
                    if (mI[0] == 0) {
                        mButton_sendSms.setEnabled(true);
                        mButton_sendSms.setText("重发验证码");
                        return;
                    } else {

                    }
                    mButton_sendSms.postDelayed(mR, 1000);
                }
            };
            mButton_sendSms.postDelayed(mR, 0);
            getSms();
        } else {
            Toast.makeText(MainActivity.this, "输入的手机格式有误", Toast.LENGTH_SHORT).show();
            mEditText_phoneNum.setText("");
        }
    }

    /**
     * 发给短信验证码到服务器上，服务器返回数据验证发送是否成功
     */
    private void getSms() {
        mSuijiMath = (int) (Math.random() * 9000 + 1000) + "";
        String url = Constant.Url.GET_SMS;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone", mPhoneNum);
        params.put("code", mSuijiMath + "");

        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    Sms sms = GsonUtils.parseJSON(s, Sms.class);
                    int status = sms.getStatus();
                    toast(sms.getMes());
                    if (status == 0) {
                        //短信发送成功
                        toast("短信发送成功");
                    } else if (status == 1) {
                        //短信发送失败
                        toast("短信发送失败");
                    }
                } catch (Exception e) {
                    toast("服务器出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.image_addialog:
                final AdDialog adDialog = new AdDialog(MainActivity.this, R.mipmap.gui00);
                adDialog.setClicklistener(new AdDialog.ClickListenerInterface() {
                    @Override
                    public void doWhat() {
                        Toast.makeText(MainActivity.this, "点击了广告", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void cancle() {
                        adDialog.dismiss();
                    }
                });
                adDialog.show();
                break;
            case R.id.ll_order_detail: //跳转出发地选择界面
                intent.setClass(MainActivity.this, ChosseSite.class);
                intent.putExtra(Constant.INTENT_KEY.CHOSSE_TYPE, Constant.INTENT_KEY.START);
                intent.putExtra(Constant.INTENT_KEY.START_SITE, mStartLatLng);
                intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
                intent.putExtra(Constant.INTENT_KEY.CITY_CODE, mCityCode);
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.CHOSSE_START_SITE);
                break;
            case R.id.rela_daijiafei://跳转到价格详情

                intent.setClass(MainActivity.this, DaiJiaCostActivity.class);
                startActivityTo(intent);
                break;
            case R.id.textView_noFee:
                fee = 0;
                mTextView_fee.setText("加小费");
                mPopupWindow.dismiss();
                break;
            case R.id.textView_fee10:
                fee = 10;
                setFee();
                break;
            case R.id.textView_fee20:
                fee = 20;
                setFee();
                break;
            case R.id.textView_fee30:
                fee = 30;
                setFee();
                break;
            case R.id.linear_popupTouMing:
                mPopupWindow.dismiss();
                break;
            case R.id.rela_xiaofei:
                showPopupWindow();
                break;
            case R.id.button_call://呼叫代驾
                if (IsLogin) {
                    getOrderIsIng(true);
                } else {
                    showLoginDialog();
                }
                break;
            case R.id.button_sendSms:
                sendSMS();
                break;
            case R.id.button_loginNext:
                sendSMS();
                break;
            case R.id.imageView_backPhone:
                mLinear_inputPhone.setVisibility(View.VISIBLE);
                mLinear_sms.setVisibility(View.GONE);
                break;
            case R.id.imageView_cancleLoginDialog:
                mLoginDialog.dismiss();
                break;
            case R.id.imageView_cancleSmsDialog:
                mLoginDialog.dismiss();
                break;
            case R.id.rela_setting:
                intent.setClass(MainActivity.this, SettingActivity.class);
                intent.putExtra(Constant.INTENT_KEY.START_SITE, mStartLatLng);
                intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
                intent.putExtra(Constant.INTENT_KEY.CITY_CODE, mCityCode);
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.SETTING);
                break;
            case R.id.imageView_mine:
                if (IsLogin) {
                    drawer_layout.openDrawer(list_left_drawer);
                } else {
                    showLoginDialog();
                }
                break;
            case R.id.rela_lianxiren:
                intent.setClass(MainActivity.this, GetContactsActivity.class);
                startActivityForResult(intent, Constant.REQUEST_RESULT_CODE.CHOSSE_CONTACTS);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
                break;
            case R.id.rela_parForMe:
                if (mAnimCheckBox_pay_forMe.isChecked()) {
                    mAnimCheckBox_pay_forMe.setChecked(false);
                } else {
                    mAnimCheckBox_pay_forMe.setChecked(true);
                }
                break;
            case R.id.rela_start:
                ChooseSite(Constant.INTENT_KEY.START, Constant.REQUEST_RESULT_CODE.CHOSSE_START_SITE);
                break;
            case R.id.rela_endSite:
                ChooseSite(Constant.INTENT_KEY.END, Constant.REQUEST_RESULT_CODE.CHOSSE_END_SITE);
                break;
            case R.id.rela_start_top:
                ChooseSite(Constant.INTENT_KEY.START, Constant.REQUEST_RESULT_CODE.CHOSSE_START_SITE);
                break;
            case R.id.rela_end_top:
                ChooseSite(Constant.INTENT_KEY.END, Constant.REQUEST_RESULT_CODE.CHOSSE_END_SITE);
                break;
            case R.id.rela_forMe:
                forMeChangText();
                break;
            case R.id.rela_forOther:
                forOtherChangText();
                break;
            case R.id.imageView_location://定位
                mLocationClient.startLocation();
                break;
            case R.id.imageView_touXiang://跳转个人信息
                if (IsLogin) {
                    intent.setClass(this, MineActivity.class);

                    startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.USER_INFO);
                    drawer_layout.closeDrawer(list_left_drawer);
                } else {
                    showLoginDialog();
                }
                break;

            case R.id.rela_all_order://所有订单
                intent.setClass(this, AllOrderActivity.class);
                intent.putExtra(Constant.INTENT_KEY.USER_ID, mUser.getDatas().getId());
                intent.putExtra(Constant.INTENT_KEY.UNFINISHED_ORDER, unfinishedOrder);
                Log.e("MainActivity", "onClick: --->>" + mUser.getDatas().getId());

                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.USER_ID);
                break;
            default:
                break;
        }
    }

    private void ChooseSite(String end, int chosseEndSite) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChosseSite.class);
        intent.putExtra(Constant.INTENT_KEY.CHOSSE_TYPE, end);
        intent.putExtra(Constant.INTENT_KEY.START_SITE, mStartLatLng);
        intent.putExtra(Constant.INTENT_KEY.CITY, mCity);

        intent.putExtra(Constant.INTENT_KEY.CITY_CODE, mCityCode);
        startActivityForResultTo(intent, chosseEndSite);
    }

    /**
     * 设置popupwindow小窗口
     */
    private void showPopupWindow() {
        View inflate = getLayoutInflater().inflate(R.layout.popoumenu, null);
        //最后一个参数为true，点击PopupWindow消失,宽必须为match，不然肯呢个会导致布局显示不完全
        inflate.findViewById(R.id.linear_popupTouMing).setOnClickListener(this);
        inflate.findViewById(R.id.textView_fee10).setOnClickListener(this);
        inflate.findViewById(R.id.textView_fee20).setOnClickListener(this);
        inflate.findViewById(R.id.textView_fee30).setOnClickListener(this);
        inflate.findViewById(R.id.textView_noFee).setOnClickListener(this);
        mPopupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置外部点击无效
        mPopupWindow.setOutsideTouchable(false);
        //设置背景变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        //当popup关闭后背景恢复
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        BitmapDrawable bitmapDrawable = new BitmapDrawable();
//        mPopupWindow.setAnimationStyle(R.style.PopupStyle);
        mPopupWindow.setBackgroundDrawable(bitmapDrawable);
        mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM, 0, 0);
    }

    private void setFee() {
        mTextView_fee.setText(fee + "元");
        mPopupWindow.dismiss();
    }

    /**
     * 登录界面
     */
    private void showLoginDialog() {
        View login_dialog_view = getLayoutInflater().inflate(R.layout.login_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.dialog);
        mLinear_inputPhone = (LinearLayout) login_dialog_view.findViewById(R.id.linear_inputPhone);
        mLinear_sms = (LinearLayout) login_dialog_view.findViewById(R.id.linear_SMS);
        mEditText_phoneNum = (EditText) login_dialog_view.findViewById(R.id.editText_phoneNum);
        mButton_sendSms = (Button) login_dialog_view.findViewById(R.id.button_sendSms);
        mTextView_phone = (TextView) login_dialog_view.findViewById(R.id.textView_phone);
        mLinear_inputPhone.setVisibility(View.VISIBLE);
        mLinear_sms.setVisibility(View.GONE);
        login_dialog_view.findViewById(R.id.imageView_cancleLoginDialog).setOnClickListener(this);
        login_dialog_view.findViewById(R.id.imageView_cancleSmsDialog).setOnClickListener(this);
        login_dialog_view.findViewById(R.id.button_loginNext).setOnClickListener(this);
        login_dialog_view.findViewById(R.id.imageView_backPhone).setOnClickListener(this);
        mButton_sendSms.setOnClickListener(this);
        mEditText_sms01 = (EditText) login_dialog_view.findViewById(R.id.editText_Sms01);
        mEditText_sms02 = (EditText) login_dialog_view.findViewById(R.id.editText_Sms02);
        mEditText_sms03 = (EditText) login_dialog_view.findViewById(R.id.editText_Sms03);
        mEditText_sms04 = (EditText) login_dialog_view.findViewById(R.id.editText_Sms04);
        mEditText_sms01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (mEditText_sms01.getText().length() > 0 && mEditText_sms02.getText().length() > 0 && mEditText_sms03.getText().length() > 0 && mEditText_sms04.getText().length() > 0) {
                        //开始验证验证码登录
                        login();
                    } else {
                        //获取焦点，当输入数字跳到下一个输入框
                        mEditText_sms02.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditText_sms02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (mEditText_sms01.getText().length() > 0 && mEditText_sms02.getText().length() > 0 && mEditText_sms03.getText().length() > 0 && mEditText_sms04.getText().length() > 0) {
                        //开始验证验证码登录
                        login();
                    } else {
                        mEditText_sms03.requestFocus();
                    }
                } else {
                    mEditText_sms01.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditText_sms03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (mEditText_sms01.getText().length() > 0 && mEditText_sms02.getText().length() > 0 && mEditText_sms03.getText().length() > 0 && mEditText_sms04.getText().length() > 0) {
                        //开始验证验证码登录
                        login();
                    } else {
                        mEditText_sms04.requestFocus();
                    }
                } else {
                    mEditText_sms02.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditText_sms04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (mEditText_sms01.getText().length() > 0 && mEditText_sms02.getText().length() > 0 && mEditText_sms03.getText().length() > 0 && mEditText_sms04.getText().length() > 0) {
                        //开始验证验证码登录
                        login();
                    } else {
//                        editText_Sms02.requestFocus();
                    }
                } else {
                    mEditText_sms03.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditText_sms04.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(mEditText_sms04.getText().length()==0){
                        mEditText_sms03.requestFocus();
                    }else {
                        return false;
                    }
                    return true;
                }
                return false;
            }
        });
        mEditText_sms03.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(mEditText_sms03.getText().length()==0){
                        mEditText_sms02.requestFocus();
                    }else {
                        return false;
                    }
                    return true;
                }
                return false;
            }
        });
        mEditText_sms02.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(mEditText_sms02.getText().length()==0){
                        mEditText_sms01.requestFocus();
                    }else {
                        return false;
                    }
                    return true;
                }
                return false;
            }
        });
        builder.setCancelable(false);
        builder.setView(login_dialog_view);
        mLoginDialog = builder.create();
        mLoginDialog.show();
        //监听后退键，关闭对话框
        mLoginDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    mLoginDialog.dismiss();
                }
                return false;
            }
        });
    }

    /**
     * 验证是否登录成功
     */
    private void login() {
        String loginCode = mEditText_sms01.getText().toString().trim() + mEditText_sms02.getText().toString().trim() + mEditText_sms03.getText().toString().trim() + mEditText_sms04.getText().toString().trim();
        if (mSuijiMath.equals(loginCode)) {
            myProgressDialog = new MyProgressDialog(MainActivity.this, "正在登录.....");
            myProgressDialog.show();
            myProgressDialog.setCancelable(false);
            myProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        SysApplication.getInstance().exit();

                    }
                    return false;
                }
            });
            toast("短信验证成功");
            //每次存储唯一标识
            final String DeviceId = Installation.id(MainActivity.this);
            //向后台服务推送用户短信验证成功，发送手机号----start----//
            String url = Constant.Url.LOGIN;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("phone", mPhoneNum + "");
            params.put("deviceid", DeviceId + "");
            Log.e("MainActivity", "个推CID: --->>" + PushManager.getInstance().getClientid(this));

            params.put("key", PushManager.getInstance().getClientid(this));

            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("onResponse ", "乘客登录" + s);
                    try {
                        mUser = GsonUtils.parseJSON(s, User.class);
                        if (mUser.getStatus() == 0) {
                            ACache aCache = ACache.get(MainActivity.this);
                            aCache.put(Constant.ACACHE.USER, mUser, 30 * ACache.TIME_DAY);
                            mTextView_phone_head.setText(mUser.getDatas().getPhone());
                            //存储手机号和用户id到本地
                            toast(mUser.getMes());
                            //友盟统计
                            MobclickAgent.onProfileSignIn(mPhoneNum);
                            myProgressDialog.dismiss();
                            mLoginDialog.dismiss();
                            IsLogin = true;
                            loadAvatarImg(mUser.getDatas().getImage());
                            setUserName(mUser.getDatas().getName());
                            initDrawer();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, mUser.getMes(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        toast("服务器出错");
                        myProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    toast("登录超时……");
                    myProgressDialog.dismiss();
                }
            });

        } else {
            toast("短信验证失败");
            myProgressDialog.dismiss();
        }
    }

    /**
     * 双击退出应用
     */
    private long currentTime = 0;

    @Override
    public void onBackPressed() {
        if (mEndlatlng != null) {
            mEndlatlng = null;
            mTextView_end.setText("你要去哪儿");
            mRela_xiaofei.setVisibility(View.GONE);
            mRela_daijiafei.setVisibility(View.GONE);
            mTextView_xiaofei.setVisibility(View.GONE);
            mTextView_daijiafei.setVisibility(View.GONE);
            mButton_call.setVisibility(View.GONE);
            mLinear_topAddress.setVisibility(View.GONE);
            mLinear_btAddress.setVisibility(View.VISIBLE);
        } else {
            if (System.currentTimeMillis() - currentTime > 1000) {
                Toast toast = Toast.makeText(MainActivity.this, "双击退出应用", Toast.LENGTH_SHORT);
                toast.show();
                currentTime = System.currentTimeMillis();
            } else {
                SysApplication.getInstance().exit();
            }
        }
    }

}
