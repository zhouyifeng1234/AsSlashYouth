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
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ItemManagePublishModel extends BaseObservable {
    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList;
    private ItemManagePublishHolderBinding itemManagePublishHolderBinding;
    private int actionType;

    public ItemManagePublishModel( MySkillManageActivity mySkillManageActivity, ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList, ItemManagePublishHolderBinding itemManagePublishHolderBinding) {
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
        this.itemManagePublishHolderBinding = itemManagePublishHolderBinding;
    }

    //上架，下架
    public void UpAndDown(long id,int action){
        actionType = action;
        MyManager.onManagerMyPublishTaskItemUpAndDown(new onAddMyCollectionList(),id,action);
    }

    public class onAddMyCollectionList implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        switch (actionType){
                            case 0:
                                ToastUtils.shortToast("下架成功");
                                itemManagePublishHolderBinding.tvMyBtn.setText(MyManager.UP);
                                itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#31C6E4"));
                                break;
                            case 1:
                                ToastUtils.shortToast("上架成功");
                                itemManagePublishHolderBinding.tvMyBtn.setText(MyManager.DOWN);
                                itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#999999"));
                                break;
                        }
                        break;
                    case 5:
                        //状态错误
                        ToastUtils.shortToast("状态错误");
                        break;
                    case 4:
                        //服务端错误
                        ToastUtils.shortToast("服务端错误");
                        break;
                    case 6:
                        //未绑定手机号
                        ToastUtils.shortToast("未绑定手机号");
                        break;
                    case 7:
                        //未实名认证
                        ToastUtils.shortToast("请进行实名认证");
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
