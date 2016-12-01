package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemManagePublishHolderBinding;
import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.viewmodel.ItemManagePublishModel;
import com.slash.youth.ui.viewmodel.ItemMySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class ManagePublishHolder extends BaseHolder<MyCollectionBean> {

    private int position;
    private MySkillManageActivity mySkillManageActivity;
    private final String myActivityTitle;
    private ArrayList<MyCollectionBean> managePublishList;
    public ItemManagePublishHolderBinding itemManagePublishHolderBinding;
    private ItemManagePublishModel itemManagePublishModel;

    public ManagePublishHolder(int position, MySkillManageActivity mySkillManageActivity,ArrayList<MyCollectionBean> managePublishList) {
        this.position = position;
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
        myActivityTitle = SpUtils.getString("myActivityTitle", "");
    }


    @Override
    public View initView() {
        itemManagePublishHolderBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_manage_publish_holder, null, false);
        itemManagePublishModel = new ItemManagePublishModel(position,mySkillManageActivity,managePublishList,itemManagePublishHolderBinding);
        itemManagePublishHolderBinding.setItemManagePublishModel(itemManagePublishModel);
        return itemManagePublishHolderBinding.getRoot();
    }

    @Override
    public void refreshView(MyCollectionBean data) {
        setView(myActivityTitle);


    }

    //设置界面
    private void setView(String myActivityTitle) {
           switch (myActivityTitle){
            case "技能管理":
                itemManagePublishHolderBinding.tvMyBtn.setText("发布");
              break;

            case "管理我发布的任务":
                itemManagePublishHolderBinding.tvMyBtn.setText("上架");
                //itemMySkillManageBinding.tvMyBtn.setText("下架");
                break;
        }
    }


}
