package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.PullToRefreshListviewBinding;
import com.slash.youth.databinding.PullToRefreshTabListviewBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.domain.SearchServiceItemBean;
import com.slash.youth.domain.SearchUserItemBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.PagerHomeServiceAdapter;
import com.slash.youth.ui.adapter.PagerMoreDemandtAdapter;
import com.slash.youth.ui.adapter.PagerMoreServiceAdapter;
import com.slash.youth.ui.adapter.PagerSearchDemandtAdapter;
import com.slash.youth.ui.adapter.PagerSearchPersonAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/12/15.
 */
public class PullToRefreshListTabViewModel extends BaseObservable {
    private ArrayList<SearchUserItemBean.DataBean.ListBean> arryListUser  = new ArrayList<>();
    private ArrayList<SearchItemDemandBean.DataBean.ListBean>  arrayListDemand = new ArrayList<>();
    private ArrayList<SearchServiceItemBean.DataBean.ListBean>  arrayListService = new ArrayList<>();
    private PagerSearchPersonAdapter pagerSearchPersonAdapter;
    private PagerSearchDemandtAdapter pagerHomeDemandtAdapter;
    private  int offset = 0;
    private int limit = 20;
    public String tag;
    public int  pattern = -1;
    public   int isauth = -1;
    public String city;
    public int sort = 1;
    public int hotSort = 0;
    public double lat;//-180 到 180
    public double lng;//-90 到 90
    private int listsize;
    private String searchType;
    private PullToRefreshTabListviewBinding pullToRefreshTabListviewBinding;
    private SearchActivity currentActivity;
    private PagerHomeServiceAdapter pagerHomeServiceAdapter;

