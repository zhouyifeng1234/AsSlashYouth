package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeFreetimeBinding;
import com.slash.youth.domain.BannerConfigBean;
import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.domain.HomeRecommendList2;
import com.slash.youth.domain.HomeTagInfoBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.activity.PublishActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.TagRecommendActivity;
import com.slash.youth.ui.activity.WebViewActivity;
import com.slash.youth.ui.adapter.HomeDemandAdapter;
import com.slash.youth.ui.adapter.HomeServiceAdapter;
import com.slash.youth.ui.view.DemandServiceToggleView;
import com.slash.youth.ui.view.NewRefreshListView;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeFreeTimeModel extends BaseObservable {
    private PagerHomeFreetimeBinding pagerHomeFreetimeBinding;
    public Activity mActivity;
    private ArrayList<FreeTimeServiceBean.DataBean.ListBean> listServiceBean = new ArrayList<>();
    private ArrayList<FreeTimeDemandBean.DataBean.ListBean> listDemandBean = new ArrayList<>();
    private ArrayList<Bitmap> bannerList = new ArrayList<>();
    private static ArrayList<String> titleArrayList = new ArrayList<>();
    private static ArrayList<String> imageArrayList = new ArrayList<>();
    private static ArrayList<String> urlArrayList = new ArrayList<>();
    public boolean mIsDisplayDemandList = true;//如果存true，表示展示需求列表，false为展示服务列表,默认为true
    private int limit = 10;
    private HomeDemandAdapter homeDemandAndDemandAdapter;
    private HomeServiceAdapter homeServiceAdapter;
    private View listMoreView;
    private int advImageUrlIndex;
    private int freeTimeLoadingPager = View.GONE;
    private Runnable runnable;
    private int totalGetDataTimes = 0;
    private int finishedGetDataTimes = 0;
    private DemandServiceToggleView viewToggleServiceDemand;

    public PagerHomeFreeTimeModel(PagerHomeFreetimeBinding pagerHomeFreetimeBinding, Activity activity, Runnable runnable) {
        this.pagerHomeFreetimeBinding = pagerHomeFreetimeBinding;
        this.mActivity = activity;
        this.runnable = runnable;
        //displayLoadLayer();
        initView();
        initScrollView();
        initData();
        initFootView();
        initListener();
    }

    //显示加载页面
    private void displayLoadLayer() {
        setFreeTimeLoadingPager(View.VISIBLE);
    }

    //隐藏加载页面
    public void hideLoadLayer() {
        setFreeTimeLoadingPager(View.GONE);
    }

    @Bindable
    public int getFreeTimeLoadingPager() {
        return freeTimeLoadingPager;
    }

    public void setFreeTimeLoadingPager(int freeTimeLoadingPager) {
        this.freeTimeLoadingPager = freeTimeLoadingPager;
        notifyPropertyChanged(BR.freeTimeLoadingPager);
    }

    //添加脚布局
    private void initFootView() {
        listMoreView = View.inflate(CommonUtils.getContext(), R.layout.first_pager_more, null);
        pagerHomeFreetimeBinding.lvHomeDemandAndService.addFooterView(listMoreView);
    }

    //平滑到顶部
    private void smoothScrollto() {
        pagerHomeFreetimeBinding.svPullData.smoothScrollTo(0, 0);
    }

    private void initScrollView() {
        pagerHomeFreetimeBinding.refreshView.setOnRefreshListener(new FreeTimeListListener());
    }

    public class FreeTimeListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDemandOrServiceListData();

                    if (homeDemandAndDemandAdapter != null) {
                        homeDemandAndDemandAdapter.notifyDataSetChanged();
                    }

                    if (homeServiceAdapter != null) {
                        homeServiceAdapter.notifyDataSetChanged();
                    }
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 2000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        }
    }

    ArrayList<String> listAdvImageUrl = new ArrayList<String>();
    int vpAdvStartIndex;

    private void initView() {
        viewToggleServiceDemand = pagerHomeFreetimeBinding.viewToggleServiceDemand;

        mIsDisplayDemandList = SpUtils.getBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, false);
        if (mIsDisplayDemandList) {
            displayDemanList();
            viewToggleServiceDemand.initView(false);
        } else {
            displayServiceList();
            viewToggleServiceDemand.initView(true);
        }
    }

    private void initData() {
        // x.image().clearCacheFiles();

        bannerList.clear();
        titleArrayList.clear();
        imageArrayList.clear();
        urlArrayList.clear();
        getDataFromServer();

        //  setPager();
    }

    private void setPager() {
        // mPagerHomeFreetimeBinding.lvHomeDemandAndService.setAdapter(new HomeDemandAndServiceAdapter(listDemandServiceBean));
        //  vpAdvStartIndex = 100000000 - 100000000 % listAdvImageUrl.size();
        vpAdvStartIndex = 100000000 - 100000000 % bannerList.size();
        //vpAdvStartIndex = 100000000 - 100000000 % imageArrayList.size();
//        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setOffscreenPageLimit(3);
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
                // return bannerList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                advImageUrlIndex = position % bannerList.size();
//                Bitmap roundedBitmap = bannerList.get(advImageUrlIndex);\
                Bitmap srcBitmap = bannerList.get(advImageUrlIndex);

              /*  advImageUrlIndex = position % imageArrayList.size();
                String advImageUrl = imageArrayList.get(advImageUrlIndex);*/

//                final CardView cardView = new CardView(CommonUtils.getContext());
//                cardView.setCardBackgroundColor(Color.TRANSPARENT);
////                cardView.setCardBackgroundColor(0xffff0000);
//                cardView.setRadius(CommonUtils.dip2px(5));
//                cardView.setCardElevation(CommonUtils.dip2px(3));
//                cardView.setMaxCardElevation(CommonUtils.dip2px(3));
//                cardView.setUseCompatPadding(true);

//                CardView.LayoutParams imgPparams = new CardView.LayoutParams(-1, -1);
                ImageView ivHomeFreetimeAdv = new ImageView(CommonUtils.getContext());
//                ivHomeFreetimeAdv.setPadding(CommonUtils.dip2px(2), CommonUtils.dip2px(2), CommonUtils.dip2px(2), CommonUtils.dip2px(2));
//                ivHomeFreetimeAdv.setLayoutParams(imgPparams);

             /*  x.image().loadDrawable(advImageUrl, ImageOptions.DEFAULT, new Callback.CommonCallback<Drawable>() {
                    @Override
                    public void onSuccess(Drawable result) {
                       // LogKit.v("Load banner pic onSuccess");
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) result;
                        Bitmap srcBitmap = bitmapDrawable.getBitmap();
                        Bitmap roundedBitmap = BitmapKit.createRoundedBitmap(srcBitmap, 5);
                        ivHomeFreetimeAdv.setImageBitmap(roundedBitmap);
                        ivHomeFreetimeAdv.setScaleType(ImageView.ScaleType.FIT_XY);

                        ivHomeFreetimeAdv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //在这里 0，青年 1
                                //点击的时候，到banner页面
                                initBanner(advImageUrlIndex);
                                //埋点
                                switch (advImageUrlIndex){
                                    case 0:
                                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_ONE);
                                        break;
                                    case 1:
                                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_TWO);
                                        break;
                                    case 2:
                                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_THREE);
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        LogKit.v("Load banner pic onError");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        LogKit.v("Load banner pic onCancelled");
                    }

                    @Override
                    public void onFinished() {
                        LogKit.v("Load banner pic onFinished");
                    }
                });
*/
                ivHomeFreetimeAdv.setImageBitmap(srcBitmap);
                ivHomeFreetimeAdv.setScaleType(ImageView.ScaleType.FIT_XY);
                ivHomeFreetimeAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //在这里 0，青年 1
                        //点击的时候，到banner页面
                        initBanner(advImageUrlIndex);
                        //埋点
                        switch (advImageUrlIndex) {
                            case 0:
                                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_ONE);
                                break;
                            case 1:
                                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_TWO);
                                break;
                            case 2:
                                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_THREE);
                                break;
                        }
                    }
                });

                ivHomeFreetimeAdv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                isClickDown = true;
                                LogKit.v("Adv Pager ACTION_DOWN");
                                break;
                            case MotionEvent.ACTION_UP:
                                isClickDown = false;
                                LogKit.v("Adv Pager ACTION_UP");
                                break;
                        }
                        return false;
                    }
                });

