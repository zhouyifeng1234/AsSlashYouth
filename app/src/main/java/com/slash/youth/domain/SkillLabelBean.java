package com.slash.youth.domain;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class SkillLabelBean implements Serializable {

//    public String labelName;
    private int f1;
    private int f2;
    private int id;
    public String tag;
//    private ArrayList<SkillLabelBean> childs =new ArrayList<>();//li
//    private ArrayList<Integer> childIds = new ArrayList<>();//li
    public SkillLabelBean(int f1,int f2,int id,String labelName) {
        this.f1 = f1;
        this.f2 = f2;
        this.id =id;
        this.tag = labelName;
    }
//    public void addChild(SkillLabelBean child){
//        childs.add(child);
//        childIds.add(child.id);
//    }
//    public  void  removeChild(SkillLabelBean child){
//        childs.remove(child);
//        childIds.remove(child.id);
//    }
//    public SkillLabelBean findChildById(int id){
//        return childs.get(childIds.indexOf(id));
//    }
//
//    public ArrayList<SkillLabelBean> getChild(){
////        ArrayList<SkillLabelBean> skillLabelBeen = new ArrayList<>();
////        Collections.copy(skillLabelBeen,childs);//避免直接操作子类集合；
////        return skillLabelBeen;
//        return  childs;
//    }



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
