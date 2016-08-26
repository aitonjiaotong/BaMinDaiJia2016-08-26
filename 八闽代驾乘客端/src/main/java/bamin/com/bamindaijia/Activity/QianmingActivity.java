package bamin.com.bamindaijia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;

public class QianmingActivity extends ZjbBaseActivity implements View.OnClickListener {

    private EditText mEdt_User_Qianming;
    private TextView mTv_EditNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
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
        findViewById(R.id.tv_complete).setOnClickListener(this);
        mEdt_User_Qianming.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("QianMingActivity", "onTextChanged:count --->>" +count );
                Log.e("QianMingActivity", "onTextChanged:start --->>" +start );
                Log.e("QianMingActivity", "onTextChanged:before --->>" +before );
                Log.e("QianMingActivity", "onTextChanged:CharSequence --->>" + s);

                mTv_EditNum.setText("" + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mEdt_User_Qianming = (EditText) findViewById(R.id.edt_user_signature);
        mTv_EditNum = (TextView) findViewById(R.id.tv_edittextNum);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageView_back:
                finishTo();
            break;
            case R.id.tv_complete:
                Intent intent = new Intent();
                intent.putExtra(Constant.INTENT_KEY.QIAN_MING,mEdt_User_Qianming.getText().toString().trim());
                setResult(Constant.REQUEST_RESULT_CODE.QIAM_MING, intent);
            break;
            default:
                break;
        }
    }
}
