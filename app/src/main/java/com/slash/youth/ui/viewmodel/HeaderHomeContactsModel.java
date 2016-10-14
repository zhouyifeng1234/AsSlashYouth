package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewHomeContactsBinding;
import com.slash.youth.databinding.ItemHscFriendRecommendBinding;
import com.slash.youth.databinding.ItemHscFriendRecommendChangeLoadBinding;
import com.slash.youth.domain.FriendRecommendBean;
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

    private View createRecommendHorizontalSpace() {
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(CommonUtils.dip2px(9), 0);
        View space = new View(CommonUtils.getContext());
        space.setLayoutParams(params);
        return space;
    }

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
            View recommendSpace = createRecommendHorizontalSpace();
            ItemFriendRecommendModel itemFriendRecommendModel = new ItemFriendRecommendModel(itemHscFriendRecommendBinding, itemFriendRecommend, recommendSpace, listFriendRecommendBean, i);
            itemHscFriendRecommendBinding.setItemFriendRecommendModel(itemFriendRecommendModel);
            mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(itemFriendRecommend);
            mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(recommendSpace);
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

}
