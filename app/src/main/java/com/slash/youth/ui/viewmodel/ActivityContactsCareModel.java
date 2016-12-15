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
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.ContactsCareAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

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

    public ActivityContactsCareModel(ActivityContactsCareBinding activityContactsCareBinding, String title, ContactsCareActivity contactsCareActivity) {
        this.activityContactsCareBinding = activityContactsCareBinding;
        this.contactsCareActivity = contactsCareActivity;
        this.title = title;
        initData();
        initView();
        listener();
    }

    private void initData() {
        contactsLists.clear();
        switch (title){
            case ContactsManager.CARE_ME:
                type=1;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.CARE_ME_PERSON);
                 break;
            case ContactsManager.MY_CARE:
                type=2;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_CARE_PERSON);
                break;
            case ContactsManager.ADD_ME:
                type = 3;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/addmelist");
                break;
            case ContactsManager.MY_ADD:
                type = 4;
                ContactsManager.getAddMeList(new onGetAddMeList(),offset,limit,GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/myaddlist");
                break;
        }
    }

    private void initView() {
        activityContactsCareBinding.tvContactsTitle.setText(title);
    }

    private void listener() {
        loadMoreListData();

        //条目点击
        activityContactsCareBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactsBean.DataBean.ListBean listBean = contactsLists.get(position);
                long uid = listBean.getUid();
                Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
                intentUserInfoActivity.putExtra("Uid", uid);
                contactsCareActivity.startActivity(intentUserInfoActivity);
            }
        });
    }

    private void loadMoreListData() {
        activityContactsCareBinding.lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
                if(visibleLastIndex == contactsLists.size()&&contactsLists.size()!=0){
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
            contactsCareAdapter = new ContactsCareAdapter(contactsLists,type);
            activityContactsCareBinding.lv.setAdapter(contactsCareAdapter);

           // agreeFriend();
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //点击按钮同意添加好友
   /* private void agreeFriend() {
        contactsCareAdapter.setItemRemoveListener(new ContactsCareAdapter.onItemRemoveListener() {
            @Override
            public void onItemRemove(int index) {
                ContactsBean.DataBean.ListBean listBean = contactsLists.get(index);
                int id = listBean.getUid();
                Long uid = new Long(id);
               // ContactsManager.onAgreeFriendProtocol(new onAgreeFriendProtocol(),uid,"contacts");
                if(isAgree){
                   //contactsLists.remove(index);
                    //contactsCareAdapter.notifyDataSetChanged();

                }
            }
        });
    }*/

    public class onAgreeFriendProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        ToastUtils.shortCenterToast("已是好友");
                        isAgree = true;
                        break;
                    case 0:
                        ToastUtils.shortCenterToast("添加好友未成功");
                        isAgree = false;
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
