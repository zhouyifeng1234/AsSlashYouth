package com.slash.youth.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/13.
 */
public class RingScoreView extends View {

    private String mRingScoreViewWidth = "";
    private String mRingScoreViewHeight = "";
    private float mWidthPx;
    private float mHeightPx;
    private float mRadius;
    private float x;
    private float y;
    private Paint mOutterCirclePaint;
    private float ringWidth;
    private Paint mInnerCirclePaint;
    private Paint mProgressOutterCirclePaint;
    private Paint mProgressInnerCirclePaint;

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
            //获取布局文件中设置的layout_width和layout_height的数据，结果是字符串类型
            mRingScoreViewWidth = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
            mRingScoreViewHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

            float widthDip = Float.parseFloat(mRingScoreViewWidth.substring(0, mRingScoreViewWidth.length() - 3));
            float heightDip = Float.parseFloat(mRingScoreViewHeight.substring(0, mRingScoreViewHeight.length() - 3));
            mWidthPx = widthDip * CommonUtils.getDensity();
            mHeightPx = heightDip * CommonUtils.getDensity();
            mRadius = mWidthPx / 2;
            x = mWidthPx / 2;
            y = mHeightPx / 2;
            mOutterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mOutterCirclePaint.setColor(0xffafdaf0);
            mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mInnerCirclePaint.setColor(0xffffffff);
            mProgressOutterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mProgressOutterCirclePaint.setColor(0xff3cc7e3);
            mProgressInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mProgressInnerCirclePaint.setColor(0xffffffff);
            ringWidth = CommonUtils.dip2px(11);

//            post(new Runnable() {
//                @Override
//                public void run() {
//                    initRingProgressDraw();
//                }
//            });

        }
    }

    public void initRingProgressDraw() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 120; i++) {
                    try {
                        if (i != 119) {
                            progressSweepAngle += (totalProgressAngle / 120);
                        } else {
                            progressSweepAngle = totalProgressAngle;
                        }
                        long startMill = System.currentTimeMillis();
                        postInvalidate();
                        long endMill = System.currentTimeMillis();
                        if (endMill - startMill <= 16) {
                            Thread.sleep(16 - (endMill - startMill));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    float totalProgressAngle = 360;
    float progressSweepAngle = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(x, y, mRadius, mOutterCirclePaint);
        canvas.drawArc(new RectF(0, 0, mWidthPx, mHeightPx), -90, progressSweepAngle, true, mProgressOutterCirclePaint);
//        canvas.drawArc(new RectF(0 + ringWidth, 0 + ringWidth, mWidthPx - ringWidth, mHeightPx - ringWidth), -90, (progressSweepAngle + 2) <= 360 ? progressSweepAngle + 2 : 360, true, mProgressInnerCirclePaint);
        canvas.drawCircle(x, y, mRadius - ringWidth, mInnerCirclePaint);
    }

    public void setTotalProgressAngle(float totalProgressAngle) {
        this.totalProgressAngle = totalProgressAngle;
    }

    public void setStartProgressAngle(float startProgressAngle) {
        this.progressSweepAngle = startProgressAngle;
    }
}
