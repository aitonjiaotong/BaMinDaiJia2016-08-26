package bamin.com.bamindaijia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.List;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.MyLatLonPoint;
import bamin.com.bamindaijia.model.MyTip;

public class AddComAddressActivity extends ZjbBaseActivity implements View.OnClickListener {

    private View mSite_head;
    private EditText mEditText_where;
    private TextView mTextView_city;
    private ListView mListView_search;
    private List<Tip> listSearch = new ArrayList<>();
    private Inputtips mInputtips;
    private MySearchAdapter mAdapterSearch;
    private String mCityCode;
    private String mCity;
    private String mChosseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_com_address);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTextView_city.setText(mCity);
        mAdapterSearch = new MySearchAdapter();
        mListView_search.setAdapter(mAdapterSearch);
    }

    @Override
    protected void setListeners() {
        mEditText_where.addTextChangedListener(new MyTextWatcher());
        mInputtips = new Inputtips(this, new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                listSearch.addAll(list);
                mAdapterSearch.notifyDataSetChanged();
                for (int j = 0; j < list.size(); j++) {
                    Log.e("onGetInputtips", "提示返回结果" + list.get(j).getName());
                }
            }
        });

        findViewById(R.id.textView_cancle).setOnClickListener(this);
        mListView_search.setOnItemClickListener(new MySearchOnItemClickListener());
    }
    class MySearchOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Tip tip = listSearch.get(position);
            Log.e("tip","Adcode:"+tip.getAdcode());
            Log.e("tip","Address:"+tip.getAddress());
            Log.e("tip","District:"+tip.getDistrict());
            Log.e("tip","PoiID:"+tip.getPoiID());
            Log.e("tip","Name:"+tip.getName());
            Log.e("tip","Postion:"+tip.getPoint());

            MyTip myTip = new MyTip();
            LatLonPoint point = tip.getPoint();
            //将高德地图中的Tip数据传到自己建立的MyTip类中
            MyLatLonPoint myLatLonPoint = new MyLatLonPoint(point.getLatitude(),point.getLongitude());
            myTip.setAdcode(tip.getAdcode());
            myTip.setAddress(tip.getAddress());
            myTip.setDistrict(tip.getDistrict());
            myTip.setID(tip.getPoiID());
            myTip.setName(tip.getName());
            myTip.setPostion(myLatLonPoint);


            if (Constant.INTENT_KEY.COMMONLY_ADDRESS_HOME.equals(mChosseType)) {
//                intent.putExtra(Constant.INTENT_KEY.START_SITE_BACK, tip.getName());
//                intent.putExtra(Constant.INTENT_KEY.START_LATLNG_BACK, listSearch.get(position).getPoint());
//
//                Log.e("START_SITE_BACK",listSearch.get(position).getName());
//                Log.e("START_LATLNG_BACK", "" + listSearch.get(position).getAddress());
//                intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
//                setResult(Constant.REQUEST_RESULT_CODE.COMMONLY_ADDRESS_HOME, intent);


//                aCache.put(Constant.ACACHE.TIP_HOME_NAME,tip.getName());
//                aCache.put(Constant.ACACHE.TIP_HOME_ADDRESS,tip.getAddress());
//                aCache.put(Constant.ACACHE.TIP_HOME_POINT,tip.getPoint());
                ACache aCache = ACache.get(AddComAddressActivity.this);
                aCache.put(Constant.ACACHE.TIP_HOME,  myTip);
                MyTip asObject = (MyTip) aCache.getAsObject(Constant.ACACHE.TIP_HOME);
                Log.e("myTip","getName:"+asObject.getName());




            } else if (Constant.INTENT_KEY.COMMONLY_ADDRESS_COMPANY.equals(mChosseType)) {
//                intent.putExtra(Constant.INTENT_KEY.END_SITE_BACK, tip.getName());
//                intent.putExtra(Constant.INTENT_KEY.END_LATLNG_BACK, tip.getPoint());
//                Log.e("END_SITE_BACK", tip.getName());
//                intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
//                setResult(Constant.REQUEST_RESULT_CODE.COMMONLY_ADDRESS_COMPANY, intent);
//                aCache.put(Constant.ACACHE.TIP_COMPANY_NAME,tip.getName());
//                aCache.put(Constant.ACACHE.TIP_COMPANY_ADDRESS,tip.getAddress());

                ACache aCache = ACache.get(AddComAddressActivity.this);
                aCache.put(Constant.ACACHE.TIP_COMPANY,  myTip);
                Log.e("myTip","getName:"+myTip.getName());
            }

            finishTo();
        }
    }
    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mCityCode = intent.getStringExtra(Constant.INTENT_KEY.CITY_CODE);
        mCity = intent.getStringExtra(Constant.INTENT_KEY.CITY);
        mChosseType = intent.getStringExtra(Constant.INTENT_KEY.CHOSSE_TYPE);
        Log.e("mChosseType","选择类型" + mChosseType);

    }

    @Override
    protected void findID() {
        mEditText_where = (EditText) findViewById(R.id.editText_where);
        mTextView_city = (TextView) findViewById(R.id.textView_city);
        mListView_search = (ListView) findViewById(R.id.listView_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_cancle:
                finishTo();
                break;
            case R.id.textView_city:
                Intent intent = new Intent(this, CityPickerActivity.class);
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.CHOOSE_CITY);
                break;
        }
    }
    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("onTextChanged", "" + s);
            try {
                listSearch.clear();
                mAdapterSearch.notifyDataSetChanged();
                if (!"".equals(s)) {
                    mInputtips.requestInputtips(s + "", "" + mCityCode);
                }
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    class MySearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listSearch.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.site_item, null);
            TextView textView_poi = (TextView) inflate.findViewById(R.id.textView_poi);
            TextView textView_detail = (TextView) inflate.findViewById(R.id.textView_detail);
            textView_poi.setText(listSearch.get(position).getName());
            textView_detail.setText(listSearch.get(position).getDistrict());
            return inflate;
        }
    }
}
