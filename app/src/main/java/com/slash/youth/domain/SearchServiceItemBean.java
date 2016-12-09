package com.slash.youth.domain;

import java.util.List;

/**
 * Created by zss on 2016/12/7.
 */
public class SearchServiceItemBean {

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
            private String city;
            private int cts;
            private int endtime;
            private int id;
            private int instalment;
            private int isauth;
            private double lat;
            private double lng;
            private String location;
            private String name;
            private int pattern;
            private int quote;
            private int quoteunit;
            private int starttime;
            private int timetype;
            private String title;
            private int uid;
            private double userservicepoint;
            private int uts;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getCts() {
                return cts;
            }

            public void setCts(int cts) {
                this.cts = cts;
            }

            public int getEndtime() {
                return endtime;
            }

            public void setEndtime(int endtime) {
                this.endtime = endtime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getInstalment() {
                return instalment;
            }

            public void setInstalment(int instalment) {
                this.instalment = instalment;
            }

            public int getIsauth() {
                return isauth;
            }

            public void setIsauth(int isauth) {
                this.isauth = isauth;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
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

            public int getQuote() {
                return quote;
            }

            public void setQuote(int quote) {
                this.quote = quote;
            }

            public int getQuoteunit() {
                return quoteunit;
            }

            public void setQuoteunit(int quoteunit) {
                this.quoteunit = quoteunit;
            }

            public int getStarttime() {
                return starttime;
            }

            public void setStarttime(int starttime) {
                this.starttime = starttime;
            }

            public int getTimetype() {
                return timetype;
            }

            public void setTimetype(int timetype) {
                this.timetype = timetype;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public double getUserservicepoint() {
                return userservicepoint;
            }

            public void setUserservicepoint(double userservicepoint) {
                this.userservicepoint = userservicepoint;
            }

            public int getUts() {
                return uts;
            }

            public void setUts(int uts) {
                this.uts = uts;
            }
        }
    }
}
