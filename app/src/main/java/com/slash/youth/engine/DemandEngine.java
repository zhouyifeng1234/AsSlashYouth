package com.slash.youth.engine;

import com.slash.youth.domain.DemandListInfo;
import com.slash.youth.http.protocol.PublishDemandProtocol;
import com.slash.youth.ui.viewmodel.PublishDemandModeModel;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 需求相关操作
 */
public class DemandEngine {

    /**
     * 获取我发布的需求
     *
     * @param userToken
     */
    public static DemandListInfo getMyPublishDemand(String userToken) {
        //TODO 具体获取我发布需求的逻辑 ，调用服务端借口获取

        return null;
    }

    /**
     * 发布需求
     */
    public static void publishDemand(PublishDemandModeModel.OnPublishDemandFinished onPublishDemandFinished, String title, String label, String tasktime, String fighttime, String anonymity, String desc, String pic, String type, String proofbox, String invoice, String pattern, String place, String placedetail, String consume, String lng, String lat, String offer, String quote) {
        PublishDemandProtocol publishDemandProtocol = new PublishDemandProtocol(title, label, tasktime, fighttime, anonymity, desc, pic, type, proofbox, invoice, pattern, place, placedetail, consume, lng, lat, offer, quote);
        publishDemandProtocol.getDataFromServer(onPublishDemandFinished);
    }
}
