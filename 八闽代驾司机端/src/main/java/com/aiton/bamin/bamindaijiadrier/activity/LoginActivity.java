package com.aiton.bamin.bamindaijiadrier.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.upgrade.UpgradeUtils;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.HTTPUtils;
import com.aiton.administrator.shane_library.shane.utils.Installation;
import com.aiton.administrator.shane_library.shane.utils.IsMobileNOorPassword;
import com.aiton.administrator.shane_library.shane.utils.NetChecker;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.Sms;
import com.aiton.bamin.bamindaijiadrier.model.User;
import com.aiton.bamin.bamindaijiadrier.model.VersionAndHouTaiIsCanUse;
import com.aiton.bamin.bamindaijiadrier.utils.SmsContent;
import com.android.volley.VolleyError;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends ZjbBaseActivity implements View.OnClickListener {

    private LinearLayout mLinear_sms;
    private LinearLayout mLinear_password;
    private TextView mTextView_switch;
    private boolean isSmsLogin = true;
    private EditText mEditText_phone_sms;
    private EditText mEditText_sms;
    private String mPhone_sms;
    private int[] mI;
    private Runnable mR;
    private String mSuijiMath;
    private TextView mTextView_getSms;
    private String mPhone_sp;
    private EditText mEditText_phone;
    private EditText mEditText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initPermission();
        if(!NetChecker.getInstance(LoginActivity.this).isNetworkOnline()){
            SingleBtnDialog singleBtnDialog = new SingleBtnDialog(LoginActivity.this, "请检查网络", "确认");
            singleBtnDialog.show();
            singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
                @Override
                public void doWhat() {
                    finishTo();
                }
            });
        }
        //个推
        PushManager instance = PushManager.getInstance();
        instance.initialize(this.getApplicationContext());
