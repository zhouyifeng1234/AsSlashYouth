package com.slash.youth.domain;

import java.util.List;

/**
 * Created by zss on 2016/11/10.
 */
public class TransactionRecoreBean {


    private DataBean data;
    /**
     * data : {"list":[{"amount":13000,"cts":1478747923717,"fromuid":10002,"id":12,"orderid":"A9E97618-14DB-4930-9936-6CB0AF22C0F6","remark":"需求付款支出","tid":185,"title":"","touid":10002,"ttype":1,"type":2},{"amount":950,"cts":1478511539795,"fromuid":10002,"id":8,"orderid":"A2D73510-1C34-4E7B-8DBA-4C7897BAC2E1","remark":"需求完成收入","tid":173,"title":"","touid":10002,"ttype":1,"type":1}]}
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
         * amount : 13000
         * cts : 1478747923717
         * fromuid : 10002
         * id : 12
         * orderid : A9E97618-14DB-4930-9936-6CB0AF22C0F6
         * remark : 需求付款支出
         * tid : 185
         * title :
         * touid : 10002
         * ttype : 1
         * type : 2
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int amount;
            private long cts;
            private int fromuid;
            private int id;
            private String orderid;
            private String remark;
            private int tid;
            private String title;
            private int touid;
            private int ttype;
            private int type;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public long getCts() {
                return cts;
            }

            public void setCts(long cts) {
                this.cts = cts;
            }

            public int getFromuid() {
                return fromuid;
            }

            public void setFromuid(int fromuid) {
                this.fromuid = fromuid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getTouid() {
                return touid;
            }

            public void setTouid(int touid) {
                this.touid = touid;
            }

            public int getTtype() {
                return ttype;
            }

            public void setTtype(int ttype) {
                this.ttype = ttype;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
