package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/29.
 */
public class ServiceFlowLogList {
    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<LogInfo> list;
    }

    public class LogInfo {
        public String action;
        public long cts;
        public int did;
        public int id;
    }
}
