package bamin.com.bamindaijia.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.aiton.administrator.shane_library.shane.widget.AnimCheckBox;
import com.aiton.administrator.shane_library.shane.widget.MyProgressDialog;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.Cost;
import bamin.com.bamindaijia.model.OrderIng;
import bamin.com.bamindaijia.model.OrderStatue;
import bamin.com.bamindaijia.model.Sms;
import bamin.com.bamindaijia.model.User;
import cz.msebera.android.httpclient.Header;

public class OrderIngActivity extends ZjbBaseActivity implements View.OnClickListener {

    private TextView mTextView_driverName;
    private TextView mTextView_driveYear;
    private TextView mTextView_orderNum;
    private TextView mTextView_start;
    private TextView mTextView_end;
    private TextView mTextView_time;
    private TextView mTextView_leftTime;
    private OrderIng mOrderIng;
    private TextView textView_OrderStatue;
    private User mUser;
    private AnimCheckBox mAnimCheckBox01;
    private AnimCheckBox mAnimCheckBox02;
    private AnimCheckBox mAnimCheckBox03;
    private TextView mTextView_cancleOrder01;
    private TextView mTextView_cancleOrder02;
    private TextView mTextView_cancleOrder03;
    private EditText mEditText_reason;
    private MyProgressDialog mProgressDialogReason;
    private Cost mCost;
    private BroadcastReceiver revevier = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.BROADCASTCODE.GETPASSAGER)) {
                String getPassager = intent.getStringExtra(Constant.INTENT_KEY.GETPASSAGER);
                OrderStatue orderStatue = GsonUtils.parseJSON(getPassager, OrderStatue.class);
                Log.e("OrderIngActivity", "onReceive: orderStatue--->>" + orderStatue.getOrderStatus());

                textView_OrderStatue.setText(orderStatue.getOrderStatus());
                mTextView_leftTime.setText("司机已到达");
            } else if (action.equals(Constant.BROADCASTCODE.DRIVER_CHANGE_MILEAGE)) {

                String driverChangeMileage = intent.getStringExtra(Constant.INTENT_KEY.DRIVER_CHANGE_MILEAGE);
                mCost = GsonUtils.parseJSON(driverChangeMileage, Cost.class);
                Intent intent01 = new Intent();
                intent01.putExtra(Constant.INTENT_KEY.COST, mCost);
                intent01.putExtra(Constant.INTENT_KEY.ORDER, mOrderIng);
                intent01.setClass(OrderIngActivity.this, PayActivity.class);
                startActivityTo(intent01);
            } else if (action.equals(Constant.BROADCASTCODE.CHANGE_END)) {

                String changeEnd = intent.getStringExtra(Constant.INTENT_KEY.CHANGE_END);
                Log.e("OrderIngActivity", "司机更改了目的地--->" + changeEnd);

                mTextView_end.setText(changeEnd);
                final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(OrderIngActivity.this, "司机更改了目的地", "确认");
                singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
                    @Override
                    public void doWhat() {
                        singleBtnDialog.dismiss();
                    }
                });
                singleBtnDialog.show();
            }
        }
    };
    private ImageView mImg_phone;
    private Button mBtn_pay;
    private ImageView mImg_order_driverImage;
    private TextView mTv_cancleOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        String pushOrderIng = intent.getStringExtra(Constant.INTENT_KEY.PUSH_ORDER_ING_VALUE);
        Log.e("initData ", "pushOrderIng " + pushOrderIng);
        if (pushOrderIng != null) {
            Log.e("initData ", "进行中的订单：" + pushOrderIng);
            mOrderIng = GsonUtils.parseJSON(pushOrderIng, OrderIng.class);
            setView();
        } else {
            Log.e("initViews ", "进行的订单，进入");
            String url = Constant.Url.GET_ORDER_IS_ING;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("customerId", mUser.getDatas().getId() + "");//上传用户ID，从网络获取订单详情
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        Log.e("onSuccess ", "进行中的订单（网络获取）:" + s);
                        mOrderIng = GsonUtils.parseJSON(s, OrderIng.class);
                        setView();
                        HasCost();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

    }

    private void HasCost() {
        if (mCost == null) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            String url = Constant.Url.ISPAY;
            RequestParams params = new RequestParams();
            params.put("orderId", mOrderIng.getId());
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        Log.e("OrderIngActivity", "onSuccess: --->>" + s);

//                        Message message = GsonUtils.parseJSON(s, Message.class);
//                        String title = message.getTitle();
//                        Log.e("title", title);
//                        String content = message.getContent();
//                        Log.e("content", content);
//                        String replace = content.replace("\'", "\"");
//                        Log.e("replace", replace);
                        Sms sms = GsonUtils.parseJSON(s, Sms.class);
                        String datas = sms.getDatas();
                        String replace = datas.replace("\'", "\"");
                        mCost = GsonUtils.parseJSON(replace, Cost.class);
                        Log.e("OrderIngActivity", "mCost: --->>" + mCost.getMeassage());

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("OrderIngActivity", "下载失败"  );


                }
            });
        }
    }

    private void setView() {
        mTextView_start.setText(mOrderIng.getStartLocation());
        mTextView_end.setText(mOrderIng.getEndLocation());
        mTextView_time.setText("下单时间：" + mOrderIng.getOrderTime());
        if ("已接单".equals(mOrderIng.getOrderStatus())) {
            mTextView_leftTime.setText("司机大概还有" + mOrderIng.getDriverComeTime() + "分钟到达");
        } else if ("服务中".equals(mOrderIng.getOrderStatus())) {
            mTextView_leftTime.setText("司机已到达");
        }
        textView_OrderStatue.setText(mOrderIng.getOrderStatus());
        Log.e("OrderIngActivity", "setView: mOrderIng--->>" + mOrderIng.getOrderStatus());

        mTextView_driverName.setText(mOrderIng.getDriverName());
        mTextView_driveYear.setText("驾龄：5年");
        mTextView_orderNum.setText("接单数：" + mOrderIng.getDriverOrderSum() + "单");
        if (textView_OrderStatue.getText().toString().equals("结算")) {
            mBtn_pay.setVisibility(View.VISIBLE);
            //取消订单，上线后要取消注释
//            mTv_cancleOrder.setVisibility(View.GONE);
        }
        String mImage = mOrderIng.getDriverImage();
        Log.e("checkLogin", "图片URL" + mImage);
        if (mImage != null && !TextUtils.isEmpty(mImage)) {
            ImageLoader.getInstance().clearDiskCache();
            UILUtils.displayImageNoAnim(mImage, mImg_order_driverImage);
        }
    }

    @Override
    protected void setListeners() {
        mTv_cancleOrder.setOnClickListener(this);
        mImg_phone.setOnClickListener(this);
        mBtn_pay.setOnClickListener(this);
    }

    @Override
    protected void initSP() {
        ACache aCache = ACache.get(OrderIngActivity.this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mImg_order_driverImage = (ImageView) findViewById(R.id.img_order_driverImage);
        mTextView_driverName = (TextView) findViewById(R.id.textView_driverName);
        mTextView_driveYear = (TextView) findViewById(R.id.textView_driveYear);
        mTextView_orderNum = (TextView) findViewById(R.id.textView_orderNum);
        mTextView_start = (TextView) findViewById(R.id.textView_start);
        mTextView_end = (TextView) findViewById(R.id.textView_end);
        mTextView_time = (TextView) findViewById(R.id.textView_time);
        mTextView_leftTime = (TextView) findViewById(R.id.textView_leftTime);
        textView_OrderStatue = (TextView) findViewById(R.id.textView_OrderStatue);
        mImg_phone = (ImageView) findViewById(R.id.img_phone);
        mBtn_pay = (Button) findViewById(R.id.btn_end_pay);
        mTv_cancleOrder = (TextView) findViewById(R.id.textView_cancleOrder);

    }

    @Override
    public void onBackPressed() {

        final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "有正在进行的订单，确认退出？", "确认", "取消");
        twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                twoBtnDialog.dismiss();
                SysApplication.getInstance().exit();
            }

            @Override
            public void doCancel() {
                twoBtnDialog.dismiss();
            }
        });
        twoBtnDialog.show();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.rela_cancle01:
                if (mAnimCheckBox01.isChecked()) {
                    mAnimCheckBox01.setChecked(false);
                } else {
                    mAnimCheckBox01.setChecked(true);
                }
                break;
            case R.id.rela_cancle02:
                if (mAnimCheckBox02.isChecked()) {
                    mAnimCheckBox02.setChecked(false);
                } else {
                    mAnimCheckBox02.setChecked(true);
                }
                break;
            case R.id.rela_cancle03:
                if (mAnimCheckBox03.isChecked()) {
                    mAnimCheckBox03.setChecked(false);
                } else {
                    mAnimCheckBox03.setChecked(true);
                }
                break;
            case R.id.button_commit:
                cancleOrder();
                break;
            case R.id.textView_cancleOrder:
                cancleOrderDialog();
                break;
            case R.id.img_phone:
                //拨打电话
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mOrderIng.getDriverPhone()));
                startActivityTo(intent);
                break;
            case R.id.btn_end_pay:
                Intent intent01 = new Intent();
                intent01.putExtra(Constant.INTENT_KEY.COST, mCost);
                intent01.putExtra(Constant.INTENT_KEY.ORDER, mOrderIng);
