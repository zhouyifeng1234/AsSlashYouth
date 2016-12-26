package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.slash.youth.R;
import com.slash.youth.ui.view.PullableListView.Pullable;

/**
 * Created by zss on 2016/11/1.
 */
public class ListviewForScrollView extends ListView  {


    public ListviewForScrollView(Context context) {
        super(context);
    }

    public ListviewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
