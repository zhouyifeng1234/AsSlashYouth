package com.slash.youth.domain;

import java.io.Serializable;

/**
 * Created by zss on 2016/10/12.
 */
public class UserInfoItemBean  {
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
            private String avatar;
            private int careertype;
            private String city;
            private String company;
            private String desc;
            private String direction;
            private int expert;
            private int id;
            private String identity;
            private String industry;
            private int isauth;
            private String name;
            private String position;
            private String province;
            private String tag;

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
        }
    }


   /* *//**
     * uinfo : {"achievetaskcount":0,"avatar":"","averageservicepoint":0,"careertype":1,"city":"北京","company":"腾讯","direction":"客户端","expert":2,"fanscount":0,"fansratio":0,"id":10002,"identity":"","industry":"互联网","isauth":1,"name":"小赵","position":"技术专家","province":"北京","tag":"android ios","totoltaskcount":0,"userservicepoint":0}
     *//*

    private DataBean data;
    *//**
     * data : {"uinfo":{"achievetaskcount":0,"avatar":"","averageservicepoint":0,"careertype":1,"city":"北京","company":"腾讯","direction":"客户端","expert":2,"fanscount":0,"fansratio":0,"id":10002,"identity":"","industry":"互联网","isauth":1,"name":"小赵","position":"技术专家","province":"北京","tag":"android ios","totoltaskcount":0,"userservicepoint":0}}
     * rescode : 0
     *//*

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

    public static class DataBean  {
        *//**
         * achievetaskcount : 0
         * avatar :
         * averageservicepoint : 0
         * careertype : 1
         * city : 北京
         * company : 腾讯
         * direction : 客户端
         * expert : 2
         * fanscount : 0
         * fansratio : 0
         * id : 10002
         * identity :
         * industry : 互联网
         * isauth : 1
         * name : 小赵
         * position : 技术专家
         * province : 北京
         * tag : android ios
         * totoltaskcount : 0
         * userservicepoint : 0
         *//*

        private UinfoBean uinfo;

        public UinfoBean getUinfo() {
            return uinfo;
        }

        public void setUinfo(UinfoBean uinfo) {
            this.uinfo = uinfo;
        }

        public static class UinfoBean implements Serializable{
            private int achievetaskcount;
            private String avatar;
            private int averageservicepoint;
            private int careertype;
            private String city;
            private String company;
            private String direction;
            private int expert;
            private int fanscount;
            private int fansratio;
            private int id;
            private String identity;
            private String industry;
            private int isauth;
            private String name;
            private String position;
            private String province;
            private String tag;
            private int totoltaskcount;
            private int userservicepoint;
            private String desc;

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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getAverageservicepoint() {
                return averageservicepoint;
            }

            public void setAverageservicepoint(int averageservicepoint) {
                this.averageservicepoint = averageservicepoint;
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

            public int getExpert() {
                return expert;
            }

            public void setExpert(int expert) {
                this.expert = expert;
            }

            public int getFanscount() {
                return fanscount;
            }

            public void setFanscount(int fanscount) {
                this.fanscount = fanscount;
            }

            public int getFansratio() {
                return fansratio;
            }

            public void setFansratio(int fansratio) {
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

            public int getTotoltaskcount() {
                return totoltaskcount;
            }

            public void setTotoltaskcount(int totoltaskcount) {
                this.totoltaskcount = totoltaskcount;
            }

            public int getUserservicepoint() {
                return userservicepoint;
            }

            public void setUserservicepoint(int userservicepoint) {
                this.userservicepoint = userservicepoint;
            }
        }
    }*/

}
