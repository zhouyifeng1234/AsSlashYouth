package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemManagePublishHolderBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.utils.DialogUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ItemManagePublishModel extends BaseObservable {
    private int position;
    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<MyCollectionBean> managePublishList;
    private ItemManagePublishHolderBinding itemManagePublishHolderBinding;

    public ItemManagePublishModel(int position, MySkillManageActivity mySkillManageActivity, ArrayList<MyCollectionBean> managePublishList, ItemManagePublishHolderBinding itemManagePublishHolderBinding) {
        this.position = position;
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
        this.itemManagePublishHolderBinding = itemManagePublishHolderBinding;
    }

    public void delete(View view){
        showDialog();
    }

    private void showDialog() {

        DialogUtils.showDialogFive(mySkillManageActivity, "是否删除该技能", "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                managePublishList.remove(position);
            }

            @Override
            public void CancleDown() {

            }
        });
    }

}
