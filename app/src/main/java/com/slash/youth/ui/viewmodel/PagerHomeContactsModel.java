package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewHomeContactsBinding;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.ContactsMyVisitorProtocol;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.adapter.HomeContactsVisitorAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeContactsModel extends BaseObservable {
    PagerHomeContactsBinding mPagerHomeContactsBinding;
    Activity mActivity;
    private  ArrayList<HomeContactsVisitorBean> listHomeContactsVisitorBean = new ArrayList<HomeContactsVisitorBean>();

    public PagerHomeContactsModel(PagerHomeContactsBinding pagerHomeContactsBinding, Activity activity) {
        this.mPagerHomeContactsBinding = pagerHomeContactsBinding;
        this.mActivity = activity;
        initView();
        initData();
    }

    private void initView() {
        HeaderListviewHomeContactsBinding headerListviewHomeContactsBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.header_listview_home_contacts, null, false);
        HeaderHomeContactsModel headerHomeContactsModel = new HeaderHomeContactsModel(headerListviewHomeContactsBinding);
        headerListviewHomeContactsBinding.setHeaderHomeContactsModel(headerHomeContactsModel);
        View vContactsHeader = headerListviewHomeContactsBinding.getRoot();
        mPagerHomeContactsBinding.lvHomeContactsVisitor.addHeaderView(vContactsHeader);
    }

    private void initData() {
        getDataFromServer();
        //设置访客列表数据
        mPagerHomeContactsBinding.lvHomeContactsVisitor.setAdapter(new HomeContactsVisitorAdapter(listHomeContactsVisitorBean));
    }

    public void getDataFromServer() {
        int offset = 0;
        int limit = 20;
        //ContactsManager.getMyVisitorList(new onGetMyVisitorList(),offset,limit);
        offset = offset+limit;

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
    }

    //获取我的访客的列表
    public class onGetMyVisitorList implements BaseProtocol.IResultExecutor<HomeContactsVisitorBean> {
        @Override
        public void execute(HomeContactsVisitorBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                HomeContactsVisitorBean.DataBean data = dataBean.getData();
                List<HomeContactsVisitorBean.DataBean.ListBean> list = data.getList();
                for (HomeContactsVisitorBean.DataBean.ListBean listBean : list) {
                    String avatar = listBean.getAvatar();
                    String company = listBean.getCompany();
                    String direction = listBean.getDirection();
                    String industry = listBean.getIndustry();
                    int isauth = listBean.getIsauth();
                    String name = listBean.getName();
                    String position = listBean.getPosition();
                    int uid = listBean.getUid();
                    long uts = listBean.getUts();
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }



}
