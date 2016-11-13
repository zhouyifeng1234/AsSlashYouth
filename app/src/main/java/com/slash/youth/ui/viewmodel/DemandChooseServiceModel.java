package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandChooseServiceBinding;
import com.slash.youth.databinding.ItemDemandChooseRecommendServiceBinding;
import com.slash.youth.databinding.ItemDemandChooseServiceBinding;
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class DemandChooseServiceModel extends BaseObservable {

    ActivityDemandChooseServiceBinding mActivityDemandChooseServiceBinding;
    Activity mActivity;
    long tid;//需求ID
    int type;//取值范围只能是: 1需求 2服务
    int roleid;//表示是'我抢的单子' 还是 '我发布的任务' 1发布者 2抢单者

    public DemandChooseServiceModel(ActivityDemandChooseServiceBinding activityDemandChooseServiceBinding, Activity activity) {
        this.mActivityDemandChooseServiceBinding = activityDemandChooseServiceBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    ArrayList<DemandPurposeListBean.PurposeInfo> listDemandChooseService = new ArrayList<DemandPurposeListBean.PurposeInfo>();
    ArrayList<DemandPurposeListBean> listDemandChooseRecommendService = new ArrayList<DemandPurposeListBean>();

    private void initData() {
        Bundle taskInfo = mActivity.getIntent().getExtras();
        tid = taskInfo.getLong("tid");
        type = taskInfo.getInt("type");
        roleid = taskInfo.getInt("roleid");
        LogKit.v("tid:" + tid + " " + type + " " + roleid);

        getTaskItemInfo();

        getDemandChooseServiceList();
        getDemandChooseRecommendServiceList();
    }


    private void initView() {
//        addDemandChooseServiceItems();//实际应该在异步请求到数据后调用
//        addDemandChooseRecommendServiceItems();//实际应该在异步请求到数据后调用
    }


    public void goBack(View v) {
        mActivity.finish();
    }


    //填充来竞标的服务方列表
    private void addDemandChooseServiceItems() {
        for (int i = 0; i < listDemandChooseService.size(); i++) {
            DemandPurposeListBean.PurposeInfo demandChooseServiceBean = listDemandChooseService.get(i);
            View itemDemandChooseService = inflateItemDemandChooseService(demandChooseServiceBean);
            if (itemDemandChooseService != null) {
                mActivityDemandChooseServiceBinding.llDemandChooseServiceList.addView(itemDemandChooseService);
            }
        }
    }

    //填充系统推荐的服务方列表
    private void addDemandChooseRecommendServiceItems() {
        for (int i = 0; i < listDemandChooseRecommendService.size(); i++) {
            DemandPurposeListBean demandChooseRecommendServiceBean = listDemandChooseRecommendService.get(i);
            View itemDemandChooseRecommendService = inflateItemDemandChooseRecommendService(demandChooseRecommendServiceBean);
            if (itemDemandChooseRecommendService != null) {
                mActivityDemandChooseServiceBinding.llDemandChooseRecommendServiceList.addView(itemDemandChooseRecommendService);
            }
        }
    }

    private View inflateItemDemandChooseService(DemandPurposeListBean.PurposeInfo demandChooseServiceBean) {
        ItemDemandChooseServiceBinding itemDemandChooseServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_service, null, false);
        ItemDemandChooseServiceModel itemDemandChooseServiceModel = new ItemDemandChooseServiceModel(itemDemandChooseServiceBinding, mActivity, demandChooseServiceBean,tid);
        itemDemandChooseServiceBinding.setItemDemandChooseServiceModel(itemDemandChooseServiceModel);
        return itemDemandChooseServiceBinding.getRoot();
    }

    private View inflateItemDemandChooseRecommendService(DemandPurposeListBean demandChooseRecommendServiceBean) {
        ItemDemandChooseRecommendServiceBinding itemDemandChooseRecommendServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_recommend_service, null, false);
        ItemDemandChooseRecommendServiceModel itemDemandChooseRecommendServiceModel = new ItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceBinding, mActivity, demandChooseRecommendServiceBean);
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
                //设置是否显示+V
                if (dataBean.isauth == 1) {
                    setIsAuthVisibility(View.VISIBLE);
                } else {
                    setIsAuthVisibility(View.INVISIBLE);
                }

                setUsername(dataBean.name);
                setTitle(dataBean.title);

                String startTimeStr = getDateTimeString(dataBean.starttime, "开始时间:yyyy年MM月dd日 HH:mm");
                setStartTime(startTimeStr);

                setQuote(dataBean.quote + "元");

                if (dataBean.instalment == 1) {
                    setIsInstalmentVisibility(View.VISIBLE);
                } else {
                    setIsInstalmentVisibility(View.GONE);
                }

                //这里不需要做判断，因为当前页面只可能是“预约中”状态
//                String stateText = getBigStateTextByStatusCode(dataBean.status);
//                setBigState(stateText);
                setBigState("预约中");
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", type + "", roleid + "");
    }

    private void getDemandChooseServiceList() {
        //模拟数据，实际应该由服务端接口返回
//        listDemandChooseService.add(new DemandPurposeListBean());
//        listDemandChooseService.add(new DemandPurposeListBean());
//        listDemandChooseService.add(new DemandPurposeListBean());
//        listDemandChooseService.add(new DemandPurposeListBean());
//        listDemandChooseService.add(new DemandPurposeListBean());
        DemandEngine.getDemandPurposeList(new BaseProtocol.IResultExecutor<DemandPurposeListBean>() {
            @Override
            public void execute(DemandPurposeListBean dataBean) {
                listDemandChooseService = dataBean.data.purpose.list;
                addDemandChooseServiceItems();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    private void getDemandChooseRecommendServiceList() {
        //模拟数据，实际应该由服务端接口返回
//        listDemandChooseRecommendService.add(new DemandPurposeListBean());
//        listDemandChooseRecommendService.add(new DemandPurposeListBean());
//        listDemandChooseRecommendService.add(new DemandPurposeListBean());
//        listDemandChooseRecommendService.add(new DemandPurposeListBean());
//        listDemandChooseRecommendService.add(new DemandPurposeListBean());
    }

    private int isAuthVisibility = View.VISIBLE;//是否显示认证
    private String username;
    private String title;
    private String startTime;
    private String quote;
    private int isInstalmentVisibility = View.GONE;
    private String bigState;


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
