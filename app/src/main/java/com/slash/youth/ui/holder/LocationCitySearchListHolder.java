package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewLocationCitySearchBinding;
import com.slash.youth.ui.viewmodel.ItemLocationCitySearchModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class LocationCitySearchListHolder extends BaseHolder<String> {

    private ItemLocationCitySearchModel mItemLocationCitySearchModel;

    @Override
    public View initView() {
        ItemListviewLocationCitySearchBinding itemListviewLocationCitySearchBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_location_city_search, null, false);
        mItemLocationCitySearchModel = new ItemLocationCitySearchModel();
        itemListviewLocationCitySearchBinding.setItemLocationCitySearchModel(mItemLocationCitySearchModel);
        return itemListviewLocationCitySearchBinding.getRoot();
    }

    @Override
    public void refreshView(String data) {
        mItemLocationCitySearchModel.setSearchCityName(data);
    }
}
