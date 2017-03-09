package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.domain.MyHomeInfoBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.pager.HomeContactsPager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeInfoPager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class ActivityHomeModel extends BaseObservable {
    ActivityHomeBinding mActivityHomeBinding;
    private int chooseServiceAndDemandLayerVisibility = View.INVISIBLE;
    public Activity mActivity;

    public ActivityHomeModel(ActivityHomeBinding activityHomeBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityHomeBinding = activityHomeBinding;
        getCurrentLoginUserInfo();//二、[用戶信息]-获取个人资料 接口中的数据，获取当前登录者的个人信息
        getCurrentLoginUserInfo2();//十三、[用戶信息]-我的首页数据  接口中的数据，用来获取手机号
        initData();
    }

    private void initData() {
        //把用户的登录信息保存到SharePreferences中
        SpUtils.setLong("uid", LoginManager.currentLoginUserId);
        SpUtils.setString("token", LoginManager.token);
        SpUtils.setString("rongToken", LoginManager.rongToken);

        //首先注册未读消息的监听器，这样每次来新的聊天消息都会有更新
        MsgManager.setTotalUnReadCountListener(new MsgManager.TotalUnReadCountListener() {

            @Override
            public void displayTotalUnReadCount(int count) {
                LogKit.v("HomeActivity unReadCount:" + count);
                if (MsgManager.taskMessageCount == -1) {
                    MsgManager.taskMessageCount = SpUtils.getInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, 0);
                }
                count += MsgManager.taskMessageCount;
                if (count <= 0) {
                    mActivityHomeBinding.tvChatInfoCount.setVisibility(View.GONE);
                } else if (count <= 99) {
                    mActivityHomeBinding.tvChatInfoCount.setText(count + "");
                    mActivityHomeBinding.tvChatInfoCount.setVisibility(View.VISIBLE);
                } else {
                    mActivityHomeBinding.tvChatInfoCount.setText("99+");
                    mActivityHomeBinding.tvChatInfoCount.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getCurrentLoginUserInfo() {
        UserInfoEngine.getMyUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                LoginManager.currentLoginUserAvatar = dataBean.data.uinfo.avatar;
                LoginManager.currentLoginUserName = dataBean.data.uinfo.name;
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取我的个人信息失败:" + result);
            }
        });
    }

    private void getCurrentLoginUserInfo2() {
        UserInfoEngine.getMyHomeInfo(new BaseProtocol.IResultExecutor<MyHomeInfoBean>() {
            @Override
            public void execute(MyHomeInfoBean dataBean) {
                LoginManager.currentLoginUserPhone = dataBean.data.myinfo.phone;
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取我的首页数据失败:" + result);
            }
        });
    }

    @Bindable
    public int getChooseServiceAndDemandLayerVisibility() {
        return chooseServiceAndDemandLayerVisibility;
    }

    public void setChooseServiceAndDemandLayerVisibility(int chooseServiceAndDemandLayerVisibility) {
        this.chooseServiceAndDemandLayerVisibility = chooseServiceAndDemandLayerVisibility;
        notifyPropertyChanged(BR.chooseServiceAndDemandLayerVisibility);
    }


    View freeTimeRootView;
    View infoRootView;
    View contactsRootView;
    View myRootView;

    /**
     * 首页底部Tab点击切换页面
     *
     * @param v
     */
    public void changePager(View v) {
        mActivityHomeBinding.flActivityHomePager.removeAllViews();
        mActivityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#666666"));
        mActivityHomeBinding.tvContact.setTextColor(Color.parseColor("#666666"));
        mActivityHomeBinding.tvInfo.setTextColor(Color.parseColor("#666666"));
        mActivityHomeBinding.tvMy.setTextColor(Color.parseColor("#666666"));

        switch (v.getId()) {
            case R.id.ll_activity_home_freetime:
                setBottomTabIcon(R.mipmap.icon_idle_hours_press, R.mipmap.home_message_btn, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);

//                if (freeTimeRootView == null) {
                HomeActivity.currentCheckedPager = new HomeFreeTimePager(mActivity);
                freeTimeRootView = HomeActivity.currentCheckedPager.getRootView();
                mActivityHomeBinding.flActivityHomePager.addView(freeTimeRootView);
//                } else {
//                    //先remove然后再add可以把rootView提取到最上面
//                    mActivityHomeBinding.flActivityHomePager.removeView(freeTimeRootView);
//                    mActivityHomeBinding.flActivityHomePager.addView(freeTimeRootView);
//                    //重新创建rootView，并且在网络数据加载完毕之后，把原来的rootView删除掉，然后把新的rootView加入进去
//                    HomeActivity.currentCheckedPager = new HomeFreeTimePager(mActivity, new Runnable() {
//
//                        @Override
//                        public void run() {
//                            View newFreeTimeRootView = HomeActivity.currentCheckedPager.getRootView();
//                            mActivityHomeBinding.flActivityHomePager.removeView(freeTimeRootView);
//                            mActivityHomeBinding.flActivityHomePager.addView(newFreeTimeRootView);
//                            freeTimeRootView = newFreeTimeRootView;
//                        }
//                    });
//                }

                LogKit.v("freetime");
                HomeActivity.currentCheckedPageNo = HomeActivity.PAGE_FREETIME;

                mActivityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#31c5e4"));
                HomeActivity.goBackPageNo = HomeActivity.PAGE_FREETIME;
                break;
            case R.id.ll_activity_home_info:
                setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.icon_message_press, R.mipmap.icon_contacts_moren, R.mipmap.home_wode_btn);

//                if (infoRootView == null) {
                HomeActivity.currentCheckedPager = new HomeInfoPager(mActivity);
                infoRootView = HomeActivity.currentCheckedPager.getRootView();
//                }
//                mActivityHomeBinding.flActivityHomePager.removeView(infoRootView);
                mActivityHomeBinding.flActivityHomePager.addView(infoRootView);

                LogKit.v("info");
                HomeActivity.currentCheckedPageNo = HomeActivity.PAGE_INFO;

                mActivityHomeBinding.tvInfo.setTextColor(Color.parseColor("#31c5e4"));
                HomeActivity.goBackPageNo = HomeActivity.PAGE_INFO;
                break;
            case R.id.ll_activity_home_contacts:
                setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.home_message_btn, R.mipmap.icon_contacts_press, R.mipmap.home_wode_btn);

//                if (contactsRootView == null) {
                HomeActivity.currentCheckedPager = new HomeContactsPager(mActivity);
                contactsRootView = HomeActivity.currentCheckedPager.getRootView();
//                }
//                mActivityHomeBinding.flActivityHomePager.removeView(contactsRootView);
                mActivityHomeBinding.flActivityHomePager.addView(contactsRootView);

                LogKit.v("contacts");
                HomeActivity.currentCheckedPageNo = HomeActivity.PAGE_CONTACTS;

                mActivityHomeBinding.tvContact.setTextColor(Color.parseColor("#31c5e4"));
                HomeActivity.goBackPageNo = HomeActivity.PAGE_CONTACTS;
                break;
            case R.id.ll_activity_home_my:
                setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.home_message_btn, R.mipmap.icon_contacts_moren, R.mipmap.icon_my_center_press);

//                if (myRootView == null) {
                HomeActivity.currentCheckedPager = new HomeMyPager(mActivity);
                myRootView = HomeActivity.currentCheckedPager.getRootView();
//                }
//                mActivityHomeBinding.flActivityHomePager.removeView(myRootView);
                mActivityHomeBinding.flActivityHomePager.addView(myRootView);

                LogKit.v("my");
                HomeActivity.currentCheckedPageNo = HomeActivity.PAGE_MY;

                mActivityHomeBinding.tvMy.setTextColor(Color.parseColor("#31c5e4"));
                HomeActivity.goBackPageNo = HomeActivity.PAGE_MY;
                break;
        }
    }

    private void setBottomTabIcon(int freetimeIcon, int infoIcon, int contactsIcon, int myIcon) {
        mActivityHomeBinding.ivFreetimeIcon.setImageResource(freetimeIcon);
        mActivityHomeBinding.ivInfoIcon.setImageResource(infoIcon);
        mActivityHomeBinding.ivContactsIcon.setImageResource(contactsIcon);
        mActivityHomeBinding.ivMyIcon.setImageResource(myIcon);
    }

    public void chooseServiceAndDemandLayerDisplay(View v) {
        setChooseServiceAndDemandLayerVisibility(View.VISIBLE);
    }

    public void chooseServiceAndDemandLayerHide(View v) {
        setChooseServiceAndDemandLayerVisibility(View.INVISIBLE);
    }


    public void publishDemand(View v) {
//        Intent intentPublishDemandActivity = new Intent(CommonUtils.getContext(), PublishDemandActivity.class);
//        intentPublishDemandActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentPublishDemandActivity);

        //修改为第二版发布需求页面
        Intent intentPublishDemandBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishDemandBaseInfoActivity.class);
        mActivity.startActivity(intentPublishDemandBaseInfoActivity);
        setChooseServiceAndDemandLayerVisibility(View.GONE);

        //测试用，直接跳转到需求详情页
