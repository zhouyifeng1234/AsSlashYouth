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
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class DemandChooseServiceModel extends BaseObservable {

    ActivityDemandChooseServiceBinding mActivityDemandChooseServiceBinding;
    Activity mActivity;
    long id;//需求ID

    public DemandChooseServiceModel(ActivityDemandChooseServiceBinding activityDemandChooseServiceBinding, Activity activity) {
        this.mActivityDemandChooseServiceBinding = activityDemandChooseServiceBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    ArrayList<DemandPurposeListBean.PurposeInfo> listDemandChooseService = new ArrayList<DemandPurposeListBean.PurposeInfo>();
    ArrayList<DemandPurposeListBean> listDemandChooseRecommendService = new ArrayList<DemandPurposeListBean>();

    private void initData() {
        id = 20;//需求ID应该通过intent传递过来，暂时写死，方便测试

        getDemandChooseServiceList();
        getDemandChooseRecommendServiceList();
    }


    private void initView() {
//        addDemandChooseServiceItems();
        addDemandChooseRecommendServiceItems();
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
        ItemDemandChooseServiceModel itemDemandChooseServiceModel = new ItemDemandChooseServiceModel(itemDemandChooseServiceBinding, mActivity, demandChooseServiceBean);
        itemDemandChooseServiceBinding.setItemDemandChooseServiceModel(itemDemandChooseServiceModel);
        return itemDemandChooseServiceBinding.getRoot();
    }

    private View inflateItemDemandChooseRecommendService(DemandPurposeListBean demandChooseRecommendServiceBean) {
        ItemDemandChooseRecommendServiceBinding itemDemandChooseRecommendServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_choose_recommend_service, null, false);
        ItemDemandChooseRecommendServiceModel itemDemandChooseRecommendServiceModel = new ItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceBinding, mActivity, demandChooseRecommendServiceBean);
        itemDemandChooseRecommendServiceBinding.setItemDemandChooseRecommendServiceModel(itemDemandChooseRecommendServiceModel);
        return itemDemandChooseRecommendServiceBinding.getRoot();
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
        }, id + "");
    }

    private void getDemandChooseRecommendServiceList() {
        //模拟数据，实际应该由服务端接口返回
        listDemandChooseRecommendService.add(new DemandPurposeListBean());
        listDemandChooseRecommendService.add(new DemandPurposeListBean());
        listDemandChooseRecommendService.add(new DemandPurposeListBean());
        listDemandChooseRecommendService.add(new DemandPurposeListBean());
        listDemandChooseRecommendService.add(new DemandPurposeListBean());
    }
}
