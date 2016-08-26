package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.widget.TwoBtnDialog;
import com.aiton.bamin.bamindaijiadrier.R;

public class SettingActivity extends ZjbBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {

    }


    @Override
    public void onClick(View v) {
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
                        Intent intent = new Intent();
                        intent.setClass(SettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivityTo(intent);
                    }

                    @Override
                    public void doCancel() {
                        twoBtnDialog.dismiss();
                    }
                });
                twoBtnDialog.show();
                break;
        }
    }
}
