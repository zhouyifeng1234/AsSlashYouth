package com.slash.youth.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhouyifeng on 2017/2/26.
 */
public class SlashViewPager extends ViewPager {

    public SlashViewPager(Context context) {
        super(context);
    }

    public SlashViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float startX = -1;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX != -1) {
                    float endX = ev.getRawX();
                    float dX = Math.abs(endX - startX);
                    if (dX > 0) {
                        isMoveViewPager = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                startX = -1;
                isMoveViewPager = false;
                break;
        }
        if (isMoveViewPager) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    boolean isMoveViewPager = false;

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startX = ev.getRawX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (startX != -1) {
//                    float endX = ev.getRawX();
//                    float dX = Math.abs(endX - startX);
//                    if (dX > 0) {
//                        isMoveViewPager = true;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                startX = -1;
//                isMoveViewPager = false;
//                break;
//        }
//        if (isMoveViewPager) {
//            return true;
//        } else {
//            return super.onTouchEvent(ev);
//        }
//    }
}
