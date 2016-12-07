package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/7.
 */
public class ServiceInstalmentListBean {
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

//{
//        "data": {
//        "list": [
//        {
//        "id": 1,
//        "percent": 0.22,
//        "status": 2
//        },
//        {
//        "id": 2,
//        "percent": 0.38,
//        "status": 2
//        },
//        {
//        "id": 3,
//        "percent": 0.21,
//        "status": 2
//        },
//        {
//        "id": 4,
//        "percent": 0.19,
//        "status": 2
//        }
//        ]
//        },
//        "rescode": 0
//        }
