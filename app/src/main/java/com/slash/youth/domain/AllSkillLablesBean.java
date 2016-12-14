package com.slash.youth.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 分好一、二、三级的标签嵌套逻辑 的实体类
 * <p/>
 * Created by zhouyifeng on 2016/12/14.
 */
public class AllSkillLablesBean implements Serializable {

    public HashMap<Long, Tag_1> mapTag_1 = new HashMap<Long, Tag_1>();

    public class Tag_1 implements Serializable {
        public long f1;
        public long f2;
        public long id;
        public String tag;
        public HashMap<Long, Tag_2> mapTag_2 = new HashMap<Long, Tag_2>();
    }

    public class Tag_2 implements Serializable {
        public long f1;
        public long f2;
        public long id;
        public String tag;
        public HashMap<Long, Tag_3> mapTag_3 = new HashMap<Long, Tag_3>();
    }

    public class Tag_3 implements Serializable {
        public long f1;
        public long f2;
        public long id;
        public String tag;
    }

    //必须先添加一级，然后添加二级，最后添加三级，不然比如当添加三级的时候，可能对应的二级或者一级还没有添加进去，就会空指针异常
    public void addTag(LoginTagBean tag) {
        if (tag.f1 == 0 && tag.f2 == 0) {//一级标签
            Tag_1 tag_1 = new Tag_1();
            tag_1.f1 = tag.f1;
            tag_1.f2 = tag.f2;
            tag_1.id = tag.id;
            tag_1.tag = tag.tag;
            mapTag_1.put(tag.id, tag_1);
        } else if (tag.f1 != 0 && tag.f2 == 0) {//二级标签
            Tag_2 tag_2 = new Tag_2();
            tag_2.f1 = tag.f1;
            tag_2.f2 = tag.f2;
            tag_2.id = tag.id;
            tag_2.tag = tag.tag;
            Tag_1 tag_1 = mapTag_1.get(tag.f1);
            if (tag_1 != null) {
                tag_1.mapTag_2.put(tag.id, tag_2);
            }
        } else {//三级标签
            Tag_3 tag_3 = new Tag_3();
            tag_3.f1 = tag.f1;
            tag_3.f2 = tag.f2;
            tag_3.id = tag.id;
            tag_3.tag = tag.tag;
            Tag_1 tag_1 = mapTag_1.get(tag.f1);
            if (tag_1 != null) {
                Tag_2 tag_2 = tag_1.mapTag_2.get(tag.f2);
                if (tag_2 != null) {
                    tag_2.mapTag_3.put(tag.id, tag_3);
                }
            }
        }
    }

    ArrayList<LoginTagBean> listTags1 = new ArrayList<LoginTagBean>();
    ArrayList<LoginTagBean> listTags2 = new ArrayList<LoginTagBean>();
    ArrayList<LoginTagBean> listTags3 = new ArrayList<LoginTagBean>();

    public void addListTags(ArrayList<LoginTagBean> listTags) {
//        for (LoginTagBean loginTagBean : listTags) {
//            addTag(loginTagBean);
//        }
        for (LoginTagBean loginTagBean : listTags) {
            if (loginTagBean.f1 == 0 && loginTagBean.f2 == 0) {//一级标签
                listTags1.add(loginTagBean);
            } else if (loginTagBean.f1 != 0 && loginTagBean.f2 == 0) {//二级标签
                listTags2.add(loginTagBean);
            } else {//三级标签
                listTags3.add(loginTagBean);
            }
        }
        for (LoginTagBean loginTagBean : listTags1) {
            addTag(loginTagBean);
        }
        for (LoginTagBean loginTagBean : listTags2) {
            addTag(loginTagBean);
        }
        for (LoginTagBean loginTagBean : listTags3) {
            addTag(loginTagBean);
        }
    }
}
