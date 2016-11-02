package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.sip.SipAudioCall;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.slash.youth.databinding.PagerHomeMyBinding;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeMyModel extends BaseObservable {
    PagerHomeMyBinding mPagerHomeMyBinding;
    Activity mActivity;
    //    float totalExpertMarks = 2000;
    float expertⅠMaxMarks = 1000;
    float expertⅡMaxMarks = 4000;
    float expertⅢMaxMarks = 10000;
    float expertⅣMaxMarks = 99999;
    float expertMarks;
    float expertMarksProgress;//0到360

    public PagerHomeMyModel(PagerHomeMyBinding pagerHomeMyBinding, Activity activity) {
        this.mPagerHomeMyBinding = pagerHomeMyBinding;
        this.mActivity = activity;
        initData();
        initAnimation();
        initView();
    }

    RotateAnimation raExpertMarksMaker;

    private void initAnimation() {
        raExpertMarksMaker = new RotateAnimation(0, expertMarksProgress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raExpertMarksMaker.setDuration(120 * 16);
        raExpertMarksMaker.setInterpolator(new LinearInterpolator());
        raExpertMarksMaker.setFillAfter(true);
    }

    private void initView() {
        mPagerHomeMyBinding.svPagerHomeMy.setVerticalScrollBarEnabled(false);
        mPagerHomeMyBinding.flHomeMyExpertMarksMaker.startAnimation(raExpertMarksMaker);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.setTotalProgressAngle(expertMarksProgress);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.post(new Runnable() {
            @Override
            public void run() {
                mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.initRingProgressDraw();
            }
        });
        initExpertMarksProgress();

    }

    private void initExpertMarksProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 120; i++) {
                    try {
                        long startMill = System.currentTimeMillis();
                        final float displayMarks = expertMarks / 120 * (i + 1);
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mPagerHomeMyBinding.tvHomeMyExpertMarks.setText((int) displayMarks + "");
                            }
                        });
                        long endMill = System.currentTimeMillis();
                        Thread.sleep(16 - (endMill - startMill));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void initData() {
        expertMarks = 9999;//这个数据实际应该从服务端获取
//        expertMarksProgress = expertMarks / totalExpertMarks * 360;
        if (expertMarks >= 0 && expertMarks <= expertⅠMaxMarks) {
            expertMarksProgress = expertMarks / expertⅠMaxMarks * 90;
        } else if (expertMarks <= expertⅡMaxMarks) {
            expertMarksProgress = 90 + (expertMarks - expertⅠMaxMarks) / (expertⅡMaxMarks - expertⅠMaxMarks) * 90;
        } else if (expertMarks <= expertⅢMaxMarks) {
            expertMarksProgress = 180 + (expertMarks - expertⅡMaxMarks) / (expertⅢMaxMarks - expertⅡMaxMarks) * 90;
        } else if (expertMarks <= expertⅣMaxMarks) {
            expertMarksProgress = 270 + (expertMarks - expertⅢMaxMarks) / (expertⅣMaxMarks - expertⅢMaxMarks) * 90;
        }
    }
        //编辑点击事件
        public void editor(View view){
            Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
            intentUserInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentUserInfoActivity);
         }
}
