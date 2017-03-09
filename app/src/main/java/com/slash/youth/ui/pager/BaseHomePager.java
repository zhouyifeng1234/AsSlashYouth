package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeBaseBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.ui.adapter.PagerHomeDemandtAdapter;
import com.slash.youth.ui.view.RefreshListView;
import com.slash.youth.ui.viewmodel.PagerHomeBaseModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
abstract public class BaseHomePager {

    private View rootView;
    private PagerHomeBaseBinding mPagerHomeBaseBinding;
    public ArrayList<String> homeAdvertisementUrlList;
    public Activity mActivity;
    private static double currentLatitude;
    private static double currentLongitude;

    public Runnable runnable;//回调,执行网络数据加载完毕后的一些回调操作

    public BaseHomePager(Activity activity) {
//        this.mActivity = activity;
//        rootView = initView();
//        initListener();
//        setData();
        this(activity, null);
    }

    public BaseHomePager(Activity activity, Runnable runnable) {
        this.runnable = runnable;
        this.mActivity = activity;
        rootView = initView();
        initListener();
        setData();
    }


    public View initView() {
        mPagerHomeBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_base, null, false);
        PagerHomeBaseModel pagerHomeBaseModel = new PagerHomeBaseModel(mPagerHomeBaseBinding);
        mPagerHomeBaseBinding.setPagerHomeBaseModel(pagerHomeBaseModel);
        pagerHomeBaseModel.browseDemandFocused();
        mPagerHomeBaseBinding.hsvPagerHomeBaseFilterSkilllabel.setHorizontalScrollBarEnabled(false);
        pagerHomeBaseModel.initView();
        return mPagerHomeBaseBinding.getRoot();
    }

    public void initListener() {
        mPagerHomeBaseBinding.vpPagerHomeBaseAdvertisement.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int childCount = mPagerHomeBaseBinding.llPagerHomeBaseAdvindicators.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ImageView ivIndicator = (ImageView) mPagerHomeBaseBinding.llPagerHomeBaseAdvindicators.getChildAt(i);
                    if (i == position) {
                        ivIndicator.setImageResource(R.mipmap.indicator_circle_choose);
                    } else {
                        ivIndicator.setImageResource(R.mipmap.indicator_circle_unchoose);
                    }
                }
                LogKit.v(position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mPagerHomeBaseBinding.llPagerHomeBaseHomecontent.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LogKit.v("onTouch");
//                return true;
//            }
//        });

    }

    ArrayList<DemandBean> listDemand = new ArrayList<DemandBean>();
    PagerHomeDemandtAdapter pagerHomeDemandtAdapter;

    //    abstract public void setData();
    public void setData() {
        mPagerHomeBaseBinding.vpPagerHomeBaseAdvertisement.setAdapter(new HomeAdvertisementAdapter());
//        getDataFromServer();//由子类实现，去服务端请求数据
        getDataFromServer(true);
        pagerHomeDemandtAdapter = new PagerHomeDemandtAdapter(listDemand);
        mPagerHomeBaseBinding.lvPagerHomeBaseContent.setAdapter(pagerHomeDemandtAdapter);
        mPagerHomeBaseBinding.lvPagerHomeBaseContent.setRefreshDataTask(new RefreshDataTask());
        mPagerHomeBaseBinding.lvPagerHomeBaseContent.setLoadMoreNewsTast(new LoadMoreNewsTask());
    }

//    abstract public DemandBean getDataFromServer();


    public View getRootView() {
        return rootView;
    }

    public class HomeAdvertisementAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
//            return homeAdvertisementUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //TODO 真是图片需要从服务端接口请求，由于相关接口目前没有完成，暂时先用本地图片实现效果
            ImageView ivHomeAdvertisementTest = new ImageView(CommonUtils.getContext());
            ivHomeAdvertisementTest.setImageResource(R.mipmap.banner);
            container.addView(ivHomeAdvertisementTest);
            return ivHomeAdvertisementTest;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    /**
     * 下拉刷新执行的回调，执行结束后需要调用refreshDataFinish()方法，用来更新状态
     */
    public class RefreshDataTask implements RefreshListView.IRefreshDataTask {

        @Override
        public void refresh() {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataFromServer(true);
                    pagerHomeDemandtAdapter.notifyDataSetChanged();
                    mPagerHomeBaseBinding.lvPagerHomeBaseContent.refreshDataFinish();
                }
            }, 2000);
        }
    }

    /**
     * 上拉加载更多执行的回调，执行完毕后需要调用loadMoreNewsFinished()方法，用来更新状态,如果加载到最后一页，则需要调用setLoadToLast()方法
     */
    public class LoadMoreNewsTask implements RefreshListView.ILoadMoreNewsTask {

        @Override
        public void loadMore() {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataFromServer(false);
                    pagerHomeDemandtAdapter.notifyDataSetChanged();
                    mPagerHomeBaseBinding.lvPagerHomeBaseContent.loadMoreNewsFinished();
                    //如果加载到最后一页，需要调用setLoadToLast()方法
                }
            }, 2000);
        }
    }

    /**
     * @param isRefresh true表示为下拉刷新或者第一次初始化加载数据操作，false表示加载更多数据
     */
    public void getDataFromServer(boolean isRefresh) {
        if (isRefresh) {
            listDemand.clear();
        }
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
    }

}
