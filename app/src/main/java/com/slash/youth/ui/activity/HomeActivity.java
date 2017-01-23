package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.ui.viewmodel.PagerHomeMyModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

public class HomeActivity extends Activity {

    public static final int PAGE_FREETIME = 0;//首页闲时
    public static final int PAGE_INFO = 1;//首页消息
    public static final int PAGE_CONTACTS = 2;//首页人脉
    public static final int PAGE_MY = 3;//首页我的

    public static int currentCheckedPageNo;
    public static BaseHomePager currentCheckedPager;

    private ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        ActivityHomeModel activityHomeModel = new ActivityHomeModel(activityHomeBinding, this);
        activityHomeBinding.setActivityHomeBinding(activityHomeModel);

        //初始化默认页面
        activityHomeBinding.flActivityHomePager.removeAllViews();
//        TextView tv = new TextView(this);
//        tv.setText("Text");
        currentCheckedPager = new HomeFreeTimePager(this);
        activityHomeBinding.flActivityHomePager.addView(currentCheckedPager.getRootView());
        currentCheckedPageNo = PAGE_FREETIME;
        activityHomeBinding.tvFreeTime.setTextColor(Color.parseColor("#31c5e4"));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserInfoEngine.MY_USER_EDITOR) {
            activityHomeBinding.flActivityHomePager.removeAllViews();
            activityHomeBinding.flActivityHomePager.addView(new HomeMyPager(this).getRootView());
        }
    }

}
