package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemHscFriendRecommendBinding;
import com.slash.youth.domain.FriendRecommendBean;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class ItemFriendRecommendModel extends BaseObservable {
    ItemHscFriendRecommendBinding mItemHscFriendRecommendBinding;
    FriendRecommendBean mFriendRecommendBean;
    View mItemFriendRecommend;
//    View mRecommendSpace;
    ArrayList<FriendRecommendBean> mListFriendRecommendBean;
    int index;

    public ItemFriendRecommendModel(ItemHscFriendRecommendBinding itemHscFriendRecommendBinding, View itemFriendRecommend, ArrayList<FriendRecommendBean> listFriendRecommendBean, int index) {
        this.mItemHscFriendRecommendBinding = itemHscFriendRecommendBinding;
        this.mFriendRecommendBean = listFriendRecommendBean.get(index);
        this.mItemFriendRecommend = itemFriendRecommend;
//        this.mRecommendSpace = recommendSpace;
        this.mListFriendRecommendBean = listFriendRecommendBean;
        this.index = index;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        if (mFriendRecommendBean.isEliteRecommedn) {
            setEliteRecommendMarkerVisibility(View.VISIBLE);
            setCommonRecommendMarkerVisibility(View.INVISIBLE);
        } else {
            setEliteRecommendMarkerVisibility(View.INVISIBLE);
            setCommonRecommendMarkerVisibility(View.VISIBLE);
        }
        setUsername(mFriendRecommendBean.username);
    }

    //删除一条推荐
    public void deleteRecommend(View v) {
        mListFriendRecommendBean.remove(mFriendRecommendBean);
        ViewGroup itemParent = (ViewGroup) mItemFriendRecommend.getParent();
        itemParent.removeView(mItemFriendRecommend);
//        itemParent.removeView(mRecommendSpace);
        //调用服务端相关接口，实现删除一个推荐条目的功能
    }

    //添加推荐的好友
    public void addFriend(View v) {
        mListFriendRecommendBean.remove(mFriendRecommendBean);
        ViewGroup itemParent = (ViewGroup) mItemFriendRecommend.getParent();
        itemParent.removeView(mItemFriendRecommend);
//        itemParent.removeView(mRecommendSpace);
        //调用服务端相关接口，实现添加一个推荐好友的操作
    }

    private int eliteRecommendMarkerVisibility;
    private int commonRecommendMarkerVisibility;
    private String username;

    @Bindable
    public int getEliteRecommendMarkerVisibility() {
        return eliteRecommendMarkerVisibility;
    }

    public void setEliteRecommendMarkerVisibility(int eliteRecommendMarkerVisibility) {
        this.eliteRecommendMarkerVisibility = eliteRecommendMarkerVisibility;
        notifyPropertyChanged(BR.eliteRecommendMarkerVisibility);
    }

    @Bindable
    public int getCommonRecommendMarkerVisibility() {
        return commonRecommendMarkerVisibility;
    }

    public void setCommonRecommendMarkerVisibility(int commonRecommendMarkerVisibility) {
        this.commonRecommendMarkerVisibility = commonRecommendMarkerVisibility;
        notifyPropertyChanged(BR.commonRecommendMarkerVisibility);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }
}
