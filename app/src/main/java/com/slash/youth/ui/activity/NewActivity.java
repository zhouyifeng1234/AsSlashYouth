package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityNewBinding;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.ui.viewmodel.ActivitySearchModel;
import com.slash.youth.ui.viewmodel.NewActivityModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/10/10.
 */
public class NewActivity extends Activity {
    private ActivityNewBinding activityNewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonUtils.setCurrentActivity(this);

        activityNewBinding = DataBindingUtil.setContentView(this, R.layout.activity_new);
        NewActivityModel newActivityModel = new NewActivityModel(activityNewBinding);
        activityNewBinding.setNewActivityModel(newActivityModel);


    }
}