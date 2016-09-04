package com.slash.youth.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.ResultErrorBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.GetUserSkillLabelProtocol;
import com.slash.youth.utils.ToastUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Start Slash Youth");
    }

    public void getData(View v) {

        BaseProtocol bp = new GetUserSkillLabelProtocol();
        bp.getDataFromServer(new BaseProtocol.IResultExecutor() {
            @Override
            public void execute(Object dataBean) {
                ToastUtils.shortToast("Success");
            }

            @Override
            public void executeError(ResultErrorBean resultErrorBean) {
                ToastUtils.shortToast(resultErrorBean.code + "\n" + resultErrorBean.data.message);
            }
        });
    }
}
