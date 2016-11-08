package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMyCollectionBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.ui.adapter.MyCollectionAdapter;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionModel extends BaseObservable {

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


    }


}
