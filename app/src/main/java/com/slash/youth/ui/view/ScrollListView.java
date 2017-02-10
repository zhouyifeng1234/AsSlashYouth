package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class ScrollListView extends ListView {


    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
