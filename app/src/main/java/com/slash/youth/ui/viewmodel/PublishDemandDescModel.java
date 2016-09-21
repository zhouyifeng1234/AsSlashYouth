package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.activity.PublishDemandModeActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandDescModel extends BaseObservable {
    ActivityPublishDemandDescribeBinding mActivityPublishDemandDescribeBinding;
    Activity mActivity;
    Bundle publishDemandDataBundle;

    public PublishDemandDescModel(ActivityPublishDemandDescribeBinding activityPublishDemandDescribeBinding, Activity activity, Bundle publishDemandDataBundle) {
        this.mActivityPublishDemandDescribeBinding = activityPublishDemandDescribeBinding;
        this.mActivity = activity;
        this.publishDemandDataBundle = publishDemandDataBundle;
        initView();
    }

    private void initView() {


    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishDemandModeActivity = new Intent(CommonUtils.getContext(), PublishDemandModeActivity.class);
        intentPublishDemandModeActivity.putExtra("publishDemandDataBundle", publishDemandDataBundle);
        intentPublishDemandModeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandModeActivity);
    }

    public void addPicture(View v) {
//        Intent intentAddPicture = new Intent();
//        intentAddPicture.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intentAddPicture.setType("image/*");
//        intentAddPicture.setAction(Intent.ACTION_GET_CONTENT);
//        CommonUtils.getContext().startActivity(intentAddPicture);

//        private File tempFile;
//        this.tempFile = new File(FilePathUtility.GetReAudioFilePath(
//                PersonalEditActivity.this, "head.jpg", true));
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
/* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
/* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
/* 出现截取界面 */
        intent.putExtra("crop", "true");
/*保存到SD*/
//        intent.putExtra("output", Uri.fromFile(tempFile));
/*设置图片像素*/
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
/*设置图片格式*/
        intent.putExtra("outputFormat", "JPEG");
/* 设置比例 1:1 */
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
/* 取得相片后返回本画面 */
        CommonUtils.getContext().startActivity(intent);
    }

}
