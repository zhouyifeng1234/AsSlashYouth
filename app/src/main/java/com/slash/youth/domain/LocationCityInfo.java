package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityInfo {
    //是否是城市名称的首字母，如果是的话，只需要取首字母，不需要城市名称
    public boolean isFirstLetter;

    public String firstLetter;

    public String CityName;

    public LocationCityInfo(boolean isFirstLetter, String firstLetter, String cityName) {
        this.isFirstLetter = isFirstLetter;
        this.firstLetter = firstLetter;
        CityName = cityName;
    }

    public String getCityName() {
        return CityName;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public boolean isFirstLetter() {
        return isFirstLetter;
    }
}