//                cardView.addView(ivHomeFreetimeAdv);
//                container.addView(cardView);
//                return cardView;
                container.addView(ivHomeFreetimeAdv);
                return ivHomeFreetimeAdv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        //pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(vpAdvStartIndex);
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(1);
//        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setPageTransformer(false, new ViewPager.PageTransformer() {
//
//            private float defaultScale = (float) 8 / (float) 9;
//            int translationX = CommonUtils.dip2px(13);
//
//            @Override
//            public void transformPage(View page, float position) {
//                if (position < -1) { // [-Infinity,-1)
//                    page.setScaleX(defaultScale);
//                    page.setScaleY(defaultScale);
//                    page.setTranslationX(translationX);
//                } else if (position <= 0) { // [-1,0]
//                    page.setScaleX((float) 1 + position / (float) 9);
//                    page.setScaleY((float) 1 + position / (float) 9);
//                    page.setTranslationX((0 - position) * translationX);
//                } else if (position <= 1) { // (0,1]
//                    page.setScaleX((float) 1 - position / (float) 9);
//                    page.setScaleY((float) 1 - position / (float) 9);
//                    page.setTranslationX((0 - position) * translationX);
//                } else { // (1,+Infinity]
//                    page.setScaleX(defaultScale);
//                    page.setScaleY(defaultScale);
//                    page.setTranslationX(-translationX);
//                }
//            }
//        });
//        bannerList
        //显示ViewPager 的指示器小圆点
        for (int i = 0; i < bannerList.size(); i++) {
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(CommonUtils.dip2px(6), CommonUtils.dip2px(6));
            if (i > 0) {
                llParams.leftMargin = CommonUtils.dip2px(10);
            }
            View vPoint = new View(CommonUtils.getContext());
            vPoint.setLayoutParams(llParams);
//            if (currentItem == i) {//选中的那个圆点
//                vPoint.setBackgroundResource(R.drawable.shape_vpindicator_selected);
//            } else {//没有选中的那个圆点
//                vPoint.setBackgroundResource(R.drawable.shape_vpindicator_unselected);
//            }
            pagerHomeFreetimeBinding.llVpIndicator.addView(vPoint);
        }
        int currentItem = pagerHomeFreetimeBinding.vpHomeFreetimeAdv.getCurrentItem();
        setSelectedVpPointer(currentItem);
    }

    private void setSelectedVpPointer(int index) {
        //这里需要做判断，因为可能执行这段代码的时候，bannerList中的数据还没有从网络加载完毕
        if (bannerList != null && bannerList.size() > 0) {
            index = index % bannerList.size();
            int childCount = pagerHomeFreetimeBinding.llVpIndicator.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View vPoint = pagerHomeFreetimeBinding.llVpIndicator.getChildAt(i);
                if (i == index) {//选中
                    vPoint.setBackgroundResource(R.drawable.shape_vpindicator_selected);
                } else {//未选中
                    vPoint.setBackgroundResource(R.drawable.shape_vpindicator_unselected);
                }
            }
        }
    }

    private void initListener() {
        final HomeVpAdvChange homeVpAdvChange = new HomeVpAdvChange();
        //设置首页广告条自动滚动
        CommonUtils.getHandler().postDelayed(homeVpAdvChange, 3000);

        //当点击的时候停止滚动，松开手指的时候继续滚动
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        CommonUtils.getHandler().removeCallbacks(homeVpAdvChange);
                        isClickDown = true;
                        break;
                    case MotionEvent.ACTION_UP:
//                        CommonUtils.getHandler().removeCallbacks(homeVpAdvChange);
//                        CommonUtils.getHandler().postDelayed(homeVpAdvChange, 3000);
                        isClickDown = false;
                        break;
                }
                return false;
            }
        });

        //条目点击
        pagerHomeFreetimeBinding.lvHomeDemandAndService
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mIsDisplayDemandList) {
                            //需求
                            if (position <= listDemandBean.size() - 1) {
                                FreeTimeDemandBean.DataBean.ListBean listBean = listDemandBean.get(position);
                                long demandId = listBean.getId();
                                Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                                intentDemandDetailActivity.putExtra("demandId", demandId);
                                mActivity.startActivity(intentDemandDetailActivity);
                            }
                        } else {
                            //服务
                            if (position <= listServiceBean.size() - 1) {
                                FreeTimeServiceBean.DataBean.ListBean listBean = listServiceBean.get(position);
                                long serviceId = listBean.getId();
                                Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                                intentServiceDetailActivity.putExtra("serviceId", serviceId);
                                mActivity.startActivity(intentServiceDetailActivity);
                            }
                        }
                    }
                });

        //加载更多的点击事件
        if (listMoreView != null) {
            listMoreView.findViewById(R.id.tv_search_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickMore();
                    //点击右侧搜索更多埋点
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_RIGHT_MORE);
                }
            });
        }

        viewToggleServiceDemand.setServiceDemandToggle(new DemandServiceToggleView.IServiceDemandToggle() {
            @Override
            public void toggleServiceDemand(boolean isCheckedService) {
                if (isCheckedService) {
                    displayServiceList(null);
                } else {
                    displayDemanList(null);
                }
            }
        });
        //这个方法只能在6.0以上的版本中使用
