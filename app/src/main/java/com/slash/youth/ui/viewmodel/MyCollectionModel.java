package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.ActivityMyCollectionBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.adapter.MyCollectionAdapter;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionModel extends BaseObservable  {

    private ActivityMyCollectionBinding activityMyCollectionBinding;
    private   ArrayList<MyCollectionBean> collectionList = new ArrayList<>();
    private MyCollectionAdapter myCollectionAdapter;

    public MyCollectionModel(ActivityMyCollectionBinding activityMyCollectionBinding) {
        this.activityMyCollectionBinding = activityMyCollectionBinding;
        initData();
        initView();

    }

    //加载数据
    private void initData() {
        int offset = 0;
      //  MyManager.getMyCollectionList(new onGetMyCollectionList(),offset,20, GlobalConstants.HttpUrl.MY_COLLECTION_LIST);

        collectionList.add(new MyCollectionBean());
        collectionList.add(new MyCollectionBean());
        collectionList.add(new MyCollectionBean());
        collectionList.add(new MyCollectionBean());
        collectionList.add(new MyCollectionBean());
        collectionList.add(new MyCollectionBean());


    }

    //加载视图
    private void initView() {
        myCollectionAdapter = new MyCollectionAdapter(collectionList);
         activityMyCollectionBinding.lvCollection.setAdapter(myCollectionAdapter);
        myCollectionAdapter.setItemRemoveListener(new MyCollectionAdapter.onItemRemoveListener() {
            @Override
            public void onItemRemove(int index) {
                collectionList.remove(index);
                myCollectionAdapter.notifyDataSetChanged();

            }
        });
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
                int status = listBean.getStatus();  //1表示可以预约，0表示不可以
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


    //添加标签
    public  void addMyCollection(int type,int tid){
        MyManager.addMyCollectionList(new onAddMyCollectionList(),type,tid,GlobalConstants.HttpUrl.MY_ADD_COLLECTION_ITEM);
    }


    //删除标签
    public  void deleteMyCollection(int type,int tid){
        MyManager.addMyCollectionList(new onAddMyCollectionList(),type,tid,GlobalConstants.HttpUrl.MY_DELETE_COLLECTION_ITEM);
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
