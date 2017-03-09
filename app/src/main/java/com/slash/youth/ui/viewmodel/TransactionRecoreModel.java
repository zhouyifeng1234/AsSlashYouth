package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.slash.youth.databinding.ActivityTransactionRecordBinding;
import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.AccountManager;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.TransactionRecoreProtocol;
import com.slash.youth.ui.adapter.ListViewAdapter;
import com.slash.youth.ui.adapter.RecordAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
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
    private int listSize;


    public TransactionRecoreModel(ActivityTransactionRecordBinding activityTransactionRecordBinding) {
        this.activityTransactionRecordBinding = activityTransactionRecordBinding;
        initListView();
        initData();
        listener();
    }


    private void initListView() {
        PullToRefreshLayout ptrl = activityTransactionRecordBinding.refreshView;
        ptrl.setOnRefreshListener(new TransactionRecoreListListener());
    }

    private void initData() {
        AccountManager.getTransactionRecore(new onGetTransactionRecore(),offset,limit);
    }

    //获取流水数据
    public class onGetTransactionRecore implements BaseProtocol.IResultExecutor<TransactionRecoreBean> {
        @Override
        public void execute(TransactionRecoreBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                TransactionRecoreBean.DataBean data = dataBean.getData();
                List<TransactionRecoreBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                if(listSize == 0){
                    activityTransactionRecordBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                }else {
                    arrayList.addAll(list);
                    recordAdapter = new RecordAdapter(arrayList);
                    activityTransactionRecordBinding.lv.setAdapter(recordAdapter);
                    activityTransactionRecordBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }
                if(listSize == 0){
                    activityTransactionRecordBinding.refreshView.setVisibility(View.GONE);
                }
            }else {
                LogKit.d("rescode: "+rescode);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //上拉下拉
    public class TransactionRecoreListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    arrayList.clear();
                    AccountManager.getTransactionRecore(new onGetTransactionRecore(),offset,limit);
                    if(recordAdapter!=null){
                        recordAdapter.notifyDataSetChanged();
                    }
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 2000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //如果加载到最后一页，需要调用setLoadToLast()方法
                    if(listSize < limit){//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        AccountManager.getTransactionRecore(new onGetTransactionRecore(),offset,limit);
                        if(recordAdapter!=null){
                            recordAdapter.notifyDataSetChanged();
                        }
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }, 2000);
        }
    }

    private void listener() {

    }
}
