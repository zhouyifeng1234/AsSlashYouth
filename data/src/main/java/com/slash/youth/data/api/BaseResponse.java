package com.slash.youth.data.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static android.R.id.message;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/7/15
 */
public class BaseResponse<T> implements Serializable {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_UNKNOW = 0;
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_NOT_FIND = 404;
    public static final int RESULT_NOT_LOGIN = 201;
    public static final int RESULT_TOKEN_EXPRIED = 202;
    public static final int RESULT_NOT_PERMISSION = 203;

    private T data;
    private int rescode;

    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getRescode() {
        return rescode;
    }

    public void setRescode(int rescode) {
        this.rescode = rescode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "code:" + rescode
                + " + result:" + data.toString();
    }
}
