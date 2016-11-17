package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChatBinding;
import com.slash.youth.ui.viewmodel.ChatModel;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatBinding activityChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        ChatModel chatModel = new ChatModel(activityChatBinding, this);
        activityChatBinding.setChatModel(chatModel);
    }
}
