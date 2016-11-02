package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.FloatViewBinding;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.UserInfoAdapter;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/1.
 */
public class ActivityUserInfoModel extends BaseObservable {

    private ActivityUserinfoBinding activityUserinfoBinding;
    private  ArrayList<UserInfoItemBean> userInfoListView = new ArrayList<>();
    private UserInfoAdapter userInfoAdapter;
    private FloatViewBinding floatViewBinding;

    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        initData();
        initView();
    }

    //加载界面
    private void initView() {
    //加载Listview布局
    userInfoAdapter = new UserInfoAdapter(userInfoListView);
    activityUserinfoBinding.lvUserinfo.setAdapter(userInfoAdapter);

    //手动ScrollView设置到顶部
    activityUserinfoBinding.sv.smoothScrollTo(0,0);

    //listview的设置不可滑动
    activityUserinfoBinding.lvUserinfo.setOnTouchListener(new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    });


    }

    //加载数据
    private void initData() {
        //网络获取数据
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
    }





}
