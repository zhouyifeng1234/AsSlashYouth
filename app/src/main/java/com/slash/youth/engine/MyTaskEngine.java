package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.BidTaskStatusProtocol;
import com.slash.youth.http.protocol.CancelCollectionProtocol;
import com.slash.youth.http.protocol.CollectionStatusProtocol;
import com.slash.youth.http.protocol.CommentProtocol;
import com.slash.youth.http.protocol.GetCommonLogProtocol;
import com.slash.youth.http.protocol.GetMyTaskListProtocol;
import com.slash.youth.http.protocol.GetTaskItemProtocol;
import com.slash.youth.http.protocol.MyTaskServiceDetailProtocol;
import com.slash.youth.http.protocol.ShareForwardProtocol;
import com.slash.youth.http.protocol.ShareReportProtocol;
import com.slash.youth.http.protocol.TagRecommendProtocol;
import com.slash.youth.http.protocol.UpAndDownTaskProtocol;

/**
 * Created by zhouyifeng on 2016/10/29.
 */
public class MyTaskEngine {

    public static final int USER_TASK_ALL_TYPE = 0;//我的所有任务
    public static final int USER_TASK_MY_PUBLISH_TYPE = 1;//我发布的任务
    public static final int USER_TASK_MY_BID_TYPE = 2;//我抢的任务
    public static final int USER_TASK_MY_HIS_TYPE = 3;//我的历史任务

    /**
     * @param onGetMyTaskListFinished
     * @param type                    列表类型 取值范围只能是: 我的任务列表类型定义 这些枚举
     * @param offset                  分页 >=0
     * @param limit                   分页 最大值20
     */
    public static void getMyTaskList(BaseProtocol.IResultExecutor onGetMyTaskListFinished, int type, int offset, int limit) {
        GetMyTaskListProtocol getMyTaskListProtocol = new GetMyTaskListProtocol(type, offset, limit);
        getMyTaskListProtocol.getDataFromServer(onGetMyTaskListFinished);
    }

    /**
     * @param onGetTaskItemFinished
     * @param tid                   任务ID
     * @param type                  取值范围只能是: 1需求 2服务
     * @param roleid                表示是'我抢的单子' 还是 '我发布的任务' 1发布者 2抢单者
     */
    public static void getMyTaskItem(BaseProtocol.IResultExecutor onGetTaskItemFinished, String tid, String type, String roleid) {
        GetTaskItemProtocol getTaskItemProtocol = new GetTaskItemProtocol(tid, type, roleid);
        getTaskItemProtocol.getDataFromServer(onGetTaskItemFinished);
    }


    /**
     * 通过tid获取服务详情信息
     */
    public static void getServiceDetailByTid(BaseProtocol.IResultExecutor onGetTaskServiceDetailFinished, String tid, String roleid) {
        MyTaskServiceDetailProtocol myTaskServiceDetailProtocol = new MyTaskServiceDetailProtocol(tid, roleid);
        myTaskServiceDetailProtocol.getDataFromServer(onGetTaskServiceDetailFinished);
    }

    /**
     * 一、[需求]-需求方评价接口
     *
     * @param onCommentFinished
     * @param quality           服务质量评分 枚举类型 1 2 3 4 5 表示等级
     * @param speed             服务速度评分 枚举类型 1 2 3 4 5 表示等级
     * @param attitude          服务态度评分 枚举类型 1 2 3 4 5 表示等级
     * @param remark            评价描述 长度小于4096字节
     * @param type              需求服务类型 1需求 2服务
     * @param tid               需求or服务ID
     * @param suid              服务者UID
     */
    public static void comment(BaseProtocol.IResultExecutor onCommentFinished, String quality, String speed, String attitude, String remark, String type, String tid, String suid) {
        CommentProtocol commentProtocol = new CommentProtocol(quality, speed, attitude, remark, type, tid, suid);
        commentProtocol.getDataFromServer(onCommentFinished);
    }

    /**
     * 二、[分享]-服务者分享上报接口
     *
     * @param onReportFinished
     * @param type             1需求 2服务
     * @param tid              需求or服务ID
     * @param rsslink          1QQ好友、2QQ空间、4微信好友、8微信朋友圈
     */
    public static void shareReport(BaseProtocol.IResultExecutor onReportFinished, String type, String tid, String rsslink) {
        ShareReportProtocol shareReportProtocol = new ShareReportProtocol(type, tid, rsslink);
        shareReportProtocol.getDataFromServer(onReportFinished);
    }

