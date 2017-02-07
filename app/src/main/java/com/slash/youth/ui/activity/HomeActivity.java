package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeInfoPager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.utils.CommonUtils;

public class HomeActivity extends Activity {
    public static final int PAGE_FREETIME = 0;//首页闲时
    public static final int PAGE_INFO = 1;//首页消息
    public static final int PAGE_CONTACTS = 2;//首页人脉
    public static final int PAGE_MY = 3;//首页我的

    public static int currentCheckedPageNo;
    public static BaseHomePager currentCheckedPager;

    private ActivityHomeBinding activityHomeBinding;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        if (currentCheckedPager instanceof HomeInfoPager) {
            HomeInfoPager homeInfoPager = (HomeInfoPager) currentCheckedPager;
            homeInfoPager.mPagerHomeInfoModel.getDataFromServer();
        }
        if (requestCode == UserInfoEngine.MY_USER_EDITOR) {
            activityHomeBinding.flActivityHomePager.removeAllViews();
            activityHomeBinding.flActivityHomePager.addView(new HomeMyPager(this).getRootView());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.slash.youth.ui.activity/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.slash.youth.ui.activity/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}
