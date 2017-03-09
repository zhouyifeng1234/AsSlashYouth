package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatMyPicBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatMyPicModel extends BaseObservable {

    ItemChatMyPicBinding mItemChatMyPicBinding;
    Activity mActivity;
    Uri mThumUri;
    boolean mIsRead;
    ImageMessage mImageMessage;
    String mTargetId;
    boolean isFail;

    public ChatMyPicModel(ItemChatMyPicBinding itemChatMyPicBinding, Activity activity, Uri thumUri, boolean isRead, ImageMessage imageMessage, String targetId, boolean isFail) {
        this.mItemChatMyPicBinding = itemChatMyPicBinding;
        this.mActivity = activity;
        this.mThumUri = thumUri;
        this.mIsRead = isRead;
        this.mImageMessage = imageMessage;
        this.mTargetId = targetId;
        this.isFail = isFail;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        LogKit.v(mThumUri.toString());
        x.image().bind(mItemChatMyPicBinding.ivChatMyPic, mThumUri.toString());
        //加载头像
        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMyPicBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMyPicBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMyPicBinding.tvChatMsgReadStatus.setText("已读");
            mItemChatMyPicBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_readed_marker_bg);
        } else {
//            mItemChatMyPicBinding.tvChatMsgReadStatus.setText("未读");
            if (isFail) {
                mItemChatMyPicBinding.ivChatSendMsgAgain.setVisibility(View.VISIBLE);
            } else {
                mItemChatMyPicBinding.tvChatMsgReadStatus.setText("送达");
                mItemChatMyPicBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_unreaded_marker_bg);
            }
        }
    }

    //重新发送消息
    public void sendMsgAgain(View v) {
        if (mImageMessage != null && !TextUtils.isEmpty(mTargetId)) {
            String pushContent = LoginManager.currentLoginUserName + ":[图片],请查看";
            RongIMClient.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, mTargetId, mImageMessage, pushContent, null, new RongIMClient.SendImageMessageCallback() {

                @Override
                public void onAttached(Message message) {
                    //保存数据库成功
                    LogKit.v("保存数据库成功");
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode code) {
                    //发送失败
                    LogKit.v("发送失败");

                }

                @Override
                public void onSuccess(Message message) {
                    //发送成功
                    LogKit.v("发送成功");
                    ImageMessage imageMessage1 = (ImageMessage) message.getContent();
                    LogKit.v(imageMessage1.getRemoteUri().toString());

                    mItemChatMyPicBinding.ivChatSendMsgAgain.setVisibility(View.GONE);
                    //重新发送以后，需要在容易的消息记录里把原来这表messageId的消息删除掉
                    int messageId = (int) mItemChatMyPicBinding.ivChatSendMsgAgain.getTag();
                    int[] ids = new int[]{messageId};
                    RongIMClient.getInstance().deleteMessages(ids, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //删除消息成功
                            //UI上面，要把这个View,提取到最下面
                            View messageView = mItemChatMyPicBinding.getRoot();
                            ViewGroup parent = (ViewGroup) messageView.getParent();
                            parent.removeView(messageView);
                            parent.addView(messageView);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }

                @Override
                public void onProgress(Message message, int progress) {
                    //发送进度
                    LogKit.v("发送进度:" + progress);
                }
            });
        }
    }
}
