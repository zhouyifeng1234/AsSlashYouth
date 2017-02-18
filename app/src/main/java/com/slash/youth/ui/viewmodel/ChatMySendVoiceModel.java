package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatMySendVoiceBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.io.IOException;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.VoiceMessage;

/**
 * Created by zhouyifeng on 2016/11/18.
 */
public class ChatMySendVoiceModel extends BaseObservable {
    ItemChatMySendVoiceBinding mItemChatMySendVoiceBinding;
    Activity mActivity;
    Uri mVoiceUri;
    int mDuration;
    boolean mIsRead;
    VoiceMessage mVoiceMessage;
    String mTargetId;
    boolean isFail;

    public ChatMySendVoiceModel(ItemChatMySendVoiceBinding itemChatMySendVoiceBinding, Activity activity, Uri voiceUri, int duration, boolean isRead, VoiceMessage voiceMessage, String targetId, boolean isFail) {
        this.mItemChatMySendVoiceBinding = itemChatMySendVoiceBinding;
        this.mActivity = activity;
        this.mVoiceUri = voiceUri;
        this.mDuration = duration;
        this.mIsRead = isRead;
        this.mVoiceMessage = voiceMessage;
        this.mTargetId = targetId;
        this.isFail = isFail;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        //计算录音条的长短，按照微信的规则，1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        int voiceLenUnit = CommonUtils.dip2px(5);
        int voiceStartLen = CommonUtils.dip2px(70);
        ViewGroup.LayoutParams layoutParams = mItemChatMySendVoiceBinding.flVoiceBox.getLayoutParams();
        if (mDuration <= 2) {
            layoutParams.width = voiceStartLen;
        } else if (mDuration <= 10) {
            layoutParams.width = voiceStartLen + (mDuration - 2) * voiceLenUnit;
        } else {
            layoutParams.width = voiceStartLen + 8 * voiceLenUnit + (mDuration - 10) / 10 * voiceLenUnit;
        }
        mItemChatMySendVoiceBinding.flVoiceBox.setLayoutParams(layoutParams);

//        setVoiceDuration(mDuration + " ̋   ");
        setVoiceDuration(mDuration + "\"");

        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMySendVoiceBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMySendVoiceBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMySendVoiceBinding.tvChatMsgReadStatus.setText("已读");
            mItemChatMySendVoiceBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_readed_marker_bg);
        } else {
//            mItemChatMySendVoiceBinding.tvChatMsgReadStatus.setText("未读");
            if (isFail) {
                mItemChatMySendVoiceBinding.ivChatSendMsgAgain.setVisibility(View.VISIBLE);
            } else {
                mItemChatMySendVoiceBinding.tvChatMsgReadStatus.setText("送达");
                mItemChatMySendVoiceBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_unreaded_marker_bg);
            }
        }
    }

    boolean isClickStartVoice = true;
    MediaPlayer mediaPlayer;

    public void playVoice(View v) {
        if (isClickStartVoice) {
            startVoice();
        } else {
            stopVoice();
        }
        isClickStartVoice = !isClickStartVoice;
    }

    //播放语音
    private void startVoice() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogKit.v("播放结束");
                isClickStartVoice = true;
                stopVoice();
            }
        });
        try {
            mediaPlayer.setDataSource(mActivity, mVoiceUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            startVoiceAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //停止播放语音
    private void stopVoice() {
        if (mediaPlayer != null) {
//            mediaPlayer.reset();
            try {
                mediaPlayer.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            stopVoiceAnimation();
        }
    }

    AnimationDrawable animVoicePlay;

    private void startVoiceAnimation() {
        mItemChatMySendVoiceBinding.ivChatVoiceAnim.setImageResource(R.drawable.anim_my_voice_play);
        animVoicePlay = (AnimationDrawable) mItemChatMySendVoiceBinding.ivChatVoiceAnim.getDrawable();
        animVoicePlay.start();
    }

    private void stopVoiceAnimation() {
        animVoicePlay.stop();
        animVoicePlay = null;
        mItemChatMySendVoiceBinding.ivChatVoiceAnim.setImageResource(R.mipmap.sound_bai3_icon);
    }


    //重新发送消息
    public void sendMsgAgain(View v) {
        if (mVoiceMessage != null && !TextUtils.isEmpty(mTargetId)) {
            String pushContent = LoginManager.currentLoginUserName + ":[语音消息],请查看";
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, mTargetId, mVoiceMessage, pushContent, null, new RongIMClient.SendMessageCallback() {
                @Override
                public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                }

                @Override
                public void onSuccess(Integer integer) {
                    mItemChatMySendVoiceBinding.ivChatSendMsgAgain.setVisibility(View.GONE);
                    //重新发送以后，需要在容易的消息记录里把原来这表messageId的消息删除掉
                    int messageId = (int) mItemChatMySendVoiceBinding.ivChatSendMsgAgain.getTag();
                    int[] ids = new int[]{messageId};
                    RongIMClient.getInstance().deleteMessages(ids, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //删除消息成功
                            //UI上面，要把这个View,提取到最下面
                            View messageView = mItemChatMySendVoiceBinding.getRoot();
                            ViewGroup parent = (ViewGroup) messageView.getParent();
                            parent.removeView(messageView);
                            parent.addView(messageView);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            }, new RongIMClient.ResultCallback<Message>() {
                @Override
                public void onSuccess(Message message) {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    private String voiceDuration;

    @Bindable
    public String getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(String voiceDuration) {
        this.voiceDuration = voiceDuration;
        notifyPropertyChanged(BR.voiceDuration);
    }

}
