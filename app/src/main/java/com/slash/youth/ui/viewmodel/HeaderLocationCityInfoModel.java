package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/10/18.
 */
public class HeaderLocationCityInfoModel extends BaseObservable {
   private  HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private Intent intent;
    private CityLocationActivity cityLocationActivity;
    private String[] cityNames = new String[3];
    private String currentyCity;

    public HeaderLocationCityInfoModel(HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding, Intent intent, CityLocationActivity cityLocationActivity) {
        this.headerListviewLocationCityInfoListBinding = headerListviewLocationCityInfoListBinding;
        this.intent = intent;
        this.cityLocationActivity = cityLocationActivity;
        initData();
        initView();
        listener();
    }


    private void initData() {
        currentyCity = SpUtils.getString("currentyCity", "");


        cityNames[0] = "城市1";
        cityNames[1] = "城市2";
        cityNames[2] = "城市3";
    }

    private void initView() {
        if(currentyCity!=null){
            setCurrentLocation(currentyCity);
        }

        if (cityNames.length!=0) {
            for (String cityName : cityNames) {
                TextView textView = getTextView(cityName);
                headerListviewLocationCityInfoListBinding.llCityContainer.addView(textView);
            }
        }
    }

    private void listener() {
        //当前城市定位
        headerListviewLocationCityInfoListBinding.tvCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = headerListviewLocationCityInfoListBinding.tvCurrentCity.getText().toString();
                intent.putExtra("currentCityAccess",cityName);
                cityLocationActivity.setResult(Constants.CURRENT_ACCESS_CITY,intent);
                cityLocationActivity.finish();
            }
        });
    }


    @NonNull
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
           intent.putExtra("currentCityAccess",cityName);
            cityLocationActivity.setResult(Constants.CURRENT_ACCESS_CITY,intent);
            cityLocationActivity.finish();
        }

    }

    //设置当前定位
    public  void setCurrentLocation(String location){
        headerListviewLocationCityInfoListBinding.tvCurrentCity.setText(location);
    }


    //存放经常访问的城市
    public void setCurrentAccessCity(String cityName){
        ArrayList<String> cityLists = new ArrayList<>();
        cityLists.add(cityName);

    }




}
