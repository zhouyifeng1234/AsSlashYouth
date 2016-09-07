package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class ItemLocationCityInfoModel extends BaseObservable {
    private String cityName;
    private int visible;

    @Bindable
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
    }

    @Bindable
    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
        notifyPropertyChanged(BR.visible);
    }
}
