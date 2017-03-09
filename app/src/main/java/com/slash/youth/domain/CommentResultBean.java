package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/11/14.
 */
public class CommentResultBean {
    public int rescode;
    public Data data;

    public class Data {
        public Evaluation evaluation;
    }

    public class Evaluation {
        public int status;//是否评价成功 1成功 0失败
        public double amount;//佣金返现金额
    }
//注意: 只能服务彻底完成后评价一次，如果评价多次则返回  {"evaluation": {}}
}
