package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChooseFriendBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ChooseFriendModel;


/**
 * Created by zss on 2016/11/2.
 */
public class MyFriendActivtiy extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private ActivityChooseFriendBinding activityChooseFriendBinding;
    private String titleString = "我的好友";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean sendFriend = intent.getBooleanExtra("sendFriend", false);
        activityChooseFriendBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_friend);
        ChooseFriendModel chooseFriendModel = new ChooseFriendModel(this, activityChooseFriendBinding, this, sendFriend);
        activityChooseFriendBinding.setChooseFriendModel(chooseFriendModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleString);
        findViewById(R.id.fl_title_right).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }
}
