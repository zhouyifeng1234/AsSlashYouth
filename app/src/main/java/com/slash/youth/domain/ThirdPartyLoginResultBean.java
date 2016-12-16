package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/13.
 */
public class ThirdPartyLoginResultBean {
    public int rescode;
    public Data data;

    public class Data {
        public String token;//如果已经登录过的，这里的token是自己服务器上的token，如果是第一次登录，这里的是3ptoken
        public long uid;
    }
}
