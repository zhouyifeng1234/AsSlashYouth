package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.GetMyTaskListProtocol;
import com.slash.youth.http.protocol.GetTaskItemProtocol;

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
}
