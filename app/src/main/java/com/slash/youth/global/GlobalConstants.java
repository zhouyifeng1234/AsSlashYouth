package com.slash.youth.global;

/**
 * Created by zhouyifeng on 2016/8/31.
 */
public class GlobalConstants {
    public static final String SP_NAME = "slash_sp.config";

    public static class SpConfigKey {
        // public static final String IS_GUID = "isGuid";
        // public static final String READ_NEWS_ID = "readNewsId";
    }

    public static class HttpUrl {

//                一、测试机器地址和登陆方式
//                IP                  NAME                    密码/ssh
//                121.42.145.178      slash-youth-office-01
//
//                114.215.83.138      slash-youth-office-02
//
//                120.27.95.99        slash-youth-office-03



//                服务HTTP端口和RPC端口汇总
//
//                服务名称              HTTP        RPC        备注
//                slash.youth.auth	    8300
//                slash.youth.pay	    8200	    6200
//                slash.youth.ucenter	8100	    6100


//                    二、Mysql账号密码
//                    H:120.27.95.99
//                    U:slashyouth
//                    P:da#@I*O(



        public static final String SERVER_HOST = "http://10.0.2.2:8080/";
        // http://localhost:8080/zhbj/categories.json
        // public static final String NEWS_CENTER_URL = SERVER_HOST
        // + "/categories.json";
        // http://localhost:8080/zhbj/10007/list_1.json
        // http://localhost:8080/zhbj/10007/724D6A55496A11726628.html
    }
}
