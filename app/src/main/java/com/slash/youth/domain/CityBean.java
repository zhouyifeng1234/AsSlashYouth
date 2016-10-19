package com.slash.youth.domain;

import java.util.List;

/**
 * Created by acer on 2016/10/11.
 */
public class CityBean {

    /**
     * name : 请选择
     * sub : [{"name":"请选择"}]
     * type : 1
     */

    private String name;
    private int type;
    /**
     * name : 请选择
     */

    private List<SubBean> sub;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SubBean> getSub() {
        return sub;
    }

    public void setSub(List<SubBean> sub) {
        this.sub = sub;
    }

    public static class SubBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
