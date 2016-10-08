package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/4.
 */
public class SearchUserBean {

    public DataBean data;
    public int rescode;

    class DataBean{
        ArrayList<UserBean> list;
    }

    class UserBean{
        String name;
        String avatar;
        String star;
        String isauth;
        String isfriend;
        String company;
        String profession;
        String lasttime;
    }

}
