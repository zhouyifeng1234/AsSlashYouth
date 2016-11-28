package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemListviewLocationCitySearchBinding;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class ItemLocationCitySearchModel extends BaseObservable {

    private String searchCityName;
    private ItemListviewLocationCitySearchBinding itemListviewLocationCitySearchBinding;

    public ItemLocationCitySearchModel(ItemListviewLocationCitySearchBinding itemListviewLocationCitySearchBinding) {
        this.itemListviewLocationCitySearchBinding = itemListviewLocationCitySearchBinding;
    }

    @Bindable
    public String getSearchCityName() {
        return searchCityName;
    }

    public void setSearchCityName(String searchCityName) {
        this.searchCityName = searchCityName;
        notifyPropertyChanged(BR.searchCityName);
    }




}
