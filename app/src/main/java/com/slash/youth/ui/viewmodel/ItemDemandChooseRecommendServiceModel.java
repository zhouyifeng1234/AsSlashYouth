package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemDemandChooseRecommendServiceBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.RecommendServiceUserBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class ItemDemandChooseRecommendServiceModel extends BaseObservable {

    ItemDemandChooseRecommendServiceBinding mItemDemandChooseRecommendServiceBinding;
    Activity mActivity;
    RecommendServiceUserBean.ServiceUserInfo serviceUserInfo;
    long serviceUserId;
    ChatCmdShareTaskBean chatCmdShareTaskBean;

    public ItemDemandChooseRecommendServiceModel(ItemDemandChooseRecommendServiceBinding itemDemandChooseRecommendServiceBinding, Activity activity, RecommendServiceUserBean.ServiceUserInfo serviceUserInfo, ChatCmdShareTaskBean chatCmdShareTaskBean) {
        this.mItemDemandChooseRecommendServiceBinding = itemDemandChooseRecommendServiceBinding;
        this.mActivity = activity;
        this.serviceUserInfo = serviceUserInfo;
        this.chatCmdShareTaskBean = chatCmdShareTaskBean;

        initData();
        initView();
    }

    private void initData() {
        serviceUserId = serviceUserInfo.uid;
    }

    private void initView() {
        String avatarStr;
        if (serviceUserInfo.avatar.startsWith("http")) {
            avatarStr = serviceUserInfo.avatar;
        } else {
            avatarStr = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + serviceUserInfo.avatar;
        }
        BitmapKit.bindImage(mItemDemandChooseRecommendServiceBinding.ivServiceUserAvatar, avatarStr);

        if (serviceUserInfo.isauth == 0) {
            //未认证
            setAuthVisibility(View.GONE);
        } else {
            //已认证
            setAuthVisibility(View.VISIBLE);
        }

        setName(serviceUserInfo.name);
        setCompanyAndPosition(serviceUserInfo.company + serviceUserInfo.position);
        if (TextUtils.isEmpty(serviceUserInfo.industry) || TextUtils.isEmpty(serviceUserInfo.direction)) {
            setIndustryAndDirection(serviceUserInfo.industry + serviceUserInfo.direction);
        } else {
            setIndustryAndDirection(serviceUserInfo.industry + "|" + serviceUserInfo.direction);
        }

    }

    /**
     * 邀请 推荐的优秀服务者
     *
     * @param v
     */
    public void invitation(View v) {
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", serviceUserId + "");
        intentChatActivity.putExtra("chatCmdName", "sendShareTask");
        intentChatActivity.putExtra("chatCmdShareTaskBean", chatCmdShareTaskBean);
        mActivity.startActivity(intentChatActivity);
    }

    /**
     * 跳转到用户个人信息页面
     *
     * @param v
     */
    public void gotoUserInfoPager(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", serviceUserInfo.uid);
        mActivity.startActivity(intentUserInfoActivity);
    }

    private int authVisibility = View.GONE;
    private String name;
    private String companyAndPosition;
    private String industryAndDirection;//IT互联网｜市场商务

    @Bindable
    public String getIndustryAndDirection() {
        return industryAndDirection;
    }

    public void setIndustryAndDirection(String industryAndDirection) {
        this.industryAndDirection = industryAndDirection;
        notifyPropertyChanged(BR.industryAndDirection);
    }

    @Bindable
    public String getCompanyAndPosition() {
        return companyAndPosition;
    }

    public void setCompanyAndPosition(String companyAndPosition) {
        this.companyAndPosition = companyAndPosition;
        notifyPropertyChanged(BR.companyAndPosition);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getAuthVisibility() {
        return authVisibility;
    }

    public void setAuthVisibility(int authVisibility) {
        this.authVisibility = authVisibility;
        notifyPropertyChanged(BR.authVisibility);
    }
}
