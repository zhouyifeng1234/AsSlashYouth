package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatOtherSendAddFriendBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendAddFriendModel extends BaseObservable {

    ItemChatOtherSendAddFriendBinding mItemChatOtherSendAddFriendBinding;
    Activity mActivity;
    ChatModel mChatModel;
    String mTargetAvatar;
    boolean mIsCanAddFriend;

    public ChatOtherSendAddFriendModel(ItemChatOtherSendAddFriendBinding itemChatOtherSendAddFriendBinding, Activity activity, ChatModel chatModel, String targetAvatar, boolean isCanAddFriend) {
        this.mItemChatOtherSendAddFriendBinding = itemChatOtherSendAddFriendBinding;
        this.mActivity = activity;
        this.mChatModel = chatModel;
        this.mTargetAvatar = targetAvatar;
        this.mIsCanAddFriend = isCanAddFriend;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if (mIsCanAddFriend) {
            mItemChatOtherSendAddFriendBinding.tvDeny.setBackgroundResource(R.drawable.shape_chat_deny_add_friend_bg);
            mItemChatOtherSendAddFriendBinding.tvAgree.setBackgroundResource(R.drawable.shape_chat_agree_add_friend_bg);
        } else {
            mItemChatOtherSendAddFriendBinding.tvDeny.setBackgroundResource(R.drawable.shape_chat_deny_change_contact_way_bg);
            mItemChatOtherSendAddFriendBinding.tvAgree.setBackgroundResource(R.drawable.shape_chat_agree_change_contact_way_bg);
        }
        if ((!"1000".equals(MsgManager.targetId)) && (!MsgManager.customerServiceUid.equals(MsgManager.targetId))) {
            BitmapKit.bindImage(mItemChatOtherSendAddFriendBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatOtherSendAddFriendBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
        }
    }

    //同意添加对方为好友
    public void agreeAddFriend(View v) {
        if (mIsCanAddFriend) {
            mChatModel.agreeAddFriend(mItemChatOtherSendAddFriendBinding.tvDeny, mItemChatOtherSendAddFriendBinding.tvAgree);
        }
    }

    //拒绝添加对方为好友
    public void refuseAddFriend(View v) {
        if (mIsCanAddFriend) {
            mChatModel.refuseAddFriend(mItemChatOtherSendAddFriendBinding.tvDeny, mItemChatOtherSendAddFriendBinding.tvAgree);
        }
    }
}
