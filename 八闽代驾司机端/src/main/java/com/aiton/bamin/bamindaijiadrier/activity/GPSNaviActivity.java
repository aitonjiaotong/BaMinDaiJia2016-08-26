package com.aiton.bamin.bamindaijiadrier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.NaviLatLng;

public class GPSNaviActivity extends Activity implements AMapNaviViewListener {

    private NaviLatLng mEndLatLng;
    private AMapNaviView mAmapAMapNaviView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gpsnavi);
        mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.map);
        mAmapAMapNaviView.onCreate(savedInstanceState);
        // 设置导航界面监听
        mAmapAMapNaviView.setAMapNaviViewListener(this);
//        AMapNaviViewOptions options = mAmapAMapNaviView.getViewOptions();
//        options.setLayoutVisible(false);
//        mAmapAMapNaviView.setViewOptions(options);
        mAmapAMapNaviView.setNaviMode(AMapNaviView.CAR_UP_MODE);
        // 设置模拟速度
        AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
        // 开启模拟导航
        AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
        //开启真是导航
//        AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
        Intent intent = getIntent();
        mEndLatLng = (NaviLatLng) intent.getSerializableExtra(Constant.INTENT_KEY.ENDLATLNG);
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
       finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    //------------------------------生命周期方法---------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAmapAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAmapAMapNaviView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mAmapAMapNaviView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAmapAMapNaviView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "确认要退出导航？", "确认", "取消");
        twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                twoBtnDialog.dismiss();
                finish();
            }

            @Override
            public void doCancel() {
                twoBtnDialog.dismiss();
            }
        });
        twoBtnDialog.show();
    }
}
