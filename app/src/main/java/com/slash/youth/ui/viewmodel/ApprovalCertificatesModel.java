package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalCertificatesModel extends BaseObservable {

    private ApprovalCertificatesBinding approvalCertificatesBinding;

    public ApprovalCertificatesModel(ApprovalCertificatesBinding approvalCertificatesBinding) {
        this.approvalCertificatesBinding = approvalCertificatesBinding;
        initData();
       // initView();
    }

    private void initView() {


    }

    private void initData() {
    }

    public void shotPhoto(View view){
        LogKit.d("拍摄照片");

    }

    public void albunPhoto(View view){
        LogKit.d("相册照片");

    }

}
