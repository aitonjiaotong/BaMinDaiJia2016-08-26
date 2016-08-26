package com.aiton.bamin.bamindaijiadrier.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.Order;
import com.aiton.bamin.bamindaijiadrier.model.OrderStatue;
import com.aiton.bamin.bamindaijiadrier.model.PaySuccess;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ComfirmCostActivity extends ZjbBaseActivity implements View.OnClickListener {
    private static final String TAG = "ComfirmCostActivity";
    private Order mOrder;
    private TextView mTextView_name;
    private TextView mTextView_time;
    private TextView mTextView_start;
    private TextView mTextView_end;
    private TextView mTextView_howLong;
    private TextView mTextView_price;
    private BroadcastReceiver recevier = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.BROADCASTCODE.PAY_SUCCESS.equals(action)) {
                String sendOrderValue = intent.getStringExtra(Constant.INTENT_KEY.PAY_SUCCESS);
                Log.e("ComfirmCostActivity", "sendOrderValue: --->>" +sendOrderValue );
                PaySuccess  paySuccess = GsonUtils.parseJSON(sendOrderValue, PaySuccess.class);
                Message msg = handler.obtainMessage();
                msg.obj = paySuccess;
                handler.sendMessage(msg);
            }
        }
    };
    private RelativeLayout mRela_wait_pay;
    private RelativeLayout mRela_success_pay;
    private Button mBtn_sure;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PaySuccess paySuccess = (PaySuccess) msg.obj;

            if (paySuccess != null) {
                Log.e("ComfirmCostActivity", "handleMessage: --paySuccess->>" + paySuccess.getPayStatus() );

                if (paySuccess.getPayStatus().equals("乘客支付完成")) {
                    mRela_wait_pay.setVisibility(View.GONE);
                    mRela_success_pay.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_cost);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTextView_name.setText(mOrder.getDatas().getCustomerName());
        mTextView_time.setText("下单时间：" + mOrder.getDatas().getOrderDate());
        mTextView_start.setText(mOrder.getDatas().getStartLocation());
        mTextView_end.setText(mOrder.getDatas().getEndLocation());
        mTextView_howLong.setText(mOrder.getDatas().getChangeMileage() + "");
        mTextView_price.setText(mOrder.getDatas().getCost() + "");
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.button_change).setOnClickListener(this);
        findViewById(R.id.img_close).setOnClickListener(this);
        mBtn_sure.setOnClickListener(this);
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mOrder = (Order) intent.getSerializableExtra(Constant.INTENT_KEY.COMFIRM_COST);
    }

    @Override
    protected void findID() {
        mTextView_name = (TextView) findViewById(R.id.textView_name);
        mTextView_time = (TextView) findViewById(R.id.textView_time);
        mTextView_start = (TextView) findViewById(R.id.textView_start);
        mTextView_end = (TextView) findViewById(R.id.textView_end);
        mTextView_howLong = (TextView) findViewById(R.id.textView_howLong);
        mTextView_price = (TextView) findViewById(R.id.textView_price);
        mRela_wait_pay = (RelativeLayout) findViewById(R.id.rela_wait_pay);
        mRela_success_pay = (RelativeLayout) findViewById(R.id.rela_success_pay);
        mBtn_sure = (Button) findViewById(R.id.button_comfrim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTCODE.PAY_SUCCESS);
        registerReceiver(recevier, filter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_comfrim:
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("orderId", mOrder.getDatas().getId());
                String url = Constant.Url.DRIVER_CONFIRM_ORDER;
                asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        Log.e("ComfirmCostActivity", "确认:" + s);

                        try {
                            OrderStatue orderStatue = GsonUtils.parseJSON(s, OrderStatue.class);
                            Toast.makeText(ComfirmCostActivity.this, "" + orderStatue.getMes(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

                mBtn_sure.setVisibility(View.GONE);
                mRela_wait_pay.setVisibility(View.VISIBLE);
                //如果乘客支付成功，显示提示信息

                break;
            case R.id.button_change:
                changeMileageDialog();
                break;
            case R.id.img_close:
                startActivityTo(new Intent(this,MainActivity.class));
                break;
        }
    }

    private void changeMileageDialog() {
        final Dialog alertDialogChangeMileage = new Dialog(this,R.style.dialog);
        View change_mileage = getLayoutInflater().inflate(R.layout.change_mileage, null);
        alertDialogChangeMileage.setContentView(change_mileage);
        final EditText editText_mileage = (EditText) change_mileage.findViewById(R.id.editText_mileage);
        change_mileage.findViewById(R.id.button_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogChangeMileage.dismiss();
            }
        });
        change_mileage.findViewById(R.id.button_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                String url = Constant.Url.DRIVER_CHANGE_MILEAGE;
                params.put("orderId", mOrder.getDatas().getId());
                params.put("mileage", editText_mileage.getText().toString().trim());
                asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        Log.e("onSuccess ", "更改公里数的返回值" + s);
                        try {
                            mOrder = GsonUtils.parseJSON(s, Order.class);
                            mTextView_howLong.setText(editText_mileage.getText().toString().trim());
                            mTextView_price.setText(mOrder.getDatas().getCost() + "");
                            alertDialogChangeMileage.dismiss();
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
        Window dialogWindow = alertDialogChangeMileage.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = ComfirmCostActivity.this.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        alertDialogChangeMileage.show();
    }

    @Override
    public void onBackPressed() {
        SysApplication.getInstance().exit();
        startActivityTo(new Intent(this,MainActivity.class));
    }
}
