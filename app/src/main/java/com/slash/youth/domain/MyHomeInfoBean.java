package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/22.
 * 十三、[用戶信息]-我的首页数据 （可以获得手机号）
 */
public class MyHomeInfoBean {
    public int rescode;
    public Data data;

    public class Data {
        public MyInfo myinfo;
    }

    public class MyInfo {
        public int achievetaskcount;
        public double amount;
        public String avatar;
        public double averageservicepoint;
        public int careertype;
        public String city;
        public String company;
        public String direction;
        public int expertlevel;
        public ArrayList<Integer> expertlevels;
        public double expertratio;
        public int expertscore;
        public int fanscount;
        public double fansratio;
        public long id;
        public String identity;
        public String industry;
        public int isauth;
        public String name;
        public String phone;
        public String position;
        public String province;
        public String tag;
        public int totoltaskcount;
        public double userservicepoint;
    }
}
