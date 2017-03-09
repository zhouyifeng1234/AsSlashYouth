package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityInfo {
    //是否是城市名称的首字母，如果是的话，只需要取首字母，不需要城市名称
    public boolean isFirstLetter;

    public String firstLetter;

    public String CityName;

    private int id;

    public LocationCityInfo(String cityName, String firstLetter) {
        CityName = cityName;
        this.firstLetter = firstLetter;
    }

    public LocationCityInfo(boolean isFirstLetter, String firstLetter, String cityName,int id) {
        this.isFirstLetter = isFirstLetter;
        this.firstLetter = firstLetter;
        this.id = id;
        this.CityName = cityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public void setFirstLetter(boolean firstLetter) {
        isFirstLetter = firstLetter;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
