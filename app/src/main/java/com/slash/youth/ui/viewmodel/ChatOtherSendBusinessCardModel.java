package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.databinding.ItemChatOtherSendBusinessCardBinding;
import com.slash.youth.domain.ChatCmdBusinesssCardBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendBusinessCardModel extends BaseObservable {

    ItemChatOtherSendBusinessCardBinding mItemChatOtherSendBusinessCardBinding;
    Activity mActivity;
    String mTargetAvatar;
    ChatCmdBusinesssCardBean mChatCmdBusinesssCardBean;

    public ChatOtherSendBusinessCardModel(ItemChatOtherSendBusinessCardBinding itemChatOtherSendBusinessCardBinding, Activity activity, String targetAvatar, ChatCmdBusinesssCardBean chatCmdBusinesssCardBean) {
        this.mItemChatOtherSendBusinessCardBinding = itemChatOtherSendBusinessCardBinding;
        this.mActivity = activity;
        this.mTargetAvatar = targetAvatar;
        this.mChatCmdBusinesssCardBean = chatCmdBusinesssCardBean;

        initData();
        initView();
    }

    private void initData() {
        //显示名片内容
        BitmapKit.bindImage(mItemChatOtherSendBusinessCardBinding.ivBusinessCardAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mChatCmdBusinesssCardBean.avatar);
        mItemChatOtherSendBusinessCardBinding.tvBusinessCardName.setText(mChatCmdBusinesssCardBean.name);
        String industryAndProfession = "";
        if (!TextUtils.isEmpty(mChatCmdBusinesssCardBean.industry) && !TextUtils.isEmpty(mChatCmdBusinesssCardBean.profession)) {
            industryAndProfession = mChatCmdBusinesssCardBean.industry + "|" + mChatCmdBusinesssCardBean.profession;
        } else {
            industryAndProfession = mChatCmdBusinesssCardBean.industry + mChatCmdBusinesssCardBean.profession;
        }
        mItemChatOtherSendBusinessCardBinding.tvBusinessIndustryProfession.setText(industryAndProfession);
    }

    private void initView() {
        if ((!"1000".equals(MsgManager.targetId)) && (!MsgManager.customerServiceUid.equals(MsgManager.targetId))) {
            BitmapKit.bindImage(mItemChatOtherSendBusinessCardBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatOtherSendBusinessCardBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
        }
    }

    public void gotoUserInfoPage(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", Long.parseLong(mChatCmdBusinesssCardBean.uid));
        mActivity.startActivity(intentUserInfoActivity);
    }

}
