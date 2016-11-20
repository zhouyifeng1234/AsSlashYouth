package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.net.Uri;

import com.slash.youth.databinding.ItemChatFriendPicBinding;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatFriendPicModel extends BaseObservable {

    ItemChatFriendPicBinding mItemChatFriendPicBinding;
    Activity mActivity;
    Uri mThumUri;

    public ChatFriendPicModel(ItemChatFriendPicBinding itemChatFriendPicBinding, Activity activity, Uri thumUri) {
        this.mItemChatFriendPicBinding = itemChatFriendPicBinding;
        this.mActivity = activity;
        this.mThumUri = thumUri;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
//        mItemChatFriendPicBinding.ivChatFriendPic.setImageURI(mThumUri);
        LogKit.v(mThumUri.toString());
        x.image().bind(mItemChatFriendPicBinding.ivChatFriendPic, mThumUri.toString());
    }

}
