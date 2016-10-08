package com.slash.youth.ui.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.slash.youth.R;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/30.
 */
public class TestActivity extends Activity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void publishDemand(View v) {
        DemandEngine.publishDemand(new BaseProtocol.IResultExecutor<String>() {

            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
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
        DemandEngine.servicePartyBidDemand(new BaseProtocol.IResultExecutor<String>() {

            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "21", "10");

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
        }, "22", "10001");
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
        DemandEngine.demandPartyPrePayment(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, "22");
    }
}
