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
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

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

    public MyCollectionModel(ActivityMyCollectionBinding activityMyCollectionBinding, MyCollectionActivity myCollectionActivity) {
        this.activityMyCollectionBinding = activityMyCollectionBinding;
        this.myCollectionActivity = myCollectionActivity;
        initData();
        listener();
    }

    private void listener() {
        activityMyCollectionBinding.lvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        getManagerPublishDataFromServer(true);
        activityMyCollectionBinding.lvCollection.setNewRefreshDataTask(new RefreshDataTask());
        activityMyCollectionBinding.lvCollection.setNewLoadMoreNewsTast(new LoadMoreNewsTask());
    }

    /**
     * @param isRefresh true表示为下拉刷新或者第一次初始化加载数据操作，false表示加载更多数据
     */
    public void getManagerPublishDataFromServer(boolean isRefresh) {
        if (isRefresh) {
            collectionList.clear();
            offset = 0;
        }
        MyManager.getMyCollectionList(new onGetMyCollectionList(),offset,limit);
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
                    getManagerPublishDataFromServer(true);
                    myCollectionAdapter.notifyDataSetChanged();
                    activityMyCollectionBinding.lvCollection.refreshDataFinish();
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
                    getManagerPublishDataFromServer(false);
                    myCollectionAdapter.notifyDataSetChanged();
                    activityMyCollectionBinding.lvCollection.loadMoreNewsFinished();
                }
            }, 2000);
        }
    }

    //获取我的收藏的列表
    public class onGetMyCollectionList implements BaseProtocol.IResultExecutor<MyCollectionBean> {
        @Override
        public void execute(MyCollectionBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                MyCollectionBean.DataBean data = dataBean.getData();
                List<MyCollectionBean.DataBean.ListBean> list = data.getList();
                int size = list.size();

                collectionList.addAll(list);
                myCollectionAdapter = new MyCollectionAdapter(collectionList,myCollectionActivity);
                activityMyCollectionBinding.lvCollection.setAdapter(myCollectionAdapter);

                //如果加载到最后一页，需要调用setLoadToLast()方法
                if(size < limit){//说明到最后一页啦
                    activityMyCollectionBinding.lvCollection.setLoadToLast();
                }else {//不是最后一页
                    offset += limit;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
        }
    }

    //添加标签
    public  void addMyCollection(int type,long tid){
        MyManager.addMyCollectionList(new onAddMyCollectionList(),type,tid);
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
