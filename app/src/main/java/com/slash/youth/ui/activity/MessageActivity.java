package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMessageBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MessageModel;

/**
 * V1.1版本新增加的消息列表页面，原来V1.0版本是在首页中的一个Page
 * <p/>
 * Created by zhouyifeng on 2017/2/28.
 */
public class MessageActivity extends BaseActivity {

    MessageModel mMessageModel;
    boolean isStartActivity = false;//这个变量用来区分是否是第一次启动Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMessageBinding activityMessageBinding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        mMessageModel = new MessageModel(activityMessageBinding, this);
        activityMessageBinding.setMessageModel(mMessageModel);

        isStartActivity = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //目前通过返回MessageActivity执行onStart()方法的，只有需求和服务的发布成功页面，以及聊天页面返回，这几种情况都需要刷新会话列表，所有不需要做判断
        //如果以后有其它情况，再做处理
        if (!isStartActivity) {
            mMessageModel.getDataFromServer();
        }
        isStartActivity = false;
    }

    public MessageModel getMessageModel() {
        return mMessageModel;
    }
}
