package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskBean {

    public String avatar;//用户头像，可以是发布者头像，也可以是竞标者头像
    public int bidnum;//抢单数量
    public long cts;//我的任务创建时间
    public int his;//是否是历史任务 0 否 1 是 （这个字段暂时无用）
    public int instalment;//分期次数,最大4期，如果无人竞标返回0客户端可以不展示

    public int isauth;//用户是否认证 1已经认证 0未认证
    public String name;//用户姓名 （可以是发布人姓名或者竞标人姓名）
    //public String profession;//发布人行业
    //public String position;//发布人工作职位
    public double quote;//报价

    public int roleid;//表示是我抢的单子 还是 我发布的任务 1发布者 2抢单者 （这个字段比较重要，用于判断单子类型）
    public long starttime;//任务开始时间，也可以为空，如果为空返回0
    public int status;//需求 or 服务状态 参考:
    public long tid;//任务ID (即:需求或者服务ID)
    public String title;//任务标题（即需求 or 服务标题）

    public int type;//需求或者服务类型 1需求 2服务
    public long uid;//用户ID
    public long uts;//我的任务最后更新时间（因为需求和服务的单子是流转状态的，列表排序依赖这个）

    public String instalmentratio;//表示分期情况，格式为30,20,10,40 (英文逗号分隔)
    public String instalmentcurr;//表示当前处于第几个分期 1 到 4

}
