package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatChangeContactWayInfoBinding;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatChangeContactWayInfoModel extends BaseObservable {

    ItemChatChangeContactWayInfoBinding mItemChatChangeContactWayInfoBinding;
    Activity mActivity;
    String otherPhone;

    public ChatChangeContactWayInfoModel(ItemChatChangeContactWayInfoBinding itemChatChangeContactWayInfoBinding, Activity activity, String name, String otherPhone) {
        this.mItemChatChangeContactWayInfoBinding = itemChatChangeContactWayInfoBinding;
        this.mActivity = activity;
        this.otherPhone = otherPhone;
        //setOtherContactInfo(name + "的手机号：" + otherPhone);
        setOtherContactInfo(otherPhone);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    /**
     * 拨打对方电话
     *
     * @param v
     */
    public void callToOther(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_CHAT_CLICK_CALL);
        Uri uri = Uri.parse("tel:" + otherPhone);
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, uri);
        mActivity.startActivity(phoneIntent);
    }

    private String otherContactInfo;

    @Bindable
    public String getOtherContactInfo() {
        return otherContactInfo;
    }

    public void setOtherContactInfo(String otherContactInfo) {
        this.otherContactInfo = otherContactInfo;
        notifyPropertyChanged(BR.otherContactInfo);
    }
}
