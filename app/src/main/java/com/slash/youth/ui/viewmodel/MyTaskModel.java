package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMyTaskBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.ui.activity.DemandChooseServiceActivity;
import com.slash.youth.ui.activity.MyBidDemandActivity;
import com.slash.youth.ui.activity.MyPublishDemandActivity;
import com.slash.youth.ui.adapter.MyTaskAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

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
        //首次进入页面，加载我的全部任务
        setTotalTaskData();
    }

    private void setTotalTaskData() {
        setMyTaskTypeText("全部任务");
        getMyTotalTaskList();
        if (listMyTask.size() > 0) {
            setMyTaskListVisibility(View.VISIBLE);
            setNoTaskVisibility(View.GONE);
            mActivityMyTaskBinding.lvMyTaskList.setAdapter(new MyTaskAdapter(listMyTask));
        } else {
            setMyTaskListVisibility(View.GONE);
            setNoTaskVisibility(View.VISIBLE);
        }
    }

    private void setMyPublishTaskData() {
        setMyTaskTypeText("发布的任务");
        getMyPublishTaskList();
        mActivityMyTaskBinding.lvMyTaskList.setAdapter(new MyTaskAdapter(listMyTask));
    }

    private void setMyBidTaskData() {
        setMyTaskTypeText("抢到的任务");
        getMyBidTaskList();
        mActivityMyTaskBinding.lvMyTaskList.setAdapter(new MyTaskAdapter(listMyTask));
    }

    private void setMyHistoryTaskData() {
        setMyTaskTypeText("历史任务");
        getMyHistoryTaskList();
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


    /**
     * 获取我全部任务
     */
    public void getMyTotalTaskList() {
        //模拟数据，实际由服务端 返回
        listMyTask.clear();
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

    /**
     * 获取我发布的任务
     */
    public void getMyPublishTaskList() {
        //模拟数据，实际由服务端 返回
        listMyTask.clear();
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
    }

    /**
     * 获取我抢的任务
     */
    public void getMyBidTaskList() {
        //模拟数据，实际由服务端 返回
        listMyTask.clear();
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
    }

    /**
     * 获取我的历史任务
     */
    public void getMyHistoryTaskList() {
        //模拟数据，实际由服务端 返回
        listMyTask.clear();
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
        listMyTask.add(new MyTaskBean());
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    //打开或关闭筛选任务的下拉框
    public void openFilterTask(View v) {
        if (openTaskVisibility == View.GONE) {
            setOpenTaskVisibility(View.VISIBLE);
        } else {
            setOpenTaskVisibility(View.GONE);
        }
    }

    //去发布任务
    public void gotoPublishTask(View v) {
//        ToastUtils.shortToast("去发布任务");
        setPublishTaskDialogVisibility(View.VISIBLE);
    }

    //去浏览任务
    public void gotoBrowseTask(View v) {
        ToastUtils.shortToast("去浏览任务");
    }

    //关闭发布任务对话框
    public void closePublishTaskDialog(View v) {
        setPublishTaskDialogVisibility(View.GONE);
    }

    //发布需求
    public void publishDemand(View v) {
        ToastUtils.shortToast("发布需求");
    }

    //发布服务
    public void publishService(View v) {
        ToastUtils.shortToast("发布服务");
    }

    //筛选全部任务
    public void filterMyTotalTask(View v) {
        setTotalTaskData();
        setOpenTaskVisibility(View.GONE);
    }

    //筛选我发布的任务
    public void filterMyPublishTask(View v) {
        setMyPublishTaskData();
        setOpenTaskVisibility(View.GONE);
    }

    //筛选我抢的任务
    public void filterMyBidTask(View v) {
        setMyBidTaskData();
        setOpenTaskVisibility(View.GONE);
    }

    //筛选我的历史任务（下架的或者过期的）
    public void filterMyHistoryTask(View v) {
        setMyHistoryTaskData();
        setOpenTaskVisibility(View.GONE);
    }

    private int openTaskVisibility = View.GONE;
    private int myTaskListVisibility = View.GONE;
    private int noTaskVisibility = View.GONE;
    private int publishTaskDialogVisibility = View.GONE;
    private String myTaskTypeText = "全部任务";

    @Bindable
    public int getOpenTaskVisibility() {
        return openTaskVisibility;
    }

    public void setOpenTaskVisibility(int openTaskVisibility) {
        this.openTaskVisibility = openTaskVisibility;
        notifyPropertyChanged(BR.openTaskVisibility);
    }

    @Bindable
    public int getNoTaskVisibility() {
        return noTaskVisibility;
    }

    public void setNoTaskVisibility(int noTaskVisibility) {
        this.noTaskVisibility = noTaskVisibility;
        notifyPropertyChanged(BR.noTaskVisibility);
    }

    @Bindable
    public int getMyTaskListVisibility() {
        return myTaskListVisibility;
    }

    public void setMyTaskListVisibility(int myTaskListVisibility) {
        this.myTaskListVisibility = myTaskListVisibility;
        notifyPropertyChanged(BR.myTaskListVisibility);
    }

    @Bindable
    public int getPublishTaskDialogVisibility() {
        return publishTaskDialogVisibility;
    }

    public void setPublishTaskDialogVisibility(int publishTaskDialogVisibility) {
        this.publishTaskDialogVisibility = publishTaskDialogVisibility;
        notifyPropertyChanged(BR.publishTaskDialogVisibility);
    }

    @Bindable
    public String getMyTaskTypeText() {
        return myTaskTypeText;
    }

    public void setMyTaskTypeText(String myTaskTypeText) {
        this.myTaskTypeText = myTaskTypeText;
        notifyPropertyChanged(BR.myTaskTypeText);
    }
}
