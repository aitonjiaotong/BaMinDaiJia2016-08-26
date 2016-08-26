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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.List;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.MyLatLonPoint;
import bamin.com.bamindaijia.model.MyTip;

public class ChosseSite extends ZjbBaseActivity implements View.OnClickListener {

    private ListView mListView_site;
    private MyAdapter mAdapter;
    private View mSite_head;
    private LatLng mStart_site;
    private List<PoiItem> mPois = new ArrayList<>();
    private EditText mEditText_where;
    private Inputtips mInputtips;
    private TextView mTextView_city;
    private String mCity;
    private String mCityCode;
    private ListView mListView_search;
    private List<Tip> listSearch = new ArrayList<>();
    private MySearchAdapter mAdapterSearch;
    private String mChosseType;
    private String address = "政府";
    private boolean isAddCommonly;
    private String commonlyAddress;
    private String commonlyPoint;
    private TextView mTv_home;
    private TextView mTv_company;
    private RelativeLayout mRela_no_result;
    private MyTip tip_home;
    private MyTip tip_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosse_site);
        init();
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        ACache aCache = ACache.get(ChosseSite.this);
        tip_home = (MyTip) aCache.getAsObject(Constant.ACACHE.TIP_HOME);
        tip_company = (MyTip) aCache.getAsObject(Constant.ACACHE.TIP_COMPANY);

