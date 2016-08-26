package com.aiton.bamin.bamindaijiadrier.activity;

import android.os.Bundle;
import android.view.View;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.bamin.bamindaijiadrier.R;

public class AddDriverActivity extends ZjbBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
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
        }
    }
}
