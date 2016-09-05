package com.slash.youth.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.slash.youth.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        System.out.println("Start Slash Youth");
    }

//    public void getData(View v) {
//
//        BaseProtocol bp = new GetUserSkillLabelProtocol();
//        bp.getDataFromServer(new BaseProtocol.IResultExecutor() {
//            @Override
//            public void execute(Object dataBean) {
//                ToastUtils.shortToast("Success");
//            }
//
//            @Override
//            public void executeError(ResultErrorBean resultErrorBean) {
//                ToastUtils.shortToast(resultErrorBean.code + "\n" + resultErrorBean.data.message);
//            }
//        });
//    }
}
