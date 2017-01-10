package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class NearLocationBean {
    public String name;
    public String address;
    public String distance;
    public double lat;
    public double lng;

    public NearLocationBean(String name, String address, String distance, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }
}