//        pagerHomeFreetimeBinding.svPullData.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int distanceTopPosition = pagerHomeFreetimeBinding.svPullData.getScrollY();
//                LogKit.v("distanceTopPosition:" + distanceTopPosition);
//            }
//        });
        pagerHomeFreetimeBinding.svPullData.postDelayed(new Runnable() {
            @Override
            public void run() {
                pagerHomeFreetimeBinding.svPullData.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollY = pagerHomeFreetimeBinding.svPullData.getScrollY();
                        LogKit.v("scrollY:" + scrollY);
                        if (scrollY >= 0) {
                            //以最大值为50dp来计算
                            float maxValue = 50;
                            float scrollYdp = CommonUtils.px2dip(scrollY);
                            if (scrollYdp > maxValue) {
                                scrollYdp = maxValue;
                            }
                            float alpha = 255 * scrollYdp / maxValue;
                            int argb = Color.argb((int) alpha, 0x31, 0xc5, 0xe4);
                            pagerHomeFreetimeBinding.rlTitleBar.setBackgroundColor(argb);
                        }
                    }
                });
            }
        }, 100);
        pagerHomeFreetimeBinding.refreshView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int topToParent = pagerHomeFreetimeBinding.svPullData.getTop();
                LogKit.v("topToParent:" + topToParent);
                if (topToParent > 0) {
                    pagerHomeFreetimeBinding.rlTitleBar.setVisibility(View.GONE);
                } else {
                    pagerHomeFreetimeBinding.rlTitleBar.setVisibility(View.VISIBLE);
                }
            }
        });
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectedVpPointer(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBanner(int advImageUrlIndex) {
        String title = titleArrayList.get(advImageUrlIndex);
        String bannerUrl = urlArrayList.get(advImageUrlIndex);

        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), WebViewActivity.class);
        intentCommonQuestionActivity.putExtra("bannerIndex", advImageUrlIndex);
        intentCommonQuestionActivity.putExtra("title", title);
        intentCommonQuestionActivity.putExtra("bannerUrl", bannerUrl);
        mActivity.startActivity(intentCommonQuestionActivity);
    }

    boolean isClickDown = false;

    public class HomeVpAdvChange implements Runnable {
        @Override
        public void run() {
            if (!isClickDown) {
                pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(pagerHomeFreetimeBinding.vpHomeFreetimeAdv.getCurrentItem() + 1);
                int currentItem = pagerHomeFreetimeBinding.vpHomeFreetimeAdv.getCurrentItem();
                setSelectedVpPointer(currentItem);
            }
            CommonUtils.getHandler().postDelayed(this, 3000);
        }
    }

    public void gotoSearchActivity(View v) {
        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        mActivity.startActivity(intentSearchActivity);
    }

    public void gotoPublishActivity(View v) {
        Intent intentPublishActivity = new Intent(CommonUtils.getContext(), PublishActivity.class);
        mActivity.startActivity(intentPublishActivity);
    }

    public void gotoTagRecommendActivity(View v) {
//        Intent intentTagRecommendActivity = new Intent(CommonUtils.getContext(), TagRecommendActivity.class);
//        mActivity.startActivity(intentTagRecommendActivity);
    }

    //切换为展示需求列条
    public void displayDemanList(View v) {
        mIsDisplayDemandList = true;
        SpUtils.setBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, mIsDisplayDemandList);
        displayDemanList();
        //需求埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_ACCESS_REQUIREMENT);
    }

    //切换为展示服务列条
    public void displayServiceList(View v) {
        mIsDisplayDemandList = false;
        SpUtils.setBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, mIsDisplayDemandList);
        displayServiceList();
        //服务埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_ACCESS_SERVICE);
    }

    //展示需求
    public void displayDemanList() {
        setDemandButtonVisibility(View.VISIBLE);
        setServiceButtonVisibility(View.INVISIBLE);
        totalGetDataTimes++;
        getDemandOrServiceListData();
    }

    //服务需求
    public void displayServiceList() {
        setDemandButtonVisibility(View.INVISIBLE);
        setServiceButtonVisibility(View.VISIBLE);
        getDemandOrServiceListData();
    }

    public void getDataFromServer() {
       /* Bitmap banner1 =BitmapFactory.decodeResource(CommonUtils.getContext().getResources(),R.mipmap.banner);
        Bitmap banner2 =BitmapFactory.decodeResource(CommonUtils.getContext().getResources(),R.mipmap.banner2);
        Bitmap banner3 =BitmapFactory.decodeResource(CommonUtils.getContext().getResources(),R.mipmap.banner3);
        bannerList.add(banner1);
        bannerList.add(banner2);
        bannerList.add(banner3);*/

//        if (titleArrayList != null && titleArrayList.size() > 0 && imageArrayList != null && imageArrayList.size() > 0 && urlArrayList != null && urlArrayList.size() > 0 && urlArrayList.size() == imageArrayList.size() && imageArrayList.size() == titleArrayList.size()) {
//            loadImage();
//        } else {
        totalGetDataTimes++;
        FirstPagerManager.onGetFirstPagerAdvertisement(new onGetFirstPagerAdvertisement());
//        }
        totalGetDataTimes++;
        FirstPagerManager.getHomeTagConfig(new OnGetTagConfigFinished());
    }

    private class OnGetTagConfigFinished implements BaseProtocol.IResultExecutor<HomeTagInfoBean> {
        @Override
        public void execute(HomeTagInfoBean dataBean) {
            ArrayList<HomeTagInfoBean.TagInfo> listTag = dataBean.tag;
            pagerHomeFreetimeBinding.llHomeFirstTags.removeAllViews();
            for (int i = 0; i < listTag.size(); i++) {
                HomeTagInfoBean.TagInfo tagInfo = listTag.get(i);
                final long tagId = tagInfo.id;//标签id
                final String tagName = tagInfo.name;//标签名字
                String tagIconUrl = tagInfo.icon;//标签图标的url

                LinearLayout.LayoutParams llParams1 = new LinearLayout.LayoutParams(-2, -2);
                if (i > 0) {
                    llParams1.leftMargin = CommonUtils.dip2px(33);
                }
                if (i == listTag.size() - 1) {
                    llParams1.rightMargin = CommonUtils.dip2px(24);
                }
                LinearLayout llTag = new LinearLayout(CommonUtils.getContext());
                llTag.setOrientation(LinearLayout.VERTICAL);
                llTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentTagRecommendActivity = new Intent(CommonUtils.getContext(), TagRecommendActivity.class);
                        intentTagRecommendActivity.putExtra("tagId", tagId);
                        intentTagRecommendActivity.putExtra("tagName", tagName);
                        mActivity.startActivity(intentTagRecommendActivity);
                    }
                });
                llTag.setLayoutParams(llParams1);

                LinearLayout.LayoutParams ivParams2 = new LinearLayout.LayoutParams(CommonUtils.dip2px(48), CommonUtils.dip2px(48));
                ivParams2.gravity = Gravity.CENTER_HORIZONTAL;
                ImageView ivTagIcon = new ImageView(CommonUtils.getContext());
                ivTagIcon.setLayoutParams(ivParams2);
                x.image().bind(ivTagIcon, tagIconUrl);

                LinearLayout.LayoutParams tvParams3 = new LinearLayout.LayoutParams(-2, -2);
                tvParams3.gravity = Gravity.CENTER_HORIZONTAL;
                tvParams3.topMargin = CommonUtils.dip2px(8);
                TextView tvTagName = new TextView(CommonUtils.getContext());
                tvTagName.setLayoutParams(tvParams3);
                tvTagName.setText(tagName);
                tvTagName.setTextSize(12);
                tvTagName.setTextColor(0xff333333);

                llTag.addView(ivTagIcon);
                llTag.addView(tvTagName);

                pagerHomeFreetimeBinding.llHomeFirstTags.addView(llTag);
            }

            finishedGetDataTimes++;
            if (finishedGetDataTimes >= totalGetDataTimes && runnable != null) {
                runnable.run();
            }
        }

        @Override
        public void executeResultError(String result) {
            finishedGetDataTimes++;
            if (finishedGetDataTimes >= totalGetDataTimes && runnable != null) {
                runnable.run();
            }
        }
    }

    public void getDemandOrServiceListData() {
        totalGetDataTimes++;
        if (mIsDisplayDemandList) {
//            FirstPagerManager.onFreeTimeDemandList(new onFreeTimeDemandList(), limit);
            FirstPagerManager.getRecommendDemand2(new BaseProtocol.IResultExecutor<HomeRecommendList2>() {
                @Override
                public void execute(HomeRecommendList2 dataBean) {
                    //这里直接把HomeRecommendList2转换成FreeTimeDemandBean（对应字段赋值）
                    FreeTimeDemandBean freeTimeDemandBean = new FreeTimeDemandBean();
                    freeTimeDemandBean.setRescode(dataBean.rescode);
                    freeTimeDemandBean.setData(new FreeTimeDemandBean.DataBean());
                    freeTimeDemandBean.getData().setList(new ArrayList<FreeTimeDemandBean.DataBean.ListBean>());
                    List<FreeTimeDemandBean.DataBean.ListBean> listRecommendDemand = freeTimeDemandBean.getData().getList();
                    ArrayList<HomeRecommendList2.RecommendInfo> reclist = dataBean.data.reclist;//精准
                    boolean isInsertRadHint = false;
                    for (HomeRecommendList2.RecommendInfo recommendInfo : reclist) {
                        FreeTimeDemandBean.DataBean.ListBean listBean = new FreeTimeDemandBean.DataBean.ListBean();
                        listBean.isReclist = true;
                        listBean.isInsertRadHint = isInsertRadHint;

                        listBean.setAnonymity(recommendInfo.anonymity);
                        listBean.setQuoteunit(recommendInfo.quoteunit);
                        listBean.setTimetype(recommendInfo.timetype);
                        listBean.setAvatar(recommendInfo.avatar);
                        listBean.setId(recommendInfo.id);
                        listBean.setInstalment(recommendInfo.instalment);
                        listBean.setIsauth(recommendInfo.isauth);
                        listBean.setLat(recommendInfo.lat);
                        listBean.setLng(recommendInfo.lng);
                        listBean.setName(recommendInfo.name);
                        listBean.setPattern(recommendInfo.pattern);
                        listBean.setPlace(recommendInfo.place);
                        listBean.setQuote(recommendInfo.quote);
                        listBean.setStarttime(recommendInfo.starttime);
                        listBean.setEndtime(recommendInfo.endtime);
                        listBean.setTitle(recommendInfo.title);
                        listBean.setUid(recommendInfo.uid);

                        listRecommendDemand.add(listBean);
                    }
                    isInsertRadHint = true;
                    ArrayList<HomeRecommendList2.RecommendInfo> radlist = dataBean.data.radlist;//模糊
                    for (HomeRecommendList2.RecommendInfo recommendInfo : radlist) {
                        FreeTimeDemandBean.DataBean.ListBean listBean = new FreeTimeDemandBean.DataBean.ListBean();
                        listBean.isReclist = false;
                        //只有模糊推荐的第一行上面才需要提示
                        listBean.isInsertRadHint = isInsertRadHint;
                        isInsertRadHint = false;

                        listBean.setAnonymity(recommendInfo.anonymity);
                        listBean.setQuoteunit(recommendInfo.quoteunit);
                        listBean.setTimetype(recommendInfo.timetype);
                        listBean.setAvatar(recommendInfo.avatar);
                        listBean.setId(recommendInfo.id);
                        listBean.setInstalment(recommendInfo.instalment);
                        listBean.setIsauth(recommendInfo.isauth);
                        listBean.setLat(recommendInfo.lat);
                        listBean.setLng(recommendInfo.lng);
                        listBean.setName(recommendInfo.name);
                        listBean.setPattern(recommendInfo.pattern);
                        listBean.setPlace(recommendInfo.place);
                        listBean.setQuote(recommendInfo.quote);
                        listBean.setStarttime(recommendInfo.starttime);
                        listBean.setEndtime(recommendInfo.endtime);
                        listBean.setTitle(recommendInfo.title);
                        listBean.setUid(recommendInfo.uid);

                        listRecommendDemand.add(listBean);
                    }

                    setRecommendDemandList(freeTimeDemandBean);
                }

                @Override
                public void executeResultError(String result) {

                }
            }, limit + "");
        } else {
//            FirstPagerManager.onFreeTimeServiceList(new onFreeTimeServiceList(), limit);
            FirstPagerManager.getRecommendService2(new BaseProtocol.IResultExecutor<HomeRecommendList2>() {
                @Override
                public void execute(HomeRecommendList2 dataBean) {
                    //这里直接把HomeRecommendList2转换成FreeTimeServiceBean（对应字段赋值）
                    FreeTimeServiceBean freeTimeServiceBean = new FreeTimeServiceBean();
                    freeTimeServiceBean.setRescode(dataBean.rescode);
                    freeTimeServiceBean.setData(new FreeTimeServiceBean.DataBean());
                    freeTimeServiceBean.getData().setList(new ArrayList<FreeTimeServiceBean.DataBean.ListBean>());
                    List<FreeTimeServiceBean.DataBean.ListBean> listRecommendService = freeTimeServiceBean.getData().getList();
                    ArrayList<HomeRecommendList2.RecommendInfo> reclist = dataBean.data.reclist;
                    boolean isInsertRadHint = false;
                    for (HomeRecommendList2.RecommendInfo recommendInfo : reclist) {
                        FreeTimeServiceBean.DataBean.ListBean listBean = new FreeTimeServiceBean.DataBean.ListBean();
                        listBean.isReclist = true;
                        listBean.isInsertRadHint = isInsertRadHint;

                        listBean.setAnonymity(recommendInfo.anonymity);
                        listBean.setQuoteunit(recommendInfo.quoteunit);
                        listBean.setTimetype(recommendInfo.timetype);
                        listBean.setAvatar(recommendInfo.avatar);
                        listBean.setId(recommendInfo.id);
                        listBean.setInstalment(recommendInfo.instalment);
                        listBean.setIsauth(recommendInfo.isauth);
                        listBean.setLat(recommendInfo.lat);
                        listBean.setLng(recommendInfo.lng);
                        listBean.setName(recommendInfo.name);
                        listBean.setPattern(recommendInfo.pattern);
                        listBean.setPlace(recommendInfo.place);
                        listBean.setQuote(recommendInfo.quote);
                        listBean.setStarttime(recommendInfo.starttime);
                        listBean.setEndtime(recommendInfo.endtime);
                        listBean.setTitle(recommendInfo.title);
                        listBean.setUid(recommendInfo.uid);

                        listRecommendService.add(listBean);
                    }
                    isInsertRadHint = true;
                    ArrayList<HomeRecommendList2.RecommendInfo> radlist = dataBean.data.radlist;
                    for (HomeRecommendList2.RecommendInfo recommendInfo : radlist) {
                        FreeTimeServiceBean.DataBean.ListBean listBean = new FreeTimeServiceBean.DataBean.ListBean();
                        listBean.isReclist = false;
                        //只有模糊推荐的第一行上面才需要提示
                        listBean.isInsertRadHint = isInsertRadHint;
                        isInsertRadHint = false;

                        listBean.setAnonymity(recommendInfo.anonymity);
                        listBean.setQuoteunit(recommendInfo.quoteunit);
                        listBean.setTimetype(recommendInfo.timetype);
                        listBean.setAvatar(recommendInfo.avatar);
                        listBean.setId(recommendInfo.id);
                        listBean.setInstalment(recommendInfo.instalment);
                        listBean.setIsauth(recommendInfo.isauth);
                        listBean.setLat(recommendInfo.lat);
                        listBean.setLng(recommendInfo.lng);
                        listBean.setName(recommendInfo.name);
                        listBean.setPattern(recommendInfo.pattern);
                        listBean.setPlace(recommendInfo.place);
                        listBean.setQuote(recommendInfo.quote);
                        listBean.setStarttime(recommendInfo.starttime);
                        listBean.setEndtime(recommendInfo.endtime);
                        listBean.setTitle(recommendInfo.title);
                        listBean.setUid(recommendInfo.uid);

                        listRecommendService.add(listBean);
                    }

                    setRecommendServiceList(freeTimeServiceBean);
                }

                @Override
                public void executeResultError(String result) {

                }
            }, limit + "");
        }
    }

    public class RefreshDataTask implements NewRefreshListView.NewIRefreshDataTask {
        @Override
        public void refresh() {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDemandOrServiceListData();
                }
            }, 2000);
        }
    }

    private int currentLoadImageCount = 0;

    //广告
    public class onGetFirstPagerAdvertisement implements BaseProtocol.IResultExecutor<BannerConfigBean> {
        @Override
        public void execute(BannerConfigBean data) {
            titleArrayList.clear();
            imageArrayList.clear();
            urlArrayList.clear();
            final List<BannerConfigBean.BannerBean> banner = data.getBanner();
            for (BannerConfigBean.BannerBean bannerBean : banner) {
                String title = bannerBean.getTitle();
                String image = bannerBean.getImage();
                String url = bannerBean.getUrl();
                titleArrayList.add(title);
                imageArrayList.add(image);
                urlArrayList.add(url);
            }

            loadImage();

            finishedGetDataTimes++;
            if (finishedGetDataTimes >= totalGetDataTimes && runnable != null) {
                runnable.run();
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
            finishedGetDataTimes++;
            if (finishedGetDataTimes >= totalGetDataTimes && runnable != null) {
                runnable.run();
            }
        }
    }

    private void loadImage() {
        for (String imageUrl : imageArrayList) {
            //缓存加载
            x.image().loadFile(imageUrl, ImageOptions.DEFAULT, new Callback.CacheCallback<File>() {
                @Override
                public boolean onCache(File result) {
                    if (result != null) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public void onSuccess(File result) {
                    Bitmap srcBitmap = BitmapFactory.decodeFile(result.getAbsolutePath());
//                        Bitmap roundedBitmap = BitmapKit.createRoundedBitmap(srcBitmap, 5);
//                        bannerList.add(roundedBitmap);
                    bannerList.add(srcBitmap);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    currentLoadImageCount++;
                    if (currentLoadImageCount >= imageArrayList.size()) {
                        setPager();
                    }
                }
            });
        }
    }

    //首页闲时需求
    public class onFreeTimeDemandList implements BaseProtocol.IResultExecutor<FreeTimeDemandBean> {
        @Override
        public void execute(FreeTimeDemandBean data) {
            setRecommendDemandList(data);
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private void setRecommendDemandList(FreeTimeDemandBean data) {
        finishedGetDataTimes++;
        if (finishedGetDataTimes >= totalGetDataTimes && runnable != null) {
            runnable.run();
        }

        int rescode = data.getRescode();
        if (rescode == 0) {
            pagerHomeFreetimeBinding.rlHomeDefaultImage.setVisibility(View.GONE);
            FreeTimeDemandBean.DataBean dataBean = data.getData();
            List<FreeTimeDemandBean.DataBean.ListBean> list = dataBean.getList();
            listsize = list.size();
            if (list.size() == 0) {
                pagerHomeFreetimeBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                pagerHomeFreetimeBinding.tvContent.setVisibility(View.GONE);
            } else {
                listDemandBean.clear();
                listDemandBean.addAll(list);
                homeDemandAndDemandAdapter = new HomeDemandAdapter(listDemandBean, mActivity);
                pagerHomeFreetimeBinding.lvHomeDemandAndService
                        .setAdapter(homeDemandAndDemandAdapter);
                pagerHomeFreetimeBinding.rlHomeDefaultImage.setVisibility(View.GONE);
            }
            smoothScrollto();
            //隐藏加载页面
            hideLoadLayer();
        }
    }

    private int listsize;

    //首页闲时服务
    public class onFreeTimeServiceList implements BaseProtocol.IResultExecutor<FreeTimeServiceBean> {
        @Override
        public void execute(FreeTimeServiceBean data) {
            setRecommendServiceList(data);
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private void setRecommendServiceList(FreeTimeServiceBean data) {
        finishedGetDataTimes++;
        if (finishedGetDataTimes >= totalGetDataTimes && runnable != null) {
            runnable.run();
        }

        int rescode = data.getRescode();
        if (rescode == 0) {
            pagerHomeFreetimeBinding.rlHomeDefaultImage.setVisibility(View.GONE);
            FreeTimeServiceBean.DataBean dataBean = data.getData();
            List<FreeTimeServiceBean.DataBean.ListBean> list = dataBean.getList();
            listsize = list.size();
            if (list.size() == 0) {
                pagerHomeFreetimeBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                pagerHomeFreetimeBinding.tvContent.setVisibility(View.GONE);
            } else {
                listServiceBean.clear();
                listServiceBean.addAll(list);
                homeServiceAdapter = new HomeServiceAdapter(listServiceBean, mActivity);
                pagerHomeFreetimeBinding.lvHomeDemandAndService
                        .setAdapter(homeServiceAdapter);
                pagerHomeFreetimeBinding.rlHomeDefaultImage.setVisibility(View.GONE);
            }
            smoothScrollto();
            //隐藏加载页面
            hideLoadLayer();
        }
    }

    //点击添加更多
    public void more(View view) {
        clickMore();
        //点击底部更多埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BOTTOM_MORE);
    }

    private void clickMore() {
        Intent intentFirstPagerMoreActivity = new Intent(CommonUtils.getContext(), FirstPagerMoreActivity.class);
        intentFirstPagerMoreActivity.putExtra("isDemand", mIsDisplayDemandList);
        intentFirstPagerMoreActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentFirstPagerMoreActivity);
    }

    private int demandButtonVisibility;
    private int serviceButtonVisibility;

    @Bindable
    public int getDemandButtonVisibility() {
        return demandButtonVisibility;
    }

    public void setDemandButtonVisibility(int demandButtonVisibility) {
        this.demandButtonVisibility = demandButtonVisibility;
        notifyPropertyChanged(BR.demandButtonVisibility);
    }

    @Bindable
    public int getServiceButtonVisibility() {
        return serviceButtonVisibility;
    }

    public void setServiceButtonVisibility(int serviceButtonVisibility) {
        this.serviceButtonVisibility = serviceButtonVisibility;
        notifyPropertyChanged(BR.serviceButtonVisibility);
    }
}
