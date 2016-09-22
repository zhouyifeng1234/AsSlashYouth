package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBinding;
import com.slash.youth.ui.activity.PublishServiceInfoActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishServiceModel extends BaseObservable {

    ActivityPublishServiceBinding mActivityPublishServiceBinding;
    Activity mActivity;

    public PublishServiceModel(ActivityPublishServiceBinding activityPublishServiceBinding, Activity activity) {
        this.mActivityPublishServiceBinding = activityPublishServiceBinding;
        initView();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishServiceInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceInfoActivity.class);
        intentPublishServiceInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishServiceInfoActivity);
    }


    private boolean isAllDayChecked = true;

    public void checkAllDay(View v) {
        if (isAllDayChecked) {
            mActivityPublishServiceBinding.ivPublishServiceCheckAllday.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            mActivityPublishServiceBinding.ivPublishServiceCheckAllday.setImageResource(R.mipmap.dui_icon);
        }
        isAllDayChecked = !isAllDayChecked;
    }

    public void chooseIdleTime(View v) {

    }


}
