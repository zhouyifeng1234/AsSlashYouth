package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.PagerHomeInfoBinding;
import com.slash.youth.domain.HomeInfoBean;
import com.slash.youth.ui.adapter.HomeInfoListAdapter;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeInfoModel extends BaseObservable {
    PagerHomeInfoBinding mPagerHomeInfoBinding;
    Activity mActivity;

    public PagerHomeInfoModel(PagerHomeInfoBinding pagerHomeInfoBinding, Activity activity) {
        this.mPagerHomeInfoBinding = pagerHomeInfoBinding;
        this.mActivity = activity;
        initView();
    }

    ArrayList<HomeInfoBean> listHomeInfoBean = new ArrayList<HomeInfoBean>();

    private void initView() {
        getDataFromServer();
        mPagerHomeInfoBinding.lvPagerHomeInfo.setAdapter(new HomeInfoListAdapter(listHomeInfoBean));
    }

    public void getDataFromServer() {
        //模拟数据
        listHomeInfoBean.add(new HomeInfoBean(true, false, "", false));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "小曹", false));
        listHomeInfoBean.add(new HomeInfoBean(false, false, "大曹", true));
        listHomeInfoBean.add(new HomeInfoBean(false, true, "曹文成", true));
    }
}
