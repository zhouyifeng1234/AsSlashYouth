package com.slash.youth.ui.view.PullableSrcollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.slash.youth.ui.view.PullableListView.Pullable;

public class PullableScrollView extends ScrollView implements Pullable {

    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        /*if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
		else*/
        return false;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }

}
