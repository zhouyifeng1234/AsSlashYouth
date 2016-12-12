package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/12.
 */
public class TokenLoginResultBean {

    public int rescode;
    public Data data;

    public class Data {
        public String token;
        //rongToken和uid不确定服务端是否会提供
        public String rongToken;
        public long uid;
    }

}
