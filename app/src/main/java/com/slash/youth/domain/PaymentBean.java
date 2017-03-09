package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/11/3.
 */
public class PaymentBean {
    public int rescode;
    public Data data;

    public class Data {
        public int status;
        public String charge;
    }
}
