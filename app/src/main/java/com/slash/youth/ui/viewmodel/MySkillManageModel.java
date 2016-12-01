package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.ActivityMySkillManageBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.adapter.ManagePublishAdapter;
import com.slash.youth.ui.adapter.MyCollectionAdapter;
import com.slash.youth.ui.adapter.MySkillManageAdapter;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/11/3.
 */
public class MySkillManageModel extends BaseObservable  {
    private ActivityMySkillManageBinding activityMySkillManageBinding;
    private  ArrayList<SkillManageBean> skillManageList = new ArrayList<>();
    private  ArrayList<MyCollectionBean> managePublishList = new ArrayList<>();
    private MySkillManageAdapter mySkillManageAdapter;
    private MySkillManageActivity mySkillManageActivity;
    private String titleName;
    private ManagePublishAdapter managePublishAdapter;

    public MySkillManageModel(ActivityMySkillManageBinding activityMySkillManageBinding,MySkillManageActivity mySkillManageActivity,String titleName) {
        this.activityMySkillManageBinding = activityMySkillManageBinding;
        this.mySkillManageActivity = mySkillManageActivity;
        this.titleName = titleName;
        initData();
    }


    private void initData() {

        switch (titleName){

            case "技能管理":
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());
                skillManageList.add(new SkillManageBean());

                //MyManager.getMyCollectionList(new onGetMyCollectionList(),0,20, GlobalConstants.HttpUrl.SKILL_MANAGE_LIST);
                mySkillManageAdapter = new MySkillManageAdapter(skillManageList,mySkillManageActivity,skillManageList);
                activityMySkillManageBinding.lvSkillManage.setAdapter(mySkillManageAdapter);
                mySkillManageAdapter.setItemRemoveListener(new MySkillManageAdapter.onItemRemoveListener() {
                    @Override
                    public void onItemRemove(int index) {
                        skillManageList.remove(index);
                        mySkillManageAdapter.notifyDataSetChanged();
                    }
                });

                break;
            case "管理我发布的任务":
                managePublishList.add(new MyCollectionBean());
                managePublishList.add(new MyCollectionBean());
                managePublishList.add(new MyCollectionBean());
                managePublishList.add(new MyCollectionBean());
                managePublishList.add(new MyCollectionBean());
                managePublishList.add(new MyCollectionBean());

               // MyManager.getMyCollectionList(new onGetMyCollectionList(),0,20, GlobalConstants.HttpUrl.MANAGE_PUBLISH_LIST);
                managePublishAdapter = new ManagePublishAdapter(managePublishList,mySkillManageActivity,managePublishList);
                activityMySkillManageBinding.lvSkillManage.setAdapter(managePublishAdapter);
                managePublishAdapter.setItemRemoveListener(new ManagePublishAdapter.onItemRemoveListener() {
                    @Override
                    public void onItemRemove(int index) {
                        managePublishList.remove(index);
                        managePublishAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    public class onGetMyCollectionList implements BaseProtocol.IResultExecutor<MyCollectionBean> {
        @Override
        public void execute(MyCollectionBean dataBean) {
            int rescode = dataBean.getRescode();
            MyCollectionBean.DataBean data = dataBean.getData();
            List<MyCollectionBean.DataBean.ListBean> list = data.getList();
            for (MyCollectionBean.DataBean.ListBean listBean : list) {
                String avatar = listBean.getAvatar();
                long cts = listBean.getCts();//收藏时间
                int instalment = listBean.getInstalment();//1表示支持分期 0表示不支持分期
                int isAuth = listBean.getIsAuth();//0表示未认证，1表示已经认证
                String name = listBean.getName();
                int quote = listBean.getQuote();//0表示对方报价
                long starttime = listBean.getStarttime();//任务开始时间 0表示未设置
                int status = listBean.getStatus();  //1表示可以下架（当前在架上），0表示可以上架（当前不在架上）
                int tid = listBean.getTid(); //需求或者服务ID
                String title = listBean.getTitle();//需求或者服务标题
                int type = listBean.getType();//1需求 2服务
                int uid = listBean.getUid();
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }


    //删除  item
    public void delete(int type,int tid,String url){
        MyManager.addMyCollectionList(new onAddMyCollectionList(),type,tid,url);

    }

    public class onAddMyCollectionList implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            SetBean.DataBean data = dataBean.getData();
            int status = data.getStatus();
            LogKit.d("status:"+status);
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }



}
