package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemServiceFlowLogBinding;
import com.slash.youth.domain.CommonLogList;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/11/13.
 */
public class ItemServiceLogModel extends BaseObservable {

    ItemServiceFlowLogBinding mItemServiceFlowLogBinding;
    Activity mActivity;
    CommonLogList.CommonLogInfo mLogInfo;

    public ItemServiceLogModel(ItemServiceFlowLogBinding itemServiceFlowLogBinding, Activity activity, CommonLogList.CommonLogInfo logInfo) {
        this.mActivity = activity;
        this.mItemServiceFlowLogBinding = itemServiceFlowLogBinding;
        this.mLogInfo = logInfo;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH:mm");
        String timeStr = sdf.format(mLogInfo.cts);
        mItemServiceFlowLogBinding.tvLogRecordTime.setText(timeStr);

        int splitIndex = mLogInfo.log.indexOf(',');
        if (splitIndex != -1) {
            String log = mLogInfo.log.substring(splitIndex + 1, mLogInfo.log.length());
            mItemServiceFlowLogBinding.tvLogAction.setText(log);
        } else {
            mItemServiceFlowLogBinding.tvLogAction.setText(mLogInfo.log);
        }
    }
}
