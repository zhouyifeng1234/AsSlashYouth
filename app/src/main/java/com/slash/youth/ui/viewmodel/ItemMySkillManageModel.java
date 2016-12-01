package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.net.sip.SipAudioCall;
import android.view.View;

import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.utils.DialogUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class ItemMySkillManageModel extends BaseObservable {

    private ItemMySkillManageBinding itemMySkillManageBinding;
    private ArrayList<SkillManageBean> skillManageList;
    private int position;
    private MySkillManageActivity mySkillManageActivity;

    public ItemMySkillManageModel(ItemMySkillManageBinding itemMySkillManageBinding,int position,ArrayList<SkillManageBean> skillManageList,MySkillManageActivity mySkillManageActivity) {
        this.itemMySkillManageBinding = itemMySkillManageBinding;
        this.position = position;
        this.skillManageList = skillManageList;
        this.mySkillManageActivity = mySkillManageActivity;
    }

    public void delete(View view){
        showDialog();

    }

    private void showDialog() {

        DialogUtils.showDialogFive(mySkillManageActivity, "是否删除该技能", "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                skillManageList.remove(position);
            }

            @Override
            public void CancleDown() {

            }
        });
    }

}
