package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.databinding.ItemDemandChooseServiceBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class ItemDemandChooseServiceModel extends BaseObservable {

    ItemDemandChooseServiceBinding mItemDemandChooseServiceBinding;
    Activity mActivty;
    DemandPurposeListBean.PurposeInfo mDemandChooseServiceBean;
    long tid;//需求ID

    public ItemDemandChooseServiceModel(ItemDemandChooseServiceBinding itemDemandChooseServiceBinding, Activity activty, DemandPurposeListBean.PurposeInfo demandChooseServiceBean, long tid) {
        this.mItemDemandChooseServiceBinding = itemDemandChooseServiceBinding;
        this.mActivty = activty;
        this.mDemandChooseServiceBean = demandChooseServiceBean;
        this.tid = tid;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        displayPurposeData();
    }

    private void displayPurposeData() {
        if (mDemandChooseServiceBean.isauth == 1) {
            mItemDemandChooseServiceBinding.ivPurposeIsauthIcon.setVisibility(View.VISIBLE);
        } else {
            mItemDemandChooseServiceBinding.ivPurposeIsauthIcon.setVisibility(View.INVISIBLE);
        }

        mItemDemandChooseServiceBinding.tvPurposeQuote.setText(mDemandChooseServiceBean.quote + "元");
        mItemDemandChooseServiceBinding.tvPurposeName.setText(mDemandChooseServiceBean.name);
        mItemDemandChooseServiceBinding.tvCompanyProfessionInfo.setText(mDemandChooseServiceBean.company + mDemandChooseServiceBean.profession);
        //显示每一期的分期比例
        String[] instalmentratioArray = mDemandChooseServiceBean.instalment.split(",");
        String instalmentratioStr = "";
        for (int i = 0; i < instalmentratioArray.length; i++) {
            String ratio = instalmentratioArray[i];
            if (TextUtils.isEmpty(ratio)) {
                continue;
            }
            LogKit.v("instalmentratioStr：" + ratio);
            ratio = (ratio.split("\\."))[1];
            if (i < instalmentratioArray.length - 1) {
                instalmentratioStr += ratio + "%/";
            } else {
                instalmentratioStr += ratio + "%";
            }
        }
        mItemDemandChooseServiceBinding.tvPurposeInstalmentratio.setText(instalmentratioStr);
        if (mDemandChooseServiceBean.status == 1) {
            mItemDemandChooseServiceBinding.tvPurposeChoose.setText("选择Ta");
        } else if (mDemandChooseServiceBean.status == 2) {
            mItemDemandChooseServiceBinding.tvPurposeChoose.setText("已选择");
        }
    }

    //需求方选择服务者
    public void selectService(View v) {
        if (mDemandChooseServiceBean.status == 1) {
            DemandEngine.demandPartySelectServiceParty(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    mItemDemandChooseServiceBinding.tvPurposeChoose.setText("已选择");
                    mDemandChooseServiceBean.status = 2;
                }

                @Override
                public void executeResultError(String result) {

                }
            }, tid + "", mDemandChooseServiceBean.uid + "");
        }
    }

    //需求方淘汰服务方（右上角的删除按钮）
    public void eliminateService(View v) {
        DemandEngine.demandEliminateService(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ViewGroup parentView = (ViewGroup) mItemDemandChooseServiceBinding.getRoot().getParent();
                parentView.removeView(mItemDemandChooseServiceBinding.getRoot());
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", mDemandChooseServiceBean.uid + "");
    }

}
