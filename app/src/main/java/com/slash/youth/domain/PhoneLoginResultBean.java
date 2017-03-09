package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/12.
 */
public class PhoneLoginResultBean {
    public int rescode;
    public Data data;

    public class Data {
        public String rongToken;
        public String token;
        public long uid;
    }
}
