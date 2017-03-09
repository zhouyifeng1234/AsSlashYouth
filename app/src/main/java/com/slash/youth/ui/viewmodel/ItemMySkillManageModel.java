package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.net.sip.SipAudioCall;
import android.view.View;

import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.utils.DialogUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class ItemMySkillManageModel extends BaseObservable {

    private ItemMySkillManageBinding itemMySkillManageBinding;
    private ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList;
    private MySkillManageActivity mySkillManageActivity;

    public ItemMySkillManageModel(ItemMySkillManageBinding itemMySkillManageBinding,ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList,MySkillManageActivity mySkillManageActivity) {
        this.itemMySkillManageBinding = itemMySkillManageBinding;
        this.skillManageList = skillManageList;
        this.mySkillManageActivity = mySkillManageActivity;
    }

}
