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

      /*  public void setData(DataBean data) {
            this.data = data;
        }*/

        public static class DataBean1 {
            private double currentmoney;
            private double freezemoney;
            private double totalincome;
            private double totalmoney;
            private double totaloutlay;

            public double getCurrentmoney() {
                return currentmoney;
            }

            public void setCurrentmoney(double currentmoney) {
                this.currentmoney = currentmoney;
            }

            public double getFreezemoney() {
                return freezemoney;
            }

            public void setFreezemoney(double freezemoney) {
                this.freezemoney = freezemoney;
            }

            public double getTotalincome() {
                return totalincome;
            }

            public void setTotalincome(double totalincome) {
                this.totalincome = totalincome;
            }

            public double getTotalmoney() {
                return totalmoney;
            }

            public void setTotalmoney(int totalmoney) {
                this.totalmoney = totalmoney;
            }

            public double getTotaloutlay() {
                return totaloutlay;
            }

            public void setTotaloutlay(int totaloutlay) {
                this.totaloutlay = totaloutlay;
            }
        }
    }


}
