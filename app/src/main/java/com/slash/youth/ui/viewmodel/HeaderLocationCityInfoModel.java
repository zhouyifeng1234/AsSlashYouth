package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.domain.CityHistoryEntity;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.SearchHistoryEntity;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zss on 2016/10/18.
 */
public class HeaderLocationCityInfoModel extends BaseObservable {
   private  HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private Intent intent;
    private Activity mActivity;
    private  ArrayList<String> cityLists = new ArrayList<>();
    private  ArrayList<String> currenty_access_lists = new ArrayList<>();
    private String currentyCity;
    private int locationType;
    private String fileName ="data/data/com.slash.youth";
    private File file;
    private CityHistoryEntityDao cityHistoryEntityDao;

    public HeaderLocationCityInfoModel(HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding, Intent intent, Activity mActivity,CityHistoryEntityDao cityHistoryEntityDao) {
        this.headerListviewLocationCityInfoListBinding = headerListviewLocationCityInfoListBinding;
        this.intent = intent;
        this.mActivity = mActivity;
        this.cityHistoryEntityDao = cityHistoryEntityDao;
        initData();
        initView();
        listener();
    }

    private void initData() {
        currentyCity = SpUtils.getString("currentyCity", "");
        initCityData();
    }

    private void initView() {
        if(currentyCity!=null){
            setCurrentLocation(currentyCity);
        }

        //集合不为空就获取城市数据
        if (currenty_access_lists.size()!=0) {
            for (String cityName : currenty_access_lists) {
                TextView textView = getTextView(cityName);
                headerListviewLocationCityInfoListBinding.llCityContainer.addView(textView);
                headerListviewLocationCityInfoListBinding.llCityContainer.setVisibility(View.VISIBLE);
                headerListviewLocationCityInfoListBinding.tvTitle.setVisibility(View.VISIBLE);
            }
        }
        locationType = intent.getIntExtra("locationType", -1);
    }

    //保存最近访问的城市
    public void saveCity(String city) {
    LogKit.d("======save======="+city);

    CityHistoryEntity cityHistoryEntity = new CityHistoryEntity();
    cityHistoryEntity.setCity(city);
    cityHistoryEntityDao.insert(cityHistoryEntity);

        /*file = new File(fileName, "CurrentyCity.text");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!file.exists()){
            file.mkdir();
        }
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(city);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //IO 读写
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(city);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    //读取最近访问的城市
    private void initCityData() {
        cityLists.clear();
        currenty_access_lists.clear();

        List<CityHistoryEntity> cityHistoryEntities = cityHistoryEntityDao.loadAll();
        for (CityHistoryEntity cityHistoryEntity : cityHistoryEntities) {
            String city = cityHistoryEntity.getCity();
            LogKit.d("===read====="+city);
        }

        Collections.reverse(cityHistoryEntities);
        if(cityHistoryEntities.size()!=0){
            if(cityHistoryEntities.size()>=0&&cityHistoryEntities.size()<3){
                for (CityHistoryEntity cityHistoryEntity : cityHistoryEntities) {
                    String city = cityHistoryEntity.getCity();
                    currenty_access_lists.add(city);
                }
            }else {
                for (int i = 0; i <= 3; i++) {
                    CityHistoryEntity cityHistoryEntity = cityHistoryEntities.get(i);
                    String city = cityHistoryEntity.getCity();
                    currenty_access_lists.add(city);
                }
            }
        }else {
        LogKit.d("没有最近访问的城市");
        }

       /* cityLists.clear();
        currenty_access_lists.clear();
        file = new File(fileName, "CurrentyCity.text");
        if(!file.exists()){
            file.mkdir();
        }
        //存储集合
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line= br.readLine())!=null){
                if(TextUtils.isEmpty(line)){
                    continue;
                }
                cityLists.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //倒序，最多取3个
        Collections.reverse(cityLists);
        if(cityLists.size()!=0){
            if(cityLists.size()<3&&cityLists.size()>=0){
                for (String city : cityLists) {
                    currenty_access_lists.add(city);
                }
            }else if(cityLists.size()>=3){
                for (int i = 0; i < 3; i++) {
                    String city = cityLists.get(i);
                    currenty_access_lists.add(city);
                }
            }
        }else {
            LogKit.d("没有最近访问的城市");
        }*/
    }

    private void listener() {
        //当前城市定位
        headerListviewLocationCityInfoListBinding.tvCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = headerListviewLocationCityInfoListBinding.tvCurrentCity.getText().toString();
                switch (locationType){
                    case 0://首页更多
                        listener.OnMoreCityClick(cityName);
                        break;
                    case 1://搜索
                        listener.OnSearchCityClick(cityName);
                        break;
                    case -1://个人编辑
                        intent.putExtra("currentCityAccess",cityName);
                        mActivity.setResult(Constants.CURRENT_ACCESS_CITY,intent);
                        mActivity.finish();
                        break;
                }
            }
        });
    }

    private TextView getTextView(String text) {
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-2, -2);
        ll.rightMargin = CommonUtils.dip2px(20);
        TextView textView = new TextView(CommonUtils.getContext());
        textView.setOnClickListener(new CityNameListener());
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTextSize(CommonUtils.dip2px(5));
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.shape_search_city);
        textView.setPadding(CommonUtils.dip2px(20),CommonUtils.dip2px(8),CommonUtils.dip2px(20),CommonUtils.dip2px(8));
        textView.setLayoutParams(ll);
        return textView;
    }


    //最近访问的城市点击事件
    public class CityNameListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String cityName = ((TextView)v).getText().toString();
            switch (locationType){
                case 0://首页更多
                    listener.OnMoreCityClick(cityName);
                    break;
                case 1://搜索
                    listener.OnSearchCityClick(cityName);
                    break;
                case -1://个人编辑
                    intent.putExtra("currentCityAccess",cityName);
                    mActivity.setResult(Constants.CURRENT_ACCESS_CITY,intent);
                    mActivity.finish();
                    break;
            }
        }
    }

    //设置当前定位
    public  void setCurrentLocation(String location){
        headerListviewLocationCityInfoListBinding.tvCurrentCity.setText(location);
    }

    //监听回调返回键
    public interface OnCityClickCListener{
        void OnSearchCityClick(String city);
        void OnMoreCityClick(String city);
    }

    private OnCityClickCListener listener;
    public void setOnCityClickCListener(OnCityClickCListener listener) {
        this.listener = listener;
    }
}
