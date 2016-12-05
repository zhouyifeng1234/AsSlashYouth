package com.slash.youth.domain;

import java.io.Serializable;

/**
 * Created by zhouyifeng on 2016/11/13.
 */
public class DemandDetailBean implements Serializable {

    public int rescode;
    public Data data;

    public class Data implements Serializable {
        public Demand demand;
    }

    public class Demand implements Serializable {
        public int anonymity;
        public int bp;
        public int consume;
        public long cts;
        public String desc;
        public long fighttime;
        public long id;
        public int instalment;
        public int invoice;
        public int iscomment;
        public int isonline;
        public double lat;
        public double lng;
        public int offer;
        public int pattern;
        public String pic;
        public String place;
        public String placedetail;
        public double price;
        public double quote;
        public String remark;
        public long starttime;
        public int status;
        public int suid;
        public String tag;
        public String title;
        public long uid;
        public long uts;
    }
}
