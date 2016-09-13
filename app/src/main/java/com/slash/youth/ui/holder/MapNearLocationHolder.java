package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewNearLocationBinding;
import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.ui.viewmodel.ItemNearLocationModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class MapNearLocationHolder extends BaseHolder<NearLocationBean> {

    private ItemNearLocationModel mItemNearLocationModel;

    @Override
    public View initView() {
        ItemListviewNearLocationBinding itemListviewNearLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_near_location, null, false);
        mItemNearLocationModel = new ItemNearLocationModel(itemListviewNearLocationBinding);
        itemListviewNearLocationBinding.setItemNearLocationModel(mItemNearLocationModel);
        return itemListviewNearLocationBinding.getRoot();
    }

    @Override
    public void refreshView(NearLocationBean data) {
        mItemNearLocationModel.setNearName(data.name);
        mItemNearLocationModel.setNearAddress(data.address);
        mItemNearLocationModel.setNearDistance(data.distance);
    }
}
