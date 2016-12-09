package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityResultAllBinding;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SearchAllProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.ui.adapter.SearchAllAdapter;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.ui.adapter.SearchResultAlAdapter;
import com.slash.youth.ui.adapter.SearchResultDemandAdapter;
import com.slash.youth.ui.adapter.SearchResultServiceAdapter;
import com.slash.youth.ui.adapter.SearchResultUserAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/10/16.
 */
public class SearchResultAllModel extends BaseObservable {

    private ActivityResultAllBinding activityResultAllBinding;
    private String text;

    private  ArrayList<SearchAllBean.DataBean.DemandListBean> demandListBeen = new ArrayList<>();
    private  ArrayList<SearchAllBean.DataBean.ServiceListBean> serviceListBeen = new ArrayList<>();
    private ArrayList<SearchAllBean.DataBean.UserListBean> userListBeen = new ArrayList<>();

    public SearchResultAllModel(ActivityResultAllBinding activityResultAllBinding ,String text) {
        this.activityResultAllBinding = activityResultAllBinding;
        this.text = text;
        initData();
    }

    //加载数据
    private void initData() {
        SearchManager.getSearchAll(new onGetSearchAll(),text);
    }

    public class onGetSearchAll implements BaseProtocol.IResultExecutor<SearchAllBean> {
        @Override
        public void execute(SearchAllBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchAllBean.DataBean data = dataBean.getData();

                List<SearchAllBean.DataBean.DemandListBean> demandList = data.getDemandList();
                demandListBeen.addAll(demandList);

                List<SearchAllBean.DataBean.ServiceListBean> serviceList = data.getServiceList();
                serviceListBeen.addAll(serviceList);

                List<SearchAllBean.DataBean.UserListBean> userList = data.getUserList();
                userListBeen.addAll(userList);


                SearchResultAlAdapter searchResultAlAdapter = new SearchResultAlAdapter(demandListBeen,serviceListBeen,userListBeen);
                activityResultAllBinding.lvSearchAll.setAdapter(searchResultAlAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
