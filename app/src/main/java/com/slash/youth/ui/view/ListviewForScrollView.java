package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.slash.youth.R;

/**
 * Created by zss on 2016/11/1.
 */
public class ListviewForScrollView extends ListView {


    public ListviewForScrollView(Context context) {
        super(context);
    }

    public ListviewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       // initView();
    }

    private void initView() {
        View footView = View.inflate(getContext(), R.layout.footer_listview_addmore, null);
        this.addFooterView(footView);
        footView.measure(0,0);
       int  footerHeight = footView.getMeasuredHeight();
        footView.setPadding(0, -footerHeight, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
