package bamin.com.bamindaijia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;

public class SettingActivity extends ZjbBaseActivity implements View.OnClickListener {

    private Parcelable mStart_site;
    private String mCity;
    private String mCityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SysApplication.getInstance().addActivity(this);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.button_exit).setOnClickListener(this);
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.rela_commonly_address).setOnClickListener(this);
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mStart_site = intent.getParcelableExtra(Constant.INTENT_KEY.START_SITE);
        Log.e("initIntent ", "chosseSite坐标" + mStart_site.toString());
        mCity = intent.getStringExtra(Constant.INTENT_KEY.CITY);
        mCityCode = intent.getStringExtra(Constant.INTENT_KEY.CITY_CODE);
    }

    @Override
    protected void findID() {

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_back:
                finishTo();
                break;
            case R.id.button_exit:

                final TwoBtnDialog twoBtnDialog = new TwoBtnDialog(this, "确定要退出吗？", "确定", "取消");
                twoBtnDialog.setClicklistener(new TwoBtnDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        twoBtnDialog.dismiss();
                        ACache aCache = ACache.get(SettingActivity.this);
                        aCache.clear();
                        setResult(Constant.REQUEST_RESULT_CODE.SETTING);
                        finishTo();
                    }

                    @Override
                    public void doCancel() {
                        twoBtnDialog.dismiss();

                    }
                });
                twoBtnDialog.show();
                break;
            case R.id.rela_commonly_address:
                intent.putExtra(Constant.INTENT_KEY.START_SITE, mStart_site);
                intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
                intent.putExtra(Constant.INTENT_KEY.CITY_CODE, mCityCode);
                startActivityTo(new Intent(SettingActivity.this, CommonlyAddressActivity.class));
                break;
        }
    }
}
