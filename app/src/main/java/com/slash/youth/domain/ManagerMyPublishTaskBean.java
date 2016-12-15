package com.slash.youth.domain;

import java.util.List;

/**
 * Created by zss on 2016/11/30.
 */
public class ManagerMyPublishTaskBean {


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
            private int cts;
            private long endtime;
            private long id;
            private int instalment;
            private int isAuth;
            private String name;
            private double quote;
            private int quoteUnit;
            private long starttime;
            private int status;
            private long tid;
            private int timetype;
            private String title;
            private int type;
            private long uid;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getCts() {
                return cts;
            }

            public void setCts(int cts) {
                this.cts = cts;
            }

            public long getEndtime() {
                return endtime;
            }

            public void setEndtime(long endtime) {
                this.endtime = endtime;
            }

            public long getStarttime() {
                return starttime;
            }

            public void setStarttime(long starttime) {
                this.starttime = starttime;
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

            public int getIsAuth() {
                return isAuth;
            }

            public void setIsAuth(int isAuth) {
                this.isAuth = isAuth;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getQuote() {
                return quote;
            }

            public void setQuote(double quote) {
                this.quote = quote;
            }

            public int getQuoteUnit() {
                return quoteUnit;
            }

            public void setQuoteUnit(int quoteUnit) {
                this.quoteUnit = quoteUnit;
            }



            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getTid() {
                return tid;
            }

            public void setTid(long tid) {
                this.tid = tid;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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
