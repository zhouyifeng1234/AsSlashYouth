package com.slash.youth.domain;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/28.
 */
public class ConversationListBean {
    public int rescode;
    public Data data;

    public class Data {
        public ArrayList<ConversationInfo> list;
    }

    public class ConversationInfo {
        public String avatar;
        public String company;
        public int isAuth;
        public String name;
        public String position;
        public long uid;
        public long uts;
    }
}
