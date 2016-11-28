package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.net.Uri;

import com.slash.youth.databinding.ItemChatMyPicBinding;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatMyPicModel extends BaseObservable {

    ItemChatMyPicBinding mItemChatMyPicBinding;
    Activity mActivity;
    Uri mThumUri;

    public ChatMyPicModel(ItemChatMyPicBinding itemChatMyPicBinding, Activity activity, Uri thumUri) {
        this.mItemChatMyPicBinding = itemChatMyPicBinding;
        this.mActivity = activity;
        this.mThumUri = thumUri;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        LogKit.v(mThumUri.toString());
        x.image().bind(mItemChatMyPicBinding.ivChatMyPic, mThumUri.toString());
    }
}
