package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewLocationCityInfoBinding;
import com.slash.youth.domain.CityClassBean;
import com.slash.youth.domain.ListCityBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.viewmodel.ItemLocationCityInfoModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityInfoHolder extends BaseHolder<LocationCityInfo> {


    private ItemLocationCityInfoModel mItemLocationCityInfo;
    private ItemListviewLocationCityInfoBinding itemListviewLocationCityInfoBinding;

    @Override
    public View initView() {
        itemListviewLocationCityInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_location_city_info, null, false);
        mItemLocationCityInfo = new ItemLocationCityInfoModel();
        itemListviewLocationCityInfoBinding.setItemLocationCityInfo(mItemLocationCityInfo);
        return itemListviewLocationCityInfoBinding.getRoot();
    }

    //R.drawable.selector_search_result_line_item_bg
    @Override
    public void refreshView(LocationCityInfo data) {
        if (data.isFirstLetter) {
            mItemLocationCityInfo.setCityName(data.firstLetter);
            mItemLocationCityInfo.setVisible(View.GONE);
        } else {
            mItemLocationCityInfo.setCityName(data.CityName);
            mItemLocationCityInfo.setVisible(View.VISIBLE);
        }

    }
}
