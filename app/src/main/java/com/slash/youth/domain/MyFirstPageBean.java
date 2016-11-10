package com.slash.youth.domain;

import java.util.List;

/**
 * Created by zss on 2016/11/9. //我的模块首页Bean
 */
public class MyFirstPageBean {


    /**
     * myinfo : {"achievetaskcount":0,"amount":102400,"avatar":"","averageservicepoint":0,"careertype":1,"city":"北京","company":"腾讯","direction":"客户端","expert":2,"expertlevel":0,"expertlevels":[],"expertscore":0,"fanscount":0,"fansratio":0,"id":10002,"identity":"","industry":"互联网","isauth":1,"name":"小赵","position":"技术专家","province":"北京","tag":"android ios","totoltaskcount":0,"userservicepoint":0}
     */

    private DataBean data;
    /**
     * data : {"myinfo":{"achievetaskcount":0,"amount":102400,"avatar":"","averageservicepoint":0,"careertype":1,"city":"北京","company":"腾讯","direction":"客户端","expert":2,"expertlevel":0,"expertlevels":[],"expertscore":0,"fanscount":0,"fansratio":0,"id":10002,"identity":"","industry":"互联网","isauth":1,"name":"小赵","position":"技术专家","province":"北京","tag":"android ios","totoltaskcount":0,"userservicepoint":0}}
     * rescode : 0
     */

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
        /**
         * achievetaskcount : 0
         * amount : 102400
         * avatar :
         * averageservicepoint : 0
         * careertype : 1
         * city : 北京
         * company : 腾讯
         * direction : 客户端
         * expert : 2
         * expertlevel : 0
         * expertlevels : []
         * expertscore : 0
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
         */

        private MyinfoBean myinfo;

        public MyinfoBean getMyinfo() {
            return myinfo;
        }

        public void setMyinfo(MyinfoBean myinfo) {
            this.myinfo = myinfo;
        }

        public static class MyinfoBean {
            private int achievetaskcount;
            private int amount;
            private String avatar;
            private int averageservicepoint;
            private int careertype;
            private String city;
            private String company;
            private String direction;
            private int expert;
            private int expertlevel;
            private int expertscore;
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
            private List<?> expertlevels;

            public int getAchievetaskcount() {
                return achievetaskcount;
            }

            public void setAchievetaskcount(int achievetaskcount) {
                this.achievetaskcount = achievetaskcount;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
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

            public int getExpertlevel() {
                return expertlevel;
            }

            public void setExpertlevel(int expertlevel) {
                this.expertlevel = expertlevel;
            }

            public int getExpertscore() {
                return expertscore;
            }

            public void setExpertscore(int expertscore) {
                this.expertscore = expertscore;
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

            public List<?> getExpertlevels() {
                return expertlevels;
            }

            public void setExpertlevels(List<?> expertlevels) {
                this.expertlevels = expertlevels;
            }
        }
    }
}
