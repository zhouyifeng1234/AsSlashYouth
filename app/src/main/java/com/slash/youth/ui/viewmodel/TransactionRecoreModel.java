package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityTransactionRecordBinding;
import com.slash.youth.domain.RecordBean;
import com.slash.youth.ui.adapter.RecordAdapter;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/6.
 */
public class TransactionRecoreModel extends BaseObservable {
    private ActivityTransactionRecordBinding activityTransactionRecordBinding;
    private  ArrayList<RecordBean> recordList = new ArrayList<>();


    public TransactionRecoreModel(ActivityTransactionRecordBinding activityTransactionRecordBinding) {
        this.activityTransactionRecordBinding = activityTransactionRecordBinding;
        initData();
        initView();
    }

    private void initView() {

        RecordAdapter recordAdapter = new RecordAdapter(recordList);
         activityTransactionRecordBinding.lv.setAdapter(recordAdapter);

    }

    private void initData() {
    //收支记录存在集合里

        recordList.add(new RecordBean());
        recordList.add(new RecordBean());
        recordList.add(new RecordBean());
        recordList.add(new RecordBean());



    }
}
