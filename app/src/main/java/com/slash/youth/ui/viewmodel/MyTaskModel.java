package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.ActivityMyTaskBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.ui.activity.DemandChooseServiceActivity;
import com.slash.youth.ui.activity.MyBidDemandActivity;
import com.slash.youth.ui.activity.MyPublishDemandActivity;
import com.slash.youth.ui.adapter.MyTaskAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskModel extends BaseObservable {

    ActivityMyTaskBinding mActivityMyTaskBinding;
    Activity mActivity;

    public MyTaskModel(ActivityMyTaskBinding activityMyTaskBinding, Activity activity) {
        this.mActivityMyTaskBinding = activityMyTaskBinding;
        this.mActivity = activity;
        initData();
        initListener();
        initView();
    }

    ArrayList<MyTaskBean> listMyTask = new ArrayList<MyTaskBean>();

    private void initData() {
        getMyTaskList();
        mActivityMyTaskBinding.lvMyTaskList.setAdapter(new MyTaskAdapter(listMyTask));
    }

    private void initListener() {
        //为了方便测试，设置Item的点击事件，实际需要做各种判断
        mActivityMyTaskBinding.lvMyTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intentMyBidDemandActivity = new Intent(CommonUtils.getContext(), MyBidDemandActivity.class);
                    mActivity.startActivity(intentMyBidDemandActivity);
                } else if (position == 2) {
                    Intent intentMyPublishDemandActivity = new Intent(CommonUtils.getContext(), MyPublishDemandActivity.class);
                    mActivity.startActivity(intentMyPublishDemandActivity);
                } else {
                    Intent intentDemandChooseServiceActivity = new Intent(CommonUtils.getContext(), DemandChooseServiceActivity.class);
                    mActivity.startActivity(intentDemandChooseServiceActivity);
                }
            }
        });
    }

    private void initView() {

    }

    public void getMyTaskList() {
        //模拟数据，实际由服务端 返回
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
    }
}
