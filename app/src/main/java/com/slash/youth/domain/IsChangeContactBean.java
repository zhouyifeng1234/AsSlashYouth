package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/22.
 */
public class IsChangeContactBean {
    public int rescode;
    public Data data;

    public class Data {
        public Data2 data;
    }

    public class Data2 {
        public int status;
        public long uid1;
        public String uid1phone;
        public long uid2;
        public String uid2phone;
    }
}
