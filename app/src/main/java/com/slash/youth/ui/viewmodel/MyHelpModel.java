package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyHelpBinding;
import com.slash.youth.ui.activity.CommonQuestionActivity;
import com.slash.youth.ui.activity.ContactUsActivity;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.activity.MyHelpActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zss on 2016/11/4.
 */
public class MyHelpModel extends BaseObservable {

    private ActivityMyHelpBinding activityMyHelpBinding;
    private MyHelpActivity myHelpActivity;
    private String text1 = " 在这里，斜杠为金领、白领、自雇者提供知识技能变现、结识人脉、提高自我价值的机会；";
    private String text2 ="在这里，斜杠为中小互联网企业及个人提供订单式、定制化、全方位的服务，促使降低成本提升核心竞争力。";


    public MyHelpModel(ActivityMyHelpBinding activityMyHelpBinding,MyHelpActivity myHelpActivity) {
        this.activityMyHelpBinding = activityMyHelpBinding;
        this.myHelpActivity = myHelpActivity;
        initView();
    }

    private void initView() {
        activityMyHelpBinding.tvLineText1.setText(text1);
        activityMyHelpBinding.tvLineText2.setText(text2);
    }

    //commonQuestion 常见问题
    public void commonQuestion(View view){
        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), CommonQuestionActivity.class);
        myHelpActivity.startActivity(intentCommonQuestionActivity);
    }

    //contactUs 联系我们
    public void contactUs(View view){
       Intent intentContactUsActivity = new Intent(CommonUtils.getContext(), ContactUsActivity.class);
        myHelpActivity.startActivity(intentContactUsActivity);
    }

     //updateVersion 版本更新
    public void updateVersion(View view){
        DialogUtils.showDialogFive(myHelpActivity, "检测到新版，是否更新？", "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {

                ToastUtils.longCenterToast("当前已是最新版本!");
            }

            @Override
            public void CancleDown() {

            }
        });
    }

}
