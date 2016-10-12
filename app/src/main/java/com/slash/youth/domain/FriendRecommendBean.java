package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class FriendRecommendBean {
    public boolean isEliteRecommedn;//true为精英推荐，false为普通推荐
    public String username;

    public FriendRecommendBean(boolean isEliteRecommedn, String username) {
        this.isEliteRecommedn = isEliteRecommedn;
        this.username = username;
    }
}
