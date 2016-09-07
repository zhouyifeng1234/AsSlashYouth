package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class ItemLocationCitySearchModel extends BaseObservable {

    private String searchCityName;

    @Bindable
    public String getSearchCityName() {
        return searchCityName;
    }

    public void setSearchCityName(String searchCityName) {
        this.searchCityName = searchCityName;
        notifyPropertyChanged(BR.searchCityName);
    }
}
