package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemMyCollectionBinding;
import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class ItemMyCollectionModel extends BaseObservable {

    private ItemMyCollectionBinding itemMyCollectionBinding;
    private ArrayList<MyCollectionBean.DataBean.ListBean> listData;
    public ItemMyCollectionModel(ItemMyCollectionBinding itemMyCollectionBinding, ArrayList<MyCollectionBean.DataBean.ListBean> listData) {
        this.itemMyCollectionBinding = itemMyCollectionBinding;
        this.listData = listData;

    }







}
