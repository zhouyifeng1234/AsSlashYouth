package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.slash.youth.R.id.iv_home_freetime_service;
import static com.slash.youth.R.id.view;

/**
 * Created by acer on 2017/2/28.
 */

public class AlphaViewScrollView extends ScrollView {

    public static final String TAG = "AlphaTitleScrollView";
    private int mSlop;
    private View[] beforView;

    private View[] afterView;
    private View hightView;
    private View afterV;
    private View beforV;


    private TextView beforTitle;
    private TextView afterTitle;
    private View view;

    public AlphaViewScrollView(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AlphaViewScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaViewScrollView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        mSlop = 10;
    }

    public void setHightView(View beforView, View afterView, View hight) {
        this.hightView = hight;
        this.afterV = afterView;
        this.beforV = beforView;
        afterV.setAlpha(0);
        beforV.setAlpha(1f);
    }

    public void setTitleTextView(TextView beforTitle, TextView afterTitle) {
        this.beforTitle = beforTitle;
        this.afterTitle = afterTitle;
    }


    /**
     * @param beforView 需要变换的view
     */
    public void setBeforView(View... beforView) {
        this.beforView = beforView;
    }

    public void setAfterView(View... afterView) {
        this.afterView = afterView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        float headHeight = hightView.getMeasuredHeight() * 3 / 4
                - beforV.getMeasuredHeight();
        int alpha = (int) (((float) t / headHeight) * 255);
        if (alpha >= 155)
            alpha = 200;
        if (alpha <= mSlop)
            alpha = 0;


        beforV.setAlpha((255 - alpha) / 255f);
        afterV.setAlpha(alpha / 255f);
//        view.getBackground().setAlpha(alpha);
//        if (beforView != null)
//            for (View v : beforView) {
//                v.getBackground().setAlpha(255 - alpha);
//                beforTitle.setAlpha((255 - alpha) / 255f);
//            }
//        if (afterView != null)
//            for (View v : afterView) {
//                v.getBackground().setAlpha(alpha);
//                afterTitle.setAlpha(alpha / 255f);
//            }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
