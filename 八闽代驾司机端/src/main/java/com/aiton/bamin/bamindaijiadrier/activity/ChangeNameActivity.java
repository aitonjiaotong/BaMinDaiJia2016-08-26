package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;


public class ChangeNameActivity extends ZjbBaseActivity implements View.OnClickListener {

    private EditText mEdt_Name;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

        if (name != null) {
            if(name.equals("请填写姓名")){
                name = "";
            }
            mEdt_Name.setText(name);
        }
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.tv_complete).setOnClickListener(this);
    }

    @Override
    protected void initSP() {
        Intent intent = getIntent();
        name = intent.getStringExtra(Constant.INTENT_KEY.CHANGE_NAME);

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mEdt_Name = (EditText) findViewById(R.id.edt_name);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finishTo();
                break;
            case R.id.tv_complete:
                Intent intent = new Intent();
                intent.putExtra(Constant.INTENT_KEY.CHANGE_NAME, mEdt_Name.getText().toString().trim());
                setResult(Constant.REQUEST_RESULT_CODE.CHANGE_NAEM, intent);
                finishTo();
                break;

            default:
                break;
        }
    }
}
