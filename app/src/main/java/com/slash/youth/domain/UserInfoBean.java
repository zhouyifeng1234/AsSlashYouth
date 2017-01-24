package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 用户个人信息实体类
 */
public class UserInfoBean {
    public int rescode;
    public Data data;

    public class Data {
        public UInfo uinfo;
    }

    public class UInfo {
        public int achievetaskcount;
        public String avatar;
        public double averageservicepoint;
        public int careertype;
        public String city;
        public String company;
        public String desc;
        public String direction;
        public int expert;
        public int fanscount;
        public double fansratio;
        public long id;
        public String identity;
        public String industry;
        public int isauth;
        public String name;
        public String position;
        public String province;
        public String tag;
        public String totoltaskcount;
        public String userservicepoint;
    }
}
