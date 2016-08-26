package com.aiton.administrator.shane_library.shane;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.KeyEvent;

import com.aiton.administrator.shane_library.R;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.AppLog;
import com.aiton.administrator.shane_library.shane.utils.NetChecker;
import com.aiton.administrator.shane_library.shane.widget.MyCarDialog;
import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;


public abstract class ZjbBaseActivity extends Activity {

    //    private ProgressDialog mProgressDialog;
    private MyCarDialog mMyCarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 14) {
            setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
            //禁止横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void init() {
        //添加当前界面到容器中
        SysApplication.getInstance().addActivity(this);
        initSP();
        initIntent();
        initData();
        findID();
        initViews();
        setListeners();
    }

    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onPageStart(getRunningActivityName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getRunningActivityName());
        MobclickAgent.onPause(this);
    }

    private String getRunningActivityName() {
        String contextString = this.toString();
        try {
            return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        } catch (Exception e) {
        }
        return "";
    }

    public void showLoadingDialog(String text) {
        if (mMyCarDialog == null) {
            mMyCarDialog = new MyCarDialog(this);
            mMyCarDialog.show();
            mMyCarDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        finishTo();
                    }
                    return false;
                }
            });
        }
    }

    public void cancelLoadingDialog() {
        if (mMyCarDialog != null && mMyCarDialog.isShowing()) {
            mMyCarDialog.dismiss();
            mMyCarDialog = null;
        }
    }

    protected abstract void initViews();

    protected abstract void setListeners();

    protected abstract void initSP();

    protected abstract void initIntent();

    protected abstract void findID();

    /**
     * 判断网络是否连接
     *
     * @return true or false
     */
    protected boolean isNetAvailable() {
        NetChecker nc = NetChecker.getInstance(this.getApplicationContext());
        if (nc.isNetworkOnline()) {
            return true;
        } else {
            return false;
        }
    }

    private void setUncaughtExceotion() {
        if (AppLog.DEBUG) {
            final UncaughtExceptionHandler subclass = Thread.currentThread().getUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                    Log.getStackTraceString(paramThrowable);
                    AppLog.LogD(paramThrowable.getMessage());
                    AppLog.LogE("uncaughtException", paramThrowable.getMessage());
                    subclass.uncaughtException(paramThread, paramThrowable);
                }
            });
        }
    }


    protected void sendSms(String mobile, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        smsManager.sendTextMessage(mobile, null, msg, null, null);
    }

    protected void startRegActivity() {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
    }

    public void startActivityTo(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);//放大淡出效果
    }

    public void startActivityForResultTo(Intent intent, int result) {
        startActivityForResult(intent, result);
        overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);//放大淡出效果
    }

    public void finishTo() {
        finish();
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
    }
}
