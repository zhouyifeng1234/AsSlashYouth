package com.slash.youth.domain;

/**
 * Created by zss on 2016/10/12.
 */
public class UserInfoItemBean {
    public boolean isDemand;//true 本人，false代表其他人

    public UserInfoItemBean(boolean isDemand) {
        this.isDemand = isDemand;
    }
}