//        String clientid = instance.getClientid(LoginActivity.this);
//        Log.e("onCreate ", "个推CID-------- "+clientid);
//        Constant.setCHANNELID(clientid);
        init();
    }



    private void checkVersionAndHouTaiIsCanUse() {
        String url = Constant.Url.CHECK_LIVE;
        Map<String, String> map = new HashMap<>();
        HTTPUtils.post(LoginActivity.this, url, map, new VolleyListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setDialogCkeck("服务器正在升级，暂停服务", "确认");
            }

            @Override
            public void onResponse(String s) {
                Log.e("onResponse ", "检查服务器" + s);
                try {
                    VersionAndHouTaiIsCanUse versionAndHouTaiIsCanUse = GsonUtils.parseJSON(s, VersionAndHouTaiIsCanUse.class);
                    if (versionAndHouTaiIsCanUse.getStatus() == 0) {
                        int ableVersion = versionAndHouTaiIsCanUse.getDatas().getAbleVersion();
                        if (versionAndHouTaiIsCanUse.getDatas().isLive()) {
                            if (Constant.ABLEVERSION < ableVersion) {
                                setDialogCkeckAble("当前版本不可用，请更新到最新版本", "确认");
                            } else {
                                checkUpGrade();
                            }
                        } else {
                            setDialogCkeck(versionAndHouTaiIsCanUse.getDatas().getMessage(), "确认");

                        }
                    } else {
                        setDialogCkeck(versionAndHouTaiIsCanUse.getMes(), "确认");
                    }
                } catch (Exception e) {
                    toast("系统出错");
                }
            }
        });
    }


    private void checkUpGrade() {
        UpgradeUtils.checkUpgrade(LoginActivity.this, Constant.Url.UP_GRADE);
    }

    private void setDialogCkeckAble(String messageTxt, String iSeeTxt) {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        TextView message = (TextView) commit_dialog.findViewById(R.id.message);
        Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
        message.setText(messageTxt);
        ISee.setText(iSeeTxt);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
        final android.app.AlertDialog dialog = builder.setView(commit_dialog)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpgradeUtils.checkUpgradeIsAble(LoginActivity.this, Constant.Url.UP_GRADE);
            }
        });
    }

    //dialog提示
    private void setDialogCkeck(String messageTxt, String iSeeTxt) {

        final SingleBtnDialog singleBtnDialog = new SingleBtnDialog(this, messageTxt, iSeeTxt);
        singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
            @Override
            public void doWhat() {
                singleBtnDialog.dismiss();
                finishTo();
            }
        });
        singleBtnDialog.show();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mLinear_sms.setVisibility(View.VISIBLE);
        mLinear_password.setVisibility(View.GONE);
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.textView_addDriver).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);
        mTextView_getSms.setOnClickListener(this);
        mTextView_switch.setOnClickListener(this);
        mEditText_phone_sms.addTextChangedListener(new MySmsTextWatcher());
        mEditText_phone.addTextChangedListener(new MyPasswordTextWatcher());
    }

    @Override
    protected void initSP() {
        ACache aCache = ACache.get(LoginActivity.this);
        User user = (User) aCache.getAsObject(Constant.ACACHE.USER);
        if (user != null) {
            startToMainActivity();
        } else {
            //检查服务器是否存活和当前版本是否可用
            checkVersionAndHouTaiIsCanUse();
        }
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mLinear_sms = (LinearLayout) findViewById(R.id.linear_sms);
        mLinear_password = (LinearLayout) findViewById(R.id.linear_password);
        mTextView_switch = (TextView) findViewById(R.id.textView_switch);
        mEditText_phone_sms = (EditText) findViewById(R.id.editText_phone_sms);
        mEditText_sms = (EditText) findViewById(R.id.editText_sms);
        mTextView_getSms = (TextView) findViewById(R.id.textView_getSms);
        mEditText_phone = (EditText) findViewById(R.id.editText_phone);
        mEditText_password = (EditText) findViewById(R.id.editText_password);
    }

    class MySmsTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 11) {
                mEditText_sms.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class MyPasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 11) {
                mEditText_password.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION.PERMISSION_READ_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SmsContent smsContent = new SmsContent(LoginActivity.this, new Handler(), mEditText_sms);
                // 注册短信变化监听
                this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContent);
                sendSMS();
            } else {
                sendSMS();
            }
        }
        //可在此继续其他操作。
        else if (requestCode ==  Constant.PERMISSION.CALL_PHONE|| requestCode == Constant.PERMISSION.ACCESS_COARSE_LOCATION || requestCode == Constant.PERMISSION.ACCESS_FINE_LOCATION || requestCode == Constant.PERMISSION.WRITE_EXTERNAL_STORAGE || requestCode == Constant.PERMISSION.READ_EXTERNAL_STORAGE || requestCode == Constant.PERMISSION.READ_PHONE_STATE) {
            initPermission();
        }
    }

    private void sendSMS() {
        mPhone_sms = mEditText_phone_sms.getText().toString().trim();
        boolean mobileNO = IsMobileNOorPassword.isMobileNO(mPhone_sms);
        if (mobileNO) {
            mTextView_getSms.setEnabled(false);
            mI = new int[]{60};

            mR = new Runnable() {
                @Override
                public void run() {
                    mTextView_getSms.setText((mI[0]--) + "秒后重发");
                    if (mI[0] == 0) {
                        mTextView_getSms.setEnabled(true);
                        mTextView_getSms.setText("获取验证码");
                        return;
                    } else {
                    }
                    mTextView_getSms.postDelayed(mR, 1000);
                }
            };
            mTextView_getSms.postDelayed(mR, 0);
            getSms();
        } else {
            Toast.makeText(LoginActivity.this, "输入的手机格式有误", Toast.LENGTH_SHORT).show();
            mEditText_phone_sms.setText("");
        }
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSms() {
        mSuijiMath = (int) (Math.random() * 9000 + 1000) + "";
        String url = Constant.Url.GET_SMS;
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhone_sms);
        map.put("code", mSuijiMath + "");
        HTTPUtils.post(LoginActivity.this, url, map, new VolleyListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast("短信发送失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    Sms sms = GsonUtils.parseJSON(s, Sms.class);
                    int status = sms.getStatus();
                    toast(sms.getMes());
                    if (status == 0) {
                        //短信发送成功
                    } else if (status == 1) {
                        //短信发送失败
                    }
                } catch (Exception e) {
                    toast("服务器出错");
                }
            }
        });
    }

    private void login(final String phone) {
        String loginCode = mEditText_sms.getText().toString().trim();
//        if (mSuijiMath.equals(loginCode)) {
            showLoadingDialog("正在登陆……");
            toast("短信验证成功");
            //每次存储唯一标识
            final String DeviceId = Installation.id(LoginActivity.this);
            //向后台服务推送用户短信验证成功，发送手机号----start----//
            //取出channaleID
            String url = Constant.Url.LOGIN;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("phone", phone + "");
            params.put("deviceid", DeviceId + "");
            params.put("key", PushManager.getInstance().getClientid(this));
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("onResponse ", "登录返回结果" + s);
                    try {
                        final User user = GsonUtils.parseJSON(s, User.class);
                        if (user.getStatus() == 0) {
                            //存储手机号和用户id到本地
                            ACache aCache = ACache.get(LoginActivity.this);
                            aCache.put(Constant.ACACHE.USER, user);
                            toast(user.getMes());
                            //友盟统计
                            MobclickAgent.onProfileSignIn(phone);
                            startToMainActivity();
                        } else {
                            toast(user.getMes());
                        }
                    } catch (Exception e) {
                        cancelLoadingDialog();
                        toast("服务器出错");
                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
//        } else {
//            toast("短信验证失败");
//        }
    }

    /**
     * 初始化权限（6.0系统需要）
     */
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.PERMISSION.ACCESS_COARSE_LOCATION);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.PERMISSION.ACCESS_FINE_LOCATION);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.PERMISSION.WRITE_EXTERNAL_STORAGE);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constant.PERMISSION.READ_EXTERNAL_STORAGE);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    Constant.PERMISSION.READ_PHONE_STATE);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    Constant.PERMISSION.RECEIVE_BOOT_COMPLETED);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS},
                    Constant.PERMISSION.RECEIVE_WRITE_SETTINGS);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE},
                    Constant.PERMISSION.RECEIVE_VIBRATE);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.DISABLE_KEYGUARD)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.DISABLE_KEYGUARD},
                    Constant.PERMISSION.RECEIVE_DISABLE_KEYGUARD);//自定义的code
        }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    Constant.PERMISSION.CALL_PHONE);//自定义的code
        }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {
            //申请CALL_PHONE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    Constant.PERMISSION.SYSTEM_ALERT_WINDOW);//自定义的code
        }
    }

    private void startToMainActivity() {
        finish();
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivityTo(intent);
        cancelLoadingDialog();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.textView_getSms:
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_SMS},
                            Constant.PERMISSION.PERMISSION_READ_SMS);
                } else {
                    sendSMS();
                }
                break;
            case R.id.textView_switch:
                isSmsLogin = !isSmsLogin;
                if (isSmsLogin) {
                    mLinear_sms.setVisibility(View.VISIBLE);
                    mLinear_password.setVisibility(View.GONE);
                    mTextView_switch.setText("切换密码登录");
                } else {
                    mLinear_sms.setVisibility(View.GONE);
                    mLinear_password.setVisibility(View.VISIBLE);
                    mTextView_switch.setText("切换快速登录");
                }
                break;
            case R.id.textView_addDriver:
                intent.setClass(LoginActivity.this, AddDriverActivity.class);
                startActivityTo(intent);
                break;
            case R.id.button_login:
                if (isSmsLogin) {
                    mPhone_sms = mEditText_phone_sms.getText().toString().trim();
                    login(mPhone_sms);
                } else {
                    //密码登录
                }
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
