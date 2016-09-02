package com.slash.youth.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.http.protocol.BaseProtocol;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Start Slash Youth");
    }

    public void getData(View v) {

        BaseProtocol bp = new BaseProtocol();
        bp.getDataFromServer();
    }
}
