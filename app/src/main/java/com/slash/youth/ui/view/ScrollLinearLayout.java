package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zhouyifeng on 2016/10/7.
 */
public class ScrollLinearLayout extends LinearLayout {

    public ScrollLinearLayout(Context context) {
        super(context);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int moveUpDistance;

    public void setMoveUpDistance(int moveUpDistance) {
        this.moveUpDistance = moveUpDistance;
    }

//    private ListView innerListView;
//
//    public void setInnerListView(ListView innerListView) {
//        this.innerListView = innerListView;
//    }

    int startY = -1;
    int marginTop = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogKit.v("onInterceptTouchEvent:" + marginTop);
        if (scrollTop(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogKit.v("onTouchEvent:" + marginTop);
        if (scrollTop(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean scrollTop(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
//                if (innerListView.getFirstVisiblePosition() > 0) {
//                    requestDisallowInterceptTouchEvent(true);
//                }
                if (startY == -1) {
                    startY = (int) ev.getRawY();
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;
//                LogKit.v("dy:" + dy);
                marginTop += dy;
                if (marginTop <= 0 && marginTop >= (-moveUpDistance)) {
                    setMarginTop();
                    startY = endY;
                    return true;
                } else if (marginTop > 0) {
                    marginTop = 0;
                } else if (marginTop < (-moveUpDistance)) {
                    marginTop = (-moveUpDistance);
                }
                setMarginTop();
                startY = endY;
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                break;
        }
        return false;
    }

    public void setMarginTop() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.topMargin = marginTop;
        setLayoutParams(params);
    }
}
