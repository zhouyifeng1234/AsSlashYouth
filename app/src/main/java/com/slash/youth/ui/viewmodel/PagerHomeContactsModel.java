package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewHomeContactsBinding;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.ContactsMyVisitorProtocol;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.HomeContactsVisitorAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.ui.view.WarpLinearLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeContactsModel extends BaseObservable {
    private PagerHomeContactsBinding mPagerHomeContactsBinding;
    private Activity mActivity;
    private int offset = 0;
    private int limit = 20;
    private  ArrayList<HomeContactsVisitorBean.DataBean.ListBean> listHomeContactsVisitorBean = new ArrayList<>();
    private HomeContactsVisitorAdapter homeContactsVisitorAdapter;
    private int listSize;
    private HeaderListviewHomeContactsBinding headerListviewHomeContactsBinding;
    private int updatePager = View.GONE;
    private HeaderHomeContactsModel headerHomeContactsModel;

    public static int getBaseDataFinishedCount = 0;
    public static int getBaseDataTotalCount = 0;
    private ActivityHomeModel activityHomeModel;

    public PagerHomeContactsModel(PagerHomeContactsBinding pagerHomeContactsBinding, Activity activity,ActivityHomeModel activityHomeModel) {
        this.mPagerHomeContactsBinding = pagerHomeContactsBinding;
        this.mActivity = activity;
        this.activityHomeModel =activityHomeModel;
      //  displayLoadLayer();
        initListView();
        initView();
        initData();
        listener();
    }

    //显示加载页面
    private void displayLoadLayer() {
        setUpdatePager(View.VISIBLE);
    }

    //隐藏加载页面
    public void hideLoadLayer() {
        setUpdatePager(View.GONE);
    }

    @Bindable
    public int getUpdatePager() {
        return updatePager;
    }

    public void setUpdatePager(int updatePager) {
        this.updatePager = updatePager;
        notifyPropertyChanged(BR.updatePager);
    }

    private void initListView() {
        PullToRefreshLayout ptrl = mPagerHomeContactsBinding.refreshView;
        ptrl.setOnRefreshListener(new VisitorListListener());
    }

    public class VisitorListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

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
                        getDataFromServer();
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if(homeContactsVisitorAdapter!=null){
                        homeContactsVisitorAdapter.notifyDataSetChanged();
                    }

                }
            }, 2000);
        }
    }

    private void initView() {
        headerListviewHomeContactsBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.header_listview_home_contacts, null, false);
        headerHomeContactsModel = new HeaderHomeContactsModel(headerListviewHomeContactsBinding,mActivity,activityHomeModel);
        headerListviewHomeContactsBinding.setHeaderHomeContactsModel(headerHomeContactsModel);
        View vContactsHeader = headerListviewHomeContactsBinding.getRoot();
        mPagerHomeContactsBinding.lvHomeContactsVisitor.addHeaderView(vContactsHeader);
    }

    public void initData() {
        getDataFromServer();
    }

    public void getDataFromServer() {
        getBaseDataTotalCount++;
        ContactsManager.getMyVisitorList(new onGetMyVisitorList(),offset,limit);
    }

    private void listener() {
        //条目点击事件
        mPagerHomeContactsBinding.lvHomeContactsVisitor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                HomeContactsVisitorBean.DataBean.ListBean listBean = listHomeContactsVisitorBean.get(position-1);
                String name = listBean.getName();
                long mId = listBean.getUid();
                Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                intentUserInfoActivity.putExtra("Uid",mId);
                mActivity.startActivity(intentUserInfoActivity);
                }
            }
        });

        headerHomeContactsModel.setOnLoadDataListener(new HeaderHomeContactsModel.OnLoadDataListener() {
            @Override
            public void OnRecommendLoadData(boolean MyRecommendFriendRecode) {
                setUpdatePager(MyRecommendFriendRecode?View.GONE:View.VISIBLE);
            }

            @Override
            public void OnRelationLoadData(boolean personRelationRecode) {
                setUpdatePager(personRelationRecode?View.GONE:View.VISIBLE);
            }
        });
    }

    //获取我的访客的列表
    public class onGetMyVisitorList implements BaseProtocol.IResultExecutor<HomeContactsVisitorBean> {
        @Override
        public void execute(HomeContactsVisitorBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                //隐藏加载页面
                getBaseDataFinishedCount++;
                if (getBaseDataFinishedCount >= getBaseDataTotalCount) {
                    hideLoadLayer();
                }
                HomeContactsVisitorBean.DataBean data = dataBean.getData();
                List<HomeContactsVisitorBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                listHomeContactsVisitorBean.addAll(list);
                //访客列表没有数据
                if(listSize == 0){
                    View rl = mPagerHomeContactsBinding.more.findViewById(R.id.rl_progress);
                    rl.setVisibility(View.GONE);
                    headerListviewHomeContactsBinding.tvNone.setVisibility(View.VISIBLE);
                }
            }
            //设置访客列表数据
            homeContactsVisitorAdapter = new HomeContactsVisitorAdapter(listHomeContactsVisitorBean);
            mPagerHomeContactsBinding.lvHomeContactsVisitor.setAdapter(homeContactsVisitorAdapter);
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //点击右上角的搜索按钮
    public void search(View view){
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_SEARCH);

        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        intentSearchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSearchActivity);
    }
}
