package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.TTSController;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.CancleOrder;
import com.aiton.bamin.bamindaijiadrier.model.Order;
import com.aiton.bamin.bamindaijiadrier.model.OrderStatue;
import com.aiton.bamin.bamindaijiadrier.model.User;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class OrderIngActivity extends ZjbBaseActivity implements RouteSearch.OnRouteSearchListener, AMapNaviListener, View.OnClickListener {

    private Order mOrder;
    private MapView mMap;
    private AMap mAMap;
    private TTSController ttsManager;
    private AMapNavi aMapNavi;
    private RouteOverLay mRouteOverLay;
    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private NaviLatLng mStart;
    private NaviLatLng mEnd;
    private AMapLocationClient mLocationClient;
    private LatLng mLocationLatLng;
    private String[] orderStatue01 = {
            "已接单",
            "服务中"
    };
    private String[] orderStatue02 = {
            "接到乘客(开始服务)",
            "到达目的地"
    };


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
                    mLocationLatLng = new LatLng(latitude,
                            longitude);
                    moveCenterMarker(mLocationLatLng);
                    initRoutInfo();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };
    private AMapLocationClientOption mLocationOption;
    private MarkerOptions mMarkerOptions;
    private Marker mMarker;
    private NaviLatLng mDriverNaviLatLng;
    private TextView mTextView_name;
    private TextView mTextView_time;
    private TextView mTextView_start;
    private TextView mTextView_end;
    private Button mButton_server;
    private ImageView mImg_phone;
    private boolean isChangeEnd = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if (Constant.BROADCASTCODE.CANCEL_ORDER.equals(action)) {
                String cancleOrder = intent.getStringExtra(Constant.INTENT_KEY.CANCELORDER);
                CancleOrder cancleOrder1 = GsonUtils.parseJSON(cancleOrder, CancleOrder.class);

                final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(OrderIngActivity.this, "乘客取消订单", "确认");
                singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
                    @Override
                    public void doWhat() {
                        singleBtnDialog.dismiss();
                        startActivityTo(new Intent(OrderIngActivity.this, MainActivity.class));

                    }
                });
                singleBtnDialog.setCancelable(false);
                singleBtnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            singleBtnDialog.dismiss();
                            startActivityTo(new Intent(OrderIngActivity.this, MainActivity.class));
                        }
                        return false;
                    }
                });
                singleBtnDialog.show();
            }
        }
    };
    private Button mBtn_change_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮

        ttsManager = TTSController.getInstance(this);
        ttsManager.init();
        aMapNavi = AMapNavi.getInstance(this);
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.addAMapNaviListener(ttsManager);
        aMapNavi.setEmulatorNaviSpeed(150);//设置模拟导航的速度
        mMap = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMap.onCreate(savedInstanceState);
        mAMap = mMap.getMap();
        mRouteOverLay = new RouteOverLay(mAMap, null, getApplicationContext());

        //设置默认定位按钮是否显示
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);//设置了地图是否允许显示缩放按钮

        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMyLocationEnabled(true);
        //返回一个CameraUpdate 对象，改变了当前可视区域的zoom 级别
        mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
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
        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        showLoadingDialog("正在定位……");
        mLocationClient.startLocation();
        init();
    }

    private void moveCenterMarker(LatLng latLng) {
        mMarkerOptions = new MarkerOptions();
        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));
        mMarkerOptions.position(latLng);
        if (mMarker != null) {
            mMarker.destroy();
        }
        mMarker = mAMap.addMarker(mMarkerOptions);
        //返回一个CameraUpdate对象，只改变地图可视区域中心点，地图缩放级别不变
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    /**
     * 计算驾车路线
     */
    private void calculateDriveRoute() {
        mStartPoints.clear();
        mEndPoints.clear();
        mStartPoints.add(mStart);
        mEndPoints.add(mEnd);
        moveCenterMarker(new LatLng(mStart.getLatitude(), mStart.getLongitude()));
        boolean isSuccess = aMapNavi.calculateDriveRoute(mStartPoints,
                mEndPoints, null, PathPlanningStrategy.DRIVING_DEFAULT);
        if (!isSuccess) {
            toast("路线计算失败,检查参数情况");
        }
    }

    /**
     * 计算步行路线
     */
    private void calculateFootRoute() {
        boolean isSuccess = aMapNavi.calculateWalkRoute(mDriverNaviLatLng, mStart);
        if (!isSuccess) {
            toast("路线计算失败,检查参数情况");
        }
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OrderIngActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMap() {
        mTextView_name.setText(mOrder.getDatas().getCustomerName());
        mTextView_time.setText("下单时间：" + mOrder.getDatas().getOrderDate());
        mTextView_start.setText(mOrder.getDatas().getStartLocation());
        mTextView_end.setText(mOrder.getDatas().getEndLocation());
        if (mOrder.getDatas().getOrderStatus().equals(orderStatue01[0])) {
            mButton_server.setText(orderStatue02[0]);
        } else if (mOrder.getDatas().getOrderStatus().equals(orderStatue01[1])) {
            mButton_server.setText(orderStatue02[1]);
            mBtn_change_end.setVisibility(View.VISIBLE);
        }
        String startCoordinate = mOrder.getDatas().getStartCoordinate();
        String endCoordinate = mOrder.getDatas().getEndCoordinate();
        String[] startStr = startCoordinate.split(",");
        double latStart = Double.parseDouble(startStr[1]);
        double lngStart = Double.parseDouble(startStr[0]);
        String[] endStr = endCoordinate.split(",");
        double latEnd = Double.parseDouble(endStr[1]);
        double lngEnd = Double.parseDouble(endStr[0]);
        mStart = new NaviLatLng(latStart, lngStart);
        mEnd = new NaviLatLng(latEnd, lngEnd);
        mDriverNaviLatLng = new NaviLatLng(mLocationLatLng.latitude, mLocationLatLng.longitude);
//        mStart = new NaviLatLng(39.989614, 116.481763);
//        mEnd = new NaviLatLng(39.983456, 116.3154950);
        if (isChangeEnd) {//如果改变地址后就使用
            calculateDriveRoute();
        } else {
            calculateFootRoute();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_RESULT_CODE.CHANGE_END && resultCode == Constant.REQUEST_RESULT_CODE.CHANGE_END) {
            mOrder = (Order) data.getSerializableExtra(Constant.INTENT_KEY.ORDER_ING);
            isChangeEnd = true;
            initMap();
            Log.e("OrderIngActivity", "改变目的地成功");

        }
    }

    private void initRoutInfo() {
        Intent intent = getIntent();
        mOrder = (Order) intent.getSerializableExtra(Constant.INTENT_KEY.ORDER_ING);
        ACache aCache = ACache.get(OrderIngActivity.this);
        User user = (User) aCache.getAsObject(Constant.ACACHE.USER);
        if (mOrder == null) {
            String url = Constant.Url.DRIVER_GET_ORDER_IS_ING;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("driverId", user.getDatas().getId());
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("onSuccess ", "司机获取正在进行的订单：" + s);
                    try {
                        mOrder = GsonUtils.parseJSON(s, Order.class);
                        if ("服务完成".equals(mOrder.getDatas().getOrderStatus())) {
                            startToComfirCostActivity(mOrder);
                        } else {
                            initMap();
                        }
                    } catch (Exception e) {
                        Log.e("onSuccess ", "解析失败");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("onFailure ", "司机获取正在进行的订单失败");
                }
            });
        } else {
            initMap();
        }
    }

    @Override
    protected void initViews() {


    }

    @Override
    protected void setListeners() {
        findViewById(R.id.button_daoHang).setOnClickListener(this);
        mBtn_change_end.setOnClickListener(this);
        mButton_server.setOnClickListener(this);
        mImg_phone.setOnClickListener(this);
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mTextView_name = (TextView) findViewById(R.id.textView_name);
        mTextView_time = (TextView) findViewById(R.id.textView_time);
        mTextView_start = (TextView) findViewById(R.id.textView_start);
        mTextView_end = (TextView) findViewById(R.id.textView_end);
        mButton_server = (Button) findViewById(R.id.button_server);
        mImg_phone = (ImageView) findViewById(R.id.img_phone);
        mBtn_change_end = (Button) findViewById(R.id.button_change_end);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMap.onDestroy();
        aMapNavi.destroy();
        ttsManager.destroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMap.onResume();
        mStartPoints.add(mStart);
        mEndPoints.add(mEnd);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMap.onSaveInstanceState(outState);
    }


    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }
        // 获取路径规划线路，显示到地图上
        mRouteOverLay.setAMapNaviPath(naviPath);
        mRouteOverLay.addToMap();
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_server:
                if (orderStatue02[0].equals(mButton_server.getText().toString().trim())) {

                    final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "确认接到乘客吗？", "确认", "取消");
                    twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            getPassager();
                            twoBtnDialog.dismiss();
                        }

                        @Override
                        public void doCancel() {
                            twoBtnDialog.dismiss();
                        }
                    });
                    twoBtnDialog.show();
                } else if (orderStatue02[1].equals(mButton_server.getText().toString().trim())) {
                    Log.e("onClick ", "服务中……");

                    final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "确认到达目的地吗？", "确认", "取消");
                    twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            arriveDestination();
                            twoBtnDialog.dismiss();
                        }

                        @Override
                        public void doCancel() {
                            twoBtnDialog.dismiss();
                        }
                    });
                    twoBtnDialog.show();
                }
                break;
            case R.id.button_daoHang:
                intent.putExtra(Constant.INTENT_KEY.ENDLATLNG, mEnd);
                intent.setClass(OrderIngActivity.this, GPSNaviActivity.class);
                startActivityTo(intent);
                break;

            case R.id.img_phone:
                //拨打电话
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mOrder.getDatas().getCustomerPhone()));
                startActivityTo(intent);
                break;
            case R.id.button_change_end:
                Order.DatasEntity datas = mOrder.getDatas();
                intent.setClass(this, ChangeEndActivity.class);


                intent.putExtra(Constant.INTENT_KEY.ORDER_ID, mOrder.getDatas().getId());

                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.CHANGE_END);
                break;
        }
    }

    /**
     * 上传订单ID，返回成功则开始服务
     */
    private void getPassager() {
        String url = Constant.Url.ORDER_START;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("orderId", mOrder.getDatas().getId());
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("onSuccess ", "接到乘客订单返回状态返回：" + s);
                try {
                    OrderStatue orderStatue = GsonUtils.parseJSON(s, OrderStatue.class);
                    if (orderStatue.getMes().equals("服务开始")) {
                        mButton_server.setText(orderStatue02[1]);
                        mBtn_change_end.setVisibility(View.VISIBLE);
                        calculateDriveRoute();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTCODE.CANCEL_ORDER);
        registerReceiver(receiver, filter);
    }


    private void arriveDestination() {
        String url = Constant.Url.ORDER_COMPLETE;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("orderId", mOrder.getDatas().getId());
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    Order order = GsonUtils.parseJSON(s, Order.class);
                    if (order.getStatus() == 0) {
                        startToComfirCostActivity(order);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void startToComfirCostActivity(Order order) {
        Intent intent = new Intent();
        intent.putExtra(Constant.INTENT_KEY.COMFIRM_COST, order);
        intent.setClass(OrderIngActivity.this, ComfirmCostActivity.class);
        startActivityTo(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SysApplication.getInstance().exit();
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        toast("路径规划出错" + i);

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }


}
