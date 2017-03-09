package com.slash.youth.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.domain.CityClassBean;
import com.slash.youth.domain.ListCityBean;
import com.slash.youth.domain.ListProvinceBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.LocationCityInfoAdapter;
import com.slash.youth.ui.adapter.LocationCitySearchListAdapter;
import com.slash.youth.ui.viewmodel.ActivityCityLocationModel;
import com.slash.youth.ui.viewmodel.HeaderLocationCityInfoModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DBManager;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PatternUtils;
import com.slash.youth.utils.Pinyin4JUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class CityLocationActivity extends BaseActivity {
    private ActivityCityLocationBinding mActivityCityLocationBinding;
    private ActivityCityLocationModel mActivityCityLocationModel;
    private SQLiteDatabase database;
    public DBManager dbHelper;
    private ArrayList<CityClassBean> citys;
    private String city_name;
    private String province_name;
    private String firstLetter;
    private ArrayList<Character> listCityNameFirstLetter = new ArrayList<Character>();
    private ArrayList<LocationCityInfo> listCityInfo = new ArrayList<LocationCityInfo>();
    private ArrayList<ListCityBean> listCity = new ArrayList();
    private ArrayList<ListProvinceBean> listProvince = new ArrayList();
    private ArrayList<String> listSearchCity = new ArrayList<String>();
    private ArrayList<CityClassBean> province;
    private int pro_sort;
    private int city_id;
    private Intent intent;
    private android.os.Handler mHanler = new android.os.Handler();
    public static HashMap<String, String> map = new HashMap<>();
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private int startY;
    private ImageView ivLocationCityFirstLetterListHeader;
    private LocationCityFirstLetterAdapter locationCityFirstLetterAdapter;
    private int heardHeight;
    private int height;
    private int itemHeight;
    private int startHeight;
    private int startIndex;
    private CityHistoryEntityDao cityHistoryEntityDao;
    private HeaderLocationCityInfoModel headerLocationCityInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化访问最近城市数据库
        cityHistoryEntityDao = SlashApplication.getInstances().getDaoSession().getCityHistoryEntityDao();

        intent = getIntent();
        //打开数据库，读取最近访问的城市
        mActivityCityLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_location);
        mActivityCityLocationModel = new ActivityCityLocationModel(mActivityCityLocationBinding, this);
        mActivityCityLocationBinding.setActivityCityLocationModel(mActivityCityLocationModel);

        headerListviewLocationCityInfoListBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.header_listview_location_city_info_list, null, false);
        headerLocationCityInfoModel = new HeaderLocationCityInfoModel(headerListviewLocationCityInfoListBinding, intent, this, cityHistoryEntityDao);
        headerListviewLocationCityInfoListBinding.setHeaderLocationCityInfoModel(headerLocationCityInfoModel);
        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.addHeaderView(headerListviewLocationCityInfoListBinding.getRoot());
        ivLocationCityFirstLetterListHeader = new ImageView(CommonUtils.getContext());
        ivLocationCityFirstLetterListHeader.setImageResource(R.mipmap.search_letter_search_icon);
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.addHeaderView(ivLocationCityFirstLetterListHeader);
        ivLocationCityFirstLetterListHeader.measure(0, 0);
        heardHeight = ivLocationCityFirstLetterListHeader.getMeasuredHeight();

        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setVerticalScrollBarEnabled(false);
        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setVerticalScrollBarEnabled(false);

        //数据库管理
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper = new DBManager(CityLocationActivity.this);
                dbHelper.openDatabase();
                dbHelper.closeDatabase();
                CommonUtils.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (char cha = 'A'; cha <= 'Z'; cha++) {
                            listCityNameFirstLetter.add(cha);
                        }
                        initData();//initData()需要在这里执行，只有当子线程的数据库拷贝操作完成以后，才能进行读取数据库的操作
                        initListener();
                    }
                });
            }
        }).start();

//        for (char cha = 'A'; cha <= 'Z'; cha++) {
//            listCityNameFirstLetter.add(cha);
//        }

//        initData();

