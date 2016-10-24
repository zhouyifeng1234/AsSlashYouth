package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CancelDemandProtocol;
import com.slash.youth.http.protocol.DemandPartyConfirmCompleteProtocol;
import com.slash.youth.http.protocol.DemandPartyGetBidListProtocol;
import com.slash.youth.http.protocol.DemandPartyPrePaymentProtocol;
import com.slash.youth.http.protocol.DemandPartySelectServicePartyProtocol;
import com.slash.youth.http.protocol.DownloadFileProtocol;
import com.slash.youth.http.protocol.GetDemandDescProtocol;
import com.slash.youth.http.protocol.GetDemandFlowLogProtocol;
import com.slash.youth.http.protocol.ImgUploadProtocol;
import com.slash.youth.http.protocol.MyPublishDemandListProtocol;
import com.slash.youth.http.protocol.MyPublishHistoryDemandListProtocol;
import com.slash.youth.http.protocol.PublishDemandProtocol;
import com.slash.youth.http.protocol.ServicePartyBidDemandProtocol;
import com.slash.youth.http.protocol.ServicePartyCompleteProtocol;
import com.slash.youth.http.protocol.ServicePartyConfirmServantProtocol;
import com.slash.youth.http.protocol.ServicePartyRejectProtocol;
import com.slash.youth.http.protocol.SetDemandDescProtocol;
import com.slash.youth.ui.activity.test.FileUploadProtocol;

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

    /**
     * 需求方取消需求
     *
     * @param onCancelDemandFinished
     * @param cancelDemandId
     */
    public static void cancelDemand(BaseProtocol.IResultExecutor onCancelDemandFinished, String cancelDemandId) {
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

    /**
     * 服务方竞标需求
     *
     * @param onBidDemandFinished
     * @param id
     * @param quote
     */
    public static void servicePartyBidDemand(BaseProtocol.IResultExecutor onBidDemandFinished, String id, String quote) {
        ServicePartyBidDemandProtocol servicePartyBidDemandProtocol = new ServicePartyBidDemandProtocol(id, quote);
        servicePartyBidDemandProtocol.getDataFromServer(onBidDemandFinished);
    }


    /**
     * 五、[需求]-需求方选择服务方
     *
     * @param onSelectServicePartyFinished
     * @param id                           需求ID
     * @param uid                          服务方UID
     */
    public static void demandPartySelectServiceParty(BaseProtocol.IResultExecutor onSelectServicePartyFinished, String id, String uid) {
        DemandPartySelectServicePartyProtocol demandPartySelectServicePartyProtocol = new DemandPartySelectServicePartyProtocol(id, uid);
        demandPartySelectServicePartyProtocol.getDataFromServer(onSelectServicePartyFinished);
    }


    /**
     * 六、[需求]-服务方确认一个服务者
     *
     * @param onConfirmServantFinished
     * @param id                       需求ID
     * @param uid                      服务方UID
     */
    public static void servicePartyConfirmServant(BaseProtocol.IResultExecutor onConfirmServantFinished, String id, String uid) {
        ServicePartyConfirmServantProtocol servicePartyConfirmServantProtocol = new ServicePartyConfirmServantProtocol(id, uid);
        servicePartyConfirmServantProtocol.getDataFromServer(onConfirmServantFinished);
    }


    /**
     * 七、[需求]-查看需求流程日志
     *
     * @param onGetDemnadFlowLogFinished
     * @param id                         需求ID
     */
    public static void getDemandFlowLog(BaseProtocol.IResultExecutor onGetDemnadFlowLogFinished, String id) {
        GetDemandFlowLogProtocol getDemandFlowLogProtocol = new GetDemandFlowLogProtocol(id);
        getDemandFlowLogProtocol.getDataFromServer(onGetDemnadFlowLogFinished);
    }

    /**
     * 八、[需求]-服务方拒绝
     *
     * @param onServicePartyRejectFinished
     * @param id                           需求ID
     */
    public static void servicePartyReject(BaseProtocol.IResultExecutor onServicePartyRejectFinished, String id) {
        ServicePartyRejectProtocol servicePartyRejectProtocol = new ServicePartyRejectProtocol(id);
        servicePartyRejectProtocol.getDataFromServer(onServicePartyRejectFinished);
    }


    /**
     * 九、[需求]-需求方预支付
     *
     * @param onDemandPartyPrePaymentFinished
     * @param id                              需求ID
     */
    public static void demandPartyPrePayment(BaseProtocol.IResultExecutor onDemandPartyPrePaymentFinished, String id) {
        DemandPartyPrePaymentProtocol demandPartyPrePaymentProtocol = new DemandPartyPrePaymentProtocol(id);
        demandPartyPrePaymentProtocol.getDataFromServer(onDemandPartyPrePaymentFinished);
    }

    /**
     * 十、[需求]-我发布的历史需求列表
     *
     * @param onGetHistoryDemandListFinished
     * @param offset                         分页 >=0
     * @param limit                          分页 >=0 最大20 如果设置=0 则系统默认10
     */
    public static void getMyPublishHistoryDemandList(BaseProtocol.IResultExecutor onGetHistoryDemandListFinished, String offset, String limit) {
        MyPublishHistoryDemandListProtocol myPublishHistoryDemandListProtocol = new MyPublishHistoryDemandListProtocol(offset, limit);
        myPublishHistoryDemandListProtocol.getDataFromServer(onGetHistoryDemandListFinished);
    }

    /**
     * 十一、[需求]-服务方完成任务
     *
     * @param onServicePartyCompleteFinished
     * @param id                             需求ID
     */
    public static void servicePartyComplete(BaseProtocol.IResultExecutor onServicePartyCompleteFinished, String id) {
        ServicePartyCompleteProtocol servicePartyCompleteProtocol = new ServicePartyCompleteProtocol(id);
        servicePartyCompleteProtocol.getDataFromServer(onServicePartyCompleteFinished);
    }

    /**
     * 十二、[需求]-服务方完成任务(应该是 需求方确认完成 ？？？)
     *
     * @param onConfirmCompleteFinished
     * @param id                        需求ID
     */
    public static void demandPartyConfirmComplete(BaseProtocol.IResultExecutor onConfirmCompleteFinished, String id) {
        DemandPartyConfirmCompleteProtocol demandPartyConfirmCompleteProtocol = new DemandPartyConfirmCompleteProtocol(id);
        demandPartyConfirmCompleteProtocol.getDataFromServer(onConfirmCompleteFinished);
    }

    /**
     * 十三、[需求]-需求方查看竞标（抢需求服务者）列表
     *
     * @param onGetBidListFinished
     * @param id                   需求ID
     */
    public static void demandPartyGetBidList(BaseProtocol.IResultExecutor onGetBidListFinished, String id) {
        DemandPartyGetBidListProtocol demandPartyGetBidListProtocol = new DemandPartyGetBidListProtocol(id);
        demandPartyGetBidListProtocol.getDataFromServer(onGetBidListFinished);
    }

    /**
     * 十四、[需求]-加载需求描述信息
     *
     * @param onGetDemandDescFinished
     * @param id                      需求ID
     */
    public static void getDemandDesc(BaseProtocol.IResultExecutor onGetDemandDescFinished, String id) {
        GetDemandDescProtocol getDemandDescProtocol = new GetDemandDescProtocol(id);
        getDemandDescProtocol.getDataFromServer(onGetDemandDescFinished);
    }

    /**
     * 十五、[需求]-设置需求描述信息
     *
     * @param onSetDemandDescFinished
     * @param id                      需求ID	>=0
     * @param desc                    需求描述信息	长度小于4096，该接口只能在服务方未确认前修改，服务方一旦确认该接口设置失效
     */
    public static void setDemandDesc(BaseProtocol.IResultExecutor onSetDemandDescFinished, String id, String desc) {
        SetDemandDescProtocol setDemandDescProtocol = new SetDemandDescProtocol(id, desc);
        setDemandDescProtocol.getDataFromServer(onSetDemandDescFinished);
    }

    //一、[文件]-图片上传
    public static void uploadFile(BaseProtocol.IResultExecutor onUploadFileFinished) {
        FileUploadProtocol fileUploadProtocol = new FileUploadProtocol();
        fileUploadProtocol.getDataFromServer(onUploadFileFinished);
    }

    //二、[文件]-图片下载
    public static void downloadFile(BaseProtocol.IResultExecutor onDownloadFileFinished) {
        DownloadFileProtocol downloadFileProtocol = new DownloadFileProtocol();
        downloadFileProtocol.getDataFromServer(onDownloadFileFinished);
    }

}
