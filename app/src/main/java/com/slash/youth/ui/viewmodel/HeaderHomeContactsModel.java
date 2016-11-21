package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewHomeContactsBinding;
import com.slash.youth.databinding.ItemHscFriendRecommendBinding;
import com.slash.youth.databinding.ItemHscFriendRecommendChangeLoadBinding;
import com.slash.youth.domain.FriendRecommendBean;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.activity.TransactionRecordActivity;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HeaderHomeContactsModel extends BaseObservable {
    HeaderListviewHomeContactsBinding mHeaderListviewHomeContactsBinding;

    public HeaderHomeContactsModel(HeaderListviewHomeContactsBinding headerListviewHomeContactsBinding) {
        this.mHeaderListviewHomeContactsBinding = headerListviewHomeContactsBinding;
        initView();
        initData();
    }

    ArrayList<FriendRecommendBean> listFriendRecommendBean = new ArrayList<FriendRecommendBean>();

    private void initView() {
        mHeaderListviewHomeContactsBinding.hsvHomeContactsRecommend.setHorizontalScrollBarEnabled(false);
    }

    private void initData() {
        getDataFromServer();
        //设置好友推荐列表数据
        displayFriendRecommend();
    }

//    private View createRecommendHorizontalSpace() {
//        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(CommonUtils.dip2px(9), 0);
//        View space = new View(CommonUtils.getContext());
//        space.setLayoutParams(params);
//        return space;
//    }

    public void getDataFromServer() {
        //模拟数据 推荐数据
        getFriendRecommendData();
    }

    public void getFriendRecommendData() {
        listFriendRecommendBean.clear();
        listFriendRecommendBean.add(new FriendRecommendBean(true, "曹文成1"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成2"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成3"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成4"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成5"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成6"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成7"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成8"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成9"));
        listFriendRecommendBean.add(new FriendRecommendBean(false, "曹文成10"));
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
    }
    //我关注
    public void myCare(View view) {
        openContactsCareActivity("我关注");
    }
    //加我的
    public void addMe(View view) {
        openContactsCareActivity("加我的");
    }
    //我加的
    public void myAdd(View view) {
        openContactsCareActivity("我加的");
    }

    //人脉潜能
    public void contactsPotential(View view) {


    }

    private void openContactsCareActivity(String title) {
        Intent intentContactsCareActivity = new Intent(CommonUtils.getContext(), ContactsCareActivity.class);
        intentContactsCareActivity.putExtra("title",title);
        intentContactsCareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentContactsCareActivity);
    }

}
