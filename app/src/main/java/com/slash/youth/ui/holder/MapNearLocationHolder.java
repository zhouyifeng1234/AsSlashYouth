package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

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
    private ItemListviewNearLocationBinding mItemListviewNearLocationBinding;

    public static ImageView ivPoiChecked;

    @Override
    public View initView() {
        mItemListviewNearLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_near_location, null, false);
        mItemNearLocationModel = new ItemNearLocationModel(mItemListviewNearLocationBinding);
        mItemListviewNearLocationBinding.setItemNearLocationModel(mItemNearLocationModel);
        return mItemListviewNearLocationBinding.getRoot();
    }

    @Override
    public void refreshView(NearLocationBean data) {
        mItemNearLocationModel.setNearName(data.name);
        mItemNearLocationModel.setNearAddress(data.address);
        mItemNearLocationModel.setNearDistance(data.distance);
        if (getCurrentPosition() == 0) {
            mItemListviewNearLocationBinding.ivPoiChecked.setImageResource(R.mipmap.jihuo_icon);
            ivPoiChecked = mItemListviewNearLocationBinding.ivPoiChecked;
        } else {
            mItemListviewNearLocationBinding.ivPoiChecked.setImageResource(R.mipmap.no_jihuo_poi_icon);
        }
    }
}
