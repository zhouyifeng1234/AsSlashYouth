package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.ActivityMySkillManageBinding;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyAddSkillActivity;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.adapter.ManagePublishAdapter;
import com.slash.youth.ui.adapter.MySkillManageAdapter;
import com.slash.youth.ui.holder.ManagePublishHolder;
import com.slash.youth.ui.view.NewRefreshListView;
import com.slash.youth.ui.view.RefreshListView;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/11/3.
 */
public class MySkillManageModel extends BaseObservable  {
    private ActivityMySkillManageBinding activityMySkillManageBinding;
    public  ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList = new ArrayList<>();
    private List<ManagerMyPublishTaskBean.DataBean.ListBean> list;
    private ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList = new ArrayList<>();//管理我发布的任务
    private MySkillManageAdapter mySkillManageAdapter;
    private MySkillManageActivity mySkillManageActivity;
    private String titleName;
    public ManagePublishAdapter managePublishAdapter;
    private int offset = 0;
    private int limit = 20;
    private int listsize;

    public MySkillManageModel(ActivityMySkillManageBinding activityMySkillManageBinding,MySkillManageActivity mySkillManageActivity,String titleName) {
        this.activityMySkillManageBinding = activityMySkillManageBinding;
        this.mySkillManageActivity = mySkillManageActivity;
        this.titleName = titleName;
        initData();

    }

    private void initData() {
        switch (titleName){
            case Constants.MY_TITLE_SKILL_MANAGER:
                getSkillMananagerDataFromServer(true);
                activityMySkillManageBinding.lvSkillManage.setNewRefreshDataTask(new RefreshDataTask());
                activityMySkillManageBinding.lvSkillManage.setNewLoadMoreNewsTast(new LoadMoreNewsTask());
                activityMySkillManageBinding.lvSkillManage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SkillManagerBean.DataBean.ListBean listBean = skillManageList.get(position);
                        int skillListId = listBean.getId();
                        mySkillManageActivity.jumpMyAddSkillActivity(mySkillManageActivity,skillListId);
                    }
                });
                break;

            case Constants.MY_TITLE_MANAGER_MY_PUBLISH:
                getManagerPublishDataFromServer(true);
                activityMySkillManageBinding.lvSkillManage.setNewRefreshDataTask(new RefreshDataTask());
                activityMySkillManageBinding.lvSkillManage.setNewLoadMoreNewsTast(new LoadMoreNewsTask());
                break;
        }
    }

    /**
     * 下拉刷新执行的回调，执行结束后需要调用refreshDataFinish()方法，用来更新状态
     */
    public class RefreshDataTask implements NewRefreshListView.NewIRefreshDataTask {
        @Override
        public void refresh() {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    switch (titleName) {
                        case Constants.MY_TITLE_SKILL_MANAGER:
                            getSkillMananagerDataFromServer(true);
                            mySkillManageAdapter.notifyDataSetChanged();
                           break;
                        case Constants.MY_TITLE_MANAGER_MY_PUBLISH:
                            getManagerPublishDataFromServer(true);
                            managePublishAdapter.notifyDataSetChanged();
                            break;
                    }
                    activityMySkillManageBinding.lvSkillManage.refreshDataFinish();
                }
            }, 2000);
        }
    }

    /**
     * 上拉加载更多执行的回调，执行完毕后需要调用loadMoreNewsFinished()方法，用来更新状态,如果加载到最后一页，则需要调用setLoadToLast()方法
     */
    public class LoadMoreNewsTask implements NewRefreshListView.NewILoadMoreNewsTask {
        @Override
        public void loadMore() {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (titleName) {
                        case Constants.MY_TITLE_SKILL_MANAGER:
                            getSkillMananagerDataFromServer(false);
                            mySkillManageAdapter.notifyDataSetChanged();
                            break;
                        case Constants.MY_TITLE_MANAGER_MY_PUBLISH:
                            getManagerPublishDataFromServer(false);
                            managePublishAdapter.notifyDataSetChanged();
                            break;
                    }
                    activityMySkillManageBinding.lvSkillManage.loadMoreNewsFinished();
                }
            }, 2000);
        }
    }

    /**
     * @param isRefresh true表示为下拉刷新或者第一次初始化加载数据操作，false表示加载更多数据
     */
    public void getManagerPublishDataFromServer(boolean isRefresh) {
        if (isRefresh) {
            managePublishList.clear();
            offset = 0;
        }
        MyManager.onManagerMyPublishTaskList(new onManagerMyPublishTaskList(),offset,limit);
    }

    //技能管理
    public void getSkillMananagerDataFromServer(boolean isRefresh) {
        if (isRefresh) {
            skillManageList.clear();
            offset = 0;
        }
        MyManager.onSkillManagerList(new onSkillManagerList(),offset,limit);
    }

    //管理我发布的任务
    public class onManagerMyPublishTaskList implements BaseProtocol.IResultExecutor<ManagerMyPublishTaskBean> {
        @Override
        public void execute(ManagerMyPublishTaskBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                ManagerMyPublishTaskBean.DataBean data = dataBean.getData();
                 list = data.getList();
                listsize = list.size();
                managePublishList.addAll(list);
                managePublishAdapter = new ManagePublishAdapter(managePublishList,mySkillManageActivity, managePublishList);
                activityMySkillManageBinding.lvSkillManage.setAdapter(managePublishAdapter);
            }
                //如果加载到最后一页，需要调用setLoadToLast()方法
                if(listsize < limit){//说明到最后一页啦
                    activityMySkillManageBinding.lvSkillManage.setLoadToLast();
                }else {//不是最后一页
                    offset += limit;
                }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //技能管理
    public class onSkillManagerList implements BaseProtocol.IResultExecutor<SkillManagerBean> {
        @Override
        public void execute(SkillManagerBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SkillManagerBean.DataBean data = dataBean.getData();
                List<SkillManagerBean.DataBean.ListBean> list = data.getList();
                listsize = list.size();
                skillManageList.addAll(list);
                mySkillManageAdapter = new MySkillManageAdapter(skillManageList,mySkillManageActivity,skillManageList);
                activityMySkillManageBinding.lvSkillManage.setAdapter(mySkillManageAdapter);
            }
            //如果加载到最后一页，需要调用setLoadToLast()方法
            if(listsize < limit){//说明到最后一页啦
                activityMySkillManageBinding.lvSkillManage.setLoadToLast();
            }else {//不是最后一页
                offset += limit;
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
