package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMapBinding;
import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.adapter.MapNearLocationAdapter;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class ActivityMapModel extends BaseObservable {
    ActivityMapBinding mActivityMapBinding;
    MapActivity mActivity;
    private int llMapInfoVisible = View.VISIBLE;
    private int svSearchListVisible = View.INVISIBLE;

    public ActivityMapModel(ActivityMapBinding activityMapBinding, MapActivity activity) {
        this.mActivityMapBinding = activityMapBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
//        initNearLocationData();
    }

    public void initNearLocationData(ArrayList<NearLocationBean> listNearLocation) {
        //TODO 附近地点数据可由地图API获取，第一个为我的当前位置
//        ArrayList<NearLocationBean> listNearLocation = new ArrayList<NearLocationBean>();
//        listNearLocation.add(new NearLocationBean("我的位置", "苏州工业园区", "0.50KM"));//第一个为我的当前位置
//        listNearLocation.add(new NearLocationBean("银行", "苏州工业园区", "0.50KM"));
//        listNearLocation.add(new NearLocationBean("酒店", "苏州工业园区", "0.50KM"));
//        listNearLocation.add(new NearLocationBean("KTV", "苏州工业园区", "0.50KM"));
//        listNearLocation.add(new NearLocationBean("饭店", "苏州工业园区", "0.50KM"));
        mActivityMapBinding.lvActivityMapNearLocation.setAdapter(new MapNearLocationAdapter(listNearLocation));
    }

    @Bindable
    public int getSvSearchListVisible() {
        return svSearchListVisible;
    }

    public void setSvSearchListVisible(int svSearchListVisible) {
        this.svSearchListVisible = svSearchListVisible;
        notifyPropertyChanged(BR.svSearchListVisible);
    }

    @Bindable
    public int getLlMapInfoVisible() {
        return llMapInfoVisible;
    }

    public void setLlMapInfoVisible(int llMapInfoVisible) {
        this.llMapInfoVisible = llMapInfoVisible;
        notifyPropertyChanged(BR.llMapInfoVisible);
    }

    public void okChooseLocation(View v) {
        Intent intentResult = new Intent();
        intentResult.putExtra("mCurrentAddress", mActivity.mCurrentAddress);
        mActivity.setResult(20, intentResult);
        mActivity.finish();
    }

    public void goBack(View v) {
        mActivity.finish();
    }
}
