package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.ui.activity.CityLocationActivity;

/**
 * Created by zss on 2016/10/17.
 */
public class SearchActivityCityLocationModel extends BaseObservable {
    private SearchActivityCityLocationBinding searchActivityCityLocationBinding;

    public SearchActivityCityLocationModel(SearchActivityCityLocationBinding searchActivityCityLocationBinding) {
        this.searchActivityCityLocationBinding = searchActivityCityLocationBinding;
    }

    private int cityInfoListVisible = View.VISIBLE;
    private int searchCityListVisible = View.INVISIBLE;

    @Bindable
    public int getCityInfoListVisible() {
        return cityInfoListVisible;
    }

    public void setCityInfoListVisible(int cityInfoListVisible) {
        this.cityInfoListVisible = cityInfoListVisible;
        notifyPropertyChanged(BR.cityInfoListVisible);
    }

    @Bindable
    public int getSearchCityListVisible() {
        return searchCityListVisible;
    }

   public void setSearchCityListVisible(int searchCityListVisible) {
        this.searchCityListVisible = searchCityListVisible;
        notifyPropertyChanged(BR.searchCityListVisible);
    }

}
