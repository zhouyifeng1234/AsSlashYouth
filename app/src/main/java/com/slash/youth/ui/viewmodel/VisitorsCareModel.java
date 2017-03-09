package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityContactsCareBinding;
import com.slash.youth.databinding.ActivityVisitorsBinding;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.activity.VisitorsActivity;
import com.slash.youth.ui.adapter.ContactsCareAdapter;
import com.slash.youth.ui.adapter.HomeContactsVisitorAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import static com.slash.youth.R.id.rl;
import static com.slash.youth.ui.viewmodel.PagerHomeContactsModel.getBaseDataFinishedCount;
import static com.slash.youth.ui.viewmodel.PagerHomeContactsModel.getBaseDataTotalCount;

/**
 * Created by acer on 2016/11/20.
 */
public class VisitorsCareModel extends BaseObservable {
    private ActivityVisitorsBinding activityContactsCareBinding;
    private String title;
    private ArrayList<HomeContactsVisitorBean.DataBean.ListBean> contactsLists = new ArrayList<>();
    private HomeContactsVisitorAdapter homeContactsVisitorAdapter;
    private int offset = 0;
    private int limit = 20;
    private int visibleLastIndex;
    private int type = -1;
    private boolean isAgree;
    private int i = -1;
    private VisitorsActivity contactsCareActivity;
    private int listSize;
    private int titleType;
    public static int getBaseDataFinishedCount = 0;
    public static int getBaseDataTotalCount = 0;

    public VisitorsCareModel(ActivityVisitorsBinding activityContactsCareBinding, String title, VisitorsActivity contactsCareActivity) {
        this.activityContactsCareBinding = activityContactsCareBinding;
        this.contactsCareActivity = contactsCareActivity;
        this.title = title;
        initListView();
        initData();
        initView();
        listener();
    }

    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = activityContactsCareBinding.refreshView;
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
                    if (listSize < limit) {//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    } else {//不是最后一页
                        offset += limit;
                        getDataFromServer();
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if (homeContactsVisitorAdapter != null) {
                        homeContactsVisitorAdapter.notifyDataSetChanged();
                    }

                }
            }, 2000);
        }
    }

    private void initData() {
        getDataFromServer();
    }

    public void getDataFromServer() {
        getBaseDataTotalCount++;
        ContactsManager.getMyVisitorList(new onGetMyVisitorList(), offset, limit);
    }

    private void initView() {
        activityContactsCareBinding.tvContactsTitle.setText(title);

    }

    //获取我的访客的列表
    public class onGetMyVisitorList implements BaseProtocol.IResultExecutor<HomeContactsVisitorBean> {
        @Override
        public void execute(HomeContactsVisitorBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                //隐藏加载页面
                getBaseDataFinishedCount++;
                if (getBaseDataFinishedCount >= getBaseDataTotalCount) {
                    activityContactsCareBinding.ivNodata.setVisibility(View.GONE);
                    activityContactsCareBinding.tvTitle.setVisibility(View.GONE);
                    activityContactsCareBinding.tvContent.setVisibility(View.GONE);
                }
                HomeContactsVisitorBean.DataBean data = dataBean.getData();
                List<HomeContactsVisitorBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                contactsLists.addAll(list);
                //访客列表没有数据
                if (listSize == 0) {
                    activityContactsCareBinding.ivNodata.setVisibility(View.VISIBLE);
                    activityContactsCareBinding.tvTitle.setVisibility(View.VISIBLE);
                    activityContactsCareBinding.tvContent.setVisibility(View.VISIBLE);
                    activityContactsCareBinding.tvTitle.setText("暂无访客");
                    activityContactsCareBinding.tvContent.setText("你还没有访客哦!");
                }
            }
            //设置访客列表数据
            homeContactsVisitorAdapter = new HomeContactsVisitorAdapter(contactsLists);
            activityContactsCareBinding.lv.setAdapter(homeContactsVisitorAdapter);
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private void listener() {
        //条目点击
        activityContactsCareBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case 1://关注我
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_FOCUS_MINE_CLICK_PERSON_DETAIL);
                        break;
                    case 2://我关注的
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_MY_FOCUS_CLICK_PERSON_DETAIL);
                        break;
                    case 3://加我的
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ADD_ME_CLICK_PERSON_DETAIL);
                        break;
                    case 4://我加的
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ME_ADD_CLICK_PERSON_DETAIL);
                        break;
                }

                HomeContactsVisitorBean.DataBean.ListBean listBean = contactsLists.get(position);
                long uid = listBean.getUid();
                Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                intentUserInfoActivity.putExtra("Uid", uid);
                contactsCareActivity.startActivity(intentUserInfoActivity);
            }
        });
    }

//    public class onGetAddMeList implements BaseProtocol.IResultExecutor<ContactsBean> {
//        @Override
//        public void execute(ContactsBean dataBean) {
//            int rescode = dataBean.getRescode();
//            if(rescode == 0){
//                ContactsBean.DataBean data = dataBean.getData();
//                List<ContactsBean.DataBean.ListBean> list = data.getList();
//                listSize = list.size();
//                activityContactsCareBinding.tvTitle.setVisibility(View.VISIBLE);
//                switch (type){
//                    case 1:
//                        activityContactsCareBinding.tvTitle.setText("暂无关注");
//                        activityContactsCareBinding.tvContent.setText("你还没有关注过任何人哦，赶紧去关注吧");
//                        break;
//                    case 2:
//                        activityContactsCareBinding.tvTitle.setText("暂无粉丝");
//                        activityContactsCareBinding.tvContent.setText("竟然一个粉丝都没有，快出去喊喊口号");
//                        break;
//                    case 3:
//                        activityContactsCareBinding.tvTitle.setText("暂无好友申请");
//                        activityContactsCareBinding.tvContent.setText("快去寻觅自己的好友吧");
//                        break;
//                    case 4:
//                        activityContactsCareBinding.tvTitle.setText("还未发出过好友申请");
//                        activityContactsCareBinding.tvContent.setText("呜呜，这么多优秀的人你都看不到");
//                        break;
//                }
//                activityContactsCareBinding.rlHomeDefaultImage.setVisibility(listSize == 0?View.VISIBLE:View.GONE);
//
//                if(listSize!=0){
//                    contactsLists.addAll(list);
//                    homeContactsVisitorAdapter = new HomeContactsVisitorAdapter(contactsLists);
//                    activityContactsCareBinding.lv.setAdapter(contactsCareAdapter);
//                }
//            }
//        }
//        @Override
//        public void executeResultError(String result) {
//            LogKit.d("result:"+result);
//        }
//    }
}
