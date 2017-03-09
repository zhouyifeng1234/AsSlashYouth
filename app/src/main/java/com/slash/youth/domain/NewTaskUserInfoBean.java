package com.slash.youth.domain;

/**
 * Created by zss on 2016/11/7.
 */
public class NewTaskUserInfoBean {
     public boolean isDemand;//true 本人，false代表其他人

    public NewTaskUserInfoBean(boolean isDemand) {
        this.isDemand = isDemand;
    }

}
