package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.viewmodel.ItemMySkillManageModel;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class MySkillManageHolder extends BaseHolder<SkillManageBean> {

    public ItemMySkillManageBinding itemMySkillManageBinding;
    private int position;
    private MySkillManageActivity mySkillManageActivity;
    private final String myActivityTitle;
    private ArrayList<SkillManageBean> skillManageList;

    public MySkillManageHolder(int position, MySkillManageActivity mySkillManageActivity,ArrayList<SkillManageBean> skillManageList) {
        this.position = position;
        this.mySkillManageActivity = mySkillManageActivity;
        this.skillManageList = skillManageList;
        myActivityTitle = SpUtils.getString("myActivityTitle", "");
    }


    @Override
    public View initView() {
        itemMySkillManageBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_skill_manage, null, false);
        ItemMySkillManageModel itemMySkillManageModel = new ItemMySkillManageModel(itemMySkillManageBinding,position,skillManageList,mySkillManageActivity);
        itemMySkillManageBinding.setItemMySkillManageModel(itemMySkillManageModel);
        return itemMySkillManageBinding.getRoot();
    }

    @Override
    public void refreshView(SkillManageBean data) {
        setView(myActivityTitle);


    }

    //设置界面
    private void setView(String myActivityTitle) {
           switch (myActivityTitle){
            case "技能管理":
                itemMySkillManageBinding.tvMyBtn.setText("发布");
              break;

            case "管理我发布的任务":
                itemMySkillManageBinding.tvMyBtn.setText("上架");
                //itemMySkillManageBinding.tvMyBtn.setText("下架");
                break;
        }
    }


}
