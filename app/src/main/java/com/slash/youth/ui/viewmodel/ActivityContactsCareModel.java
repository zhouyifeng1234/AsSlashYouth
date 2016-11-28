package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.net.sip.SipSession;
import android.widget.AbsListView;

import com.slash.youth.databinding.ActivityContactsCareBinding;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.domain.RecommendFriendBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.adapter.ContactsCareAdapter;
import com.slash.youth.utils.LogKit;

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



    public ActivityContactsCareModel(ActivityContactsCareBinding activityContactsCareBinding,String title) {
        this.activityContactsCareBinding = activityContactsCareBinding;
        this.title = title;
        initData();
        initView();
        listener();
    }

    private void initData() {
        contactsLists.clear();
        switch (title){
            case "关注我的":
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.CARE_ME_PERSON);
                type=1;
                 break;
            case "我关注":
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_CARE_PERSON);
                type=2;
                break;
            case "加我的":
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/addmelist");
                type = 3;
            case "我加的":
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/myaddlist");
                type = 4;
                break;
        }
    }

    private void initView() {
        activityContactsCareBinding.tvContactsTitle.setText(title);
    }

    private void listener() {
        loadMoreListData();

    }

    private void loadMoreListData() {
        activityContactsCareBinding.lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
                if(visibleLastIndex == contactsLists.size()){
                    offset = visibleLastIndex;
                    switch (type){
                        case 1:
                            ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.CARE_ME_PERSON);
                            break;
                        case 2:
                            ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_CARE_PERSON);
                            break;
                        case 3:
                            ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit, GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/addmelist");
                            break;
                        case 4:
                            ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/myaddlist");
                            break;
                    }
                }
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
                contactsLists.addAll(list);
            }
            contactsCareAdapter = new ContactsCareAdapter(contactsLists);
            activityContactsCareBinding.lv.setAdapter(contactsCareAdapter);
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
