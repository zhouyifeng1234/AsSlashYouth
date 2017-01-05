package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.BidTaskStatusProtocol;
import com.slash.youth.http.protocol.CommentProtocol;
import com.slash.youth.http.protocol.GetMyTaskListProtocol;
import com.slash.youth.http.protocol.GetTaskItemProtocol;
import com.slash.youth.http.protocol.MyTaskServiceDetailProtocol;
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
     * 三、[需求]-查看评价和分享状态接口
     */
    public static void getCommentStatus(BaseProtocol.IResultExecutor onGetCommentStatusFinished, String tid, String type) {
        QueryCommentStatusProtocol queryCommentStatusProtocol = new QueryCommentStatusProtocol(tid, type);
        queryCommentStatusProtocol.getDataFromServer(onGetCommentStatusFinished);
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
}
