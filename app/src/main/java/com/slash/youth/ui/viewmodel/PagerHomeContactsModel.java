package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHscFriendRecommendBinding;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.domain.FriendRecommendBean;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.ui.adapter.HomeContactsVisitorAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeContactsModel extends BaseObservable {
    PagerHomeContactsBinding mPagerHomeContactsBinding;
    Activity mActivity;

    public PagerHomeContactsModel(PagerHomeContactsBinding pagerHomeContactsBinding, Activity activity) {
        this.mPagerHomeContactsBinding = pagerHomeContactsBinding;
        this.mActivity = activity;
        initView();
        initData();
    }

    ArrayList<HomeContactsVisitorBean> listHomeContactsVisitorBean = new ArrayList<HomeContactsVisitorBean>();
    ArrayList<FriendRecommendBean> listFriendRecommendBean = new ArrayList<FriendRecommendBean>();

    private void initView() {
        mPagerHomeContactsBinding.hsvHomeContactsRecommend.setHorizontalScrollBarEnabled(false);
    }

    private void initData() {
        getDataFromServer();
        //设置访客列表数据
        mPagerHomeContactsBinding.lvHomeContactsVisitor.setAdapter(new HomeContactsVisitorAdapter(listHomeContactsVisitorBean));

        //设置好友推荐列表数据
        for (int i = 0; i < listFriendRecommendBean.size(); i++) {
            ItemHscFriendRecommendBinding itemHscFriendRecommendBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_hsc_friend_recommend, null, false);
            View itemFriendRecommend = itemHscFriendRecommendBinding.getRoot();
            View recommendSpace = createRecommendHorizontalSpace();
            ItemFriendRecommendModel itemFriendRecommendModel = new ItemFriendRecommendModel(itemHscFriendRecommendBinding, itemFriendRecommend, recommendSpace, listFriendRecommendBean, i);
            itemHscFriendRecommendBinding.setItemFriendRecommendModel(itemFriendRecommendModel);
            mPagerHomeContactsBinding.llHomeContactsRecommend.addView(itemFriendRecommend);
            if (i < listFriendRecommendBean.size() - 1) {
                mPagerHomeContactsBinding.llHomeContactsRecommend.addView(recommendSpace);
            }
        }
    }

    private View createRecommendHorizontalSpace() {
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(CommonUtils.dip2px(9), 0);
        View space = new View(CommonUtils.getContext());
        space.setLayoutParams(params);
        return space;
    }

    public void getDataFromServer() {
        //模拟数据 访客列表数据
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());

        //模拟数据 推荐数据
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
}
