package bamin.com.bamindaijia.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.igexin.sdk.PushManager;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.User;

public class WelcomeActivity extends ZjbBaseActivity {

    private User mUser;
//    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
//    //开启悬浮窗的Service
//    Intent floatWinIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initPermission();
        PushManager instance = PushManager.getInstance();
        instance.initialize(this.getApplicationContext());
        String clientid = instance.getClientid(WelcomeActivity.this);
        Log.e("onCreate ", "个推CID--------" + clientid);

        init();
        toMainActivity();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initSP() {
        ACache aCache = ACache.get(WelcomeActivity.this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {

    }

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
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请CALL_PHONE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    Constant.PERMISSION.CALL_PHONE);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {
            //申请CALL_PHONE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    Constant.PERMISSION.SYSTEM_ALERT_WINDOW);//自定义的code
        }

    }


//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //可在此继续其他操作。
//        if (requestCode == Constant.PERMISSION.CALL_PHONE || requestCode == Constant.PERMISSION.RECEIVE_BOOT_COMPLETED || requestCode == Constant.PERMISSION.ACCESS_COARSE_LOCATION || requestCode == Constant.PERMISSION.ACCESS_FINE_LOCATION || requestCode == Constant.PERMISSION.WRITE_EXTERNAL_STORAGE || requestCode == Constant.PERMISSION.READ_EXTERNAL_STORAGE || requestCode == Constant.PERMISSION.READ_PHONE_STATE) {
//            initPermission();
//        }
//        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
//            if (!Settings.canDrawOverlays(this)) {
//                Toast.makeText(WelcomeActivity.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(WelcomeActivity.this, "权限授予成功！", Toast.LENGTH_SHORT).show();
//                //启动FxService
//                startService(floatWinIntent);
//            }
//
//        }
//    }





    private void toMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivityTo(intent);
                finish();
            }
        }, 2000);
    }

}
