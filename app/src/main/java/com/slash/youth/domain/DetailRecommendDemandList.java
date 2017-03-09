package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/1/4.
 */
public class DetailRecommendDemandList {
    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<RecommendDemandInfo> list;
    }

    public class RecommendDemandInfo {
        public int anonymity;
        public String avatar;
        public long id;
        public int instalment;
        public int isauth;
        public double lat;
        public double lng;
        public String name;
        public int pattern;//1线下 0线上
        public String place;
        public double quote;
        public long starttime;
        public String title;
        public long uid;
    }
}
