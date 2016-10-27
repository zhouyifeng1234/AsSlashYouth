package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandChooseServiceBinding;
import com.slash.youth.databinding.ItemDemandChooseRecommendServiceBinding;
import com.slash.youth.databinding.ItemDemandChooseServiceBinding;
import com.slash.youth.domain.DemandChooseServiceBean;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class DemandChooseServiceModel extends BaseObservable {

    ActivityDemandChooseServiceBinding mActivityDemandChooseServiceBinding;
    Activity mActivity;

    public DemandChooseServiceModel(ActivityDemandChooseServiceBinding activityDemandChooseServiceBinding, Activity activity) {
        this.mActivityDemandChooseServiceBinding = activityDemandChooseServiceBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    ArrayList<DemandChooseServiceBean> listDemandChooseService = new ArrayList<DemandChooseServiceBean>();
    ArrayList<DemandChooseServiceBean> listDemandChooseRecommendService = new ArrayList<DemandChooseServiceBean>();

    private void initData() {
        getDemandChooseServiceList();
        getDemandChooseRecommendServiceList();
    }


    private void initView() {
        addDemandChooseServiceItems();
        addDemandChooseRecommendServiceItems();
    }


    public void goBack(View v) {
        mActivity.finish();
    }


    //填充来竞标的服务方列表
    private void addDemandChooseServiceItems() {
        for (int i = 0; i < listDemandChooseService.size(); i++) {
            DemandChooseServiceBean demandChooseServiceBean = listDemandChooseService.get(i);
            View itemDemandChooseService = inflateItemDemandChooseService(demandChooseServiceBean);
            if (itemDemandChooseService != null) {
                mActivityDemandChooseServiceBinding.llDemandChooseServiceList.addView(itemDemandChooseService);
            }
        }
    }

    //填充系统推荐的服务方列表
    private void addDemandChooseRecommendServiceItems() {
        for (int i = 0; i < listDemandChooseRecommendService.size(); i++) {
            DemandChooseServiceBean demandChooseRecommendServiceBean = listDemandChooseRecommendService.get(i);
            View itemDemandChooseRecommendService = inflateItemDemandChooseRecommendService(demandChooseRecommendServiceBean);
            if (itemDemandChooseRecommendService != null) {
                mActivityDemandChooseServiceBinding.llDemandChooseRecommendServiceList.addView(itemDemandChooseRecommendService);
            }
        }
    }

    private View inflateItemDemandChooseService(DemandChooseServiceBean demandChooseServiceBean) {
        ItemDemandChooseServiceBinding itemDemandChooseServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_service, null, false);
        ItemDemandChooseServiceModel itemDemandChooseServiceModel = new ItemDemandChooseServiceModel(itemDemandChooseServiceBinding, mActivity, demandChooseServiceBean);
        itemDemandChooseServiceBinding.setItemDemandChooseServiceModel(itemDemandChooseServiceModel);
        return itemDemandChooseServiceBinding.getRoot();
    }

    private View inflateItemDemandChooseRecommendService(DemandChooseServiceBean demandChooseRecommendServiceBean) {
        ItemDemandChooseRecommendServiceBinding itemDemandChooseRecommendServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_recommend_service, null, false);
        ItemDemandChooseRecommendServiceModel itemDemandChooseRecommendServiceModel = new ItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceBinding, mActivity, demandChooseRecommendServiceBean);
        itemDemandChooseRecommendServiceBinding.setItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceModel);
        return itemDemandChooseRecommendServiceBinding.getRoot();
    }

    private void getDemandChooseServiceList() {
        //模拟数据，实际应该由服务端接口返回
        listDemandChooseService.add(new DemandChooseServiceBean());
        listDemandChooseService.add(new DemandChooseServiceBean());
        listDemandChooseService.add(new DemandChooseServiceBean());
        listDemandChooseService.add(new DemandChooseServiceBean());
        listDemandChooseService.add(new DemandChooseServiceBean());
    }

    private void getDemandChooseRecommendServiceList() {
        //模拟数据，实际应该由服务端接口返回
        listDemandChooseRecommendService.add(new DemandChooseServiceBean());
        listDemandChooseRecommendService.add(new DemandChooseServiceBean());
        listDemandChooseRecommendService.add(new DemandChooseServiceBean());
        listDemandChooseRecommendService.add(new DemandChooseServiceBean());
        listDemandChooseRecommendService.add(new DemandChooseServiceBean());
    }
}
