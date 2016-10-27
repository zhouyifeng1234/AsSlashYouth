package com.slash.youth.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zss on 2016/10/25.
 */
public class SkillLabelGetBean {


    /**
     * data : [{"f1":1,"f2":1,"id":3,"tags":"ff_ff123","uid":10000},{"f1":1,"f2":2,"id":4,"tags":"ff123","uid":10000},{"f1":1,"f2":4,"id":5,"tags":"qwe_123123_asdasd_名模_龙门_爹地，分_你，末，末_得到_积极了_仔细想想_敏敏_啊，a_看看咯哦_今明_民工","uid":10000},{"f1":1,"f2":7,"id":6,"tags":"积极了","uid":10000},{"f1":1,"f2":6,"id":7,"tags":"名模","uid":10000},{"f1":1,"f2":5,"id":8,"tags":"看看咯哦_今明_民工","uid":10000},{"f1":1,"f2":8,"id":9,"tags":"今明_民工","uid":10000},{"f1":1,"f2":9,"id":10,"tags":"哦哦哦哦哦哦哦哦哦","uid":10000}]
     * rescode : 0
     */

    private int rescode;
    /**
     * f1 : 1
     * f2 : 1
     * id : 3
     * tags : ff_ff123
     * uid : 10000
     */

    public List<DataBean> data;

    public int getRescode() {
        return rescode;
    }

    public void setRescode(int rescode) {
        this.rescode = rescode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public  class DataBean implements Serializable{
        private int f1;
        private int f2;
        private int id;
        private String tags;
        private int uid;

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

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
