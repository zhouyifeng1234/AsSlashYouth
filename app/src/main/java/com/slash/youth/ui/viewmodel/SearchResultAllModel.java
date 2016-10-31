package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityResultAllBinding;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SearchAllProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/10/16.
 */
public class SearchResultAllModel extends BaseObservable {

    private ActivityResultAllBinding activityResultAllBinding;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private ArrayList<SearchAllBean.DataBean.DemandListBean> demandList;
    private ArrayList<?> serviceList;
    private ArrayList<SearchAllBean.DataBean.UserListBean> userList;
    private PagerSearchItemAdapter pagerSearchItemAdapter;
    private String text;
    private SearchNeedResultTabModel searchNeedResultTabModel;

    public SearchResultAllModel(ActivityResultAllBinding activityResultAllBinding ,String text) {
        this.activityResultAllBinding = activityResultAllBinding;
        this.text = text;
        initView();
        initData();
        listener();

    }
    //加载视图
    private void initView() {

    }

    //加载数据
    private void initData() {
        SearchAllProtocol searchAllProtocol = new SearchAllProtocol(text);
        searchAllProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SearchAllBean>() {
            @Override
            public void execute(SearchAllBean dataBean) {
                SearchAllBean.DataBean data = dataBean.getData();
                demandList = data.getDemandList();
                serviceList = data.getServiceList();
                userList = data.getUserList();
                pagerSearchItemAdapter = new PagerSearchItemAdapter(demandList, serviceList, userList, new PagerSearchItemAdapter.OnClickMoreListener() {
                    @Override
                    public void onClickMore(int btn) {
                       // mActivitySearchBinding.flSearchFirst.removeView(ActivitySearchModel.this.listView);
                        switch (btn){
                            case needMoreBtn://点击更多按钮，跳转到需求页面
                                SpUtils.setString("searchType","热搜需求");
                                break;
                            case serviceMoreBtn:
                                SpUtils.setString("searchType","热搜服务");
                                break;
                            case persionMoreBtn:
                                SpUtils.setString("searchType","搜人");
                                break;
                        }
                        currentActivity.activitySearchBinding.etActivitySearchAssociation.setText(null);
                        showMoreSearch();
                    }
                });
                activityResultAllBinding.lvSearchAll.setAdapter(pagerSearchItemAdapter);
            }
            @Override
            public void executeResultError(String result) {
                LogKit.d("executeResultError"+result);
            }
        });
    }

    //监听
    private void listener() {

    }

    private void showMoreSearch() {
        currentActivity.changeView(2);
        searchNeedResultTabModel = new SearchNeedResultTabModel(currentActivity.searchNeedResultTabBinding);
        currentActivity.searchNeedResultTabBinding.setSearchNeedResultTabModel(searchNeedResultTabModel);
    }


}
