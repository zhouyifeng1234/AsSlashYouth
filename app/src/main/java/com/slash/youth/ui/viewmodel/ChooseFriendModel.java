package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityChooseFriendBinding;
import com.slash.youth.domain.ChatCmdBusinesssCardBean;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.ChooseFriendAdapter;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.event.MessageEvent;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseFriendModel extends BaseObservable {
    private ActivityChooseFriendBinding activityChooseFriendBinding;
    private ArrayList<MyFriendListBean.DataBean.ListBean> friendArrayList = new ArrayList<>();
    private ArrayList<Character> letterList = new ArrayList<>();
    private LocationCityFirstLetterAdapter locationCityFirstLetterAdapter;
    private ChooseFriendAdapter chooseFriendAdapter;
    private MyFriendActivtiy chooseFriendActivtiy;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int offset = 0;
    private int limit = 20;
    private int listSize;
    private boolean sendFriend;
    Activity mActivity;
    ChatCmdShareTaskBean chatCmdShareTaskBean;
    private int sendFriendDialogVisibility = View.GONE;
    private String sendName;
    private long targetId;

    private int addMeFriendLocalCount;
    int addMeFriendCount;

    public ChooseFriendModel(Activity activity, ActivityChooseFriendBinding activityChooseFriendBinding, MyFriendActivtiy chooseFriendActivtiy, boolean sendFriend) {
        this.activityChooseFriendBinding = activityChooseFriendBinding;
        this.chooseFriendActivtiy = chooseFriendActivtiy;
        this.sendFriend = sendFriend;
        this.mActivity = activity;
        initListView();
        initData();
        initView();
        listener();
    }

    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = activityChooseFriendBinding.refreshView;
        ptrl.setOnRefreshListener(new ChooseFriendListListener());
    }

    //首页展示的数据
    public class onPersonRelationFirstPage implements BaseProtocol.IResultExecutor<PersonRelationBean> {
        @Override
        public void execute(PersonRelationBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                PersonRelationBean.DataBean data = dataBean.getData();
                PersonRelationBean.DataBean.InfoBean info = data.getInfo();
                addMeFriendCount = info.getAddMeFriendCount();

                if (addMeFriendLocalCount != addMeFriendCount) {
                    activityChooseFriendBinding.ivRequest.setVisibility(View.VISIBLE);
                }
                //访客接口还没有
//                if (myAddFriendLocalCount != myAddFriendCount) {
//                    mPagerHomeMyBinding.ivMineVisitors.setVisibility(View.VISIBLE);
//                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    public class ChooseFriendListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    friendArrayList.clear();
                    ContactsManager.getMyFriendList(new onMyFriendList(), offset, limit);
                    if (chooseFriendAdapter != null) {
                        chooseFriendAdapter.notifyDataSetChanged();
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
                    if (listSize < limit) {//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    } else {//不是最后一页
                        offset += limit;
                        ContactsManager.getMyFriendList(new onMyFriendList(), offset, limit);
                        if (chooseFriendAdapter != null) {
                            chooseFriendAdapter.notifyDataSetChanged();
                        }
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }, 2000);
        }
    }

    private void initData() {
        chatCmdShareTaskBean = (ChatCmdShareTaskBean) mActivity.getIntent().getSerializableExtra("chatCmdShareTaskBean");
        ContactsManager.getMyFriendList(new onMyFriendList(), offset, limit);

        addMeFriendLocalCount = SpUtils.getInt("addMeFriendCount", 0);
        for (char cha = 'A'; cha <= 'Z'; cha++) {
            letterList.add(cha);
        }
    }

    private void initView() {
        locationCityFirstLetterAdapter = new LocationCityFirstLetterAdapter(letterList);
        activityChooseFriendBinding.lvLetter.setAdapter(locationCityFirstLetterAdapter);

        ContactsManager.getPersonRelationFirstPage(new onPersonRelationFirstPage());
    }

    private void listener() {
        activityChooseFriendBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chatCmdShareTaskBean == null) {
                    if (sendFriend) {
                        showSendDialog(position);
                    } else {
                        MyFriendListBean.DataBean.ListBean listBean = friendArrayList.get(position);
                        long uid = listBean.getUid();
                        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                        intentUserInfoActivity.putExtra("Uid", uid);
                        chooseFriendActivtiy.startActivity(intentUserInfoActivity);
                    }
                } else {
                    //进行斜杠好友分享任务（需求或者服务）
                    long friendUid = friendArrayList.get(position).getUid();
                    Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
                    intentChatActivity.putExtra("targetId", friendUid + "");
                    intentChatActivity.putExtra("chatCmdName", "sendShareTask");
                    intentChatActivity.putExtra("chatCmdShareTaskBean", chatCmdShareTaskBean);
                    mActivity.startActivity(intentChatActivity);
                }
            }
        });
    }

    private void showSendDialog(int position) {
        setSendFriendDialogVisibility(View.VISIBLE);
        MyFriendListBean.DataBean.ListBean listBean = friendArrayList.get(position);
        String name = listBean.getName();
        setSendName("发送给" + name);
        targetId = listBean.getUid();
    }

    //加我的
    public void addMe(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ADD_ME);
        openContactsCareActivity(ContactsManager.ADD_ME, "3");
        activityChooseFriendBinding.ivRequest.setVisibility(View.GONE);
        SpUtils.setInt("addMeFriendCount", addMeFriendCount);
        EventBus.getDefault().post(new MessageEvent());
    }

    //我加的
    public void meAdd(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ADD_ME);
        openContactsCareActivity(ContactsManager.MY_ADD, "4");
    }


    private void openContactsCareActivity(String title, String type) {
        Intent intentContactsCareActivity = new Intent(CommonUtils.getContext(), ContactsCareActivity.class);
        intentContactsCareActivity.putExtra("title", title);
        intentContactsCareActivity.putExtra("type", type);
        mActivity.startActivity(intentContactsCareActivity);
    }

    public class onMyFriendList implements BaseProtocol.IResultExecutor<MyFriendListBean> {
        @Override
        public void execute(MyFriendListBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                MyFriendListBean.DataBean data = dataBean.getData();
                List<MyFriendListBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                if (listSize == 0) {
                    activityChooseFriendBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                    activityChooseFriendBinding.tvContent.setVisibility(View.VISIBLE);
                } else {
                    friendArrayList.addAll(list);
                    chooseFriendAdapter = new ChooseFriendAdapter(friendArrayList);
                    activityChooseFriendBinding.lv.setAdapter(chooseFriendAdapter);
                    activityChooseFriendBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    @Bindable
    public int getSendFriendDialogVisibility() {
        return sendFriendDialogVisibility;
    }

    public void setSendFriendDialogVisibility(int sendFriendDialogVisibility) {
        this.sendFriendDialogVisibility = sendFriendDialogVisibility;
        notifyPropertyChanged(BR.sendFriendDialogVisibility);
    }

    @Bindable
    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
        notifyPropertyChanged(BR.sendName);
    }

    public void sendCannel(View view) {
        setSendFriendDialogVisibility(View.GONE);
    }

    public void sendSure(View view) {
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", String.valueOf(targetId));
        intentChatActivity.putExtra("chatCmdName", "sendBusinessCard");
        ChatCmdBusinesssCardBean chatCmdBusinesssCardBean = (ChatCmdBusinesssCardBean) chooseFriendActivtiy.getIntent().getSerializableExtra("ChatCmdBusinesssCardBean");
        intentChatActivity.putExtra("chatCmdBusinesssCardBean", chatCmdBusinesssCardBean);
        mActivity.startActivity(intentChatActivity);
    }
}
