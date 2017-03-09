package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemDemandChooseServiceBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class ItemDemandChooseServiceModel extends BaseObservable {

    ItemDemandChooseServiceBinding mItemDemandChooseServiceBinding;
    Activity mActivty;
    DemandPurposeListBean.PurposeInfo mDemandChooseServiceBean;
    long tid;//需求ID
    String demandTitle;//需求标题

    public ItemDemandChooseServiceModel(ItemDemandChooseServiceBinding itemDemandChooseServiceBinding, Activity activty, DemandPurposeListBean.PurposeInfo demandChooseServiceBean, long tid, String demandTitle) {
        this.mItemDemandChooseServiceBinding = itemDemandChooseServiceBinding;
        this.mActivty = activty;
        this.mDemandChooseServiceBean = demandChooseServiceBean;
        this.tid = tid;
        this.demandTitle = demandTitle;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        displayPurposeData();
    }

    //打开聊天界面 聊一聊
    public void haveAChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_CHAT);

        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", mDemandChooseServiceBean.uid + "");
        Bundle taskInfoBundle = new Bundle();
        taskInfoBundle.putLong("tid", tid);
        taskInfoBundle.putInt("type", 1);
        taskInfoBundle.putString("title", demandTitle);
        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
        mActivty.startActivity(intentChatActivity);
    }

    private void displayPurposeData() {
        if (mDemandChooseServiceBean.isauth == 1) {
            mItemDemandChooseServiceBinding.ivPurposeIsauthIcon.setVisibility(View.VISIBLE);
        } else {
            mItemDemandChooseServiceBinding.ivPurposeIsauthIcon.setVisibility(View.INVISIBLE);
        }

        mItemDemandChooseServiceBinding.tvPurposeQuote.setText((int) mDemandChooseServiceBean.quote + "元");
        mItemDemandChooseServiceBinding.tvPurposeName.setText(mDemandChooseServiceBean.name);
        mItemDemandChooseServiceBinding.tvCompanyProfessionInfo.setText(mDemandChooseServiceBean.company + mDemandChooseServiceBean.position);
        LogKit.v("company:" + mDemandChooseServiceBean.company + "  profession:" + mDemandChooseServiceBean.position);
//        if (TextUtils.isEmpty(mDemandChooseServiceBean.industry) || TextUtils.isEmpty(mDemandChooseServiceBean.direction)) {
//            mItemDemandChooseServiceBinding.tvIndustryDirection.setText(mDemandChooseServiceBean.industry + mDemandChooseServiceBean.direction);
//        } else {
//            mItemDemandChooseServiceBinding.tvIndustryDirection.setText(mDemandChooseServiceBean.industry + "|" + mDemandChooseServiceBean.direction);
//        }
//        SimpleDateFormat sdfBidStarttime = new SimpleDateFormat("01月19日 15:45");
        SimpleDateFormat sdfBidStarttime = new SimpleDateFormat("MM月dd日 HH:mm");
        String bidStarttime = sdfBidStarttime.format(mDemandChooseServiceBean.starttime);
        mItemDemandChooseServiceBinding.tvBidDemandStarttime.setText(bidStarttime);

        //显示每一期的分期比例
        String[] instalmentratioArray = mDemandChooseServiceBean.instalment.split(",");
        String instalmentratioStr = "";
        for (int i = 0; i < instalmentratioArray.length; i++) {
            String ratio = instalmentratioArray[i];
            if (TextUtils.isEmpty(ratio)) {
                continue;
            }
            LogKit.v("instalmentratioStr：" + ratio);
//            ratio = (ratio.split("\\."))[1];
            ratio = (int) (Double.parseDouble(ratio) * 100) + "";
            if (i < instalmentratioArray.length - 1) {
                instalmentratioStr += ratio + "%/";
            } else {
                instalmentratioStr += ratio + "%";
            }
        }
        if (TextUtils.isEmpty(instalmentratioStr) || instalmentratioStr.equals("100%")) {
            mItemDemandChooseServiceBinding.tvPurposeInstalmentratio.setVisibility(View.INVISIBLE);
            mItemDemandChooseServiceBinding.tvInstalmentLabelText.setText("不分期");
        } else {
            mItemDemandChooseServiceBinding.tvPurposeInstalmentratio.setText(instalmentratioStr);
        }
        //加载头像
        BitmapKit.bindImage(mItemDemandChooseServiceBinding.ivServiceUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mDemandChooseServiceBean.avatar);
        //纠纷处理方式
        if (mDemandChooseServiceBean.bp == 1) {//1平台方式
            mItemDemandChooseServiceBinding.ivBpIcon.setImageResource(R.mipmap.platform_icon);
            mItemDemandChooseServiceBinding.tvBpText.setText("平台处理纠纷");
        } else {//2协商方式
            mItemDemandChooseServiceBinding.ivBpIcon.setImageResource(R.mipmap.negotiation_icon);
            mItemDemandChooseServiceBinding.tvBpText.setText("协商处理纠纷");
        }

        if (mDemandChooseServiceBean.status == 1) {//服务方竞标
            mItemDemandChooseServiceBinding.tvPurposeChoose.setText("选择Ta");
        } else if (mDemandChooseServiceBean.status == 2) {//需求方选择了服务方
            mItemDemandChooseServiceBinding.tvPurposeChoose.setText("已选择");
        } else if (mDemandChooseServiceBean.status == 3) {//服务方确认了需求方
            //这里不需要处理，因为当服务方选择确认了需求方，就会跳转到四个圈的需求订单详情页面
        } else if (mDemandChooseServiceBean.status == 4) {//服务方拒绝了需求方
            mItemDemandChooseServiceBinding.tvPurposeChoose.setVisibility(View.GONE);
            setRefusedIconVisibility(View.VISIBLE);
        }
    }

    //需求方选择服务者
    public void selectService(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_CHOOSE_TA);

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
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_ELIMINATE_TA);

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

    /**
     * 跳转到用户个人信息页面
     *
     * @param v
     */
    public void gotoUserInfoPager(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", mDemandChooseServiceBean.uid);
        mActivty.startActivity(intentUserInfoActivity);
    }

    private int refusedIconVisibility = View.GONE;

    @Bindable
    public int getRefusedIconVisibility() {
        return refusedIconVisibility;
    }

    public void setRefusedIconVisibility(int refusedIconVisibility) {
        this.refusedIconVisibility = refusedIconVisibility;
        notifyPropertyChanged(BR.refusedIconVisibility);
    }
}
