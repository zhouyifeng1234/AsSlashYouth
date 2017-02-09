package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemHscFriendRecommendBinding;
import com.slash.youth.domain.FriendRecommendBean;
import com.slash.youth.domain.RecommendFriendBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.LoginBindProtocol;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class ItemFriendRecommendModel extends BaseObservable {
    ItemHscFriendRecommendBinding mItemHscFriendRecommendBinding;
    private RecommendFriendBean.DataBean.ListBean mFriendRecommendBean;
    View mItemFriendRecommend;
    View mRecommendSpace;
    ArrayList<RecommendFriendBean.DataBean.ListBean> mListFriendRecommendBean;
   private   int index;
    private String avatar;
    private String company;
    private String position;
    private int isauth;
    private int limit = 10;

    public ItemFriendRecommendModel(ItemHscFriendRecommendBinding itemHscFriendRecommendBinding, View itemFriendRecommend, ArrayList<RecommendFriendBean.DataBean.ListBean> listFriendRecommendBean, int index,View recommendSpace) {
        this.mItemHscFriendRecommendBinding = itemHscFriendRecommendBinding;
        this.mFriendRecommendBean = listFriendRecommendBean.get(index);
        this.mItemFriendRecommend = itemFriendRecommend;
       this.mRecommendSpace = recommendSpace;
        this.mListFriendRecommendBean = listFriendRecommendBean;
        this.index = index;
        initData();
        initVisible();
    }
    //获取网络推荐好友的数据
    private void initData() {
        setView(mFriendRecommendBean);
    }

    private void initVisible() {
        setCommonRecommendMarkerVisibility(View.INVISIBLE);
        setEliteRecommendMarkerVisibility(View.INVISIBLE);
      /*  if (mFriendRecommendBean.isEliteRecommedn) {
            setEliteRecommendMarkerVisibility(View.VISIBLE);
            setCommonRecommendMarkerVisibility(View.INVISIBLE);
        } else {
            setEliteRecommendMarkerVisibility(View.INVISIBLE);
            setCommonRecommendMarkerVisibility(View.VISIBLE);
        }*/
    }

    //删除一条推荐
    public void deleteRecommend(View v) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_RECOMMEND_CLICK_CLOSE);

       // itemParent.removeView(mRecommendSpace);
        long uid = mFriendRecommendBean.getUid();
        ContactsManager.AddBlackFriend(new onAddBlackFriend(),uid);
    }

    //添加推荐的好友
    public void addFriend(View v) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_RECOMMEND_CLICK_ADD_FRIEND);

        long uid = mFriendRecommendBean.getUid();
        ContactsManager.onAddFriendRelationProtocol(new  onAddFriendRelationProtocol(),uid,"   ");
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

    public void setCompany(String company) {
        this.company = company;
        notifyPropertyChanged(BR.company);
    }

    @Bindable
    public String getCompany() {
        return company;
    }

    public void setPosition(String position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    @Bindable
    public String getPosition() {
        return position;
    }


    //设置推荐好友的数据
    private void setView(RecommendFriendBean.DataBean.ListBean listBean) {
        setUsername(listBean.getName());
        avatar = listBean.getAvatar();
        if(avatar!=null){
            BitmapKit.bindImage(mItemHscFriendRecommendBinding.ivAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        String company = listBean.getCompany();
        if(company!=null){
            setCompany(company);
        }
        String direction = listBean.getDirection();
        String industry = listBean.getIndustry();
        long uid = listBean.getUid();
        setPosition(listBean.getPosition());
        int isauth = listBean.getIsauth();
        switch (isauth){
            case 1:
                mItemHscFriendRecommendBinding.ivRecommendV.setVisibility(View.VISIBLE);
                break;
            case 0:
                mItemHscFriendRecommendBinding.ivRecommendV.setVisibility(View.GONE);
                break;
            default:
                mItemHscFriendRecommendBinding.ivRecommendV.setVisibility(View.GONE);
                break;
        }
    }

    //加好友关系
    public class onAddFriendRelationProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        mItemHscFriendRecommendBinding.btnAddFriend.setText("已申请");
                        LogKit.d("表示发起申请成功-并且对方未确认");
                        break;
                    case 2:
                        mItemHscFriendRecommendBinding.btnAddFriend.setText("已申请");
                        LogKit.d("表示发起申请成功-已经成为好友关系");
                        break;
                    case 0:
                        LogKit.d("status:"+status+"status=0表示发起申请失败");
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //拉黑朋友
    public class onAddBlackFriend implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1://1加入黑名单成功
                        mListFriendRecommendBean.remove(mFriendRecommendBean);
                        ViewGroup itemParent = (ViewGroup) mItemFriendRecommend.getParent();
                        itemParent.removeView(mItemFriendRecommend);
                        itemParent.removeView(mRecommendSpace);
                        break;
                    case 0://0加入黑名单失败
                        LogKit.d("加入黑名单失败");
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //点击头像看他的资料
    public void avater(View view){
        long uid = mFriendRecommendBean.getUid();
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", uid);
        intentUserInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentUserInfoActivity);
    }
}
