package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishServiceModeBinding;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceModeModel extends BaseObservable {
    ActivityPublishServiceModeBinding mActivityPublishServiceModeBinding;
    Activity mActivity;

    public PublishServiceModeModel(ActivityPublishServiceModeBinding activityPublishServiceModeBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceModeBinding = activityPublishServiceModeBinding;
        initView();
    }

    private void initView() {


    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void publishService(View v) {
        //这里应该调用服务端相关接口进行发布服务
        ToastUtils.shortToast("发布服务成功");
    }
}
