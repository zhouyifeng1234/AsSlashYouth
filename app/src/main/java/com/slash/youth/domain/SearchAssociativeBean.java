package com.slash.youth.domain;

import android.nfc.Tag;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/4.  搜索联想词
 */
public class SearchAssociativeBean {

    public DataBean data;
    public int rescode;

    public class DataBean{
        ArrayList<TagBean> list;
    }

    class TagBean{
        String tag;
    }

}
