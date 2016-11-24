package com.slash.youth.domain;

import android.view.View;

/**
 * Created by zhouyifeng on 2016/11/24.
 */
public class SendMessageBean {

    public long sendTime;

    public View vReadStatus;

    public SendMessageBean(long sendTime, View vReadStatus) {
        this.sendTime = sendTime;
        this.vReadStatus = vReadStatus;
    }
}
