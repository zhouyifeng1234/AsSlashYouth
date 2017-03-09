package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.SpannableString;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatFriendTextBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.MyTaskActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.TextUtil;

import java.util.Arrays;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatFriendTextModel extends BaseObservable {
    ItemChatFriendTextBinding mItemChatFriendTextBinding;
    Activity mActivity;
    String mTargetAvatar;

    public ChatFriendTextModel(ItemChatFriendTextBinding itemChatFriendTextBinding, Activity activity, String targetAvatar) {
        this.mItemChatFriendTextBinding = itemChatFriendTextBinding;
        this.mActivity = activity;
        this.mTargetAvatar = targetAvatar;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if ((!"1000".equals(MsgManager.targetId)) && (!MsgManager.customerServiceUid.equals(MsgManager.targetId))) {
            BitmapKit.bindImage(mItemChatFriendTextBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatFriendTextBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
        }
    }

    public void gotoTask(View v) {
        //判断 如果是1000号（消息助手），且内容中带有tid，就跳转到我的任务列表页面
        if ("1000".equals(MsgManager.targetId)) {
            if (extraInfo.contains("\"tid\"")) {
                Intent intentMyTaskActivity = new Intent(CommonUtils.getContext(), MyTaskActivity.class);
                mActivity.startActivity(intentMyTaskActivity);
            }
        }
    }

    private SpannableString textContent;
    private String extraInfo;

    @Bindable
    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
        notifyPropertyChanged(BR.extraInfo);
    }

    @Bindable
    public SpannableString getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = TextUtil.matcherSearchTitle(CommonUtils.getContext().getResources().getColor(R.color.app_text_bg_blue),textContent, Arrays.asList(CommonUtils.getContext().getResources().getStringArray(R.array.keyword)));
        notifyPropertyChanged(BR.textContent);
    }


}
