package com.slash.youth.domain;

/**
 * Created by zss on 2016/11/14.
 */
public class ListCityBean {
    public String firstLetter;
    public String CityName;
    public int id;

    public ListCityBean(String firstLetter, String cityName,int id) {
        this.firstLetter = firstLetter;
        this.CityName = cityName;
        this.id = id;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }
}
