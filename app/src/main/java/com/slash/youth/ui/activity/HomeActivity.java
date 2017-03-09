package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeContactsPager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeInfoPager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * V1.0版的HomeActivity
 */
public class HomeActivity extends BaseActivity {
    public static final int PAGE_FREETIME = 0;//首页闲时
    public static final int PAGE_INFO = 1;//首页消息
    public static final int PAGE_CONTACTS = 2;//首页人脉
    public static final int PAGE_MY = 3;//首页我的

    public static int currentCheckedPageNo;
    public static BaseHomePager currentCheckedPager;

    private ActivityHomeBinding activityHomeBinding;

    public static int goBackPageNo;//从其他页面返回首页时需要首页展示的pager
    public static ActivityHomeModel activityHomeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        activityHomeModel = new ActivityHomeModel(activityHomeBinding, this);
        activityHomeBinding.setActivityHomeBinding(activityHomeModel);

        setBottomTabIcon(R.mipmap.icon_idle_hours_press, R.mipmap.home_message_btn, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);
        currentCheckedPager = new HomeFreeTimePager(this);
        currentCheckedPageNo = PAGE_FREETIME;
        activityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#31c5e4"));
        activityHomeBinding.flActivityHomePager.addView(currentCheckedPager.getRootView());

        goBackPageNo = PAGE_FREETIME;
    }


    @Override
    protected void onStart() {
        super.onStart();
        //刚进入首页的时候，或者是回退到首页的时候(这两种情况都会调用onStart方法)，通过融云的API获取总的未读消息数，初始化小圆点数量
        RongIMClient.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                int totalUnreadCount = integer;
                LogKit.v("HomeActivity unReadCount:" + totalUnreadCount);
                if (MsgManager.taskMessageCount == -1) {
                    MsgManager.taskMessageCount = SpUtils.getInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, 0);
                }
                totalUnreadCount += MsgManager.taskMessageCount;
                if (totalUnreadCount <= 0) {
                    activityHomeBinding.tvChatInfoCount.setVisibility(View.GONE);
                } else if (totalUnreadCount <= 99) {
                    activityHomeBinding.tvChatInfoCount.setText(totalUnreadCount + "");
                    activityHomeBinding.tvChatInfoCount.setVisibility(View.VISIBLE);
                } else {
                    activityHomeBinding.tvChatInfoCount.setText("99+");
                    activityHomeBinding.tvChatInfoCount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        }, Conversation.ConversationType.PRIVATE);

        if (goBackPageNo != currentCheckedPageNo) {
            activityHomeBinding.flActivityHomePager.removeAllViews();
            activityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#666666"));
            activityHomeBinding.tvContact.setTextColor(Color.parseColor("#666666"));
            activityHomeBinding.tvInfo.setTextColor(Color.parseColor("#666666"));
            activityHomeBinding.tvMy.setTextColor(Color.parseColor("#666666"));
            switch (goBackPageNo) {
                case PAGE_FREETIME:
                    setBottomTabIcon(R.mipmap.icon_idle_hours_press, R.mipmap.home_message_btn, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);
                    currentCheckedPager = new HomeFreeTimePager(this);
                    currentCheckedPageNo = PAGE_FREETIME;
                    activityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#31c5e4"));
                    break;
                case PAGE_INFO:
                    setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.icon_message_press, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);
                    currentCheckedPager = new HomeInfoPager(this);
                    currentCheckedPageNo = PAGE_INFO;
                    activityHomeBinding.tvInfo.setTextColor(Color.parseColor("#31c5e4"));
                    break;
                case PAGE_CONTACTS:
                    setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.home_message_btn, R.mipmap.icon_contacts_press, R.mipmap.home_wode_btn);
                    currentCheckedPager = new HomeContactsPager(this);
                    currentCheckedPageNo = PAGE_CONTACTS;
                    activityHomeBinding.tvContact.setTextColor(Color.parseColor("#31c5e4"));
                    break;
                case PAGE_MY:
                    setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.home_message_btn, R.mipmap.icon_contacts_moren, R.mipmap.icon_my_center_press);
                    currentCheckedPager = new HomeMyPager(this);
                    currentCheckedPageNo = PAGE_MY;
                    activityHomeBinding.tvMy.setTextColor(Color.parseColor("#31c5e4"));
                    break;
                default:
                    LogKit.v("error goBackPageNo:" + goBackPageNo);
                    break;
            }
            activityHomeBinding.flActivityHomePager.addView(currentCheckedPager.getRootView());
        } else {
            if (currentCheckedPager instanceof HomeInfoPager) {
                HomeInfoPager homeInfoPager = (HomeInfoPager) currentCheckedPager;
                homeInfoPager.mPagerHomeInfoModel.getDataFromServer();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UserInfoEngine.MY_USER_EDITOR:
                activityHomeBinding.flActivityHomePager.removeAllViews();
                activityHomeBinding.flActivityHomePager.addView(new HomeMyPager(this).getRootView());
                break;
            case Constants.APPROVAL:
                activityHomeBinding.flActivityHomePager.removeAllViews();
                activityHomeBinding.flActivityHomePager.addView(new HomeMyPager(this).getRootView());
                break;
        }
    }

    private void setBottomTabIcon(int freetimeIcon, int infoIcon, int contactsIcon, int myIcon) {
        activityHomeBinding.ivFreetimeIcon.setImageResource(freetimeIcon);
        activityHomeBinding.ivInfoIcon.setImageResource(infoIcon);
        activityHomeBinding.ivContactsIcon.setImageResource(contactsIcon);
        activityHomeBinding.ivMyIcon.setImageResource(myIcon);
    }
}
