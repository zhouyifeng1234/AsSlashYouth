package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class NearLocationBean {
    public String name;
    public String address;
    public String distance;

    public NearLocationBean(String name,String address, String distance ) {
        this.address = address;
        this.distance = distance;
        this.name = name;
    }
}
