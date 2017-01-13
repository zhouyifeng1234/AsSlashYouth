package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChooseFriendBinding;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.ChooseFriendAdapter;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseFriendModel  extends BaseObservable{
    private ActivityChooseFriendBinding activityChooseFriendBinding;
    private  ArrayList<MyFriendListBean.DataBean.ListBean> friendArrayList = new ArrayList<>();
    private ArrayList<Character> letterList = new ArrayList<>();
    private LocationCityFirstLetterAdapter locationCityFirstLetterAdapter;
    private ChooseFriendAdapter chooseFriendAdapter;
    private MyFriendActivtiy chooseFriendActivtiy;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int offset = 0;
    private int limit = 20;
    private int listSize;
    private boolean sendFriend;

    public ChooseFriendModel(ActivityChooseFriendBinding activityChooseFriendBinding,MyFriendActivtiy chooseFriendActivtiy,boolean sendFriend) {
        this.activityChooseFriendBinding = activityChooseFriendBinding;
        this.chooseFriendActivtiy = chooseFriendActivtiy;
        this.sendFriend = sendFriend;
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

    public class ChooseFriendListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    offset = 0;
                    friendArrayList.clear();
                    ContactsManager.getMyFriendList(new onMyFriendList(),offset,limit);
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
                        ContactsManager.getMyFriendList(new onMyFriendList(),offset,limit);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }, 2000);
        }
    }

    private void initData() {
        ContactsManager.getMyFriendList(new onMyFriendList(),offset,limit);

        for (char cha = 'A'; cha <= 'Z'; cha++) {
            letterList.add(cha);
        }
    }

    private void initView() {
        locationCityFirstLetterAdapter = new LocationCityFirstLetterAdapter(letterList);
        activityChooseFriendBinding.lvLetter.setAdapter(locationCityFirstLetterAdapter);
    }

    private void listener() {
        activityChooseFriendBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sendFriend){
                    showSendDialog(position);
                }else {
                    MyFriendListBean.DataBean.ListBean listBean = friendArrayList.get(position);
                    long uid = listBean.getUid();
                    Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                    intentUserInfoActivity.putExtra("Uid", uid);
                    chooseFriendActivtiy.startActivity(intentUserInfoActivity);
                }
            }
        });
    }

    private void showSendDialog(int position) {
        MyFriendListBean.DataBean.ListBean listBean = friendArrayList.get(position);
        String name = listBean.getName();
        DialogUtils.showDialogFive(chooseFriendActivtiy, "发给"+name, "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                LogKit.d("OK");

                ToastUtils.shortCenterToast("推荐好友ok");
            }

            @Override
            public void CancleDown() {
                LogKit.d("cannel");
            }
        });
    }

    public class onMyFriendList implements BaseProtocol.IResultExecutor<MyFriendListBean> {
        @Override
        public void execute(MyFriendListBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                MyFriendListBean.DataBean data = dataBean.getData();
                List<MyFriendListBean.DataBean.ListBean> list = data.getList();
                listSize = list.size();
                friendArrayList.addAll(list);
            }
            chooseFriendAdapter = new ChooseFriendAdapter(friendArrayList);
            activityChooseFriendBinding.lv.setAdapter(chooseFriendAdapter);
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
