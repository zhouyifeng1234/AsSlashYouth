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
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeFreetimeBinding;
import com.slash.youth.domain.BannerConfigBean;
import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.WebViewActivity;
import com.slash.youth.ui.adapter.HomeDemandAdapter;
import com.slash.youth.ui.adapter.HomeServiceAdapter;
import com.slash.youth.ui.view.NewRefreshListView;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.AuthHeaderUtils;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeFreeTimeModel extends BaseObservable {
    private PagerHomeFreetimeBinding pagerHomeFreetimeBinding;
    public Activity mActivity;
    private ArrayList<FreeTimeServiceBean.DataBean.ListBean> listServiceBean = new ArrayList<>();
    private ArrayList<FreeTimeDemandBean.DataBean.ListBean> listDemandBean = new ArrayList<>();
    private ArrayList<Bitmap> bannerList = new ArrayList<>();
    private ArrayList<String> titleArrayList = new ArrayList<>();
    private ArrayList<String> imageArrayList = new ArrayList<>();
    private ArrayList<String> urlArrayList = new ArrayList<>();
    public boolean mIsDisplayDemandList = true;//如果存true，表示展示需求列表，false为展示服务列表,默认为true
    private int limit = 10;
    private HomeDemandAdapter homeDemandAndDemandAdapter;
    private HomeServiceAdapter homeServiceAdapter;
    private View listMoreView;
    private int advImageUrlIndex;
    private int freeTimeLoadingPager = View.GONE;

    public PagerHomeFreeTimeModel(PagerHomeFreetimeBinding pagerHomeFreetimeBinding, Activity activity) {
        this.pagerHomeFreetimeBinding = pagerHomeFreetimeBinding;
        this.mActivity = activity;
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
        pagerHomeFreetimeBinding.scrollView.smoothScrollTo(0, 0);
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
        mIsDisplayDemandList = SpUtils.getBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, false);
        if (mIsDisplayDemandList) {
            displayDemanList();
        } else {
            displayServiceList();
        }
    }

    private void initData() {
//        x.image().clearCacheFiles();
        titleArrayList.clear();
        imageArrayList.clear();
        urlArrayList.clear();
        getDataFromServer();

//        CommonUtils.getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                setPager();
//            }
//        });
        setPager();
    }

    ImageOptions imageOptions;

    private void createImageOptions(final String url) {
        ImageOptions.Builder builder = new ImageOptions.Builder();
        imageOptions = builder.build();
        builder.setParamsBuilder(new ImageOptions.ParamsBuilder() {
            @Override
            public RequestParams buildParams(RequestParams params, ImageOptions options) {
                SSLContext sslContext = BaseProtocol.getSSLContext(CommonUtils.getApplication());
                params.setSslSocketFactory(sslContext.getSocketFactory());

                params.addHeader("uid", LoginManager.currentLoginUserId + "");
                params.addHeader("pass", "1");
                params.addHeader("token", LoginManager.token);
                Map headerMap = AuthHeaderUtils.getBasicAuthHeader("POST", url);
                String date = (String) headerMap.get("Date");
                String authorizationStr = (String) headerMap.get("Authorization");
                params.addHeader("Date", date);
                params.addHeader("Authorization", authorizationStr);
                return params;
            }
        });
//        builder.setImageScaleType(scaleType);
    }

    boolean isSetBannerAdapter = false;

    private void setPager() {
        LogKit.v("-------------set Paget imageArrayList size:" + imageArrayList.size() + "------------------");
        // mPagerHomeFreetimeBinding.lvHomeDemandAndService.setAdapter(new HomeDemandAndServiceAdapter(listDemandServiceBean));
        //  vpAdvStartIndex = 100000000 - 100000000 % listAdvImageUrl.size();
        vpAdvStartIndex = 100000000 - 100000000 % bannerList.size();
//        vpAdvStartIndex = 100000000 - 100000000 % imageArrayList.size();
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setOffscreenPageLimit(3);
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                advImageUrlIndex = position % bannerList.size();
                Bitmap roundedBitmap = bannerList.get(advImageUrlIndex);

//                advImageUrlIndex = position % imageArrayList.size();
//                String advImageUrl = imageArrayList.get(advImageUrlIndex);

                final CardView cardView = new CardView(CommonUtils.getContext());
                cardView.setCardBackgroundColor(Color.TRANSPARENT);
//                cardView.setCardBackgroundColor(0xffff0000);
                cardView.setRadius(CommonUtils.dip2px(5));
                cardView.setCardElevation(CommonUtils.dip2px(3));
                cardView.setMaxCardElevation(CommonUtils.dip2px(3));
                cardView.setUseCompatPadding(true);

                CardView.LayoutParams imgPparams = new CardView.LayoutParams(-1, -1);
                final ImageView ivHomeFreetimeAdv = new ImageView(CommonUtils.getContext());
//                ivHomeFreetimeAdv.setPadding(CommonUtils.dip2px(2), CommonUtils.dip2px(2), CommonUtils.dip2px(2), CommonUtils.dip2px(2));
                ivHomeFreetimeAdv.setLayoutParams(imgPparams);

                LogKit.v("--------------advImageUrlIndex:" + advImageUrlIndex + "--------------------");
                LogKit.v("-----------loadDrawable---------------");
//                x.image().loadDrawable(advImageUrl, imageOptions, new Callback.CommonCallback<Drawable>() {
//                    @Override
//                    public void onSuccess(final Drawable result) {
//                        LogKit.v("Load banner pic onSuccess");
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                BitmapDrawable bitmapDrawable = (BitmapDrawable) result;
//                                Bitmap srcBitmap = bitmapDrawable.getBitmap();
//                                final Bitmap roundedBitmap = BitmapKit.createRoundedBitmap(srcBitmap, 5);
//                                CommonUtils.getHandler().post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ivHomeFreetimeAdv.setImageBitmap(roundedBitmap);
//                                        ivHomeFreetimeAdv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//                                        ivHomeFreetimeAdv.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                //在这里 0，青年 1
//                                                //点击的时候，到banner页面
//                                                initBanner(advImageUrlIndex);
//                                                //埋点
//                                                switch (advImageUrlIndex) {
//                                                    case 0:
//                                                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_ONE);
//                                                        break;
//                                                    case 1:
//                                                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_TWO);
//                                                        break;
//                                                    case 2:
//                                                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_BANNER_THREE);
//                                                        break;
//                                                }
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        }).start();
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        LogKit.v("Load banner pic onError");
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                        LogKit.v("Load banner pic onCancelled");
//                    }
//
//                    @Override
//                    public void onFinished() {
//                        LogKit.v("Load banner pic onFinished");
//                    }
//                });

                ivHomeFreetimeAdv.setImageBitmap(roundedBitmap);
                ivHomeFreetimeAdv.setScaleType(ImageView.ScaleType.FIT_XY);
