package com.slash.youth.ui.activity.test;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.slash.youth.R;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/30.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final ScrollView svTestButton = (ScrollView) findViewById(R.id.sv_activity_test);
        svTestButton.post(new Runnable() {
            @Override
            public void run() {
                svTestButton.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void publishDemand(View v) {
//        DemandEngine.publishDemand(new BaseProtocol.IResultExecutor<PublishDemandResultBean>() {
//
//            @Override
//            public void execute(PublishDemandResultBean dataBean) {
//                ToastUtils.shortToast(dataBean.data.id + "");
//            }
//
//            @Override
//            public void executeResultError(String result) {
//
//            }
//        }, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public void cancelDemand(View v) {
        EditText tvCancelDemandId = (EditText) findViewById(R.id.tv_cancel_demand_id);
        DemandEngine.cancelDemand(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tvCancelDemandId.getText().toString());
    }

    public void getMyPublishDemandList(View v) {
//        ToastUtils.shortToast("getMyPublishDemandList");
        DemandEngine.getMyPublishDemandList(new OnGetMyPublishDemandListFinished());
    }

    public class OnGetMyPublishDemandListFinished implements BaseProtocol.IResultExecutor<String> {

        @Override
        public void execute(String dataBean) {
            ToastUtils.shortToast(dataBean);
        }

        @Override
        public void executeResultError(String result) {

        }
    }

    //似乎返回结果status都是为0？？
    public void servicePartyBidDemand(View v) {
//        DemandEngine.servicePartyBidDemand(new BaseProtocol.IResultExecutor<String>() {
//
//            @Override
//            public void execute(String dataBean) {
//                ToastUtils.shortToast(dataBean);
//            }
//
//            @Override
//            public void executeResultError(String result) {
//
//            }
//        }, "21", "10");

    }

    public void demandPartySelectServiceParty(View v) {
        DemandEngine.demandPartySelectServiceParty(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "21", "10001");
    }

    //六、[需求]-服务方确认一个服务者
    public void servicePartyConfirmServant(View v) {
        DemandEngine.servicePartyConfirmServant(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22");
    }

    //七、[需求]-查看需求流程日志
    public void getDemandFlowLog(View v) {
        DemandEngine.getDemandFlowLog(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22");
    }

    //八、[需求]-服务方拒绝
    public void servicePartyReject(View v) {
        DemandEngine.servicePartyReject(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22");
    }

    //九、[需求]-需求方预支付
    // 似乎无法使用跳过验证的方法，{"code":1,"data":{"message":"auth invalid."}}
    public void demandPartyPrePayment(View v) {
//        DemandEngine.demandPartyPrePayment(new BaseProtocol.IResultExecutor<String>() {
//            @Override
//            public void execute(String dataBean) {
//                ToastUtils.shortToast(dataBean);
//            }
//
//            @Override
//            public void executeResultError(String result) {
//
//            }
//        }, "22", "1300", "0");
    }

    //十、[需求]-我发布的历史需求列表
    public void getMyPublishHistoryDemandList(View v) {
        DemandEngine.getMyPublishHistoryDemandList(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "0", "20");
    }

    //十一、[需求]-服务方完成任务
    public void servicePartyComplete(View v) {
        DemandEngine.servicePartyComplete(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22", "1");
    }

    //十二、[需求]-服务方完成任务(应该是 需求方确认完成 ？？？)
    public void demandPartyConfirmComplete(View v) {
        DemandEngine.demandPartyConfirmComplete(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22", "4");
    }

    //十三、[需求]-需求方查看竞标（抢需求服务者）列表
    //Error 404 Not Found  （似乎还没有提供，是加入了跳过验证的参数后就404）
    public void demandPartyGetBidList(View v) {
        DemandEngine.demandPartyGetBidList(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22");
    }

    //十四、[需求]-加载需求描述信息
    //Error 404 Not Found 和上面一样的情况
    public void getDemandDesc(View v) {
        DemandEngine.getDemandDesc(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22");
    }

    //十五、[需求]-设置需求描述信息
    //Error 404 Not Found 和上面一样的情况
    public void setDemandDesc(View v) {
        DemandEngine.setDemandDesc(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22", "前端页面开发");
    }

    //一、[文件]-图片上传
    public void uploadFile(View v) {
        DemandEngine.uploadFile(new BaseProtocol.IResultExecutor<UploadFileResultBean>() {
            @Override
            public void execute(UploadFileResultBean dataBean) {
                ToastUtils.shortToast(dataBean.toString());
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "");
    }

    //二、[文件]-图片下载
    public void downloadFile(View v) {
        DemandEngine.downloadFile(new BaseProtocol.IResultExecutor<byte[]>() {
            @Override
            public void execute(byte[] dataBean) {
                ImageView iv = (ImageView) findViewById(R.id.iv_download_img);
                iv.setImageBitmap(BitmapFactory.decodeByteArray(dataBean, 0, dataBean.length));
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "group1/M00/00/00/eBtfY1gM2JmAa1SOAAJJOkiaAls.ac3597");
    }


    //BA认证测试
    public void testBA(View v) {
        ServiceEngine.getServiceDetail(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
            @Override
            public void execute(ServiceDetailBean dataBean) {
                LogKit.v("Service title:" + dataBean.data.service.title);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "88");
    }
}
