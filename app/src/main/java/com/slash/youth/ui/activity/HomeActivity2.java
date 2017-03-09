package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHome2Binding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.activity.base.BusActivity;
import com.slash.youth.ui.event.MessageEvent;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeWorkbenchPager;
import com.slash.youth.ui.viewmodel.ActivityHomeModel2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.slash.youth.ui.activity.HomeActivity.activityHomeModel;
import static org.xutils.http.HttpMethod.HEAD;

/**
 * V1.1版本改版的HomeActivity
 * <p/>
 * Created by zhouyifeng on 2017/2/27.
 */

public class HomeActivity2 extends BusActivity {
    public static final int REQUEST_CODE_TO_TASK_DETAIL = 10000;


    ActivityHome2Binding mActivityHome2Binding;
    ActivityHomeModel2 mActivityHomeModel2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityHome2Binding = DataBindingUtil.setContentView(this, R.layout.activity_home2);
        mActivityHomeModel2 = new ActivityHomeModel2(mActivityHome2Binding, this);
        mActivityHome2Binding.setActivityHomeModel2(mActivityHomeModel2);

    }

    public ViewPager getInnerViewPager() {
        return mActivityHome2Binding.vpHomePager;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        mActivityHomeModel2.updateMessage();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_TO_TASK_DETAIL == requestCode) {
            //如果当前显示的是任务Pager，就刷新任务列表
            BaseHomePager currentPager = mActivityHomeModel2.getCurrentPager();
            if (currentPager != null) {
                if (currentPager instanceof HomeWorkbenchPager) {
                    HomeWorkbenchPager homeWorkbenchPager = (HomeWorkbenchPager) currentPager;
                    homeWorkbenchPager.mPagerHomeWorkbenchModel.backRefreshTaskListStatus();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        MsgManager.exit();
        super.onBackPressed();
    }
}
