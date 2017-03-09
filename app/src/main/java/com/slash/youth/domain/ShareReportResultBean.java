package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2017/2/21.
 */
public class ShareReportResultBean {

    public int rescode;
    public Evaluation evaluation;

    public class Evaluation {
        public int status;
        public double amount;
    }

}
