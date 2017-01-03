package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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
import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.UserInfoEngine;
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
import com.slash.youth.ui.view.PullableListView.MyListener;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeFreeTimeModel extends BaseObservable {
    private PagerHomeFreetimeBinding pagerHomeFreetimeBinding;
    public Activity mActivity;
    private  ArrayList<FreeTimeServiceBean.DataBean.ListBean> listServiceBean = new ArrayList<>();
    private  ArrayList<FreeTimeDemandBean.DataBean.ListBean> listDemandBean = new ArrayList<>();
    public boolean mIsDisplayDemandList = true;//如果存true，表示展示需求列表，false为展示服务列表,默认为true
    private int limit=10;
    private HomeDemandAdapter homeDemandAndDemandAdapter;
    private HomeServiceAdapter homeServiceAdapter;
    private View listMoreView;

    public PagerHomeFreeTimeModel(PagerHomeFreetimeBinding pagerHomeFreetimeBinding, Activity activity) {
        this.pagerHomeFreetimeBinding = pagerHomeFreetimeBinding;
        this.mActivity = activity;
        initView();
        initScrollView();
        initData();
        initFootView();
        initListener();
    }

    //添加脚布局
    private void initFootView() {
        listMoreView = View.inflate(CommonUtils.getContext(), R.layout.first_pager_more, null);
        pagerHomeFreetimeBinding.lvHomeDemandAndService.addFooterView(listMoreView);
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
        mIsDisplayDemandList = SpUtils.getBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, true);
        if (mIsDisplayDemandList) {
            displayDemanList();
        } else {
            displayServiceList();
        }
    }


    private void initData() {
//        x.image().clearCacheFiles();
        getDataFromServer();
//        mPagerHomeFreetimeBinding.lvHomeDemandAndService.setAdapter(new HomeDemandAndServiceAdapter(listDemandServiceBean));
        vpAdvStartIndex = 100000000 - 100000000 % listAdvImageUrl.size();
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
            public Object instantiateItem(ViewGroup container, int position) {
                int advImageUrlIndex = position % listAdvImageUrl.size();
                String advImageUrl = listAdvImageUrl.get(advImageUrlIndex);//实际广告图片应该根据该URL来加载

                CardView cardView = new CardView(CommonUtils.getContext());
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
//                ivHomeFreetimeAdv.setBackgroundResource(R.drawable.shape_rounded_my_news_center);//经测试，ImageView通过背景设置圆角矩形边框无效
//                ivHomeFreetimeAdv.setImageResource(R.mipmap.banner);//模拟数据，实际广告图片应该从服务端返回的URL获取
//                Bitmap srcBitmap = BitmapFactory.decodeResource(CommonUtils.getContext().getResources(), R.mipmap.banner);
//                Bitmap roundedBitmap = BitmapKit.createRoundedBitmap(srcBitmap, 5);
//                ivHomeFreetimeAdv.setImageBitmap(roundedBitmap);

                x.image().loadDrawable(advImageUrl, ImageOptions.DEFAULT, new Callback.CommonCallback<Drawable>() {
                    @Override
                    public void onSuccess(Drawable result) {
                        LogKit.v("Load banner pic onSuccess");
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) result;
                        Bitmap srcBitmap = bitmapDrawable.getBitmap();
                        Bitmap roundedBitmap = BitmapKit.createRoundedBitmap(srcBitmap, 5);
                        ivHomeFreetimeAdv.setImageBitmap(roundedBitmap);
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
                ivHomeFreetimeAdv.setScaleType(ImageView.ScaleType.FIT_XY);
                cardView.addView(ivHomeFreetimeAdv);

                container.addView(cardView);
                return cardView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(vpAdvStartIndex);
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
                        //点击的时候，到banner页面
                        initBanner();
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
            }
        });

    }

    private void initBanner() {
        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), WebViewActivity.class);
        intentCommonQuestionActivity.putExtra("banner","banner");
        mActivity.startActivity(intentCommonQuestionActivity);
    }

    public class HomeVpAdvChange implements Runnable {
        @Override
        public void run() {
            pagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(pagerHomeFreetimeBinding.vpHomeFreetimeAdv.getCurrentItem() + 1);
            CommonUtils.getHandler().postDelayed(this, 2000);
        }
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


    public void gotoSearchActivity(View v) {
        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        intentSearchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSearchActivity);
    }

    //切换为展示需求列条
    public void displayDemanList(View v) {
        mIsDisplayDemandList = true;
        SpUtils.setBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, mIsDisplayDemandList);
        displayDemanList();
    }


    //切换为展示服务列条
    public void displayServiceList(View v) {
        mIsDisplayDemandList = false;
        SpUtils.setBoolean(GlobalConstants.SpConfigKey.HOME_IS_DISPLAY_DEMAND_LIST, mIsDisplayDemandList);
        displayServiceList();
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
        //模拟数据 首页广告条图片URL
        listAdvImageUrl.add("http://pic33.nipic.com/20130916/3420027_192919547000_2.jpg");
        listAdvImageUrl.add("http://b.hiphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=4f04be8ab8014a90853e42bb99470263/b8389b504fc2d562d426d1d5e61190ef76c66cdf.jpg?v=tbs");
        listAdvImageUrl.add("http://pic36.nipic.com/20131128/11748057_141932278338_2.jpg");
        listAdvImageUrl.add("http://img5.imgtn.bdimg.com/it/u=2033765348,1346395611&fm=21&gp=0.jpg");
        listAdvImageUrl.add("http://img1.imgtn.bdimg.com/it/u=1659898221,3810685472&fm=21&gp=0.jpg");
        listAdvImageUrl.add("http://pic44.nipic.com/20140721/11624852_001107119409_2.jpg");
        listAdvImageUrl.add("http://img0.imgtn.bdimg.com/it/u=938096994,3074232342&fm=21&gp=0.jpg");
        listAdvImageUrl.add("http://img1.imgtn.bdimg.com/it/u=1794894692,1423685501&fm=21&gp=0.jpg");

        //FirstPagerManager.onGetFirstPagerAdvertisement(new onGetFirstPagerAdvertisement(),GlobalConstants.HttpUrl.FIRST_PAHER_ADVERTISEMENT_ONE);
        // FirstPagerManager.onGetFirstPagerAdvertisement(new onGetFirstPagerAdvertisement(),GlobalConstants.HttpUrl.FIRST_PAHER_ADVERTISEMENT_THREE);
        //FirstPagerManager.onGetFirstPagerAdvertisement(new onGetFirstPagerAdvertisement(),GlobalConstants.HttpUrl.FIRST_PAHER_ADVERTISEMENT_TWO);
    }

    public void getDemandOrServiceListData() {
        if (mIsDisplayDemandList) {
            listDemandBean.clear();
            FirstPagerManager.onFreeTimeDemandList(new onFreeTimeDemandList(),limit);
        } else {
            listServiceBean.clear();
            FirstPagerManager.onFreeTimeServiceList(new onFreeTimeServiceList(),limit);
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
    public class onGetFirstPagerAdvertisement implements BaseProtocol.IResultExecutor<String> {
        @Override
        public void execute(String data) {

            listAdvImageUrl.add(data);

        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //首页闲时需求
    public class onFreeTimeDemandList implements BaseProtocol.IResultExecutor<FreeTimeDemandBean> {
        @Override
        public void execute(FreeTimeDemandBean data) {
            int rescode = data.getRescode();
            if(rescode == 0){
                FreeTimeDemandBean.DataBean dataBean = data.getData();
                List<FreeTimeDemandBean.DataBean.ListBean> list = dataBean.getList();
                listDemandBean.addAll(list);
                homeDemandAndDemandAdapter = new HomeDemandAdapter(listDemandBean,mActivity);
                pagerHomeFreetimeBinding.lvHomeDemandAndService
                .setAdapter(homeDemandAndDemandAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    private int listsize;
    //首页闲时服务
    public class onFreeTimeServiceList implements BaseProtocol.IResultExecutor<FreeTimeServiceBean> {
        @Override
        public void execute(FreeTimeServiceBean data) {
            int rescode = data.getRescode();
            if(rescode == 0){
                FreeTimeServiceBean.DataBean dataBean = data.getData();
                List<FreeTimeServiceBean.DataBean.ListBean> list = dataBean.getList();
                listsize = list.size();
                listServiceBean.addAll(list);
                homeServiceAdapter = new HomeServiceAdapter(listServiceBean,mActivity);
                pagerHomeFreetimeBinding.lvHomeDemandAndService
                .setAdapter(homeServiceAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //点击添加更多
    public void more(View view){
        clickMore();
    }

    private void clickMore() {
        Intent intentFirstPagerMoreActivity = new Intent(CommonUtils.getContext(), FirstPagerMoreActivity.class);
        intentFirstPagerMoreActivity.putExtra("isDemand",mIsDisplayDemandList);
        intentFirstPagerMoreActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentFirstPagerMoreActivity);
    }

}
