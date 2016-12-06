package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class ServiceOrderStatusBean {
    public int rescode;
    public Data data;

    public class Data {
        public Service service;
    }

    public class Service {
        public int status;
    }
}
