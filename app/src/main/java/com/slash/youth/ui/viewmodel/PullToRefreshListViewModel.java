package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.slash.youth.databinding.PullToRefreshListviewBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.adapter.PagerMoreDemandtAdapter;
import com.slash.youth.ui.adapter.PagerMoreServiceAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/12/15.
 */
public class PullToRefreshListViewModel extends BaseObservable {
    private PullToRefreshListviewBinding pullToRefreshListviewBinding;
    private ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean> arrayListDemand = new ArrayList<>();
    private ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean>  arrayListService = new ArrayList<>();
    private PagerMoreDemandtAdapter pagerHomeDemandtAdapter;
    private boolean isDemand;
    private  int offset = 0;
    private int limit = 20;
    public String tag = "";
    public int  pattern = -1;
    public   int isauth = -1;
    public String city = null;
    public int sort = -1;
    public double lat = 181;//-180 到 180
    public double lng = 91;//-90 到 90
    private FirstPagerMoreActivity firstPagerMoreActivity;
    private int listsize;
    private SearchActivity currentActivity;
    private String searchType;
    private PagerMoreServiceAdapter pagerHomeServiceAdapter;

    public PullToRefreshListViewModel(PullToRefreshListviewBinding pullToRefreshListviewBinding,boolean isDemand,FirstPagerMoreActivity firstPagerMoreActivity) {
        this.pullToRefreshListviewBinding = pullToRefreshListviewBinding;
        this.firstPagerMoreActivity = firstPagerMoreActivity;
        this.isDemand = isDemand;
        initListView();
        initData();
        listener();
    }
    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = pullToRefreshListviewBinding.refreshView;
        ptrl.setOnRefreshListener(new FreeTimeListListener());
    }

    private void initData() {
        getData(isDemand);
    }

    public void getData(boolean isDemand) {
        if(isDemand){
             FirstPagerManager.onFreeTimeMoreDemandList(new onFreeTimeMoreDemandList(),pattern,  isauth,  city,  sort, lng, lat, offset,limit);
        }else {
             FirstPagerManager.onFreeTimeMoreServiceList(new onFreeTimeMoreServiceList(),tag,  pattern,  isauth,  city,  sort, lng, lat, offset,limit);
        }
    }

    //不同的条目点击事件
    private void listener() {
        pullToRefreshListviewBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(isDemand){
                   FreeTimeMoreDemandBean.DataBean.ListBean listBean = arrayListDemand.get(position);
                   long demandId = listBean.getId();
                   Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                   intentDemandDetailActivity.putExtra("demandId", demandId);
                   firstPagerMoreActivity.startActivity(intentDemandDetailActivity);

               } else {
                   FreeTimeMoreServiceBean.DataBean.ListBean listBean = arrayListService.get(position);
                   long serviceId = listBean.getId();
                   Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                   intentServiceDetailActivity.putExtra("serviceId", serviceId);
                   firstPagerMoreActivity.startActivity(intentServiceDetailActivity);
               }
            }
        });
    }

    public class FreeTimeListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    arrayListDemand.clear();
                    arrayListService.clear();
                    getData(isDemand);
                    if(pagerHomeDemandtAdapter!=null){
                        pagerHomeDemandtAdapter.notifyDataSetChanged();
                    }

                    if(pagerHomeServiceAdapter!=null){
                        pagerHomeServiceAdapter.notifyDataSetChanged();
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
                    if(listsize < limit){//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        getData(isDemand);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if(pagerHomeDemandtAdapter!=null){
                        pagerHomeDemandtAdapter.notifyDataSetChanged();
                    }

                    if(pagerHomeServiceAdapter!=null){
                        pagerHomeServiceAdapter.notifyDataSetChanged();
                    }
                }
            }, 2000);
        }
    }

    //服务
    public class onFreeTimeMoreServiceList implements BaseProtocol.IResultExecutor<FreeTimeMoreServiceBean> {
        @Override
        public void execute(FreeTimeMoreServiceBean data) {
            int rescode = data.getRescode();
            if(rescode == 0){
                FreeTimeMoreServiceBean.DataBean dataBean  = data.getData();
                List<FreeTimeMoreServiceBean.DataBean.ListBean> list = dataBean.getList();
                listsize = list.size();
                if( listsize == 0){
                    pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    pullToRefreshListviewBinding.tvContent.setVisibility(View.GONE);
                }else {
                    arrayListService.addAll(list);
                    pagerHomeServiceAdapter = new PagerMoreServiceAdapter(arrayListService,firstPagerMoreActivity);
                    pullToRefreshListviewBinding.lv.setAdapter(pagerHomeServiceAdapter);
                    pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //需求
    public class onFreeTimeMoreDemandList implements BaseProtocol.IResultExecutor<FreeTimeMoreDemandBean> {
        @Override
        public void execute(FreeTimeMoreDemandBean data) {
            int rescode = data.getRescode();
            if(rescode == 0){
                FreeTimeMoreDemandBean.DataBean dataBean = data.getData();
                List<FreeTimeMoreDemandBean.DataBean.ListBean> list = dataBean.getList();
                listsize = list.size();
                if(listsize == 0){
                    pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    pullToRefreshListviewBinding.tvContent.setVisibility(View.GONE);
                }else {
                    arrayListDemand.addAll(list);
                    pagerHomeDemandtAdapter = new PagerMoreDemandtAdapter(arrayListDemand,firstPagerMoreActivity);
                    pullToRefreshListviewBinding.lv.setAdapter(pagerHomeDemandtAdapter);
                    pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
