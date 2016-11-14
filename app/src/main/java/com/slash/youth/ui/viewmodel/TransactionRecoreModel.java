package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.slash.youth.databinding.ActivityTransactionRecordBinding;
import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.TransactionRecoreProtocol;
import com.slash.youth.ui.adapter.ListViewAdapter;
import com.slash.youth.ui.adapter.RecordAdapter;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zss on 2016/11/6.
 */
public class TransactionRecoreModel extends BaseObservable {
    private ActivityTransactionRecordBinding activityTransactionRecordBinding;
    private RecordAdapter recordAdapter;
    private  int offset = 0;//默认从0开始
    private int limit = 20;//每一页最多展示20条
    private  ArrayList<TransactionRecoreBean.DataBean.ListBean> arrayList = new ArrayList<>();


    public TransactionRecoreModel(ActivityTransactionRecordBinding activityTransactionRecordBinding) {
        this.activityTransactionRecordBinding = activityTransactionRecordBinding;
        initData();
        initView();
    }

    private void initView() {
        activityTransactionRecordBinding.lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        //弹一个进度条，等加载完成，显示
                        offset = view.getCount();
                         getListData(offset,limit);
                        recordAdapter.notifyDataSetChanged();
                        activityTransactionRecordBinding.lv.setSelection(view.getCount());

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initData() {
        getListData(offset,limit);
    }

    private void getListData(int offset,int limit) {
        TransactionRecoreProtocol transactionRecoreProtocol = new TransactionRecoreProtocol(offset,limit);
        transactionRecoreProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<TransactionRecoreBean>() {
            @Override
            public void execute(TransactionRecoreBean dataBean) {
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    TransactionRecoreBean.DataBean data = dataBean.getData();
                    List<TransactionRecoreBean.DataBean.ListBean> list = data.getList();
                    arrayList.addAll(list);
                    recordAdapter = new RecordAdapter(arrayList);
                    activityTransactionRecordBinding.lv.setAdapter(recordAdapter);

                }else {
                    LogKit.d("rescode: "+rescode);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

}
