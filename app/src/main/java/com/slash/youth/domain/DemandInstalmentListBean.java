package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/1/18.
 */
public class DemandInstalmentListBean {
    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<InstalmentInfo> list;
    }

    public class InstalmentInfo {
        public int id;//第几个分期
        public double percent;//百分比  0.22
        public int status;//完成情况  0表示未开始  1表示服务方完成  2表示需求方确认此分期完成
    }
}
