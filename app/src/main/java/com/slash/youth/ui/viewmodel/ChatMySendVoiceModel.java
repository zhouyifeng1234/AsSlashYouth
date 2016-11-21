package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatMySendVoiceBinding;
import com.slash.youth.utils.LogKit;

import java.io.IOException;

/**
 * Created by zhouyifeng on 2016/11/18.
 */
public class ChatMySendVoiceModel extends BaseObservable {
    ItemChatMySendVoiceBinding mItemChatMySendVoiceBinding;
    Activity mActivity;
    Uri mVoiceUri;
    int mDuration;


    public ChatMySendVoiceModel(ItemChatMySendVoiceBinding itemChatMySendVoiceBinding, Activity activity, Uri voiceUri, int duration) {
        this.mItemChatMySendVoiceBinding = itemChatMySendVoiceBinding;
        this.mActivity = activity;
        this.mVoiceUri = voiceUri;
        this.mDuration = duration;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        setVoiceDuration(mDuration + " ̋   ");
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
