package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHome2Binding;
import com.slash.youth.ui.pager.BaseHomePager;
import com.slash.youth.ui.pager.HomeFreeTimePager;
import com.slash.youth.ui.pager.HomeMyPager;
import com.slash.youth.ui.pager.HomeWorkbenchPager;

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

    public ActivityHomeModel2(ActivityHome2Binding activityHome2Binding, Activity activity) {
        this.mActivityHome2Binding = activityHome2Binding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {
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
                if (position == 0) {
                    setBottomTabIcon(R.mipmap.found_activation, R.mipmap.home_message_btn, R.mipmap.home_wode_btn);
                    setBottomTabTextColor(0xff31c5e4, 0xff666666, 0xff666666);
                } else if (position == 1) {
                    setBottomTabIcon(R.mipmap.found_default, R.mipmap.icon_message_press, R.mipmap.home_wode_btn);
                    setBottomTabTextColor(0xff666666, 0xff31c5e4, 0xff666666);
                } else if (position == 2) {
                    setBottomTabIcon(R.mipmap.found_default, R.mipmap.home_message_btn, R.mipmap.icon_my_center_press);
                    setBottomTabTextColor(0xff666666, 0xff666666, 0xff31c5e4);
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
        listPagers.add(new HomeMyPager(mActivity));
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
        setBottomTabIcon(R.mipmap.found_activation, R.mipmap.home_message_btn, R.mipmap.home_wode_btn);
        setBottomTabTextColor(0xff31c5e4, 0xff666666, 0xff666666);
        mActivityHome2Binding.vpHomePager.setCurrentItem(0);
    }

    /**
     * 切换到工作台
     *
     * @param v
     */
    public void checkedWorkbench(View v) {
        setBottomTabIcon(R.mipmap.found_default, R.mipmap.icon_message_press, R.mipmap.home_wode_btn);
        setBottomTabTextColor(0xff666666, 0xff31c5e4, 0xff666666);
        mActivityHome2Binding.vpHomePager.setCurrentItem(1);
    }

    /**
     * 切换到我的页面
     *
     * @param v
     */
    public void checkedMyPager(View v) {
        setBottomTabIcon(R.mipmap.found_default, R.mipmap.home_message_btn, R.mipmap.icon_my_center_press);
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

}
