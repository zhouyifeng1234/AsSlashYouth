package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityExamineCertificatesBinding;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/6.
 */
public class ExamineCertificatesModel extends BaseObservable {
    private ActivityExamineCertificatesBinding activityExamineCertificatesBinding;

    public ExamineCertificatesModel(ActivityExamineCertificatesBinding activityExamineCertificatesBinding) {
        this.activityExamineCertificatesBinding = activityExamineCertificatesBinding;
    }

    //提交审核
    public void examine(View view){
        LogKit.d("提交审核");


    }

    //重新上传
    public void upload(View view){
        LogKit.d("上传");


    }


}
