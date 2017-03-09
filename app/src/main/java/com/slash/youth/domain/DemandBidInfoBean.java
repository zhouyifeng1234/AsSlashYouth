package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2017/2/15.
 */
public class DemandBidInfoBean {

    public int rescode;
    public Data data;

    public class Data {
        public Bidinfo bidinfo;
    }

    public class Bidinfo {
        public int bp;
        public String instalment;
        public double quote;
        public long starttime;
    }
}
