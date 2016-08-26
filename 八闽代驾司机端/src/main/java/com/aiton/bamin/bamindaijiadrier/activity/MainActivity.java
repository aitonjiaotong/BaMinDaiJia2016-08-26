package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.upgrade.UpgradeUtils;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.HTTPUtils;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;
import com.aiton.administrator.shane_library.shane.widget.AdDialog;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.TTSController;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.Order;
import com.aiton.bamin.bamindaijiadrier.model.SendOrder;
import com.aiton.bamin.bamindaijiadrier.model.User;
import com.aiton.bamin.bamindaijiadrier.model.VersionAndHouTaiIsCanUse;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.UploadInfo;
import com.android.volley.VolleyError;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends ZjbBaseActivity implements View.OnClickListener {
    //    MapView mMapView = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //    private com.amap.api.maps.AMap AMap;
//    private MarkerOptions mMarkerOptions = null;
//    private Marker mMarker;
    private LatLng mLocationLatLng;
    private boolean isShouFaChe = false;
    private DrawerLayout drawer_layout;
    private LinearLayout list_left_drawer;
    private List<SendOrder> mSendOrderList = new ArrayList<>();

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度
                    cancelLoadingDialog();
                    //设置上车地址
                    mLocationLatLng = new LatLng(latitude, longitude);
//                    moveCenterMarker(mLocationLatLng);
                    if (isShouFaChe) {
                        initNear();
                    }
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };
    private TextView mTextView_shoufache;
    private String mPhone;
    private String mId;
    private String mDeviceId;
    private ImageView mImageView_shoufache;
    private BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.BROADCASTCODE.SEND_ORDER.equals(action)) {
                String sendOrderValue = intent.getStringExtra(Constant.INTENT_KEY.SEND_ORDER_VALUE);
                SendOrder sendOrder = GsonUtils.parseJSON(sendOrderValue, SendOrder.class);
                ttsManager.stopSpeaking();
                ttsManager.startSpeaking();
                ttsManager.playText(sendOrder.getStartLocation() + "到" + sendOrder.getEndLocation() + "," + sendOrder.getMileage() + "公里");
                mSendOrderList.add(sendOrder);
                mOrderAdapter.notifyDataSetChanged();
            }
        }
    };
    private ListView mListView_getorder;
    private MyAdapter mOrderAdapter;
    private String name;
    private TTSController ttsManager;
    private TextView mTextView_phone_head;
    private User mUser;
    private ImageView mImg_Avatar;
    private ImageView mImg_mine;
    private TextView mTv_time;
    private boolean unfinishedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        //语音引擎初始化
        ttsManager = TTSController.getInstance(this);
        ttsManager.init();
        ttsManager.startSpeaking();
        init();
        //检查服务器是否存活和当前版本是否可用
        checkVersionAndHouTaiIsCanUse();
        initMap(savedInstanceState);
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

            final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "为了定位准确，请打开GPS", "确定", "取消");
            twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    twoBtnDialog.dismiss();
                }

                @Override
                public void doCancel() {
                    twoBtnDialog.dismiss();
                }
            });
        }
    }
    //初始化附近派单并开始上传司机坐标
    private void initNear() {
        //构造上传位置信息
        UploadInfo loadInfo = new UploadInfo();
        //设置上传位置的坐标系支持AMap坐标数据与GPS数据
        loadInfo.setCoordType(NearbySearch.AMAP);
        //设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取
        LatLonPoint position = new LatLonPoint(mLocationLatLng.latitude, mLocationLatLng.longitude);
        loadInfo.setPoint(position);
        //设置上传用户id
        loadInfo.setUserID(mId);
        //调用异步上传接口
        NearbySearch search = NearbySearch.getInstance(getApplicationContext());
        search.uploadNearbyInfoAsyn(loadInfo);
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 检查服务器是否存活和当前版本是否可用
     */
    private void checkVersionAndHouTaiIsCanUse() {
        String url = Constant.Url.CHECK_LIVE;
        Map<String, String> map = new HashMap<>();
        HTTPUtils.post(MainActivity.this, url, map, new VolleyListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setDialogCkeck("服务器正在升级，暂停服务", "确认");
            }

            @Override
            public void onResponse(String s) {
                Log.e("onResponse ", "检查当前版本是否可用" + s);
                try {
                    VersionAndHouTaiIsCanUse versionAndHouTaiIsCanUse = GsonUtils.parseJSON(s, VersionAndHouTaiIsCanUse.class);
                    if (versionAndHouTaiIsCanUse.getStatus() == 0) {
                        int ableVersion = versionAndHouTaiIsCanUse.getDatas().getAbleVersion();
                        if (versionAndHouTaiIsCanUse.getDatas().isLive()) {
                            if (Constant.ABLEVERSION < ableVersion) {
                                setDialogCkeckAble("当前版本不可用，请更新到最新版本", "确认");
                            } else {
                                checkUpGrade();
                                //检查是否在其他设备上登录
                                checkIsLoginOnOtherDevice();
                            }
                        } else {
                            setDialogCkeck(versionAndHouTaiIsCanUse.getDatas().getMessage(), "确认");
                        }
                    } else {
                        setDialogCkeck(versionAndHouTaiIsCanUse.getMes(), "确认");
                    }
                } catch (Exception e) {
                    toast("服务器出错");
                }
            }
        });
    }

    /**
     * 检查是否在其他设备上登录
     */
    private void checkIsLoginOnOtherDevice() {
        Log.e("nOtherDevice", "设备号" + mDeviceId + "用户号" + mId);
        if (!"".equals(mDeviceId)) {
            String url = Constant.Url.LOADLOGINID;
            Map<String, String> map = new HashMap<>();
            map.put("account_id", mId + "");
            HTTPUtils.post(MainActivity.this, url, map, new VolleyListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("onErrorResponse ", "获取唯一设备号失败");
                }

                @Override
                public void onResponse(String s) {
                    Log.e("onResponse ", "检查是否在其他设备上登录" + s);
                    try {
                        User user = GsonUtils.parseJSON(s, User.class);
                        if (user.getStatus() == 0) {
                            if (!(user.getDatas().getDeviceid()).equals(mDeviceId)) {
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
            });
        }
    }

    /**
     * 检查是否有正在进行的订单
     */
    private void getOrderIsIng(final boolean faChe) {
        Log.e("getOrderIsIng ", "进去检查请求");
        showLoadingDialog("检查是否有正在进行中的订单……");
        String url = Constant.Url.DRIVER_FIND_NOT_FINISH_ORDER;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("driverId", mId);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("onSuccess ", "检查是否有订单正在进行：" + s);
                try {
                    if ("true".equals(s)) {
                        unfinishedOrder = true;

                        Log.e("onSuccess ", "有在进行");
                        cancelLoadingDialog();
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
                        cancelLoadingDialog();
                        Log.e("onSuccess ", "没有进行");
                        if (faChe){
                            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                            LinearInterpolator lin = new LinearInterpolator();
                            animation.setInterpolator(lin);
                            ttsManager.startSpeaking();
                            ttsManager.playText("八闽出行，风雨同行！");
                            mTextView_shoufache.setText("停止");
                            mImageView_shoufache.setImageResource(R.mipmap.circle_jiedan);
                            if (animation != null) {
                                mImageView_shoufache.startAnimation(animation);
                            }
                            initNear();
                            isShouFaChe = true;
                            unfinishedOrder = false;

                        }
                    }
                } catch (Exception e) {
                    Log.e("onSuccess ", "检查出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure ", "检查请求失败");
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
        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(this, messageTxt, iSeeTxt);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();
                //清除用户登录信息
                ACache aCache = ACache.get(MainActivity.this);
                aCache.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityTo(intent);
            }
        });
        singleBtnDialog.show();
    }

    private void checkUpGrade() {
        UpgradeUtils.checkUpgrade(MainActivity.this, Constant.Url.UP_GRADE);
    }

    private void setDialogCkeckAble(String messageTxt, String iSeeTxt) {
        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(this, messageTxt, iSeeTxt);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();
                UpgradeUtils.checkUpgradeIsAble(MainActivity.this, Constant.Url.UP_GRADE);

            }
        });
        singleBtnDialog.show();
    }

    //dialog提示
    private void setDialogCkeck(String messageTxt, String iSeeTxt) {
        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(this, messageTxt, iSeeTxt);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();
                SysApplication.getInstance().exit();

            }
        });
        singleBtnDialog.show();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        showLoadingDialog("正在定位……");
        mOrderAdapter = new MyAdapter();
        mListView_getorder.setAdapter(mOrderAdapter);
        if (mUser != null) {
            setUserName(name);
            loadAvatarImg(mUser.getDatas().getImage());
        }
    }

    @Override
    protected void setListeners() {
//        findViewById(R.id.imageView_location).setOnClickListener(this);
        findViewById(R.id.imageView_mine).setOnClickListener(this);
        findViewById(R.id.rela_mine).setOnClickListener(this);
        findViewById(R.id.rela_setting).setOnClickListener(this);
        findViewById(R.id.rela_shoufache).setOnClickListener(this);
        mImg_Avatar.setOnClickListener(this);
        findViewById(R.id.image_adDialog).setOnClickListener(this);
        findViewById(R.id.rela_all_order).setOnClickListener(this);
    }

    @Override
    protected void initSP() {
        ACache aCache = ACache.get(MainActivity.this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);
        if (mUser != null) {

            mId = "" + mUser.getDatas().getId();
            name = mUser.getDatas().getName();
            mDeviceId = mUser.getDatas().getDeviceid();
            Log.e("MainActivity", "imgUrl: --->>" + mUser.getDatas().getImage());
            Log.e("MainActivity", "用户ID: --->>" + mId);


        }

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_left_drawer = (LinearLayout) findViewById(R.id.list_left_drawer);
//        mMapView = (MapView) findViewById(R.id.map);
        mTextView_shoufache = (TextView) findViewById(R.id.textView_shoufache);
        mImageView_shoufache = (ImageView) findViewById(R.id.imageView_shoufache);
        mListView_getorder = (ListView) findViewById(R.id.listView_getorder);
        mTextView_phone_head = (TextView) findViewById(R.id.textView_phone_head);
        mImg_Avatar = (ImageView) findViewById(R.id.imageView_touXiang);
        mImg_mine = (ImageView) findViewById(R.id.imageView_mine);
        mTv_time = (TextView) findViewById(R.id.tv_time);

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSendOrderList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.get_order_item, null);
            TextView textView_start = (TextView) inflate.findViewById(R.id.textView_start);
            TextView textView_end = (TextView) inflate.findViewById(R.id.textView_end);
            TextView textView_HowLong = (TextView) inflate.findViewById(R.id.textView_HowLong);
            TextView textView_cost = (TextView) inflate.findViewById(R.id.textView_cost);
            TextView textView_fee = (TextView) inflate.findViewById(R.id.textView_fee);
            if (mSendOrderList.size() > 0) {
                inflate.findViewById(R.id.button_ignore).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSendOrderList.remove(position);
                        mOrderAdapter.notifyDataSetChanged();
                    }
                });
                inflate.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aceptOrder(position);
                    }
                });
                SendOrder sendOrder = mSendOrderList.get(position);
                textView_start.setText(sendOrder.getStartLocation());
                textView_end.setText(sendOrder.getEndLocation());
                textView_HowLong.setText(sendOrder.getMileage() + "公里");
                textView_cost.setText(sendOrder.getMoney() + "");
                textView_fee.setText(sendOrder.getFee() + "");
            }
            return inflate;
        }
    }

    /**
     * 司机接单
     *
     * @param position
     */
    private void aceptOrder(final int position) {
        String url = Constant.Url.ACECPT_ORDER;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        SendOrder sendOrder = mSendOrderList.get(position);
        params.put("orderId", sendOrder.getOrderId());
        params.put("driverId", mId);
        params.put("driverPhone", mUser.getDatas().getPhone());
        params.put("driverName", name);
        params.put("driverCoordinate", mLocationLatLng.longitude + "," + mLocationLatLng.latitude);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    Log.e("onSuccess ", "接受订单返回值" + s);
                    Order order = GsonUtils.parseJSON(s, Order.class);
                    int status = order.getStatus();
                    ttsManager.stopSpeaking();
                    ttsManager.startSpeaking();
                    if (status == 0) {
                        clearLocation();
                        ttsManager.playText("抢单成功");
                        mSendOrderList.remove(position);
                        mOrderAdapter.notifyDataSetChanged();
                        Intent intent = new Intent();
                        intent.putExtra(Constant.INTENT_KEY.ORDER_ING, order);
                        intent.setClass(MainActivity.this, OrderIngActivity.class);
                        startActivityTo(intent);
                    } else if (status == 1) {
                        ttsManager.playText("抢单失败");
                        toast("抢单失败");
                        mSendOrderList.remove(position);
                        mOrderAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
//        mMapView.onCreate(savedInstanceState);
//        AMap = mMapView.getMap();
//        AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }
//        };
//        AMap.setInfoWindowAdapter(infoWindowAdapter);
        //设置默认定位按钮是否显示
//        UiSettings uiSettings = AMap.getUiSettings();
//        uiSettings.setMyLocationButtonEnabled(false);
//        uiSettings.setZoomControlsEnabled(false);
//        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        AMap.setMyLocationEnabled(true);
//        AMap.moveCamera(CameraUpdateFactory.zoomBy(6));
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
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

//    private void moveCenterMarker(LatLng latLng) {
//        mMarkerOptions = new MarkerOptions();
//        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));
//        mMarkerOptions.position(latLng);
//        if (mMarker != null) {
//            mMarker.destroy();
//        }
//        mMarker = AMap.addMarker(mMarkerOptions);
//        AMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//    }


    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位
    }

    @Override
    protected void onDestroy() {
        Log.e("onDestroy ", "onDestroy ");
        clearLocation();
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。
        //调用销毁功能，在应用的合适生命周期需要销毁附近功能
        unregisterReceiver(recevier);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
//        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
//        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.image_adDialog:
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
            case R.id.rela_setting:
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivityTo(intent);
                break;
            case R.id.rela_mine:

                break;
            case R.id.imageView_mine:
                drawer_layout.openDrawer(list_left_drawer);
                break;
            case R.id.rela_shoufache:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                LinearInterpolator lin = new LinearInterpolator();
                animation.setInterpolator(lin);
                ttsManager.stopSpeaking();
                if (isShouFaChe) {
                    ttsManager.startSpeaking();
                    ttsManager.playText("停止接单了！");
                    mTextView_shoufache.setText("接单");
                    mImageView_shoufache.setImageResource(R.mipmap.circle);
                    mImageView_shoufache.clearAnimation();
                    clearLocation();
                } else {
                    getOrderIsIng(true);
                }
                break;
            case R.id.imageView_touXiang://跳转个人信息
                intent.setClass(this, MineActivity.class);
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.USER_INFO);
                drawer_layout.closeDrawer(list_left_drawer);
                break;
            case R.id.rela_all_order://所有订单
                intent.setClass(this, AllOrderActivity.class);
                intent.putExtra(Constant.INTENT_KEY.USER_ID,mUser.getDatas().getId());
                intent.putExtra(Constant.INTENT_KEY.UNFINISHED_ORDER, unfinishedOrder);
                Log.e("MainActivity", "用户ID: --->>" +mUser.getDatas().getId() );
                startActivityForResultTo(intent,Constant.REQUEST_RESULT_CODE.USER_ID);
                break;
//            case R.id.imageView_location:
//                mLocationClient.startLocation();
//                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_RESULT_CODE.USER_INFO && resultCode == Constant.REQUEST_RESULT_CODE.USER_INFO) {
            //用户信息
            String userName = data.getStringExtra(Constant.INTENT_KEY.USER_NAME);
            String userAvatar = data.getStringExtra(Constant.INTENT_KEY.USER_AVATAR);
            Log.e("MainActivity", "用户姓名: --->>" + userName);
            Log.e("MainActivity", "用户头像: --->>" + userAvatar);

            setUserName(userName);
            loadAvatarImg(userAvatar);
        }

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

    /**
     * 清除司机位置
     */
    private void clearLocation() {
        isShouFaChe = false;
        Log.e("clearLocation ", "清除司机" + mId);
        //获取附近实例，并设置要清楚用户的id
        NearbySearch instance = NearbySearch.getInstance(getApplicationContext());
        instance.setUserID("" + mId);
        //调用异步清除用户接口
        instance.clearUserInfoAsyn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart ", "onStart main");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTCODE.SEND_ORDER);
        registerReceiver(recevier, filter);
    }

    @Override
    public void onBackPressed() {
        finish();
//        SysApplication.getInstance().exit();
    }

}
