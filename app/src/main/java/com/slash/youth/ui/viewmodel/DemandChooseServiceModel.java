package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandChooseServiceBinding;
import com.slash.youth.databinding.ItemDemandChooseRecommendServiceBinding;
import com.slash.youth.databinding.ItemDemandChooseServiceBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.RecommendServiceUserBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.MyPublishDemandActivity;
import com.slash.youth.ui.view.RefreshScrollView;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class DemandChooseServiceModel extends BaseObservable {

    ActivityDemandChooseServiceBinding mActivityDemandChooseServiceBinding;
    Activity mActivity;
    long tid;//需求ID
    int type;//取值范围只能是: 1需求 2服务
    int roleid;//表示是'我抢的单子' 还是 '我发布的任务' 1发布者 2抢单者
    ChatCmdShareTaskBean chatCmdShareTaskBean;
    Bundle taskInfo;

    public DemandChooseServiceModel(ActivityDemandChooseServiceBinding activityDemandChooseServiceBinding, Activity activity) {
        this.mActivityDemandChooseServiceBinding = activityDemandChooseServiceBinding;
        this.mActivity = activity;
        displayLoadLayer();
        initData();
        initView();
        initListener();
    }

    ArrayList<DemandPurposeListBean.PurposeInfo> listDemandChooseService = new ArrayList<DemandPurposeListBean.PurposeInfo>();
    ArrayList<RecommendServiceUserBean.ServiceUserInfo> listDemandChooseRecommendService = new ArrayList<RecommendServiceUserBean.ServiceUserInfo>();

    private void initData() {
        taskInfo = mActivity.getIntent().getExtras();
        tid = taskInfo.getLong("tid");
        type = taskInfo.getInt("type");
        roleid = taskInfo.getInt("roleid");
        LogKit.v("tid:" + tid + " " + type + " " + roleid);

        getTaskItemInfo();
    }


    private void initView() {

    }

    private void initListener() {
        mActivityDemandChooseServiceBinding.svDemandChooseService.setRefreshTask(new RefreshScrollView.IRefreshTask() {
            @Override
            public void refresh() {
                displayLoadLayer();
                getTaskItemInfo();
            }
        });
    }


    public void goBack(View v) {
        mActivity.finish();
    }

    public void gotoDemandDetail(View v) {
        Intent intentDeamndDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
        intentDeamndDetailActivity.putExtra("demandId", tid);
        mActivity.startActivity(intentDeamndDetailActivity);
    }


    //填充来竞标的服务方列表
    private void addDemandChooseServiceItems() {
        mActivityDemandChooseServiceBinding.llDemandChooseServiceList.removeAllViews();
        if (listDemandChooseService.size() <= 0) {
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llParams.topMargin = CommonUtils.dip2px(13);
            llParams.bottomMargin = CommonUtils.dip2px(15);
            TextView tvNoInfo = new TextView(CommonUtils.getContext());
            tvNoInfo.setLayoutParams(llParams);
            tvNoInfo.setText("暂无信息");
            tvNoInfo.setTextColor(0xff31C6E4);
            tvNoInfo.setTextSize(15);
            tvNoInfo.setGravity(Gravity.CENTER);
            mActivityDemandChooseServiceBinding.llDemandChooseServiceList.addView(tvNoInfo);
        } else {
            for (int i = 0; i < listDemandChooseService.size(); i++) {
                DemandPurposeListBean.PurposeInfo demandChooseServiceBean = listDemandChooseService.get(i);
                View itemDemandChooseService = inflateItemDemandChooseService(demandChooseServiceBean);
                if (itemDemandChooseService != null) {
                    mActivityDemandChooseServiceBinding.llDemandChooseServiceList.addView(itemDemandChooseService);
                }
            }
        }
    }

    //填充系统推荐的服务方列表
    private void addDemandChooseRecommendServiceItems() {
        mActivityDemandChooseServiceBinding.llDemandChooseRecommendServiceList.removeAllViews();
        for (int i = 0; i < listDemandChooseRecommendService.size(); i++) {
            RecommendServiceUserBean.ServiceUserInfo serviceUserInfo = listDemandChooseRecommendService.get(i);
            View itemDemandChooseRecommendService = inflateItemDemandChooseRecommendService(serviceUserInfo);
            if (itemDemandChooseRecommendService != null) {
                mActivityDemandChooseServiceBinding.llDemandChooseRecommendServiceList.addView(itemDemandChooseRecommendService);
            }
        }
    }

    private View inflateItemDemandChooseService(DemandPurposeListBean.PurposeInfo demandChooseServiceBean) {
        ItemDemandChooseServiceBinding itemDemandChooseServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_service, null, false);
        ItemDemandChooseServiceModel itemDemandChooseServiceModel = new ItemDemandChooseServiceModel(itemDemandChooseServiceBinding, mActivity, demandChooseServiceBean, tid, title);
        itemDemandChooseServiceBinding.setItemDemandChooseServiceModel(itemDemandChooseServiceModel);
        return itemDemandChooseServiceBinding.getRoot();
    }

    private View inflateItemDemandChooseRecommendService(RecommendServiceUserBean.ServiceUserInfo serviceUserInfo) {
        ItemDemandChooseRecommendServiceBinding itemDemandChooseRecommendServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_recommend_service, null, false);
        ItemDemandChooseRecommendServiceModel itemDemandChooseRecommendServiceModel = new ItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceBinding, mActivity, serviceUserInfo, chatCmdShareTaskBean);
        itemDemandChooseRecommendServiceBinding.setItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceModel);
        return itemDemandChooseRecommendServiceBinding.getRoot();
    }


    private String getDateTimeString(long mills, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(mills);
    }

    private String getBigStateTextByStatusCode(int status) {
        if (status == 1) {//已发布
            return "预约中";
        } else if (status == 2) {//已取消
            return "已过期";
        } else if (status == 3) {//被拒绝
            return "已过期";
        } else if (status == 4) {//待选择
            return "预约中";
        } else if (status == 5) {//待确认
            return "预约中";
        } else if (status == 6) {//待支付
            return "待支付";
        } else if (status == 7) {//进行中
            return "服务中";
        } else if (status == 8) {//已完成 已结束  注意指的是 : 全部分期被需求方确认后
            return "待评价";
        } else if (status == 9) {//退款中
            return "服务中";
        } else if (status == 10) {//已退款
            return "已过期";
        } else if (status == 11) {//已淘汰
            return "已过期";
        }
        return "";
    }

    private void getTaskItemInfo() {
        MyTaskEngine.getMyTaskItem(new BaseProtocol.IResultExecutor<MyTaskItemBean>() {
            @Override
            public void execute(MyTaskItemBean myTaskItemBean) {
                MyTaskBean dataBean = myTaskItemBean.data.taskinfo;
                if (!(dataBean.status == 1 || dataBean.status == 4 || dataBean.status == 5)) {
                    Intent intentMyPublishDemandActivity = new Intent(CommonUtils.getContext(), MyPublishDemandActivity.class);
                    intentMyPublishDemandActivity.putExtras(taskInfo);
                    mActivity.startActivity(intentMyPublishDemandActivity);
                    mActivity.finish();
                    return;
                }
                //设置是否显示+V
                if (dataBean.isauth == 1) {
                    setIsAuthVisibility(View.VISIBLE);
                } else {
                    setIsAuthVisibility(View.INVISIBLE);
                }

                setUsername(dataBean.name);
//                setTitle(dataBean.title + "订单");
                setTitle(dataBean.title);

//                String startTimeStr = getDateTimeString(dataBean.starttime, "开始时间:yyyy年MM月dd日 HH:mm");
//                setStartTime(startTimeStr);
                //因为发布需求时去掉了自己选择时间，所以这里肯定是“随时”
                setStartTime("开始时间:随时");

                if (dataBean.quote <= 0) {
                    setQuote("服务方报价");
                } else {
                    setQuote((int) dataBean.quote + "元");
                }

                if (dataBean.instalment == 1) {
                    setIsInstalmentVisibility(View.VISIBLE);
                } else {
                    setIsInstalmentVisibility(View.GONE);
                }
                //设置头像(这是我发布的需求任务的意向单列表，这页面只有我能看到，所以当前登录者就是需求发布者)
                BitmapKit.bindImage(mActivityDemandChooseServiceBinding.ivDemandUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);

                //这里不需要做判断，因为当前页面只可能是“预约中”状态
//                String stateText = getBigStateTextByStatusCode(dataBean.status);
//                setBigState(stateText);
//                setBigState("预约中");
                setBigState("待抢单");
                //需求分享实体，可以分享给推荐的优秀服务者
                chatCmdShareTaskBean = new ChatCmdShareTaskBean();
                chatCmdShareTaskBean.uid = LoginManager.currentLoginUserId;
                chatCmdShareTaskBean.avatar = LoginManager.currentLoginUserAvatar;
                chatCmdShareTaskBean.title = dataBean.title;
                chatCmdShareTaskBean.quote = getQuote();
                chatCmdShareTaskBean.type = type;//服务或者需求
                chatCmdShareTaskBean.tid = tid;

                getDemandChooseServiceList();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("加载任务item信息失败:" + result);
            }
        }, tid + "", type + "", roleid + "");
    }

    private void getDemandChooseServiceList() {
        DemandEngine.getDemandPurposeList(new BaseProtocol.IResultExecutor<DemandPurposeListBean>() {
            @Override
            public void execute(DemandPurposeListBean dataBean) {
                listDemandChooseService = dataBean.data.purpose.list;
                addDemandChooseServiceItems();
                hideLoadLayer();//在这里显示就可以了，优秀服务方推荐列表再慢慢显示
                getDemandChooseRecommendServiceList();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取抢单服务者列表失败:" + result);
            }
        }, tid + "");
    }

    private void getDemandChooseRecommendServiceList() {
        DemandEngine.getRecommendServiceUser(new BaseProtocol.IResultExecutor<RecommendServiceUserBean>() {
            @Override
            public void execute(RecommendServiceUserBean dataBean) {
                listDemandChooseRecommendService = dataBean.data.list;
                addDemandChooseRecommendServiceItems();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取优秀服务方推荐失败:" + result);
            }
        }, tid + "", "5");
    }

    /**
     * 刚进入页面时，显示加载层
     */
    private void displayLoadLayer() {
        setLoadLayerVisibility(View.VISIBLE);
    }

    /**
     * 数据加载完毕后,隐藏加载层
     */
    private void hideLoadLayer() {
        setLoadLayerVisibility(View.GONE);
    }

    private int isAuthVisibility = View.VISIBLE;//是否显示认证
    private String username;
    private String title;
    private String startTime;
    private String quote;
    private int isInstalmentVisibility = View.GONE;
    private String bigState;
    private int loadLayerVisibility;

    @Bindable
    public int getLoadLayerVisibility() {
        return loadLayerVisibility;
    }

    public void setLoadLayerVisibility(int loadLayerVisibility) {
        this.loadLayerVisibility = loadLayerVisibility;
        notifyPropertyChanged(BR.loadLayerVisibility);
    }

    @Bindable
    public int getIsAuthVisibility() {
        return isAuthVisibility;
    }

    public void setIsAuthVisibility(int isAuthVisibility) {
        this.isAuthVisibility = isAuthVisibility;
        notifyPropertyChanged(BR.isAuthVisibility);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        notifyPropertyChanged(BR.startTime);
    }

    @Bindable
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
        notifyPropertyChanged(BR.quote);
    }

    @Bindable
    public int getIsInstalmentVisibility() {
        return isInstalmentVisibility;
    }

    public void setIsInstalmentVisibility(int isInstalmentVisibility) {
        this.isInstalmentVisibility = isInstalmentVisibility;
        notifyPropertyChanged(BR.isInstalmentVisibility);
    }

    @Bindable
    public String getBigState() {
        return bigState;
    }

    public void setBigState(String bigState) {
        this.bigState = bigState;
        notifyPropertyChanged(BR.bigState);
    }
}
