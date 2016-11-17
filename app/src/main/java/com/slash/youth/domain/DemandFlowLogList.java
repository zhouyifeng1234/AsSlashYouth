package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/13.
 */
public class DemandFlowLogList {

    public int rescode;

    public Data data;

    public class Data {
        public ArrayList<LogInfo> list;
    }

    public class LogInfo {
        public String action;//日志动作
        public long cts;//日志记录时间
        public int did;//需求唯一ID
        public int id;//流程seqid
    }

}
