package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/18.
 */
public class RecommendDemandUserBean {
    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<DemandUserInfo> list;
    }

    public class DemandUserInfo {
        public String avatar;
        public String company;
        public String direction;
        public String industry;
        public int isauth;
        public String name;
        public String position;
        public long uid;
    }
}
