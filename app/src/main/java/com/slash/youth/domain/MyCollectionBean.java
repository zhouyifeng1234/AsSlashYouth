package com.slash.youth.domain;

import java.util.List;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionBean {


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
            public int anonymity;
            private String avatar;
            private long cts;
            private int instalment;
            private int isAuth;
            private String name;
            private int quote;
            private long starttime;
            private int status;
            private long tid;
            private String title;
            private int type;
            private long uid;
            private int quoteunit;
            private int timetype;

            public int getTimetype() {
                return timetype;
            }

            public void setTimetype(int timetype) {
                this.timetype = timetype;
            }

            public int getQuoteunit() {
                return quoteunit;
            }

            public void setQuoteunit(int quoteunit) {
                this.quoteunit = quoteunit;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public long getCts() {
                return cts;
            }

            public void setCts(long cts) {
                this.cts = cts;
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

            public int getQuote() {
                return quote;
            }

            public void setQuote(int quote) {
                this.quote = quote;
            }

            public long getStarttime() {
                return starttime;
            }

            public void setStarttime(long starttime) {
                this.starttime = starttime;
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
