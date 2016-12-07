package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChatBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.ui.viewmodel.ChatModel;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatActivity extends Activity {

    private ChatModel mChatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatBinding activityChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mChatModel = new ChatModel(activityChatBinding, this);
        activityChatBinding.setChatModel(mChatModel);
    }

    @Override
    protected void onDestroy() {
        MsgManager.removeChatTextListener();
        MsgManager.removeChatPicListener();
        MsgManager.removeChatVoiceListener();
        MsgManager.removeChatOtherCmdListener();
        MsgManager.removeHistoryListener();
        MsgManager.removeRelatedTaskListener();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    mChatModel.soundRecord();
                } else {
                    // Permission Denied
                    ToastUtils.shortToast("RECORD_AUDIO Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
