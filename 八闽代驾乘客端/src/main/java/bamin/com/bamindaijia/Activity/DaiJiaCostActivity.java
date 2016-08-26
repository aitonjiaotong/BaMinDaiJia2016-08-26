package bamin.com.bamindaijia.Activity;

import android.os.Bundle;
import android.view.View;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;

import bamin.com.bamindaijia.R;


public class DaiJiaCostActivity extends ZjbBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dai_jia_cost);
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
        findViewById(R.id.imageView_cancle).setOnClickListener(this);
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
            case R.id.imageView_cancle:
                finishTo();
                break;
        }
    }
}
