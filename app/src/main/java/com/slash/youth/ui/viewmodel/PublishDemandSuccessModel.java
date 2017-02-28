package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityPublishDemandSuccessBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.RecommendServiceUserBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.MessageActivity;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.ShareTaskUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandSuccessModel extends BaseObservable {

    ActivityPublishDemandSuccessBinding mActivityPublishDemandSuccessBinding;
    Activity mActivity;
    long demandId;
    boolean isUpdate;
    ArrayList<RecommendServiceUserBean.ServiceUserInfo> listRecommendServiceUser;

    public PublishDemandSuccessModel(ActivityPublishDemandSuccessBinding activityPublishDemandSuccessBinding, Activity activity) {
        this.mActivityPublishDemandSuccessBinding = activityPublishDemandSuccessBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        demandId = mActivity.getIntent().getLongExtra("demandId", -1);
        isUpdate = mActivity.getIntent().getBooleanExtra("isUpdate", false);
        getDataFromServer();
    }

    DemandDetailBean demandDetailBean = null;

    private void getDataFromServer() {
        //或者需求ID，获取需求详情
        DemandEngine.getDemandDetail(new BaseProtocol.IResultExecutor<DemandDetailBean>() {
            @Override
            public void execute(DemandDetailBean dataBean) {
                demandDetailBean = dataBean;
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("查看需求详情失败:" + result);
            }
        }, demandId + "");
        //获取推荐服务者列表，一共有5个推荐服务者
        DemandEngine.getRecommendServiceUser(new BaseProtocol.IResultExecutor<RecommendServiceUserBean>() {
            @Override
            public void execute(RecommendServiceUserBean dataBean) {
                listRecommendServiceUser = dataBean.data.list;
                if (listRecommendServiceUser != null && listRecommendServiceUser.size() > 0) {
                    setRecommendServiceInfoVisibility(View.VISIBLE);
                    mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setAdapter(new RecommendServicePartAdapter(listRecommendServiceUser));
                } else {
                    setRecommendServiceInfoVisibility(View.GONE);
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取推荐的服务者失败");
            }
        }, demandId + "", "5");
    }

    private void initView() {
        if (isUpdate) {
            setUpdateSuccessHintVisibility(View.VISIBLE);
            setPublishSuccessHintVisibility(View.GONE);
            mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setVisibility(View.GONE);
            mActivityPublishDemandSuccessBinding.tvPublishDemandText.setText("修改需求");
            mActivityPublishDemandSuccessBinding.tvPublishSuccessText.setText("修改成功");
            setUpdateSuccessVisibility(View.VISIBLE);
            setPublishSuccessVisibility(View.GONE);
        } else {
            setUpdateSuccessHintVisibility(View.GONE);
            setPublishSuccessHintVisibility(View.VISIBLE);
            mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setVisibility(View.VISIBLE);
            mActivityPublishDemandSuccessBinding.tvPublishDemandText.setText("发布需求");
            mActivityPublishDemandSuccessBinding.tvPublishSuccessText.setText("发布成功");
            setUpdateSuccessVisibility(View.GONE);
            setPublishSuccessVisibility(View.VISIBLE);
        }
        mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setVerticalScrollBarEnabled(false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("展示有效期至yyyy年MM月dd日HH:mm");
        String displayValidityDatetime = simpleDateFormat.format(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        mActivityPublishDemandSuccessBinding.tvDisplayValidityDatetime.setText(displayValidityDatetime);
        setAfterUpdateValidDate(displayValidityDatetime);
    }

    public void closeSuccessActivity(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_CLOSE);

        mActivity.finish();
    }

    public void gotoDemandDetail(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_CLICK_DETAIL);

        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
        intentDemandDetailActivity.putExtra("demandId", demandId);
        mActivity.startActivity(intentDemandDetailActivity);
    }

    /**
     * 点击“马上邀请他们按钮”，把需求分享给选中的服务者
     *
     * @param v
     */
    public void shareDemand(View v) {
        if (demandDetailBean != null && listRecommendServiceUser != null) {
            final ArrayList<Integer> listCheckedItemId = RecommendServicePartAdapter.listCheckedItemId;
            if (listCheckedItemId.size() == 1) {
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_INVITE_ONE);

                //只选中了一个，分享的时候会弹出聊天框
                int position = listCheckedItemId.get(0);
                RecommendServiceUserBean.ServiceUserInfo serviceUserInfo = listRecommendServiceUser.get(position);
                DemandDetailBean.Demand demand = demandDetailBean.data.demand;

                String targetId = serviceUserInfo.uid + "";
                ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
                chatCmdShareTaskBean.uid = LoginManager.currentLoginUserId;
                chatCmdShareTaskBean.avatar = LoginManager.currentLoginUserAvatar;
                chatCmdShareTaskBean.title = demand.title;
                if (demand.quote <= 0) {
                    chatCmdShareTaskBean.quote = "服务方报价";
                } else {
                    chatCmdShareTaskBean.quote = demand.quote + "元";
                }
                chatCmdShareTaskBean.type = 1;
                chatCmdShareTaskBean.tid = demandId;

                Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
                intentChatActivity.putExtra("targetId", targetId);
                intentChatActivity.putExtra("chatCmdName", "sendShareTask");
                intentChatActivity.putExtra("chatCmdShareTaskBean", chatCmdShareTaskBean);
                mActivity.startActivity(intentChatActivity);

//                String recommendDemandText = "我发布了需求《" + demand.title + "》，快来抢单吧";
//                ShareTaskUtils.sendText(recommendDemandText, targetId);

            } else if (listCheckedItemId.size() >= 2) {
                int size = listCheckedItemId.size();
                if (size == 2) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_INVITE_TWO);
                } else if (size == 3) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_INVITE_THREE);
                } else if (size == 4) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_INVITE_FOUR);
                } else if (size == 5) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_SUCCESS_INVITE_FIVE);
                }

                //选中了多个，分享的时候不弹出聊天框
                for (int position : listCheckedItemId) {
                    RecommendServiceUserBean.ServiceUserInfo serviceUserInfo = listRecommendServiceUser.get(position);
                    DemandDetailBean.Demand demand = demandDetailBean.data.demand;

                    String targetId = serviceUserInfo.uid + "";
                    ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
                    chatCmdShareTaskBean.uid = LoginManager.currentLoginUserId;
                    chatCmdShareTaskBean.avatar = LoginManager.currentLoginUserAvatar;
                    chatCmdShareTaskBean.title = demand.title;
                    if (demand.quote <= 0) {
                        chatCmdShareTaskBean.quote = "服务方报价";
                    } else {
                        chatCmdShareTaskBean.quote = demand.quote + "元";
                    }
                    chatCmdShareTaskBean.type = 1;
                    chatCmdShareTaskBean.tid = demandId;

                    ShareTaskUtils.sendShareTask(chatCmdShareTaskBean, targetId);
                }
                ToastUtils.shortToast("邀请已经发送成功");
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DemandDetailBean.Demand demand = demandDetailBean.data.demand;
//                        String recommendDemandText = LoginManager.currentLoginUserName + "发布了需求《" + demand.title + "》，快来抢单吧";
                        String recommendDemandText = "我发布了需求《" + demand.title + "》，快来抢单吧";
                        for (int position : listCheckedItemId) {
                            RecommendServiceUserBean.ServiceUserInfo serviceUserInfo = listRecommendServiceUser.get(position);
                            String targetId = serviceUserInfo.uid + "";
                            ShareTaskUtils.sendText(recommendDemandText, targetId);
                        }
