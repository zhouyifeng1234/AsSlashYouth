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
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.domain.RecommendFriendBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.ContactsCareActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.List;

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
    private int limit = 5;

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

    }

//    private View createRecommendHorizontalSpace() {
//        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(CommonUtils.dip2px(9), 0);
//        View space = new View(CommonUtils.getContext());
//        space.setLayoutParams(params);
//        return space;
//    }

   /* public void getDataFromServer() {
        //模拟数据 推荐数据
        getFriendRecommendData();
    }
*/
    public void getFriendRecommendData() {
        listFriendRecommendBean.clear();
        ContactsManager.getMyRecommendFriendList(new onGetMyRecommendFriendList(),limit);
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


}