//        String homeName = aCache.getAsString(Constant.ACACHE.TIP_HOME_NAME);
//        String companyName = aCache.getAsString(Constant.ACACHE.TIP_COMPANY_NAME);
        if (tip_home != null) {
            String homeName = tip_home.getName();

            Log.e("tip_home", tip_home.toString());
            if (!homeName.equals("")) {
                Log.e("homeName", homeName);

                mTv_home.setText(homeName);
            }
        }

        if (tip_company != null) {
            Log.e("tip_company", tip_company.toString());
            String companyName = tip_company.getName();
            if (!companyName.equals("")) {
                Log.e("companyName", companyName);
                mTv_company.setText(companyName);
            }
        }


    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListView_site.addHeaderView(mSite_head);
        mListView_site.setAdapter(mAdapter);
        mTextView_city.setText(mCity);
        mAdapterSearch = new MySearchAdapter();
        mListView_search.setAdapter(mAdapterSearch);

        if(Constant.INTENT_KEY.START.equals(mChosseType)){
            mEditText_where.setHint("你从哪里出发");
        }else if(Constant.INTENT_KEY.END.equals(mChosseType)){
            mEditText_where.setHint("你要到哪里去");
        }
        fanBianMa();

    }

    @Override
    protected void setListeners() {
        mEditText_where.addTextChangedListener(new MyTextWatcher());
        mInputtips = new Inputtips(ChosseSite.this, new Inputtips.InputtipsListener() {
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
        findViewById(R.id.relaL_address_company).setOnClickListener(this);
        findViewById(R.id.relaL_address_home).setOnClickListener(this);
        mTextView_city.setOnClickListener(this);
        mListView_site.setOnItemClickListener(new MySiteOnItemClickListener());
        mListView_search.setOnItemClickListener(new MySearchOnItemClickListener());
    }

    @Override
    protected void initSP() {

    }

    protected void initIntent() {
        Intent intent = getIntent();
        mStart_site = intent.getParcelableExtra(Constant.INTENT_KEY.START_SITE);
        Log.e("initIntent ", "chosseSite坐标" + mStart_site.toString());
        mCity = intent.getStringExtra(Constant.INTENT_KEY.CITY);
        mCityCode = intent.getStringExtra(Constant.INTENT_KEY.CITY_CODE);
        mChosseType = intent.getStringExtra(Constant.INTENT_KEY.CHOSSE_TYPE);
        Log.e("initIntent", "选择方式" + mChosseType);

        isAddCommonly = intent.getBooleanExtra(Constant.INTENT_KEY.COMMONLY_ADDRESS, false);
        Log.e("isAddCommonly", "添加常用地址" + isAddCommonly);
    }

    protected void findID() {
        mListView_site = (ListView) findViewById(R.id.listView_site);
        mSite_head = getLayoutInflater().inflate(R.layout.site_head, null);
        mEditText_where = (EditText) findViewById(R.id.editText_where);
        mTextView_city = (TextView) findViewById(R.id.textView_city);
        mListView_search = (ListView) findViewById(R.id.listView_search);
        mTv_home = (TextView) mSite_head.findViewById(R.id.tv_address_home);
        mTv_company = (TextView) mSite_head.findViewById(R.id.tv_address_company);
        mRela_no_result = (RelativeLayout) findViewById(R.id.relaL_no_result);
    }

    class MySiteOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position = position - 1;
            Intent intent = new Intent();
            PoiItem poiItem = mPois.get(position);

            resultAddress( poiItem.getTitle(),poiItem.getLatLonPoint(),poiItem.getAdName());
        }
    }

    class MySearchOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            resultAddress(listSearch.get(position).getName(),listSearch.get(position).getPoint() ,listSearch.get(position).getAddress());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            if (requestCode == Constant.REQUEST_RESULT_CODE.CHOOSE_CITY && resultCode == Constant.REQUEST_RESULT_CODE.CHOOSE_CITY) {
                mCity = data.getStringExtra(Constant.INTENT_KEY.CITY);
                Log.e("CHOOSE_CITY", mCity);
                mTextView_city.setText(mCity);
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.textView_cancle:
                finishTo();
                break;
            case R.id.textView_city:

                intent.setClass(this, CityPickerActivity.class);
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.CHOOSE_CITY);
                break;
            case R.id.relaL_address_company:
                //判断，如果常用地址有数据，则跳转回主界面，如果没有，则跳转到设置界面
                if (mTv_company.getText().toString().equals("设置公司地址")) {
                    intent.setClass(this, AddComAddressActivity.class);
                    intent.putExtra(Constant.INTENT_KEY.CHOSSE_TYPE,Constant.INTENT_KEY.COMMONLY_ADDRESS_COMPANY);
                    startActivityTo(intent);
                } else {
                    MyLatLonPoint point = tip_company.getPoint();
                    LatLonPoint latLonPoint = new LatLonPoint(point.getLatitude(),point.getLongitude());
                    resultAddress( tip_company.getName(),latLonPoint,"");
                }
                break;
            case R.id.relaL_address_home:
                //判断，如果常用地址有数据，则跳转回主界面，如果没有，则跳转到设置界面
                if (mTv_home.getText().toString().equals("设置家的地址")) {
                    intent.setClass(this, AddComAddressActivity.class);
                    intent.putExtra(Constant.INTENT_KEY.CHOSSE_TYPE,Constant.INTENT_KEY.COMMONLY_ADDRESS_HOME);
                    startActivityTo(intent);
                } else {
                    MyLatLonPoint point = tip_home.getPoint();
                    LatLonPoint latLonPoint = new LatLonPoint(point.getLatitude(),point.getLongitude());
                    resultAddress( tip_home.getName(),latLonPoint,"");
                }
                break;
        }
    }

    /**
     * 返回起点或终点的数据
     * @param name 地址名
     * @param latLonPoint  地址坐标
     */
    private void resultAddress( String name ,LatLonPoint latLonPoint,String address) {
        Intent intent = new Intent();
        if (Constant.INTENT_KEY.START.equals(mChosseType)) {
            intent.putExtra(Constant.INTENT_KEY.START_SITE_BACK, name);
            intent.putExtra(Constant.INTENT_KEY.START_LATLNG_BACK, latLonPoint);
            intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
            intent.putExtra(Constant.INTENT_KEY.CHOSSE_SITE_ADDRESS_START,address);
            setResult(Constant.REQUEST_RESULT_CODE.CHOSSE_START_SITE, intent);
        } else if (Constant.INTENT_KEY.END.equals(mChosseType)) {
            intent.putExtra(Constant.INTENT_KEY.END_SITE_BACK, name);
            intent.putExtra(Constant.INTENT_KEY.END_LATLNG_BACK, latLonPoint);
            intent.putExtra(Constant.INTENT_KEY.CITY, mCity);
            intent.putExtra(Constant.INTENT_KEY.CHOSSE_SITE_ADDRESS_END,address);
            setResult(Constant.REQUEST_RESULT_CODE.CHOSSE_END_SITE, intent);
        }
        finishTo();
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

    //地理坐标转换为中文地址
    private void fanBianMa() {
        GeocodeSearch geocodeSearch = new GeocodeSearch(ChosseSite.this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                Log.e("onRegeocodeSearched",
                        regeocodeAddress.getBusinessAreas().get(0).getName()
                                + "\n" + regeocodeAddress.getAois().get(0).getAoiName()
                                + "\n" + regeocodeAddress.getCrossroads().get(0).getFirstRoadName()
                                + "\n" + regeocodeAddress.getRoads().size()
                );
                mPois.clear();
                mPois.addAll(regeocodeAddress.getPois());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        LatLonPoint latLonPoint = new LatLonPoint(mStart_site.latitude, mStart_site.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
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
                mAdapter.notifyDataSetChanged();
                mAdapterSearch.notifyDataSetChanged();
                if (!"".equals(s)) {

                    mInputtips.requestInputtips(s + "", "" + mCityCode);
                    mListView_site.setVisibility(View.GONE);
                    mListView_search.setVisibility(View.VISIBLE);
                }
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("afterTextChanged", "" + s);
            if (s.length() == 0) {
                mListView_site.setVisibility(View.VISIBLE);
                mListView_search.setVisibility(View.GONE);
            }
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPois.size();
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
            if (mPois.size() > 0) {
                textView_poi.setText(mPois.get(position).getTitle());
                textView_detail.setText(mPois.get(position).getSnippet());
            }
            return inflate;
        }
    }
}