//        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
//        intentDemandDetailActivity.putExtra("demandId", 273l);
//        mActivity.startActivity(intentDemandDetailActivity);
    }

    public void publishService(View v) {
//        ToastUtils.shortToast("发布服务");
//        Intent intentPublishServiceActivity = new Intent(CommonUtils.getContext(), PublishServiceActivity.class);
//        intentPublishServiceActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentPublishServiceActivity);

//        Intent intentSecondTimePublishServiceActivity = new Intent(CommonUtils.getContext(), SecondTimePublishServiceActivity.class);
//        intentSecondTimePublishServiceActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentSecondTimePublishServiceActivity);


        Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
        mActivity.startActivity(intentPublishServiceBaseInfoActivity);
        setChooseServiceAndDemandLayerVisibility(View.GONE);

        //测试用，直接跳转到需求详情页
//        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
//        intentServiceDetailActivity.putExtra("serviceId", 103l);
//        mActivity.startActivity(intentServiceDetailActivity);
    }

    private int redPointHintVisibility = View.GONE;

    @Bindable
    public int getRedPointHintVisibility() {
        return redPointHintVisibility;
    }

    public void setRedPointHintVisibility(int redPointHintVisibility) {
        this.redPointHintVisibility = redPointHintVisibility;
        notifyPropertyChanged(BR.redPointHintVisibility);
    }
}
