package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityTagRecommendBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.TagRecommendModel;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class TagRecommendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTagRecommendBinding activityTagRecommendBinding = DataBindingUtil.setContentView(this, R.layout.activity_tag_recommend);
        TagRecommendModel tagRecommendModel = new TagRecommendModel(activityTagRecommendBinding, this);
        activityTagRecommendBinding.setTagRecommendModel(tagRecommendModel);
    }
}
