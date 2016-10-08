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
}
