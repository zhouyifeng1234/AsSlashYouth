package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * V1.1版首页需求服务推荐列表实体
 * <p/>
 * Created by zhouyifeng on 2017/3/2.
 */
public class HomeRecommendList2 {

    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<RecommendInfo> radlist;//补全的
        public ArrayList<RecommendInfo> reclist;//精准的
    }

    public class RecommendInfo {
        public int anonymity;
        public String avatar;
        public long endtime;
        public long id;
        public int instalment;
        public int isauth;
        public double lat;
        public double lng;
        public String name;
        public int pattern;
        public String place;
        public double quote;
        public int quoteunit;
        public long starttime;
        public int timetype;
        public String title;
        public long uid;
    }
}
