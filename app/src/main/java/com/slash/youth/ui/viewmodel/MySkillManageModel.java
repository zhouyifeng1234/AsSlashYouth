package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMySkillManageBinding;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.adapter.MySkillManageAdapter;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class MySkillManageModel extends BaseObservable {
    private ActivityMySkillManageBinding activityMySkillManageBinding;
    private  ArrayList<SkillManageBean> skillManageList = new ArrayList<>();
    private MySkillManageAdapter mySkillManageAdapter;
    private MySkillManageActivity mySkillManageActivity;
    private String titleName;

    public MySkillManageModel(ActivityMySkillManageBinding activityMySkillManageBinding,MySkillManageActivity mySkillManageActivity,String titleName) {
        this.activityMySkillManageBinding = activityMySkillManageBinding;
        this.mySkillManageActivity = mySkillManageActivity;
        this.titleName = titleName;
        initData();
        initView();
    }


    private void initData() {

        switch (titleName){

            case "技能管理":
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());

                break;
            case "管理我发布的任务":
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                break;
        }
    }

    private void initView() {
        mySkillManageAdapter = new MySkillManageAdapter(skillManageList,mySkillManageActivity);
        activityMySkillManageBinding.lvSkillManage.setAdapter(mySkillManageAdapter);




    }
}
