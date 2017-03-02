package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;

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
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.ui.adapter.ContactsCareAdapter;
import com.slash.youth.ui.adapter.PagerHomeDemandtAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

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
    public HeaderListviewHomeContactsBinding mHeaderListviewHomeContactsBinding;
    private PersonRelationBean.DataBean.InfoBean info;
    private ArrayList<RecommendFriendBean.DataBean.ListBean> listFriendRecommendBean = new ArrayList<>();
    private int addMeFriendCount;
    private int friendCount;
    private int myAddFriendCount;
    private int myFollowCount;
    private int myFansCount;
    private int limit = 10;
    private int type = -1;
    private Activity mActivity;
    private int myFansLocalCount;
    private int myFollowLocalCount;
    private int myAddFriendLocalCount;
    private int addMeFriendLocalCount;
    private ActivityHomeModel activityHomeModel;
    private boolean isMyFans;
    private boolean isMyFollow;
    private boolean isMyAddFriend;
    private boolean isAddMeFriend;

    public HeaderHomeContactsModel(HeaderListviewHomeContactsBinding headerListviewHomeContactsBinding, Activity mActivity, ActivityHomeModel activityHomeModel) {
        this.mHeaderListviewHomeContactsBinding = headerListviewHomeContactsBinding;
        this.mActivity = mActivity;
        this.activityHomeModel = activityHomeModel;
        initLocalData();
        initView();
        initData();
        lisitener();
    }

    private void lisitener() {
        if (isMyFans && isMyFollow && isAddMeFriend && isMyAddFriend) {
            activityHomeModel.setRedPointHintVisibility(View.GONE);
        }
    }

    //获取本地的数据
    private void initLocalData() {
        myFansLocalCount = SpUtils.getInt("myFansCount", 0);
        myFollowLocalCount = SpUtils.getInt("myFollowCount", 0);
        myAddFriendLocalCount = SpUtils.getInt("myAddFriendCount", 0);
        addMeFriendLocalCount = SpUtils.getInt("addMeFriendCount", 0);
    }

    private void initView() {
        activityHomeModel.setRedPointHintVisibility(View.GONE);
        mHeaderListviewHomeContactsBinding.hsvHomeContactsRecommend.setHorizontalScrollBarEnabled(false);
        //获取首页的信息
        PagerHomeContactsModel.getBaseDataTotalCount++;
        ContactsManager.getPersonRelationFirstPage(new onPersonRelationFirstPage());
    }

    private void initData() {
        getFriendRecommendData();
    }

    private View createRecommendHorizontalSpace() {
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(CommonUtils.dip2px(9), 0);
        View space = new View(CommonUtils.getContext());
        space.setLayoutParams(params);
        return space;
    }

    public void getFriendRecommendData() {
        listFriendRecommendBean.clear();
        PagerHomeContactsModel.getBaseDataTotalCount++;
        ContactsManager.getMyRecommendFriendList(new onGetMyRecommendFriendList(), limit);
    }

    //设置好友推荐列表数据
    public void displayFriendRecommend() {
        clearFriendRecommend();
        for (int i = 0; i < listFriendRecommendBean.size(); i++) {
            ItemHscFriendRecommendBinding itemHscFriendRecommendBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_hsc_friend_recommend, null, false);
            View itemFriendRecommend = itemHscFriendRecommendBinding.getRoot();
            View recommendSpace = createRecommendHorizontalSpace();
            ItemFriendRecommendModel itemFriendRecommendModel = new ItemFriendRecommendModel(itemHscFriendRecommendBinding, itemFriendRecommend, listFriendRecommendBean, i, recommendSpace);
            itemHscFriendRecommendBinding.setItemFriendRecommendModel(itemFriendRecommendModel);
            mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(itemFriendRecommend);
            mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(recommendSpace);
        }
        //设置好友推荐换一批按钮
        setChangeALotFriendRecommendButton();
    }

    public void setChangeALotFriendRecommendButton() {
        ItemHscFriendRecommendChangeLoadBinding itemHscFriendRecommendChangeLoadBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_hsc_friend_recommend_change_load, null, false);
        FriendRecommendChangeLoadModel friendRecommendChangeLoadModel = new FriendRecommendChangeLoadModel(itemHscFriendRecommendChangeLoadBinding, this, mHeaderListviewHomeContactsBinding.hsvHomeContactsRecommend);
        itemHscFriendRecommendChangeLoadBinding.setFriendRecommendChangeLoadModel(friendRecommendChangeLoadModel);
        mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.addView(itemHscFriendRecommendChangeLoadBinding.getRoot());
    }

    public void clearFriendRecommend() {
        mHeaderListviewHomeContactsBinding.llHomeContactsRecommend.removeAllViews();
    }

    //关注我的
    public void careMe(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_FOCUS_MINE);

        openContactsCareActivity(ContactsManager.CARE_ME);
        type = 1;
        mHeaderListviewHomeContactsBinding.viewRedSpot1.setVisibility(View.GONE);
        SpUtils.setInt("myFollowCount", myFollowCount);
        isMyFollow = false;
    }

    //我关注
    public void myCare(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_MY_FOCUS);

        openContactsCareActivity(ContactsManager.MY_CARE);
        type = 2;
        mHeaderListviewHomeContactsBinding.viewRedSpot2.setVisibility(View.GONE);
        SpUtils.setInt("myFansCount", myFansCount);
        isMyFans = false;
    }

    //加我的
    public void addMe(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ADD_ME);

        openContactsCareActivity(ContactsManager.ADD_ME);
        type = 3;
        mHeaderListviewHomeContactsBinding.viewRedSpot3.setVisibility(View.GONE);
        SpUtils.setInt("addMeFriendCount", addMeFriendCount);
        isAddMeFriend = false;
    }

    //我加的
    public void myAdd(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ME_ADD);

        openContactsCareActivity(ContactsManager.MY_ADD);
        type = 4;
        mHeaderListviewHomeContactsBinding.viewRedSpot4.setVisibility(View.GONE);
        SpUtils.setInt("myAddFriendCount", myAddFriendCount);
        isMyAddFriend = false;
    }

    //人脉潜能
    public void contactsPotential(View view) {
    }

    //我的好友
    public void MyFriend(View view) {
        Intent intentChooseFriendActivtiy = new Intent(CommonUtils.getContext(), MyFriendActivtiy.class);
        mActivity.startActivity(intentChooseFriendActivtiy);
    }

    private void openContactsCareActivity(String title) {
        Intent intentContactsCareActivity = new Intent(CommonUtils.getContext(), ContactsCareActivity.class);
        intentContactsCareActivity.putExtra("title", title);
        intentContactsCareActivity.putExtra("type", type);
        mActivity.startActivity(intentContactsCareActivity);
    }

    //首页展示的数据
    public class onPersonRelationFirstPage implements BaseProtocol.IResultExecutor<PersonRelationBean> {
        @Override
        public void execute(PersonRelationBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                //隐藏加载页面
                PagerHomeContactsModel.getBaseDataFinishedCount++;
                if (PagerHomeContactsModel.getBaseDataFinishedCount >= PagerHomeContactsModel.getBaseDataTotalCount) {
                    listener.OnRelationLoadData(true);
                }

                PersonRelationBean.DataBean data = dataBean.getData();
                info = data.getInfo();
                addMeFriendCount = info.getAddMeFriendCount();
                friendCount = info.getFriendCount();
                myAddFriendCount = info.getMyAddFriendCount();
                myFollowCount = info.getMyFollowCount();
                myFansCount = info.getMyFansCount();

                mHeaderListviewHomeContactsBinding.tvCareMe.setText(String.valueOf(myFollowCount));
                mHeaderListviewHomeContactsBinding.tvMyCare.setText(String.valueOf(myFansCount));
                mHeaderListviewHomeContactsBinding.tvMyAdd.setText(String.valueOf(myAddFriendCount));
                mHeaderListviewHomeContactsBinding.tvAddMe.setText(String.valueOf(addMeFriendCount));

                //保存一下在本地
                // if(myFollowLocalCount!=0){
                if (myFansCount != myFansLocalCount) {
                    mHeaderListviewHomeContactsBinding.viewRedSpot2.setVisibility(View.VISIBLE);
                    activityHomeModel.setRedPointHintVisibility(View.VISIBLE);
                    isMyFans = true;
                }
                // }

                // if(myFansLocalCount!=0){
                if (myFollowCount != myFollowLocalCount) {
                    mHeaderListviewHomeContactsBinding.viewRedSpot1.setVisibility(View.VISIBLE);
                    activityHomeModel.setRedPointHintVisibility(View.VISIBLE);
                    isMyFollow = true;
                }
                //  }

                // if(addMeFriendCount!=0){
                if (addMeFriendLocalCount != addMeFriendCount) {
                    mHeaderListviewHomeContactsBinding.viewRedSpot3.setVisibility(View.VISIBLE);
                    activityHomeModel.setRedPointHintVisibility(View.VISIBLE);
                    isAddMeFriend = true;
                }
                // }

                //  if(myAddFriendCount!=0){
                if (myAddFriendLocalCount != myAddFriendCount) {
                    mHeaderListviewHomeContactsBinding.viewRedSpot4.setVisibility(View.VISIBLE);
                    activityHomeModel.setRedPointHintVisibility(View.VISIBLE);
                    isMyAddFriend = true;
                }
                // }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //获取好友推荐列表数据
    public class onGetMyRecommendFriendList implements BaseProtocol.IResultExecutor<RecommendFriendBean> {
        @Override
        public void execute(RecommendFriendBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                //隐藏加载页面
                PagerHomeContactsModel.getBaseDataFinishedCount++;
                if (PagerHomeContactsModel.getBaseDataFinishedCount >= PagerHomeContactsModel.getBaseDataTotalCount) {
                    listener.OnRecommendLoadData(true);
                }

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
            LogKit.d("result:" + result);
        }
    }


    //监听数据获取
    public interface OnLoadDataListener {
        void OnRecommendLoadData(boolean MyRecommendFriendRecode);

        void OnRelationLoadData(boolean personRelationRecode);
    }

    private OnLoadDataListener listener;

    public void setOnLoadDataListener(OnLoadDataListener listener) {
        this.listener = listener;
    }
}
