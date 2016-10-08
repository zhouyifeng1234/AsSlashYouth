package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 需求实体类，发需求 等场景需要使用
 */
public class DemandBean {
    public DemandBean(){}
    public DemandBean(long lasttime){
        this.lasttime=lasttime;
    }
    public long  lasttime;
}
