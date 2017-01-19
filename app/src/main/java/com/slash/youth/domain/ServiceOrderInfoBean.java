package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class ServiceOrderInfoBean {
    public int rescode;
    public Data data;

    public class Data {
        public Order order;
    }

    public class Order {
        public int bp;
        public int channel;
        public String chid;
        public int chtype;
        public long cts;
        public long endtime;
        public long id;
        public int instalment;
        public int iscommit;
        public String orderid;
        public double quote;
        public String reason;
        public String reasondetail;
        public int refundamount;
        public String refunddesc;
        public String reid;
        public long sid;
        public long starttime;
        public int status;
        public long suid;
        public long uid;
        public long uts;
    }
}
