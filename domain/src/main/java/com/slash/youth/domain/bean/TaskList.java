package com.slash.youth.domain.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by acer on 2017/3/9.
 */

public class TaskList {

    private ArrayList<TaskBean> list;

    public ArrayList<TaskBean> getList() {
        return list;
    }

    public void setList(ArrayList<TaskBean> list) {
        this.list = list;
    }

    public static class TaskBean implements Serializable {

        public String avatar;//用户头像，可以是发布者头像，也可以是竞标者头像
        public int bidnum;//抢单数量
        public long cts;//我的任务创建时间
        public int his;//是否是历史任务 0 否 1 是 （这个字段暂时无用）
        public long id;
        public int instalment;//0 or 1 表示是否开启分期   0不允许分期 1允许分期
        public int instalmentcurr;//表示当前处于第几个分期 1 到 4
        public int instalmentcurrfinish;//表示当期是否服务方完成 0未完成 1已经完成
        public String instalmentratio;//表示分期情况，格式为30,20,10,40 (英文逗号分隔)

        public int isauth;//用户是否认证 1已经认证 0未认证
        public String name;//用户姓名 （可以是发布人姓名或者竞标人姓名）
        //public String profession;//发布人行业
        //public String position;//发布人工作职位
        public double quote;//报价
        public int quoteunit;//报价单位，服务中需要使用
        public int rectify;

        public int roleid;//表示是我抢的单子 还是 我发布的任务 1发布者 2抢单者 （这个字段比较重要，用于判断单子类型）
        public long starttime;//任务开始时间，也可以为空，如果为空返回0
        public long endtime;//结束时间
        public int status;//需求 or 服务状态 参考:
        public long tid;//任务ID (即:需求或者服务ID)
        public String title;//任务标题（即需求 or 服务标题）

        public int type;//需求或者服务类型 1需求 2服务
        public long uid;//用户ID
        public long uts;//我的任务最后更新时间（因为需求和服务的单子是流转状态的，列表排序依赖这个）


        public int timetype;//服务中才有

        public String dname;//服务中的需求方名字，这个字段暂时服务端还没有返回，先写在这里

    }
}
