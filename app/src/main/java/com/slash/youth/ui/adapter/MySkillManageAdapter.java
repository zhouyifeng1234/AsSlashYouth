package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.MySkillManageHolder;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class MySkillManageAdapter extends SlashBaseAdapter<SkillManageBean> {

    private MySkillManageActivity mySkillManageActivity;
    public MySkillManageAdapter(ArrayList<SkillManageBean> listData, MySkillManageActivity mySkillManageActivity) {
        super(listData);
        this.mySkillManageActivity = mySkillManageActivity;
    }

    @Override
    public ArrayList<SkillManageBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {

        return new MySkillManageHolder(position,mySkillManageActivity);
    }
}
