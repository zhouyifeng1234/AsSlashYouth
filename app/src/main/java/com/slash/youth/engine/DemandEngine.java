package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CancelDemandProtocol;
import com.slash.youth.http.protocol.MyPublishDemandListProtocol;
import com.slash.youth.http.protocol.PublishDemandProtocol;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 需求相关操作
 */
public class DemandEngine {


    /**
     * 发布需求
     */
    public static void publishDemand(BaseProtocol.IResultExecutor onPublishDemandFinished, String title, String label, String tasktime, String fighttime, String anonymity, String desc, String pic, String type, String proofbox, String invoice, String pattern, String place, String placedetail, String consume, String lng, String lat, String offer, String quote) {
        PublishDemandProtocol publishDemandProtocol = new PublishDemandProtocol(title, label, tasktime, fighttime, anonymity, desc, pic, type, proofbox, invoice, pattern, place, placedetail, consume, lng, lat, offer, quote);
        publishDemandProtocol.getDataFromServer(onPublishDemandFinished);
    }

    public static void cancelDemand(BaseProtocol.IResultExecutor onCancelDemandFinished,String cancelDemandId) {
        CancelDemandProtocol cancelDemandProtocol = new CancelDemandProtocol(cancelDemandId);
        cancelDemandProtocol.getDataFromServer(onCancelDemandFinished);
    }

    /**
     * 获取我发布的需求列表
     *
     * @return
     */
    public static void getMyPublishDemandList(BaseProtocol.IResultExecutor onGetMyPublishDemandListFinished) {
        MyPublishDemandListProtocol myPublishDemandListProtocol = new MyPublishDemandListProtocol();
        myPublishDemandListProtocol.getDataFromServer(onGetMyPublishDemandListFinished);
    }
}
