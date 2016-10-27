package com.slash.youth.domain;

import java.io.Serializable;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class SkillLabelBean implements Serializable {

    //public String labelName;
    private int f1;
    private int f2;
    private int id;
    public String tag;

    public SkillLabelBean(String labelName,int f1,int f2,int id) {
        this.tag = labelName;
        this.f1 = f1;
        this.f2 = f2;
        this.id =id;
    }

    public int getF1() {
        return f1;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public int getF2() {
        return f2;
    }

    public void setF2(int f2) {
        this.f2 = f2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



}
