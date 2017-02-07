package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeInfoPager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ShareTaskUtils;

public class HomeActivity extends Activity {
    public static final int PAGE_FREETIME = 0;//首页闲时
    public static final int PAGE_INFO = 1;//首页消息
    public static final int PAGE_CONTACTS = 2;//首页人脉
    public static final int PAGE_MY = 3;//首页我的

    public static int currentCheckedPageNo;
    public static BaseHomePager currentCheckedPager;

    private ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        ActivityHomeModel activityHomeModel = new ActivityHomeModel(activityHomeBinding, this);
        activityHomeBinding.setActivityHomeBinding(activityHomeModel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        boolean publishSuccessShareTask = intent.getBooleanExtra(ShareTaskUtils.PUBLISH_SUCCESS_SHARE_TASK, false);

        //初始化默认页面
        activityHomeBinding.flActivityHomePager.removeAllViews();
        activityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#666666"));
        activityHomeBinding.tvContact.setTextColor(Color.parseColor("#666666"));
        activityHomeBinding.tvInfo.setTextColor(Color.parseColor("#666666"));
        activityHomeBinding.tvMy.setTextColor(Color.parseColor("#666666"));
        if (publishSuccessShareTask) {
            setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.icon_message_press, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);
            currentCheckedPager = new HomeInfoPager(this);
            currentCheckedPageNo = PAGE_INFO;
            activityHomeBinding.tvInfo.setTextColor(Color.parseColor("#31c5e4"));
        } else {
            setBottomTabIcon(R.mipmap.icon_idle_hours_press, R.mipmap.home_message_btn, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);
            currentCheckedPager = new HomeFreeTimePager(this);
            currentCheckedPageNo = PAGE_FREETIME;
            activityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#31c5e4"));
        }
        activityHomeBinding.flActivityHomePager.addView(currentCheckedPager.getRootView());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentCheckedPager instanceof HomeInfoPager) {
            HomeInfoPager homeInfoPager = (HomeInfoPager) currentCheckedPager;
            homeInfoPager.mPagerHomeInfoModel.getDataFromServer();
        }
        if (requestCode == UserInfoEngine.MY_USER_EDITOR) {
            activityHomeBinding.flActivityHomePager.removeAllViews();
            activityHomeBinding.flActivityHomePager.addView(new HomeMyPager(this).getRootView());
        }
    }

    private void setBottomTabIcon(int freetimeIcon, int infoIcon, int contactsIcon, int myIcon) {
        activityHomeBinding.ivFreetimeIcon.setImageResource(freetimeIcon);
        activityHomeBinding.ivInfoIcon.setImageResource(infoIcon);
        activityHomeBinding.ivContactsIcon.setImageResource(contactsIcon);
        activityHomeBinding.ivMyIcon.setImageResource(myIcon);
    }
}
