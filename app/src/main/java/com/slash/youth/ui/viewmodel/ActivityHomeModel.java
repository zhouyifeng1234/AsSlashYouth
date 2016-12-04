package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.pager.HomeContactsPager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeInfoPager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

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
    }

    @Bindable
    public int getChooseServiceAndDemandLayerVisibility() {
        return chooseServiceAndDemandLayerVisibility;
    }

    public void setChooseServiceAndDemandLayerVisibility(int chooseServiceAndDemandLayerVisibility) {
        this.chooseServiceAndDemandLayerVisibility = chooseServiceAndDemandLayerVisibility;
        notifyPropertyChanged(BR.chooseServiceAndDemandLayerVisibility);
    }

    /**
     * 首页底部Tab点击切换页面
     *
     * @param v
     */
    public void changePager(View v) {
        mActivityHomeBinding.flActivityHomePager.removeAllViews();
        switch (v.getId()) {
            case R.id.ll_activity_home_freetime:
                setBottomTabIcon(R.mipmap.home_xianshi_btn, R.mipmap.home_message_btn, R.mipmap.home_renmai_btn, R.mipmap.home_wode_btn);
                mActivityHomeBinding.flActivityHomePager.addView(new HomeFreeTimePager(mActivity).getRootView());
                LogKit.v("freetime");
                break;
            case R.id.ll_activity_home_info:
                setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.icon_message_press, R.mipmap.home_renmai_btn, R.mipmap.home_wode_btn);
                mActivityHomeBinding.flActivityHomePager.addView(new HomeInfoPager(mActivity).getRootView());
                LogKit.v("info");
                break;
            case R.id.ll_activity_home_contacts:
                setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.home_message_btn, R.mipmap.icon_contacts_press, R.mipmap.home_wode_btn);
                mActivityHomeBinding.flActivityHomePager.addView(new HomeContactsPager(mActivity).getRootView());
                LogKit.v("contacts");
                break;
            case R.id.ll_activity_home_my:
                setBottomTabIcon(R.mipmap.icon_idle_hours_moren, R.mipmap.home_message_btn, R.mipmap.home_renmai_btn, R.mipmap.icon_my_center_press);
                mActivityHomeBinding.flActivityHomePager.addView(new HomeMyPager(mActivity).getRootView());
                LogKit.v("my");
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
        intentPublishDemandBaseInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandBaseInfoActivity);
    }

    public void publishService(View v) {
//        ToastUtils.shortToast("发布服务");
//        Intent intentPublishServiceActivity = new Intent(CommonUtils.getContext(), PublishServiceActivity.class);
//        intentPublishServiceActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentPublishServiceActivity);

//        Intent intentSecondTimePublishServiceActivity = new Intent(CommonUtils.getContext(), SecondTimePublishServiceActivity.class);
//        intentSecondTimePublishServiceActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentSecondTimePublishServiceActivity);


//        Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
//        mActivity.startActivity(intentPublishServiceBaseInfoActivity);

        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
        intentServiceDetailActivity.putExtra("serviceId", 103l);
        mActivity.startActivity(intentServiceDetailActivity);
    }
}
