package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.databinding.BaseObservable;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.slash.youth.databinding.ActivityChooseFriendBinding;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.adapter.ChooseFriendAdapter;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;

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

    public ChooseFriendModel(ActivityChooseFriendBinding activityChooseFriendBinding,MyFriendActivtiy chooseFriendActivtiy) {
        this.activityChooseFriendBinding = activityChooseFriendBinding;
        this.chooseFriendActivtiy = chooseFriendActivtiy;
        initData();
        initView();
        listener();
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

    @TargetApi(Build.VERSION_CODES.M)
    private void listener() {
        activityChooseFriendBinding.lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSendDialog(position);
            }
        });

       activityChooseFriendBinding.lvFriend.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView view, int scrollState) {
           }

           @Override
           public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
               if(visibleLastIndex == friendArrayList.size()){
                   offset = visibleLastIndex;
                   ContactsManager.getMyFriendList(new onMyFriendList(),offset,limit);
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
                friendArrayList.addAll(list);
            }
            chooseFriendAdapter = new ChooseFriendAdapter(friendArrayList);
            activityChooseFriendBinding.lvFriend.setAdapter(chooseFriendAdapter);
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
