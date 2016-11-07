package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityContactUsBinding;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/4.
 */
public class ContactUsModel extends BaseObservable {

    private ActivityContactUsBinding activityContactUsBinding;

    public ContactUsModel(ActivityContactUsBinding activityContactUsBinding) {
        this.activityContactUsBinding = activityContactUsBinding;
    }

    public void question(View view){
        LogKit.d("问题");
    }

    public void contactUs(View view){
        LogKit.d("联系我们");
    }

    public void updateVersion(View view){
        LogKit.d("更新版本");
    }

}