//        initListener();
    }

    private void initData() {
        File fileDb = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
        LogKit.v("城市名数据库文件的大小 fileDB size:" + fileDb.length());

        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
        citys = getCity();
        province = getProvince();
        database.close();

        for (CityClassBean cityClass : province) {
            province_name = cityClass.city_name;
            pro_sort = cityClass.city_id;
            listProvince.add(new ListProvinceBean(pro_sort, province_name));
        }

        for (CityClassBean cityClass : citys) {
            city_name = cityClass.city_name;
            city_id = cityClass.city_id;
            String s = Pinyin4JUtils.cn2PYInitial(city_name);
            firstLetter = s.substring(0, 1);
            listCity.add(new ListCityBean(firstLetter, city_name, city_id));
        }

        for (Character character : listCityNameFirstLetter) {
            listCityInfo.add(new LocationCityInfo(true, character.toString(), "", -1));
            for (ListCityBean listCityBean : listCity) {
                String firstCityLetter = listCityBean.getFirstLetter();
                String cityName = listCityBean.getCityName();
                int id = listCityBean.id;
                if (firstCityLetter.equals(character.toString())) {
                    listCityInfo.add(new LocationCityInfo(false, "", cityName, id));
                }
            }
        }

        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setAdapter(new LocationCityInfoAdapter(listCityInfo));
        locationCityFirstLetterAdapter = new LocationCityFirstLetterAdapter(listCityNameFirstLetter);
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setAdapter(locationCityFirstLetterAdapter);
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
                    int id = cur.getInt(cur.getColumnIndex("ProId"));
                    CityClassBean city = new CityClassBean("", 0);
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
                    int id = cur.getInt(cur.getColumnIndex("ProId"));
                    CityClassBean city = new CityClassBean("", 0);
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

    private int index = -1;

    private void initListener() {
        headerLocationCityInfoModel.setOnAllCityClickCListener(new HeaderLocationCityInfoModel.OnAllCityClickCListener() {
            @Override
            public void OnAllCityClick() {
            }
        });

        //点击条目，获得省市，传递到所在地
        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    LocationCityInfo locationCityInfo = listCityInfo.get(position - 1);
                    boolean isFirstLetter = locationCityInfo.isFirstLetter;
                    String cityName = locationCityInfo.CityName;
                    int id1 = locationCityInfo.getId();
                    for (ListProvinceBean listProvinceBean : listProvince) {
                        int proId = listProvinceBean.proId;
                        if (proId == id1) {
                            String provinceName = listProvinceBean.provinceName;
                            intent.putExtra("city", cityName);
                            intent.putExtra("province", provinceName);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            }
        });

        //滑动事件
        touchListener();

        //搜索框的监听
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
                String first = null;
                if (s != null) {
                    listSearchCity.clear();
                    String text = s.toString();
                    String pinyin = Pinyin4JUtils.cn2PYInitial(text);
                    if (pinyin != "") {
                        first = pinyin.substring(0, 1);
                    }
                    //如果是汉字
                    if (PatternUtils.match(PatternUtils.chineseRegex, text)) {
                        for (ListCityBean listCityBean : listCity) {
                            String cityName = listCityBean.getCityName();
                            String pinyinCity = Pinyin4JUtils.cn2PYInitial(cityName);
                            String firstLetter = pinyinCity.substring(0, 1);
                            if (cityName.contains(text) && firstLetter.equals(first)) {
                                listSearchCity.add(cityName);
                            }
                        }
                    } else if (PatternUtils.match(PatternUtils.letterRegex, text)) {//如果是字母
                        for (ListCityBean listCityBean : listCity) {
                            String substring = text.substring(0, 1);
                            String firstLetter = listCityBean.getFirstLetter();
                            if (firstLetter.equals(substring.toUpperCase())) {
                                listSearchCity.add(listCityBean.getCityName());
                            }
                        }
                    }
                    mActivityCityLocationBinding.lvActivityCityLocationSearchList.setAdapter(new LocationCitySearchListAdapter(listSearchCity));
                }
            }
        });

        //点击条目
        mActivityCityLocationBinding.lvActivityCityLocationSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = listSearchCity.get(position);
                for (int i = 0; i < listCityInfo.size(); i++) {
                    String city = listCityInfo.get(i).getCityName();
                    if (city.equals(cityName)) {
                        index = i;
                    }
                }
                mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(index + mActivityCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                mActivityCityLocationModel.setSearchCityListVisible(View.INVISIBLE);
                mActivityCityLocationModel.setCityInfoListVisible(View.VISIBLE);
            }
        });
    }

    //触摸滑动事件
    private void touchListener() {
        ListAdapter adapter = mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        //总长
        height = totalHeight - heardHeight;
        //每一个字母的长度
        itemHeight = height / 26;

        mActivityCityLocationBinding.rlBar.measure(0, 0);
        int titleHeight = mActivityCityLocationBinding.rlBar.getMeasuredHeight();

        //从A开的距离
        WindowManager wm = (WindowManager) CommonUtils.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int phoneHeight = wm.getDefaultDisplay().getHeight();
        startHeight = (phoneHeight - totalHeight - titleHeight) / 2 + titleHeight;

        //触摸事件
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = (int) event.getRawY();
                        index = (startY - startHeight) / itemHeight;
                        startIndex = index - 1;
                        if (startIndex >= 0 && startIndex < 26) {
                            String firstStartLetter = listCityNameFirstLetter.get(startIndex).toString();
                            mActivityCityLocationBinding.tv.setText(firstStartLetter);
                            mActivityCityLocationBinding.tv.setVisibility(View.VISIBLE);
                            mHanler.removeCallbacksAndMessages(null);
                            mHanler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityCityLocationBinding.tv.setVisibility(View.GONE);
                                }
                            }, 1000);
                            //左边定位到具体的城市字母开头
                            for (int i = 0; i < listCityInfo.size(); i++) {
                                LocationCityInfo locationCityInfo = listCityInfo.get(i);
                                String firstCityLetter = locationCityInfo.getFirstLetter();
                                if (firstCityLetter.equals(firstStartLetter)) {
                                    mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i + mActivityCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int rawY = (int) event.getRawY();
                        int moveY = rawY - startY;
                        //对应的字母
                        int count = moveY / itemHeight;
                        int moveIndex = startIndex + count;
                        if (moveIndex >= 0 && moveIndex < 26) {
                            String firstMoveLetter = listCityNameFirstLetter.get(moveIndex).toString();
                            mActivityCityLocationBinding.tv.setText(firstMoveLetter);
                            mActivityCityLocationBinding.tv.setVisibility(View.VISIBLE);
                            mHanler.removeCallbacksAndMessages(null);
                            mHanler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mActivityCityLocationBinding.tv.setVisibility(View.GONE);
                                }
                            }, 1000);

                            //左边定位到具体的城市字母开头
                            for (int i = 0; i < listCityInfo.size(); i++) {
                                LocationCityInfo locationCityInfo = listCityInfo.get(i);
                                String firstCityLetter = locationCityInfo.getFirstLetter();
                                if (firstCityLetter.equals(firstMoveLetter)) {
                                    mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i + mActivityCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        int endY = (int) event.getRawY();
                        break;
                }
                return true;
            }
        });
    }
}
