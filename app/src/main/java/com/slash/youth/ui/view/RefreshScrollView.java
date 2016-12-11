package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/12/11.
 */
public class RefreshScrollView extends ScrollView {

    public RefreshScrollView(Context context) {
        super(context);
//        this.context = context;
//        initView();
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.context = context;
//        initView();
    }

    public RefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.context = context;
//        initView();
    }

//    private Context context;
//    private View refreshHeader;
//    private int refreshHeaderHeight = 0;

//    public void initView() {
//        refreshHeader = View.inflate(context, R.layout.header_listview_refresh,
//                null);
//        refreshHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                refreshHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                refreshHeaderHeight = refreshHeader.getHeight();
//                LogKit.v("refreshHeaderHeight:" + refreshHeaderHeight);
//            }
//        });
//    }

    private int refreshPullHeight = CommonUtils.dip2px(100);
    private int currentScrollY = 0;

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//        LogKit.v("----------ScrollView overScrollBy-------- ");
        LogKit.v("scrollY:" + scrollY + " deltaY:" + deltaY + " scrollRangeY:" + scrollRangeY + "  maxOverScrollY:" + maxOverScrollY);
        currentScrollY = scrollY;
//        currentScrollY += deltaY;
        LogKit.v("isTouchEvent:" + isTouchEvent);
        if (scrollY <= 0 && isTouchEvent) {
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, refreshPullHeight, isTouchEvent);
        } else {
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (currentScrollY <= -(refreshPullHeight / 2)) {
                    if (mRefreshTask != null) {
                        mRefreshTask.refresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    IRefreshTask mRefreshTask;

    public void setRefreshTask(IRefreshTask refreshTask) {
        this.mRefreshTask = refreshTask;
    }

    public interface IRefreshTask {
        public void refresh();
    }
}
