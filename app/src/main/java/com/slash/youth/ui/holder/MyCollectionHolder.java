package com.slash.youth.ui.holder;

import android.content.pm.ProviderInfo;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyCollectionBinding;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.ui.viewmodel.ItemMyCollectionModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionHolder extends BaseHolder<MyCollectionBean> {

    public ItemMyCollectionBinding itemMyCollectionBinding;
    private int position;
    private ArrayList<MyCollectionBean> listData;
    private MyCollectionBean mdata;

    public MyCollectionHolder(int position,ArrayList<MyCollectionBean> listData) {
        this.position = position;
        this.listData = listData;
    }

    @Override
    public View initView() {
        itemMyCollectionBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_collection, null, false);
        ItemMyCollectionModel itemMyCollectionModel = new ItemMyCollectionModel(itemMyCollectionBinding,position,listData);
        itemMyCollectionBinding.setItemMyCollectionModel(itemMyCollectionModel);
        return itemMyCollectionBinding.getRoot();
    }

    @Override
    public void refreshView(MyCollectionBean data) {


    }






}
