package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/12/26.
 */
public class CommentStatusBean {
    public int rescode;
    public Data data;

    public class Data {
        public Evaluation evaluation;
    }

    public class Evaluation {
        public int attitude;
        public long cts = 0;
        public long fromuid;
        public long id;
        public int quality;
        public String remark;
        public int rsstype;
        public int share;
        public int speed;
        public long tid;
        public long touid;
        public int type;
        public long uts;
    }

}