    public PullToRefreshListTabViewModel(PullToRefreshTabListviewBinding pullToRefreshTabListviewBinding,String searchType,SearchActivity currentActivity,String tag) {
         this.pullToRefreshTabListviewBinding =  pullToRefreshTabListviewBinding;
        this.searchType = searchType;
        this.tag = tag;
        this.currentActivity =currentActivity;
        initListView();
        initData();
        listener();
    }
    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = pullToRefreshTabListviewBinding.refreshView;
        ptrl.setOnRefreshListener(new SearchTabListListener());
    }

    private void initData() {
        getData(searchType);
    }

    public void clear(){
        if(arrayListDemand!=null){
            arrayListDemand.clear();
        }
       if(arrayListService!=null){
           arrayListService.clear();
       }
       if( arryListUser!=null){
           arryListUser.clear();
       }
    }


    public void getData(String searchType) {

        switch (searchType){
            case SearchManager.HOT_SEARCH_DEMEND:
                SearchManager.getSearchDemandList(new onGetSearchDemandList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                SearchManager.getSearchServiceList(new onGetSearchServiceList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case SearchManager.HOT_SEARCH_PERSON:
                SearchManager.getSearchUserList(new onGetSearchUserList(),tag,isauth,sort, offset,limit);
                break;
        }
    }

    //不同的条目点击事件
    private void listener() {
        pullToRefreshTabListviewBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchType = SpUtils.getString("searchType", "");
                    switch (searchType){
                        case SearchManager.HOT_SEARCH_DEMEND:
                            SearchItemDemandBean.DataBean.ListBean demandListBean = arrayListDemand.get(position);
                            long demandId = demandListBean.getId();
                            Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                            intentDemandDetailActivity.putExtra("demandId", demandId);
                            currentActivity.startActivity(intentDemandDetailActivity);
                            break;
                        case SearchManager.HOT_SEARCH_SERVICE:
                            SearchServiceItemBean.DataBean.ListBean serviceListBean = arrayListService.get(position);
                            long serviceId = serviceListBean.getId();
                            Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                            intentServiceDetailActivity.putExtra("serviceId", serviceId);
                            currentActivity.startActivity(intentServiceDetailActivity);
                            break;
                        case SearchManager.HOT_SEARCH_PERSON:
                            Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                            SearchUserItemBean.DataBean.ListBean listBean = arryListUser.get(position);
                            long uid = listBean.getUid();
                            intentUserInfoActivity.putExtra("Uid", uid);
                            currentActivity.startActivity(intentUserInfoActivity);
                            break;
                    }
            }
        });
    }

    public class SearchTabListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    arrayListDemand.clear();
                    arrayListService.clear();
                    arrayListService.clear();
                    getData(searchType);
                    if(pagerHomeDemandtAdapter!=null){
                        pagerHomeDemandtAdapter.notifyDataSetChanged();
                    }
                    if(pagerHomeServiceAdapter!=null){
                        pagerHomeServiceAdapter.notifyDataSetChanged();
                    }
                    if(pagerSearchPersonAdapter!=null){
                        pagerSearchPersonAdapter.notifyDataSetChanged();
                    }

                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 1000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //如果加载到最后一页，需要调用setLoadToLast()方法
                    if(listsize < limit){//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        getData(searchType);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if(pagerHomeDemandtAdapter!=null){
                        pagerHomeDemandtAdapter.notifyDataSetChanged();
                    }
                    if(pagerHomeServiceAdapter!=null){
                        pagerHomeServiceAdapter.notifyDataSetChanged();
                    }
                    if(pagerSearchPersonAdapter!=null){
                        pagerSearchPersonAdapter.notifyDataSetChanged();
                    }
                }
            }, 1000);
        }
    }

    //需求
    public class onGetSearchDemandList implements BaseProtocol.IResultExecutor<SearchItemDemandBean> {
        @Override
        public void execute(SearchItemDemandBean dataBean) {
            arrayListDemand.clear();
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchItemDemandBean.DataBean data = dataBean.getData();
                List<SearchItemDemandBean.DataBean.ListBean> list = data.getList();
                listsize= list.size();
                if(listsize == 0){
                    pullToRefreshTabListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    pullToRefreshTabListviewBinding.tvContent.setVisibility(View.VISIBLE);
                }else {
                    arrayListDemand.addAll(list);
                    pagerHomeDemandtAdapter = new PagerSearchDemandtAdapter(arrayListDemand);
                    pullToRefreshTabListviewBinding.lv.setAdapter(pagerHomeDemandtAdapter);
                    pullToRefreshTabListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }

                if(pagerHomeDemandtAdapter!=null){
                    pagerHomeDemandtAdapter.notifyDataSetChanged();
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //服务
    public class onGetSearchServiceList implements BaseProtocol.IResultExecutor<SearchServiceItemBean> {
        @Override
        public void execute(SearchServiceItemBean dataBean) {
            arrayListService.clear();
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchServiceItemBean.DataBean data = dataBean.getData();
                List<SearchServiceItemBean.DataBean.ListBean> list = data.getList();
                listsize= list.size();
                if(listsize == 0){
                    pullToRefreshTabListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    pullToRefreshTabListviewBinding.tvContent.setVisibility(View.VISIBLE);
                }else {
                    arrayListService.addAll(list);
                    pagerHomeServiceAdapter = new PagerHomeServiceAdapter(arrayListService);
                    pullToRefreshTabListviewBinding.lv.setAdapter(pagerHomeServiceAdapter);
                    pullToRefreshTabListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }

                if(pagerHomeServiceAdapter!=null){
                    pagerHomeServiceAdapter.notifyDataSetChanged();
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //搜人
    public class onGetSearchUserList implements BaseProtocol.IResultExecutor<SearchUserItemBean> {
        @Override
        public void execute(SearchUserItemBean dataBean) {
            arryListUser.clear();
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchUserItemBean.DataBean data = dataBean.getData();
                List<SearchUserItemBean.DataBean.ListBean> list = data.getList();
                listsize= list.size();
                if(listsize == 0){
                    pullToRefreshTabListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    pullToRefreshTabListviewBinding.tvContent.setVisibility(View.VISIBLE);
                }else {
                arryListUser.addAll(list);
                pagerSearchPersonAdapter = new PagerSearchPersonAdapter(arryListUser);
                pullToRefreshTabListviewBinding.lv.setAdapter(pagerSearchPersonAdapter);
                pullToRefreshTabListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }

             if(pagerSearchPersonAdapter!=null){
                 pagerSearchPersonAdapter.notifyDataSetChanged();
             }

            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
