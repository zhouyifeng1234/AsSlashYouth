package com.slash.youth.domain.bean;

import java.util.List;

/**
 * Created by acer on 2017/3/7.
 */

public class FindServices {
    private List<ListBean> radlist;
    private List<ListBean> reclist;

    public List<ListBean> getRadlist() {
        return radlist;
    }

    public void setRadlist(List<ListBean> radlist) {
        this.radlist = radlist;
    }

    public List<ListBean> getReclist() {
        return reclist;
    }

    public void setReclist(List<ListBean> reclist) {
        this.reclist = reclist;
    }

    public static class ListBean {
        public boolean isReclist;
        public boolean isInsertRadHint;
        private int anonymity;
        private String avatar;
        private long endtime;
        private long id;
        private int instalment;
        private int isauth;
        private double lat;
        private double lng;
        private String name;
        private int pattern;
        private String place;
        private double quote;
        private int quoteunit;
        private long starttime;
        private int timetype;
        private String title;
        private long uid;

        public int getAnonymity() {
            return anonymity;
        }

        public void setAnonymity(int anonymity) {
            this.anonymity = anonymity;
        }

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

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public double getQuote() {
            return quote;
        }

        public void setQuote(double quote) {
            this.quote = quote;
        }

        public int getQuoteunit() {
            return quoteunit;
        }

        public void setQuoteunit(int quoteunit) {
            this.quoteunit = quoteunit;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
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

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }
    }
}