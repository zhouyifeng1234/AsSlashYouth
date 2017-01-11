package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivitySheildBinding;
import com.slash.youth.domain.SheildPersonBean;
import com.slash.youth.ui.adapter.SheildAdapter;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class SheildModel extends BaseObservable {
    private ActivitySheildBinding activitySheildBinding;
    private  ArrayList<SheildPersonBean> sheildList = new ArrayList<>();
    private SheildAdapter sheildAdapter;

    public SheildModel(ActivitySheildBinding activitySheildBinding) {
        this.activitySheildBinding = activitySheildBinding;
        initData();
        initView();
    }

    private void initData() {
        sheildList.add(new SheildPersonBean());
        sheildList.add(new SheildPersonBean());
    }

    private void initView() {
        sheildAdapter = new SheildAdapter(sheildList);
        activitySheildBinding.lvSheild.setAdapter(sheildAdapter);
    }

}
