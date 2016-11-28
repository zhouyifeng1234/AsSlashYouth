package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.sip.SipSession;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.ui.activity.MyAddSkillActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashNumberPicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillModel extends BaseObservable {

    private SlashAddPicLayout addPic;
    private int totalNum = 300;
    private int num;
    private ActivityMyAddSkillBinding activityMyAddSkillBinding;
    private MyAddSkillActivity myAddSkillActivity;
    private SlashNumberPicker npUnit;
    private String[] unitArr = {"次","个","幅"};

    public MyAddSkillModel(ActivityMyAddSkillBinding activityMyAddSkillBinding,MyAddSkillActivity myAddSkillActivity) {
        this.activityMyAddSkillBinding = activityMyAddSkillBinding;
        this.myAddSkillActivity = myAddSkillActivity;
        initView();
        initData();
        listener();
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
        String unit = unitArr[value];
        if(!unit.isEmpty()){
            activityMyAddSkillBinding.tvUnit.setText(unit);
        }
    }

    //监听事件
    private void listener() {
        activityMyAddSkillBinding.etSkillManageDesc.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                num = s.length();
                selectionEnd = activityMyAddSkillBinding.etSkillManageDesc.getSelectionEnd();
                activityMyAddSkillBinding.tvDescNum.setText(num+"/"+totalNum);
                // selectionStart=activityReportTaBinding.etReportOther.getSelectionStart();
                if (wordNum.length() > totalNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    activityMyAddSkillBinding.etSkillManageDesc.setText(s);
                    activityMyAddSkillBinding.etSkillManageDesc.setSelection(tempSelection);//设置光标在最后
                }
                LogKit.d("========++++"+text);
            }
        });
    }

    //跳转到标签页面。关联标签
    public void jumpSkillLabel(View view){
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        myAddSkillActivity.startActivityForResult(intentSubscribeActivity, Constants.CONTACTS_ADD_SKILL_LABELS);
    }



}
