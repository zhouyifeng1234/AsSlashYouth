package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemPushInfoBinding;
import com.slash.youth.domain.PushInfoBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/22.
 */
public class ItemPushInfoModel extends BaseObservable {
    ItemPushInfoBinding mItemPushInfoBinding;
    Activity mActivity;
    PushInfoBean mPushInfoBean;
    String username;
    String avatar;

    public ItemPushInfoModel(ItemPushInfoBinding itemPushInfoBinding, Activity activity, PushInfoBean pushInfoBean) {
        this.mItemPushInfoBinding = itemPushInfoBinding;
        this.mActivity = activity;
        mPushInfoBean = pushInfoBean;

        initData();
        initView();
    }

    private void initData() {
        if (mPushInfoBean.senderUserId.equals("1000")) {
            username = "斜杠消息助手";
            setPushUserInfo();
        } else if (mPushInfoBean.senderUserId.equals(MsgManager.customerServiceUid)) {
            username = "斜杠客服助手";
            setPushUserInfo();
        } else {
            UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
                @Override
                public void execute(UserInfoBean dataBean) {
                    UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                    username = uinfo.name;
                    avatar = uinfo.avatar;
                    setPushUserInfo();
                }

                @Override
                public void executeResultError(String result) {

                }
            }, mPushInfoBean.senderUserId, "0");
        }
    }

    private void initView() {

    }

    private void setPushUserInfo() {
        if (mPushInfoBean.senderUserId.equals("1000")) {
            mItemPushInfoBinding.ivPushAvatar.setImageResource(R.mipmap.message_icon);
        } else if (mPushInfoBean.senderUserId.equals(MsgManager.customerServiceUid)) {
            mItemPushInfoBinding.ivPushAvatar.setImageResource(R.mipmap.customer_service_icon);
        } else {
            BitmapKit.bindImage(mItemPushInfoBinding.ivPushAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        setPushName(username);
        if (mPushInfoBean.msg_type == PushInfoBean.CHAT_TEXT_MSG) {
            setPushText(mPushInfoBean.pushText);
        } else {
            setPushText(username + mPushInfoBean.pushText);
        }
    }

    private String pushText;
    private String pushName;

    @Bindable
    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
        notifyPropertyChanged(BR.pushName);
    }

    @Bindable
    public String getPushText() {
        return pushText;
    }

    public void setPushText(String pushText) {
        this.pushText = pushText;
        notifyPropertyChanged(BR.pushText);
    }
}
