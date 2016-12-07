package com.slash.youth.domain;

/**
 * Created by acer on 2016/11/29.
 */
public class FansBean {


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
        private int fans;
        private int follow;

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }
    }
}
