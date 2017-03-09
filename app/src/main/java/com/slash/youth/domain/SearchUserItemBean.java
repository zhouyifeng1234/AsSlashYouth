package com.slash.youth.domain;

import java.util.List;

/**
 * Created by acer on 2016/12/7.
 */
public class SearchUserItemBean {


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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String avatar;
            private String company;
            private int expertscore;
            private int isauth;
            private String name;
            private String namesplit;
            private String position;
            private String tag;
            private long uid;
            private String direction;
            private String identity;
            private int careertype;

            public int getCareertype() {
                return careertype;
            }

            public void setCareertype(int careertype) {
                this.careertype = careertype;
            }

            public String getIdentity() {
                return identity;
            }

            public void setIdentity(String identity) {
                this.identity = identity;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public int getExpertscore() {
                return expertscore;
            }

            public void setExpertscore(int expertscore) {
                this.expertscore = expertscore;
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

            public String getNamesplit() {
                return namesplit;
            }

            public void setNamesplit(String namesplit) {
                this.namesplit = namesplit;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public long getUid() {
                return uid;
            }

            public void setUid(long uid) {
                this.uid = uid;
            }
        }
    }
}
