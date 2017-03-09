package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.CityClassBean;
import com.slash.youth.domain.ListCityBean;
import com.slash.youth.domain.ListProvinceBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.LocationCityInfoAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DBManager;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.Pinyin4JUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by zss on 2016/10/17.
 */
public class SearchActivityCityLocationModel extends BaseObservable {
    private SearchActivityCityLocationBinding searchActivityCityLocationBinding;
    public DBManager dbHelper;
    private SQLiteDatabase database;
    public ArrayList<Character> listCityNameFirstLetter;
    public ArrayList<ListCityBean> listCity = new ArrayList();
    public ArrayList<LocationCityInfo> listCityInfo = new ArrayList<>();
    public static String cityName;
    private Activity mActivity;

    public SearchActivityCityLocationModel(SearchActivityCityLocationBinding searchActivityCityLocationBinding,Activity mActivity) {
        this.searchActivityCityLocationBinding = searchActivityCityLocationBinding;
        this.mActivity = mActivity;
        initData();
        initView();
        listener();
    }

    private int cityInfoListVisible = View.VISIBLE;
    private int searchCityListVisible = View.INVISIBLE;
    @Bindable
    public int getCityInfoListVisible() {
        return cityInfoListVisible;
    }
    public void setCityInfoListVisible(int cityInfoListVisible) {
        this.cityInfoListVisible = cityInfoListVisible;
        notifyPropertyChanged(BR.cityInfoListVisible);
    }

    @Bindable
    public int getSearchCityListVisible() {
        return searchCityListVisible;
    }

   public void setSearchCityListVisible(int searchCityListVisible) {
        this.searchCityListVisible = searchCityListVisible;
        notifyPropertyChanged(BR.searchCityListVisible);
    }

    private void initData() {
        dbHelper = new DBManager(mActivity);
        dbHelper.openDatabase();
        dbHelper.closeDatabase();
        setCityAndFirstLetterData();
    }

    private void initView() {
     //展示数据
    searchActivityCityLocationBinding.lvActivityCityLocationCityinfo.setAdapter(new LocationCityInfoAdapter(listCityInfo));
    searchActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setAdapter(new LocationCityFirstLetterAdapter(listCityNameFirstLetter));
    }

    //设置城市数据
    private void setCityAndFirstLetterData() {
        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
        ArrayList<CityClassBean> citys = getCity();//城市列表
        database.close();

        //A到Z的集合
        listCityNameFirstLetter = new ArrayList<Character>();
        for (char cha = 'A'; cha <= 'Z'; cha++) {
            listCityNameFirstLetter.add(cha);
        }

        //获取城市并包含对应首字母的集合
        for (CityClassBean cityClass : citys) {
            String  city_name = cityClass.city_name;
            int  city_id = cityClass.city_id;
            String s = Pinyin4JUtils.cn2PYInitial(city_name);
            String firstLetter = s.substring(0, 1);
            listCity.add(new ListCityBean(firstLetter,city_name,city_id));
        }

        //转化成带有城市名首字母的列表
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

    //事件监听，快速点击索引联动
    private void listener() {
    //点击城市列表，显示选中的城市名
    searchActivityCityLocationBinding.lvActivityCityLocationCityinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position!=0){
                LocationCityInfo locationCityInfo = listCityInfo.get(position-1);
                boolean isFirstLetter = locationCityInfo.isFirstLetter;
                if(!isFirstLetter){
                    cityName = locationCityInfo.CityName;
                    if(cityName.endsWith("市")){
                        cityName = cityName.substring(0, cityName.length()-1);
                    }

                    if(cityName.endsWith("区")){
                        if(!cityName.endsWith("地区")){
                            cityName = cityName.substring(0, cityName.length()-1);
                        }
                    }

                    if(cityName.endsWith("县")){
                        if(cityName.endsWith("自治县")){
                            cityName = cityName.substring(0, cityName.length()-1);
                        }
                    }

                    listener.OnClick(cityName);
            }
        }
        }
    });

    //点击当前城市
        searchActivityCityLocationBinding.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityName.endsWith("市")){
                    cityName = cityName.substring(0, cityName.length()-1);
                }

                if(cityName.endsWith("区")){
                    if(!cityName.endsWith("地区")){
                        cityName = cityName.substring(0, cityName.length()-1);
                    }
                }

                if(cityName.endsWith("县")){
                    if(cityName.endsWith("自治县")){
                        cityName = cityName.substring(0, cityName.length()-1);
                    }
                }
                listener.OnClick(cityName);
            }
        });
    }

    //显示中间的字
    private android.os.Handler mHanler = new android.os.Handler();
    private void showTextView(String text) {
        searchActivityCityLocationBinding.tv.setVisibility(View.VISIBLE);
        searchActivityCityLocationBinding.tv.setText(text);
        mHanler.removeCallbacksAndMessages(null);
        mHanler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchActivityCityLocationBinding.tv.setVisibility(View.GONE);
            }
        },1000);
    }

    public interface onClickCListener{
        void OnClick(String cityName);
    }

    private onClickCListener listener;
    public void setOnClickListener(onClickCListener listener) {
        this.listener = listener;
    }

}
