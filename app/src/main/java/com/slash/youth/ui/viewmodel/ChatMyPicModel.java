package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.net.Uri;

import com.slash.youth.databinding.ItemChatMyPicBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatMyPicModel extends BaseObservable {

    ItemChatMyPicBinding mItemChatMyPicBinding;
    Activity mActivity;
    Uri mThumUri;
    boolean mIsRead;

    public ChatMyPicModel(ItemChatMyPicBinding itemChatMyPicBinding, Activity activity, Uri thumUri, boolean isRead) {
        this.mItemChatMyPicBinding = itemChatMyPicBinding;
        this.mActivity = activity;
        this.mThumUri = thumUri;
        this.mIsRead = isRead;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        LogKit.v(mThumUri.toString());
        x.image().bind(mItemChatMyPicBinding.ivChatMyPic, mThumUri.toString());
        //加载图像
        BitmapKit.bindImage(mItemChatMyPicBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);

        if (mIsRead) {
            mItemChatMyPicBinding.tvChatMsgReadStatus.setText("已读");
        } else {
            mItemChatMyPicBinding.tvChatMsgReadStatus.setText("未读");
        }
    }
}
