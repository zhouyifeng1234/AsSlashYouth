package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommentBinding;

/**
 * 当服务方顺利完成需求方的任务之后，需求方对服务方评价F
 * Created by zhouyifeng on 2016/11/11.
 */
public class CommentModel extends BaseObservable {

    ActivityCommentBinding mActivityCommentBinding;
    Activity mActivity;

    int serviceQualityMarks = 0;
    int completeSpeedMarks = 0;
    int serviceAttitudeMarks = 0;


    public CommentModel(ActivityCommentBinding activityCommentBinding, Activity activity) {
        this.mActivityCommentBinding = activityCommentBinding;
        this.mActivity = activity;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void giveMarks(View v) {
        ViewGroup viewGroup = (ViewGroup) v.getParent();
        int currentCheckedIndex = viewGroup.indexOfChild(v);
        displayStars(viewGroup, currentCheckedIndex);
        switch (viewGroup.getId()) {
            case R.id.ll_service_quality_stars:
                serviceQualityMarks = currentCheckedIndex + 1;
                break;
            case R.id.ll_complete_speed_stars:
                completeSpeedMarks = currentCheckedIndex + 1;
                break;
            case R.id.ll_service_attitude_stars:
                serviceAttitudeMarks = currentCheckedIndex + 1;
                break;
        }
    }

    private void displayStars(ViewGroup viewGroup, int currentCheckedIndex) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView ivStar = (ImageView) viewGroup.getChildAt(i);
            if (i <= currentCheckedIndex) {
                ivStar.setImageResource(R.mipmap.activation_star);
            } else {
                ivStar.setImageResource(R.mipmap.default_star);
            }
        }
    }

}
