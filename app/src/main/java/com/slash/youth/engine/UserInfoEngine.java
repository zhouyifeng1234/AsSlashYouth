package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.NewDemandAndServiceProtocol;
import com.slash.youth.http.protocol.OtherUserInfoProtocol;

/**
 * Created by zhouyifeng on 2016/12/1.
 */
public class UserInfoEngine {
    public static final String TASK_TIME_TITLE = "任务时间:";

    /**
     * 一、[用戶信息]-用户个人信息（获取别人的用户信息）
     */
    public static void getOtherUserInfo(BaseProtocol.IResultExecutor onGetOtherUserInfoFinished, String uid, String isvisitor) {
        OtherUserInfoProtocol otherUserInfoProtocol = new OtherUserInfoProtocol(uid, isvisitor);
        otherUserInfoProtocol.getDataFromServer(onGetOtherUserInfoFinished);
    }

    /**
     * 二、[用戶信息]-获取个人资料(获取我的个人信息资料，自己的信息，当前登陆者的信息)
     */
    public static void getMyUserInfo() {

    }

    //[最近列表]-查看用户最近发布的列表
    public static void getNewDemandAndServiceList(BaseProtocol.IResultExecutor onGetNewDemandAndServiceList, long uid, int  offset,int  limit) {
        NewDemandAndServiceProtocol newDemandAndServiceProtocol = new NewDemandAndServiceProtocol(uid,offset,limit);
        newDemandAndServiceProtocol.getDataFromServer(onGetNewDemandAndServiceList);
    }



}
