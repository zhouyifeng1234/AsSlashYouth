package com.slash.youth.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/10/13.
 */
public class RingScoreView extends View {

    private String mRingScoreViewWidth = "";
    private String mRingScoreViewHeight = "";

    public RingScoreView(Context context) {
        super(context);
        initView(null);
    }

    public RingScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public RingScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
//            ToastUtils.shortToast("attr");
            mRingScoreViewWidth = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
            mRingScoreViewHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
            LogKit.v("mRingScoreViewWidth:" + mRingScoreViewWidth);
            LogKit.v("mRingScoreViewHeight:" + mRingScoreViewHeight);
//            int intWidth =  mRingScoreViewWidth.substring(0, mRingScoreViewWidth.length() - 4);

//
//            LogKit.v("mRingScoreViewWidth:" + attrs.getAttributeValue(0));
//            LogKit.v("mRingScoreViewWidth:" + attrs.getAttributeValue(1));
//            LogKit.v("mRingScoreViewHeight:" + mRingScoreViewHeight);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int mRingScoreViewWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int mRingScoreViewHeight = MeasureSpec.getSize(heightMeasureSpec);
//        LogKit.v("mRingScoreViewWidth:" + mRingScoreViewWidth);
//        LogKit.v("mRingScoreViewHeight:" + mRingScoreViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {


    }
}
