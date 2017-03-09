package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatOtherSendVoiceBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.io.IOException;

/**
 * Created by zhouyifeng on 2016/11/18.
 */
public class ChatOtherSendVoiceModel extends BaseObservable {
    ItemChatOtherSendVoiceBinding mItemChatOtherSendVoiceBinding;
    Activity mActivity;
    Uri mVoiceUri;
    int mDuration;
    String mTargetAvatar;

    public ChatOtherSendVoiceModel(ItemChatOtherSendVoiceBinding itemChatOtherSendVoiceBinding, Activity activity, Uri voiceUri, int duration, String targetAvatar) {
        this.mItemChatOtherSendVoiceBinding = itemChatOtherSendVoiceBinding;
        this.mActivity = activity;
        this.mVoiceUri = voiceUri;
        this.mDuration = duration;
        this.mTargetAvatar = targetAvatar;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        //计算录音条的长短，按照微信的规则，1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        int voiceLenUnit = CommonUtils.dip2px(5);
        int voiceStartLen = CommonUtils.dip2px(70);
        ViewGroup.LayoutParams layoutParams = mItemChatOtherSendVoiceBinding.flVoiceBox.getLayoutParams();
        if (mDuration <= 2) {
            layoutParams.width = voiceStartLen;
        } else if (mDuration <= 10) {
            layoutParams.width = voiceStartLen + (mDuration - 2) * voiceLenUnit;
        } else {
            layoutParams.width = voiceStartLen + 8 * voiceLenUnit + (mDuration - 10) / 10 * voiceLenUnit;
        }
        mItemChatOtherSendVoiceBinding.flVoiceBox.setLayoutParams(layoutParams);

//        setVoiceDuration(mDuration + " ̋   ");
        setVoiceDuration(mDuration + "\"");

        if ((!"1000".equals(MsgManager.targetId)) && (!MsgManager.customerServiceUid.equals(MsgManager.targetId))) {
            BitmapKit.bindImage(mItemChatOtherSendVoiceBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatOtherSendVoiceBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
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
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            stopVoiceAnimation();
        }
    }

    AnimationDrawable animVoicePlay;

    private void startVoiceAnimation() {
        mItemChatOtherSendVoiceBinding.ivChatVoiceAnim.setImageResource(R.drawable.anim_other_voice_play);
        animVoicePlay = (AnimationDrawable) mItemChatOtherSendVoiceBinding.ivChatVoiceAnim.getDrawable();
        animVoicePlay.start();
    }

    private void stopVoiceAnimation() {
        animVoicePlay.stop();
        animVoicePlay = null;
        mItemChatOtherSendVoiceBinding.ivChatVoiceAnim.setImageResource(R.mipmap.sound3_icon);
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
