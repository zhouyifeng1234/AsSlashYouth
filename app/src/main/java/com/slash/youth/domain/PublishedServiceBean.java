package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishedServiceBean {

    public String title;
    public String price;
    public String type;
    public int buyCount;

    public PublishedServiceBean(String title, String price, String type, int buyCount) {
        this.title = title;
        this.price = price;
        this.type = type;
        this.buyCount = buyCount;
    }
}
