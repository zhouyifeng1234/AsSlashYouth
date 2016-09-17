package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.ui.activity.PublishDemandActivity;
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
    ActivityHomeBinding activityHomeBinding;

    public ActivityHomeModel(ActivityHomeBinding activityHomeBinding) {
        this.activityHomeBinding = activityHomeBinding;
    }

    /**
     * 首页底部Tab点击切换页面
     *
     * @param v
     */
    public void changePager(View v) {
        activityHomeBinding.flActivityHomePager.removeAllViews();
        switch (v.getId()) {
            case R.id.ll_activity_home_freetime:
                activityHomeBinding.flActivityHomePager.addView(new HomeFreeTimePager().getRootView());
                LogKit.v("freetime");
                break;
            case R.id.ll_activity_home_info:
                activityHomeBinding.flActivityHomePager.addView(new HomeInfoPager().getRootView());
                LogKit.v("info");
                break;
            case R.id.ll_activity_home_contacts:
                activityHomeBinding.flActivityHomePager.addView(new HomeContactsPager().getRootView());
                LogKit.v("contacts");
                break;
            case R.id.ll_activity_home_my:
                activityHomeBinding.flActivityHomePager.addView(new HomeMyPager().getRootView());
                LogKit.v("my");
                break;
        }
    }

    public void publishService(View v) {
//        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
//        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentMapActivity);

        Intent intentPublishDemandActivity = new Intent(CommonUtils.getContext(), PublishDemandActivity.class);
        intentPublishDemandActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandActivity);
    }

}
