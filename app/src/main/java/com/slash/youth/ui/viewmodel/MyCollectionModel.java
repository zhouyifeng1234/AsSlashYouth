package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.sip.SipSession;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyCollectionBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.ManagePublishAdapter;
import com.slash.youth.ui.adapter.MyCollectionAdapter;
import com.slash.youth.ui.view.NewRefreshListView;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionModel extends BaseObservable  {
    private ActivityMyCollectionBinding activityMyCollectionBinding;
    private   ArrayList<MyCollectionBean.DataBean.ListBean> collectionList = new ArrayList<>();
    private MyCollectionAdapter myCollectionAdapter;
    private MyCollectionActivity myCollectionActivity;
    private  int offset = 0;
    private int limit = 20;
    private int listSize;

    public MyCollectionModel(ActivityMyCollectionBinding activityMyCollectionBinding, MyCollectionActivity myCollectionActivity) {
        this.activityMyCollectionBinding = activityMyCollectionBinding;
        this.myCollectionActivity = myCollectionActivity;
        initListView();
        initData();
        listener();
    }

    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = activityMyCollectionBinding.refreshView;
        ptrl.setOnRefreshListener(new MyCollectionListListener());
    }

    public class MyCollectionListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    collectionList.clear();
                    MyManager.getMyCollectionList(new onGetMyCollectionList(),offset,limit);
                    if(myCollectionAdapter!=null){
                        myCollectionAdapter.notifyDataSetChanged();
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
                    if(listSize < limit){//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        MyManager.getMyCollectionList(new onGetMyCollectionList(),offset,limit);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if(myCollectionAdapter!=null){
                        myCollectionAdapter.notifyDataSetChanged();
                    }
                }
            }, 2000);
        }
    }

    private void listener() {
        activityMyCollectionBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_COLLECT_CLICK_DETAIL);

                MyCollectionBean.DataBean.ListBean listBean = collectionList.get(position);
                int type = listBean.getType();//1需求 2服务
                switch (type){
                    case 1:
                        long demandId = listBean.getTid(); //需求或者服务ID
                        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                        intentDemandDetailActivity.putExtra("demandId", demandId);
                        myCollectionActivity.startActivity(intentDemandDetailActivity);
                        break;
                    case 2:
                        long serviceId = listBean.getTid();
                        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                        intentServiceDetailActivity.putExtra("serviceId", serviceId);
                        myCollectionActivity.startActivity(intentServiceDetailActivity);
                        break;
                }
            }
        });
    }

    //加载数据
    private void initData() {
        MyManager.getMyCollectionList(new onGetMyCollectionList(),offset,limit);
    }

    //获取我的收藏的列表
    public class onGetMyCollectionList implements BaseProtocol.IResultExecutor<MyCollectionBean> {
        @Override
        public void execute(MyCollectionBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                MyCollectionBean.DataBean data = dataBean.getData();
                List<MyCollectionBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                if(listSize == 0){
                    activityMyCollectionBinding.tvContent.setVisibility(View.VISIBLE);
                    activityMyCollectionBinding.rlCollectionDefaultImage.setVisibility(View.VISIBLE);
                }else {
                    collectionList.addAll(list);
                    myCollectionAdapter = new MyCollectionAdapter(collectionList,myCollectionActivity);
                    activityMyCollectionBinding.lv.setAdapter(myCollectionAdapter);
                    activityMyCollectionBinding.rlCollectionDefaultImage.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
        }
    }

    /*//添加标签
    public  void addMyCollection(int type,long tid){
        MyManager.addMyCollectionList(new onAddMyCollectionList(),type,tid);
    }*/

   /* public class onAddMyCollectionList implements BaseProtocol.IResultExecutor<SetBean> {
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
    }*/
}
