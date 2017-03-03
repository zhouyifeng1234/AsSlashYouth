package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.FirstPagerAdvertisementProtocol;
import com.slash.youth.http.protocol.FreeTimeDemandProtocol;
import com.slash.youth.http.protocol.FreeTimeMoreDemandProtocol;
import com.slash.youth.http.protocol.FreeTimeMoreServiceListProtocol;
import com.slash.youth.http.protocol.FreeTimeServiceListProtocol;
import com.slash.youth.http.protocol.HomeTagConfigProtocol;
import com.slash.youth.http.protocol.RecommendDemand2Protocol;
import com.slash.youth.http.protocol.RecommendDemandMore2Protocol;
import com.slash.youth.http.protocol.RecommendService2Protocol;
import com.slash.youth.http.protocol.RecommendServiceMore2Protocol;


/**
 * Created by acer on 2016/12/8.
 */
public class FirstPagerManager {
    public static final String PATTERN_UP = "线上";
    public static final String DEMAND_INSTALMENT = "一次性到帐";
    public static final String SERVICE_INSTALMENT = "分期到帐";
    public static final String DEMAND_QUOTE = "服务方报价";
    public static final String PATTERN_DOWN = "线下";
    public static final String FREE_TIME = "闲置时间:";
    public static final String ANY_TIME = "随时";
    public static final String START_TIME = "发布时间:";
    public static final String[] QUOTEUNITS = {"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
    public static final String QUOTE = "报价:¥";
    /*闲时类型*/
    public static final int SERVICE_TIMETYPE_USER_DEFINED = 0;
    public static final String TIMETYPE_USER_DEFINED = "用户默认";
    public static final int SERVICE_TIMETYPE_WEEKEND = 2;
    public static final String TIMETYPE_WEEKEND = "周末";
    public static final int SERVICE_TIMETYPE_AFTER_WORK = 1;
    public static final String TIMETYPE_AFTER_WORK = "下班以后";
    public static final int SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND = 3;
    public static final String AFTER_WORK_AND_WEEKEND = "下班后和周末";
    public static final int SERVICE_TIMETYPE_ANYTIME = 4;
    public static final String TIMETYPE_ANYTIME = "随时";
    public static final String[] TIMETYPES = {"下班以后", "周末", "下班后及周末", "随时"};


    //首页广告
    public static void onGetFirstPagerAdvertisement(BaseProtocol.IResultExecutor onGetFirstPagerAdvertisement) {
        FirstPagerAdvertisementProtocol firstPagerAdvertisementProtocol = new FirstPagerAdvertisementProtocol();
        firstPagerAdvertisementProtocol.getDataFromServer(onGetFirstPagerAdvertisement);
    }

    //[闲时]-更多服务列表查询
    public static void onFreeTimeMoreServiceList(BaseProtocol.IResultExecutor onFreeTimeServiceList, String tag, int pattern, int isauth, String city, int sort, double lng, double lat, int offset, int limit) {
        FreeTimeMoreServiceListProtocol freeTimeServiceListProtocol = new FreeTimeMoreServiceListProtocol(tag, pattern, isauth, city, sort, lng, lat, offset, limit);
        freeTimeServiceListProtocol.getDataFromServer(onFreeTimeServiceList);
    }

    //[闲时]-更多需求列表查询
    public static void onFreeTimeMoreDemandList(BaseProtocol.IResultExecutor onFreeTimeServiceList, int pattern, int isauth, String city, int sort, double lng, double lat, int offset, int limit) {
        FreeTimeMoreDemandProtocol freeTimeDemandProtocol = new FreeTimeMoreDemandProtocol(pattern, isauth, city, sort, lng, lat, offset, limit);
        freeTimeDemandProtocol.getDataFromServer(onFreeTimeServiceList);
    }

    //[推荐]-首页推荐列表
    public static void onFreeTimeDemandList(BaseProtocol.IResultExecutor onFreeTimeServiceList, int limit) {
        FreeTimeDemandProtocol freeTimeDemandProtocol = new FreeTimeDemandProtocol(limit);
        freeTimeDemandProtocol.getDataFromServer(onFreeTimeServiceList);
    }

    //[推荐]-首页需求推荐列表
    public static void onFreeTimeServiceList(BaseProtocol.IResultExecutor onFreeTimeServiceList, int limit) {
        FreeTimeServiceListProtocol freeTimeServiceListProtocol = new FreeTimeServiceListProtocol(limit);
        freeTimeServiceListProtocol.getDataFromServer(onFreeTimeServiceList);
    }

    /**
     * 二、[推广]-首页Tag配置信息接口
     */
    public static void getHomeTagConfig(BaseProtocol.IResultExecutor onGetTagConfigFinished) {
        HomeTagConfigProtocol homeTagConfigProtocol = new HomeTagConfigProtocol();
        homeTagConfigProtocol.getDataFromServer(onGetTagConfigFinished);

    }


    /**
     * V1.1版首页需求推荐列表
     */
    public static void getRecommendDemand2(BaseProtocol.IResultExecutor onGetRecommendDemandFinished, String limit) {
        RecommendDemand2Protocol recommendDemand2Protocol = new RecommendDemand2Protocol(limit);
        recommendDemand2Protocol.getDataFromServer(onGetRecommendDemandFinished);
    }

    /**
     * V1.1版首页服务推荐列表
     */
    public static void getRecommendService2(BaseProtocol.IResultExecutor onGetRecommendServiceFinished, String limit) {
        RecommendService2Protocol recommendDemand2Protocol = new RecommendService2Protocol(limit);
        recommendDemand2Protocol.getDataFromServer(onGetRecommendServiceFinished);
    }

    /**
     * V1.1版新增  三、[推荐]-更多需求推荐列表
     *
     * @param onGetMoreDemandFinished
     * @param rec_offset              //推荐分页offset
     * @param rec_limit               //推荐分页limit
     * @param rad_offset              //随机分页offset
     * @param rad_limit               //随机分页limit
     */
    public static void getRecommendDemandMore2(BaseProtocol.IResultExecutor onGetMoreDemandFinished, String rec_offset, String rec_limit, String rad_offset, String rad_limit) {
        RecommendDemandMore2Protocol recommendDemandMore2Protocol = new RecommendDemandMore2Protocol(rec_offset, rec_limit, rad_offset, rad_limit);
        recommendDemandMore2Protocol.getDataFromServer(onGetMoreDemandFinished);
    }

    /**
     * V1.1版新增  四、[推荐]-更多服务推荐列表
     *
     * @param onGetMoreServiceFinished
     * @param rec_offset               //推荐分页offset
     * @param rec_limit                //推荐分页limit
     * @param rad_offset               //随机分页offset
     * @param rad_limit                //随机分页limit
     */
    public static void getRecommendServiceMore2(BaseProtocol.IResultExecutor onGetMoreServiceFinished, String rec_offset, String rec_limit, String rad_offset, String rad_limit) {
        RecommendServiceMore2Protocol recommendServiceMore2Protocol = new RecommendServiceMore2Protocol(rec_offset, rec_limit, rad_offset, rad_limit);
        recommendServiceMore2Protocol.getDataFromServer(onGetMoreServiceFinished);
    }

}
