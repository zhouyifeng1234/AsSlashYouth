package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityPublishServiceSuccessBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.RecommendDemandUserBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.MessageActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.adapter.RecommendDemandAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.ShareTaskUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class PublishServiceSuccessModel extends BaseObservable {

    ActivityPublishServiceSuccessBinding mActivityPublishServiceSuccessBinding;
    Activity mActivity;
    long serviceId;
    boolean isUpdate;
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

    public PublishServiceSuccessModel(ActivityPublishServiceSuccessBinding activityPublishServiceSuccessBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceSuccessBinding = activityPublishServiceSuccessBinding;
        initData();
        initView();
    }

    private void initData() {
        serviceId = mActivity.getIntent().getLongExtra("serviceId", -1);
        isUpdate = mActivity.getIntent().getBooleanExtra("isUpdate", false);
        getDataFromServer();
    }

    ServiceDetailBean serviceDetailBean = null;
    ArrayList<RecommendDemandUserBean.DemandUserInfo> listRecommendDemandUser;

    private void getDataFromServer() {
        UserInfoEngine.getUserAuthStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                if (dataBean.data.status == 2) {
                    //已认证
                    setAuthLayerVisibility(View.VISIBLE);
                    setNoAuthLayerVisibility(View.GONE);
                    ServiceEngine.getServiceDetail(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
                        @Override
                        public void execute(ServiceDetailBean dataBean) {
                            serviceDetailBean = dataBean;
                        }

                        @Override
                        public void executeResultError(String result) {

                        }
                    }, serviceId + "");

                    ServiceEngine.getRecommendDemandUser(new BaseProtocol.IResultExecutor<RecommendDemandUserBean>() {
                        @Override
                        public void execute(RecommendDemandUserBean dataBean) {
                            listRecommendDemandUser = dataBean.data.list;
                            if (listRecommendDemandUser != null && listRecommendDemandUser.size() > 0) {
                                setRecommendDemandInfoVisibility(View.VISIBLE);
                                mActivityPublishServiceSuccessBinding.lvRecommendDemand.setAdapter(new RecommendDemandAdapter(dataBean.data.list));
                            } else {
                                setRecommendDemandInfoVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void executeResultError(String result) {
                            ToastUtils.shortToast("获取推荐的需求者失败");
                        }
                    }, serviceId + "", "5");
                } else {
                    //未认证
                    setAuthLayerVisibility(View.GONE);
                    setNoAuthLayerVisibility(View.VISIBLE);
                }
            }

            @Override
            public void executeResultError(String result) {
                //这里不会执行
            }
        });

