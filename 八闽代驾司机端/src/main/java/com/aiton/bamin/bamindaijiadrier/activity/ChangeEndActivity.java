package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.widget.MyProgressDialog;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.Order;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ChangeEndActivity extends ZjbBaseActivity implements View.OnClickListener {

    private View mSite_head;
    private EditText mEditText_where;
    private TextView mTextView_city;
    private ListView mListView_search;
    private List<Tip> listSearch = new ArrayList<>();
    private Inputtips mInputtips;
    private MySearchAdapter mAdapterSearch;
    private String locationCity;
    private MyProgressDialog mProgressDialog;
    private String mCityCode;

    private String locationAddress;
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
                    locationCity = amapLocation.getCity();
                    locationAddress = amapLocation.getAddress();
                    locationPoint = "" + longitude + "," + latitude;
                    mTextView_city.setText(locationCity);
                    Log.e("ChangeEndActivity", "定位城市: --->>" + locationCity);
                    Log.e("ChangeEndActivity", "定位地址: --->>" + locationAddress);
                    Log.e("ChangeEndActivity", "定位经纬度: --->>" + locationPoint);


                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private String locationPoint;
    private String endName;
    private int mOrder_id;
    private String endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_com_address);
        init();
        initMap();
    }
    private void setProgressDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new MyProgressDialog(this,text);
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
    @Override
    protected void initData() {

    }
    private void initMap(){
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
    @Override
    protected void initViews() {
        setProgressDialog("正在定位……");

        mAdapterSearch = new MySearchAdapter();
        mListView_search.setAdapter(mAdapterSearch);
    }

    @Override
    protected void setListeners() {
        mEditText_where.addTextChangedListener(new MyTextWatcher());
        mInputtips = new Inputtips(this, new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                listSearch.addAll(list);
                mAdapterSearch.notifyDataSetChanged();
                for (int j = 0; j < list.size(); j++) {
                    Log.e("onGetInputtips", "提示返回结果" + list.get(j).getName());
                }
            }
        });

        findViewById(R.id.textView_cancle).setOnClickListener(this);
        mListView_search.setOnItemClickListener(new MySearchOnItemClickListener());
    }
    class MySearchOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Tip tip = listSearch.get(position);
            endName = tip.getName();
            LatLonPoint point = tip.getPoint();
            double latitude = point.getLatitude();
            double longitude = point.getLongitude();
            endPoint = "" + longitude +"," + latitude;
            Log.e("ChangeEndActivity", "目的地: --->>" + endName);
            Log.e("ChangeEndActivity", "目的地坐标: --->>" + endPoint);


            pushOrder();




        }
    }

    private void pushOrder() {
        String url = Constant.Url.CHANGE_END;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id",mOrder_id);
//        params.put("", locationAddress);
        params.put("changeCoordinate",locationPoint);
        params.put("endLocation",endName);
        params.put("endCoordinate",endPoint);

        asyncHttpClient.post(url,params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("ChangeEndActivity", "onSuccess: --->>" +s );

                try {
                    Order order = GsonUtils.parseJSON(s, Order.class);
                    if(order.getMes().equals("更改目的地成功")){
                        Intent intent = new Intent();
                        intent.putExtra(Constant.INTENT_KEY.ORDER_ING,order);
                        setResult(Constant.REQUEST_RESULT_CODE.CHANGE_END,intent);

                        finishTo();
                    }else{
                        Toast.makeText(ChangeEndActivity.this, "更改目的地失败", Toast.LENGTH_LONG).show();
                    }


                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ChangeEndActivity", "失败" );

            }
        });
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mOrder_id = intent.getIntExtra(Constant.INTENT_KEY.ORDER_ID, 0);
        Log.e("ChangeEndActivity", "订单ID: --->>" + mOrder_id);

    }

    @Override
    protected void findID() {
        mEditText_where = (EditText) findViewById(R.id.editText_where);
        mTextView_city = (TextView) findViewById(R.id.textView_city);
        mListView_search = (ListView) findViewById(R.id.listView_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_cancle:
                finishTo();
                break;
        }
    }
    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("onTextChanged", "" + s);
            try {
                listSearch.clear();
                mAdapterSearch.notifyDataSetChanged();
                if (!"".equals(s)) {
                    mInputtips.requestInputtips(s + "", "" + mCityCode);
                }
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    class MySearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listSearch.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.site_item, null);
            TextView textView_poi = (TextView) inflate.findViewById(R.id.textView_poi);
            TextView textView_detail = (TextView) inflate.findViewById(R.id.textView_detail);
            textView_poi.setText(listSearch.get(position).getName());
            textView_detail.setText(listSearch.get(position).getDistrict());
            return inflate;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。

    }
}
