package com.slash.youth.domain.bean;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class HomeTagInfoBean {

    public ArrayList<TagInfo> tag;

    public class TagInfo {
        public long id;
        public String name;
        public String icon;
    }
}
