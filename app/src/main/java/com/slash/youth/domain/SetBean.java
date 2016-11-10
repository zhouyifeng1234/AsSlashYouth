package com.slash.youth.domain;

/**
 * Created by zss on 2016/11/8.
 */
public class SetBean {


    /**
     * status : 1
     */

    public int rescode;

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
