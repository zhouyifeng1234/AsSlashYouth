package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/4.
 */
public class SearchAllBean {

    public DataBean data;
    public int rescode;

    class DataBean{
        ArrayList<TitleBean> demandlist;
        ArrayList<TitleBean> servicelist;
        ArrayList<NameBean> userlist;
    }
    class TitleBean{
        String title;
    }
    class NameBean{
        String name;
    }
}
