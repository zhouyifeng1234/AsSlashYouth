package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/2/5.
 */
public class CommonLogList {
    public int rescode;

    public Data data;

    public class Data {
        public ArrayList<CommonLogInfo> list;
    }

    public class CommonLogInfo {
        public long cts;
        public String log;
        public int roleid;
        public long tid;
        public int type;
        public long uid;
    }
}
