package com.slash.youth.domain;

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

        public static class UinfoBean {
            private int achievetaskcount;
            private String avatar;
            private int averageservicepoint;
            private int careertype;
            private String city;
            private String company;
            private String desc;
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
            private double userservicepoint;

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

            public double getUserservicepoint() {
                return userservicepoint;
            }

            public void setUserservicepoint(double userservicepoint) {
                this.userservicepoint = userservicepoint;
            }
        }
    }
}
