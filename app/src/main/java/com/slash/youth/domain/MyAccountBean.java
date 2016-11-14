package com.slash.youth.domain;

/**
 * Created by zss on 2016/11/10.
 */
public class MyAccountBean {


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
        private DataBean1 data;

        public DataBean1 getData() {
            return data;
        }

        public void setData(DataBean1 data) {
            this.data = data;
        }

        public static class DataBean1 {
            private int currentmoney;
            private int freezemoney;
            private int totalincome;
            private int totalmoney;
            private int totaloutlay;

            public int getCurrentmoney() {
                return currentmoney;
            }

            public void setCurrentmoney(int currentmoney) {
                this.currentmoney = currentmoney;
            }

            public int getFreezemoney() {
                return freezemoney;
            }

            public void setFreezemoney(int freezemoney) {
                this.freezemoney = freezemoney;
            }

            public int getTotalincome() {
                return totalincome;
            }

            public void setTotalincome(int totalincome) {
                this.totalincome = totalincome;
            }

            public int getTotalmoney() {
                return totalmoney;
            }

            public void setTotalmoney(int totalmoney) {
                this.totalmoney = totalmoney;
            }

            public int getTotaloutlay() {
                return totaloutlay;
            }

            public void setTotaloutlay(int totaloutlay) {
                this.totaloutlay = totaloutlay;
            }
        }
    }
}
