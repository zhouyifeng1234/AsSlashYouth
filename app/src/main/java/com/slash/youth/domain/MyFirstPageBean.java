package com.slash.youth.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zss on 2016/11/9. //我的模块首页Bean
 */
public class MyFirstPageBean {
    private DataBean data;
    private int rescode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getRescode() {
        return rescode;
    }

    public void setRescode(int rescode) {
        this.rescode = rescode;
    }

    public static class DataBean {
        private MyinfoBean myinfo;

        public MyinfoBean getMyinfo() {
            return myinfo;
        }

        public void setMyinfo(MyinfoBean myinfo) {
            this.myinfo = myinfo;
        }

        public static class MyinfoBean implements Serializable{
            private int achievetaskcount;
            private double amount;
            private String avatar;
            private double averageservicepoint;
            private String desc;
            private int careertype;
            private String city;
            private String company;
            private String direction;
            private int expertlevel;
            private double expertratio;
            private int expertscore;
            private int fanscount;
            private double fansratio;
            private int id;
            private String identity;
            private String industry;
            private int isauth;
            private String name;
            private String phone;
            private String position;
            private String province;
            private String tag;
            private long totoltaskcount;
            private double userservicepoint;
            private List<Integer> expertlevels;


            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getAchievetaskcount() {
                return achievetaskcount;
            }

            public void setAchievetaskcount(int achievetaskcount) {
                this.achievetaskcount = achievetaskcount;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public double getAverageservicepoint() {
                return averageservicepoint;
            }

            public void setAverageservicepoint(double averageservicepoint) {
                this.averageservicepoint = averageservicepoint;
            }

            public int getFanscount() {
                return fanscount;
            }

            public void setFanscount(int fanscount) {
                this.fanscount = fanscount;
            }

            public int getCareertype() {
                return careertype;
            }

            public void setCareertype(int careertype) {
                this.careertype = careertype;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public int getExpertlevel() {
                return expertlevel;
            }

            public void setExpertlevel(int expertlevel) {
                this.expertlevel = expertlevel;
            }

            public double getExpertratio() {
                return expertratio;
            }

            public void setExpertratio(double expertratio) {
                this.expertratio = expertratio;
            }

            public int getExpertscore() {
                return expertscore;
            }

            public void setExpertscore(int expertscore) {
                this.expertscore = expertscore;
            }


            public double getFansratio() {
                return fansratio;
            }

            public void setFansratio(double fansratio) {
                this.fansratio = fansratio;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIdentity() {
                return identity;
            }

            public void setIdentity(String identity) {
                this.identity = identity;
            }

            public String getIndustry() {
                return industry;
            }

            public void setIndustry(String industry) {
                this.industry = industry;
            }

            public int getIsauth() {
                return isauth;
            }

            public void setIsauth(int isauth) {
                this.isauth = isauth;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public long getTotoltaskcount() {
                return totoltaskcount;
            }

            public void setTotoltaskcount(long totoltaskcount) {
                this.totoltaskcount = totoltaskcount;
            }

            public double getUserservicepoint() {
                return userservicepoint;
            }

            public void setUserservicepoint(double userservicepoint) {
                this.userservicepoint = userservicepoint;
            }

            public List<Integer> getExpertlevels() {
                return expertlevels;
            }

            public void setExpertlevels(List<Integer> expertlevels) {
                this.expertlevels = expertlevels;
            }
        }
    }


}
