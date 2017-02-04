package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.sip.SipSession;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMySkillManageBinding;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.MyAddSkillActivity;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.adapter.ManagePublishAdapter;
import com.slash.youth.ui.adapter.MySkillManageAdapter;
import com.slash.youth.ui.holder.ManagePublishHolder;
import com.slash.youth.ui.view.NewRefreshListView;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.ui.view.RefreshListView;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

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
    private String avater;
    private String name;

    public MySkillManageModel(ActivityMySkillManageBinding activityMySkillManageBinding,MySkillManageActivity mySkillManageActivity,String titleName,String avater,String name) {
        this.activityMySkillManageBinding = activityMySkillManageBinding;
        this.mySkillManageActivity = mySkillManageActivity;
        this.titleName = titleName;
        this.avater = avater;
        this.name = name;
        initListView();
        initData();
        listener();
    }
    //加载listView
    private void initListView() {
        skillManageList.clear();
        PullToRefreshLayout ptrl = activityMySkillManageBinding.refreshView;
        ptrl.setOnRefreshListener(new MySkillManageListListener());
    }

    public void  getdata( String titleName) {
        switch (titleName){
            case Constants.MY_TITLE_SKILL_MANAGER:
                MyManager.onSkillManagerList(new onSkillManagerList(),offset,limit);
                break;
            case Constants.MY_TITLE_MANAGER_MY_PUBLISH:
                MyManager.onManagerMyPublishTaskList(new onManagerMyPublishTaskList(),offset,limit);
                break;
        }
    }

    public class MySkillManageListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    skillManageList.clear();
                    managePublishList.clear();
                    getdata(titleName);
                    if(managePublishAdapter!=null){
                        managePublishAdapter.notifyDataSetChanged();
                    }
                    if(mySkillManageAdapter!=null){
                        mySkillManageAdapter.notifyDataSetChanged();
                    }
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 2000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //如果加载到最后一页，需要调用setLoadToLast()方法
                    if(listsize < limit){//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        getdata(titleName);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if(managePublishAdapter!=null){
                        managePublishAdapter.notifyDataSetChanged();
                    }
                    if(mySkillManageAdapter!=null){
                        mySkillManageAdapter.notifyDataSetChanged();
                    }
                }
            }, 2000);
        }
    }

    private void listener() {

        switch (titleName){
            case Constants.MY_TITLE_SKILL_MANAGER:
                activityMySkillManageBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SkillManagerBean.DataBean.ListBean listBean = skillManageList.get(position);
                        long skillListId = listBean.getId();
                        Intent intentMyAddSkillActivity = new Intent(CommonUtils.getContext(), MyAddSkillActivity.class);
                        intentMyAddSkillActivity.putExtra("skillListId",skillListId);
                        intentMyAddSkillActivity.putExtra("position",position);
                        intentMyAddSkillActivity.putExtra("skillTemplteType", Constants.UPDATE_SKILL_MANAGER_ONE);
                        mySkillManageActivity.startActivityForResult(intentMyAddSkillActivity, Constants.UPDATE_SKILL_MANAGER_ONE);
                    }
                });
                break;
            case Constants.MY_TITLE_MANAGER_MY_PUBLISH:
                activityMySkillManageBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ManagerMyPublishTaskBean.DataBean.ListBean publishData = managePublishList.get(position);
                        int type = publishData.getType();
                        switch (type){
                            case 1:
                                long demandId  = publishData.getTid();
                                Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                                intentDemandDetailActivity.putExtra("demandId", demandId);
                                mySkillManageActivity.startActivity(intentDemandDetailActivity);
                                break;
                            case 2:
                                long serviceId = publishData.getTid();
                                Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                                intentServiceDetailActivity.putExtra("serviceId", serviceId);
                                mySkillManageActivity.startActivity(intentServiceDetailActivity);
                                break;
                        }
                    }
                });
                break;
        }
    }

    private void initData() {
        getdata(titleName);
    }

    //管理我发布的任务(我的发布)
    public class onManagerMyPublishTaskList implements BaseProtocol.IResultExecutor<ManagerMyPublishTaskBean> {
        @Override
        public void execute(ManagerMyPublishTaskBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                ManagerMyPublishTaskBean.DataBean data = dataBean.getData();
                 list = data.getList();
                listsize = list.size();
                if(list.size() == 0){
                    activityMySkillManageBinding.tvTitle.setVisibility(View.VISIBLE);
                    activityMySkillManageBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    activityMySkillManageBinding.tvTitle.setText("暂无任务");
                }else {
                    managePublishList.addAll(list);
                    managePublishAdapter = new ManagePublishAdapter(managePublishList, mySkillManageActivity, managePublishList);
                    activityMySkillManageBinding.lv.setAdapter(managePublishAdapter);
                    activityMySkillManageBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }
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
                if(listsize == 0){
                    activityMySkillManageBinding.tvTitle.setVisibility(View.VISIBLE);
                    activityMySkillManageBinding.tvTitle.setText("暂无技能");
                    activityMySkillManageBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                }else {
                    skillManageList.addAll(list);
                    mySkillManageAdapter = new MySkillManageAdapter(skillManageList,mySkillManageActivity,skillManageList,avater,name);
                    activityMySkillManageBinding.lv.setAdapter(mySkillManageAdapter);
                    activityMySkillManageBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
