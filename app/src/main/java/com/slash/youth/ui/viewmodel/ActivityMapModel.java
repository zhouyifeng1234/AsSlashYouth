package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMapBinding;
import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.ui.adapter.MapNearLocationAdapter;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class ActivityMapModel extends BaseObservable {
    ActivityMapBinding mActivityMapBinding;

    public ActivityMapModel(ActivityMapBinding activityMapBinding) {
        this.mActivityMapBinding = activityMapBinding;
        initView();
    }

    private void initView() {
        initNearLocationData();
    }

    private void initNearLocationData() {
        //TODO 附近地点数据可由地图API获取，第一个为我的当前位置
        ArrayList<NearLocationBean> listNearLocation = new ArrayList<NearLocationBean>();
        listNearLocation.add(new NearLocationBean("我的位置", "苏州工业园区", "0.50KM"));//第一个为我的当前位置
        listNearLocation.add(new NearLocationBean("银行", "苏州工业园区", "0.50KM"));
        listNearLocation.add(new NearLocationBean("酒店", "苏州工业园区", "0.50KM"));
        listNearLocation.add(new NearLocationBean("KTV", "苏州工业园区", "0.50KM"));
        listNearLocation.add(new NearLocationBean("饭店", "苏州工业园区", "0.50KM"));
        mActivityMapBinding.lvActivityMapNearLocation.setAdapter(new MapNearLocationAdapter(listNearLocation));
    }


}
