package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.ui.activity.MyAddSkillActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashNumberPicker;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillModel extends BaseObservable {

    private SlashAddPicLayout addPic;
    private ActivityMyAddSkillBinding activityMyAddSkillBinding;
    private MyAddSkillActivity myAddSkillActivity;
    private SlashNumberPicker npUnit;
    private String[] unitArr = {"次","个","幅"};

    public MyAddSkillModel(ActivityMyAddSkillBinding activityMyAddSkillBinding,MyAddSkillActivity myAddSkillActivity) {
        this.activityMyAddSkillBinding = activityMyAddSkillBinding;
        this.myAddSkillActivity = myAddSkillActivity;
        initView();
        initData();
    }

    private void initView() {
        addPic = activityMyAddSkillBinding.addPic;
        addPic.setActivity(myAddSkillActivity);
        addPic.initPic();
        npUnit = activityMyAddSkillBinding.npUnit;
    }

    private void initData() {

    npUnit.setDisplayedValues(unitArr);
    npUnit.setMinValue(0);
    npUnit.setMaxValue(unitArr.length - 1);
    npUnit.setValue(1);
    }

    private int rlChooseMainLabelVisible = View.INVISIBLE;

    @Bindable
    public int getRlChooseMainLabelVisible() {
        return rlChooseMainLabelVisible;
    }

    public void setRlChooseMainLabelVisible(int rlChooseMainLabelVisible) {
        this.rlChooseMainLabelVisible = rlChooseMainLabelVisible;
        notifyPropertyChanged(BR.rlChooseMainLabelVisible);
    }

    //点击选择单位
    public void chooseUnit(View view){
        setRlChooseMainLabelVisible(View.VISIBLE);
    }

    //确定单位
    public void sure(View view){
        setRlChooseMainLabelVisible(View.INVISIBLE);
        int value = npUnit.getValue();
        String s = unitArr[value];
        LogKit.d("===="+s);

    }


}
