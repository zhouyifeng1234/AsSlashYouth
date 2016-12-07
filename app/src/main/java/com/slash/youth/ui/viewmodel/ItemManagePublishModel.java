package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.view.View;

import com.slash.youth.databinding.ItemManagePublishHolderBinding;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ItemManagePublishModel extends BaseObservable {
    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList;
    private ItemManagePublishHolderBinding itemManagePublishHolderBinding;

    public ItemManagePublishModel( MySkillManageActivity mySkillManageActivity, ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList, ItemManagePublishHolderBinding itemManagePublishHolderBinding) {
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
        this.itemManagePublishHolderBinding = itemManagePublishHolderBinding;
    }

    //上架，下架
    public void UpAndDown(int id,int action){
        MyManager.onManagerMyPublishTaskItemUpAndDown(new onAddMyCollectionList(),id,action);
    }

    public class onAddMyCollectionList implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                LogKit.d("status:"+status);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
