package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMyTaskBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.ui.adapter.MyTaskAdapter;

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
        initView();
    }

    ArrayList<MyTaskBean> listMyTask = new ArrayList<MyTaskBean>();

    private void initData() {
        getMyTaskList();
        mActivityMyTaskBinding.lvMyTaskList.setAdapter(new MyTaskAdapter(listMyTask));
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