//              Log.e("OrderIngActivity", "mCost--" + mCost.toString());
                Log.e("OrderIngActivity", "mOrderIng--" + mOrderIng.toString());
                intent01.setClass(OrderIngActivity.this, PayActivity.class);
                startActivityTo(intent01);
                break;
        }
    }

    private void cancleOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderIngActivity.this);
        View cancle_order_dialog = getLayoutInflater().inflate(R.layout.cancle_order_dialog, null);
        mAnimCheckBox01 = (AnimCheckBox) cancle_order_dialog.findViewById(R.id.AnimCheckBox01);
        mAnimCheckBox02 = (AnimCheckBox) cancle_order_dialog.findViewById(R.id.AnimCheckBox02);
        mAnimCheckBox03 = (AnimCheckBox) cancle_order_dialog.findViewById(R.id.AnimCheckBox03);
        mAnimCheckBox01.setChecked(false);
        mAnimCheckBox02.setChecked(false);
        mAnimCheckBox03.setChecked(false);
        mTextView_cancleOrder01 = (TextView) cancle_order_dialog.findViewById(R.id.textView_cancleOrder01);
        mTextView_cancleOrder02 = (TextView) cancle_order_dialog.findViewById(R.id.textView_cancleOrder02);
        mTextView_cancleOrder03 = (TextView) cancle_order_dialog.findViewById(R.id.textView_cancleOrder03);
        mEditText_reason = (EditText) cancle_order_dialog.findViewById(R.id.editText_reason);
        cancle_order_dialog.findViewById(R.id.rela_cancle01).setOnClickListener(this);
        cancle_order_dialog.findViewById(R.id.rela_cancle02).setOnClickListener(this);
        cancle_order_dialog.findViewById(R.id.rela_cancle03).setOnClickListener(this);
        cancle_order_dialog.findViewById(R.id.button_commit).setOnClickListener(this);
        builder.setView(cancle_order_dialog)
                .create()
                .show();
    }

    private void cancleOrder() {
        String cancleReason = "";
        if (mAnimCheckBox01.isChecked()) {
            cancleReason = mTextView_cancleOrder01.getText().toString().trim() + "，";
        }
        if (mAnimCheckBox02.isChecked()) {
            cancleReason = cancleReason + mTextView_cancleOrder02.getText().toString().trim() + "，";
        }
        if (mAnimCheckBox03.isChecked()) {
            cancleReason = cancleReason + mTextView_cancleOrder03.getText().toString().trim() + "，";
        }
        cancleReason = cancleReason + mEditText_reason.getText().toString().trim() + "。";
        if (!mAnimCheckBox01.isChecked() && !mAnimCheckBox02.isChecked()
                && !mAnimCheckBox03.isChecked() && mEditText_reason.getText().toString().trim().equals("")) {
            toast("请说明原因");
        } else {
            String url = Constant.Url.CUSTOMER_CANCEL_ORDER;
            final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            mProgressDialogReason = new MyProgressDialog(this, "提交……");
            mProgressDialogReason.setCancelable(false);
            mProgressDialogReason.show();
            RequestParams params = new RequestParams();
            params.put("id", mOrderIng.getId() + "");
            params.put("cancelCause", cancleReason);
            params.put("customerPhone", mUser.getDatas().getPhone());
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        Log.e("onSuccess ", "取消订单返回值：" + s);
                        toast("取消订单成功");
                        mProgressDialogReason.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(OrderIngActivity.this, MainActivity.class);
                        startActivityTo(intent);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    toast("取消订单失败");
                    mProgressDialogReason.dismiss();
                }
            });

            mProgressDialogReason.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        mProgressDialogReason.dismiss();
                        asyncHttpClient.cancelRequests(OrderIngActivity.this, true);
                    }
                    return false;
                }
            });
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

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intent = new IntentFilter();
        intent.addAction(Constant.BROADCASTCODE.GETPASSAGER);
        intent.addAction(Constant.BROADCASTCODE.DRIVER_CHANGE_MILEAGE);
        intent.addAction(Constant.BROADCASTCODE.CHANGE_END);
        registerReceiver(revevier, intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(revevier);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
