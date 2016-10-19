package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.BR;
import com.slash.youth.databinding.PagerHomeFreetimeBinding;
import com.slash.youth.domain.HomeDemandAndServiceBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.HomeDemandAndServiceAdapter;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeFreeTimeModel extends BaseObservable {

    public Activity mActivity;
    PagerHomeFreetimeBinding mPagerHomeFreetimeBinding;
    private boolean mIsDisplayDemandList = true;//如果存true，表示展示需求列表，false为展示服务列表,默认为true

    public PagerHomeFreeTimeModel(PagerHomeFreetimeBinding pagerHomeFreetimeBinding, Activity activity) {
        this.mActivity = activity;
        this.mPagerHomeFreetimeBinding = pagerHomeFreetimeBinding;
        initView();
        initData();
        initListener();
    }

    ArrayList<HomeDemandAndServiceBean> listDemandServiceBean = new ArrayList<HomeDemandAndServiceBean>();
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
        mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.setOffscreenPageLimit(3);
        mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.setAdapter(new PagerAdapter() {
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
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) result;
                        Bitmap srcBitmap = bitmapDrawable.getBitmap();
                        Bitmap roundedBitmap = BitmapKit.createRoundedBitmap(srcBitmap, 5);
                        ivHomeFreetimeAdv.setImageBitmap(roundedBitmap);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

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
        mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(vpAdvStartIndex);
        mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.setPageTransformer(false, new ViewPager.PageTransformer() {

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
        mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.setOnTouchListener(new View.OnTouchListener() {
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
    }

    public class HomeVpAdvChange implements Runnable {

        @Override
        public void run() {
            mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.setCurrentItem(mPagerHomeFreetimeBinding.vpHomeFreetimeAdv.getCurrentItem() + 1);
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

    public void displayDemanList() {
        setDemandButtonVisibility(View.VISIBLE);
        setServiceButtonVisibility(View.INVISIBLE);
        displayDemandOrServiceList();
    }

    public void displayServiceList() {
        setDemandButtonVisibility(View.INVISIBLE);
        setServiceButtonVisibility(View.VISIBLE);
        displayDemandOrServiceList();
    }

    public void displayDemandOrServiceList() {
        getDemandOrServiceListData();
        mPagerHomeFreetimeBinding.lvHomeDemandAndService.setAdapter(new HomeDemandAndServiceAdapter(listDemandServiceBean));
    }

    public void getDataFromServer() {
        //模拟数据 首页闲时 需求、服务列表
//        getDemandOrServiceListData();

        //模拟数据 首页广告条图片URL
        listAdvImageUrl.add("http://pic33.nipic.com/20130916/3420027_192919547000_2.jpg");
        listAdvImageUrl.add("http://b.hiphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=4f04be8ab8014a90853e42bb99470263/b8389b504fc2d562d426d1d5e61190ef76c66cdf.jpg?v=tbs");
        listAdvImageUrl.add("http://pic36.nipic.com/20131128/11748057_141932278338_2.jpg");
        listAdvImageUrl.add("http://img5.imgtn.bdimg.com/it/u=2033765348,1346395611&fm=21&gp=0.jpg");
        listAdvImageUrl.add("http://img1.imgtn.bdimg.com/it/u=1659898221,3810685472&fm=21&gp=0.jpg");
        listAdvImageUrl.add("http://pic44.nipic.com/20140721/11624852_001107119409_2.jpg");
        listAdvImageUrl.add("http://img0.imgtn.bdimg.com/it/u=938096994,3074232342&fm=21&gp=0.jpg");
        listAdvImageUrl.add("http://img1.imgtn.bdimg.com/it/u=1794894692,1423685501&fm=21&gp=0.jpg");
    }

    public void getDemandOrServiceListData() {
        //模拟数据 首页闲时 需求、服务列表
        listDemandServiceBean.clear();

        if (mIsDisplayDemandList) {
            //加载需求列表数据
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(true));
        } else {
            //加载服务列表数据
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
            listDemandServiceBean.add(new HomeDemandAndServiceBean(false));
        }
    }
}
