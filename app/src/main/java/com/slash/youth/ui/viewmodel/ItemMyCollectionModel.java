package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemMyCollectionBinding;
import com.slash.youth.databinding.ItemMySkillManageBinding;

/**
 * Created by zss on 2016/11/3.
 */
public class ItemMyCollectionModel extends BaseObservable {

    private ItemMyCollectionBinding itemMyCollectionBinding;
    public ItemMyCollectionModel(ItemMyCollectionBinding itemMyCollectionBinding) {
        this.itemMyCollectionBinding = itemMyCollectionBinding;

       // listener();
    }



}
