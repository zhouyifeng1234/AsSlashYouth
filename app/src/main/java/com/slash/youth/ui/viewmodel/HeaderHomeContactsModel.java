package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewHomeContactsBinding;
import com.slash.youth.databinding.ItemHscFriendRecommendBinding;
import com.slash.youth.databinding.ItemHscFriendRecommendChangeLoadBinding;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.domain.FriendRecommendBean;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.domain.RecommendFriendBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.adapter.ContactsCareAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HeaderHomeContactsModel extends BaseObservable {
    HeaderListviewHomeContactsBinding mHeaderListviewHomeContactsBinding;
    private PersonRelationBean.DataBean.InfoBean info;
    private  ArrayList<RecommendFriendBean.DataBean.ListBean> listFriendRecommendBean = new ArrayList<>();
    private int addMeFriendCount;
    private int friendCount;
    private int myAddFriendCount;
    private int myFollowCount;
    private int myFansCount;
    private int limit = 10;
    private int type = -1;

    private  ArrayList<Long> uidList = new ArrayList<>();


    public HeaderHomeContactsModel(HeaderListviewHomeContactsBinding headerListviewHomeContactsBinding) {
        this.mHeaderListviewHomeContactsBinding = headerListviewHomeContactsBinding;
        initView();
        initData();

    }

    private void initView() {
        mHeaderListviewHomeContactsBinding.hsvHomeContactsRecommend.setHorizontalScrollBarEnabled(false);
        //获取首页的信息
        ContactsManager.getPersonRelationFirstPage(new onPersonRelationFirstPage());

        mHeaderListviewHomeContactsBinding.tvCareMe.setText( String.valueOf(myFansCount) );
        mHeaderListviewHomeContactsBinding.tvMyCare.setText( String.valueOf(myFollowCount));
        mHeaderListviewHomeContactsBinding.tvMyAdd.setText(String.valueOf(myAddFriendCount));
        mHeaderListviewHomeContactsBinding.tvAddMe.setText(String.valueOf(addMeFriendCount));
    }

    private void initData() {
        getFriendRecommendData();
        ContactsManager.getAddMeList(new onGetAddMeList(),0,20, GlobalConstants.HttpUrl.CARE_ME_PERSON);
        ContactsManager.getAddMeList(new onGetAddMeList(),0,20,GlobalConstants.HttpUrl.MY_FRIEND_LIST_HOST+"/addmelist");
    }

//    private View createRecommendHorizontalSpace() {
//        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(CommonUtils.dip2px(9), 0);
//        View space = new View(CommonUtils.getContext());
//        space.setLayoutParams(params);
//        return space;
//    }


    public void getFriendRecommendData() {
        listFriendRecommendBean.clear();
      //  ContactsManager.getMyRecommendFriendList(new onGetMyRecommendFriendList(),limit);
    }

    public void displayFriendRecommend() {
        clearFriendRecommend();
        for (int i = 0; i < listFriendRecommendBean.size(); i++) {
            ItemHscFriendRecommendBinding itemHscFriendRecommendBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_hsc_friend_recommend, null, false);
            View itemFriendRecommend = itemHscFriendRecommendBinding.getRoot();
//            View recommendSpace = createRecommendHorizontalSpace();
            ItemFriendRecommendModel itemFriendRecommendModel = new ItemFriendRecommendModel(itemHscFriendRecommendBinding, itemFriendRecommend, listFriendRecommendBean, i);
            itemHscFriendRecommendBinding.setItemFriendRecommendModel(itemFriendRecommendModel);
            mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(itemFriendRecommend);
//            mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(recommendSpace);
        }
        //设置好友推荐换一批按钮
        setChangeALotFriendRecommendButton();
    }

    public void setChangeALotFriendRecommendButton() {
        ItemHscFriendRecommendChangeLoadBinding itemHscFriendRecommendChangeLoadBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_hsc_friend_recommend_change_load, null, false);
        FriendRecommendChangeLoadModel friendRecommendChangeLoadModel = new FriendRecommendChangeLoadModel(itemHscFriendRecommendChangeLoadBinding, this,mHeaderListviewHomeContactsBinding.hsvHomeContactsRecommend);
        itemHscFriendRecommendChangeLoadBinding.setFriendRecommendChangeLoadModel(friendRecommendChangeLoadModel);
        mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(itemHscFriendRecommendChangeLoadBinding.getRoot());
    }

    public void clearFriendRecommend() {
        mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.removeAllViews();
    }

    //关注我的
    public void careMe(View view) {
        openContactsCareActivity("关注我的");
        type=1;
    }
    //我关注
    public void myCare(View view) {
        openContactsCareActivity("我关注");
    }
    //加我的
    public void addMe(View view) {
        openContactsCareActivity("加我的");
        type=3;
    }
    //我加的
    public void myAdd(View view) {
        openContactsCareActivity("我加的");
    }

    //人脉潜能
    public void contactsPotential(View view) {
    }

    //我的好友
    public void MyFriend(View view){
    Intent intentChooseFriendActivtiy = new Intent(CommonUtils.getContext(), MyFriendActivtiy.class);
    intentChooseFriendActivtiy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    CommonUtils.getContext().startActivity(intentChooseFriendActivtiy);
    }

    private void openContactsCareActivity(String title) {
        Intent intentContactsCareActivity = new Intent(CommonUtils.getContext(), ContactsCareActivity.class);
        intentContactsCareActivity.putExtra("title",title);
        intentContactsCareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentContactsCareActivity);
    }

    //首页展示的数据
    public class onPersonRelationFirstPage implements BaseProtocol.IResultExecutor<PersonRelationBean> {
        @Override
        public void execute(PersonRelationBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                PersonRelationBean.DataBean data = dataBean.getData();
                info = data.getInfo();
                addMeFriendCount = info.getAddMeFriendCount();
                friendCount = info.getFriendCount();
                myAddFriendCount = info.getMyAddFriendCount();
                myFollowCount = info.getMyFollowCount();
                myFansCount = info.getMyFansCount();
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    public class onGetMyRecommendFriendList implements BaseProtocol.IResultExecutor<RecommendFriendBean> {
        @Override
        public void execute(RecommendFriendBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                RecommendFriendBean.DataBean data = dataBean.getData();
                List<RecommendFriendBean.DataBean.ListBean> list = data.getList();
                for (RecommendFriendBean.DataBean.ListBean listBean : list) {
                    listFriendRecommendBean.add(listBean);
                }
                //设置好友推荐列表数据
                displayFriendRecommend();
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //获取关注我的和加我的uid
    public class onGetAddMeList implements BaseProtocol.IResultExecutor<ContactsBean> {
        @Override
        public void execute(ContactsBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                ContactsBean.DataBean data = dataBean.getData();
                List<ContactsBean.DataBean.ListBean> list = data.getList();
                for (ContactsBean.DataBean.ListBean listBean : list) {
                    long uid = listBean.getUid();
                    switch (type){
                        case 1:
                            uidList.add(uid);
                            //showRedSpoit(uidList,"CareMe");
                            //uidList.clear();
                            break;
                        case 3:
                            uidList.add(uid);
                          //  showRedSpoit(uidList,"AddMe");
                          //  uidList.clear();
                            break;
                    }
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //显示小红点
    public void showRedSpoit( ArrayList<Integer> uidList,String url){

        ArrayList<Integer> dateFromLocal = IOUtils.getDateFromLocal(IOUtils.UID_PATH+url);
        //1,先读取本地的UID，如果没有，就存，如果有，就遍历一下，看看是否完全一样，一样，就不显示红点
        //如果有不一样，就找出不一样的，就把不一样的存在，里面，显示小红点
       if (dateFromLocal.isEmpty()) {
            mHeaderListviewHomeContactsBinding.viewRedSpot1.setVisibility(View.VISIBLE);
            IOUtils.saveDate2Local(IOUtils.UID_PATH+url,uidList);
        }else if(!dateFromLocal.isEmpty()){
            boolean equals = dateFromLocal.equals(uidList);
            if(equals){
                mHeaderListviewHomeContactsBinding.viewRedSpot1.setVisibility(View.GONE);
            }else {
                mHeaderListviewHomeContactsBinding.viewRedSpot1.setVisibility(View.VISIBLE);
                for (int i = 0; i < uidList.size(); i++) {
                    Integer integer = uidList.get(i);
                    if (!uidList.contains(integer)) {
                        dateFromLocal.add(integer);
                        IOUtils.saveDate2Local(IOUtils.UID_PATH+url,dateFromLocal);
                    }
                }
            }
        }
    }

}
