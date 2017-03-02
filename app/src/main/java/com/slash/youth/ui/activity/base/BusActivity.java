package com.slash.youth.ui.activity.base;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by acer on 2017/3/1.
 */

public class BusActivity extends BaseActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
}
