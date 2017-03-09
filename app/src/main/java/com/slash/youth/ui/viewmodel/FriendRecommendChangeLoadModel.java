package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.slash.youth.databinding.ItemHscFriendRecommendChangeLoadBinding;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouyifeng on 2016/10/14.
 */
public class FriendRecommendChangeLoadModel extends BaseObservable {
    ItemHscFriendRecommendChangeLoadBinding mItemHscFriendRecommendChangeLoadBinding;
    HeaderHomeContactsModel mHeaderHomeContactsModel;
    HorizontalScrollView mHsvFriendRecommend;

    public FriendRecommendChangeLoadModel(ItemHscFriendRecommendChangeLoadBinding itemHscFriendRecommendChangeLoadBinding, HeaderHomeContactsModel headerHomeContactsModel, HorizontalScrollView hsvFriendRecommend) {
        this.mItemHscFriendRecommendChangeLoadBinding = itemHscFriendRecommendChangeLoadBinding;
        this.mHeaderHomeContactsModel = headerHomeContactsModel;
        this.mHsvFriendRecommend = hsvFriendRecommend;
    }

    public void changeALotFriendRecommend(View v) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_RECOMMEND_CLICK_CHANGE);

        mHeaderHomeContactsModel.getFriendRecommendData();
        mHeaderHomeContactsModel.displayFriendRecommend();
        mHsvFriendRecommend.fullScroll(View.FOCUS_LEFT);
    }
}