    /**
     * 三、[需求]-查看评价和分享状态接口
     */
    public static void getCommentStatus(BaseProtocol.IResultExecutor onGetCommentStatusFinished, String tid, String type) {
        QueryCommentStatusProtocol queryCommentStatusProtocol = new QueryCommentStatusProtocol(tid, type);
        queryCommentStatusProtocol.getDataFromServer(onGetCommentStatusFinished);
    }

    /**
     * 四、[转发]-服务者转发服务到外界专家系统加分
     *
     * @param onForwardFinished
     * @param id                需求or服务ID
     */
    public static void shareForward(BaseProtocol.IResultExecutor onForwardFinished, String id) {
        ShareForwardProtocol shareForwardProtocol = new ShareForwardProtocol(id);
        shareForwardProtocol.getDataFromServer(onForwardFinished);
    }


    /**
     * 根据需求或者服务ID上架或者下架需求服务
     *
     * @param onUpOrDownFinished
     * @param tid                需求或者服务ID
     * @param type               类型 1需求 2服务
     * @param action             1上架 0下架
     */
    public static void upAndDownTask(BaseProtocol.IResultExecutor onUpOrDownFinished, String tid, String type, String action) {
        UpAndDownTaskProtocol upAndDownTaskProtocol = new UpAndDownTaskProtocol(tid, type, action);
        upAndDownTaskProtocol.getDataFromServer(onUpOrDownFinished);
    }


    /**
     * 四、[需求]-是否预约过某服务或者抢单过某需求
     *
     * @param onGetBidTaskStatusFinished
     * @param type                       1需求 2服务
     * @param tid                        任务ID
     */
    public static void getBidTaskStatus(BaseProtocol.IResultExecutor onGetBidTaskStatusFinished, String type, String tid) {
        BidTaskStatusProtocol bidTaskStatusProtocol = new BidTaskStatusProtocol(type, tid);
        bidTaskStatusProtocol.getDataFromServer(onGetBidTaskStatusFinished);
    }

    /**
     * 三、[我的收藏]-取消收藏
     *
     * @param onCancelCollectionFinished
     * @param type                       1需求 2服务
     * @param tid                        需求or服务ID
     */
    public static void cancelCollection(BaseProtocol.IResultExecutor onCancelCollectionFinished, String type, String tid) {
        CancelCollectionProtocol cancelCollectionProtocol = new CancelCollectionProtocol(type, tid);
        cancelCollectionProtocol.getDataFromServer(onCancelCollectionFinished);
    }

    /**
     * 四、[我的收藏]-是否收藏某任务
     *
     * @param onGetCollectionStatusFinished
     * @param type                          1需求 2服务
     * @param tid                           需求或者服务ID
     */
    public static void getCollectionStatus(BaseProtocol.IResultExecutor onGetCollectionStatusFinished, String type, String tid) {
        CollectionStatusProtocol collectionStatusProtocol = new CollectionStatusProtocol(type, tid);
        collectionStatusProtocol.getDataFromServer(onGetCollectionStatusFinished);
    }


    /**
     * 一、[通用任务日志]-需求流程日志接口
     *
     * @param onGetLogFinished
     * @param tid              需求or服务 任务ID
     * @param type             1需求 or 2 服务
     * @param roleid           1发布者 2预约者（抢单者）
     */
    public static void getLog(BaseProtocol.IResultExecutor onGetLogFinished, String tid, String type, String roleid) {
        GetCommonLogProtocol getCommonLogProtocol = new GetCommonLogProtocol(tid, type, roleid);
        getCommonLogProtocol.getDataFromServer(onGetLogFinished);
    }

    /**
     * 一、[标签]-标签查询 首页分级标签查询接口
     *
     * @param onGetRecommendListFinished
     * @param tag                        tag对应的唯一ID
     * @param level                      tag的level 1表示一级标签，2表示二级标签
     * @param offset
     * @param limit
     */
    public static void getTagRecommendList(BaseProtocol.IResultExecutor onGetRecommendListFinished, String tag, String level, String offset, String limit) {
        TagRecommendProtocol tagRecommendProcotol = new TagRecommendProtocol(tag, level, offset, limit);
        tagRecommendProcotol.getDataFromServer(onGetRecommendListFinished);
    }
}
