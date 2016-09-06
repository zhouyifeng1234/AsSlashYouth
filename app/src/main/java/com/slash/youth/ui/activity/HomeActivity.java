package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        ActivityHomeModel activityHomeModel = new ActivityHomeModel(activityHomeBinding);
        activityHomeBinding.setActivityHomeBinding(activityHomeModel);

        //初始化默认页面
        activityHomeBinding.flActivityHomePager.removeAllViews();
//        TextView tv = new TextView(this);
//        tv.setText("Text");
        activityHomeBinding.flActivityHomePager.addView(new HomeFreeTimePager().getRootView());
    }

//    public void getData(View v) {
//
//        BaseProtocol bp = new GetUserSkillLabelProtocol();
//        bp.getDataFromServer(new BaseProtocol.IResultExecutor() {
//            @Override
//            public void execute(Object dataBean) {
//                ToastUtils.shortToast("Success");
//            }
//
//            @Override
//            public void executeError(ResultErrorBean resultErrorBean) {
//                ToastUtils.shortToast(resultErrorBean.code + "\n" + resultErrorBean.data.message);
//            }
//        });
//    }
}
