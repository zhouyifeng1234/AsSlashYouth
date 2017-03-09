package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/6.
 */
public class DemandPurposeListBean {
    public int rescode;
    public Data data;

    public class Data {
        public Purpose purpose;
    }

    public class Purpose {
        public ArrayList<PurposeInfo> list;
        public int status;//需求本身的状态
    }

    public class PurposeInfo {
        public String avatar;//服务者头像地址
        public int bp;//服务者退款方式 1平台方式 2自主方式
        public String company;//服务者所在公司
        public String direction;
        public String industry;
        public String instalment;//服务者分期情况 如果为空字符串，表示服务者不要求分期
        public int isauth;
        public String name;//服务者名称
        //        public int onlinestamp;//服务者上次登录时间戳
//        public String profession;//服务者公司职位
        public String position;
        public double quote;//服务者报价信息
        public long starttime;
        public int status;//服务者意向单状态
        public long uid;//服务者UID
    }
}
