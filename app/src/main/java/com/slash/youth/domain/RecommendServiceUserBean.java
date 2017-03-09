package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/18.
 */
public class RecommendServiceUserBean {

    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<ServiceUserInfo> list;
    }

    public class ServiceUserInfo {
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
