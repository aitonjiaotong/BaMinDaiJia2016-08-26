package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.AllOrder;
import com.aiton.bamin.bamindaijiadrier.model.Order;
import com.aiton.bamin.bamindaijiadrier.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class AllOrderActivity extends ZjbBaseActivity implements View.OnClickListener {

    private RelativeLayout mRela_order_null;
    private ListView mList_all_order;
    private MyOrderAdapter myOrderAdapter;
    private User mUser;
    private List<AllOrder.DatasBean> mDatas = new ArrayList<>();
    private int userID;
    private View listHead;
    private TextView mTv_time;
    private TextView mTv_state;
    private TextView mTv_start;
    private TextView mTv_end;
    private TextView mTv_price;
    private boolean unfinished_order;
    private Order mOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);
        init();
    }

    @Override
    protected void initData() {
        String url = Constant.Url.ALL_ORDER;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("driverId", mUser.getDatas().getId());
        params.put("driverId", userID);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    AllOrder allOrder = GsonUtils.parseJSON(s, AllOrder.class);
                    List<AllOrder.DatasBean> datas = allOrder.getDatas();
                    mDatas.addAll(datas);
                    Log.e("AllOrderActivity", "mDatas: --->>" + mDatas.size());
                    if (mDatas.size() > 0) {
                        mList_all_order.setVisibility(View.VISIBLE);
                        mRela_order_null.setVisibility(View.GONE);
                    } else {
                        mRela_order_null.setVisibility(View.VISIBLE);
                        mList_all_order.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("AllOrderActivity", "下载订单失败");
            }
        });

        if (unfinished_order) {
            String url1 = Constant.Url.DRIVER_GET_ORDER_IS_ING;
            RequestParams params1 = new RequestParams();
            params1.put("driverId", userID);
            asyncHttpClient.post(url1, params1, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("onSuccess ", "司机获取正在进行的订单：" + s);
                    try {
                        mOrder = GsonUtils.parseJSON(s, Order.class);
                        Order.DatasEntity datas = mOrder.getDatas();
                        mTv_time.setText("下单时间：" + datas.getOrderTime());
                        mTv_state.setText("订单状态：待付款");
                        mTv_start.setText(datas.getStartLocation());
                        mTv_end.setText(datas.getEndLocation());
                        mTv_price.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("onSuccess ", "解析失败");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("onFailure ", "司机获取正在进行的订单失败");
                }
            });
        }
    }

    @Override
    protected void initViews() {

        if (unfinished_order) {
            mList_all_order.addHeaderView(listHead);
            mList_all_order.setVisibility(View.VISIBLE);
            mRela_order_null.setVisibility(View.GONE);
        }

        myOrderAdapter = new MyOrderAdapter();
        mList_all_order.setAdapter(myOrderAdapter);
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        mList_all_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (unfinished_order && position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(AllOrderActivity.this, OrderIngActivity.class);
                    startActivityTo(intent);
                }

            }
        });
    }

    @Override
    protected void initSP() {

        ACache aCache = ACache.get(this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        userID = intent.getIntExtra(Constant.INTENT_KEY.USER_ID, 0);
        unfinished_order = intent.getBooleanExtra(Constant.INTENT_KEY.UNFINISHED_ORDER, false);

        Log.e("AllOrderActivity", "用户ID: --->>" + userID);
        Log.e("AllOrderActivity", "是否有未完成的订单: --->>" + unfinished_order);

    }

    @Override
    protected void findID() {
        mRela_order_null = (RelativeLayout) findViewById(R.id.rela_order_null);
        mList_all_order = (ListView) findViewById(R.id.list_all_order);

        listHead = getLayoutInflater().inflate(R.layout.item_order_info, null);
        mTv_time = (TextView) listHead.findViewById(R.id.textView_time);
        mTv_state = (TextView) listHead.findViewById(R.id.textView_OrderState);
        mTv_start = (TextView) listHead.findViewById(R.id.textView_start);
        mTv_end = (TextView) listHead.findViewById(R.id.textView_end);
        mTv_price = (TextView) listHead.findViewById(R.id.textView_price);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finishTo();
                break;
            default:
                break;
        }
    }

    class MyOrderAdapter extends BaseAdapter {
        class ViewHolder {
            TextView mTv_time;
            TextView mTv_state;
            TextView mTv_start;
            TextView mTv_end;
            TextView mTv_price;
        }

        @Override
        public int getCount() {
            return mDatas.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_order_info, null);
                holder.mTv_time = (TextView) convertView.findViewById(R.id.textView_time);
                holder.mTv_state = (TextView) convertView.findViewById(R.id.textView_OrderState);
                holder.mTv_start = (TextView) convertView.findViewById(R.id.textView_start);
                holder.mTv_end = (TextView) convertView.findViewById(R.id.textView_end);
                holder.mTv_price = (TextView) convertView.findViewById(R.id.textView_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AllOrder.DatasBean datasBean = mDatas.get(position);
            holder.mTv_time.setText("下单时间：" + datasBean.getOrderDate());
            holder.mTv_state.setText("订单状态：" + datasBean.getOrderStatus());
            holder.mTv_start.setText(datasBean.getStartLocation());
            holder.mTv_end.setText(datasBean.getEndLocation());
            if (datasBean.getFinalPrice() != null) {
                holder.mTv_price.setVisibility(View.VISIBLE);
                holder.mTv_price.setText("" + datasBean.getCost() + "元");
            } else {
                holder.mTv_price.setVisibility(View.GONE);
            }


            return convertView;
        }
    }
}
