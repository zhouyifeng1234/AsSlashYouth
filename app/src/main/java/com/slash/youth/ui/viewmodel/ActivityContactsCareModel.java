package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.sip.SipSession;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityContactsCareBinding;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.domain.RecommendFriendBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.ContactsCareAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2016/11/20.
 */
public class ActivityContactsCareModel extends BaseObservable {
    private ActivityContactsCareBinding activityContactsCareBinding;
    private String title;
    private ArrayList<ContactsBean.DataBean.ListBean> contactsLists = new ArrayList<>();
    private ContactsCareAdapter contactsCareAdapter;
    private int offset = 0;
    private int limit  = 20;
    private int visibleLastIndex;
    private int type =-1;
    private boolean isAgree;
    private int i = -1;
    private  ContactsCareActivity contactsCareActivity;
    private int listSize;
    private int titleType;

    public ActivityContactsCareModel(ActivityContactsCareBinding activityContactsCareBinding, String title, ContactsCareActivity contactsCareActivity,int type) {
        this.activityContactsCareBinding = activityContactsCareBinding;
        this.contactsCareActivity = contactsCareActivity;
        this.title = title;
        this.titleType = type;
        initListView();
        initData();
        initView();
        listener();
    }
    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = activityContactsCareBinding.refreshView;
        ptrl.setOnRefreshListener(new ActivityContactsCareListListener());
    }

    public class ActivityContactsCareListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    contactsLists.clear();
                    getData( title);
                    if(contactsCareAdapter!=null){
                        contactsCareAdapter.notifyDataSetChanged();
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
                    if(listSize < limit){
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        getData( title);
                        if(contactsCareAdapter!=null){
                            contactsCareAdapter.notifyDataSetChanged();
                        }
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }, 2000);
        }
    }

    private void initData() {
        getData( title);
    }

    private void getData(String title) {
        switch (title){
            case ContactsManager.CARE_ME:
                type=1;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_CARE_PERSON);
                break;
            case ContactsManager.MY_CARE:
                type=2;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.CARE_ME_PERSON);
                break;
            case ContactsManager.ADD_ME:
                type = 3;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_ADD_ME_LIST);
                break;
            case ContactsManager.MY_ADD:
                type = 4;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_MY_ADD_LIST);
                break;
        }
    }

    private void initView() {
        activityContactsCareBinding.tvContactsTitle.setText(title);

    }

    private void listener() {
        //条目点击
        activityContactsCareBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
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

                ContactsBean.DataBean.ListBean listBean = contactsLists.get(position);
                long uid = listBean.getUid();
                Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                intentUserInfoActivity.putExtra("Uid", uid);
                contactsCareActivity.startActivity(intentUserInfoActivity);
            }
        });
    }

    public class onGetAddMeList implements BaseProtocol.IResultExecutor<ContactsBean> {
        @Override
        public void execute(ContactsBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                ContactsBean.DataBean data = dataBean.getData();
                List<ContactsBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                activityContactsCareBinding.tvTitle.setVisibility(View.VISIBLE);
                switch (type){
                    case 1:
                        activityContactsCareBinding.tvTitle.setText("暂无关注");
                        activityContactsCareBinding.tvContent.setText("你还没有关注过任何人哦，赶紧去关注吧");
                        break;
                    case 2:
                        activityContactsCareBinding.tvTitle.setText("暂无粉丝");
                        activityContactsCareBinding.tvContent.setText("竟然一个粉丝都没有，快出去喊喊口号");
                        break;
                    case 3:
                        activityContactsCareBinding.tvTitle.setText("暂无好友申请");
                        activityContactsCareBinding.tvContent.setText("快去寻觅自己的好友吧");
                        break;
                    case 4:
                        activityContactsCareBinding.tvTitle.setText("还未发出过好友申请");
                        activityContactsCareBinding.tvContent.setText("呜呜，这么多优秀的人你都看不到");
                        break;
                }
                activityContactsCareBinding.rlHomeDefaultImage.setVisibility(listSize == 0?View.VISIBLE:View.GONE);

                if(listSize!=0){
                    contactsLists.addAll(list);
                    contactsCareAdapter = new ContactsCareAdapter(contactsLists,type);
                    activityContactsCareBinding.lv.setAdapter(contactsCareAdapter);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
