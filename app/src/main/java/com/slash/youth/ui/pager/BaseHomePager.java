package com.slash.youth.ui.pager;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeBaseBinding;
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

    public BaseHomePager() {
        rootView = initView();
        initListener();
        setViewContent();
    }

    public View initView() {
        mPagerHomeBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_base, null, false);
        mPagerHomeBaseBinding.vpPagerHomeBaseAdvertisement.setAdapter(new HomeAdvertisementAdapter());

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
    }

    abstract public void setViewContent();

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

}
