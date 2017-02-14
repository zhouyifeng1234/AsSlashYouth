package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatMySendBusinessCardBinding;
import com.slash.youth.domain.ChatCmdBusinesssCardBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMySendBusinessCardModel extends BaseObservable {
    ItemChatMySendBusinessCardBinding mItemChatMySendBusinessCardBinding;
    Activity mActivity;
    boolean mIsRead;
    ChatCmdBusinesssCardBean mChatCmdBusinesssCardBean;


    public ChatMySendBusinessCardModel(ItemChatMySendBusinessCardBinding itemChatMySendBusinessCardBinding, Activity activity, boolean isRead, ChatCmdBusinesssCardBean chatCmdBusinesssCardBean) {
        this.mItemChatMySendBusinessCardBinding = itemChatMySendBusinessCardBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;
        this.mChatCmdBusinesssCardBean = chatCmdBusinesssCardBean;

        initData();
        initView();
    }

    private void initData() {
        //显示名片内容
        BitmapKit.bindImage(mItemChatMySendBusinessCardBinding.ivBusinessCardAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mChatCmdBusinesssCardBean.avatar);
        mItemChatMySendBusinessCardBinding.tvBusinessCardName.setText(mChatCmdBusinesssCardBean.name);
        String industryAndProfession = "";
        if (!TextUtils.isEmpty(mChatCmdBusinesssCardBean.industry) && !TextUtils.isEmpty(mChatCmdBusinesssCardBean.profession)) {
            industryAndProfession = mChatCmdBusinesssCardBean.industry + "|" + mChatCmdBusinesssCardBean.profession;
        } else {
            industryAndProfession = mChatCmdBusinesssCardBean.industry + mChatCmdBusinesssCardBean.profession;
        }
        mItemChatMySendBusinessCardBinding.tvBusinessIndustryProfession.setText(industryAndProfession);
    }

    private void initView() {
        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMySendBusinessCardBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMySendBusinessCardBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("已读");
        } else {
            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("未读");
        }
    }

    public void gotoUserInfoPage(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", mChatCmdBusinesssCardBean.uid);
        mActivity.startActivity(intentUserInfoActivity);
    }

}