//                        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
//                        HomeActivity.goBackPageNo = HomeActivity.PAGE_INFO;
//                        mActivity.startActivity(intentHomeActivity);
                        Intent intentMessageActivity = new Intent(CommonUtils.getContext(), MessageActivity.class);
                        mActivity.startActivity(intentMessageActivity);
                    }
                }, 1500);
            }
        }
    }

    private int publishSuccessHintVisibility;
    private int updateSuccessHintVisibility;
    private int publishSuccessVisibility;
    private int updateSuccessVisibility = View.GONE;
    private String afterUpdateValidDate;

    private int recommendServiceInfoVisibility;

    @Bindable
    public int getRecommendServiceInfoVisibility() {
        return recommendServiceInfoVisibility;
    }

    public void setRecommendServiceInfoVisibility(int recommendServiceInfoVisibility) {
        this.recommendServiceInfoVisibility = recommendServiceInfoVisibility;
        notifyPropertyChanged(BR.recommendServiceInfoVisibility);
    }

    @Bindable
    public String getAfterUpdateValidDate() {
        return afterUpdateValidDate;
    }

    public void setAfterUpdateValidDate(String afterUpdateValidDate) {
        this.afterUpdateValidDate = afterUpdateValidDate;
        notifyPropertyChanged(BR.afterUpdateValidDate);
    }

    @Bindable
    public int getUpdateSuccessVisibility() {
        return updateSuccessVisibility;
    }

    public void setUpdateSuccessVisibility(int updateSuccessVisibility) {
        this.updateSuccessVisibility = updateSuccessVisibility;
        notifyPropertyChanged(BR.updateSuccessVisibility);
    }

    @Bindable
    public int getPublishSuccessVisibility() {
        return publishSuccessVisibility;
    }

    public void setPublishSuccessVisibility(int publishSuccessVisibility) {
        this.publishSuccessVisibility = publishSuccessVisibility;
        notifyPropertyChanged(BR.publishSuccessVisibility);
    }

    @Bindable
    public int getUpdateSuccessHintVisibility() {
        return updateSuccessHintVisibility;
    }

    public void setUpdateSuccessHintVisibility(int updateSuccessHintVisibility) {
        this.updateSuccessHintVisibility = updateSuccessHintVisibility;
        notifyPropertyChanged(BR.updateSuccessHintVisibility);
    }

    @Bindable
    public int getPublishSuccessHintVisibility() {
        return publishSuccessHintVisibility;
    }

    public void setPublishSuccessHintVisibility(int publishSuccessHintVisibility) {
        this.publishSuccessHintVisibility = publishSuccessHintVisibility;
        notifyPropertyChanged(BR.publishSuccessHintVisibility);
    }
}