//        ServiceEngine.getRecommendDemandUser();
    }

    private void initView() {
        if (isUpdate) {
            setUpdateSuccessHintVisibility(View.VISIBLE);
            setPublishSuccessHintVisibility(View.GONE);
            mActivityPublishServiceSuccessBinding.lvRecommendDemand.setVisibility(View.GONE);
            mActivityPublishServiceSuccessBinding.tvPublishServiceText.setText("修改服务");
            mActivityPublishServiceSuccessBinding.tvPublishSuccessText.setText("修改成功");

            setUpdateSuccessVisibility(View.VISIBLE);
            setPublishSuccessVisibility(View.GONE);
        } else {
            setUpdateSuccessHintVisibility(View.GONE);
            setPublishSuccessHintVisibility(View.VISIBLE);
            mActivityPublishServiceSuccessBinding.lvRecommendDemand.setVisibility(View.VISIBLE);

            setUpdateSuccessVisibility(View.GONE);
            setPublishSuccessVisibility(View.VISIBLE);
        }
        mActivityPublishServiceSuccessBinding.lvRecommendDemand.setVerticalScrollBarEnabled(false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("展示有效期至yyyy年MM月dd日HH:mm");
        String displayValidityDatetime = simpleDateFormat.format(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        mActivityPublishServiceSuccessBinding.tvDisplayValidityDatetime.setText(displayValidityDatetime);
        setAfterUpdateValidDate(displayValidityDatetime);
    }

    public void closeSuccessActivity(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_CLOSE);

        mActivity.finish();
    }

    //跳转到服务详情页
    public void gotoServiceDetail(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_CLICK_DETAIL);

        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
        intentServiceDetailActivity.putExtra("serviceId", serviceId);
        mActivity.startActivity(intentServiceDetailActivity);
    }

    /**
     * 如果发服务者未认证，点击去认证
     *
     * @param v
     */
    public void gotoAuth(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_CLICK_APPROVE);

        Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
        intentApprovalActivity.putExtra("Uid", LoginManager.currentLoginUserId);
        mActivity.startActivity(intentApprovalActivity);
    }

    /**
     * 服务发布成功页面，点击“马上联系他们”，将服务分享给推荐的需求者
     *
     * @param v
     */
    public void shareTask(View v) {
        if (serviceDetailBean != null && listRecommendDemandUser != null) {
            final ArrayList<Integer> listCheckedItemId = RecommendDemandAdapter.listCheckedItemId;
            if (listCheckedItemId.size() == 1) {
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_INVITE_ONE);

                //只选中了一个，分享的时候会弹出聊天框
                int position = listCheckedItemId.get(0);
                RecommendDemandUserBean.DemandUserInfo demandUserInfo = listRecommendDemandUser.get(position);
                ServiceDetailBean.Service service = serviceDetailBean.data.service;

                String targetId = demandUserInfo.uid + "";
                ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
                chatCmdShareTaskBean.uid = LoginManager.currentLoginUserId;
                chatCmdShareTaskBean.avatar = LoginManager.currentLoginUserAvatar;
                chatCmdShareTaskBean.title = service.title;
                String quoteStr = "";
                if (service.quoteunit > 0 && service.quoteunit < 9) {
                    quoteStr = service.quote + "元/" + optionalPriceUnit[service.quoteunit - 1];
                } else {
                    quoteStr = service.quote + "";
                }
                chatCmdShareTaskBean.quote = quoteStr;


                chatCmdShareTaskBean.type = 2;
                chatCmdShareTaskBean.tid = serviceId;

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
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_INVITE_TWO);
                } else if (size == 3) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_INVITE_THREE);
                } else if (size == 4) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_INVITE_FOUR);
                } else if (size == 5) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_SUCCESS_INVITE_FIVE);
                }

                //选中了多个，分享的时候不弹出聊天框
                for (int position : listCheckedItemId) {
                    RecommendDemandUserBean.DemandUserInfo demandUserInfo = listRecommendDemandUser.get(position);
                    ServiceDetailBean.Service service = serviceDetailBean.data.service;

                    String targetId = demandUserInfo.uid + "";
                    ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
                    chatCmdShareTaskBean.uid = LoginManager.currentLoginUserId;
                    chatCmdShareTaskBean.avatar = LoginManager.currentLoginUserAvatar;
                    chatCmdShareTaskBean.title = service.title;
                    String quoteStr = "";
                    if (service.quoteunit > 0 && service.quoteunit < 9) {
                        quoteStr = service.quote + "元/" + optionalPriceUnit[service.quoteunit - 1];
                    } else {
                        quoteStr = service.quote + "";
                    }
                    chatCmdShareTaskBean.quote = quoteStr;

                    chatCmdShareTaskBean.type = 2;
                    chatCmdShareTaskBean.tid = serviceId;

                    ShareTaskUtils.sendShareTask(chatCmdShareTaskBean, targetId);
                }
                ToastUtils.shortToast("邀请已经发送成功");
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ServiceDetailBean.Service service = serviceDetailBean.data.service;
//                        String recommendDemandText = LoginManager.currentLoginUserName + "发布了需求《" + demand.title + "》，快来抢单吧";
                        String recommendDemandText = "我发布了服务《" + service.title + "》，快来抢单吧";
                        for (int position : listCheckedItemId) {
                            RecommendDemandUserBean.DemandUserInfo demandUserInfo = listRecommendDemandUser.get(position);
                            String targetId = demandUserInfo.uid + "";
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
    private int authLayerVisibility;
    private int noAuthLayerVisibility;

    private int updateSuccessVisibility = View.GONE;
    private int publishSuccessVisibility;
    private String afterUpdateValidDate;

    private int recommendDemandInfoVisibility;

    @Bindable
    public int getRecommendDemandInfoVisibility() {
        return recommendDemandInfoVisibility;
    }

    public void setRecommendDemandInfoVisibility(int recommendDemandInfoVisibility) {
        this.recommendDemandInfoVisibility = recommendDemandInfoVisibility;
        notifyPropertyChanged(BR.recommendDemandInfoVisibility);
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
    public int getNoAuthLayerVisibility() {
        return noAuthLayerVisibility;
    }

    public void setNoAuthLayerVisibility(int noAuthLayerVisibility) {
        this.noAuthLayerVisibility = noAuthLayerVisibility;
        notifyPropertyChanged(BR.noAuthLayerVisibility);
    }

    @Bindable
    public int getAuthLayerVisibility() {
        return authLayerVisibility;
    }

    public void setAuthLayerVisibility(int authLayerVisibility) {
        this.authLayerVisibility = authLayerVisibility;
        notifyPropertyChanged(BR.authLayerVisibility);
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
