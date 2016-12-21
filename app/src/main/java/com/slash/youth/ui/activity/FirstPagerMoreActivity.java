package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityEditorIdentityBinding;
import com.slash.youth.databinding.ActivityFirstPagerMoreBinding;
import com.slash.youth.ui.viewmodel.EditorIdentityModel;
import com.slash.youth.ui.viewmodel.FirstPagerDemandModel;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/1.
 */
public class FirstPagerMoreActivity extends Activity {
    private ActivityFirstPagerMoreBinding activityFirstPagerMoreBinding;
    private static final String  TITLE ="更多服务";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean isDemand = intent.getBooleanExtra("isDemand", false);
        activityFirstPagerMoreBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_pager_more);
        if(!isDemand){
            activityFirstPagerMoreBinding.tvFirstPagerTitle.setText(TITLE);
        }
        FirstPagerDemandModel firstPagerDemandModel = new FirstPagerDemandModel(activityFirstPagerMoreBinding,isDemand,this);
        activityFirstPagerMoreBinding.setFirstPagerDemandModel(firstPagerDemandModel);
    }

}
