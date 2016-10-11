package com.slash.youth.ui.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/10/10.
 */
public class ScaleViewPagerTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_scale_viewpager);
        final ViewPager vpTest = (ViewPager) findViewById(R.id.vp_test);
        vpTest.setOffscreenPageLimit(3);
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.ll_root);
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vpTest.dispatchTouchEvent(event);
            }
        });

        vpTest.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ViewPager.LayoutParams imgBoxParams = new ViewPager.LayoutParams();
                FrameLayout flImagBox = new FrameLayout(CommonUtils.getContext());
                flImagBox.setPadding(CommonUtils.dip2px(6), 0, CommonUtils.dip2px(6), 0);
                flImagBox.setLayoutParams(imgBoxParams);
                FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(-1, -1);
                ImageView ivImg = new ImageView(CommonUtils.getContext());
                ivImg.setImageResource(R.mipmap.banner);
                ivImg.setLayoutParams(imgParams);
                flImagBox.addView(ivImg);
                container.addView(flImagBox);
                return flImagBox;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView((View) object);
            }
        });

        vpTest.setCurrentItem(50);

        vpTest.setPageTransformer(false, new ViewPager.PageTransformer() {
            private float defaultScale = (float) 4 / (float) 5;


            @Override
            public void transformPage(View page, float position) {
                LogKit.v(position + "");
                FrameLayout imgBox = (FrameLayout) page;
                ImageView img = (ImageView) imgBox.getChildAt(0);

//                int diffWidth = (imgBox.getWidth() - img.getWidth()) / 2;

                if (position < -1) { // [-Infinity,-1)
                    imgBox.setScaleX(defaultScale);
                    imgBox.setScaleY(defaultScale);
//                    img.setTranslationX(diffWidth);

                } else if (position <= 0) { // [-1,0]
                    imgBox.setScaleX((float) 1 + position / (float) 5);
                    imgBox.setScaleY((float) 1 + position / (float) 5);
//                    img.setTranslationX((0 - position) * diffWidth);

                } else if (position <= 1) { // (0,1]
                    imgBox.setScaleX((float) 1 - position / (float) 5);
                    imgBox.setScaleY((float) 1 - position / (float) 5);
//                    img.setTranslationX((0 - position) * diffWidth);

                } else { // (1,+Infinity]
                    imgBox.setScaleX(defaultScale);
                    imgBox.setScaleY(defaultScale);
//                    img.setTranslationX(-diffWidth);
                }
            }
        });
    }
}
