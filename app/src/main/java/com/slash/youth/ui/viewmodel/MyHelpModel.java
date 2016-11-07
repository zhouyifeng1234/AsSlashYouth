package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyHelpBinding;
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


    public MyHelpModel(ActivityMyHelpBinding activityMyHelpBinding,MyHelpActivity myHelpActivity) {
        this.activityMyHelpBinding = activityMyHelpBinding;
        this.myHelpActivity = myHelpActivity;
    }


    //commonQuestion
    public void commonQuestion(View view){
        LogKit.d("常见问题");

    }

    //contactUs
    public void contactUs(View view){
        LogKit.d("联系我们");
       Intent intentContactUsActivity = new Intent(CommonUtils.getContext(), ContactUsActivity.class);
        intentContactUsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentContactUsActivity);
    }

     //updateVersion
    public void updateVersion(View view){
        LogKit.d("版本更新");

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
