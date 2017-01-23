package com.slash.youth.domain;

import java.io.Serializable;

/**
 * Created by acer on 2016/11/29.
 */
public class OtherInfoBean {


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
        private UinfoBean uinfo;

        public UinfoBean getUinfo() {
            return uinfo;
        }

        public void setUinfo(UinfoBean uinfo) {
            this.uinfo = uinfo;
        }

        public static class UinfoBean implements Serializable{
            private long achievetaskcount;
            private String avatar;
            private double averageservicepoint;
            private int careertype;
            private String city;
            private String company;
            private String desc;
            private String direction;
            private int expert;
            private int fanscount;
            private double fansratio;
            private int id;
            private String identity;
            private String industry;
            private int isauth;
            private String name;
            private String position;
            private String province;
            private String tag;
            private long totoltaskcount;
            private double userservicepoint;
            private int relationshipscount;


            public int getRelationshipscount() {
                return relationshipscount;
            }

            public void setRelationshipscount(int relationshipscount) {
                this.relationshipscount = relationshipscount;
            }



            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public int getExpert() {
                return expert;
            }

            public void setExpert(int expert) {
                this.expert = expert;
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

            public long getAchievetaskcount() {
                return achievetaskcount;
            }

            public void setAchievetaskcount(long achievetaskcount) {
                this.achievetaskcount = achievetaskcount;
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

            public double getFansratio() {
                return fansratio;
            }

            public void setFansratio(double fansratio) {
                this.fansratio = fansratio;
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
        }
    }
}
