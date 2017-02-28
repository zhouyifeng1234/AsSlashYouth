package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHome2Binding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ActivityHomeModel2;

/**
 * V1.1版本改版的HomeActivity
 * <p>
 * Created by zhouyifeng on 2017/2/27.
 */
public class HomeActivity2 extends BaseActivity {
    ActivityHome2Binding mActivityHome2Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityHome2Binding = DataBindingUtil.setContentView(this, R.layout.activity_home2);
        ActivityHomeModel2 activityHomeModel2 = new ActivityHomeModel2(mActivityHome2Binding, this);
        mActivityHome2Binding.setActivityHomeModel2(activityHomeModel2);

    }

    public ViewPager getInnerViewPager() {
        return mActivityHome2Binding.vpHomePager;
    }

}
