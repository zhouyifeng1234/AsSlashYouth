package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/4.
 */
public class ResultErrorBean {
    //{"code":2,"data":{"message":"signature check error : date timeout"}}
    public int code;
    public ErrorInfo data;

    public class ErrorInfo {
        public String message;
    }
}
