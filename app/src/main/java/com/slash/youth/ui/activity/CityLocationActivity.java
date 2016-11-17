package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.domain.CityClassBean;
import com.slash.youth.domain.ListCityBean;
import com.slash.youth.domain.ListProvinceBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.LocationCityInfoAdapter;
import com.slash.youth.ui.adapter.LocationCitySearchListAdapter;
import com.slash.youth.ui.viewmodel.ActivityCityLocationModel;
import com.slash.youth.ui.viewmodel.HeaderLocationCityInfoModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DBManager;
import com.slash.youth.utils.Pinyin4JUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class CityLocationActivity extends Activity {
    private ActivityCityLocationBinding mActivityCityLocationBinding;
    private HashMap<String, Integer> mHashFirstLetterIndex = new HashMap<String, Integer>();
    private ActivityCityLocationModel mActivityCityLocationModel;
    private SQLiteDatabase database;
    public DBManager dbHelper;
    private ArrayList<CityClassBean> citys;
    private String city_name;
    private String province_name;
    private String firstLetter;
    private   ArrayList<Character> listCityNameFirstLetter = new ArrayList<Character>();
    private ArrayList<LocationCityInfo> listCityInfo = new ArrayList<LocationCityInfo>();
    private ArrayList<ListCityBean> listCity = new ArrayList();
    private ArrayList<ListProvinceBean> listProvince = new ArrayList();
    private ArrayList<CityClassBean> province;
    private int pro_sort;
    private int city_id;
    private Intent intent;
     public static HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         intent = getIntent();

        mActivityCityLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_location);
        mActivityCityLocationModel = new ActivityCityLocationModel(mActivityCityLocationBinding, this);
        mActivityCityLocationBinding.setActivityCityLocationModel(mActivityCityLocationModel);

        HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.header_listview_location_city_info_list, null, false);
        HeaderLocationCityInfoModel headerLocationCityInfoModel = new HeaderLocationCityInfoModel();
        headerListviewLocationCityInfoListBinding.setHeaderLocationCityInfoModel(headerLocationCityInfoModel);
        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.addHeaderView(headerListviewLocationCityInfoListBinding.getRoot());
        ImageView ivLocationCityFirstLetterListHeader = new ImageView(CommonUtils.getContext());
        ivLocationCityFirstLetterListHeader.setImageResource(R.mipmap.search_letter_search_icon);
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.addHeaderView(ivLocationCityFirstLetterListHeader);

        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setVerticalScrollBarEnabled(false);
        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setVerticalScrollBarEnabled(false);

        //数据库管理
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper = new DBManager(CityLocationActivity.this);
                dbHelper.openDatabase();
                dbHelper.closeDatabase();
            }
        });

        for (char cha = 'A'; cha <= 'Z'; cha++) {
            listCityNameFirstLetter.add(cha);
        }

        initData();

        initListener();
    }

    private void initData() {
        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
        citys = getCity();
        province = getProvince();
        database.close();

        for (CityClassBean cityClass : province) {
            province_name = cityClass.city_name;
            pro_sort = cityClass.city_id;
            listProvince.add(new ListProvinceBean(pro_sort,province_name));
        }

        for (CityClassBean cityClass : citys) {
            city_name = cityClass.city_name;
            city_id = cityClass.city_id;
            String s = Pinyin4JUtils.cn2PYInitial(city_name);
            firstLetter = s.substring(0, 1);
            listCity.add(new ListCityBean(firstLetter,city_name,city_id));
        }

        for (Character character : listCityNameFirstLetter) {
            listCityInfo.add(new LocationCityInfo(true, character.toString(), "",-1));
            for (ListCityBean listCityBean : listCity) {
                String firstCityLetter = listCityBean.getFirstLetter();
                String cityName = listCityBean.getCityName();
                int id = listCityBean.id;
                if(firstCityLetter.equals(character.toString())){
                    listCityInfo.add(new LocationCityInfo(false,"",cityName,id));
                }
            }
        }

        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setAdapter(new LocationCityInfoAdapter(listCityInfo));
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setAdapter(new LocationCityFirstLetterAdapter(listCityNameFirstLetter));
        //搜索的自动提示数据，实际应该由服务端返回
        //搜索自动提示测试数据
        ArrayList<String> listSearchCity = new ArrayList<String>();
        listSearchCity.add("苏州");
        listSearchCity.add("上海");
        listSearchCity.add("北京");
        mActivityCityLocationBinding.lvActivityCityLocationSearchList.setAdapter(new LocationCitySearchListAdapter(listSearchCity));
    }

    //获取城市
    private ArrayList<CityClassBean> getCity() {
        Cursor cur = database.rawQuery("SELECT * FROM T_City", null);
        if (cur != null) {
            int NUM_CITY = cur.getCount();
            ArrayList<CityClassBean> taxicity = new ArrayList<CityClassBean>(NUM_CITY);
            if (cur.moveToFirst()) {
                do {
                    String name = cur.getString(cur.getColumnIndex("CityName"));
                    int id = cur.getInt(cur.getColumnIndex("ProID"));
                    CityClassBean city = new CityClassBean("", 0);
                    //  System.out.println(name);
                    city.city_name = name;
                    city.city_id = id;
                    taxicity.add(city);
                } while (cur.moveToNext());
            }
            return taxicity;
        } else {
            return null;
        }
    }

    //获取省份
    private ArrayList<CityClassBean> getProvince() {
        Cursor cur = database.rawQuery("SELECT * FROM T_Province", null);
        if (cur != null) {
            int NUM_CITY = cur.getCount();
            ArrayList<CityClassBean> taxicity = new ArrayList<CityClassBean>(NUM_CITY);
            if (cur.moveToFirst()) {
                do {
                    String name = cur.getString(cur.getColumnIndex("ProName"));
                    int id = cur.getInt(cur.getColumnIndex("ProSort"));
                    CityClassBean city = new CityClassBean("", 0);
                     // System.out.println(name);
                    city.city_name = name;
                    city.city_id = id;
                    taxicity.add(city);
                } while (cur.moveToNext());
            }
            return taxicity;
        } else {
            return null;
        }
    }

    private void initListener() {
        //点击条目，获得省市，传递到所在地
        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               LocationCityInfo locationCityInfo = listCityInfo.get(position-1);
                boolean isFirstLetter = locationCityInfo.isFirstLetter;
                String cityName1 = locationCityInfo.CityName;
                int id1 = locationCityInfo.getId();
                for (ListProvinceBean listProvinceBean : listProvince) {
                    int proId = listProvinceBean.proId;
                    if(proId == id1){
                        String provinceName = listProvinceBean.provinceName;
                        intent.putExtra("city",cityName1);
                        intent.putExtra("province",provinceName);
                        setResult(RESULT_OK,intent);
                        //map.put("city",cityName1);
                        //map.put("province",provinceName);
                        finish();
                    }
                }
            }
        });

        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv= (TextView) view;
//                ToastUtils.shortToast(position + " "+tv.getText());
//                int clickIndex = position - mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.getHeaderViewsCount();
                if (position > 0) {
                    TextView tvFirstLetter = (TextView) view;
                    String firstLetter = tvFirstLetter.getText().toString();

                    for (int i = 0; i < listCityInfo.size(); i++) {
                        LocationCityInfo locationCityInfo = listCityInfo.get(i);
                        String firstCityLetter = locationCityInfo.getFirstLetter();
                        if(firstCityLetter.equals(firstLetter) ){
                            ToastUtils.shortCenterToast(" "+firstLetter);
                            mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i + mActivityCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                        }
                    }
                }
            }
        });
        mActivityCityLocationBinding.etActivityCityLocationSearchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mActivityCityLocationModel.setCityInfoListVisible(View.VISIBLE);
                    mActivityCityLocationModel.setSearchCityListVisible(View.INVISIBLE);
                } else {
                    mActivityCityLocationModel.setCityInfoListVisible(View.INVISIBLE);
                    mActivityCityLocationModel.setSearchCityListVisible(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
