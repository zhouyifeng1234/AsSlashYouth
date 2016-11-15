package com.slash.youth.ui.activity;

import android.app.Activity;
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
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.LocationCityInfoAdapter;
import com.slash.youth.ui.adapter.LocationCitySearchListAdapter;
import com.slash.youth.ui.viewmodel.ActivityCityLocationModel;
import com.slash.youth.ui.viewmodel.HeaderLocationCityInfoModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class  CityLocationActivity extends Activity {

    private ActivityCityLocationBinding mActivityCityLocationBinding;
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private ActivityCityLocationModel mActivityCityLocationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListener();
    }

    private void initData() throws IOException {

        InputStream is = getAssets().open("province_city_zone.db");
        LogKit.d("考虑到富商巨贾 == "+is.toString());










        //城市名称模拟数据
        ArrayList<LocationCityInfo> listCityInfo = new ArrayList<LocationCityInfo>();
        listCityInfo.add(new LocationCityInfo(true, "A", ""));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(true, "S", ""));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(true, "X", ""));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        //城市名称首字母模拟数据
        ArrayList<Character> listCityNameFirstLetter = new ArrayList<Character>();
        for (char cha = 'A'; cha <= 'Z'; cha++) {
            listCityNameFirstLetter.add(cha);
        }

        mHashFirstLetterIndex = new HashMap<String, Integer>();
        mHashFirstLetterIndex.put("A", 0);
        mHashFirstLetterIndex.put("S", 13);
        mHashFirstLetterIndex.put("X", 26);

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

    private void initListener() {
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv= (TextView) view;
//                ToastUtils.shortToast(position + " "+tv.getText());
//                int clickIndex = position - mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.getHeaderViewsCount();
                if (position > 0) {
                    TextView tvFirstLetter = (TextView) view;
                    String firstLetter = tvFirstLetter.getText().toString();
                    if (mHashFirstLetterIndex.containsKey(firstLetter)) {
                        Integer firstLetterIndex = mHashFirstLetterIndex.get(firstLetter);
//                    ToastUtils.shortToast(firstLetterIndex + "");
                        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(firstLetterIndex + mActivityCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
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
