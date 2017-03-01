package com.slash.youth.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouyifeng on 2017/1/6.
 */
public class GuidActivity extends BaseActivity {

    private ViewPager mVpGuidPagers;
    private int[] guidPicResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guid);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mVpGuidPagers = (ViewPager) findViewById(R.id.vp_guid_pagers);
    }

    private void initData() {
        guidPicResources = new int[]{R.mipmap.guid_pic_1, R.mipmap.guid_pic_2, R.mipmap.guid_pic_3, R.mipmap.guid_pic_4};
        mVpGuidPagers.setAdapter(new GuidPagerAdapter());
    }

    private void initListener() {
        mVpGuidPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {//第二个引导页
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.CLICK_PAGE_TWO);
                    LogKit.v("Guid Page two");
                } else if (position == 2) {//第三个引导页
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.CLICK_PAGE_THREE);
                    LogKit.v("Guid Page three");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class GuidPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guidPicResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout flBox = new FrameLayout(CommonUtils.getContext());

            FrameLayout.LayoutParams flParamsImg = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            int guidImgResource = guidPicResources[position];
            ImageView ivGuidImg = new ImageView(CommonUtils.getContext());
            ivGuidImg.setScaleType(ImageView.ScaleType.FIT_XY);
            ivGuidImg.setImageResource(guidImgResource);
            ivGuidImg.setLayoutParams(flParamsImg);
            flBox.addView(ivGuidImg);
            if (position == getCount() - 1) {
                FrameLayout.LayoutParams flParamsTv = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                TextView tvExperience = new TextView(CommonUtils.getContext());
                flParamsTv.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                flParamsTv.bottomMargin = CommonUtils.dip2px(50);
                tvExperience.setText("立即体验");
                tvExperience.setTextColor(0xffffffff);
                tvExperience.setTextSize(14);
                tvExperience.setGravity(Gravity.CENTER);
                tvExperience.setBackgroundResource(R.mipmap.experience_btn_bg);
                tvExperience.setLayoutParams(flParamsTv);
                tvExperience.setOnClickListener(new GotoLoginClickListener());
                flBox.addView(tvExperience);
            }
            container.addView(flBox);
            return flBox;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class GotoLoginClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intentLoginActivity = new Intent(CommonUtils.getContext(), LoginActivity.class);
            startActivity(intentLoginActivity);
            finish();
        }
    }
}
