package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewLocationCitySearchBinding;
import com.slash.youth.ui.viewmodel.ItemLocationCitySearchModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class LocationCitySearchListHolder extends BaseHolder<String>  {

    private ItemLocationCitySearchModel mItemLocationCitySearchModel;
    private ItemListviewLocationCitySearchBinding itemListviewLocationCitySearchBinding;

    @Override
    public View initView() {
        itemListviewLocationCitySearchBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_location_city_search, null, false);
        mItemLocationCitySearchModel = new ItemLocationCitySearchModel(itemListviewLocationCitySearchBinding);
        itemListviewLocationCitySearchBinding.setItemLocationCitySearchModel(mItemLocationCitySearchModel);
        return itemListviewLocationCitySearchBinding.getRoot();
    }

    @Override
    public void refreshView(String data) {
        mItemLocationCitySearchModel.setSearchCityName(data);
    }


}
