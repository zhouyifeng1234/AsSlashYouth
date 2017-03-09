package com.slash.youth.domain;

import java.util.List;

/**
 * Created by zss on 2016/9/4.
 */
public class SearchUserBean {
    private DataBean data;
    /**
     * data : {"list":[{"avatar":"http://p5.gexing.com/GSF/touxiang/20161004/1620/57f3665bd094a.jpg@!200x200_3?recache=20131108","company":"北京网易科技","id":10020,"identity":"产品设计师","isauth":1,"isfriend":0,"lasttime":1475651473000,"name":"刘静","profession":"产品经理","star":5}]}
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
         * avatar : http://p5.gexing.com/GSF/touxiang/20161004/1620/57f3665bd094a.jpg@!200x200_3?recache=20131108
         * company : 北京网易科技
         * id : 10020
         * identity : 产品设计师
         * isauth : 1
         * isfriend : 0
         * lasttime : 1475651473000
         * name : 刘静
         * profession : 产品经理
         * star : 5
         */

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
            private int id;
            private String identity;
            private int isauth;
            private int isfriend;
            private long lasttime;
            private String name;
            private String profession;
            private int star;

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

            public int getIsauth() {
                return isauth;
            }

            public void setIsauth(int isauth) {
                this.isauth = isauth;
            }

            public int getIsfriend() {
                return isfriend;
            }

            public void setIsfriend(int isfriend) {
                this.isfriend = isfriend;
            }

            public long getLasttime() {
                return lasttime;
            }

            public void setLasttime(long lasttime) {
                this.lasttime = lasttime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfession() {
                return profession;
            }

            public void setProfession(String profession) {
                this.profession = profession;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }
        }
    }

  /*  public DataBean data;
    public int rescode;

    class DataBean{
        ArrayList<UserBean> list;
    }

    class UserBean{
        String name;
        String avatar;
        String star;
        String isauth;
        String isfriend;
        String company;
        String profession;
        String lasttime;
    }*/

}