//                LogKit.v(advImageUrl);
//                BitmapKit.bindImage(ivHomeFreetimeAdv, advImageUrl, ImageView.ScaleType.FIT_XY, 5);//这个banner图片，不需要BA认证，所以不需要传url
//                BitmapKit.bindImage(ivHomeFreetimeAdv, "http://pic35.nipic.com/20131115/6704106_153707247000_2.jpg", ImageView.ScaleType.FIT_XY, 5);//这个banner图片，不需要BA认证，所以不需要传url
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

                cardView.addView(ivHomeFreetimeAdv);
                container.addView(cardView);
                return cardView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        LogKit.v("set Pager setCurrentItem 1");
//        CommonUtils.getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(vpAdvStartIndex);
//            }
//        });
//        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.post(new Runnable() {
//            @Override
//            public void run() {
//                pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(vpAdvStartIndex);
//            }
//        });
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(vpAdvStartIndex);
        LogKit.v("set Pager setCurrentItem 2");
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setPageTransformer(false, new ViewPager.PageTransformer() {

            private float defaultScale = (float) 8 / (float) 9;
            int translationX = CommonUtils.dip2px(13);

            @Override
            public void transformPage(View page, float position) {
                if (position < -1) { // [-Infinity,-1)
                    page.setScaleX(defaultScale);
                    page.setScaleY(defaultScale);
                    page.setTranslationX(translationX);
                } else if (position <= 0) { // [-1,0]
                    page.setScaleX((float) 1 + position / (float) 9);
                    page.setScaleY((float) 1 + position / (float) 9);
                    page.setTranslationX((0 - position) * translationX);
                } else if (position <= 1) { // (0,1]
                    page.setScaleX((float) 1 - position / (float) 9);
                    page.setScaleY((float) 1 - position / (float) 9);
                    page.setTranslationX((0 - position) * translationX);
                } else { // (1,+Infinity]
                    page.setScaleX(defaultScale);
                    page.setScaleY(defaultScale);
                    page.setTranslationX(-translationX);
                }
            }
        });
        isSetBannerAdapter = true;
    }

    private void initListener() {
        final HomeVpAdvChange homeVpAdvChange = new HomeVpAdvChange();
        //设置首页广告条自动滚动
        CommonUtils.getHandler().postDelayed(homeVpAdvChange, 2000);

        //当点击的时候停止滚动，松开手指的时候继续滚动
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        CommonUtils.getHandler().removeCallbacks(homeVpAdvChange);
                        break;
                    case MotionEvent.ACTION_UP:
                        CommonUtils.getHandler().postDelayed(homeVpAdvChange, 2000);
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
                            FreeTimeDemandBean.DataBean.ListBean listBean = listDemandBean.get(position);
                            long demandId = listBean.getId();
                            Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                            intentDemandDetailActivity.putExtra("demandId", demandId);
                            mActivity.startActivity(intentDemandDetailActivity);
                        } else {
                            //服务
                            FreeTimeServiceBean.DataBean.ListBean listBean = listServiceBean.get(position);
                            long serviceId = listBean.getId();
                            Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                            intentServiceDetailActivity.putExtra("serviceId", serviceId);
                            mActivity.startActivity(intentServiceDetailActivity);
                        }
                    }
                });

        //加载更多的点击事件
        listMoreView.findViewById(R.id.tv_search_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMore();
                //点击右侧搜索更多埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_CLICK_RIGHT_MORE);
            }
        });
    }

    private void initBanner(int advImageUrlIndex) {

        String title = titleArrayList.get(advImageUrlIndex);
        String bannerUrl = urlArrayList.get(advImageUrlIndex);

        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), WebViewActivity.class);
        intentCommonQuestionActivity.putExtra("bannerIndex", advImageUrlIndex);
        mActivity.startActivity(intentCommonQuestionActivity);
    }

    public class HomeVpAdvChange implements Runnable {
        @Override
        public void run() {
            LogKit.v("HomeVpAdvChange setCurrentItem");
            if (isSetBannerAdapter) {
                pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(pagerHomeFreetimeBinding.vpHomeFreetimeAdv.getCurrentItem() + 1);
            }
            CommonUtils.getHandler().postDelayed(this, 2000);
        }
    }

    public void gotoSearchActivity(View v) {
        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        mActivity.startActivity(intentSearchActivity);
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
        getDemandOrServiceListData();
    }

    //服务需求
    public void displayServiceList() {
        setDemandButtonVisibility(View.INVISIBLE);
        setServiceButtonVisibility(View.VISIBLE);
        getDemandOrServiceListData();
    }

    public void getDataFromServer() {
        Bitmap banner1 = BitmapFactory.decodeResource(CommonUtils.getContext().getResources(), R.mipmap.banner);
        Bitmap banner2 = BitmapFactory.decodeResource(CommonUtils.getContext().getResources(), R.mipmap.banner2);
        Bitmap banner3 = BitmapFactory.decodeResource(CommonUtils.getContext().getResources(), R.mipmap.banner3);
        bannerList.add(banner1);
        bannerList.add(banner2);
        bannerList.add(banner3);

//        FirstPagerManager.onGetFirstPagerAdvertisement(new onGetFirstPagerAdvertisement());

    }

    public void getDemandOrServiceListData() {
        if (mIsDisplayDemandList) {
            listDemandBean.clear();
            FirstPagerManager.onFreeTimeDemandList(new onFreeTimeDemandList(), limit);
        } else {
            listServiceBean.clear();
            FirstPagerManager.onFreeTimeServiceList(new onFreeTimeServiceList(), limit);
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

    //广告
    public class onGetFirstPagerAdvertisement implements BaseProtocol.IResultExecutor<BannerConfigBean> {
        @Override
        public void execute(BannerConfigBean data) {
            LogKit.v("--------------------get banner success-------------------------");
            List<BannerConfigBean.BannerBean> banner = data.getBanner();
            createImageOptions(banner.get(0).getImage());
            for (BannerConfigBean.BannerBean bannerBean : banner) {
                String title = bannerBean.getTitle();
                String image = bannerBean.getImage();
                String url = bannerBean.getUrl();
                titleArrayList.add(title);
                imageArrayList.add(image);
                urlArrayList.add(url);
            }
            setPager();
        }

        @Override
        public void executeResultError(String result) {
            LogKit.v("--------------------get banner error-------------------------");
            LogKit.d("result:" + result);
        }
    }

    //首页闲时需求
    public class onFreeTimeDemandList implements BaseProtocol.IResultExecutor<FreeTimeDemandBean> {
        @Override
        public void execute(FreeTimeDemandBean data) {
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

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private int listsize;

    //首页闲时服务
    public class onFreeTimeServiceList implements BaseProtocol.IResultExecutor<FreeTimeServiceBean> {
        @Override
        public void execute(FreeTimeServiceBean data) {
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

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
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
