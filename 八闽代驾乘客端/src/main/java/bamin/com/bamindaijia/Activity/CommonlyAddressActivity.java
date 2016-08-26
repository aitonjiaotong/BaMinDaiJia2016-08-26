package bamin.com.bamindaijia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.widget.AnimCheckBox;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.MyTip;

public class CommonlyAddressActivity extends ZjbBaseActivity implements View.OnClickListener {

    private Parcelable mStart_site;
    private String mCity;
    private String mCityCode;
    private String mChosseType;
    private MyTip tip_home;
    private MyTip tip_company;
    private TextView mTv_home;
    private TextView mTv_home_detail;
    private TextView mTv_company;
    private TextView mTv_company_detail;
    private TextView mTv_home_in;
    private TextView mTv_company_in;
    private TextView mTv_delete;
    private ImageView mImg_home_arrow;
    private ImageView mImg_company_arrow;
    private LinearLayout mLineL_home;
    private LinearLayout mLineL_company;
    private AnimCheckBox mACB_home;
    private AnimCheckBox mACB_company;
    private boolean isDelete;
    private boolean homeIsCheck;
    private boolean companyIsCheck;
    private String name_company;
    private String name_home;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonly_address);
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
    protected void onStart() {
        super.onStart();
        aCache = ACache.get(CommonlyAddressActivity.this);
        tip_home = (MyTip) aCache.getAsObject(Constant.ACACHE.TIP_HOME);
        tip_company = (MyTip) aCache.getAsObject(Constant.ACACHE.TIP_COMPANY);
        //初始化家的地址
        if (tip_home != null) {
            name_home = tip_home.getName();
            String address_home = tip_home.getAddress();
            if (!name_home.equals("")) {
                mTv_home_in.setText("家    ");
                mLineL_home.setVisibility(View.VISIBLE);
                mTv_home.setText(name_home);
                mTv_home_detail.setText(address_home);
                //如果地址名和详情地址一样，或详情地址为空，则隐藏详情地址
                if (name_home.equals(address_home) || address_home.equals("")) {
                    mTv_home_detail.setVisibility(View.GONE);
                }
            }
        } else {
            mTv_home_in.setText("请输入回家地址");
            mLineL_home.setVisibility(View.GONE);

        }
        //初始化公司地址
        if (tip_company != null) {
            name_company = tip_company.getName();
            String address_company = tip_company.getAddress();
            if (!name_company.equals("")) {
                mTv_company_in.setText("公司");
                mLineL_company.setVisibility(View.VISIBLE);
                mTv_company.setText(name_company);
                mTv_company_detail.setText(address_company);
                //如果地址名和详情地址一样，或详情地址为空，则隐藏详情地址
                if (name_company.equals(address_company) || address_company.equals("")) {
                    mTv_company_detail.setVisibility(View.GONE);
                }
            }
        } else {
            mTv_company_in.setText("请输入公司地址");
            mLineL_company.setVisibility(View.GONE);

        }
        hideDelete();
    }

    private void hideDelete() {
//        if (tip_company == null && tip_home == null) {
        if (mTv_company_in.getText().toString().equals("请输入公司地址") && mTv_home_in.getText().toString().equals("请输入回家地址")) {
            mTv_delete.setVisibility(View.GONE);
        } else {
            mTv_delete.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.rela_commonly_address_home).setOnClickListener(this);
        findViewById(R.id.rela_commonly_address_company).setOnClickListener(this);
        mTv_delete.setOnClickListener(this);

    }

    @Override
    protected void initSP() {


    }

    @Override
    protected void initIntent() {
    }

    @Override
    protected void findID() {

        mLineL_home = (LinearLayout) findViewById(R.id.lineL_address_home);
        mLineL_company = (LinearLayout) findViewById(R.id.lineL_address_company);
        mTv_home = (TextView) findViewById(R.id.tv_address_home);
        mTv_home_detail = (TextView) findViewById(R.id.tv_address_home_detail);
        mTv_company = (TextView) findViewById(R.id.tv_address_company);
        mTv_company_detail = (TextView) findViewById(R.id.tv_address_company_detail);
        mTv_home_in = (TextView) findViewById(R.id.tv_address_home_in);
        mTv_company_in = (TextView) findViewById(R.id.tv_address_company_in);
        mTv_delete = (TextView) findViewById(R.id.tv_delete);
        mImg_home_arrow = (ImageView) findViewById(R.id.img_home_arrow);
        mImg_company_arrow = (ImageView) findViewById(R.id.img_company_arrow);
        mACB_home = (AnimCheckBox) findViewById(R.id.AnimCheckBox_del_home);
        mACB_company = (AnimCheckBox) findViewById(R.id.AnimCheckBox_del_company);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imageView_back:
                finishTo();
                break;
            case R.id.rela_commonly_address_home:
                if (isDelete) {
                    mACB_home.setChecked(homeIsCheck);
                    homeIsCheck = !homeIsCheck;
                } else {
                    intent.setClass(this, AddComAddressActivity.class);
                    intent.putExtra(Constant.INTENT_KEY.CHOSSE_TYPE,Constant.INTENT_KEY.COMMONLY_ADDRESS_HOME);
                    startActivityTo(intent);
                }
                break;
            case R.id.rela_commonly_address_company:
                if (isDelete) {
                    mACB_company.setChecked(companyIsCheck);
                    companyIsCheck = !companyIsCheck;
                } else {
                    intent.setClass(this, AddComAddressActivity.class);
                    intent.putExtra(Constant.INTENT_KEY.CHOSSE_TYPE, Constant.INTENT_KEY.COMMONLY_ADDRESS_COMPANY);
                    startActivityTo(intent);
                }
                break;
            case R.id.tv_delete:
                isDelete = !isDelete;
                if (isDelete) {
                    mTv_delete.setText("完成");
                    if (tip_home != null) {
                        if (!name_home.equals("")) {
                            mACB_home.setVisibility(View.VISIBLE);
                        }
                    }
                    if (tip_company != null) {
                        if (!name_company.equals("")) {
                            mACB_company.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    mTv_delete.setText("删除");
                    mACB_home.setVisibility(View.GONE);
                    mACB_company.setVisibility(View.GONE);
                    if (mACB_home.isChecked()) {
                        aCache.remove(Constant.ACACHE.TIP_HOME);
                        mTv_home_in.setText("请输入回家地址");
                        mLineL_home.setVisibility(View.GONE);
                    }
                    if (mACB_company.isChecked()) {
                        aCache.remove(Constant.ACACHE.TIP_COMPANY);
                        mTv_company_in.setText("请输入公司地址");
                        mLineL_company.setVisibility(View.GONE);
                    }
                }
                hideDelete();
                break;

        }
    }
}
