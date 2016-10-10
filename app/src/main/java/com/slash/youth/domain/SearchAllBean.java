package com.slash.youth.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/9/4.
 */
public class SearchAllBean {
    private DataBean data;
    /**
     * data : {"demandList":[{"avatar":"http://xxxxxxxxx","endtime":1484093980000,"id":22,"isauth":0,"isinstallment":1,"name":"李旭","pattern":0,"place":"五道口清华科技园","quote":1000,"star":4,"starttime":1483993980000,"title":"刘静让我在线聊天","type":1,"uid":10003},{"avatar":"http://xxxxxxxxx","endtime":1484093980000,"id":21,"isauth":1,"isinstallment":1,"name":"李旭","pattern":0,"place":"五道口清华科技园","quote":1000,"star":4,"starttime":1483993980000,"title":"刘静让我在线聊天","type":1,"uid":10002},{"avatar":"http://xxxxxxxxx","endtime":1484093980000,"id":23,"isauth":0,"isinstallment":1,"name":"李旭","pattern":1,"place":"五道口清华科技园","quote":1000,"star":4,"starttime":1483993980000,"title":"刘静让我马上搬家","type":1,"uid":10003},{"avatar":"http://xxxxxxxxx","endtime":1484093980000,"id":20,"isauth":1,"isinstallment":1,"name":"李旭","pattern":1,"place":"五道口清华科技园","quote":1000,"star":4,"starttime":1483993980000,"title":"刘静让我去找人给她搬家","type":1,"uid":10002}],"serviceList":[],"userList":[{"avatar":"http://p5.gexing.com/GSF/touxiang/20161004/1620/57f3665bd094a.jpg@!200x200_3?recache=20131108","company":"北京网易科技","id":10020,"identity":"产品设计师","isauth":1,"isfriend":0,"lasttime":1475651473000,"name":"刘静","profession":"产品经理","star":5}]}
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
         * avatar : http://xxxxxxxxx
         * endtime : 1484093980000
         * id : 22
         * isauth : 0
         * isinstallment : 1
         * name : 李旭
         * pattern : 0
         * place : 五道口清华科技园
         * quote : 1000
         * star : 4
         * starttime : 1483993980000
         * title : 刘静让我在线聊天
         * type : 1
         * uid : 10003
         */

        private ArrayList<DemandListBean> demandList;
        private ArrayList<?> serviceList;
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

        private ArrayList<UserListBean> userList;

        public ArrayList<DemandListBean> getDemandList() {
            return demandList;
        }

        public void setDemandList(ArrayList<DemandListBean> demandList) {
            this.demandList = demandList;
        }

        public ArrayList<?> getServiceList() {
            return serviceList;
        }

        public void setServiceList(ArrayList<?> serviceList) {
            this.serviceList = serviceList;
        }

        public ArrayList<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(ArrayList<UserListBean> userList) {
            this.userList = userList;
        }

        public static class DemandListBean {
            private String avatar;
            private long endtime;
            private int id;
            private int isauth;
            private int isinstallment;
            private String name;
            private int pattern;
            private String place;
            private int quote;
            private int star;
            private long starttime;
            private String title;
            private int type;
            private int uid;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public long getEndtime() {
                return endtime;
            }

            public void setEndtime(long endtime) {
                this.endtime = endtime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsauth() {
                return isauth;
            }

            public void setIsauth(int isauth) {
                this.isauth = isauth;
            }

            public int getIsinstallment() {
                return isinstallment;
            }

            public void setIsinstallment(int isinstallment) {
                this.isinstallment = isinstallment;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPattern() {
                return pattern;
            }

            public void setPattern(int pattern) {
                this.pattern = pattern;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public int getQuote() {
                return quote;
            }

            public void setQuote(int quote) {
                this.quote = quote;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public long getStarttime() {
                return starttime;
            }

            public void setStarttime(long starttime) {
                this.starttime = starttime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }

        public static class UserListBean {
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

    /*public DataBean data;
    public int rescode;

    class DataBean{
        ArrayList<TitleBean> demandlist;
        ArrayList<TitleBean> servicelist;
        ArrayList<NameBean> userlist;
    }
    class TitleBean{
        String title;
    }
    class NameBean{
        String name;
    }*/






}
