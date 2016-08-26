package bamin.com.bamindaijia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.Zalipay.PayResult;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.Cost;
import bamin.com.bamindaijia.model.OrderIng;
import bamin.com.bamindaijia.model.Sign;
import cz.msebera.android.httpclient.Header;

public class PayActivity extends ZjbBaseActivity implements View.OnClickListener {

    private Cost mCost;
    private OrderIng mOrder;
    private TextView mTextView_start;
    private TextView mTextView_end;
    private TextView mTextView_time;
    private TextView mTextView_mileage;
    private TextView mTextView_cost;
    private TextView mTextView_mes;
    private RelativeLayout mRela_mes;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private String serial;

    /**
     * 支付宝支付动作完成后的回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息\
                    String[] split = resultInfo.split("&");
//                    String[] split1 = split[2].split("\"");
//                    serial = split1[1];
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //支付成功向金点通发送确认订单
                        paySucces();
//                        setDialog01("支付成功", "确认");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            setFailDialog("支付结果确认中", "确认");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            cancleOrder();
                            setFailDialog("支付失败", "确认");
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };
    private RadioGroup mRadio_pay_mode;
    private RadioButton mRadioBtn_weixin;

    private void paySucces() {
        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
        //发透传给司机，弹出dialog
        setSuccessDialog("支付成功", "确认");
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//
//        RequestParams params = new RequestParams();
//        params.put("orderId",mCost.getOrderId());
//        params.put("cost",mCost.getCost());
//        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String s = new String(responseBody);
//                try {
//
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTextView_start.setText(mOrder.getStartLocation());
        mTextView_end.setText(mOrder.getEndLocation());
        mTextView_time.setText("下单时间：" + mOrder.getOrderTime());
        mTextView_mileage.setText(mCost.getChangeMileage() + "");
        mTextView_cost.setText((mCost.getCost()) + "");
        mTextView_mes.setText(mCost.getMeassage());
//        mRadioBtn_weixin.setChecked(true);

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.button_pay).setOnClickListener(this);
//        mRadio_pay_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int checkedRadioButtonId = group.getCheckedRadioButtonId();
//                Log.e("PayActivity", "onCheckedChanged: --->>" + checkedRadioButtonId);
//
//                RadioButton radioButton  = (RadioButton) findViewById(checkedRadioButtonId);
//                Log.e("PayActivity", "onCheckedChanged: --->>" +checkedId );
//                radioButton.setChecked(true);
//            }
//        });

    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mCost = (Cost) intent.getSerializableExtra(Constant.INTENT_KEY.COST);
        mOrder = (OrderIng) intent.getSerializableExtra(Constant.INTENT_KEY.ORDER);
        if ("".equals(mCost.getMeassage())) {
            mRela_mes.setVisibility(View.GONE);
        }
    }

    @Override
    protected void findID() {
        mTextView_start = (TextView) findViewById(R.id.textView_start);
        mTextView_end = (TextView) findViewById(R.id.textView_end);
        mTextView_time = (TextView) findViewById(R.id.textView_time);
        mTextView_mileage = (TextView) findViewById(R.id.textView_mileage);
        mTextView_cost = (TextView) findViewById(R.id.textView_cost);
        mTextView_mes = (TextView) findViewById(R.id.textView_mes);
        mRela_mes = (RelativeLayout) findViewById(R.id.rela_mes);
        mRadio_pay_mode = (RadioGroup) findViewById(R.id.radio_pay_mode);
//        mRadioBtn_weixin = (RadioButton) findViewById(R.id.radiobtn_pay_weixin);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //支付失败dialog提示
    private void setFailDialog(String messageTxt, String iSeeTxt) {

        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(this, messageTxt, iSeeTxt);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();

            }
        });
        singleBtnDialog.show();
    }

    //支付成功dialog提示
    private void setSuccessDialog(String messageTxt, String iSeeTxt) {

        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(this, messageTxt, iSeeTxt);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();
                startActivity(new Intent(PayActivity.this, MainActivity.class));

            }
        });
        singleBtnDialog.show();
    }

    private void getSign() {
        String url = Constant.Url.GETSIGN;

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("out_trade_no", mCost.getOrderId());
        params.put("subject", mOrder.getStartLocation() + "-" + mOrder.getEndLocation());
        params.put("total_fee", (mCost.getCost() + mCost.getFee()) + "");
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);

                Sign sign = GsonUtils.parseJSON(s, Sign.class);
                final String body = sign.getBody();
                Log.e("onResponse", "返回值" + body);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(PayActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(body, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_pay:
                getSign();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        SysApplication.getInstance().exit();
    }
}
