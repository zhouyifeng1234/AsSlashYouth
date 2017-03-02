package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/3/1.
 */
public class TagRecommendList {

    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<TagRecommendInfo> list;
    }

    public class TagRecommendInfo {
        public int anonymity;
        public String avatar;
        public String city;
        public long cts;
        public long endtime;
        public long id;
        public int instalment;
        public int isauth;
        public int isonline;
        public double lat;
        public double lng;
        public String location;
        public String name;
        public int pattern;
        public String place;
        public double quote;
        public int quoteunit;
        public long starttime;
        public String tag;
        public String tagorg1;
        public String tagorg2;
        public int timetype;
        public String title;
        public int type;
        public long uid;
        public long uts;
    }
}
