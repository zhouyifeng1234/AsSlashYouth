package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHome2Binding;
import com.slash.youth.domain.MyHomeInfoBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.ui.pager.HomeWorkbenchPager;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * V1.1改版的新版本
 * <p/>
 * Created by zhouyifeng on 2017/2/27.
 */
public class ActivityHomeModel2 extends BaseObservable {

    ActivityHome2Binding mActivityHome2Binding;
    Activity mActivity;
    ArrayList<BaseHomePager> listPagers = new ArrayList<BaseHomePager>();

    HomeMyPager homeMyPager;

    public ActivityHomeModel2(ActivityHome2Binding activityHome2Binding, Activity activity) {
        this.mActivityHome2Binding = activityHome2Binding;
        this.mActivity = activity;
        getCurrentLoginUserInfo();//二、[用戶信息]-获取个人资料 接口中的数据，获取当前登录者的个人信息
        getCurrentLoginUserInfo2();//十三、[用戶信息]-我的首页数据  接口中的数据，用来获取手机号
        initData();
        initView();
        initListener();
    }

    private void initData() {
        //把用户的登录信息保存到SharePreferences中
        SpUtils.setLong("uid", LoginManager.currentLoginUserId);
        SpUtils.setString("token", LoginManager.token);
        SpUtils.setString("rongToken", LoginManager.rongToken);

        initPagers();
        mActivityHome2Binding.vpHomePager.setAdapter(new HomePagerAdapter());
        //默认选中第一页
        mActivityHome2Binding.vpHomePager.setCurrentItem(0);
    }

    private void initView() {
        //默认选中发现tab
        checkedFind(null);
    }

    private void initListener() {
        mActivityHome2Binding.vpHomePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogKit.v("--------------onPageSelected----------------");
                if (position == 0) {
                    setBottomTabIcon(R.mipmap.found_icon_activation, R.mipmap.task_icon_unactivation, R.mipmap.my_center_icon);
                    setBottomTabTextColor(0xff31c5e4, 0xff666666, 0xff666666);
                } else if (position == 1) {
                    setBottomTabIcon(R.mipmap.found_icon, R.mipmap.task_icon_activation, R.mipmap.my_center_icon);
                    setBottomTabTextColor(0xff666666, 0xff31c5e4, 0xff666666);
                } else if (position == 2) {
                    setBottomTabIcon(R.mipmap.found_icon, R.mipmap.task_icon_unactivation, R.mipmap.my_center_icon_activation);
                    setBottomTabTextColor(0xff666666, 0xff666666, 0xff31c5e4);
                    BaseHomePager baseHomePager = listPagers.get(position);
                    if (baseHomePager instanceof HomeMyPager) {//这个条件正常情况应该是肯定成立的
                        HomeMyPager homeMyPager = (HomeMyPager) baseHomePager;
                        homeMyPager.doMarksAnimation();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPagers() {
        //首页目前是3个
        listPagers.add(new HomeFreeTimePager(mActivity));
        listPagers.add(new HomeWorkbenchPager(mActivity));
        homeMyPager = new HomeMyPager(mActivity);
        listPagers.add(homeMyPager);
    }

    public void updateMessage() {
        homeMyPager.updateMessage();
    }

    public BaseHomePager getCurrentPager() {
        if (listPagers != null && listPagers.size() > 0) {
            int currentItem = mActivityHome2Binding.vpHomePager.getCurrentItem();
            currentItem = currentItem % listPagers.size();
            return listPagers.get(currentItem);
        } else {
            return null;
        }
    }

    /**
     * 切换到发现
     *
     * @param v
     */
    public void checkedFind(View v) {
        setBottomTabIcon(R.mipmap.found_icon_activation, R.mipmap.task_icon_unactivation, R.mipmap.my_center_icon);
        setBottomTabTextColor(0xff31c5e4, 0xff666666, 0xff666666);
        mActivityHome2Binding.vpHomePager.setCurrentItem(0);
    }

    /**
     * 切换到工作台
     *
     * @param v
     */
    public void checkedWorkbench(View v) {
        setBottomTabIcon(R.mipmap.found_icon, R.mipmap.task_icon_activation, R.mipmap.my_center_icon);
        setBottomTabTextColor(0xff666666, 0xff31c5e4, 0xff666666);
        mActivityHome2Binding.vpHomePager.setCurrentItem(1);
    }

    /**
     * 切换到我的页面
     *
     * @param v
     */
    public void checkedMyPager(View v) {
        setBottomTabIcon(R.mipmap.found_icon, R.mipmap.task_icon_unactivation, R.mipmap.my_center_icon_activation);
        setBottomTabTextColor(0xff666666, 0xff666666, 0xff31c5e4);
        mActivityHome2Binding.vpHomePager.setCurrentItem(2);
    }

    private void setBottomTabIcon(int findIconResId, int workbenchResId, int myResId) {
        mActivityHome2Binding.ivFindIcon.setImageResource(findIconResId);
        mActivityHome2Binding.ivWorkbenchIcon.setImageResource(workbenchResId);
        mActivityHome2Binding.ivMyIcon.setImageResource(myResId);
    }

    private void setBottomTabTextColor(int findTextColor, int workbenchTextColor, int myTextColor) {
        mActivityHome2Binding.tvFind.setTextColor(findTextColor);
        mActivityHome2Binding.tvWorkbench.setTextColor(workbenchTextColor);
        mActivityHome2Binding.tvMy.setTextColor(myTextColor);
    }

    private class HomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseHomePager baseHomePager = listPagers.get(position);
            View rootView = baseHomePager.getRootView();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void getCurrentLoginUserInfo() {
        UserInfoEngine.getMyUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                LoginManager.currentLoginUserAvatar = dataBean.data.uinfo.avatar;
                LoginManager.currentLoginUserName = dataBean.data.uinfo.name;
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取我的个人信息失败:" + result);
            }
        });
    }

    private void getCurrentLoginUserInfo2() {
        UserInfoEngine.getMyHomeInfo(new BaseProtocol.IResultExecutor<MyHomeInfoBean>() {
            @Override
            public void execute(MyHomeInfoBean dataBean) {
                LoginManager.currentLoginUserPhone = dataBean.data.myinfo.phone;
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取我的首页数据失败:" + result);
            }
        });
    }
}
