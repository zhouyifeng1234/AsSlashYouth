package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatOtherChangeContactWayBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

import java.io.File;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherChangeContactWayModel extends BaseObservable {

    ItemChatOtherChangeContactWayBinding mItemChatOtherChangeContactWayBinding;
    Activity mActivity;
    ChatModel mChatModel;
    String mOtherPhone;
    String mTargetAvatar;
    boolean mIsChangeContact;
    private String targetId;

    public ChatOtherChangeContactWayModel(ItemChatOtherChangeContactWayBinding itemChatOtherChangeContactWayBinding, Activity activity, ChatModel chatModel, String otherPhone, String targetAvatar, boolean isChangeContact, String targetId) {
        this.mItemChatOtherChangeContactWayBinding = itemChatOtherChangeContactWayBinding;
        this.mActivity = activity;
        this.mChatModel = chatModel;
        this.mOtherPhone = otherPhone;
        this.mTargetAvatar = targetAvatar;
        this.mIsChangeContact = isChangeContact;
        this.targetId = targetId;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if (mIsChangeContact) {
            mItemChatOtherChangeContactWayBinding.tvDeny.setBackgroundResource(R.drawable.shape_chat_deny_change_contact_way_bg);
            mItemChatOtherChangeContactWayBinding.tvAgree.setBackgroundResource(R.drawable.shape_chat_agree_change_contact_way_bg);
            mItemChatOtherChangeContactWayBinding.tvDeny.setEnabled(false);
            mItemChatOtherChangeContactWayBinding.tvAgree.setEnabled(false);
        } else {
            File dataDir = CommonUtils.getContext().getFilesDir();
            File denyChangeContactInfoFile = new File(dataDir,
                    "denyChangeContactInfoDir/" + LoginManager.currentLoginUserId + "to" + targetId);
            if (denyChangeContactInfoFile.exists()) {
                mItemChatOtherChangeContactWayBinding.tvDeny.setBackgroundResource(R.drawable.shape_chat_deny_change_contact_way_bg);
                mItemChatOtherChangeContactWayBinding.tvAgree.setBackgroundResource(R.drawable.shape_chat_agree_change_contact_way_bg);
                mItemChatOtherChangeContactWayBinding.tvDeny.setEnabled(false);
                mItemChatOtherChangeContactWayBinding.tvAgree.setEnabled(false);
            } else {
                mItemChatOtherChangeContactWayBinding.tvDeny.setBackgroundResource(R.drawable.shape_chat_deny_add_friend_bg);
                mItemChatOtherChangeContactWayBinding.tvAgree.setBackgroundResource(R.drawable.shape_chat_agree_add_friend_bg);
            }
        }

        if ((!"1000".equals(MsgManager.targetId)) && (!MsgManager.customerServiceUid.equals(MsgManager.targetId))) {
            BitmapKit.bindImage(mItemChatOtherChangeContactWayBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatOtherChangeContactWayBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
        }
    }

    //同意交换联系方式
    public void agreeChangeContact(View v) {
        mChatModel.agreeChangeContact(mOtherPhone, mItemChatOtherChangeContactWayBinding.tvDeny, mItemChatOtherChangeContactWayBinding.tvAgree);
    }

    //拒绝交换联系方式
    public void refuseChangeContact(View v) {
        mChatModel.refuseChangeContact(mItemChatOtherChangeContactWayBinding.tvDeny, mItemChatOtherChangeContactWayBinding.tvAgree);
    }
}
